package services;

import dao.UniversityLeagueDao;
import domains.League;
import exceptions.UniversityLeagueException;
import models.*;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.LeagueTableResponse;
import responses.UniversityStatsResponse;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UniversityLeagueManager implements LeagueManager, Serializable {

    @Inject
    UniversityLeagueDao universityLeagueDao;

    @Override
    public void createSeason(String season) throws UniversityLeagueException {

        League<UniversityFootballClub> universityLeague = new League<UniversityFootballClub>();

        League<UniversityFootballClub> league = universityLeagueDao.find(season).orElse(null);

        if(league == null){
            universityLeague.setSeason(season);
            universityLeagueDao.save(season, universityLeague);
        }else{
            throw new UniversityLeagueException("Season Already Exists");
        }

    }

    @Override
    public List<String> listSeasons(){

        List<String> response = new ArrayList<String>();

        universityLeagueDao.listAll().forEach((e)->{
            response.add(e.getSeason());
        });

        return response;

    }

    @Override
    public void createAndAddFootballClub(String season, AddFootballClubRequest addFootballClubRequest) throws UniversityLeagueException{

        String name = addFootballClubRequest.getName();

        if(name == null) throw new UniversityLeagueException("Name Cannot Be Empty");
        if(name.isBlank() || name.isEmpty()) throw new UniversityLeagueException("Name Cannot Be Empty");

        String university = addFootballClubRequest.getUniversity();
        String nickname = addFootballClubRequest.getNickname();
        String crestUrl = addFootballClubRequest.getCrestUrl();
        String manager = addFootballClubRequest.getManager();
        String coach = addFootballClubRequest.getCoach();
        String website = addFootballClubRequest.getWebsite();
        String adln1 = addFootballClubRequest.getAddressLine1();
        String adln2 = addFootballClubRequest.getAddressLine2();
        String adln3 = addFootballClubRequest.getAddressLine3();
        String district = addFootballClubRequest.getAddressLine3();
        String state = addFootballClubRequest.getState();
        String country = addFootballClubRequest.getCountry();
        String postalCode = addFootballClubRequest.getPostalCode();
        String color1 = addFootballClubRequest.getColor1();
        String color2 = addFootballClubRequest.getColor2();


        ArrayList<String> lanes = new ArrayList<String>();
        lanes.add(adln1);
        lanes.add(adln2);
        lanes.add(adln3);

        Location location = new Location(lanes, district, state);
        location.setPostalCode(postalCode);

        Color color = new Color();
        color.setColor1(color1);
        color.setColor2(color2);

        UniversityFootballClub universityFootballClub = new UniversityFootballClub(university, name.toLowerCase(), location);
        universityFootballClub.setColors(color);
        if(nickname != null)
            if(!nickname.isEmpty() && !nickname.isBlank()) universityFootballClub.setNickname(nickname.toLowerCase());
        universityFootballClub.setCrest(crestUrl);
        universityFootballClub.setManager(manager);
        universityFootballClub.setHeadCoach(coach);
        universityFootballClub.setWebsite(website);

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> leagueClubs = universityLeague.getClubs();

        if(!checkNameAvailability(leagueClubs, name)){
            throw new UniversityLeagueException("Club Name Already Exists");
        }

        leagueClubs.put(name, universityFootballClub);

    }

    @Override
    public void deleteFootballClub(String season, String name) throws UniversityLeagueException {

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> leagueClubs = universityLeague.getClubs();

        if(!leagueClubs.isEmpty()){
            if(!checkNameAvailability(leagueClubs, name)){
                leagueClubs.remove(name);
            }
            else{
                throw new UniversityLeagueException("No Clubs Available With the Given Name.");
            }
        }else{
            throw new UniversityLeagueException("No Clubs Has Been Entered");
        }
    }

    @Override
    public UniversityStatsResponse displayStats(String season, String name) throws UniversityLeagueException{

        UniversityStatsResponse statsResponse = new UniversityStatsResponse();

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> leagueClubs = universityLeague.getClubs();

        if(!leagueClubs.isEmpty()){

            if(!checkNameAvailability(leagueClubs, name)){
                UniversityFootballClub universityFootballClub = leagueClubs.get(name);

                if(universityFootballClub.getNickname()!=null && !universityFootballClub.getNickname().isEmpty())
                    statsResponse.setNickName(universityFootballClub.getNickname());
                if(universityFootballClub.getWebsite()!=null && !universityFootballClub.getWebsite().isEmpty())
                    statsResponse.setWebsite(universityFootballClub.getWebsite());
                statsResponse.setUniversity(universityFootballClub.getUniversityName());
                statsResponse.setTtlPoints(String.valueOf(universityFootballClub.getPoints()));
                statsResponse.setTtlGoals(String.valueOf(universityFootballClub.getNoOfGoals()));
                statsResponse.setTtlGoalsConceded(String.valueOf(universityFootballClub.getNoOfGoalsConceded()));
                statsResponse.setNoOfWinnedMatches(String.valueOf(universityFootballClub.getWins()));
                statsResponse.setNoOfDrawedMatches(String.valueOf(universityFootballClub.getDraws()));
                statsResponse.setNoOfLossedMatches(String.valueOf(universityFootballClub.getDefeats()));

            }else{
                throw new UniversityLeagueException("No Clubs Found With the Given Name");
            }

            return statsResponse;
        }else{
            throw new UniversityLeagueException("No Clubs Has been Entered!");
        }

    }

    @Override
    public ArrayList<LeagueTableResponse> displayPremierLeagueTable(String season) throws UniversityLeagueException{

        ArrayList<LeagueTableResponse> leagueTableResponses = new ArrayList<LeagueTableResponse>();

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> leagueClubs = universityLeague.getClubs();

        if(!leagueClubs.isEmpty()){

            Map<String, UniversityFootballClub> sortedClubs = leagueClubs.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            for(UniversityFootballClub footballClub : sortedClubs.values()){

                String name = footballClub.getName();
                String points = Integer.toString(footballClub.getPoints());
                String goalDifference = Integer.toString(footballClub.calculateGoalDifference());
                String noOfWins = Integer.toString(footballClub.getWins());
                String noOfDraws = Integer.toString(footballClub.getDraws());
                String noOfDefeats = Integer.toString(footballClub.getDefeats());

                LeagueTableResponse leagueTableResponse = new LeagueTableResponse();
                leagueTableResponse.setName(name);
                leagueTableResponse.setPoints(points);
                leagueTableResponse.setGoalDifference(goalDifference);
                leagueTableResponse.setNoOfWins(noOfWins);
                leagueTableResponse.setNoOfDraws(noOfDraws);
                leagueTableResponse.setNoOfDefeats(noOfDefeats);

                leagueTableResponses.add(leagueTableResponse);

            }
            return leagueTableResponses;
        }else{
            throw new UniversityLeagueException("No Clubs Has been Entered");
        }

    }

    @Override
    public void addMatch(String season, AddMatchRequest request) throws UniversityLeagueException {

        String playedDate = request.getPlayedDate();
        String startedTime = request.getStartedTime();

        if(startedTime == null) throw new UniversityLeagueException("startedTime Cannot be Empty");
        if(startedTime.isBlank() || startedTime.isEmpty()) throw new UniversityLeagueException("startedTime Cannot be Empty");

        String location = request.getLocation();
        String teamName1 = request.getTeamName1();
        if(teamName1 == null) throw new UniversityLeagueException("Team Name 1 Cannot be Empty");
        String teamName2 = request.getTeamName2();
        if(teamName2 == null) throw new UniversityLeagueException("Team Name 2 Cannot be Empty");

        double score1 = request.getScore1();
        double score2 = request.getScore2();
        String status = request.getStatus();

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> leagueClubs = universityLeague.getClubs();

        if(!leagueClubs.isEmpty() && leagueClubs.size() > 1){

            if(teamName1.equalsIgnoreCase(teamName2)){
                throw new UniversityLeagueException("Two Clubs Should be Different");
            }

            if(checkNameAvailability(leagueClubs, (teamName1))){
                throw new UniversityLeagueException("Invalid, Team Name 1 is Not Available");
            }

            if(checkNameAvailability(leagueClubs, teamName2)){
                throw new UniversityLeagueException("Invalid, Team Name 2 is Not Available");
            }

            String datetimestr = playedDate + " " + startedTime;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(datetimestr, formatter);

            Location locObj = new Location(null, location, null);

            Match match = new Match(dateTime, locObj, leagueClubs.get(teamName1.toLowerCase()), leagueClubs.get(teamName2.toLowerCase()), score1, score2);
            match.setStatus(status);
            processMatch(match);

        }
        else{
            throw new UniversityLeagueException("No Sufficient Amount of Clubs");
        }

    }

    public boolean checkNameAvailability(Map<String, UniversityFootballClub> clubs, String name){
        if(!clubs.isEmpty()){
            boolean status = true;
            for(String key : clubs.keySet()){
                if(key.equalsIgnoreCase(name)){
                    status = false;
                    break;
                }
            }
            return status;
        }else{
            return true;
        }
    }

    @Override
    public void testLoad(String season) throws UniversityLeagueException{

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> clubs = universityLeague.getClubs();


        UniversityFootballClub f1 = new UniversityFootballClub("universityName", "Club1", new Location(null, "", ""));
        f1.setPoints(10);
        f1.setNoOfGoals(10);
        f1.setNoOfGoalsConceded(5);
        clubs.put("club1", f1);

        UniversityFootballClub f2 = new UniversityFootballClub("universityName","Club2", new Location(null, "", ""));
        f2.setPoints(10);
        f2.setNoOfGoals(10);
        f2.setNoOfGoalsConceded(5);
        clubs.put("club2", f2);

        UniversityFootballClub f3 = new UniversityFootballClub("universityName","Club3", new Location(null, "", ""));
        f3.setPoints(10);
        f3.setNoOfGoals(10);
        f3.setNoOfGoalsConceded(5);
        clubs.put("club3", f3);

        UniversityFootballClub f4 = new UniversityFootballClub("universityName","Club4", new Location(null, "", ""));
        f4.setPoints(10);
        f4.setNoOfGoals(10);
        f4.setNoOfGoalsConceded(5);
        clubs.put("club4", f4);

        UniversityFootballClub f5 = new UniversityFootballClub("universityName","Club5", new Location(null, "", ""));
        f5.setPoints(10);
        f5.setNoOfGoals(10);
        f5.setNoOfGoalsConceded(1);
        clubs.put("club5", f5);

        UniversityFootballClub f6 = new UniversityFootballClub("universityName","Club6", new Location(null, "", ""));
        f6.setPoints(10);
        f6.setNoOfGoals(10);
        f6.setNoOfGoalsConceded(5);
        clubs.put("club6", f6);

        UniversityFootballClub f7 = new UniversityFootballClub("universityName","Club7", new Location(null, "", ""));
        f7.setPoints(15);
        f7.setNoOfGoals(10);
        f7.setNoOfGoalsConceded(4);
        clubs.put("club7", f7);

        UniversityFootballClub f8 = new UniversityFootballClub("universityName","Club8", new Location(null, "", ""));
        f8.setPoints(10);
        f8.setNoOfGoals(10);
        f8.setNoOfGoalsConceded(5);
        clubs.put("club8", f8);

        UniversityFootballClub f9 = new UniversityFootballClub("universityName","Club9", new Location(null, "", ""));
        f9.setPoints(15);
        f9.setNoOfGoals(10);
        f9.setNoOfGoalsConceded(5);
        clubs.put("club9", f9);
    }

    private void processMatch(Match match){

        FootballClub club1 = match.getTeam1();
        FootballClub club2 = match.getTeam2();

        club1.addMatch(match);
        club2.addMatch(match);


        if(match.getTeam1Score() > match.getTeam2Score()){

            club1.matchWinned();
            club1.setNoOfGoals((int)match.getTeam1Score());
            club1.setNoOfGoalsConceded((int)match.getTeam2Score());

            club2.matchLossed();
            club2.setNoOfGoals((int)match.getTeam2Score());
            club2.setNoOfGoalsConceded((int)match.getTeam1Score());

        }

        if(match.getTeam2Score() > match.getTeam1Score()){

            club2.matchWinned();
            club2.setNoOfGoals((int)match.getTeam2Score());
            club2.setNoOfGoalsConceded((int)match.getTeam1Score());

            club1.matchLossed();
            club1.setNoOfGoals((int)match.getTeam1Score());
            club1.setNoOfGoalsConceded((int)match.getTeam2Score());

        }

        if(match.getTeam1Score() == match.getTeam2Score()){

            club1.matchDrawed();
            club1.setNoOfGoals((int)match.getTeam1Score());
            club1.setNoOfGoalsConceded((int)match.getTeam2Score());

            club2.matchDrawed();
            club2.setNoOfGoals((int)match.getTeam2Score());
            club2.setNoOfGoalsConceded((int)match.getTeam1Score());

        }


    }

    @Override
    public Set<String> getFootBallClubs(String season) throws UniversityLeagueException{

        League<UniversityFootballClub> universityLeague = universityLeagueDao.find(season).orElse(null);

        if(universityLeague == null){
            throw new UniversityLeagueException("No Season Available, Create First.");
        }

        Map<String, UniversityFootballClub> leagueClubs = universityLeague.getClubs();

        return leagueClubs.keySet();
    }

    @Override
    public void loadState() throws UniversityLeagueException{

        Path path = Paths.get("data/ulm.state");

        if(Files.exists(path.toAbsolutePath())){

            File file = new File(path.toAbsolutePath().toString());

            try{

                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                UniversityLeagueDao plDao = (UniversityLeagueDao) objectInputStream.readObject();
                universityLeagueDao = plDao;

                objectInputStream.close();
                fileInputStream.close();

            }catch (Exception e){
                throw new UniversityLeagueException("Loading State Failed: " + e.getMessage());
            }

        }else{
            throw new UniversityLeagueException("Data Source Not Available");
        }

    }

    @Override
    public void saveState() throws UniversityLeagueException{

        Path path = Paths.get("data/ulm.state");
        File file = new File(path.toAbsolutePath().toString());

        try{

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(universityLeagueDao);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (Exception e){
            throw new UniversityLeagueException("File Saving Failed: " + e.getMessage());
        }

    }

}
