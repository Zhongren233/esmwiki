package moe.zr.service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.concurrent.ExecutionException;

public interface MyPageService {
    void getMyPage() throws BadPaddingException, IllegalBlockSizeException, ExecutionException, InterruptedException;
}