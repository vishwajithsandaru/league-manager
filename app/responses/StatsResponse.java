package responses;

public class StatsResponse {
    private String nickName;
    private String website;
    private String ttlPoints;
    private String ttlGoals;
    private String ttlGoalsConceded;
    private String noOfWinnedMatches;
    private String noOfDrawedMatches;
    private String noOfLossedMatches;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTtlPoints() {
        return ttlPoints;
    }

    public void setTtlPoints(String ttlPoints) {
        this.ttlPoints = ttlPoints;
    }

    public String getTtlGoals() {
        return ttlGoals;
    }

    public void setTtlGoals(String ttlGoals) {
        this.ttlGoals = ttlGoals;
    }

    public String getTtlGoalsConceded() {
        return ttlGoalsConceded;
    }

    public void setTtlGoalsConceded(String ttlGoalsConceded) {
        this.ttlGoalsConceded = ttlGoalsConceded;
    }

    public String getNoOfWinnedMatches() {
        return noOfWinnedMatches;
    }

    public void setNoOfWinnedMatches(String noOfWinnedMatches) {
        this.noOfWinnedMatches = noOfWinnedMatches;
    }

    public String getNoOfDrawedMatches() {
        return noOfDrawedMatches;
    }

    public void setNoOfDrawedMatches(String noOfDrawedMatches) {
        this.noOfDrawedMatches = noOfDrawedMatches;
    }

    public String getNoOfLossedMatches() {
        return noOfLossedMatches;
    }

    public void setNoOfLossedMatches(String noOfLossedMatches) {
        this.noOfLossedMatches = noOfLossedMatches;
    }
}
