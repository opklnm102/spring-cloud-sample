package me.dong.aws.s3.operation;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * S3AsyncClient를 이용한 파일 업로드
 */
@Slf4j
@Component
public class AwsS3StreamUploader {
    private final S3AsyncClient s3Client;
    private final ExecutorService executorService;

    public AwsS3StreamUploader(S3AsyncClient s3AsyncClient) {
        this.s3Client = s3AsyncClient;
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * file을 S3 bucket에 저장
     *
     * @param filePath   file path in bucket
     * @param uploadFile file
     */
    public void upload(String bucket, String filePath, MultipartFile uploadFile) {
        var key = generateKey(filePath, uploadFile.getName());
        try (var inputStream = uploadFile.getInputStream()) {
            s3Client.putObject(b -> b.bucket(bucket).key(key), AsyncRequestBody.fromInputStream(builder -> builder.inputStream(inputStream).contentLength(uploadFile.getSize()).executor(executorService)))
                    .exceptionally(e -> {
                        throw new BaseException(ErrorType.FILE_UPLOAD_FAILED, e);
                    })
                    .join();
        } catch (IOException e) {
            throw new BaseException(ErrorType.FILE_UPLOAD_FAILED, e);
        }
    }

    private String generateKey(String filePath, String fileName) {
        return String.format("%s/%s", filePath, fileName);
    }
}
