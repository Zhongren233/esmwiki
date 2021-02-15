package moe.zr.enums;

public enum EventPointReward {
    FIRST_CARD("一卡", 350 * 10000),
    SECOND_CARD("二卡", 600 * 10000),
    THIRD_CARD("三卡", 1100 * 10000),
    FOURTH_CARD("四卡", 1350 * 10000),
    MAX_EVO("满破", 2100 * 10000);

    private final String gear;
    private final Integer point;

    EventPointReward(String gear, Integer point) {
        this.gear = gear;
        this.point = point;
    }

    public String getGear() {
        return gear;
    }

    public Integer getPoint() {
        return point;
    }

    public static EventPointReward getEventPointReward(String gear) {
        for (EventPointReward value : values()) {
            if (value.gear.equals(gear)) {
                return value;
            }
        }
        return null;
    }

}
