package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.*;

import SogangSolutionShare.BE.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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

    @Before
    public void setUp() {
        questionTagRepository.deleteAll();
        questionRepository.deleteAll();
        tagRepository.deleteAll();
        categoryRepository.deleteAll();
        memberRepository.deleteAll();

        Member member1 = new Member();
        member1.setName("testMember");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("testMember2");
        memberRepository.save(member2);

        Category category1 = new Category();
        category1.setName("testCategory1");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("testCategory2");
        categoryRepository.save(category2);

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        Question question1 = new Question(null, member1, category1, "title1", "content1", null, null, 5L);
        Question question2 = new Question(null, member1, category1, "title2", "content2", null, null, 10L);
        Question question3 = new Question(null, member2, category2, "title3", "content3", null, null, 7L);

        questionRepository.saveAll(Arrays.asList(question1, question2, question3));

        QuestionTag questionTag1 = new QuestionTag(null, question1, tag1, null);
        QuestionTag questionTag2 = new QuestionTag(null, question1, tag2, null);

        QuestionTag questionTag3 = new QuestionTag(null, question2, tag1, null);
        QuestionTag questionTag4 = new QuestionTag(null, question2, tag3, null);

        QuestionTag questionTag5 = new QuestionTag(null, question3, tag2, null);

        questionTagRepository.saveAll(Arrays.asList(questionTag1, questionTag2, questionTag3, questionTag4, questionTag5));

        session = new MockHttpSession();
        session.setAttribute("loginMember", member1);

        /*
        {
          "memberId": 1,
          "categoryName": "testCategory1",
          "title": "title1",
          "content": "content1",
          "likeCount": 5,
          "tags": ["tag1", "tag2"]
        }
        {
          "memberId": 1,
          "categoryName": "testCategory1",
          "title": "title2",
          "content": "content2",
          "likeCount": 5,
          "tags": ["tag1", "tag3"]
        }
        */

        /*
        {
          "memberId": 2,
          "categoryName": "testCategory2",
          "title": "title3",
          "content": "content3",
          "likeCount": 7,
          "tags": ["tag2"]
        }
        */
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