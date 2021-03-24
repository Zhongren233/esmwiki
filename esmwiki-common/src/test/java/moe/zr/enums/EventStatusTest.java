package moe.zr.enums;

import junit.framework.TestCase;
import org.junit.Test;

public class EventStatusTest {
    @Test
    public void test() {
        EventStatus aa = EventStatus.valueOf("aa");
        System.out.println(aa);
    }
}