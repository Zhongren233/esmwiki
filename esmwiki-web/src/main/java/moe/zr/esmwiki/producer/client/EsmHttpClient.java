package moe.zr.esmwiki.producer.client;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class EsmHttpClient {
    final
    CloseableHttpAsyncClient httpClient;

    public EsmHttpClient(CloseableHttpAsyncClient client) {
        this.httpClient = client;
    }

    public Value executeAsMessagepack(HttpPost post) throws IOException, BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        byte[] tmp = new byte[30 * 1000];
        Future<HttpResponse> execute = httpClient.execute(post, null);
        HttpResponse httpResponse = execute.get();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(httpResponse.getEntity().getContent());
        int read = bufferedInputStream.read(tmp);
        tmp = Arrays.copyOf(tmp, read);
        Value value = new MessagePack().read(CryptoUtils.decrypt(tmp));
        if (statusCode != 200) {
            throw new RuntimeException(value.toString());
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
            throw new RuntimeException("状态码不为200");
        }
    }


}
