package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.SearchCriteria;
import SogangSolutionShare.BE.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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

    private Pageable createPageable(Integer page, Integer size, String orderBy){
        Sort sort;
        switch (orderBy) {
            case "most-liked":
                sort = Sort.by("likeCount").descending();
                break;
            case "latest":
            default:
                sort = Sort.by("createdAt").descending();
                break;
        }
        return PageRequest.of(page, size, sort);
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
                        .likeCount(0L)
                        .viewCount(0L)
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

    public QuestionDTO findQuestionById(Long questionId) {
        Question question = questionRepository.findOneById(questionId);
        return convertToDTO(question);
    }

    public void updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());
        question.setUpdatedAt(LocalDateTime.now());

        log.info("Question updated: {}", question);
    }


    public Page<QuestionDTO> findQuestionsByMemberId(Long memberId, Pageable pageable) {
        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member does not exist"));
        Page<Question> questionPage = questionRepository.findAllByMemberId(member.getId(), pageable);
        return questionPage.map(this::convertToDTO);
    }
    public Page<QuestionDTO> findQuestionsByCategoryId(Criteria criteria, Long categoryId) {
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Category category = categoryRepository.findById(categoryId).orElse(null);

        Page<Question> questionPage = questionRepository.findByCategory(category, pageable);
        return questionPage.map(this::convertToDTO);
    }
    public Page<QuestionDTO> findQuestionsByTagId(Criteria criteria, Long tagId) {
        Pageable pageable =createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Tag tag = tagRepository.findById(tagId).orElse(null);

        Page<Question> questionPage = findQuestionsByTags(List.of(tag), pageable);
        return questionPage.map(this::convertToDTO);
    }

    public Page<QuestionDTO> findQuestions(Criteria criteria){
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());

        Page<Question> questionPage = questionRepository.findAll(pageable);
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
    private Page<Question> findQuestionsByCategories(List<Category> categories, Pageable pageable) {
        return questionRepository.findByCategoryIn(categories, pageable);
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


    private Page<QuestionDTO> searchByTitlesAndContents(String query, Pageable pageable) {
        Page<Question> questionPage = questionRepository.findByTitlesAndContents(query, pageable);
        return questionPage.map(this::convertToDTO);
    }
    private Page<QuestionDTO> searchByTags(String tagString, Pageable pageable) {
        List<String> tagNames = tagString != null ? Arrays.asList(tagString.split(",")) : List.of();
        List<Tag> tags = getTagsByNames(tagNames);
        Page<Question> questionPage = findQuestionsByTags(tags, pageable);
        return questionPage.map(this::convertToDTO);

    }

    private List<Tag> getTagsByNames(List<String> tagNames) {
        return tagNames.stream()
                .map(tagRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Page<QuestionDTO> searchByCategories(String categoryString, Pageable pageable) {
        List<String> categoryNames = categoryString != null ? Arrays.asList(categoryString.split(",")) : List.of();
        List<Category> categories = getCategoriesByNames(categoryNames);
        Page<Question> questionPage = findQuestionsByCategories(categories, pageable);
        return questionPage.map(this::convertToDTO);
    }

    private List<Category> getCategoriesByNames(List<String> categoryNames) {
        return categoryNames.stream()
                .map(categoryRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Page<QuestionDTO> searchQuestions(SearchCriteria searchCriteria) {
        String searchType = searchCriteria.getType();
        Pageable pageable = createPageable(searchCriteria.getPage(), searchCriteria.getSize(), searchCriteria.getOrderBy());
        return switch (searchType) {
            case "tag" -> searchByTags(searchCriteria.getQ(), pageable);
            case "category" -> searchByCategories(searchCriteria.getQ(), pageable);
            default -> searchByTitlesAndContents(searchCriteria.getQ(), pageable);
        };
    }

    @Transactional
    public void updateViewCount(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        question.addViewCount();
    }


}
