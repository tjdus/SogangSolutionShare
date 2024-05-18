package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Answer;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.repository.AnswerRepository;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    public void createAnswer(AnswerDTO answerDTO) {

        // questionId로 Question 찾아서 없으면 예외처리
        Question question = questionRepository.findById(answerDTO.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));

        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(answerDTO.getMemberId()).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));

        // Answer 생성하고 저장
        Answer createdAnswer = Answer.builder()
                .question(question)
                .member(member)
                .content(answerDTO.getContent())
                .build();

        log.info("Answer created: {}", createdAnswer);

        answerRepository.save(createdAnswer);
    }

    public void updateAnswer(Long answerId, String content) {
            // answerId로 Answer 찾아서 없으면 예외처리
            Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("Answer does not exist"));

            // Answer 수정하고 저장
            answer.setContent(content);

            log.info("Answer updated: {}", answer);
    }

    public void deleteAnswer(Long answerId) {
        // answerId로 Answer 찾아서 없으면 예외처리
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("Answer does not exist"));

        // Answer 삭제
        answerRepository.delete(answer);

        log.info("Answer deleted: {}", answerId);
    }
}
