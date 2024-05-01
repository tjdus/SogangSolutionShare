package SogangSolutionShare.BE;

import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.repository.CategoryRepository;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
@RequiredArgsConstructor
public class BeApplication {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

	public static void main(String[] args) {
		SpringApplication.run(BeApplication.class, args);
	}

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Member member = new Member();
        member.setName("tester1");
        member.setEmail("tester1@naver.com");

        memberRepository.save(member);

        Category category = new Category();
        category.setName("미적분학");
        categoryRepository.save(category);


        Question question = new Question();
        question.setMember(member);
        question.setCategory(category);
        question.setTitle("미적분학 문제");
        question.setContent("미적분학 문제입니다.");
        questionRepository.save(question);

    }

}
