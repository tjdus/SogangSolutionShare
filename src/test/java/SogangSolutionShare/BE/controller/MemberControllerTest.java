package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void createMember() throws Exception {
        mockMvc.perform(post("/api/join")
                        .param("loginId", "tester1")
                        .param("password", "1234")
                        .param("name", "tester2")
                        .param("email", "tester1@naver.com")
                        .param("isAuthorized", "true"))
                .andExpect(status().isCreated());

        memberRepository.findByLoginId("tester1").ifPresent(
                member -> {
                    assertEquals(member.getName(), "tester2");
                    assertEquals(member.getEmail(), "tester1@naver.com");
                }
        );
    }

    @Test
    public void getQuestions() {

    }

}