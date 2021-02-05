package moe.zr.esmwiki.producer.util;

import com.fasterxml.jackson.databind.JsonNode;
import moe.zr.pojo.RankingRecord;

public class ParseUtils {
    public static RankingRecord getRecord(JsonNode jsonNode) {
        String currentTime = jsonNode.get("current_time").textValue();
        String date = currentTime.substring(0, 16);
        JsonNode ranking = jsonNode.get("ranking");
        JsonNode x = ranking.get(0);
        int point = x.get("point").asInt();
        int rank = x.get("rank").asInt();
        int eventId = jsonNode.get("eventId").asInt();
        return new RankingRecord(null, date, eventId, point, rank);
    }
}
