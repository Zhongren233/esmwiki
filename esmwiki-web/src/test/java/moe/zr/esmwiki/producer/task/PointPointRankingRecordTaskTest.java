package moe.zr.esmwiki.producer.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PointPointRankingRecordTaskTest {
    @Autowired
    RankingRecordTask task;

    @Test
    void test() {
        String[] s = "/task settask 20210227-12:00".split(" ");
        System.out.println(task.onMessage(s));
    }

}

