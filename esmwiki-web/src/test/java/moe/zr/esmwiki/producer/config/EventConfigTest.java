package moe.zr.esmwiki.producer.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;

@SpringBootTest
class EventConfigTest {

    @Autowired
    private EventConfig eventConfig;

    @Test
    void onMessage() {
        String s = "/config {0} {1}";

        String format = MessageFormat.format(s, "type", "组合");
        System.out.println(format);
        System.out.println(eventConfig.onMessage(format.split(" ")));

        format = MessageFormat.format(s, "event", "off");
        System.out.println(format);
        System.out.println(eventConfig.onMessage(format.split(" ")));

        format = MessageFormat.format(s, "date", "20210327-12:00");
        System.out.println(format);
        System.out.println(eventConfig.onMessage(format.split(" ")));
    }
}