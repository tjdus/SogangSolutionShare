package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.annotation.Login;
import SogangSolutionShare.BE.domain.Member;
import SogangSolutionShare.BE.domain.dto.ScrapDTO;
import SogangSolutionShare.BE.service.ScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    // 스크랩 생성 API
    @PostMapping
    public ResponseEntity<Void> createScrap(@Login Member loginMember, @RequestBody ScrapDTO scrapDTO) {
        scrapService.createScrap(loginMember.getId(), scrapDTO.getQuestionId());
        return ResponseEntity.ok().build();
    }
    // 스크랩 삭제 API
    @DeleteMapping
    public ResponseEntity<Void> deleteScrap(@Login Member loginMember, @RequestBody ScrapDTO scrapDTO) {
        scrapService.deleteScrap(loginMember.getId(), scrapDTO.getQuestionId());
        return ResponseEntity.ok().build();
    }

    // 내가 스크랩한 질문 인지 확인하는 API
    @GetMapping("/{questionId}")
    public ResponseEntity<Boolean> isScrap(@Login Member loginMember, @PathVariable Long questionId) {
        return ResponseEntity.ok(scrapService.isUserScrap(loginMember.getId(), questionId));
    }


}
