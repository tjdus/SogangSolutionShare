package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Question;
import SogangSolutionShare.BE.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
    Question findOneById(Long id);
    Page<Question> findByCategoryIn(List<Category> categories, Pageable pageable);
    Page<Question> findByCategory(Category category, Pageable pageable);
    Page<Question> findByIdIn(List<Long> ids, Pageable pageable);
    @Query("SELECT q FROM Question q WHERE q.title LIKE %:query% OR q.content LIKE %:query%")
    Page<Question> findByTitlesAndContents(@Param("query") String query, Pageable pageable);

    Page<Question> findByTitleContains(String title, Pageable pageable);

    Page<Question> findByContentContains(String content, Pageable pageable);

    Page<Question> findByQuestionTagsTagIn(List<Tag> tags, Pageable pageable);

    Optional<Question> findByTitle(String title);

    @EntityGraph(attributePaths = {"questionLike"})
    Page<Question> findAllByMember(Member member, Pageable pageable);
}
