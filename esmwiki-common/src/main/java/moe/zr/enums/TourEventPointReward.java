package moe.zr.enums;

public enum TourEventPointReward implements IEventPointReward{
    FIRST_FIRST_CARD("第一张一卡", 300 * 10000),
    FIRST_SECOND_CARD("第一张二卡", 750 * 10000),
    FIRST_THIRD_CARD("第一张三卡", 950 * 10000),
    FIRST_FOURTH_CARD("第一张四卡", 1500 * 10000),
    FIRST_MAX_EVO("第一张满破", 2100 * 10000),

    SECOND_FIRST_CARD("第二张一卡", 350 * 10000),
    SECOND_SECOND_CARD("第二张二卡", 600 * 10000),
    SECOND_THIRD_CARD("第二张三卡", 1100 * 10000),
    SECOND_FOURTH_CARD("第二张四卡", 1350 * 10000),
    SECOND_MAX_EVO("第二张满破", 2200 * 10000);

    private final String gear;
    private final Integer point;

    TourEventPointReward(String gear, Integer point) {
        this.gear = gear;
        this.point = point;
    }

    public String getGear() {
        return gear;
    }

    public Integer getPoint() {
        return point;
    }

    public static TourEventPointReward getEventPointReward(String gear) {
        for (TourEventPointReward value : values()) {
            if (value.gear.equals(gear)) {
                return value;
            }
        }
        return null;
    }

}
