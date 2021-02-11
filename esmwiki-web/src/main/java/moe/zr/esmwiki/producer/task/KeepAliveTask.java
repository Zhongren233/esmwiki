package moe.zr.esmwiki.producer.task;

import lombok.extern.slf4j.Slf4j;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.MyPageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.concurrent.ExecutionException;

@EnableScheduling
@Configuration
@Slf4j
public class KeepAliveTask implements IMessageQuickReply {
    private boolean alive = true;
    final
    MyPageService myPageService;

    public KeepAliveTask(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    private void keepAlive() {
        if (alive) {
            try {
                myPageService.getMyPage();
                log.info("成功获取MyPage");
            } catch (BadPaddingException | IllegalBlockSizeException | ExecutionException | RuntimeException | InterruptedException e) {
                e.printStackTrace();
                alive = false;
            }
        }
    }

    @Override
    public String onMessage(String[] str) {
        return alive ? "alive" : "大约孔乙己的确死了。";
    }

    @Override
    public String commandPrefix() {
        return "/alive";
    }
}