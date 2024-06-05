package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.SearchCriteria;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final QuestionService questionService;
    @GetMapping
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "type", defaultValue = "title") String type){

        SearchCriteria searchCriteria = new SearchCriteria(page-1, size, orderBy, q, type);
        Page<QuestionDTO> questions = questionService.searchQuestions(searchCriteria);
        return ResponseEntity.ok(questions);
    }
}
