package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Answer;
import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.AnswerLikeDTO;
import SogangSolutionShare.BE.domain.dto.QuestionLikeDTO;
import SogangSolutionShare.BE.repository.AnswerRepository;
import SogangSolutionShare.BE.repository.CategoryRepository;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private AnswerRepository answerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        session = new MockHttpSession();

        Member member = new Member();
        member.setName("name");
        member.setPassword("password");
        memberRepository.save(member);

        Category category = new Category();
        category.setName("category");
        categoryRepository.save(category);

        Question question = new Question();
        question.setTitle("question");
        question.setContent("content");
        question.setCategory(category);
        question.setMember(member);
        questionRepository.save(question);

        Answer answer = new Answer();
        answer.setContent("content");
        answer.setQuestion(question);
        answer.setMember(member);
        answerRepository.save(answer);

        question = new Question();
        question.setTitle("question1");
        question.setContent("content1");
        question.setCategory(category);
        question.setMember(member);
        questionRepository.save(question);


        session = new MockHttpSession();
        session.setAttribute("loginMember", member);
    }

    @Test
    public void createQuestionLike() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");

        QuestionLikeDTO questionLikeDTO = new QuestionLikeDTO();
        questionLikeDTO.setQuestionId(question.get().getId());

        String in = objectMapper.writeValueAsString(questionLikeDTO);

        mockMvc.perform(post("/api/like/question")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/like/question/{questionId}", question.get().getId())
                        .session(session))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("1", result);

    }

    @Test
    public void createQuestionDislike() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");

        QuestionLikeDTO questionLikeDTO = new QuestionLikeDTO();
        questionLikeDTO.setQuestionId(question.get().getId());

        String in = objectMapper.writeValueAsString(questionLikeDTO);

        mockMvc.perform(post("/api/dislike/question")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/like/question/{questionId}", question.get().getId())
                        .session(session))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("0", result);
    }

    @Test
    public void createAnswerLike() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");
        question.ifPresent(value -> log.info("question: {}", value));
        List<Answer> answers = answerRepository.findByQuestionId(question.get().getId());

        Answer answer = answers.get(0);

        AnswerLikeDTO answerLikeDTO = new AnswerLikeDTO();
        answerLikeDTO.setAnswerId(answer.getId());

        String in = objectMapper.writeValueAsString(answerLikeDTO);

        mockMvc.perform(post("/api/like/answer")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/like/answer/{answerId}", answer.getId())
                        .session(session))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("1", result);

    }

    @Test
    public void createAnswerDislike() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");
        Answer answer = answerRepository.findByQuestionId(question.get().getId()).get(0);

        AnswerLikeDTO answerLikeDTO = new AnswerLikeDTO();
        answerLikeDTO.setAnswerId(answer.getId());

        String in = objectMapper.writeValueAsString(answerLikeDTO);

        mockMvc.perform(post("/api/dislike/answer")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/like/answer/{answerId}", answer.getId())
                        .session(session))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("0", result);
    }

    @Test
    public void changeQuestionLikeToDisLike() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");

        QuestionLikeDTO questionLikeDTO = new QuestionLikeDTO();
        questionLikeDTO.setQuestionId(question.get().getId());

        String in = objectMapper.writeValueAsString(questionLikeDTO);

        mockMvc.perform(post("/api/like/question")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/like/question/{questionId}", question.get().getId())
                        .session(session))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("1", result);

        mockMvc.perform(post("/api/dislike/question")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        resultActions = mockMvc.perform(get("/api/like/question/{questionId}", question.get().getId())
                        .session(session))
                .andExpect(status().isOk());

        result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("0", result);
    }

    @Test
    public void changeAnswerLikeToDisLike() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");
        Answer answer = answerRepository.findByQuestionId(question.get().getId()).get(0);

        AnswerLikeDTO answerLikeDTO = new AnswerLikeDTO();
        answerLikeDTO.setAnswerId(answer.getId());

        String in = objectMapper.writeValueAsString(answerLikeDTO);

        mockMvc.perform(post("/api/like/answer")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/like/answer/{answerId}", answer.getId())
                        .session(session))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("1", result);

        mockMvc.perform(post("/api/dislike/answer")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        resultActions = mockMvc.perform(get("/api/like/answer/{answerId}", answer.getId())
                        .session(session))
                .andExpect(status().isOk());

        result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("0", result);
    }

    @Test
    public void createQuestionLikeAndSearch() throws Exception {
        Optional<Question> question = questionRepository.findByTitle("question");

        QuestionLikeDTO questionLikeDTO = new QuestionLikeDTO();
        questionLikeDTO.setQuestionId(question.get().getId());

        String in = objectMapper.writeValueAsString(questionLikeDTO);

        mockMvc.perform(post("/api/like/question")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());

        question = questionRepository.findByTitle("question1");

        questionLikeDTO = new QuestionLikeDTO();
        questionLikeDTO.setQuestionId(question.get().getId());

        in = objectMapper.writeValueAsString(questionLikeDTO);

        mockMvc.perform(post("/api/like/question")
                        .session(session)
                        .contentType("application/json")
                        .content(in))
                .andExpect(status().isOk());


        ResultActions resultActions = mockMvc.perform(get("/api/profile/like/questions")
                        .session(session)
                        .param("page", "1"))
                .andExpect(status().isOk());

        String result = resultActions.andReturn().getResponse().getContentAsString();

        log.info("result: {}", result);

        assertTrue(result.contains("question"));
        assertTrue(result.contains("question1"));
    }
}