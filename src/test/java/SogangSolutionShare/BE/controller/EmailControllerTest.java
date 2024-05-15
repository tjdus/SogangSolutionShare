package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Temp;
import SogangSolutionShare.BE.domain.dto.EmailCheckDTO;
import SogangSolutionShare.BE.domain.dto.EmailRequestDTO;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.TempRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class EmailControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TempRepository tempRepository;
    private MockHttpSession session;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        Member member = new Member();
        member.setName("testMember");
        memberRepository.save(member);

        session = new MockHttpSession();
        session.setAttribute("loginMember", member);

        objectMapper = new ObjectMapper();
    }

    @Test
    public void 이메일_인증() throws Exception {
        // given
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setEmail("bon0057@naver.com");

        // when
        mockMvc.perform(post("/email/send")
                .session(session)
                .content(objectMapper.writeValueAsString(emailRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Temp temp = tempRepository.findByEmail(emailRequestDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        EmailCheckDTO emailCheckDTO = new EmailCheckDTO();
        emailCheckDTO.setEmail(emailRequestDTO.getEmail());
        emailCheckDTO.setAuthorizationNumber(temp.getAuthorizationNumber());

        // then
        mockMvc.perform(post("/email/check")
                .session(session)
                .content(objectMapper.writeValueAsString(emailCheckDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}