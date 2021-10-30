package moe.zr.esmwiki.producer.util;

import moe.zr.enums.Direction;
import moe.zr.enums.Luck;
import moe.zr.esmwiki.producer.repository.FortuneRepository;
import moe.zr.pojo.DailyFortune;
import moe.zr.pojo.Fortune;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class DailyFortuneProducer {
    final
    Random random;
    final
    FortuneRepository fortuneRepository;

    public DailyFortuneProducer(Random random, FortuneRepository fortuneRepository) {
        this.random = random;
        this.fortuneRepository = fortuneRepository;
    }

    public DailyFortune getDailyFortune() {
        DailyFortune dailyFortune = new DailyFortune();
        List<Fortune> random = fortuneRepository.getRandom(3);
        dailyFortune.setLuck(Luck.randomLuck());
        switch (dailyFortune.getLuck()) {
            case VERY_LUCKY:
                dailyFortune.setUnluckyFortune(random.subList(0, 1));
                dailyFortune.setLuckyFortune(random.subList(1, random.size()));
                break;
            case VERY_UNLUCKY:
                dailyFortune.setUnluckyFortune(random.subList(1, random.size()));
                dailyFortune.setLuckyFortune(random.subList(0, 1));
                break;
            default:
                dailyFortune.setUnluckyFortune(random.subList(0, 1));
                dailyFortune.setLuckyFortune(random.subList(1, 2));
        }
        dailyFortune.setDirection(Direction.getRandom());
        halloweenCheck(dailyFortune);
        return dailyFortune;
    }

    private void halloweenCheck(DailyFortune dailyFortune) {
        //for test
        LocalDate now = LocalDate.now();
        if (now.getMonth().getValue() == 10) {
            if (now.getDayOfMonth() == 31) {
                if (this.random.nextBoolean()) {
                    if (this.random.nextBoolean()) {
                        dailyFortune.setLuck(Luck.TREAT);
                    }else {
                        dailyFortune.setLuck(Luck.TRICK);
                    }
                }
            }
        }
    }
}
