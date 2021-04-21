package moe.zr.enums;

import java.util.Random;

public enum Direction {

    EAST("东"), WEST("西"), SOUTH("南"), NORTH("北"), FLOOR("地板"), CEILING("天花板");
    private String name;
    Direction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Direction getRandom() {
        Random random = new Random();
        int i = random.nextInt(Direction.values().length);
        Direction direction = null;
        switch (i) {
            case 0:
                direction = EAST;
                break;
            case 1:
                direction = WEST;
                break;
            case 2:
                direction = SOUTH;
                break;
            case 3:
                direction = NORTH;
                break;
            case 4:
                direction = FLOOR;
                break;
            case 5:
                direction = CEILING;
                break;
        }
        return direction;
    }
}
