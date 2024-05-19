package SogangSolutionShare.BE.controller;

import SogangSolutionShare.BE.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;

    @PostMapping("/test")
    public ResponseEntity<Void> test(@RequestParam("file") MultipartFile file) {

        s3Service.upload(file);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/test")
    public ResponseEntity<Void> delete(@RequestParam("file") String file) {
        s3Service.delete(file);
        return ResponseEntity.ok().build();
    }
}
