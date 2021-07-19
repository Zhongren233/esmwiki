package moe.zr.esmwiki.producer.util;

import org.junit.jupiter.api.Test;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void decrypt() throws IOException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Zhong\\Documents\\0");
        byte[] bytes = fileInputStream.readAllBytes();
        byte[] decrypt = CryptoUtils.decrypt(bytes);
        MessagePack messagePack = new MessagePack();
        Value read = messagePack.read(decrypt);
        System.out.println(read);
    }
}