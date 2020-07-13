package songbox.house.service;

import java.util.List;

public interface DiscogsEntityService<T> {
    void addBatch(List<T> releases);

    long getTotal();
}
