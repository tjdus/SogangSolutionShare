package SogangSolutionShare.BE.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TagDTO {
    private Long id;
    private String name;


}
