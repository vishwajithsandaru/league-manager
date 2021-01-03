package services;

import exceptions.CommonServiceException;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonService {

    @Inject
    PremierLeagueManager premierLeagueManager;

    @Inject
    SchoolLeagueManager schoolLeagueManager;

    @Inject
    UniversityLeagueManager universityLeagueManager;

    public Set<String> listSeasons() throws CommonServiceException{

        try{

            Set<String> pmlSet = new HashSet<>();
            Set<String> uniSet = new HashSet<>();
            Set<String> sclSet = new HashSet<>();

            List<String> pml = premierLeagueManager.listSeasons();
            List<String> uni = universityLeagueManager.listSeasons();
            List<String> scl = schoolLeagueManager.listSeasons();

            for(String str : pml){
                pmlSet.add(str);
            }

            for(String str : uni){
                uniSet.add(str);
            }

            for(String str : scl){
                sclSet.add(str);
            }

            pmlSet.retainAll(uniSet);
            pmlSet.retainAll(sclSet);

            return pmlSet;

        }catch (Exception e){
            throw new CommonServiceException("No Seasons Available: " + e.getMessage());
        }

    }

}
