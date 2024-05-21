package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Answer;
import SogangSolutionShare.BE.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestionIdAndIdx(Long questionId, Integer idx);

    Page<Answer> findAllByQuestion(Question question, Pageable pageable);

    List<Answer> findByQuestionId(Long id);
}
