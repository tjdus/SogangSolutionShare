package SogangSolutionShare.BE.domain.dto;

import lombok.Data;

@Data
public class LikeCountResponseDTO {
    private Long likeCount;
    private Long dislikeCount;

    public LikeCountResponseDTO(Long questionLikeCount, Long questionDislikeCount) {
        this.likeCount = questionLikeCount;
        this.dislikeCount = questionDislikeCount;
    }
}
