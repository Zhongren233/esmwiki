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
class PointPointRankingRecordTaskTest {

    @Test
    void recordRank() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse = simpleDateFormat.parse("2020-10-23 12:00");
        System.out.println("simpleDateFormat.format(parse) = " + simpleDateFormat.format(parse));
    }
}