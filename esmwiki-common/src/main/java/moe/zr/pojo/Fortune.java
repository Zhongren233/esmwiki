package moe.zr.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Fortune {
    @Id
    private String _id;
    private String name;
    private String unsuitable;
    private String suitable;

}
