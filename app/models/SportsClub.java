package models;

import java.io.Serializable;

public abstract class SportsClub implements Serializable {

	private String name;

	private String nickname;

	private Location location;

	private Color colors;

	private String crest;

	private String manager;

	private String headCoach;

	private String website;

	public SportsClub(String name, Location location){
		this.name = name;
		this.location = location;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Color getColors() {
		return colors;
	}

	public void setColors(Color colors) {
		this.colors = colors;
	}

	public String getCrest() {
		return crest;
	}

	public void setCrest(String crest) {
		this.crest = crest;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getHeadCoach() {
		return headCoach;
	}

	public void setHeadCoach(String headCoach) {
		this.headCoach = headCoach;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SportsClub other = (SportsClub) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}