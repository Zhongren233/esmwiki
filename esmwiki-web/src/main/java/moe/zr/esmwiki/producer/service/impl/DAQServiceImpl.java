package moe.zr.esmwiki.producer.service.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.util.ReplyUtils;
import moe.zr.pojo.PointRanking;
import moe.zr.pojo.ScoreRanking;
import moe.zr.qqbot.entry.IMessageQuickReply;
import moe.zr.qqbot.entry.Message;
import moe.zr.service.DAQService;
import moe.zr.service.EventService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class DAQServiceImpl implements DAQService, IMessageQuickReply {
    final
    EventService eventService;
    final
    ReplyUtils replyUtils;
    final
    MongoTemplate mongoTemplate;

    public DAQServiceImpl(EventService eventService, ReplyUtils replyUtils, MongoTemplate mongoTemplate) {
        this.eventService = eventService;
        this.replyUtils = replyUtils;
        this.mongoTemplate = mongoTemplate;
    }

    @Async
    public void saveAllRanking() {
        try {
            ListenableFuture<Integer> pointRankingResult = eventService.saveAllPointRanking();
            ListenableFuture<Integer> scoreRankingResult = eventService.saveAllScoreRanking();
            pointRankingResult.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    replyUtils.sendMessage(ex.getMessage());
                    log.error("在爬取pointRanking时异常", ex);
                }

                @Override
                public void onSuccess(Integer result) {
                    replyUtils.sendMessage("成功获得" + result + "页pointRanking");
                }
            });
            scoreRankingResult.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    replyUtils.sendMessage(ex.getMessage());
                    log.error("在爬取scoreRanking时异常", ex);
                }

                @Override
                public void onSuccess(Integer result) {
                    replyUtils.sendMessage("成功获得" + result + "页scoreRanking");
                }
            });

        } catch (BadPaddingException | InterruptedException | ParseException | IOException | ExecutionException | IllegalBlockSizeException e) {
            log.error("发生异常", e);
            replyUtils.sendMessage("在爬取榜单时发生了异常，呜呜呜");
        } catch (TimeoutException e) {
            log.warn(e.getMessage());
            replyUtils.sendMessage(e.getMessage());
        }
    }

    public String dropCollection() {
        mongoTemplate.dropCollection(PointRanking.class);
        mongoTemplate.dropCollection(ScoreRanking.class);
        return "删除集合成功";
    }

    @Autowired
    MongoClient mongoClient;
    public String mergeCollection() {
        MongoDatabase esmusic = mongoClient.getDatabase("ESMUSIC");
        replyUtils.sendMessage("merging point rankings....");
        esmusic.getCollection("pointRanking").aggregate(Collections.singletonList(new Document("$merge",
                new Document("into", "pastPointRanking")))).toCollection();
        replyUtils.sendMessage("merging score rankings....");
        esmusic.getCollection("scoreRanking").aggregate(Collections.singletonList(new Document("$merge",
                new Document("into", "pastScoreRanking")))).toCollection();

        return null;
    }

    @Override
    public String onMessage(String[] str) {
        switch (str[1]) {
            case "get":
                saveAllRanking();
                return "手动进行爬榜";
            case "drop":
                return dropCollection();
            case "merge":
                return mergeCollection();
        }
        return null;
    }

    @Override
    public String onMessage(Message message) {
        if (message.getGroupId() == 773891409) {
            return onMessage(message.getRawMessage().split(" "));
        }
        return "喵呜";
    }

    @Override
    public String commandPrefix() {
        return "/daq";
    }
}
