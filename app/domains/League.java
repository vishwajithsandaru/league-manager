package domains;

import models.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class League<T> implements Serializable {

    private String season;
    private Map<String, T> clubs = new HashMap<String, T>();
    private List<Match> matches = new ArrayList<Match>();
    private Class<T> cls;
    private String type;


    public League(){
        if(cls == FootballClub.class){
            type="premier";
        }
        if(cls == UniversityFootballClub.class){
            type="university";
        }
        if(cls == SchoolFootballClub.class){
            type="school";
        }
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public void setClubs(Map<String, T> clubs) {
        this.clubs = clubs;
    }

    public Map<String, T> getClubs() {
        return clubs;
    }

    public Class<T> getCls() {
        return cls;
    }

    public void addMatch(Match match){
        matches.add(match);
    }

    public List<Match> getMatches(){
        return matches;
    }

    public Match getMatchByDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Match match : matches){
            LocalDate dateTime = match.getDateTime().toLocalDate();
            String dt = dateTime.format(formatter);

            if(date.equals(dt)){
                return match;
            }

        }
        return null;
    }

}
