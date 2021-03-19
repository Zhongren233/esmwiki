package moe.zr.esmwiki.producer.service.impl;

import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import moe.zr.service.MyPageService;

@SpringBootTest
class MyPageServiceImplTest {
    @Autowired
    MyPageService service;

    @Test
    void getMyPage() throws IllegalBlockSizeException, ExecutionException, InterruptedException, BadPaddingException {
        service.getMyPage();
    }
}