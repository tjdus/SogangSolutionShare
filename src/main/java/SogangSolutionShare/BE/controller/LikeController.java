package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
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
    public ResponseEntity<Void> createQuestionLike(@RequestBody QuestionLikeDTO questionLikeDTO) {
        // 좋아요 생성 API
        likeService.createQuestionLike(questionLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/question")
    public ResponseEntity<Void> deleteQuestionLike(@RequestBody QuestionLikeDTO questionLikeDTO) {
        // 좋아요 생성 API
        likeService.deleteQuestionLike(questionLikeDTO);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/answer")
    public ResponseEntity<Void> createAnswerLike(@RequestBody AnswerLikeDTO answerLikeDTO) {
        // 좋아요 생성 API
        likeService.createAnswerLike(answerLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/answer")
    public ResponseEntity<Void> deleteAnswerLike(@RequestBody AnswerLikeDTO answerLikeDTO) {
        // 좋아요 생성 API
        likeService.deleteAnswerLike(answerLikeDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Boolean> isUserQuestionLike(@Login Member loginMember, @PathVariable Long questionId) {
        // 로그인한 사용자가 해당 질문에 좋아요를 눌렀는지 여부
        return ResponseEntity.ok(likeService.isUserQuestionLike(loginMember, questionId));
    }

    @GetMapping("/answer/{answerId}")
    public ResponseEntity<Boolean> isUserAnswerLike(@Login Member loginMember, @PathVariable Long answerId) {
        // 로그인한 사용자가 해당 답변에 좋아요를 눌렀는지 여부
        return ResponseEntity.ok(likeService.isUserAnswerLike(loginMember, answerId));
    }
}
