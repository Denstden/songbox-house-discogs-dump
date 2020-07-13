package songbox.house.service.dump;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.util.CollectionUtils.isEmpty;
import static songbox.house.service.dump.DiscogsDumpReader.readReleases;

@Slf4j
@Service
public class DiscogsDumpIterateService implements DiscogsDumpProcessingService {
    @Override
    public boolean processDump(LocalDate date) {
        AtomicLong total = new AtomicLong();
        AtomicLong electronic = new AtomicLong();
        return readReleases(date, release -> {
            if (!isEmpty(release.getGenres()) && release.getGenres().contains("Electronic")) {
                electronic.incrementAndGet();
            }
            if (total.incrementAndGet() % 1000 == 0) {
                log.info("Read {} items, electronic {}", total.get(), electronic.get());
            }
        });
    }
}
