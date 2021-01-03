package dao;

import domains.League;
import models.FootballClub;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LeagueDao<T> {

    Optional<T> find(String s);
    List<T> listAll();
    void save(String s, T t);
    void delete(String s);
    void clean();

}
