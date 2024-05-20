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
@ActiveProfiles("test")
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
        member.setId(1L);
        member.setEmail("test@example.com");
        member.setLoginId("testuser");
        member.setName("Test User");
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
        QuestionDTO createdQuestion = questionService.createQuestion(1L, questionRequestDTO);

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
    }

    @Test
    public void deleteQuestion() {
    }
    @Test
    public void getQuestionsByMemberId() throws Exception {
        mockMvc.perform(get("/member/1/questions")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title2"))
                .andExpect(jsonPath("$.content[1].title").value("title1"));
    }

    @Test
    public void getQuestion() throws Exception {
        mockMvc.perform(get("/question/1")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.categoryName").value("testCategory1"))
                .andExpect(jsonPath("$.tags[0]").value("tag1"));
    }
    @Test
    public void getQuestions() throws Exception {
        //최신순
        mockMvc.perform(get("/questions")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].title").value("title3"))
                .andExpect(jsonPath("$.content[1].title").value("title2"))
                .andExpect(jsonPath("$.content[2].title").value("title1"));


        //좋아요 순
        mockMvc.perform(get("/questions")
                        .param("orderBy", "most-liked")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].title").value("title2"))
                .andExpect(jsonPath("$.content[1].title").value("title3"))
                .andExpect(jsonPath("$.content[2].title").value("title1"))
                .andExpect(jsonPath("$.content[0].likeCount").value(10))
                .andExpect(jsonPath("$.content[1].likeCount").value(7))
                .andExpect(jsonPath("$.content[2].likeCount").value(5));
    }

    @Test
    public void getQuestionsWithPaging() throws Exception {
        // 페이지 사이즈 1
        // 1 페이지
        mockMvc.perform(get("/questions")
                        .param("page", "1")
                        .param("size", "1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.content[0].title").value("title3"));
        // 2 페이지
        mockMvc.perform(get("/questions")
                        .param("page", "2")
                        .param("size", "1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.content[0].title").value("title2"));

        // 페이지 사이즈 2
        // 좋아요순
        // 1 페이지
        mockMvc.perform(get("/questions")
                        .param("page", "1")
                        .param("size", "2")
                        .param("orderBy", "most-liked")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.content[0].title").value("title2"))
                .andExpect(jsonPath("$.content[1].title").value("title3"));
        // 2 페이지
        mockMvc.perform(get("/questions")
                        .param("page", "2")
                        .param("size", "2")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.content[0].title").value("title1"));
    }

    @Test
    public void searchQuestionsByQuery() throws Exception {
        mockMvc.perform(get("/search")
                .param("q", "title1")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title1"));
    }

    @Test
    public void searchQuestionsByTags() throws Exception {
        mockMvc.perform(get("/search")
                .param("q", "tag1")
                .param("type", "tag")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title2"))
                .andExpect(jsonPath("$.content[1].title").value("title1"));
    }
    @Test
    public void searchQuestionsByCategories() throws Exception {
        mockMvc.perform(get("/search")
                        .param("q", "testCategory1,testCategory2")
                        .param("type", "category")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title3"))
                .andExpect(jsonPath("$.content[1].title").value("title2"))
                .andExpect(jsonPath("$.content[2].title").value("title1"));
    }
}