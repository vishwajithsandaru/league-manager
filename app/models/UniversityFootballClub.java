package models;

public class UniversityFootballClub extends FootballClub {

	private String universityName;

	public UniversityFootballClub(String universityName, String name, Location location) {
		super(name, location);
		this.universityName = universityName;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UniversityFootballClub other = (UniversityFootballClub) obj;
		if (universityName == null) {
			if (other.universityName != null)
				return false;
		} else if (!universityName.equals(other.universityName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((universityName == null) ? 0 : universityName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "UniversityFootballClub [universityName=" + universityName + "]";
	}
	
}