package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.EmailCheckDTO;
import SogangSolutionShare.BE.domain.dto.EmailRequestDTO;
import SogangSolutionShare.BE.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO){
        log.info("Email: {}", emailRequestDTO.getEmail());
        // 이메일 전송 API
        try {
            emailService.sendAuthorizationEmail(emailRequestDTO.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이메일 전송에 실패하였습니다.");
        }
        return ResponseEntity.ok("이메일 인증번호 전송에 성공하였습니다.");
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkEmail(@RequestBody EmailCheckDTO emailDTO) {
        // 이메일 인증 확인 API
        String msg = emailService.checkEmail(emailDTO.getEmail(), emailDTO.getAuthorizationNumber());

        if(msg.equals("인증에 성공하셨습니다.")) {
            return ResponseEntity.ok(msg);
        }
        return ResponseEntity.badRequest().body(msg);
    }
}
