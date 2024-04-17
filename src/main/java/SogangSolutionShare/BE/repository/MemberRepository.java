package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByKakaoId(Long kakaoId);
}
