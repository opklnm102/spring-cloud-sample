package me.dong.aws.s3.service;

import lombok.RequiredArgsConstructor;
import me.dong.aws.s3.repository.FileDownloadRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

/*
upload에서 presign URL 사용시 bucket에서 CORS 설정 필요

browser -> app -> S3에서 app을 경유하며 발생하는 비효율을 제거
presign URL을 통해 browser -> S3로 직접 접근
 */
@Service
@RequiredArgsConstructor
public class PresignUrlService {
    private final S3Presigner s3Presigner;
    private final FileDownloadRepository fileDownloads;

    public String upload() {
        return "";
    }

    public String download(String downloadKey) {
        var fileDownload = fileDownloads.get(downloadKey)
                                        .orElseThrow();
        
        var getObjectRequest = GetObjectRequest.builder()
                                               .bucket(fileDownload.bucket())
                                               .key(fileDownload.key())
                                               .build();

        return s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                                                                   .signatureDuration(Duration.ofMinutes(5))
                                                                   .getObjectRequest(getObjectRequest)
                                                                   .build()).url().toString();
    }
}

/*
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }


     @GetMapping(value = "/test2")
    public ResponseEntity<String> test2() {
        var getObjectRequest = GetObjectRequest.builder()
                .bucket("yanolja-dev-pf-bucket")
                .key("search-dump-qa/dump/roomtypelist/2022/09/13/14/2022091314-log_roomtypelist.json")
                .build();

        var presignedUrl = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build()).url();

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, presignedUrl.toString())
                .build();
    }

 */
