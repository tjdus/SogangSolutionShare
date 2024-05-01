package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 가입 API")
    @Test
    public void createMember() throws Exception {
        mockMvc.perform(post("/member")
                        .param("name", "tester1")
                        .param("email", "tester1@naver.com"))
                .andExpect(status().isOk());

        memberRepository.findById(1L).ifPresent(
                member -> {
                    assertEquals(member.getName(), "tester1");
                    assertEquals(member.getEmail(), "tester1@naver.com");
                }
        );
    }

    @Test
    public void getQuestions() {
    }
}