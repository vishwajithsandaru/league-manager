package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Match implements Serializable {

	private FootballClub team1;

	private FootballClub team2;

	private double team1Score;

	private double team2Score;

	private LocalDateTime dateTime;

	private Location location;

	private String status;

	public Match(LocalDateTime dateTime, Location location, FootballClub team1, FootballClub team2, double score1, double score2) {
		this.dateTime = dateTime;
		this.location = location;
		this.team1 = team1;
		this.team2 = team2;
		this.team1Score = score1;
		this.team2Score = score2;
	}

	public void setTeam1(FootballClub team1) {
		this.team1 = team1;
	}

	public FootballClub getTeam1() {
		return team1;
	}

	public void setTeam2(FootballClub team2) {
		this.team2 = team2;
	}

	public FootballClub getTeam2() {
		return team2;
	}

	public void setTeam1Score(double team1Score) {
		this.team1Score = team1Score;
	}

	public double getTeam1Score() {
		return team1Score;
	}

	public void setTeam2Score(double team2Score) {
		this.team2Score = team2Score;
	}

	public double getTeam2Score() {
		return team2Score;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((team1 == null) ? 0 : team1.hashCode());
		long temp;
		temp = Double.doubleToLongBits(team1Score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((team2 == null) ? 0 : team2.hashCode());
		temp = Double.doubleToLongBits(team2Score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Match [dateTime=" + dateTime + ", location=" + location + ", status=" + status + ", team1=" + team1
				+ ", team1Score=" + team1Score + ", team2=" + team2 + ", team2Score=" + team2Score + "]";
	}

}