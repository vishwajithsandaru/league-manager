package dao;

import domains.League;
import models.FootballClub;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.*;

@Singleton
public class PremierLeagueDao implements LeagueDao<League<FootballClub>>, Serializable {

    Map<String, League<FootballClub>> leagues = new HashMap<String, League<FootballClub>>();


    @Override
    public Optional<League<FootballClub>> find(String s){

        League<FootballClub> league = null;

        try{
            league = leagues.get(s);
        }catch (Exception e){
            league = null;
        }

        return Optional.ofNullable(league);

    }

    @Override
    public List<League<FootballClub>> listAll() {

        List<League<FootballClub>> leagueList = new ArrayList<>(leagues.values());

        if(leagueList.isEmpty()){

        }

        return leagueList;
    }

    @Override
    public void save(String s, League<FootballClub> footballClubLeague){
        leagues.put(s, footballClubLeague);
    }

    @Override
    public void delete(String s) {

        try{
            leagues.remove(s.toLowerCase());
        }catch (Exception e){

        }

    }
}
