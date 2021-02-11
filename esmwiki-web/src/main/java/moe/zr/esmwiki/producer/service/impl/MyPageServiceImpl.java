package moe.zr.esmwiki.producer.service.impl;

import moe.zr.esmwiki.producer.client.EsmHttpClient;
import moe.zr.esmwiki.producer.util.RequestUtils;
import moe.zr.service.MyPageService;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.concurrent.ExecutionException;

@Service
public class MyPageServiceImpl implements MyPageService {
    final
    EsmHttpClient httpClient;
    final
    RequestUtils utils;

    public MyPageServiceImpl(EsmHttpClient httpClient, RequestUtils utils) {
        this.httpClient = httpClient;
        this.utils = utils;
    }

    @Override
    public void getMyPage() throws BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException {
        String uri = "https://saki-server.happyelements.cn/get/my_page";
        HttpPost httpPost = utils.buildHttpRequest(uri, utils.basicRequest());
        httpClient.executeNoResponse(httpPost);
    }
}
