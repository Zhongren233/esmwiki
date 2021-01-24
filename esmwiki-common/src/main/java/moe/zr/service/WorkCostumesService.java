package moe.zr.service;


import moe.zr.entry.WorkCostume;
import moe.zr.vo.Page;
import moe.zr.vo.WebVO;
import moe.zr.vo.WorkCostumeVO;

public interface WorkCostumesService {

    Page queryPageByCardVO(WorkCostumeVO workCostumeVo);


}