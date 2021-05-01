package moe.zr.esmwiki.producer.service.impl;

import moe.zr.service.FriendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FriendServiceImplTest {
    @Autowired
    FriendService friendService;
    @Test
    void findUserIdByQuery() throws IllegalBlockSizeException, ExecutionException, BadPaddingException, IOException {
        System.out.println(friendService.findUserIdByQuery("雪泉"));
    }

}