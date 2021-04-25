package moe.zr.qqbot.entry;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessage {
    Long groupId = 773891409L;
    String message;
}
