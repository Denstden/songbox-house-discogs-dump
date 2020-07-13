package songbox.house.service.dump;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import songbox.house.converter.DiscogsReleaseConverter;
import songbox.house.domain.entity.DiscogsReleaseEntity;
import songbox.house.queue.DiscogsDtoConsumer;
import songbox.house.queue.DiscogsDtoProducer;
import songbox.house.service.DiscogsReleasesService;
import tslic.discogs.dump.models.Release;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;
import static songbox.house.service.dump.DiscogsDumpReader.readReleases;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class DiscogsDumpImportToDatabaseService implements DiscogsDumpProcessingService {
    private static final DiscogsReleaseConverter RELEASE_CONVERTER = new DiscogsReleaseConverter();

    DiscogsReleasesService discogsReleasesService;
    Integer batchSize;
    Integer pollTimeoutMs;
    Integer pollIntervalMs;
    Integer sleepMs;
    Integer maxQueueSize;

    @Autowired
    public DiscogsDumpImportToDatabaseService(DiscogsReleasesService discogsReleasesService,
            @Value("${songbox.house.discogs.dump.consumer.batch.size:10}") Integer batchSize,
            @Value("${songbox.house.discogs.dump.consumer.poll.timeout.ms:1000}") Integer pollTimeoutMs,
            @Value("${songbox.house.discogs.dump.consumer.poll.interval.ms:10}") Integer pollIntervalMs,
            @Value("${songbox.house.discogs.dump.consumer.sleep.ms:3000}") Integer sleepMs,
            @Value("${songbox.house.discogs.dump.queue.max.size:100000}") Integer maxSize) {
        this.discogsReleasesService = discogsReleasesService;
        this.batchSize = batchSize;
        this.pollTimeoutMs = pollTimeoutMs;
        this.pollIntervalMs = pollIntervalMs;
        this.sleepMs = sleepMs;
        this.maxQueueSize = maxSize;
    }

    @Override
    public boolean processDump(LocalDate date) {
        return importReleases(date);
        //TODO importMasters, importLabels, importArtists
    }

    private boolean importReleases(LocalDate date) {
        log.info("Importing releases for {}. Exists in the DB: {} releases", date, discogsReleasesService.getTotal());
        Function<Release, DiscogsReleaseEntity> convert = RELEASE_CONVERTER::convert;
        Consumer<List<DiscogsReleaseEntity>> batchConsumer = discogsReleasesService::addBatch;
        Function<Consumer<Release>, Boolean> readFunction =
                (releaseConsumer -> readReleases(date, releaseConsumer));
        boolean result = processItemsBatched(convert,
                batchConsumer,
                readFunction,
                //TODO remove filter
                release -> !CollectionUtils.isEmpty(release.getGenres()) && release.getGenres().contains("Electronic"),
                //TODO allow parallel run(fix DEADLOCK)
                1);
        log.info("Finished");
        return result;
    }

    private <T, E> boolean processItemsBatched(Function<T, E> converter, Consumer<List<E>> processor,
            Function<Consumer<T>, Boolean> readFunction, Predicate<T> filterPredicate, int threads) {

        BlockingQueue<T> queue = new LinkedBlockingQueue<>();
        DiscogsDtoProducer<T> producer = new DiscogsDtoProducer<>(queue, filterPredicate, maxQueueSize);
        DiscogsDtoConsumer<T, E> consumer = new DiscogsDtoConsumer<>(queue, converter, processor,
                batchSize, pollTimeoutMs, pollIntervalMs, sleepMs);

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        Stream.iterate(0, i -> ++i)
                .limit(threads)
                .forEach(i -> executorService.submit(consumer));

        final Boolean result = readFunction.apply(producer::add);
        producer.flush();
        consumer.setProducerFinished();

        try {
            executorService.awaitTermination(100, TimeUnit.HOURS);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
