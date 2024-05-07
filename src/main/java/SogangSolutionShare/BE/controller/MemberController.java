package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.MemberDTO;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.MemberService;
import SogangSolutionShare.BE.service.QuestionService;
import lombok.RequiredArgsConstructor;
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
    public void updateMember(@PathVariable("memberId") Long memberId, MemberDTO memberDTO) {
        memberService.updateMember(memberId, memberDTO);
    }

    // 회원 질문 조회 API
    @GetMapping("/{memberId}/question")
    public ResponseEntity<List<QuestionDTO>> getQuestions(@PathVariable("memberId") Long memberId) {
        List<QuestionDTO> questions = questionService.getQuestions(memberId);
        return ResponseEntity.ok(questions);
    }

    /* 테스트 용 추후 삭제해야함 */
    //회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getMembers() {
        List<MemberDTO> members = memberService.getMembers();
        return ResponseEntity.ok(members);

    }
}
