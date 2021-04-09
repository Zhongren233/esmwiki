package moe.zr.esmwiki.producer.service.impl;

import moe.zr.qqbot.entry.Message;
import moe.zr.service.SudoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SudoServiceImplTest {
    @Autowired
    SudoService sudoService;
    @Test
    void onMessage() {
        Message message = new Message();
        message.setGroupId(1L);
        message.setRawMessage("/sudo /stk 70001701 -id");
        System.out.println(sudoService.onMessage(message));
    }
}