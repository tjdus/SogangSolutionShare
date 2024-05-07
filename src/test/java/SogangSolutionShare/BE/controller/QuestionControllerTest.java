package SogangSolutionShare.BE.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    public void getAllQuestions() throws Exception {
        mockMvc.perform(post("/question")
                        .param("memberId", "1")
                        .param("categoryName", "미적분학")
                        .param("title", "test title1")
                        .param("content", "test content1")
                        .param("tags", "tag1", "tag2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/question/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].tags").isArray())  // 첫 번째 요소의 태그가 배열인지 확인
                .andExpect(jsonPath("$[1].tags[0]").value("tag1"))  // 첫 번째 태그의 값이 'tag1'인지 확인
                .andExpect(jsonPath("$[1].tags[1]").value("tag2"));

    }
}