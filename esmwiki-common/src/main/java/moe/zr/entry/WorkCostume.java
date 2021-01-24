package moe.zr.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "workCostume")
public class WorkCostume implements Serializable {
    private static final long serialVersionUID = -3848147787378010808L;


    @Id
    String id;
    String costumeName;
    String image;
    String character;
    String series;
    Integer active;
    Integer passion;
    Integer unique;
    Integer smart;
    Integer technique;
    Integer charisma;
    String appendDate;
    String howToGet;

}
