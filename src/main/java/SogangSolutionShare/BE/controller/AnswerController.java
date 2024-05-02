package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;

    // 질문에 답변 작성 API
    @PostMapping
    public ResponseEntity<Void> createAnswer(@ModelAttribute AnswerDTO answerDTO) {
        log.info("AnswerDTO: {}", answerDTO);
        answerService.createAnswer(answerDTO);

        return ResponseEntity.ok().build();
    }

    // 질문에 답변 수정 API
    @PatchMapping("/{answerId}")
    public ResponseEntity<Void> updateAnswer(@PathVariable("answerId") Long answerId, String content) {

        answerService.updateAnswer(answerId, content);

        return ResponseEntity.ok().build();
    }
}
