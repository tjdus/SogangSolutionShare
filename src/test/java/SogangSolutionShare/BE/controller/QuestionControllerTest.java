package SogangSolutionShare.BE.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void createQuestion() throws Exception {
       mockMvc.perform(post("/question")
                       .param("memberId", "1")
                       .param("categoryName", "미적분학")
                       .param("title", "test title1")
                       .param("content", "test content1")
                       .param("tags", "tag1", "tag2"))
               .andExpect(status().isOk());
    }

    @Test
    public void updateQuestion() {
    }

    @Test
    public void deleteQuestion() {
    }
}