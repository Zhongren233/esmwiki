package moe.zr.esmwiki.producer.service.impl;

import moe.zr.vo.CardVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.ws.rs.WebApplicationException;
import java.net.HttpURLConnection;

public class CardVOUtils {
    public static PageRequest parsePageRequest(CardVO cardVO) {
        if (cardVO.getSize() < 1 || cardVO.getPage() < 1)
            throw new WebApplicationException(HttpURLConnection.HTTP_BAD_REQUEST);
        Sort sort;
        if (cardVO.getSort() != null)
            sort = Sort.by(cardVO.getSort());
        else
            sort = Sort.unsorted();

        System.out.println(sort);
        return PageRequest.of(cardVO.getPage() - 1,
                cardVO.getSize(),
                sort
        );
    }
}
