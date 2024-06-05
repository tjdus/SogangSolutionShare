package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Answer;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.domain.dto.AnswerRequestDTO;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.service.AnswerService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService answerService;

    // 질문에 답변 작성 API
    @PostMapping("/question/{questionId}/answer")
    public ResponseEntity<AnswerDTO> createAnswer(
            @Login Member loginMember,
            @PathVariable("questionId") Long questionId,
            @Valid @ModelAttribute AnswerRequestDTO answerRequestDTO) {
        log.info("AnswerDTO: {}", answerRequestDTO);
        AnswerDTO answerDTO = answerService.createAnswer(loginMember.getId(), questionId, answerRequestDTO);

        return ResponseEntity.ok(answerDTO);
    }

    // 질문에 답변 수정 API
    @PatchMapping("/question/{questionId}/answer/{index}")
    public ResponseEntity<AnswerDTO> updateAnswer(
            @Login Member loginMember,
            @PathVariable("questionId") Long questionId,
            @PathVariable("index") Integer index,
            @Valid @ModelAttribute AnswerRequestDTO answerRequestDTO) {

        AnswerDTO answerDTO = answerService.updateAnswer(loginMember.getId(), questionId, index, answerRequestDTO);

        return ResponseEntity.ok(answerDTO);
    }
    // 질문에 답변 조회 API
    @GetMapping("/question/{questionId}/answer/{index}")
    public ResponseEntity<AnswerDTO> getAnswer(
            @PathVariable("questionId") Long questionId,
            @PathVariable("index") Integer index) {

        AnswerDTO answerDTO = answerService.getAnswer(questionId, index);
        return ResponseEntity.ok(answerDTO);
    }

    // 답변 삭제 API
    @DeleteMapping("/question/{questionId}/answer/{index}")
    public ResponseEntity<Void> deleteAnswer(
            @Login Member loginMember,
            @PathVariable("questionId") Long questionId,
            @PathVariable("index") Integer index) {
        answerService.deleteAnswer(loginMember.getId(), questionId, index);
        return ResponseEntity.ok().build();
    }

    //질문에 대한 답변 전체 조회 API
    @GetMapping("/question/{questionId}/answers")
    public ResponseEntity<Page<AnswerDTO>> getAnswers(
            @PathVariable("questionId") Long questionId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "orderBy", defaultValue = "latest") String orderBy) {

        Criteria criteria = new Criteria(page - 1, size, orderBy);
        Page<AnswerDTO> answers = answerService.findAnswersByQuestionId(questionId, criteria);

        return ResponseEntity.ok(answers);
    }

}
