package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.WorkCostume;
import moe.zr.esmwiki.producer.util.WebVOUtils;
import moe.zr.service.WorkCostumesService;
import moe.zr.vo.WorkCostumeVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@DubboService
@Component
@Path("/api/work/costumes")
public class WorkCostumesServiceImpl implements WorkCostumesService {
    final MongoTemplate template;

    public WorkCostumesServiceImpl(MongoTemplate template) {
        this.template = template;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public moe.zr.vo.Page queryPageByCardVO(WorkCostumeVO workCostumeVo) {
        PageRequest pageRequest = WebVOUtils.parsePageRequest(workCostumeVo,WorkCostume.class);
        WorkCostume data = workCostumeVo.getData();
        Query query = new Query();
        if (data != null) {
            Criteria criteria = Criteria.byExample(Example.of(data));
            query.addCriteria(criteria);
        }
        List<String> select = workCostumeVo.getSelect();
        if (select != null ) {
            if (select.size() > 3) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            else for (String s : select) query.addCriteria(Criteria.where(s).ne(0));
        }
        long count = template.count(query, WorkCostume.class);
        query.with(pageRequest);
        List<WorkCostume> cards = template.find(query, WorkCostume.class);
        return WebVOUtils.buildPage(cards, count, workCostumeVo);
    }
}
