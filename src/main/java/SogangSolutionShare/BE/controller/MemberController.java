package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.MemberDTO;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.MemberService;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final QuestionService questionService;

    // 회원 정보 수정 API
    @PatchMapping("/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable("memberId") Long memberId, MemberDTO memberDTO) {
        String message = memberService.updateMember(memberId, memberDTO);

        if(message.equals("해당하는 회원이 존재하지 않습니다.")) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok().build();
    }

    // 회원 질문 조회 API
    @GetMapping("/{memberId}/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @PathVariable("memberId") Long memberId,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionDTO> questions = questionService.findQuestionsByMemberId(memberId, pageable);
        return ResponseEntity.ok(questions);
    }

    //회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getMembers() {
        List<MemberDTO> members = memberService.getMembers();
        return ResponseEntity.ok(members);

    }
}
