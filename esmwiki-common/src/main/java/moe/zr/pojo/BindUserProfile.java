package moe.zr.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
@Data
public class BindUserProfile {
    @Id
   private String _id;
   private Long qqNumber;
   private Integer userId;
}
