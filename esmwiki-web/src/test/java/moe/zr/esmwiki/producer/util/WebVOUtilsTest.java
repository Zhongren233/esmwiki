package moe.zr.esmwiki.producer.util;

import moe.zr.entry.WorkCostume;
import moe.zr.vo.WebVO;
import org.junit.jupiter.api.Test;

class WebVOUtilsTest {

    @Test
    void buildPage() {
        WebVO<Object> objectWebVO = new WebVO<>();
        objectWebVO.setSize(30);
        objectWebVO.setPage(1);
        System.out.println("WebVOUtils.buildPage(null,60,objectWebVO).getTotalPage() = " + WebVOUtils.buildPage(null, 61, objectWebVO).getTotalPage());
    }

    @Test
    void test() throws NoSuchFieldException {
        System.out.println("WorkCostume.class.getField(\"passion\") = " + WorkCostume.class.getDeclaredField("passion"));
    }
}