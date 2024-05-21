package SogangSolutionShare.BE.domain;

import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    @Lob
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedDate
    private LocalDateTime updatedAt;

    @Builder.Default
    @Min(0)
    private Long likeCount = 0L;
    @Builder.Default
    @Min(0)
    private Long viewCount = 0L;
    @Builder.Default
    @Min(0)
    private Long answerCount = 0L;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionTag> questionTags = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    public QuestionDTO toDTO(){
        List<String> tagNames = questionTags.stream()
                .map(questionTag -> questionTag.getTag().getName())
                .collect(Collectors.toList());

        return QuestionDTO.builder()
                .id(id)
                .loginId(member.getLoginId())
                .title(title)
                .content(content)
                .category(category.getName())
                .tags(tagNames)
                .likeCount(likeCount)
                .viewCount(viewCount)
                .answerCount(answerCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addQuestionTag(Tag tag) {
        QuestionTag questionTag = QuestionTag.builder()
                .question(this)
                .tag(tag)
                .build();
        questionTags.add(questionTag);
    }
    public void clearQuestionTags() {
        questionTags.forEach(questionTag -> {
            questionTag.setTag(null);
            questionTag.setQuestion(null);
        });
        this.questionTags.clear();
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
        setAnswerCount(answerCount+1);
    }

    public void deleteAnswer(Answer answer) {
        answers.remove(answer);
        setAnswerCount(answerCount-1);
    }

}
