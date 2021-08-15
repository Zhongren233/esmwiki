package moe.zr.service;

import org.springframework.util.concurrent.ListenableFuture;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public interface EventService {

    ListenableFuture<Long> saveAllPointRanking() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException, TimeoutException;

    ListenableFuture<Long> saveAllScoreRanking() throws BadPaddingException, InterruptedException, ParseException, IOException, ExecutionException, IllegalBlockSizeException, TimeoutException;

}
