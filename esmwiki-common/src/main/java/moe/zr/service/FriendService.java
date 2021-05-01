package moe.zr.service;

import moe.zr.entry.hekk.Friend;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface FriendService {
    Friend findUserIdByQuery(String query) throws BadPaddingException, IllegalBlockSizeException, IOException, ExecutionException;
}
