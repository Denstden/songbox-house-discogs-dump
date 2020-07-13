package songbox.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import songbox.house.domain.entity.DiscogsReleaseEntity;

@Repository
public interface DiscogsReleaseRepository extends JpaRepository<DiscogsReleaseEntity, Integer> {
}
