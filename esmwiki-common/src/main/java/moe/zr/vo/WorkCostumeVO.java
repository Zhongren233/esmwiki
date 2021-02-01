package moe.zr.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.zr.entry.WorkCostume;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WorkCostumeVO extends WebVO<WorkCostume> {
    private Integer page;
    private Integer size;
    private String sort;
    private WorkCostume data;
    private List<String> select;
}
