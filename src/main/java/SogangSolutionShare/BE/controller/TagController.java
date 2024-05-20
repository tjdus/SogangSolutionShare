package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final QuestionService questionService;
    @GetMapping("/tag/{tagId}/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestionsByTagId(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy,
            @PathVariable("tagId") Long tagId){

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<QuestionDTO> questions = questionService.findQuestionsByTagId(criteria, tagId);
        return ResponseEntity.ok(questions);

    }

    @GetMapping("/tagged/{tags}")
    public ResponseEntity<Page<QuestionDTO>> getQuestionsByTags(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy,
            @PathVariable("tags") String tags) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);
        Page<QuestionDTO> questions = questionService.findQuestionsByTags(criteria, tags);
        return ResponseEntity.ok(questions);
    }
}
