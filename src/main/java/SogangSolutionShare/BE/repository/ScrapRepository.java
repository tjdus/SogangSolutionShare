package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByQuestionIdAndMemberId(Long questionId, Long memberId);
}
