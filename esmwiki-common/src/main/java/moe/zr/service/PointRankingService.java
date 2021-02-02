package moe.zr.service;

import com.fasterxml.jackson.databind.JsonNode;
import moe.zr.enums.EventRankingNavigationType;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.text.ParseException;

public interface PointRankingService {
    JsonNode getRankingRecord(Integer page) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException;

    JsonNode getRankingRecord(EventRankingNavigationType type) throws IOException, BadPaddingException, IllegalBlockSizeException, ParseException;


}
