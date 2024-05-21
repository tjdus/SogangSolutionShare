package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.AnswerDTO;
import SogangSolutionShare.BE.domain.dto.AnswerRequestDTO;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.repository.AnswerRepository;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import SogangSolutionShare.BE.service.AnswerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AnswerControllerTest {

    @Autowired
    public MockMvc mockMvc;

    private MockHttpSession session;

    @Autowired private QuestionRepository questionRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private AnswerRepository answerRepository;
    @Autowired private AnswerService answerService;

    @Test
    public void createAnswer() throws Exception {
        Member newMember = new Member();
        newMember.setName("new member");
        memberRepository.save(newMember);

        Member questionMember = memberRepository.findByName("name").orElse(null);
        Member answerMember = memberRepository.findByName("new member").orElse(null);
        // member 질문 생성
        Question question = Question.builder()
                .member(questionMember)
                .title("title")
                .build();

        questionRepository.save(question);

        AnswerRequestDTO answerRequestDTO = AnswerRequestDTO.builder()
                .content("answer content")
                .build();
        // 답변 생성
        answerService.createAnswer(answerMember.getId(), question.getId(), answerRequestDTO);
        AnswerDTO createdAnswer = answerService.getAnswer(question.getId(), 1);

        assertNotNull(createdAnswer);
        assertEquals("answer content", createdAnswer.getContent());

    }

    @Test
    public void updateAnswer() {
        Member newMember = new Member();
        newMember.setName("new member");
        memberRepository.save(newMember);

        Member questionMember = memberRepository.findByName("name").orElse(null);
        Member answerMember = memberRepository.findByName("new member").orElse(null);
        // member 질문 생성
        Question question = Question.builder()
                .member(questionMember)
                .title("title")
                .build();

        questionRepository.save(question);

        AnswerRequestDTO answerRequestDTO = AnswerRequestDTO.builder()
                .content("answer content")
                .build();
        // 답변 생성
        answerService.createAnswer(answerMember.getId(), question.getId(), answerRequestDTO);
        AnswerDTO createdAnswer = answerService.getAnswer(question.getId(), 1);

        assertNotNull(createdAnswer);
        assertEquals("answer content", createdAnswer.getContent());
        // 답변 수정
        AnswerRequestDTO updateRequest = AnswerRequestDTO.builder()
                .content("updated content")
                .build();
        AnswerDTO updatedAnswer = answerService.updateAnswer(answerMember.getId(), question.getId(), 1, updateRequest);

        assertNotNull(updatedAnswer);
        assertEquals("updated content", updatedAnswer.getContent());
    }

    @Test
    public void deleteAnswer(){
        Member newMember = new Member();
        newMember.setName("new member");
        memberRepository.save(newMember);

        Member questionMember = memberRepository.findByName("name").orElse(null);
        Member answerMember = memberRepository.findByName("new member").orElse(null);
        // member 질문 생성
        Question question = Question.builder()
                .member(questionMember)
                .title("title")
                .build();

        questionRepository.save(question);

        AnswerRequestDTO answerRequestDTO = AnswerRequestDTO.builder()
                .content("answer content")
                .build();
        // 답변 생성
        answerService.createAnswer(answerMember.getId(), question.getId(), answerRequestDTO);
        AnswerDTO createdAnswer = answerService.getAnswer(question.getId(), 1);

        answerService.deleteAnswer(answerMember.getId(), question.getId(), 1);
        assertNull(answerRepository.findByQuestionIdAndIdx(question.getId(), 1).orElse(null));
    }

    @Test
    public void getAnswers() throws Exception {
        // 멤버 생성
        Member newMember1 = new Member();
        newMember1.setName("new member1");
        memberRepository.save(newMember1);
        Member newMember2 = new Member();
        newMember2.setName("new member2");
        memberRepository.save(newMember2);

        Member questionMember = memberRepository.findByName("name").orElse(null);
        Member answerMember1 = memberRepository.findByName("new member1").orElse(null);
        Member answerMember2 = memberRepository.findByName("new member2").orElse(null);
        // member 질문 생성
        Question question = Question.builder()
                .member(questionMember)
                .title("title")
                .build();

        questionRepository.save(question);

        AnswerRequestDTO answerRequestDTO1 = AnswerRequestDTO.builder()
                .content("answer content1")
                .build();
        AnswerRequestDTO answerRequestDTO2 = AnswerRequestDTO.builder()
                .content("answer content2")
                .build();
        // 답변 생성
        answerService.createAnswer(answerMember1.getId(), question.getId(), answerRequestDTO1);
        answerService.createAnswer(answerMember2.getId(), question.getId(), answerRequestDTO2);

        Criteria criteria = new Criteria(0, 10, "latest");
        Page<AnswerDTO> answersPage = answerService.findAnswersByQuestionId(question.getId(), criteria);

        assertNotNull(answersPage);

        // 답변 목록이 비어있지 않은지 확인
        assertFalse(answersPage.isEmpty());

        // 답변 내용 확인
        List<AnswerDTO> answers = answersPage.getContent();
        assertEquals(2, answers.size());

        assertEquals("answer content1", answers.get(1).getContent());
        assertEquals("answer content2", answers.get(0).getContent());
    }
}