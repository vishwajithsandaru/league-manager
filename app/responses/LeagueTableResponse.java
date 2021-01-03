package responses;

public class LeagueTableResponse {

    private String name;
    private String points;
    private String goalDifference;
    private String noOfWins;
    private String noOfDraws;
    private String noOfDefeats;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(String goalDifference) {
        this.goalDifference = goalDifference;
    }

    public String getNoOfWins() {
        return noOfWins;
    }

    public void setNoOfWins(String noOfWins) {
        this.noOfWins = noOfWins;
    }

    public String getNoOfDraws() {
        return noOfDraws;
    }

    public void setNoOfDraws(String noOfDraws) {
        this.noOfDraws = noOfDraws;
    }

    public String getNoOfDefeats() {
        return noOfDefeats;
    }

    public void setNoOfDefeats(String noOfDefeats) {
        this.noOfDefeats = noOfDefeats;
    }
}
