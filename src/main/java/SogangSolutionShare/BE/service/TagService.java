package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.MemberTag;
import SogangSolutionShare.BE.domain.Tag;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.exception.MemberNotFoundException;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.MemberTagRepository;
import SogangSolutionShare.BE.repository.QuestionTagRepository;
import SogangSolutionShare.BE.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static SogangSolutionShare.BE.controller.PageableUtil.createPageable;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final QuestionTagRepository questionTagRepository;
    private final MemberTagRepository memberTagRepository;

    @Transactional(readOnly = true)
    public Page<Tag> findTagsByMemberId(Long memberId, Criteria criteria) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());

        return memberTagRepository.findAllByMember(member, pageable).map(MemberTag::getTag);
    }
}
