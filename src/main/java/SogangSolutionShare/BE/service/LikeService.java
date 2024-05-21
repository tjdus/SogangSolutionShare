package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.AnswerLikeDTO;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.QuestionLikeDTO;
import SogangSolutionShare.BE.exception.AlreadyLikeException;
import SogangSolutionShare.BE.exception.AnswerNotFoundException;
import SogangSolutionShare.BE.exception.MemberNotFoundException;
import SogangSolutionShare.BE.exception.QuestionNotFoundException;
import SogangSolutionShare.BE.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {

    private final QuestionLikeRepository questionLikeRepository;
    private final AnswerLikeRepository answerLikeRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public void createQuestionLike(QuestionLikeDTO questionLikeDTO) {
        // Question 객체와 Member 객체를 가져와서 좋아요를 생성한다.
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId())
                .orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(questionLikeDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        questionLikeRepository.findByQuestionIdAndMemberId(questionLikeDTO.getQuestionId(), questionLikeDTO.getMemberId())
                .ifPresent(questionLike -> {
                    throw new AlreadyLikeException();
                });

        question.setLikeCount(question.getLikeCount()+1);
        questionLikeRepository.save(QuestionLike.builder()
                .question(question)
                .member(member)
                .build());
    }

    public void deleteQuestionLike(QuestionLikeDTO questionLikeDTO) {
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));
        Member member = memberRepository.findById(questionLikeDTO.getMemberId()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Optional<QuestionLike> questionLike = questionLikeRepository.findByQuestionIdAndMemberId(question.getId(), member.getId());

        if(questionLike.isPresent()) {
            question.setLikeCount(question.getLikeCount()-1);
            questionLikeRepository.delete(questionLike.get());

            log.info("QuestionLike deleted: {}", questionLike);
        }
    }

    public void createAnswerLike(AnswerLikeDTO answerLikeDTO) {
        // Answer 객체와 Member 객체를 가져와서 좋아요를 생성한다.
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId())
                .orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(answerLikeDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        answerLikeRepository.findByAnswerIdAndMemberId(answerLikeDTO.getAnswerId(), answerLikeDTO.getMemberId())
                .ifPresent(answerLike -> {
                    throw new AlreadyLikeException();
                });

        answer.setLikeCount(answer.getLikeCount()+1);
        answerLikeRepository.save(AnswerLike.builder()
                .answer(answer)
                .member(member)
                .build());
    }

    public void deleteAnswerLike(AnswerLikeDTO answerLikeDTO) {
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId())
                .orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(answerLikeDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<AnswerLike> answerLike = answerLikeRepository.findByAnswerIdAndMemberId(answer.getId(), member.getId());

        if(answerLike.isPresent()) {
            answer.setLikeCount(answer.getLikeCount()-1);
            answerLikeRepository.delete(answerLike.get());

            log.info("AnswerLike deleted: {}", answerLike);
        }
    }


}
