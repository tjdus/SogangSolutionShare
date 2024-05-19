package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Category;
import SogangSolutionShare.BE.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);
    Question findOneById(Long id);
    Page<Question> findByCategoryIn(List<Category> categories, Pageable pageable);
    Page<Question> findByCategory(Category category, Pageable pageable);
    Page<Question> findByIdIn(List<Long> ids, Pageable pageable);
    @Query("SELECT q FROM Question q WHERE q.title LIKE %:query% OR q.content LIKE %:query%")
    Page<Question> findByTitlesAndContents(@Param("query") String query, Pageable pageable);
}
