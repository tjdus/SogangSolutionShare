package SogangSolutionShare.BE.service;

import SogangSolutionShare.BE.domain.*;
import SogangSolutionShare.BE.domain.dto.Criteria;
import SogangSolutionShare.BE.domain.dto.QuestionDTO;
import SogangSolutionShare.BE.domain.dto.QuestionRequestDTO;
import SogangSolutionShare.BE.domain.dto.SearchCriteria;
import SogangSolutionShare.BE.exception.*;
import SogangSolutionShare.BE.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static SogangSolutionShare.BE.controller.PageableUtil.createPageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;

    private final QuestionLikeRepository questionLikeRepository;

    private final AttachmentRepository attachmentRepository;
    private final MemberTagRepository memberTagRepository;
    private final S3Service s3Service;

    @Transactional
    public QuestionDTO createQuestion(Long memberId, QuestionRequestDTO questionRequest) {
        log.info("QuestionRequest : {}", questionRequest);

        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // CategoryName으로 Category 찾아서 없으면 예외처리
        Category category = categoryRepository.findByName(questionRequest.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        // TagName으로 Tag 찾아서 없으면 태그 생성
        List<Tag> tagList = questionRequest.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .toList();

        tagList.forEach(tag -> memberTagRepository.findByMemberAndTag(member, tag)
                .orElseGet(() -> memberTagRepository.save(MemberTag.builder()
                        .member(member)
                        .tag(tag)
                        .build())));

        // 질문 생성
        Question createdQuestion = Question.builder()
                .member(member)
                .category(category)
                .title(questionRequest.getTitle())
                .content(questionRequest.getContent())
                .build();

        // 태그 저장
        tagList.forEach(createdQuestion::addQuestionTag);

        Question question = questionRepository.save(createdQuestion);
        member.getQuestions().add(question);

        // 첨부파일 저장
        if(questionRequest.getAttachments() == null) {
            questionRequest.setAttachments(new ArrayList<>());
        }
        else {
            questionRequest.getAttachments().forEach(attachment -> {
                String fileName = s3Service.upload(attachment);
                Attachment am = Attachment.builder()
                        .question(createdQuestion)
                        .fileName(fileName)
                        .build();
                Attachment save = attachmentRepository.save(am);
                createdQuestion.addAttachment(save);
            });
        }

        log.info("Question created: {}", createdQuestion);

        return createdQuestion.toDTO();
    }

    public QuestionDTO findQuestionById(Long questionId) {
        Question question = questionRepository.findOneById(questionId);
        return question.toDTO();
    }

    @Transactional
    public QuestionDTO updateQuestion(Long memberId, Long questionId, QuestionRequestDTO questionRequestDTO) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        // 글 수정 권한 확인
        if (!question.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }

        // 제목과 내용 수정
        question.setTitle(questionRequestDTO.getTitle());
        question.setContent(questionRequestDTO.getContent());

        // 카테고리 수정
        Category category = categoryRepository.findByName(questionRequestDTO.getCategory())
                .orElseThrow(CategoryNotFoundException::new);
        question.setCategory(category);

        // 기존 태그 제거
        question.clearQuestionTags();

        // 새로운 태그 추가
        List<Tag> tags = questionRequestDTO.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .toList();

        tags.forEach(question::addQuestionTag);

        log.info("Question updated: {}", question);

        return question.toDTO();
    }

    public Page<QuestionDTO> findQuestionsByMemberId(Long memberId, Criteria criteria) {
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        // memberId로 Member 찾아서 없으면 예외처리
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Page<Question> questionPage = questionRepository.findAllByMemberId(member.getId(), pageable);
        return questionPage.map(Question::toDTO);
    }
    public Page<QuestionDTO> findQuestionsByCategoryId(Criteria criteria, Long categoryId) {
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Category category = categoryRepository.findById(categoryId).orElse(null);

        Page<Question> questionPage = questionRepository.findByCategory(category, pageable);
        return questionPage.map(Question::toDTO);
    }
    public Page<QuestionDTO> findQuestionsByTagId(Criteria criteria, Long tagId) {
        Pageable pageable =createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Tag tag = tagRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
        Page<Question> questionPage = questionRepository.findByQuestionTagsTagIn(List.of(tag), pageable);
        return questionPage.map(Question::toDTO);
    }

    public Page<QuestionDTO> findQuestionsByTags(Criteria criteria, String tags) {
        String[] tagNames = tags.split("\\+");

        // 태그 이름에 해당하는 Tag 엔티티들을 조회하여 리스트에 저장
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresent(tagList::add);
        }
        Pageable pageable =createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Page<Question> questionPage = questionRepository.findByQuestionTagsTagIn(tagList, pageable);

        return questionPage.map(Question::toDTO);
    }

    public Page<QuestionDTO> findQuestions(Criteria criteria){
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());

        Page<Question> questionPage = questionRepository.findAll(pageable);
        return questionPage.map(Question::toDTO);
    }


    public void deleteQuestion(Long memberId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        // 글 삭제 권한 확인
        if (!question.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }
        questionRepository.delete(question);

        log.info("Question deleted: {}", question);
    }


    private Page<QuestionDTO> searchByTitlesAndContents(String query, Pageable pageable) {
        Page<Question> questionPage = questionRepository.findByTitlesAndContents(query, pageable);
        return questionPage.map(Question::toDTO);
    }


    private List<Tag> getTagsByNames(List<String> tagNames) {
        return tagNames.stream()
                .map(tagRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }



    private List<Category> getCategoriesByNames(List<String> categoryNames) {
        return categoryNames.stream()
                .map(categoryRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Page<QuestionDTO> searchByTitle(String title, Pageable pageable) {
        return questionRepository.findByTitleContains(title, pageable).map(Question::toDTO);
    }
    private Page<QuestionDTO> searchByContent(String content, Pageable pageable) {
        return questionRepository.findByContentContains(content, pageable).map(Question::toDTO);
    }
    public Page<QuestionDTO> searchQuestions(SearchCriteria searchCriteria) {
        String searchType = searchCriteria.getType();
        Pageable pageable = createPageable(searchCriteria.getPage(), searchCriteria.getSize(), searchCriteria.getOrderBy());
        return switch (searchType) {
            case "content" -> searchByContent(searchCriteria.getQ(), pageable);
            case "title+content" -> searchByTitlesAndContents(searchCriteria.getQ(), pageable);
            default -> searchByTitle(searchCriteria.getQ(), pageable);
        };
    }

    @Transactional
    public void updateViewCount(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question does not exist"));
        question.addViewCount();
    }


    @Transactional(readOnly = true)
    public Long getQuestionCount() {
        return questionRepository.count();
    }

    public Page<QuestionLike> findLikeQuestionsByMemberId(Long memberId, Criteria criteria) {
        Pageable pageable = createPageable(criteria.getPage(), criteria.getSize(), criteria.getOrderBy());
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        Page<QuestionLike> questionLikes = questionLikeRepository.findAllByMember(member, pageable);

        return questionLikes;
    }
}
