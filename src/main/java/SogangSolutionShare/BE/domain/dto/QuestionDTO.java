package SogangSolutionShare.BE.domain.dto;

import SogangSolutionShare.BE.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String loginId;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private List<String> attachments;
    private Long likeCount;
    private Long dislikeCount;
    private Long viewCount;
    private Long answerCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
