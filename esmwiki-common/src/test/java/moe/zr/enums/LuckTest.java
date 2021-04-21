package moe.zr.enums;

import org.junit.Test;

public class LuckTest {
    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Luck.randomLuck().getLuck());
        }
    }
}