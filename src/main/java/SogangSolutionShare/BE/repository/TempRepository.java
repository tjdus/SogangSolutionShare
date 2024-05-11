package SogangSolutionShare.BE.repository;

import SogangSolutionShare.BE.domain.Temp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempRepository extends JpaRepository<Temp, Long> {
    Optional<Temp> findByEmail(String email);
}
