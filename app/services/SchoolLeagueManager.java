package services;

import dao.SchoolLeagueDao;
import domains.League;
import exceptions.SchoolLeagueException;
import exceptions.SchoolLeagueException;
import models.*;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.LeagueTableResponse;
import responses.MatchResponse;
import responses.SchoolStatsResponse;
import utils.PremierLeagueUtil;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SchoolLeagueManager implements LeagueManager{

    @Inject
    SchoolLeagueDao schoolLeagueDao;

    @Override
    public void createSeason(String season) throws SchoolLeagueException {

        League<SchoolFootballClub> schoolLeague = new League<SchoolFootballClub>();

        League<SchoolFootballClub> league = schoolLeagueDao.find(season).orElse(null);

        if(league == null){
            schoolLeague.setSeason(season);
            schoolLeagueDao.save(season, schoolLeague);
        }else{
            throw new SchoolLeagueException("Season Already Exists");
        }

    }

    @Override
    public List<String> listSeasons(){

        List<String> response = new ArrayList<String>();

        schoolLeagueDao.listAll().forEach((e)->{
            response.add(e.getSeason());
        });

        return response;

    }

    @Override
    public void createAndAddFootballClub(String season, AddFootballClubRequest addFootballClubRequest) throws SchoolLeagueException{

        String name = addFootballClubRequest.getName();

        if(name == null) throw new SchoolLeagueException("Name Cannot Be Empty");
        if(name.isBlank() || name.isEmpty()) throw new SchoolLeagueException("Name Cannot Be Empty");

        String school = addFootballClubRequest.getSchool();
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

        SchoolFootballClub schoolFootballClub = new SchoolFootballClub(school, name.toLowerCase(), location);
        schoolFootballClub.setColors(color);
        if(nickname != null)
            if(!nickname.isEmpty() && !nickname.isBlank()) schoolFootballClub.setNickname(nickname.toLowerCase());
        schoolFootballClub.setCrest(crestUrl);
        schoolFootballClub.setManager(manager);
        schoolFootballClub.setHeadCoach(coach);
        schoolFootballClub.setWebsite(website);

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> leagueClubs = schoolLeague.getClubs();

        if(!checkNameAvailability(leagueClubs, name)){
            throw new SchoolLeagueException("Club Name Already Exists");
        }

        leagueClubs.put(name, schoolFootballClub);

    }

    @Override
    public void deleteFootballClub(String season, String name) throws SchoolLeagueException {

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> leagueClubs = schoolLeague.getClubs();

        if(!leagueClubs.isEmpty()){
            if(!checkNameAvailability(leagueClubs, name)){
                leagueClubs.remove(name);
            }
            else{
                throw new SchoolLeagueException("No Clubs Available With the Given Name.");
            }
        }else{
            throw new SchoolLeagueException("No Clubs Has Been Entered");
        }
    }

    @Override
    public SchoolStatsResponse displayStats(String season, String name) throws SchoolLeagueException{

        SchoolStatsResponse statsResponse = new SchoolStatsResponse();

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> leagueClubs = schoolLeague.getClubs();

        if(!leagueClubs.isEmpty()){

            if(!checkNameAvailability(leagueClubs, name)){
                SchoolFootballClub schoolFootballClub = leagueClubs.get(name);

                if(schoolFootballClub.getNickname()!=null && !schoolFootballClub.getNickname().isEmpty())
                    statsResponse.setNickName(schoolFootballClub.getNickname());
                if(schoolFootballClub.getWebsite()!=null && !schoolFootballClub.getWebsite().isEmpty())
                    statsResponse.setWebsite(schoolFootballClub.getWebsite());
                statsResponse.setSchool(schoolFootballClub.getSchoolName());
                statsResponse.setTtlPoints(String.valueOf(schoolFootballClub.getPoints()));
                statsResponse.setTtlGoals(String.valueOf(schoolFootballClub.getNoOfGoals()));
                statsResponse.setTtlGoalsConceded(String.valueOf(schoolFootballClub.getNoOfGoalsConceded()));
                statsResponse.setNoOfWinnedMatches(String.valueOf(schoolFootballClub.getWins()));
                statsResponse.setNoOfDrawedMatches(String.valueOf(schoolFootballClub.getDraws()));
                statsResponse.setNoOfLossedMatches(String.valueOf(schoolFootballClub.getDefeats()));

            }else{
                throw new SchoolLeagueException("No Clubs Found With the Given Name");
            }

            return statsResponse;
        }else{
            throw new SchoolLeagueException("No Clubs Has been Entered!");
        }

    }

    @Override
    public ArrayList<LeagueTableResponse> displayPremierLeagueTable(String season) throws SchoolLeagueException{

        ArrayList<LeagueTableResponse> leagueTableResponses = new ArrayList<LeagueTableResponse>();

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> leagueClubs = schoolLeague.getClubs();

        if(!leagueClubs.isEmpty()){

            Map<String, SchoolFootballClub> sortedClubs = leagueClubs.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            for(SchoolFootballClub footballClub : sortedClubs.values()){

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
            throw new SchoolLeagueException("No Clubs Has been Entered");
        }

    }

    @Override
    public void addMatch(String season, AddMatchRequest request) throws SchoolLeagueException {

        String playedDate = request.getPlayedDate();
        String startedTime = request.getStartedTime();

        if(startedTime == null) throw new SchoolLeagueException("startedTime Cannot be Empty");
        if(startedTime.isBlank() || startedTime.isEmpty()) throw new SchoolLeagueException("startedTime Cannot be Empty");

        String location = request.getLocation();
        String teamName1 = request.getTeamName1();
        if(teamName1 == null) throw new SchoolLeagueException("Team Name 1 Cannot be Empty");
        String teamName2 = request.getTeamName2();
        if(teamName2 == null) throw new SchoolLeagueException("Team Name 2 Cannot be Empty");

        double score1 = request.getScore1();
        double score2 = request.getScore2();
        String status = request.getStatus();

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> leagueClubs = schoolLeague.getClubs();

        if(!leagueClubs.isEmpty() && leagueClubs.size() > 1){

            if(teamName1.equalsIgnoreCase(teamName2)){
                throw new SchoolLeagueException("Two Clubs Should be Different");
            }

            if(checkNameAvailability(leagueClubs, (teamName1))){
                throw new SchoolLeagueException("Invalid, Team Name 1 is Not Available");
            }

            if(checkNameAvailability(leagueClubs, teamName2)){
                throw new SchoolLeagueException("Invalid, Team Name 2 is Not Available");
            }

            String datetimestr = playedDate + " " + startedTime;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(datetimestr, formatter);

            Location locObj = new Location(null, location, null);

            Match match = new Match(dateTime, locObj, leagueClubs.get(teamName1.toLowerCase()), leagueClubs.get(teamName2.toLowerCase()), score1, score2);
            match.setStatus(status);
            processMatch(match);
            schoolLeague.addMatch(match);

        }
        else{
            throw new SchoolLeagueException("No Sufficient Amount of Clubs");
        }

    }

    public boolean checkNameAvailability(Map<String, SchoolFootballClub> clubs, String name){
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
    public void testLoad(String season) throws SchoolLeagueException{

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> clubs = schoolLeague.getClubs();


        SchoolFootballClub f1 = new SchoolFootballClub("schoolName", "Club1", new Location(null, "", ""));
        f1.setPoints(10);
        f1.setNoOfGoals(10);
        f1.setNoOfGoalsConceded(5);
        clubs.put("club1", f1);

        SchoolFootballClub f2 = new SchoolFootballClub("schoolName","Club2", new Location(null, "", ""));
        f2.setPoints(10);
        f2.setNoOfGoals(10);
        f2.setNoOfGoalsConceded(5);
        clubs.put("club2", f2);

        SchoolFootballClub f3 = new SchoolFootballClub("schoolName","Club3", new Location(null, "", ""));
        f3.setPoints(10);
        f3.setNoOfGoals(10);
        f3.setNoOfGoalsConceded(5);
        clubs.put("club3", f3);

        SchoolFootballClub f4 = new SchoolFootballClub("schoolName","Club4", new Location(null, "", ""));
        f4.setPoints(10);
        f4.setNoOfGoals(10);
        f4.setNoOfGoalsConceded(5);
        clubs.put("club4", f4);

        SchoolFootballClub f5 = new SchoolFootballClub("schoolName","Club5", new Location(null, "", ""));
        f5.setPoints(10);
        f5.setNoOfGoals(10);
        f5.setNoOfGoalsConceded(1);
        clubs.put("club5", f5);

        SchoolFootballClub f6 = new SchoolFootballClub("schoolName","Club6", new Location(null, "", ""));
        f6.setPoints(10);
        f6.setNoOfGoals(10);
        f6.setNoOfGoalsConceded(5);
        clubs.put("club6", f6);

        SchoolFootballClub f7 = new SchoolFootballClub("schoolName","Club7", new Location(null, "", ""));
        f7.setPoints(15);
        f7.setNoOfGoals(10);
        f7.setNoOfGoalsConceded(4);
        clubs.put("club7", f7);

        SchoolFootballClub f8 = new SchoolFootballClub("schoolName","Club8", new Location(null, "", ""));
        f8.setPoints(10);
        f8.setNoOfGoals(10);
        f8.setNoOfGoalsConceded(5);
        clubs.put("club8", f8);

        SchoolFootballClub f9 = new SchoolFootballClub("schoolName","Club9", new Location(null, "", ""));
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
    public Set<String> getFootBallClubs(String season) throws SchoolLeagueException{

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        Map<String, SchoolFootballClub> leagueClubs = schoolLeague.getClubs();

        return leagueClubs.keySet();
    }

    @Override
    public void loadState() throws SchoolLeagueException{

        Path path = Paths.get("data/slm.state");

        if(Files.exists(path.toAbsolutePath())){

            File file = new File(path.toAbsolutePath().toString());

            try{

                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                SchoolLeagueDao plDao = (SchoolLeagueDao) objectInputStream.readObject();

                schoolLeagueDao.clean();

                Map<String, League<SchoolFootballClub>> leagues = plDao.getDataSet();

                for(String key : leagues.keySet()){
                    schoolLeagueDao.save(key, leagues.get(key));
                }

                objectInputStream.close();
                fileInputStream.close();

            }catch (Exception e){
                throw new SchoolLeagueException("Loading State Failed: " + e.getMessage());
            }

        }else{
            throw new SchoolLeagueException("Data Source Not Available");
        }

    }

    @Override
    public void saveState() throws SchoolLeagueException{

        Path path = Paths.get("data/slm.state");
        File file = new File(path.toAbsolutePath().toString());

        try{

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(schoolLeagueDao);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (Exception e){
            throw new SchoolLeagueException("File Saving Failed: " + e.getMessage());
        }

    }

    @Override
    public void addRandomMatch(String season) throws SchoolLeagueException {

        League<SchoolFootballClub> league = schoolLeagueDao.find(season).orElse(null);

        if(league != null){

            int seasonYear = Integer.parseInt(season.substring(0, season.length()-3));
            String status = null;
            List<String> clubNamesArray = new ArrayList<>();
            Set<String> clubNames = league.getClubs().keySet();
            for (String str : clubNames){
                clubNamesArray.add(str);
            }

            if(clubNamesArray.size()<2){
                throw new SchoolLeagueException("No Sufficient Amount Of Clubs Available");
            }

            Random rand = new Random();
            String team1 = clubNamesArray.get(rand.nextInt(clubNamesArray.size()));
            String team2 = clubNamesArray.get(rand.nextInt(clubNamesArray.size()));
            while(team1.equals(team2)){
                team2 = clubNamesArray.get(rand.nextInt(clubNamesArray.size()));
            }
            String date = PremierLeagueUtil.randomDate(seasonYear);
            String time = PremierLeagueUtil.randomTime();
            int score1 = PremierLeagueUtil.getRandomNumber(0, 18);
            int score2 = PremierLeagueUtil.getRandomNumber(0, 18);
            if(score1 > score2){
                status = team1 + " Won";
            }else if(score1 < score2){
                status = team2 + " Won";
            }else{
                status = "Draw";
            }

            AddMatchRequest addMatchRequest = new AddMatchRequest();
            addMatchRequest.setPlayedDate(date);
            addMatchRequest.setStartedTime(time);
            addMatchRequest.setTeamName1(team1);
            addMatchRequest.setTeamName2(team2);
            addMatchRequest.setScore1(score1);
            addMatchRequest.setScore2(score2);
            addMatchRequest.setLocation("N/A");

            addMatch(season, addMatchRequest);

        }else{
            throw new SchoolLeagueException("No Season Available, Create First");
        }

    }

    @Override
    public List<MatchResponse> listMatches(String season) throws SchoolLeagueException {

        League<SchoolFootballClub> schoolLeague = schoolLeagueDao.find(season).orElse(null);

        if(schoolLeague == null){
            throw new SchoolLeagueException("No Season Available, Create First.");
        }

        List<Match> matches = schoolLeague.getMatches();

        if(matches.isEmpty()) throw new SchoolLeagueException("No Matches Has Been Entered");

        List<MatchResponse> matchResponses = new ArrayList<MatchResponse>();

        for(Match match : matches){

            MatchResponse matchResponse = new MatchResponse();
            matchResponse.setTeam1(match.getTeam1().getName());
            matchResponse.setTeam2(match.getTeam2().getName());
            matchResponse.setScore1(String.valueOf(match.getTeam1Score()));
            matchResponse.setScore2(String.valueOf(match.getTeam2Score()));

            LocalDate localdate = match.getDateTime().toLocalDate();
            LocalTime time = match.getDateTime().toLocalTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            matchResponse.setDate(localdate.format(formatter));
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            matchResponse.setTime(time.format(formatter));
            matchResponse.setStatus(match.getStatus());

            matchResponses.add(matchResponse);

        }

        Collections.sort(matchResponses);

        return matchResponses;

    }

    @Override
    public Match getMatchFromDate(String season, String date) throws SchoolLeagueException{

        League<SchoolFootballClub> league = schoolLeagueDao.find(season).orElse(null);
        if(league == null) throw new SchoolLeagueException("No Seasons Available, Create First");

        Match res = league.getMatchByDate(date);
        if(res == null) throw new SchoolLeagueException("No Match Found");

        return res;

    }


}
