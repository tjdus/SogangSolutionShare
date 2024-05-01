package SogangSolutionShare.BE.domain.Oauth;

public enum OauthServerType {
    GOOGLE, NAVER, KAKAO;

    public static OauthServerType fromString(String type) {
        return OauthServerType.valueOf(type.toUpperCase());
    }
}
