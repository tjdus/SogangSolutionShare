package SogangSolutionShare.BE.infra.oauth.kakao.client;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.infra.oauth.kakao.KakaoOauthConfig;
import SogangSolutionShare.BE.infra.oauth.kakao.dto.KaKaoMemberResponse;
import SogangSolutionShare.BE.infra.oauth.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KaKaoMemberClient {
    private final KaKaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;

    public Member fetch(String code) {
        MultiValueMap<String, String> params = getAccessTokenParams(code);
        KakaoToken kakaoToken = kakaoApiClient.getKaKaoToken(params);
        KaKaoMemberResponse kaKaoMemberResponse =
                kakaoApiClient.getKaKaoMemberInfo("Bearer " + kakaoToken.accessToken());
        return kaKaoMemberResponse.toMember();
    }

    private MultiValueMap<String, String> getAccessTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", code);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }
}
