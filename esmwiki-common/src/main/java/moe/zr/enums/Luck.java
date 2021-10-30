package moe.zr.enums;

import java.util.Random;

public enum Luck {
    VERY_LUCKY("大吉"), LUCKY("吉"), UNLUCKY("凶"), VERY_UNLUCKY("大凶"),TRICK("🦇TRICK🦇"),TREAT("🍬TREAT🍬");

    private String luck;

    Luck(String luck) {
        this.luck = luck;
    }

    public String getLuck() {
        return luck;
    }

    public static Luck randomLuck() {
        int i = new Random().nextInt(4);
        Luck luck = null;
        switch (i) {
            case 0:
                luck = VERY_LUCKY;
                break;
            case 1:
                luck = LUCKY;
                break;
            case 2:
                luck = UNLUCKY;
                break;
            case 3:
                luck = VERY_UNLUCKY;
                break;
        }
        return luck;
    }
}
