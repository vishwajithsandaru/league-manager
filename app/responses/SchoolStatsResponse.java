package responses;

public class SchoolStatsResponse extends StatsResponse{

    String school;

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }
}
