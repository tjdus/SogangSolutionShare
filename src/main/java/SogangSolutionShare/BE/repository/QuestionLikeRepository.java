package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.QuestionLike;
import SogangSolutionShare.BE.domain.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Long> {
    QuestionLike findByQuestionIdAndMemberId(Long questionId, Long memberId);
}
