package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.Scrap;
import SogangSolutionShare.BE.exception.AlreadyScrapException;
import SogangSolutionShare.BE.exception.MemberNotFoundException;
import SogangSolutionShare.BE.exception.QuestionNotFoundException;
import SogangSolutionShare.BE.exception.ScrapNotFoundException;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import SogangSolutionShare.BE.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
