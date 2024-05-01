package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByKakaoId(Long kakaoId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);
}
