package moe.zr.esmwiki.producer.controller;

import moe.zr.service.WorkCostumesService;
import moe.zr.vo.Page;
import moe.zr.vo.WorkCostumeVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workCostumes")
public class WorkCostumesController {
    final
    WorkCostumesService service;

    public WorkCostumesController(WorkCostumesService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Page getCards(@RequestBody WorkCostumeVO workCostumeVO){
        System.out.println(workCostumeVO);
        return service.queryPageByCardVO(workCostumeVO);
    }
}
