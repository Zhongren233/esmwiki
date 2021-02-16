package moe.zr.enums;

public enum EventType {
    TOUR("巡演"),UNIT("组合"), SHUFFLE("洗牌");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static EventType getEventType(String type) {
        for (EventType value : EventType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
