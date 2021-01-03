package services;


import dao.PremierLeagueDao;
import domains.League;
import exceptions.PremierLeagueException;
import models.Color;
import models.FootballClub;
import models.Location;
import models.Match;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.LeagueTableResponse;
import responses.MatchResponse;
import responses.StatsResponse;
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

public class PremierLeagueManager implements LeagueManager, Serializable {

	@Inject
	PremierLeagueDao premierLeagueDao;

	@Override
	public void createSeason(String season) throws PremierLeagueException {

		League<FootballClub> premierLeague = new League<FootballClub>();

		League<FootballClub> league = premierLeagueDao.find(season).orElse(null);

		if(league == null){
			premierLeague.setSeason(season);
			premierLeagueDao.save(season, premierLeague);
		}else{
			throw new PremierLeagueException("Season Already Exists");
		}

	}

	@Override
	public List<String> listSeasons(){

		List<String> response = new ArrayList<String>();

		premierLeagueDao.listAll().forEach((e)->{
			response.add(e.getSeason());
		});

		return response;

	}

	@Override
	public void createAndAddFootballClub(String season, AddFootballClubRequest addFootballClubRequest) throws PremierLeagueException{

		String name = addFootballClubRequest.getName();

		if(name == null) throw new PremierLeagueException("Name Cannot Be Empty");
		if(name.isBlank() || name.isEmpty()) throw new PremierLeagueException("Name Cannot Be Empty");

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

		FootballClub footballClub = new FootballClub(name.toLowerCase(), location);
		footballClub.setColors(color);
		if(nickname != null)
			if(!nickname.isEmpty() && !nickname.isBlank()) footballClub.setNickname(nickname.toLowerCase());
		footballClub.setCrest(crestUrl);
		footballClub.setManager(manager);
		footballClub.setHeadCoach(coach);
		footballClub.setWebsite(website);

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> leagueClubs = premierLeague.getClubs();

		if(!checkNameAvailability(leagueClubs, name)){
			throw new PremierLeagueException("Club Name Already Exists");
		}

		leagueClubs.put(name, footballClub);

	}

	@Override
	public void deleteFootballClub(String season, String name) throws PremierLeagueException {

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> leagueClubs = premierLeague.getClubs();

		if(!leagueClubs.isEmpty()){
			if(!checkNameAvailability(leagueClubs, name)){
				leagueClubs.remove(name);
			}
			else{
				throw new PremierLeagueException("No Clubs Available With the Given Name.");
			}
		}else{
			throw new PremierLeagueException("No Clubs Has Been Entered");
		}
	}

	@Override
	public StatsResponse displayStats(String season, String name) throws PremierLeagueException{

		StatsResponse statsResponse = new StatsResponse();

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> leagueClubs = premierLeague.getClubs();

		if(!leagueClubs.isEmpty()){

			if(!checkNameAvailability(leagueClubs, name)){
				FootballClub footBallClub = leagueClubs.get(name);

				if(footBallClub.getNickname()!=null && !footBallClub.getNickname().isEmpty())
					statsResponse.setNickName(footBallClub.getNickname());
				if(footBallClub.getWebsite()!=null && !footBallClub.getWebsite().isEmpty())
					statsResponse.setWebsite(footBallClub.getWebsite());
				statsResponse.setTtlPoints(String.valueOf(footBallClub.getPoints()));
				statsResponse.setTtlGoals(String.valueOf(footBallClub.getNoOfGoals()));
				statsResponse.setTtlGoalsConceded(String.valueOf(footBallClub.getNoOfGoalsConceded()));
				statsResponse.setNoOfWinnedMatches(String.valueOf(footBallClub.getWins()));
				statsResponse.setNoOfDrawedMatches(String.valueOf(footBallClub.getDraws()));
				statsResponse.setNoOfLossedMatches(String.valueOf(footBallClub.getDefeats()));

			}else{
				throw new PremierLeagueException("No Clubs Found With the Given Name");
			}

			return statsResponse;
		}else{
			throw new PremierLeagueException("No Clubs Has been Entered!");
		}

	}

	@Override
	public ArrayList<LeagueTableResponse> displayPremierLeagueTable(String season) throws PremierLeagueException{

		ArrayList<LeagueTableResponse> leagueTableResponses = new ArrayList<LeagueTableResponse>();

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> leagueClubs = premierLeague.getClubs();

		if(!leagueClubs.isEmpty()){

			Map<String, FootballClub> sortedClubs = leagueClubs.entrySet()
														.stream()
														.sorted(Map.Entry.comparingByValue())
														.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

			for(FootballClub footballClub : sortedClubs.values()){

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
			throw new PremierLeagueException("No Clubs Has been Entered");
		}

	}

	@Override
	public void addMatch(String season, AddMatchRequest request) throws PremierLeagueException {

		String playedDate = request.getPlayedDate();
		String startedTime = request.getStartedTime();

		if(startedTime == null) throw new PremierLeagueException("startedTime Cannot be Empty");
		if(startedTime.isBlank() || startedTime.isEmpty()) throw new PremierLeagueException("startedTime Cannot be Empty");

		String location = request.getLocation();
		String teamName1 = request.getTeamName1();
		if(teamName1 == null) throw new PremierLeagueException("Team Name 1 Cannot be Empty");
		String teamName2 = request.getTeamName2();
		if(teamName2 == null) throw new PremierLeagueException("Team Name 2 Cannot be Empty");

		double score1 = request.getScore1();
		double score2 = request.getScore2();
		String status = request.getStatus();

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> leagueClubs = premierLeague.getClubs();

		if(!leagueClubs.isEmpty() && leagueClubs.size() > 1){

			if(teamName1.equalsIgnoreCase(teamName2)){
				throw new PremierLeagueException("Two Clubs Should be Different");
			}

			if(checkNameAvailability(leagueClubs, (teamName1))){
				throw new PremierLeagueException("Invalid, Team Name 1 is Not Available");
			}

			if(checkNameAvailability(leagueClubs, teamName2)){
				throw new PremierLeagueException("Invalid, Team Name 2 is Not Available");
			}

			String datetimestr = playedDate + " " + startedTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(datetimestr, formatter);

			Location locObj = new Location(null, location, null);

			Match match = new Match(dateTime, locObj, leagueClubs.get(teamName1.toLowerCase()), leagueClubs.get(teamName2.toLowerCase()), score1, score2);
			match.setStatus(status);
			processMatch(match);
            premierLeague.addMatch(match);

		}
		else{
			throw new PremierLeagueException("No Sufficient Amount of Clubs");
		}

	}

	public boolean checkNameAvailability(Map<String, FootballClub> clubs, String name){
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
	public void testLoad(String season) throws PremierLeagueException{

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> clubs = premierLeague.getClubs();


		FootballClub f1 = new FootballClub("Club1", new Location(null, "", ""));
		f1.setPoints(10);
		f1.setNoOfGoals(10);
		f1.setNoOfGoalsConceded(5);
		clubs.put("club1", f1);

		FootballClub f2 = new FootballClub("Club2", new Location(null, "", ""));
		f2.setPoints(10);
		f2.setNoOfGoals(10);
		f2.setNoOfGoalsConceded(5);
		clubs.put("club2", f2);

		FootballClub f3 = new FootballClub("Club3", new Location(null, "", ""));
		f3.setPoints(10);
		f3.setNoOfGoals(10);
		f3.setNoOfGoalsConceded(5);
		clubs.put("club3", f3);

		FootballClub f4 = new FootballClub("Club4", new Location(null, "", ""));
		f4.setPoints(10);
		f4.setNoOfGoals(10);
		f4.setNoOfGoalsConceded(5);
		clubs.put("club4", f4);

		FootballClub f5 = new FootballClub("Club5", new Location(null, "", ""));
		f5.setPoints(10);
		f5.setNoOfGoals(10);
		f5.setNoOfGoalsConceded(1);
		clubs.put("club5", f5);

		FootballClub f6 = new FootballClub("Club6", new Location(null, "", ""));
		f6.setPoints(10);
		f6.setNoOfGoals(10);
		f6.setNoOfGoalsConceded(5);
		clubs.put("club6", f6);

		FootballClub f7 = new FootballClub("Club7", new Location(null, "", ""));
		f7.setPoints(15);
		f7.setNoOfGoals(10);
		f7.setNoOfGoalsConceded(4);
		clubs.put("club7", f7);

		FootballClub f8 = new FootballClub("Club8", new Location(null, "", ""));
		f8.setPoints(10);
		f8.setNoOfGoals(10);
		f8.setNoOfGoalsConceded(5);
		clubs.put("club8", f8);

		FootballClub f9 = new FootballClub("Club9", new Location(null, "", ""));
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
	public Set<String> getFootBallClubs(String season) throws PremierLeagueException{

		League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

		if(premierLeague == null){
			throw new PremierLeagueException("No Season Available, Create First.");
		}

		Map<String, FootballClub> leagueClubs = premierLeague.getClubs();

		return leagueClubs.keySet();
	}

	@Override
	public void loadState() throws PremierLeagueException{

		Path path = Paths.get("data/plm.state");

		if(Files.exists(path.toAbsolutePath())){

			File file = new File(path.toAbsolutePath().toString());

			try{

				FileInputStream fileInputStream = new FileInputStream(file);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

				PremierLeagueDao plDao = (PremierLeagueDao) objectInputStream.readObject();

				premierLeagueDao.clean();

				Map<String, League<FootballClub>> leagues = plDao.getDataSet();

				for(String key : leagues.keySet()){
					premierLeagueDao.save(key, leagues.get(key));
				}

				objectInputStream.close();
				fileInputStream.close();

			}catch (Exception e){
				throw new PremierLeagueException("Loading State Failed: " + e.getMessage());
			}

		}else{
			throw new PremierLeagueException("Data Source Not Available");
		}

	}

	@Override
	public void saveState() throws PremierLeagueException{

		Path path = Paths.get("data/plm.state");
		File file = new File(path.toAbsolutePath().toString());

		try{

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(premierLeagueDao);
			objectOutputStream.flush();
			objectOutputStream.close();
			fileOutputStream.flush();
			fileOutputStream.close();

		}catch (Exception e){
			throw new PremierLeagueException("File Saving Failed: " + e.getMessage());
		}

	}

	@Override
	public void addRandomMatch(String season) throws PremierLeagueException{

		League<FootballClub> league = premierLeagueDao.find(season).orElse(null);

		if(league != null){

		    int seasonYear = Integer.parseInt(season.substring(0, season.length()-3));
		    String status = null;
			List<String> clubNamesArray = new ArrayList<>();
			Set<String> clubNames = league.getClubs().keySet();
			for (String str : clubNames){
				clubNamesArray.add(str);
			}

			if(clubNamesArray.size()<2){
				throw new PremierLeagueException("No Sufficient Amount Of Clubs Available");
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
			throw new PremierLeagueException("No Season Available, Create First");
		}

	}

    @Override
    public List<MatchResponse> listMatches(String season) throws PremierLeagueException {

        League<FootballClub> premierLeague = premierLeagueDao.find(season).orElse(null);

        if(premierLeague == null){
            throw new PremierLeagueException("No Season Available, Create First.");
        }

        List<Match> matches = premierLeague.getMatches();

        if(matches.isEmpty()) throw new PremierLeagueException("No Matches Has Been Entered");

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
	public Match getMatchFromDate(String season, String date) throws PremierLeagueException{

		League<FootballClub> league = premierLeagueDao.find(season).orElse(null);
		if(league == null) throw new PremierLeagueException("No Seasons Available, Create First");

		Match res = league.getMatchByDate(date);
		if(res == null) throw new PremierLeagueException("No Match Found");

		return res;

	}


}