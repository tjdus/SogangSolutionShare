package SogangSolutionShare.BE.infra.oauth.kakao.dto;

import SogangSolutionShare.BE.domain.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KaKaoMemberResponse (
        Long id,
        LocalDateTime connectedAt,
        boolean hasSignedUp,
        KaKaoAccount kakaoAccount
) {

    public Member toMember() {
        return Member.builder()
                .name(kakaoAccount.profile.nickname)
                .kakaoId(id)
                .build();
    }


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record KaKaoAccount(
            boolean profileNeedsAgreement,
            Profile profile,
            boolean emailNeedsAgreement,
            boolean isEmailValid,
            boolean isEmailVerified,
            String email
    ) {
    }

    public record Profile(
            String nickname,
            String thumbnailImageUrl,
            String profileImageUrl
    ) {
    }
}
