package me.dong.aws.s3.operation;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static software.amazon.awssdk.transfer.s3.SizeConstant.GB;

/**
 * Created by huekim on 2019. 11. 11..
 */
@Slf4j
@Component
public class AwsS3TransferManagerDownloader {
    private final S3TransferManager s3TransferManager;

    public AwsS3TransferManagerDownloader(S3TransferManager s3TransferManager) {
        this.s3TransferManager = s3TransferManager;
    }

    public Resource download(String bucket, String objectKey) {
        var destinationFile = Paths.get(FileUtils.getFileName(objectKey) + LocalDateTime.now());
        s3TransferManager.downloadFile(builder -> builder.getObjectRequest(b -> b.bucket(bucket).key(objectKey))
                                                         .addTransferListener(LoggingTransferListener.create())
                                                         .destination(destinationFile))
                         .completionFuture()
                         .thenAccept(result -> {
                             log.info("downloaded file: {}, eTag: {}", result.response().contentLength(), result.response().eTag());
                         })
                         .exceptionally(e -> {
                             throw new BaseException(ErrorType.FILE_DOWNLOAD_FAILED, e);
                         })
                         .join();

        try {
            return new InputStreamResource(Files.newInputStream(destinationFile));
        } catch (IOException e) {
            throw new BaseException(ErrorType.FILE_DOWNLOAD_FAILED, e);
        } finally {
            FileUtils.delete(destinationFile.toFile());
        }
    }
}


