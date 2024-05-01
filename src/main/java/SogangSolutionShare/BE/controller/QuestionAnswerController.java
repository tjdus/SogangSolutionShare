package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.AnswerService;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;


    // 질문 작성 API
    @PostMapping
    public ResponseEntity<Void> createQuestion(@ModelAttribute QuestionDTO questionDTO) {
        log.info("QuestionDTO: {}", questionDTO);
        questionService.createQuestion(questionDTO);
        return ResponseEntity.ok().build();
    }

    // 질문 수정 API
    @PatchMapping("/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable("questionId") Long questionId, @ModelAttribute QuestionDTO questionDTO) {
        questionService.updateQuestion(questionId, questionDTO);
        return ResponseEntity.ok().build();
    }

    // 질문 삭제 API
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }

    // 질문에 답변 작성 API
    @PostMapping("/{questionId}/answer")
    public ResponseEntity<Void> createAnswer(@PathVariable("questionId") Long questionId, @ModelAttribute AnswerDTO answerDTO) {
        log.info("AnswerDTO: {}", answerDTO);
        answerService.createAnswer(questionId, answerDTO);

        return ResponseEntity.ok().build();
    }

    // 질문에 답변 수정 API
    @PatchMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Void> updateAnswer(@PathVariable("questionId") Long questionId, @PathVariable Long answerId, String content) {

        answerService.updateAnswer(questionId, answerId, content);

        return ResponseEntity.ok().build();
    }
}
