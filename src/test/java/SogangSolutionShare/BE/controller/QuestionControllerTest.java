package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.*;

import SogangSolutionShare.BE.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    public MockMvc mockMvc;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private QuestionTagRepository questionTagRepository;

    @BeforeEach
    public void setUp() {
        questionTagRepository.deleteAll();
        questionRepository.deleteAll();
        tagRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();

        Member member = new Member();
        member.setName("testMember");
        memberRepository.save(member);
        Category category = new Category();
        category.setName("testCategory");
        categoryRepository.save(category);
        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));

        Question question1 = new Question(null, member, category, "title1", "content1", null, null, 5L);
        Question question2 = new Question(null, member, category, "title2", "content2", null, null, 10L);
        questionRepository.saveAll(Arrays.asList(question1, question2));

        QuestionTag questionTag1 = new QuestionTag(null, question1, tag1, null);
        QuestionTag questionTag2 = new QuestionTag(null, question1, tag2, null);
        questionTagRepository.saveAll(Arrays.asList(questionTag1, questionTag2));
    }
//    @Test
//    public void createQuestion() throws Exception {
//       mockMvc.perform(post("/question")
//                       .param("memberId", "1")
//                       .param("categoryName", "미적분학")
//                       .param("title", "test title1")
//                       .param("content", "test content1")
//                       .param("tags", "tag1", "tag2"))
//               .andExpect(status().isOk());
//    }

    @Test
    public void updateQuestion() {
    }

    @Test
    public void deleteQuestion() {
    }

    @Test
    public void getQuestion() throws Exception {
        mockMvc.perform(get("/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.tags[0]").value("tag1"));
    }
    @Test
    public void getQuestions() throws Exception {
        mockMvc.perform(get("/question/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("title2"))
                .andExpect(jsonPath("$.content[1].title").value("title1"))
                .andExpect(jsonPath("$.content[0].likeCount").value(10))
                .andExpect(jsonPath("$.content[1].likeCount").value(5));


    }

    @Test
    public void getQuestionsByMemberId() throws Exception {
        mockMvc.perform(get("/member/1/question"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].tags[0]").value("tag1"))
                .andExpect(jsonPath("$[0].tags[1]").value("tag2"));
    }

    @Test
    public void testGetQuestionsWithPaging() throws Exception {
        mockMvc.perform(get("/question/questions")
                        .param("page", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())  // HTTP 200 상태 코드 확인
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].tags[0]").value("tag1"));
        mockMvc.perform(get("/question/questions")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "likeCount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title2"))
                .andExpect(jsonPath("$.content[1].title").value("title1"));

        mockMvc.perform(get("/question/questions")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "likeCount,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title1"))
                .andExpect(jsonPath("$.content[1].title").value("title2"));
        mockMvc.perform(get("/question/questions")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "createdAt,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("title1"))
                .andExpect(jsonPath("$.content[1].title").value("title2"));
    }

}