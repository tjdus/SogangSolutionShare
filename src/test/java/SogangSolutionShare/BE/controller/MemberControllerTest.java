package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
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
                        .param("loginID", "tester1")
                        .param("password", "1234")
                        .param("name", "tester2")
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
    @Test
    public void getMembers() throws Exception {
        mockMvc.perform(post("/join")
                        .param("loginID", "tester2")
                        .param("password", "1234")
                        .param("name", "tester2")
                        .param("email", "tester1@naver.com"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/join")
                        .param("loginID", "tester3")
                        .param("password", "1234")
                        .param("name", "tester3")
                        .param("email", "tester2@naver.com"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/member/members"))
                .andExpect(status().isOk())  // 상태가 200 OK인지 확인합니다.
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("tester1"))
                .andExpect(jsonPath("$[1].name").value("tester2"));
    }
}