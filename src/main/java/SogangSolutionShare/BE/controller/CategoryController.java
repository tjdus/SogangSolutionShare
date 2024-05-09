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
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final QuestionService questionService;
    @GetMapping("/{categoryName}/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestionsByCategory(
            @PathVariable("categoryName") String category,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "latest") String orderBy,
            @RequestParam(name = "tags", required = false) Optional<List<String>> tags){

        Sort sort = switch (orderBy) {
            case "most-liked" -> Sort.by(Sort.Direction.DESC, "likeCount");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<QuestionDTO> questions = questionService.findQuestions(Optional.of(category), tags, pageable);
        return ResponseEntity.ok(questions);

    }
}
