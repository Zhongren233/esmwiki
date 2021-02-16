package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.util.CryptoUtils;
import moe.zr.esmwiki.producer.util.RequestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.jupiter.api.Test;
import org.msgpack.MessagePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

@SpringBootTest
class PointRankingServiceImplTest {
    @Autowired
    PointRankingServiceImpl service;
    @Autowired
    CloseableHttpAsyncClient asyncClient;
    @Autowired
    RequestUtils utils;

    @Test
    void getRankingRecord() throws BadPaddingException, InterruptedException, IOException, ExecutionException, IllegalBlockSizeException {
        long l = currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            out.println(service.getRankingRecord(i));
        }
        out.println(currentTimeMillis() - l);
    }

    private String initContent(int page) {
        return utils.basicRequest() + "&page=" + page;
    }

    @Test
    void async() throws BadPaddingException, IllegalBlockSizeException {
        long l = currentTimeMillis();
        ArrayList<Future<HttpResponse>> futures = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            HttpPost httpPost = utils.buildHttpRequest("https://saki-server.happyelements.cn/get/events/point_ranking", initContent(i));
            Future<HttpResponse> execute = asyncClient.execute(httpPost, null);
            futures.add(execute);
        }
        byte[] bytes = new byte[30 * 1000];
        futures.forEach(future -> {
            try {
                InputStream content = future.get().getEntity().getContent();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(content);
                int read = bufferedInputStream.read(bytes);
                byte[] bytes1 = Arrays.copyOf(bytes, read);
                new MessagePack().read(CryptoUtils.decrypt(bytes1));
                bufferedInputStream.close();
            } catch (IOException | InterruptedException | ExecutionException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        });
    }

    @Test
    void getCount() throws
            InterruptedException, ExecutionException, BadPaddingException, IllegalBlockSizeException, IOException {
        Integer count = service.getPointRewardCount(600 * 10000);
        out.println(count);
    }

    @Test
    void testOnMessage() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("/pr count 第一张一卡");
        commands.add("/pr count 第一张二卡");
        commands.add("/pr count 第一张三卡");
        commands.add("/pr count 第一张四卡");
        commands.add("/pr count 第一张满破");
        commands.add("/pr count 第二张一卡");
        commands.add("/pr count 第二张二卡");
        commands.add("/pr count 第二张三卡");
        commands.add("/pr count 第二张四卡");
        commands.add("/pr count 第二张满破");
        commands.add("/pr count 一卡");
        commands.add("/pr count 二卡");
        commands.add("/pr count 三卡");
        commands.add("/pr count 四卡");
        commands.add("/pr count 满破");
        commands.add("/pr now");
        commands.add("/pr ???");
        commands.add("/pr count 神秘东西");
        commands.forEach(s ->
                out.println(service.onMessage(s.split(" ")))
        );
    }
}