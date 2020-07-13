package songbox.house.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import songbox.house.domain.entity.DiscogsReleaseEntity;
import songbox.house.repository.DiscogsReleaseRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
@Transactional
public class DiscogsReleasesServiceImpl implements DiscogsReleasesService {

    DiscogsReleaseRepository discogsReleaseRepository;
    private static final AtomicLong COUNT_TOTAL = new AtomicLong();

    @Override
    public void addBatch(List<DiscogsReleaseEntity> releases) {
        log.info("Saving batch of {} elements, total added {}", releases.size(), COUNT_TOTAL.addAndGet(releases.size()));
        discogsReleaseRepository.saveAll(releases);
    }

    @Override
    public long getTotal() {
        return discogsReleaseRepository.count();
    }
}
