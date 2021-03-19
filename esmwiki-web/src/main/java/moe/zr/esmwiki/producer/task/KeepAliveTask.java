package moe.zr.esmwiki.producer.task;

import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.service.MyPageService;

@EnableScheduling
@Configuration
@Slf4j
public class KeepAliveTask implements IMessageQuickReply {
    private boolean alive = true;
    final
    MyPageService myPageService;
    final
    ReplyUtils replyUtils;

    public KeepAliveTask(MyPageService myPageService, ReplyUtils replyUtils) {
        this.myPageService = myPageService;
        this.replyUtils = replyUtils;
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    private void keepAlive() {
        if (alive) {
            try {
                myPageService.getMyPage();
                log.info("成功获取MyPage");
            } catch (BadPaddingException | IllegalBlockSizeException | ExecutionException | RuntimeException | InterruptedException e) {
                e.printStackTrace();
                log.error("获取MyPage出错", e);
                replyUtils.sendMessage("获取MyPage出错，已停止工作");
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