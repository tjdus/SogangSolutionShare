package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.MemberDTO;
import SogangSolutionShare.BE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public void createMember(MemberDTO memberDTO) {
        // 같은 email 주소를 가진 Member 이미 존재하면 예외처리
        if(memberRepository.findByEmail(memberDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Member already exists");
        }

        // Member 생성하고 저장
        Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setName(memberDTO.getName());

        log.info("Member created: {}", member);

        memberRepository.save(member);
    }

    public void updateMember(Long memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));
        member.setEmail(memberDTO.getEmail());
        member.setName(memberDTO.getName());

        log.info("Member updated: {}", member);
    }
}
