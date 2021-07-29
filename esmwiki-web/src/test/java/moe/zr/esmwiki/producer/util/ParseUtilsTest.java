package moe.zr.esmwiki.producer.util;

import org.junit.jupiter.api.Test;

class ParseUtilsTest {
    @Test
    void test() {
        long millis = System.currentTimeMillis();
        long l = millis / 60000 * 60000;
        System.out.println(millis);
        System.out.println(l);
    }
}