package moe.zr.esmwiki.producer.util;


import moe.zr.vo.Page;
import moe.zr.vo.WebVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class WebVOUtils {
    public static PageRequest parsePageRequest(WebVO<?> webVO,Class<?> c) {
        if (webVO.getPage() == null) webVO.setPage(1);
        if (webVO.getSize() == null) webVO.setSize(20);

        if (webVO.getSize() < 1 || webVO.getPage() < 1)
            throw new IllegalArgumentException("不合法的参数");

        Sort sort = Sort.unsorted();

        if (webVO.getSort() != null) {
            try {
                String strsort = webVO.getSort();
                System.out.println(strsort);
                c.getDeclaredField(strsort);
                sort = Sort.by(strsort).descending();
            } catch (NoSuchFieldException ignored) {
            }
        }

        return PageRequest.of(webVO.getPage() - 1,
                webVO.getSize(),
                sort
        );
    }

    public static Page buildPage(List<?> list, long count, WebVO<?> webVO) {
        Page page = new Page();
        long totalPage = (long) Math.ceil((double) count / webVO.getSize());
        page.setTotalPage(totalPage);
        page.setCurrentPage(((long) webVO.getPage()));
        page.setData(list);
        return page;
    }

}
