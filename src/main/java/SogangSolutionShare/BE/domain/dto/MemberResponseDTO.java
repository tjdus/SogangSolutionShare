package SogangSolutionShare.BE.domain.dto;

import SogangSolutionShare.BE.domain.Member;
import lombok.Data;

@Data
public class MemberResponseDTO {
    private String loginId;
    private String email;
    private String name;

    public MemberResponseDTO(Member member) {
        this.loginId = member.getLoginId();
        this.email = member.getEmail();
        this.name = member.getName();
    }
}
