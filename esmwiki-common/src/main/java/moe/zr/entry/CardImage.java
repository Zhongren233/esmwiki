package moe.zr.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CardImage implements Serializable {
    private static final long serialVersionUID = -7849510417474244654L;
    private String normalImage;
    private String evolutionImage;
}
