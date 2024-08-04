package me.dong.aws.s3.operation;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by huekim on 2019. 11. 06..
 *
 * S3AsyncClient를 이용한 파일 업로드
 * 단일 object는 5TB까지 가능하나 PUT method가 5GB까지만 지원하여 용량 확인하여 multipart upload 필요
 */
@Slf4j
@Component
public class AwsS3Uploader implements FileUploader {
    private final S3AsyncClient s3Client;

    public AwsS3Uploader(S3AsyncClient s3AsyncClient) {
        this.s3Client = s3AsyncClient;
    }

    /**
     * file을 S3 bucket에 저장
     *
     * @param filePath   file path in bucket
     * @param uploadFile file
     */
    @Override
    public void upload(String bucket, String filePath, File uploadFile) {
        var key = generateKey(filePath, uploadFile.getName());
        s3Client.putObject(b -> b.bucket(bucket).key(key), AsyncRequestBody.fromFile(uploadFile))
                .exceptionally(e -> {
                    throw new BaseException(ErrorType.FILE_UPLOAD_FAILED, e);
                })
                .join();
    }

    /**
     * raw string을 based64 encoded string으로 S3 bucket에 저장
     *
     * @param filePath directory path in bucket
     * @param fileName file name
     * @param content  raw string
     * @return
     */
    @Override
    public void upload(String bucket, String filePath, String fileName, String content) {
        var encodedContent = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
        var key = generateKey(filePath, fileName);
        s3Client.putObject(b -> b.bucket(bucket).key(key), AsyncRequestBody.fromString(encodedContent))
                .exceptionally(e -> {
                    throw new BaseException(ErrorType.FILE_UPLOAD_FAILED, e);
                })
                .join();
    }

    private String generateKey(String filePath, String fileName) {
        return String.format("%s/%s", filePath, fileName);
    }
}
