package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.infra.oauth.kakao.KaKaoOauthCodeRequestUriProvider;
import SogangSolutionShare.BE.infra.oauth.kakao.client.KaKaoMemberClient;
import SogangSolutionShare.BE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final MemberRepository memberRepository;
    private final KaKaoMemberClient kaKaoMemberClient;
    private final KaKaoOauthCodeRequestUriProvider kakaoOauthCodeRequestUriProvider;
    public String getOauthCodeRequestUri() {
        return kakaoOauthCodeRequestUriProvider.getOauthCodeRequestUri();
    }

    /*public Long loginWithKaKao(String code) {
        Member member = kaKaoMemberClient.fetch(code);

        Member find = memberRepository.findByKakaoId(member.getKakaoId());
        if(find == null) {
            Member saved = memberRepository.save(member);
            return saved.getId();
        }
        return find.getId();
    }*/
}
