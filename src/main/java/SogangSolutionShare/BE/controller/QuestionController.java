package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    // 질문 작성 API
    @PostMapping
    public ResponseEntity<Void> createQuestion(@ModelAttribute QuestionDTO questionDTO) {
        log.info("QuestionDTO: {}", questionDTO);
        questionService.createQuestion(questionDTO);
        return ResponseEntity.ok().build();
    }
    // 질문 조회 API
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("questionId") Long questionId) {
        QuestionDTO questionDTO = questionService.findQuestion(questionId);
        return ResponseEntity.ok(questionDTO);
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

    // 질문 전체 조회
    // sort와 pagination 기능 구현
    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "latest") String orderBy,
            @RequestParam(name = "category", required = false) Optional<String> category,
            @RequestParam(name = "tags", required = false) Optional<List<String>> tags) {
        Sort sort = switch (orderBy) {
            case "most-liked" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<QuestionDTO> questions = questionService.findQuestions(category, tags, pageable);
        return ResponseEntity.ok(questions);
    }
}
