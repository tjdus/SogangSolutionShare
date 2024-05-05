package SogangSolutionShare.BE.domain.dto;

import lombok.Data;

@Data
public class JoinDTO {
    private String loginId;
    private String password;
    private String name;
    private String email;
}
