package SogangSolutionShare.BE.domain.dto;

import SogangSolutionShare.BE.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionDTO {
    private Long memberId;
    private String categoryName;
    private String title;
    private String content;
    private Long likeCount;
    private List<String> tags;


}
