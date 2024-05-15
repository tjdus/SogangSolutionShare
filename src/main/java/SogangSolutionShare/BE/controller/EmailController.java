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
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public void sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) throws MessagingException {
        log.info("Email: {}", emailRequestDTO.getEmail());
        // 이메일 전송 API
        emailService.sendAuthorizationEmail(emailRequestDTO.getEmail());
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
