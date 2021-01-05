package services;


import exceptions.PremierLeagueException;
import exceptions.UserInputException;
import models.FootballClub;
import models.Match;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.LeagueTableResponse;
import responses.MatchResponse;
import responses.StatsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LeagueManager {

	public void createSeason(String season) throws Exception;

	public List<String> listSeasons();

	public void createAndAddFootballClub(String season, AddFootballClubRequest addFootballClubRequest) throws Exception;

	public void deleteFootballClub(String season, String name) throws Exception;

	public StatsResponse displayStats(String season, String name) throws Exception;

	public ArrayList<LeagueTableResponse> displayPremierLeagueTable(String season) throws Exception;

	public void addMatch(String season, AddMatchRequest addMatchRequest) throws Exception;

	public Set<String> getFootBallClubs(String season) throws Exception;

	public void testLoad(String season) throws Exception;

	public void loadState() throws Exception;

	public void saveState() throws Exception;

	public void addRandomMatch(String season) throws Exception;

	public List<MatchResponse> listMatches(String season) throws Exception;

	public Match getMatchFromDate(String season, String date) throws Exception;

}