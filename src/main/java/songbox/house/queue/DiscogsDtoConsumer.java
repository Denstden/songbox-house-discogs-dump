package songbox.house.queue;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import songbox.house.util.RetryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class DiscogsDtoConsumer<DiscogsDTO, Entity> implements Runnable {

    private static final AtomicBoolean PRODUCER_FINISHED = new AtomicBoolean(false);

    BlockingQueue<DiscogsDTO> queue;
    Function<DiscogsDTO, Entity> converter;
    Consumer<List<Entity>> processor;
    Integer batchSize;
    Integer pollTimeoutMs;
    Integer pollIntervalMs;
    Integer sleepMs;

    public DiscogsDtoConsumer(BlockingQueue<DiscogsDTO> queue, Function<DiscogsDTO, Entity> converter,
            Consumer<List<Entity>> processor,
            Integer batchSize, Integer pollTimeoutMs, Integer pollIntervalMs,
            Integer sleepMs) {
        this.queue = queue;
        this.converter = converter;
        this.processor = processor;
        this.batchSize = batchSize;
        this.pollTimeoutMs = pollTimeoutMs;
        this.pollIntervalMs = pollIntervalMs;
        this.sleepMs = sleepMs;
    }

    @Override
    public void run() {
        try {
            List<DiscogsDTO> batch;
            do {
                batch = getBatch(batchSize, pollTimeoutMs, pollIntervalMs);
                if (batch != null && !batch.isEmpty()) {
                    log.debug("Processing batch of {} elements", batch.size());
                    List<Entity> entities = batch.stream()
                            .map(converter)
                            .collect(toList());
                    long startedAt = currentTimeMillis();
                    RetryUtil.executeWithRetryOnException(processor, entities);
                    log.info("Processed batch of {} items for {}ms", entities.size(), currentTimeMillis() - startedAt);
                } else {
                    Thread.sleep(sleepMs);
                    log.info("Consumer sleeping {}ms", sleepMs);
                }
            } while (!isEmpty(batch) || !PRODUCER_FINISHED.get());
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    public void setProducerFinished() {
        PRODUCER_FINISHED.set(true);
    }

    private List<DiscogsDTO> getBatch(int count, int timeoutMs, int pollTimeoutMs) throws InterruptedException {
        int i = 0;
        List<DiscogsDTO> batch = new ArrayList<>();
        long startedMs = currentTimeMillis();
        while (!queue.isEmpty() && i < count) {
            final DiscogsDTO item = queue.poll(pollTimeoutMs, MILLISECONDS);
            ofNullable(item).map(batch::add);
            i++;
            if (item == null) {
                if (currentTimeMillis() - startedMs < timeoutMs + pollTimeoutMs) {
                    log.info("Sleeping {}", pollTimeoutMs);
                    Thread.sleep(pollTimeoutMs);
                } else {
                    break;
                }
            }
        }
        return batch;
    }

}
