package SogangSolutionShare.BE.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
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
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;


    @Test
    public void createQuestionLike() throws Exception {

        /*mockMvc.perform(post("/like/question")
                        .param("questionId", "1")
                        .param("memberId", "1"))
                .andExpect(status().isOk());*/
    }

    @Test
    public void createAnswerLike() {
    }
}