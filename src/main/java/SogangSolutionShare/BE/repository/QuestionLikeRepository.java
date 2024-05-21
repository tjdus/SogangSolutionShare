package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.QuestionLike;
import SogangSolutionShare.BE.domain.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Long> {
    Optional<QuestionLike> findByQuestionIdAndMemberId(Long questionId, Long memberId);
}
