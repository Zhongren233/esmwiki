package moe.zr.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Car {
    @Id
    private String _id;
    private Long qqNumber;
    private String text;
    private Long time;
}
