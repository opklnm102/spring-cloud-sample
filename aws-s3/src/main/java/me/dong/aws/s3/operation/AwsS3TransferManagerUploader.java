package me.dong.aws.s3.operation;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Created by huekim on 2019. 11. 06..
 * <p>
 * S3AsyncClient를 이용한 파일 업로드
 */
@Slf4j
@Component
public class AwsS3TransferManagerUploader implements FileUploader {
    private final S3TransferManager s3TransferManager;

    public AwsS3TransferManagerUploader(S3TransferManager s3TransferManager) {
        this.s3TransferManager = s3TransferManager;
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
        s3TransferManager.uploadFile(builder -> builder.putObjectRequest(b -> b.bucket(bucket).key(key))
                                                       .addTransferListener(LoggingTransferListener.create())
                                                       .source(uploadFile))
                         .completionFuture()
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
        try {
            upload(bucket, filePath, Files.writeString(Paths.get(fileName), encodedContent).toFile());
        } catch (IOException e) {
            throw new BaseException(ErrorType.FILE_UPLOAD_FAILED, e);
        }
    }

    private String generateKey(String filePath, String fileName) {
        return String.format("%s/%s", filePath, fileName);
    }
}
