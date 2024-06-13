package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/oauth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/kakao")
    public ResponseEntity<Void> redirectOauthCodeRequestUri(
            HttpServletResponse response) throws IOException {
        String redirectUri = oauthService.getOauthCodeRequestUri();
        log.info("redirectUri: {}", redirectUri);
        response.sendRedirect(redirectUri);
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/login/kakao")
    public ResponseEntity<Long> loginWithKakao(
            @RequestParam String code) {
        log.info("code: {}", code);
        Long loginId = oauthService.loginWithKaKao(code);
        log.info("loginId: {}", loginId);
        return ResponseEntity.ok(loginId);
    }*/
}
