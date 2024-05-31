package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.Scrap;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.exception.AlreadyScrapException;
import SogangSolutionShare.BE.exception.MemberNotFoundException;
import SogangSolutionShare.BE.exception.QuestionNotFoundException;
import SogangSolutionShare.BE.exception.ScrapNotFoundException;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import SogangSolutionShare.BE.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static SogangSolutionShare.BE.controller.PageableUtil.createPageable;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createScrap(Long memberId, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        scrapRepository.findByQuestionIdAndMemberId(questionId, memberId)
                .ifPresent(scrap -> {
                    throw new AlreadyScrapException();
                });

        scrapRepository.save(Scrap.builder()
                .member(member)
                .question(question)
                .build());
    }

    @Transactional
    public void deleteScrap(Long memberId, Long questionId) {
        Scrap scrap = scrapRepository.findByQuestionIdAndMemberId(questionId, memberId)
                .orElseThrow(ScrapNotFoundException::new);
        scrapRepository.delete(scrap);
    }

    @Transactional(readOnly = true)
    public Boolean isUserScrap(Long memberId, Long questionId) {
        return scrapRepository.findByQuestionIdAndMemberId(questionId, memberId).isPresent();
    }

    public Page<QuestionDTO> findScrapQuestionsByMemberId(Long memberId, Criteria criteria) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Page<Question> questions = scrapRepository.findAllByMember(member, pageable).map(Scrap::getQuestion);

        return questions.map(Question::toDTO);
    }
}
