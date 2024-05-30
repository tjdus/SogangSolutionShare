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

    public void createQuestionLike(Member loginMember, QuestionLikeDTO questionLikeDTO) {
        // Question 객체와 Member 객체를 가져와서 좋아요를 생성한다.
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId())
                .orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<QuestionLike> ql = questionLikeRepository.findByQuestionIdAndMemberId(question.getId(), member.getId());
        // 이미 싫어요를 눌렀다면 좋아요로 변경한다.
        if(ql.isPresent() && !ql.get().getIsLike()) {
            ql.get().setIsLike(true);
            question.setLikeCount(question.getLikeCount()+1);
            question.setDislikeCount(question.getDislikeCount()-1);
        } else if(ql.isEmpty()) {
            // 좋아요 & 싫어요를 누르지 않았다면 좋아요를 누른다.
            question.setLikeCount(question.getLikeCount()+1);
            questionLikeRepository.save(QuestionLike.builder()
                    .question(question)
                    .member(member)
                    .isLike(true)
                    .build());
        }
    }

    public void createQuestionDislike(Member loginMember, QuestionLikeDTO questionLikeDTO) {
        // Question 객체와 Member 객체를 가져와서 싫어요를 생성한다.
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId())
                .orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<QuestionLike> ql = questionLikeRepository.findByQuestionIdAndMemberId(question.getId(), member.getId());
        // 이미 좋아요를 눌렀다면 싫어요로 변경한다.
        if(ql.isPresent() && ql.get().getIsLike()) {
            ql.get().setIsLike(false);
            question.setLikeCount(question.getLikeCount()-1);
            question.setDislikeCount(question.getDislikeCount()+1);
        } else if(ql.isEmpty()) {
            // 좋아요 & 싫어요를 누르지 않았다면 싫어요를 누른다.
            question.setDislikeCount(question.getDislikeCount()+1);
            questionLikeRepository.save(QuestionLike.builder()
                    .question(question)
                    .member(member)
                    .isLike(false)
                    .build());
        }
    }

    public void deleteQuestionLike(Member loginMember, QuestionLikeDTO questionLikeDTO) {
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId()).orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotFoundException::new);

        Optional<QuestionLike> questionLike = questionLikeRepository.findByQuestionIdAndMemberId(question.getId(), member.getId());

        if(questionLike.isPresent() && questionLike.get().getIsLike()) {
            question.setLikeCount(question.getLikeCount()-1);
            questionLikeRepository.delete(questionLike.get());
        }
    }

    public void deleteQuestionDislike(Member loginMember, QuestionLikeDTO questionLikeDTO) {
        Question question = questionRepository.findById(questionLikeDTO.getQuestionId()).orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotFoundException::new);

        Optional<QuestionLike> questionLike = questionLikeRepository.findByQuestionIdAndMemberId(question.getId(), member.getId());

        if(questionLike.isPresent() && !questionLike.get().getIsLike()) {
            question.setDislikeCount(question.getDislikeCount()-1);
            questionLikeRepository.delete(questionLike.get());
        }
    }

    public void createAnswerLike(Member loginMember, AnswerLikeDTO answerLikeDTO) {
        // Answer 객체와 Member 객체를 가져와서 좋아요를 생성한다.
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId())
                .orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<AnswerLike> al = answerLikeRepository.findByAnswerIdAndMemberId(answer.getId(), member.getId());

        // 이미 싫어요를 눌렀다면 좋아요로 변경한다.
        if(al.isPresent() && !al.get().getIsLike()) {
            al.get().setIsLike(true);
            answer.setLikeCount(answer.getLikeCount()+1);
            answer.setDislikeCount(answer.getDislikeCount()-1);
        } else if(al.isEmpty()) {
            // 좋아요 & 싫어요를 누르지 않았다면 좋아요를 누른다.
            answer.setLikeCount(answer.getLikeCount()+1);
            answerLikeRepository.save(AnswerLike.builder()
                    .answer(answer)
                    .member(member)
                    .isLike(true)
                    .build());
        }
    }

    public void createAnswerDislike(Member loginMember, AnswerLikeDTO answerLikeDTO) {
        // Answer 객체와 Member 객체를 가져와서 싫어요를 생성한다.
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId())
                .orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<AnswerLike> al = answerLikeRepository.findByAnswerIdAndMemberId(answer.getId(), member.getId());

        // 이미 좋아요를 눌렀다면 싫어요로 변경한다.
        if(al.isPresent() && al.get().getIsLike()) {
            al.get().setIsLike(false);
            answer.setLikeCount(answer.getLikeCount()-1);
            answer.setDislikeCount(answer.getDislikeCount()+1);
        } else if(al.isEmpty()) {
            // 좋아요 & 싫어요를 누르지 않았다면 싫어요를 누른다.
            answer.setDislikeCount(answer.getDislikeCount()+1);
            answerLikeRepository.save(AnswerLike.builder()
                    .answer(answer)
                    .member(member)
                    .isLike(false)
                    .build());
        }
    }

    public void deleteAnswerLike(Member loginMember, AnswerLikeDTO answerLikeDTO) {
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId())
                .orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<AnswerLike> answerLike = answerLikeRepository.findByAnswerIdAndMemberId(answer.getId(), member.getId());

        if(answerLike.isPresent() && answerLike.get().getIsLike()) {
            answer.setLikeCount(answer.getLikeCount()-1);
            answerLikeRepository.delete(answerLike.get());
        }
    }

    public void deleteAnswerDislike(Member loginMember, AnswerLikeDTO answerLikeDTO) {
        Answer answer = answerRepository.findById(answerLikeDTO.getAnswerId())
                .orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<AnswerLike> answerLike = answerLikeRepository.findByAnswerIdAndMemberId(answer.getId(), member.getId());

        if(answerLike.isPresent() && !answerLike.get().getIsLike()) {
            answer.setDislikeCount(answer.getDislikeCount()-1);
            answerLikeRepository.delete(answerLike.get());
        }
    }


    public Long isUserQuestionLike(Member loginMember, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotFoundException::new);

        Optional<QuestionLike> questionLike = questionLikeRepository.findByQuestionIdAndMemberId(question.getId(), member.getId());

        if(questionLike.isPresent() && questionLike.get().getIsLike()) {
            return 1L;
        } else if(questionLike.isPresent() && !questionLike.get().getIsLike()) {
            return 0L;
        } else {
            return -1L;
        }
    }

    public Long isUserAnswerLike(Member loginMember, Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotFoundException::new);

        Optional<AnswerLike> answerLike = answerLikeRepository.findByAnswerIdAndMemberId(answer.getId(), member.getId());

        if(answerLike.isPresent() && answerLike.get().getIsLike()) {
            return 1L;
        } else if(answerLike.isPresent() && !answerLike.get().getIsLike()) {
            return 0L;
        } else {
            return -1L;
        }
    }


    public Long getQuestionLikeCount(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        return question.getLikeCount();
    }

    public Long getQuestionDislikeCount(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        return question.getDislikeCount();
    }

    public Long getAnswerLikeCount(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        return answer.getLikeCount();
    }

    public Long getAnswerDislikeCount(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        return answer.getDislikeCount();
    }
}
