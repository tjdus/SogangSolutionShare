package SogangSolutionShare.BE;

import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.repository.CategoryRepository;
import SogangSolutionShare.BE.repository.MemberRepository;
import SogangSolutionShare.BE.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import java.util.TimeZone;

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

    @PostConstruct
    void time() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
