package moe.zr.esmwiki.producer.service.impl;

import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.service.DAQService;
import moe.zr.service.EventService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DAQServiceImpl implements DAQService {
    final
    EventService eventService;
    final
    ReplyUtils replyUtils;

    public DAQServiceImpl(EventService eventService, ReplyUtils replyUtils) {
        this.eventService = eventService;
        this.replyUtils = replyUtils;
    }

    @Async
    public void saveAllRanking() {
        try {
            ListenableFuture<Integer> pointRankingResult = eventService.saveAllPointRanking();
            ListenableFuture<Integer> scoreRankingResult = eventService.saveAllScoreRanking();
            pointRankingResult.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    replyUtils.sendMessage("失败了失败了失败了失败了失败了");
                    log.error("在爬取scoreRanking时失败", ex);
                }

                @Override
                public void onSuccess(Integer result) {
                    replyUtils.sendMessage("成功获得" + result + "页pointRanking");
                }
            });
            scoreRankingResult.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    replyUtils.sendMessage("失败了失败了失败了失败了失败了");
                    log.error("在爬取scoreRanking时失败", ex);
                }

                @Override
                public void onSuccess(Integer result) {
                    replyUtils.sendMessage("成功获得" + result + "页scoreRanking");
                }
            });

        } catch (BadPaddingException | InterruptedException | ParseException | IOException | ExecutionException | IllegalBlockSizeException e) {
            log.error("发生异常", e);
            replyUtils.sendMessage("在爬取榜单时发生了异常，呜呜呜");
        }
    }
}
