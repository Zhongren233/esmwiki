package moe.zr.esmwiki.producer.util;

import lombok.extern.slf4j.Slf4j;
import moe.zr.esmwiki.producer.config.DAQConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.UUID;

@Component
@Slf4j
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
        return "msg_id=" +UUID.randomUUID()+
                "&major="+major +
                "&resMd5="+resMd5 +
                "&packageName=apple" + //这俩也不能动
                "&platform=iOS" + //这俩也不能动
                "&session="+config.getSession() +
                "&hei_token="+config.getToken() +
                "&login_type=mobile"; //这个不能动
    }

    public HttpPost buildHttpRequest(String uri, String content) throws BadPaddingException, IllegalBlockSizeException {
        HttpPost httpPost = new HttpPost(uri);
        log.info(uri);
        log.info(content);
        addHeader(httpPost);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(CryptoUtils.encrypt(content));
        httpPost.setEntity(byteArrayEntity);
        return httpPost;
    }

    private void addHeader(HttpPost request) {
        request.addHeader("Authorization", "Token " + config.getToken());
        request.addHeader("Content-Type", "application/octet-stream");
        request.addHeader("X-Game-Version", major);//？这啥啊
    }

}
