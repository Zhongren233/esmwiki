package moe.zr.esmwiki.producer.util;

import moe.zr.entry.Card;
import moe.zr.vo.CardVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.ws.rs.WebApplicationException;
import java.net.HttpURLConnection;

public class CardVOUtils {
    public static PageRequest parsePageRequest(CardVO cardVO) {
        if (cardVO.getPage() == null) cardVO.setPage(1);
        if (cardVO.getSize() == null) cardVO.setSize(20);

        if (cardVO.getSize() < 1 || cardVO.getPage() < 1)
            throw new WebApplicationException(HttpURLConnection.HTTP_BAD_REQUEST);

        Sort sort=Sort.unsorted();

        if (cardVO.getSort() != null) {
            try {
                Card.class.getDeclaredField(cardVO.getSort());
                sort = Sort.by(cardVO.getSort());
            } catch (NoSuchFieldException ignored) { }
        }


        return PageRequest.of(cardVO.getPage() - 1,
                cardVO.getSize(),
                sort
        );
    }
}
