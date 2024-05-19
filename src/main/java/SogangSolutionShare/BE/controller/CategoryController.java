package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.CategoryService;
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
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestParam("name") String name) {
        categoryService.createCategory(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categoryId}/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestionsByCategory(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy,
            @PathVariable("categoryId") Long categoryId){

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<QuestionDTO> questions = questionService.findQuestionsByCategoryId(criteria, categoryId);
        return ResponseEntity.ok(questions);

    }
}
