package dao;

import domains.League;
import models.FootballClub;
import models.SchoolFootballClub;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.*;

@Singleton
public class SchoolLeagueDao implements LeagueDao<League<SchoolFootballClub>>, Serializable {

    Map<String, League<SchoolFootballClub>> leagues = new HashMap<String, League<SchoolFootballClub>>();


    @Override
    public Optional<League<SchoolFootballClub>> find(String s){

        League<SchoolFootballClub> league = null;

        try{
            league = leagues.get(s);
        }catch (Exception e){
            league = null;
        }

        return Optional.ofNullable(league);

    }

    @Override
    public List<League<SchoolFootballClub>> listAll() {

        List<League<SchoolFootballClub>> leagueList = new ArrayList<>(leagues.values());

        if(leagueList.isEmpty()){

        }

        return leagueList;
    }

    @Override
    public void save(String s, League<SchoolFootballClub> schoolFootballClubLeague){
        leagues.put(s, schoolFootballClubLeague);
    }

    @Override
    public void delete(String s) {

        try{
            leagues.remove(s.toLowerCase());
        }catch (Exception e){

        }

    }

    @Override
    public void clean(){
        leagues.clear();
    }

    public Map<String, League<SchoolFootballClub>> getDataSet(){
        return leagues;
    }

}
