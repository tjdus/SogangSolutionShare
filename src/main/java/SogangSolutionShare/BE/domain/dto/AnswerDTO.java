package SogangSolutionShare.BE.domain.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private Long questionId;
    private Long memberId;
    private String content;

}
