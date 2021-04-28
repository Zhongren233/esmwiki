package moe.zr.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
@Data
public class Strategy {
    @Id
    String _id;
    String prefix;
    String message;
    Boolean hide;
    Boolean isGroup;
}
