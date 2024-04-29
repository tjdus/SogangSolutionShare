package SogangSolutionShare.BE.domain.dto;

import lombok.Data;

@Data
public class QuestionDTO {
    private Long memberId;
    private String title;
    private String content;
}
