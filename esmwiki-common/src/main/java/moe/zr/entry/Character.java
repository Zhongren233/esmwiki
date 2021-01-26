package moe.zr.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "character")
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    String name;
    String furigana;
    String image;
    String unit;
    String cvName;
    String birthday;
    String birthMonth;
    String bloodType;
    Integer height;
    Integer weight;
    String hobby;
    String specialSkill;
    String circle;
}
