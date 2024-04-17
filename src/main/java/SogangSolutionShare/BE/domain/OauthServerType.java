package SogangSolutionShare.BE.domain;

public enum OauthServerType {
    GOOGLE, NAVER, KAKAO;

    public static OauthServerType fromString(String type) {
        return OauthServerType.valueOf(type.toUpperCase());
    }
}
