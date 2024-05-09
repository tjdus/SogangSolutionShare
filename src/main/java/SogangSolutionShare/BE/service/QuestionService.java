package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;

    private QuestionDTO convertToDTO(Question question) {
        List<String> tagNames = questionTagRepository.findByQuestionId(question.getId())
                .stream()
                .map(qt -> qt.getTag().getName())
                .collect(Collectors.toList());
        return question.toDTO(tagNames);
    }

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

    public QuestionDTO findQuestion(Long questionId) {
        Question question = questionRepository.findOneById(questionId);
        return convertToDTO(question);
    }

    public void updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());

        log.info("Question updated: {}", question);
    }


    public Page<QuestionDTO> findQuestionsByMemberId(Long memberId, Pageable pageable) {
        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));
        Page<Question> questionPage = questionRepository.findAllByMemberId(member.getId(), pageable);
        return questionPage.map(this::convertToDTO);
    }

    public Page<QuestionDTO> findQuestions(Optional<String> categoryName, Optional<List<String>> tagNames, Pageable pageable){
        Category category = categoryName
                .flatMap(categoryRepository::findByName)
                .orElse(null);
        List<Tag> tags = tagNames
                .map(names -> names.stream()
                        .map(tagRepository::findByName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .orElse(null);

        Page<Question> questionPage;
        if (category != null && tags != null && !tags.isEmpty()) {
            questionPage = findQuestionsByCategoryAndTags(category, tags, pageable);
        } else if (category != null) {
            questionPage = findQuestionsByCategory(category, pageable);
        } else if (tags != null && !tags.isEmpty()) {
            questionPage = findQuestionsByTags(tags, pageable);
        } else {
            questionPage = questionRepository.findAll(pageable);
        }
        return questionPage.map(this::convertToDTO);
    }

    private Page<Question> findQuestionsByTags(List<Tag> tags, Pageable pageable) {
        List<Long> questionIds = questionTagRepository.findByTagIn(tags).stream()
                .map(QuestionTag::getQuestion)
                .map(Question::getId)
                .distinct()
                .collect(Collectors.toList());

        return questionRepository.findByIdIn(questionIds, pageable);
    }
    private Page<Question> findQuestionsByCategory(Category category, Pageable pageable) {
        return questionRepository.findByCategory(category, pageable);
    }
    private Page<Question> findQuestionsByCategoryAndTags(Category category, List<Tag> tags, Pageable pageable){
        List<QuestionTag> questionTags = questionTagRepository.findByTagIn(tags);

        List<Long> questionIds = questionTags.stream()
                .map(QuestionTag::getQuestion)
                .filter(question -> category.equals(question.getCategory())) // 카테고리 필터링
                .map(Question::getId)
                .distinct()
                .collect(Collectors.toList());
        return questionRepository.findByIdIn(questionIds, pageable);
    }

    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        questionRepository.delete(question);

        log.info("Question deleted: {}", question);
    }
}
