package moe.zr.esmwiki.producer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import moe.zr.entry.hekk.AppMessage;
import moe.zr.esmwiki.producer.util.CryptoUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@Slf4j
public class EsmHttpClient {
    final
    CloseableHttpAsyncClient httpClient;
    final
    ObjectMapper mapper;

    public EsmHttpClient(CloseableHttpAsyncClient client, ObjectMapper mapper) {
        this.httpClient = client;
        this.mapper = mapper;
    }

    public Value executeAsMessagepack(HttpPost post) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException {
        byte[] tmp = new byte[30 * 1000];
        Future<HttpResponse> execute = httpClient.execute(post, null);
        HttpResponse httpResponse;
        try {
            httpResponse = execute.get();
        } catch (InterruptedException e) {
            log.error("", e);
            throw new RuntimeException("中断异常");
        }
        assert httpResponse != null;
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
        int read = bufferedInputStream.read(tmp);
        tmp = Arrays.copyOf(tmp, read);
        Value value = new MessagePack().read(CryptoUtils.decrypt(tmp));
        if (statusCode != 200) {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            String content = value.toString();
            log.error("请求出现异常，相关AppMessage:{}", content);
            AppMessage appMessage = mapper.readValue(content, AppMessage.class);
            throw new RuntimeException(appMessage.toString());
        }
        /*
            close!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        bufferedInputStream.close();
        return value;
    }

    public void executeNoResponse(HttpPost post) throws ExecutionException, InterruptedException {
        Future<HttpResponse> execute = httpClient.execute(post, null);
        HttpResponse httpResponse = execute.get();
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
                byte[] bytes = new byte[300 * 1000];
                int read = bufferedInputStream.read(bytes);
                bytes = Arrays.copyOf(bytes, read);
                Value read1 = new MessagePack().read(CryptoUtils.decrypt(bytes));
                log.error("状态码不为200,可用的信息:{}",read1.toString());
            } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("状态码不为200,可用的信息已提供在日志");
        }
    }


}
