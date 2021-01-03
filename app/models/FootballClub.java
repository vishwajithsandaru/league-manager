package models;

import java.util.ArrayList;

public class FootballClub extends SportsClub implements Comparable<FootballClub>{

	private int totalSeasonsPlayed = 0;

	private int wins = 0;

	private int draws = 0;

	private int defeats = 0;

	private int noOfGoals = 0;

	private int noOfGoalsConceded = 0;

	private int points = 0;

	private ArrayList<Match> matchers = new ArrayList<Match>();

	private Stadium homeStadium;

	public FootballClub(String name, Location location) {
		super(name, location);
	}

	public int getTotalSeasonsPlayed() {
		return totalSeasonsPlayed;
	}

	public void setTotalSeasonsPlayed(int totalSeasonsPlayed) {
		this.totalSeasonsPlayed = totalSeasonsPlayed;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public int getDefeats() {
		return defeats;
	}

	public void setDefeats(int defeats) {
		this.defeats = defeats;
	}

	public int getNoOfGoals() {
		return noOfGoals;
	}

	public void setNoOfGoals(int noOfGoals) {
		this.noOfGoals += noOfGoals;
	}

	public int getNoOfGoalsConceded() {
		return noOfGoalsConceded;
	}

	public void setNoOfGoalsConceded(int noOfGoalsConceded) {
		this.noOfGoalsConceded += noOfGoalsConceded;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void addMatch(Match match){
		matchers.add(match);
	}

	public int getNoOfMatchers(){
		return matchers.size();
	}

	public Stadium getHomeStadium() {
		return homeStadium;
	}

	public void setHomeStadium(Stadium homeStadium) {
		this.homeStadium = homeStadium;
	}

	public void matchWinned(){
		points += 3;
		wins += 1;
	}

	public void matchDrawed(){
		points += 1;
		draws += 1;
	}

	public void matchLossed(){
		defeats += 1;
	}

	public int calculateGoalDifference(){
		return noOfGoals - noOfGoalsConceded;
	}

	@Override
	public int compareTo(FootballClub footballClub) {
		int returnVal;
		if(points == footballClub.getPoints()){
			returnVal = footballClub.calculateGoalDifference() - calculateGoalDifference();
		}else{
			returnVal = footballClub.getPoints() - points;
		}
		return returnVal;
	}

}