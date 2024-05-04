package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.AnswerLikeDTO;
import SogangSolutionShare.BE.domain.dto.QuestionLikeDTO;
import SogangSolutionShare.BE.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/question")
    public ResponseEntity<Void> createQuestionLike(@ModelAttribute QuestionLikeDTO questionLikeDTO) {
        // 좋아요 생성 API
        likeService.createQuestionLike(questionLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/question")
    public ResponseEntity<Void> deleteQuestionLike(@ModelAttribute QuestionLikeDTO questionLikeDTO) {
        // 좋아요 생성 API
        likeService.deleteQuestionLike(questionLikeDTO);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/answer")
    public ResponseEntity<Void> createAnswerLike(@ModelAttribute AnswerLikeDTO answerLikeDTO) {
        // 좋아요 생성 API
        likeService.createAnswerLike(answerLikeDTO);
        return ResponseEntity.ok().build();
    }
}
