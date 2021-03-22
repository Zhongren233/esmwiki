package moe.zr.esmwiki.producer.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class PointPointRankingRecordTaskTest {
    @Autowired
    RankingRecordTask task;

    @Test
    void test() {
        String[] s = "/task settask 20210227-12:00".split(" ");
    }

}

