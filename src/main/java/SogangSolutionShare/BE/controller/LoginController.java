package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.JoinDTO;
import SogangSolutionShare.BE.domain.dto.LoginDTO;
import SogangSolutionShare.BE.domain.dto.MemberResponseDTO;
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


    /**
     * 회원가입 API
     * @param joinDTO : loginId, password, name, email
     * @return 성공시 200, 실패시 400
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@ModelAttribute JoinDTO joinDTO) {
        // 회원가입 비즈니스 로직
        String message = loginService.createMember(joinDTO);

        // 회원가입 실패
        if(message.equals("이미 가입된 이메일입니다.") || message.equals("이미 가입된 로그인 아이디입니다.")) {
            // 400 Bad Request
            return ResponseEntity.badRequest().body(message);
        }
        // 200 OK
        return ResponseEntity.ok(message);
    }

    /**
     * 로그인 API
     * @param loginDTO : loginId, password
     * @return 성공시 200, 실패시 400
     */

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDTO> login(@ModelAttribute LoginDTO loginDTO, HttpServletRequest request) {
        // 로그인 비즈니스 로직
        Member member = loginService.login(loginDTO.getLoginId(), loginDTO.getPassword());

        // 로그인 실패
        if(member == null) {
            // 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        // 세션 저장
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member);

        // 200 OK
        return ResponseEntity.ok(new MemberResponseDTO(member));
    }
}
