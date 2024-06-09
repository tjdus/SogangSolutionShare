package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.MemberTag;
import SogangSolutionShare.BE.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

    @EntityGraph(attributePaths = {"tag"})
    Page<MemberTag> findAllByMember(Member member, Pageable pageable);

    Optional<MemberTag> findByMemberAndTag(Member member, Tag tag);

    void deleteByMemberAndTag(Member member, Tag tag);

}
