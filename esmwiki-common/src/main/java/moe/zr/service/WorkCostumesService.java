package moe.zr.service;


import moe.zr.vo.Page;
import moe.zr.vo.WorkCostumeVO;

public interface WorkCostumesService {

    Page queryPageByCardVO(WorkCostumeVO workCostumeVo);


}