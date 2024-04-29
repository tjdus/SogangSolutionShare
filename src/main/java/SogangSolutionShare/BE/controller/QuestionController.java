package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;


    // 질문 작성 API
    @PostMapping("/question")
    public ResponseEntity<Void> createQuestion(@ModelAttribute QuestionDTO questionDTO) {
        questionService.createQuestion(questionDTO);
        return ResponseEntity.ok().build();
    }

    // 질문 수정 API
    @PostMapping("/question/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long questionId, @ModelAttribute QuestionDTO questionDTO) {
        questionService.updateQuestion(questionId, questionDTO);
        return ResponseEntity.ok().build();
    }

    // 질문 삭제 API

    // 질문에 답변 작성 API
}
