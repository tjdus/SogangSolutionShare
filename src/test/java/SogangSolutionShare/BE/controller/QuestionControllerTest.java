package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.*;

import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.QuestionRequestDTO;
import SogangSolutionShare.BE.repository.*;
import SogangSolutionShare.BE.service.QuestionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    public MockMvc mockMvc;

    private MockHttpSession session;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private QuestionTagRepository questionTagRepository;

    @Autowired private QuestionService questionService;

    @Before
    public void setUp() {
        questionTagRepository.deleteAll();
        questionRepository.deleteAll();
        tagRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();

        Member member = new Member();
        member.setName("name");
        member.setPassword("password");
        memberRepository.save(member);

        Category category = new Category();
        category.setName("category");
        categoryRepository.save(category);


        session = new MockHttpSession();
        session.setAttribute("loginMember", member);

    }
    @Test
    public void createQuestion() throws Exception {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                .title("title")
                .content("content")
                .category("category")
                .tags(List.of("tag1", "tag2"))
                .build();
        Member member = memberRepository.findByName("name").orElse(null);
        assert member != null;
        Long memberId = member.getId();
        QuestionDTO createdQuestion = questionService.createQuestion(memberId, questionRequestDTO);

        assertNotNull(createdQuestion);
        assertEquals("title", createdQuestion.getTitle());
        assertEquals("content", createdQuestion.getContent());
        assertEquals("category", createdQuestion.getCategory());
        assertTrue(createdQuestion.getTags().contains("tag1"));
        assertTrue(createdQuestion.getTags().contains("tag2"));

        Question question = questionRepository.findOneById(createdQuestion.getId());
        assertNotNull(question);
    }
    @Test
    public void updateQuestion() {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                .title("title")
                .content("content")
                .category("category")
                .tags(List.of("tag1", "tag2"))
                .build();
        Member member = memberRepository.findByName("name").orElse(null);
        assert member != null;
        Long memberId = member.getId();
        QuestionDTO createdQuestion = questionService.createQuestion(memberId, questionRequestDTO);
        Long questionId = createdQuestion.getId();

        QuestionRequestDTO questionUpdateRequest = QuestionRequestDTO.builder()
                .title("updated title")
                .content("updated content")
                .category("category")
                .tags(List.of("tag3", "tag4"))
                .build();

        QuestionDTO updatedQuestion = questionService.updateQuestion(memberId, questionId, questionUpdateRequest);

        assertNotNull(updatedQuestion);
        assertEquals("updated title", updatedQuestion.getTitle());
        assertEquals("updated content", updatedQuestion.getContent());
        assertEquals("category", updatedQuestion.getCategory());
        assertTrue(updatedQuestion.getTags().contains("tag3"));
        assertTrue(updatedQuestion.getTags().contains("tag4"));
        // 기존 연관 관계가 제거되었는지 확인
        assertFalse(questionTagRepository.findByQuestionId(questionId).stream().anyMatch(questionTag -> questionTag.getTag().getName().equals("tag1")));
        assertFalse(questionTagRepository.findByQuestionId(questionId).stream().anyMatch(questionTag -> questionTag.getTag().getName().equals("tag2")));

        // 새로운 연관 관계가 추가되었는지 확인
        assertTrue(questionTagRepository.findByQuestionId(questionId).stream().anyMatch(questionTag -> questionTag.getTag().getName().equals("tag3")));
        assertTrue(questionTagRepository.findByQuestionId(questionId).stream().anyMatch(questionTag -> questionTag.getTag().getName().equals("tag4")));

    }

    @Test
    public void deleteQuestion() {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                .title("title")
                .content("content")
                .category("category")
                .tags(List.of("tag1", "tag2"))
                .build();
        Member member = memberRepository.findByName("name").orElse(null);
        Long memberId = member.getId();
        QuestionDTO createdQuestion = questionService.createQuestion(memberId, questionRequestDTO);
        Long questionId = createdQuestion.getId();

        questionService.deleteQuestion(memberId, questionId);
        assertNull(questionRepository.findOneById(questionId));
    }
    @Test
    public void getQuestionsByMemberId() throws Exception {

    }

    @Test
    public void getQuestion() throws Exception {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                .title("title")
                .content("content")
                .category("category")
                .tags(List.of("tag1", "tag2"))
                .build();
        Member member = memberRepository.findByName("name").orElse(null);
        assert member != null;
        Long memberId = member.getId();
        QuestionDTO createdQuestion = questionService.createQuestion(memberId, questionRequestDTO);
        Long questionId = createdQuestion.getId();

        QuestionDTO questionDTO = questionService.findQuestionById(questionId);

        assertNotNull(questionDTO);
        assertEquals("title", questionDTO.getTitle());
        assertEquals("content", questionDTO.getContent());
        assertEquals("category", questionDTO.getCategory());
        assertTrue(questionDTO.getTags().contains("tag1"));
        assertTrue(questionDTO.getTags().contains("tag2"));
    }

    @Test
    public void searchQuestionsByQuery() throws Exception {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                .title("title")
                .content("content")
                .category("category")
                .tags(List.of("tag1", "tag2"))
                .build();
        Member member = memberRepository.findByName("name").orElse(null);
        assert member != null;
        Long memberId = member.getId();
        QuestionDTO createdQuestion = questionService.createQuestion(memberId, questionRequestDTO);

        mockMvc.perform(get("/api/search")
                .param("q", "title")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(createdQuestion.getId()));
    }

    @Test
    public void getQuestionsCount() throws Exception {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                .title("title")
                .content("content")
                .category("category")
                .tags(List.of("tag1", "tag2"))
                .build();
        Member member = memberRepository.findByName("name").orElse(null);
        assert member != null;
        Long memberId = member.getId();
        QuestionDTO createdQuestion = questionService.createQuestion(memberId, questionRequestDTO);

        ResultActions resultActions = mockMvc.perform(get("/api/questions/count")
                        .session(session))
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals("1", contentAsString);
    }

}