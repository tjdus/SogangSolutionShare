package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;


    public void createQuestion(QuestionDTO questionDTO) {
        Long memberId = questionDTO.getMemberId();

        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));

        // CategoryName 로 Category 찾아서 없으면 예외처리
        Category category = categoryRepository.findByName(questionDTO.getCategoryName()).orElseThrow(() -> new IllegalArgumentException("Category does not exist"));

        // TagName으로 Tag 찾아서 없으면 태그 생성
        List<String> tags = questionDTO.getTags();
        tags.forEach(tagName -> {
            tagRepository.findByName(tagName).orElseGet(() -> {
                return tagRepository.save(new Tag(tagName));
            });
        });


        log.info("Member: {}", member);
        log.info("Category: {}", category);

        // Member 존재하면 Question 생성하고 저장
        Question createdQuestion = Question.builder()
                        .member(member)
                        .category(category)
                        .title(questionDTO.getTitle())
                        .content(questionDTO.getContent())
                        .build();

        log.info("Question created: {}", createdQuestion);

        questionRepository.save(createdQuestion);

        // Question과 Tag 연결
        tags.forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName).orElseThrow(() -> new IllegalArgumentException("Tag does not exist"));
            questionTagRepository.save(QuestionTag.builder()
                    .question(createdQuestion)
                    .tag(tag)
                    .build());
        });


    }

    public void updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());

        log.info("Question updated: {}", question);
    }

    public List<Question> getQuestions(Long memberId) {
        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));

        // Member 존재하면 Member의 Question들을 반환
        return questionRepository.findAllByMemberId(member.getId());

    }

    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        questionRepository.delete(question);

        log.info("Question deleted: {}", question);
    }
}
