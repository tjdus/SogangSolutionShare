package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.QuestionLike;
import SogangSolutionShare.BE.domain.Tag;
import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.TagDTO;
import SogangSolutionShare.BE.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ScrapService scrapService;
    private final TagService tagService;

    // 내가 작성한 질문 조회 API
    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @Login Member loginMember,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<QuestionDTO> questions = questionService.findQuestionsByMemberId(loginMember.getId(), criteria);
        return ResponseEntity.ok(questions);
    }

    // 내가 작성한 답변 조회 API
    @GetMapping("/answers")
    public ResponseEntity<Page<AnswerDTO>> getAnswers(
            @Login Member loginMember,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<AnswerDTO> answers = answerService.findAnswersByMemberId(loginMember.getId(), criteria);
        return ResponseEntity.ok(answers);
    }

    // 내가 좋아요를 누른 질문 조회 API
    @GetMapping("/like/questions")
    public ResponseEntity<Page<QuestionDTO>> getLikeQuestions(
            @Login Member loginMember,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<Question> questions = questionService.findLikeQuestionsByMemberId(loginMember.getId(), criteria).map(QuestionLike::getQuestion);
        return ResponseEntity.ok(questions.map(Question::toDTO));
    }

    // 내가 좋아요를 누른 답변 조회 API
    @GetMapping("/like/answers")
    public ResponseEntity<Page<AnswerDTO>> getLikeAnswers(
            @Login Member loginMember,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<AnswerDTO> answers = answerService.findLikeAnswersByMemberId(loginMember.getId(), criteria);
        return ResponseEntity.ok(answers);
    }

    // 내가 스크랩한 질문 조회 API
    @GetMapping("/scrap/questions")
    public ResponseEntity<Page<QuestionDTO>> getScrapQuestions(
            @Login Member loginMember,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<QuestionDTO> scrapQuestions = scrapService.findScrapQuestionsByMemberId(loginMember.getId(), criteria);
        return ResponseEntity.ok(scrapQuestions);
    }

    // 내가 작성한 태그 조회 API
    @GetMapping("/tags")
    public ResponseEntity<Page<TagDTO>> getTags(
            @Login Member loginMember,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<Tag> tags = tagService.findTagsByMemberId(loginMember.getId(), criteria);
        return ResponseEntity.ok(tags.map(Tag::toDTO));
    }


}
