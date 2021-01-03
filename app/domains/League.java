package domains;

import models.FootballClub;
import models.SchoolFootballClub;
import models.UniversityFootballClub;
import models.UniversityLeague;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class League<T> implements Serializable {

    private String season;
    private Map<String, T> clubs = new HashMap<String, T>();
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
}
