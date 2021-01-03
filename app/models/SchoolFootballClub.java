package models;

public class SchoolFootballClub extends FootballClub {

	private String schoolName;

	public SchoolFootballClub(String schoolName, String name, Location location) {
		super(name, location);
		this.schoolName = schoolName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchoolFootballClub other = (SchoolFootballClub) obj;
		if (schoolName == null) {
			if (other.schoolName != null)
				return false;
		} else if (!schoolName.equals(other.schoolName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((schoolName == null) ? 0 : schoolName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "SchoolFootballClub [schoolName=" + schoolName + "]";
	}

}