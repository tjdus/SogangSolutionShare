package SogangSolutionShare.BE.domain;

import SogangSolutionShare.BE.domain.dto.TagDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<QuestionTag> questionTags;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<MemberTag> memberTags;

    public Tag(String tagName) {
        this.name = tagName;
    }

    public TagDTO toDTO() {
        return TagDTO.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
