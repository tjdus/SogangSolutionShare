package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.QuestionRequestDTO;
import SogangSolutionShare.BE.service.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    // 질문 작성 API
    @PostMapping("/question")
    public ResponseEntity<QuestionDTO> createQuestion(
            @Login Member loginMember,
            @Valid @ModelAttribute QuestionRequestDTO questionRequestDTO) {

        QuestionDTO createdQuestion = questionService.createQuestion(loginMember.getId(), questionRequestDTO);
        return ResponseEntity.ok(createdQuestion);
    }
    // 질문 조회 API
    @GetMapping("/question/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("questionId") Long questionId, HttpServletRequest request, HttpServletResponse response) {
        QuestionDTO questionDTO = questionService.findQuestionById(questionId);

        Cookie viewCookie = null;
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("View")) {
                viewCookie = cookie;
            }
        }

        if(viewCookie == null) {
            questionService.updateViewCount(questionId);
            Cookie newCookie = new Cookie("View", "|" + questionId + "|");
            response.addCookie(newCookie);
        } else {
            String value = viewCookie.getValue();
            if(!value.contains("|" + questionId + "|")) {
                questionService.updateViewCount(questionId);
                value += "|" + questionId + "|";
                viewCookie.setValue(value);
                response.addCookie(viewCookie);
            }
        }


        return ResponseEntity.ok(questionDTO);
    }

    // 질문 수정 API
    @PatchMapping("/question/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable("questionId") Long questionId,
            @Login Member loginMember,
            @Valid @ModelAttribute QuestionRequestDTO questionRequestDTO) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(loginMember.getId(), questionId, questionRequestDTO);
        return ResponseEntity.ok(updatedQuestion);
    }

    // 질문 삭제 API
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable("questionId") Long questionId,
            @Login Member loginMember) {
        questionService.deleteQuestion(loginMember.getId(), questionId);
        return ResponseEntity.ok().build();
    }

    // 질문 전체 조회
    // sort와 pagination 기능 구현
    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);

        Page<QuestionDTO> questions = questionService.findQuestions(criteria);
        return ResponseEntity.ok(questions);
    }

    // 질문 개수 조회
    @GetMapping("/questions/count")
    public ResponseEntity<Long> getQuestionCount() {
        Long count = questionService.getQuestionCount();
        return ResponseEntity.ok(count);
    }
}
