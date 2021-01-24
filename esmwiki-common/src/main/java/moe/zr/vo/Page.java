package moe.zr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page{
    Long totalPage;
    Long currentPage;
    List<?> data;

    public Page(org.springframework.data.domain.Page<?> page){
        data = page.getContent();
        totalPage= (long) page.getTotalPages();
        currentPage = (long) (page.getNumber() + 1);
    }
}
