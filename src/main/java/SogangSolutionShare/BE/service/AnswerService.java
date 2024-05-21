package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.Answer;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.domain.dto.AnswerRequestDTO;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.exception.ForbiddenException;
import SogangSolutionShare.BE.exception.MemberNotFoundException;
import SogangSolutionShare.BE.exception.QuestionNotFoundException;
import SogangSolutionShare.BE.repository.AnswerRepository;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static SogangSolutionShare.BE.controller.PageableUtil.createPageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public AnswerDTO createAnswer(Long memberId, Long questionId, AnswerRequestDTO answerRequestDTO) {

        // questionId로 Question 찾아서 없으면 예외처리
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        // 질문 글 작성자와 같은 사람이라면 예외 처리
        if (question.getMember().getId().equals(memberId)) {
            // 추후 예외 이름 변경 필요
            throw new IllegalArgumentException("can not answer");
        }

        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        // Answer 생성하고 저장
        Answer createdAnswer = Answer.builder()
                .question(question)
                .member(member)
                .content(answerRequestDTO.getContent())
                .idx(question.getAnswers().size() + 1)
                .build();
        question.addAnswer(createdAnswer);

        log.info("Answer created: {}", createdAnswer);

        answerRepository.save(createdAnswer);

        return createdAnswer.toDTO();
    }
    @Transactional
    public AnswerDTO updateAnswer(Long memberId, Long questionId, Integer index, AnswerRequestDTO answerRequestDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        // questionId와 index로 Answer 찾아서 없으면 예외처리
        Answer answer = answerRepository.findByQuestionIdAndIdx(questionId, index).orElseThrow(QuestionNotFoundException::new);

        // 딥변 수정 권한 확인
        if (!answer.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }

        // Answer 수정하고 저장
        answer.setContent(answerRequestDTO.getContent());

        log.info("Answer updated: {}", answer);

        return answer.toDTO();
    }
    @Transactional
    public AnswerDTO getAnswer(Long questionId, Integer idx) {
        Answer answer = answerRepository.findByQuestionIdAndIdx(questionId, idx).orElseThrow(QuestionNotFoundException::new);
        return answer.toDTO();
    }
    @Transactional
    public void deleteAnswer(Long memberId, Long questionId, Integer index) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        // questionId와 index로 Answer 찾아서 없으면 예외처리
        Answer answer = answerRepository.findByQuestionIdAndIdx(questionId, index).orElseThrow(QuestionNotFoundException::new);
        question.deleteAnswer(answer);


        log.info("Answer deleted: {}", answer);
    }

    public Page<AnswerDTO> findAnswersByQuestionId(Long questionId, Criteria criteria) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());

        Page<Answer> answerPage = answerRepository.findAllByQuestion(question, pageable);
        return answerPage.map(Answer::toDTO);
    }
}
