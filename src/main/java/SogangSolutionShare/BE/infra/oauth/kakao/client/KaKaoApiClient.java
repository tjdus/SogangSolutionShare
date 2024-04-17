package SogangSolutionShare.BE.infra.oauth.kakao.client;

import SogangSolutionShare.BE.infra.oauth.kakao.dto.KaKaoMemberResponse;
import SogangSolutionShare.BE.infra.oauth.kakao.dto.KakaoToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface KaKaoApiClient {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = "application/x-www-form-urlencoded")
    KakaoToken getKaKaoToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange(url = "https://kapi.kakao.com/v2/user/me")
    KaKaoMemberResponse getKaKaoMemberInfo(@RequestHeader("Authorization") String accessToken);
}
