package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;


    public void createQuestion(QuestionDTO questionDTO) {
        Long memberId = questionDTO.getMemberId();

        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));

        // Member 존재하면 Question 생성하고 저장
        Question createdQuestion = new Question();
        createdQuestion.setMember(member);
        createdQuestion.setTitle(questionDTO.getTitle());
        createdQuestion.setContent(questionDTO.getContent());

        log.info("Question created: {}", createdQuestion);

        questionRepository.save(createdQuestion);
    }

    public void updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());

        log.info("Question updated: {}", question);
    }

    public List<Question> getQuestions(Long memberId) {
        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));

        // Member 존재하면 Member의 Question들을 반환
        return questionRepository.findAllByMemberId(member.getId());

    }
}
