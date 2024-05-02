package SogangSolutionShare.BE.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long memberId;
    private String categoryName;
    private String title;
    private String content;

    private List<String> tags;
}
