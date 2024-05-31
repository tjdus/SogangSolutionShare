package SogangSolutionShare.BE.repository;


import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.QuestionTag;
import SogangSolutionShare.BE.domain.Tag;
import SogangSolutionShare.BE.domain.dto.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
    List<QuestionTag> findByQuestionId(Long questionId);
    List<QuestionTag> findByTagIn(List<Tag> tags);
}
