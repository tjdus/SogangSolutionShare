package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.Criteria;
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
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TagControllerTest {
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
        List<String> tagNames = List.of("tag1", "tag2", "tag3");

        List<Tag> tagList = tagNames.stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .toList();

        Question question1 = Question.builder()
                .member(member)
                .title("title1")
                .content("content")
                .category(category)
                .build();
        question1.addQuestionTag(tagRepository.findByName("tag1").orElse(null));
        question1.addQuestionTag(tagRepository.findByName("tag2").orElse(null));

        questionRepository.save(question1);

        Question question2 = Question.builder()
                .member(member)
                .title("title2")
                .content("content")
                .category(category)
                .build();

        question1.addQuestionTag(tagRepository.findByName("tag1").orElse(null));
        question1.addQuestionTag(tagRepository.findByName("tag3").orElse(null));
        questionRepository.save(question2);
    }

    @Test
    public void searchByTags() throws Exception {
        Criteria criteria = new Criteria(0, 10, "latest");
        Page<QuestionDTO> questions = questionService.findQuestionsByTags(criteria, "tag1+tag2");
        for (QuestionDTO question : questions.getContent()) {
            assertTrue(question.getTags().contains("tag1"));
        }
    }
}
