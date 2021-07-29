package moe.zr.esmwiki.producer.util;

import com.fasterxml.jackson.databind.JsonNode;
import moe.zr.pojo.RankingRecord;

public class ParseUtils {
    public static RankingRecord getRecord(JsonNode jsonNode, Integer offset) {
        long date = System.currentTimeMillis() / 60000 * 60000;
        JsonNode ranking = jsonNode.get("ranking");
        JsonNode x = ranking.get(offset);
        int point = x.get("point").asInt();
        int rank = x.get("rank").asInt();
        int eventId = jsonNode.get("eventId").asInt();
        return new RankingRecord(date, eventId, point, rank);
    }
}
