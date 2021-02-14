package moe.zr.qqbot.entry;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessage {
    Integer groupId = 773891409;
    String message;
}
