package SogangSolutionShare.BE.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {

    @NotEmpty(message = "로그인 아이디를 입력해주세요.")
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
}
