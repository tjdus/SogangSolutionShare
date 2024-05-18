package SogangSolutionShare.BE.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria extends Criteria {
    private String q;
    private String type;

    public SearchCriteria(Integer page, Integer size, String orderBy, String q, String type) {
        super(page, size, orderBy);
        this.q = q;
        this.type = type;
    }

}
