package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByQuestionIdAndMemberId(Long questionId, Long memberId);


    @EntityGraph(attributePaths = {"question"})
    Page<Scrap> findAllByMember(Member member, Pageable pageable);
}
