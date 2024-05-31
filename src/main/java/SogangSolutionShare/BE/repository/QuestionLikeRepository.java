package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.QuestionLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Long> {
    Optional<QuestionLike> findByQuestionIdAndMemberId(Long questionId, Long memberId);

    @EntityGraph(attributePaths = {"question"})
    Page<QuestionLike> findAllByMember(Member member, Pageable pageable);
}
