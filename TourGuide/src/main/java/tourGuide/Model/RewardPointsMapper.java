package tourGuide.Model;

/**
 * RewardPointsMapper Model, used for Reward SVC
 */
public class RewardPointsMapper {

    private Integer points;

    public RewardPointsMapper() {
    }

    public RewardPointsMapper(Integer points) {
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}
