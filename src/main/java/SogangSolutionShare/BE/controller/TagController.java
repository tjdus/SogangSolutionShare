package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.TagRequestDTO;
import SogangSolutionShare.BE.service.QuestionService;
import SogangSolutionShare.BE.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

    private final QuestionService questionService;
    private final TagService tagService;
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

    @PostMapping("/tag")
    public ResponseEntity<Void> createTag(@Login Member loginMember, @RequestBody TagRequestDTO tagReqeustDTO) {
        tagService.createTag(loginMember.getId(), tagReqeustDTO.getTags());
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/tag/{tagName}")
    public ResponseEntity<Void> deleteTag(@Login Member loginMember, @PathVariable("tagName") String tagName) {
        tagService.deleteTag(loginMember.getId(), tagName);
        return ResponseEntity.ok().build();
    }
}
