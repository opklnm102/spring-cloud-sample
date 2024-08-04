package me.dong.aws.s3.controller;

import lombok.RequiredArgsConstructor;
import me.dong.aws.s3.service.PresignUrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v3/files")
public class FileV3Controller {
    private final PresignUrlService presignUrlService;

    @PostMapping(value = "/upload")
    public ResponseEntity<Void> upload() {
        presignUrlService.upload();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/download/{downloadKey}")
    public ResponseEntity<String> download(@PathVariable("downloadKey") String downloadKey) {
        return ResponseEntity.status(HttpStatus.FOUND)
                             .header(HttpHeaders.LOCATION, presignUrlService.download(downloadKey))
                             .build();
    }
}
/*
Presign URL을 사용
upload download 처리는 S3에 위임

app에서 처리하므로 upload, download를 중계하게 되어 layency가 늘어난다

 */
