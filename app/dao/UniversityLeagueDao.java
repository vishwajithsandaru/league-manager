package dao;

import domains.League;
import models.FootballClub;
import models.SchoolFootballClub;
import models.UniversityFootballClub;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.*;

@Singleton
public class UniversityLeagueDao implements LeagueDao<League<UniversityFootballClub>>, Serializable {

    Map<String, League<UniversityFootballClub>> leagues = new HashMap<String, League<UniversityFootballClub>>();

    @Override
    public Optional<League<UniversityFootballClub>> find(String s){

        League<UniversityFootballClub> league = null;

        try{
            league = leagues.get(s);
        }catch (Exception e){
            league = null;
        }

        return Optional.ofNullable(league);

    }

    @Override
    public List<League<UniversityFootballClub>> listAll() {

        List<League<UniversityFootballClub>> leagueList = new ArrayList<>(leagues.values());

        if(leagueList.isEmpty()){

        }

        return leagueList;
    }

    @Override
    public void save(String s, League<UniversityFootballClub> universityFootballClubLeague){
        leagues.put(s, universityFootballClubLeague);
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

    public Map<String, League<UniversityFootballClub>> getDataSet(){
        return leagues;
    }

}
