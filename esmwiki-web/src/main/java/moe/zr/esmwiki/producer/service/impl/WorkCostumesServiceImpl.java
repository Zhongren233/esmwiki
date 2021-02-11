package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.WorkCostume;
import moe.zr.esmwiki.producer.util.WebVOUtils;
import moe.zr.service.WorkCostumesService;
import moe.zr.vo.WorkCostumeVO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkCostumesServiceImpl implements WorkCostumesService {
    final MongoTemplate template;

    public WorkCostumesServiceImpl(MongoTemplate template) {
        this.template = template;
    }

    public moe.zr.vo.Page queryPageByCardVO(WorkCostumeVO workCostumeVo) {
        PageRequest pageRequest = WebVOUtils.parsePageRequest(workCostumeVo, WorkCostume.class);
        WorkCostume data = workCostumeVo.getData();
        Query query = new Query();
        if (data != null) {
            Criteria criteria = Criteria.byExample(Example.of(data));
            query.addCriteria(criteria);
        }
        List<String> select = workCostumeVo.getSelect();
        if (select != null) {
            if (select.size() > 3)
                throw new IllegalArgumentException();
            else
                for (String s : select)
                    query.addCriteria(Criteria.where(s).ne(0));
        }
        long count = template.count(query, WorkCostume.class);
        query.with(pageRequest);
        List<WorkCostume> cards = template.find(query, WorkCostume.class);
        return WebVOUtils.buildPage(cards, count, workCostumeVo);
    }
}
