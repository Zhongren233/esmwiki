package moe.zr.esmwiki.producer.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class CryptoUtils {
    public static Cipher deCryptoCipher = null;
    public static Cipher enCryptoCipher = null;

    static {
        Security.addProvider(new BouncyCastleProvider());
        String data = "saki#*e49x%tt-7m%P/+g";
        byte[] md5 = DigestUtils.md5(data);
        byte[] sha1 = DigestUtils.sha1(data);
        byte[] key = new byte[16];
        byte[] iv = new byte[16];
        System.arraycopy(md5, 0, key, 0, 16);
        System.arraycopy(sha1, 0, key, 8, 8);
        System.arraycopy(sha1, 0, iv, 0, 16);
        try {
            deCryptoCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            enCryptoCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        try {
            assert deCryptoCipher != null;
            deCryptoCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            enCryptoCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public static byte[] decrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        return deCryptoCipher.doFinal(bytes);
    }

    public static byte[] encrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        return enCryptoCipher.doFinal(bytes);
    }

    public static byte[] encrypt(String s) throws BadPaddingException, IllegalBlockSizeException {
        return encrypt(s.getBytes(StandardCharsets.UTF_8));
    }
}
