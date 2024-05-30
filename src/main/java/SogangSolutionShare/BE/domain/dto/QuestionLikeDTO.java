package SogangSolutionShare.BE.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class QuestionLikeDTO {
    @NotEmpty(message = "질문 ID는 필수 입력 값입니다.")
    private Long questionId;
}
