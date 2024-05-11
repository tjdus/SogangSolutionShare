package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Temp;
import SogangSolutionShare.BE.repository.TempRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final TempRepository tempRepository;


    public void sendAuthorizationEmail(String email) throws MessagingException {
        // 이메일 전송 로직
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Sogang Solution Share 회원가입 인증 메일입니다.");

        Context context = new Context();
        context.setVariable("number", String.valueOf(new Random().nextInt(1000000) % 1000000));
        tempRepository.save(new Temp(email, context.getVariable("number").toString()));

        String html = templateEngine.process("templateMail", context);
        helper.setText(html, true);

        javaMailSender.send(message);
    }

    public String checkEmail(String email, String authorizationNumber) {
        // 이메일 인증 확인 로직
        Temp temp = tempRepository.findByEmail(email).orElse(null);
        if(temp == null || !temp.getAuthorizationNumber().equals(authorizationNumber)) {
            return "인증번호가 일치하지 않습니다.";
        }
        return "인증에 성공하셨습니다.";
    }
}
