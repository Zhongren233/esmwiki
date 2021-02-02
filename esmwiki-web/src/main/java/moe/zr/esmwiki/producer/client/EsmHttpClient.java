package moe.zr.esmwiki.producer.client;

import moe.zr.esmwiki.producer.util.CryptoUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;

@Component
public class EsmHttpClient {
    final
    CloseableHttpClient httpClient;

    public EsmHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Value execute(HttpPost post) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] tmp = new byte[30 * 1000];
        CloseableHttpResponse execute = httpClient.execute(post);
        int statusCode = execute.getStatusLine().getStatusCode();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(execute.getEntity().getContent());
        int read = bufferedInputStream.read(tmp);
        tmp = Arrays.copyOf(tmp, read);
        Value value = new MessagePack().read(CryptoUtils.decrypt(tmp));
        if (statusCode!=200) {
            throw new RuntimeException(value.toString());
        }
        /*
            close!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        execute.close();
        return value;
    }

}
