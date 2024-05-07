package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.JoinDTO;
import SogangSolutionShare.BE.domain.dto.MemberDTO;
import SogangSolutionShare.BE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member createMember(JoinDTO joinDTO) {
        // 같은 로그인 아이디를 가진 Member 이미 존재하면 예외처리
        if(memberRepository.findByEmail(joinDTO.getLoginId()).isPresent()) {
            return null;
        }

        // Member 생성하고 저장
        Member createdMember = Member.builder()
                .loginId(joinDTO.getLoginId())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .email(joinDTO.getEmail())
                .name(joinDTO.getName())
                .build();

        log.info("Member created: {}", createdMember);

        memberRepository.save(createdMember);

        return createdMember;
    }

    public void updateMember(Long memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));
        member.setEmail(memberDTO.getEmail());
        member.setName(memberDTO.getName());

        log.info("Member updated: {}", member);
    }

    public Member login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId);
        String encodedPassword = (member == null) ? "" : member.getPassword();

        if(member == null || !passwordEncoder.matches(password, encodedPassword)) {
            return null;
        }
        return member;
    }

    public List<MemberDTO> getMembers(){
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream()
                .map(Member::toDTO)
                .collect(Collectors.toList());
    }
}
