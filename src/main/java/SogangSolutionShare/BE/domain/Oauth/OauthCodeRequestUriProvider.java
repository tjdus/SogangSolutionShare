package SogangSolutionShare.BE.domain.Oauth;

public interface OauthCodeRequestUriProvider {
    OauthServerType getOauthServerType();
    String getOauthCodeRequestUri();
}
