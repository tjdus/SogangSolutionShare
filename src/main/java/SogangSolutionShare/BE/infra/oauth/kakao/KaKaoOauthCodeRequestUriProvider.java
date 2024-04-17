package SogangSolutionShare.BE.infra.oauth.kakao;

import SogangSolutionShare.BE.domain.OauthCodeRequestUriProvider;
import SogangSolutionShare.BE.domain.OauthServerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KaKaoOauthCodeRequestUriProvider implements OauthCodeRequestUriProvider {

    private final KakaoOauthConfig kakaoOauthConfig;
    @Override
    public OauthServerType getOauthServerType() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String getOauthCodeRequestUri() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoOauthConfig.clientId())
                .queryParam("redirect_uri", kakaoOauthConfig.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(",", kakaoOauthConfig.scopes()))
                .toUriString();
    }
}
