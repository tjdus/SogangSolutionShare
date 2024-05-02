package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.AnswerLikeDTO;
import SogangSolutionShare.BE.domain.dto.QuestionLikeDTO;
import SogangSolutionShare.BE.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final QuestionLikeRepository questionLikeRepository;
    private final AnswerLikeRepository answerLikeRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public void createQuestionLike(QuestionLikeDTO questionLikeDTO) {
        // Question 객체와 Member 객체를 가져와서 좋아요를 생성한다.
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));
        Member member = memberRepository.findById(questionLikeDTO.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        questionLikeRepository.save(QuestionLike.builder()
                .question(question)
                .member(member)
                .build());
    }

    public void createAnswerLike(AnswerLikeDTO answerLikeDTO) {
        // Answer 객체와 Member 객체를 가져와서 좋아요를 생성한다.
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId()).orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));
        Member member = memberRepository.findById(answerLikeDTO.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        answerLikeRepository.save(AnswerLike.builder()
                .answer(answer)
                .member(member)
                .build());
    }
}
