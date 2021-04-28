package moe.zr.qqbot.entry;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Message {
    Long groupId;
    Long messageId;
    Integer messageSeq;
    String messageType;
    String postType;
    String rawMessage;
    Long selfId;
    String subType;
    Long Time;
    Long userId;

    @Override
    public String toString() {
        return "Message{" +
                "groupId=" + groupId +
                ", messageType='" + messageType + '\'' +
                ", rawMessage='" + rawMessage + '\'' +
                ", Time=" + Time +
                ", userId=" + userId +
                '}';
    }
}
