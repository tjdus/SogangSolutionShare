package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.JoinDTO;
import SogangSolutionShare.BE.domain.dto.LoginDTO;
import SogangSolutionShare.BE.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService loginService;


    @PostMapping("join")
    public ResponseEntity<Void> join(@ModelAttribute JoinDTO joinDTO) {
        // 회원가입 API
        Member member = loginService.createMember(joinDTO);

        if(member == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();

    }
    @PostMapping("/login")
    public ResponseEntity<Member> login(@ModelAttribute LoginDTO loginDTO, HttpServletRequest request) {
        // 로그인 API
        Member member = loginService.login(loginDTO.getLoginId(), loginDTO.getPassword());

        // 로그인 실패
        if(member == null) {
            return ResponseEntity.badRequest().build();
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);

        return ResponseEntity.ok(member);

    }
}
