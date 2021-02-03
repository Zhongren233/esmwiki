package moe.zr.esmwiki.producer.config;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateFormatConfigTest {

    @Test
    void test() {
        String source = "2021-02-03 12:36:52";

        System.out.println(source.substring(0, 16));
    }
}