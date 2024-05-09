package SogangSolutionShare.BE.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createAnswer() throws Exception {
        /*mockMvc.perform(post("/answer")
                        .param("questionId", "1")
                        .param("memberId", "1")
                        .param("content", "test content1"))
                .andExpect(status().isOk());*/
    }

    @Test
    public void updateAnswer() {
    }
}