package songbox.house.service.dump;

import java.time.LocalDate;

public interface DiscogsDumpProcessingService {
    boolean processDump(LocalDate date);
}
