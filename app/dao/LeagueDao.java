package dao;

import java.util.List;
import java.util.Optional;

public interface LeagueDao<T> {

    Optional<T> find(String s);
    List<T> listAll();
    void save(String s, T t);
    void delete(String s);

}
