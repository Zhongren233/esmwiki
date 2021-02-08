package moe.zr.esmwiki.producer.util;

import moe.zr.esmwiki.producer.config.DAQConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.UUID;

@Component
public class RequestUtils {
    final
    DAQConfig config;
    @Value("${es.major}")
    private String major;
    @Value("${es.resMd5}")
    private String resMd5;

    public RequestUtils(DAQConfig config) {
        this.config = config;
    }

    public String basicRequest() {
        return "msg_id=" + UUID.randomUUID().toString() +
                "&major=" + major +
                "&resMd5=" + resMd5 +
                "&packageName=apple&platform=iOS" +   //这玩意不能动
                "&login_type=mobile" +
                "&heiToken=" + config.getToken() +
                "&session=" + config.getSession() +
                "&login_type=mobile";
    }

    public HttpPost buildHttpRequest(String uri, String content) throws BadPaddingException, IllegalBlockSizeException {
        HttpPost httpPost = new HttpPost(uri);
        addHeader(httpPost);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(CryptoUtils.encrypt(content));
        httpPost.setEntity(byteArrayEntity);
        return httpPost;
    }

    private void addHeader(HttpPost request) {
        request.addHeader("Authorization", "Token " + config.getToken());
        request.addHeader("Content-Type", "application/octet-stream");
    }

}
