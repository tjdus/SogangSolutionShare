package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.JoinDTO;
import SogangSolutionShare.BE.domain.dto.LoginDTO;
import SogangSolutionShare.BE.domain.dto.MemberResponseDTO;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService loginService;
    private final MemberRepository memberRepository;


    /**
     * 회원가입 API
     * @param joinDTO : loginId, password, name, email
     * @return 성공시 201, 실패시 400, 409
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@Validated @ModelAttribute JoinDTO joinDTO, Errors errors) {
        if(errors.hasErrors()) {
            // 400 Bad Request
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0).getDefaultMessage());
        }

        // 회원가입 비즈니스 로직
        String message = loginService.createMember(joinDTO);

        // 회원가입 실패
        if(message.equals("이미 가입된 이메일입니다.") || message.equals("이미 가입된 로그인 아이디입니다.")) {
            // 409 Conflict
            return ResponseEntity.status(409).body(message);
        }
        // 201 Created
        return ResponseEntity.created(null).body(message);
    }

    /**
     * 로그인 API
     * @param loginDTO : loginId, password
     * @return 성공시 200, 실패시 400, 409
     */

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDTO> login(@Validated @ModelAttribute LoginDTO loginDTO, Errors errors, HttpServletRequest request) {
        if(errors.hasErrors()) {
            // 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        // 로그인 비즈니스 로직
        Member member = loginService.login(loginDTO.getLoginId(), loginDTO.getPassword());

        // 로그인 실패
        if(member == null) {
            // 401 Unauthorized
            return ResponseEntity.status(401).build();
        }

        // 세션 저장
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);

        // 200 OK
        return ResponseEntity.ok(new MemberResponseDTO(member));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        //response.addCookie(new Cookie("JSESSIONID", null));
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/session")
    public ResponseEntity<Void> session(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("loginMember");
        if(member == null || memberRepository.findById(member.getId()).isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok().build();
    }
}
