package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.domain.dto.ScrapDTO;
import SogangSolutionShare.BE.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    // 스크랩 생성 API
    @PostMapping
    public ResponseEntity<Void> createScrap(@RequestBody ScrapDTO scrapDTO) {
        scrapService.createScrap(scrapDTO.getMemberId(), scrapDTO.getQuestionId());
        return ResponseEntity.ok().build();
    }
    // 스크랩 삭제 API
    @DeleteMapping
    public ResponseEntity<Void> deleteScrap(@RequestBody ScrapDTO scrapDTO) {
        scrapService.deleteScrap(scrapDTO.getMemberId(), scrapDTO.getQuestionId());
        return ResponseEntity.ok().build();
    }


}
