package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.MemberDTO;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.service.MemberService;
import SogangSolutionShare.BE.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final QuestionService questionService;

    // 회원 정보 조회 API
    @GetMapping
    public ResponseEntity<MemberDTO> getMember(@Login Member loginMember) {
        MemberDTO memberDTO = memberService.getMember(loginMember.getId());
        return ResponseEntity.ok(memberDTO);
    }

    // 회원 정보 수정 API
    @PatchMapping
    public ResponseEntity<String> updateMember(@Login Member loginMember, @ModelAttribute MemberDTO memberDTO) {
        log.info("loginMember: {}", loginMember);
        String message = memberService.updateMember(loginMember.getId(), memberDTO);

        if(message.equals("해당하는 회원이 존재하지 않습니다.")) {
            return ResponseEntity.status(404).body(message);
        }
        return ResponseEntity.ok("회원 정보 수정 성공");
    }

    // 회원 탈퇴 API
    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Login Member loginMember, HttpServletRequest request) {
        memberService.deleteMember(loginMember.getId());

        // 세션 삭제
        request.getSession().invalidate();

        return ResponseEntity.ok().build();
    }

    // 회원 비밀번호 수정 API
    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(@Login Member loginMember, @RequestParam String password) {
        String message = memberService.updatePassword(loginMember.getId(), password);
        return ResponseEntity.ok(message);
    }

    // 회원 질문 조회 API
    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @Login Member loginMember,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionDTO> questions = questionService.findQuestionsByMemberId(loginMember.getId(), pageable);
        return ResponseEntity.ok(questions);
    }

    //회원 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getMembers() {
        List<MemberDTO> members = memberService.getMembers();
        return ResponseEntity.ok(members);

    }
}
