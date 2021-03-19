package moe.zr.esmwiki.producer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
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

}