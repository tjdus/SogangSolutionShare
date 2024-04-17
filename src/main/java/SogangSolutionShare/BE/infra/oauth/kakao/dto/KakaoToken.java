package SogangSolutionShare.BE.infra.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoToken (
        String accessToken,
        String tokenType,
        String refreshToken,
        Long expiresIn,
        String scope
) {
}
