package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.AnswerLikeDTO;
import SogangSolutionShare.BE.domain.dto.LikeCountResponseDTO;
import SogangSolutionShare.BE.domain.dto.QuestionLikeDTO;
import SogangSolutionShare.BE.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like/question")
    public ResponseEntity<Void> createQuestionLike(@Login Member loginMember, @RequestBody QuestionLikeDTO questionLikeDTO) {
        // 좋아요 생성 API
        likeService.createQuestionLike(loginMember, questionLikeDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike/question")
    public ResponseEntity<Void> createQuestionDislike(@Login Member loginMember, @RequestBody QuestionLikeDTO questionLikeDTO) {
        // 싫어요 생성 API
        likeService.createQuestionDislike(loginMember, questionLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like/question")
    public ResponseEntity<Void> deleteQuestionLike(@Login Member loginMember, @RequestBody QuestionLikeDTO questionLikeDTO) {
        // 좋아요 삭제 API
        likeService.deleteQuestionLike(loginMember, questionLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dislike/question")
    public ResponseEntity<Void> deleteQuestionDislike(@Login Member loginMember, @RequestBody QuestionLikeDTO questionLikeDTO) {
        // 싫어요 삭제 API
        likeService.deleteQuestionDislike(loginMember, questionLikeDTO);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/like/answer")
    public ResponseEntity<Void> createAnswerLike(@Login Member loginMember, @RequestBody AnswerLikeDTO answerLikeDTO) {
        // 좋아요 생성 API
        likeService.createAnswerLike(loginMember, answerLikeDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike/answer")
    public ResponseEntity<Void> createAnswerDislike(@Login Member loginMember, @RequestBody AnswerLikeDTO answerLikeDTO) {
        // 싫어요 생성 API
        likeService.createAnswerDislike(loginMember, answerLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like/answer")
    public ResponseEntity<Void> deleteAnswerLike(@Login Member loginMember, @RequestBody AnswerLikeDTO answerLikeDTO) {
        // 좋아요 삭제 API
        likeService.deleteAnswerLike(loginMember, answerLikeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dislike/answer")
    public ResponseEntity<Void> deleteAnswerDislike(@Login Member loginMember, @RequestBody AnswerLikeDTO answerLikeDTO) {
        // 싫어요 삭제 API
        likeService.deleteAnswerDislike(loginMember, answerLikeDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/like/question/{questionId}")
    public ResponseEntity<Long> isUserQuestionLike(@Login Member loginMember, @PathVariable Long questionId) {
        // 로그인한 사용자가 해당 질문에 좋아요를 눌렀는지 여부
        return ResponseEntity.ok(likeService.isUserQuestionLike(loginMember, questionId));
    }

    @GetMapping("/like/answer/{answerId}")
    public ResponseEntity<Long> isUserAnswerLike(@Login Member loginMember, @PathVariable Long answerId) {
        // 로그인한 사용자가 해당 답변에 좋아요를 눌렀는지 여부
        return ResponseEntity.ok(likeService.isUserAnswerLike(loginMember, answerId));
    }

    @GetMapping("like/question/count")
    public ResponseEntity<LikeCountResponseDTO> getQuestionLikeCount(@RequestParam Long questionId) {
        // 질문의 좋아요 수 조회
        return ResponseEntity.ok(new LikeCountResponseDTO(likeService.getQuestionLikeCount(questionId), likeService.getQuestionDislikeCount(questionId)));
    }

    @GetMapping("like/answer/count")
    public ResponseEntity<LikeCountResponseDTO> getAnswerLikeCount(@RequestParam Long answerId) {
        // 답변의 좋아요 수 조회
        return ResponseEntity.ok(new LikeCountResponseDTO(likeService.getAnswerLikeCount(answerId), likeService.getAnswerDislikeCount(answerId)));
    }
}
