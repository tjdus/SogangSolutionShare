package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.AnswerLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {
    Optional<AnswerLike> findByAnswerIdAndMemberId(Long answerId, Long memberId);
}
