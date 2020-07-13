package songbox.house.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Slf4j
public class DiscogsDtoProducer<DiscogsDTO> {

    private final BlockingQueue<DiscogsDTO> queue;
    private final Predicate<DiscogsDTO> filterPredicate;
    private final BlockingQueue<DiscogsDTO> buffer = new LinkedBlockingQueue<>();

    private static final AtomicLong TOTAL_READ = new AtomicLong();
    private static final AtomicLong TOTAL_ADDED = new AtomicLong();

    private final Integer maxSize;

    //for save
    private final int batchSize = 1000;
    private final int producerSleepMs = 60000;

    //for print
//    private final int batchSize = 10000;
//    private final int producerSleepMs = 3000;

    public DiscogsDtoProducer(BlockingQueue<DiscogsDTO> queue, Predicate<DiscogsDTO> filterPredicate, int maxSize) {
        this.queue = queue;
        this.filterPredicate = filterPredicate;
        this.maxSize = maxSize;
    }

    public void add(DiscogsDTO item) {
        if (filterPredicate.test(item)) {
            buffer.add(item);
            TOTAL_READ.incrementAndGet();
            if (buffer.size() >= batchSize) {
                List<DiscogsDTO> batch = new ArrayList<>();
                buffer.drainTo(batch, batchSize);
                queue.addAll(batch);
                log.info("Buffer {}, queue {}, total read {}, total added {}", buffer.size(), queue.size(),
                        TOTAL_READ.get(), TOTAL_ADDED.addAndGet(batchSize));
                while (queue.size() > maxSize) {
                    try {
                        log.info("Producer sleeping for {}ms", producerSleepMs);
                        Thread.sleep(producerSleepMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void flush() {
        if (!buffer.isEmpty()) {
            queue.addAll(buffer);
            buffer.clear();
        }
    }
}
