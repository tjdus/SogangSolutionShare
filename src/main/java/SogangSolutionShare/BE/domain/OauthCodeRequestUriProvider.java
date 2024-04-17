package SogangSolutionShare.BE.domain;

public interface OauthCodeRequestUriProvider {
    OauthServerType getOauthServerType();
    String getOauthCodeRequestUri();
}
