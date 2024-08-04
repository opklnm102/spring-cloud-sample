package me.dong.aws.s3.service;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;
import me.dong.aws.s3.operation.AwsS3TransferManagerDownloader;
import me.dong.aws.s3.operation.FileUploader;
import me.dong.aws.s3.operation.FileUtils;
import me.dong.aws.s3.repository.FileDownloadRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/*
MultipartFile, File을 이용해 disk에 저장하여 전달하는 방식
S3TransferManager의 속도가 빠르다
S3 --> app --> browser로 시간 소요
disk에 저장하므로 disk space 관리 필요
 */
@Service
@Slf4j
public class MultipartStorageService {
    private final FileDownloadRepository fileDownloadRepository;
    private final FileUploader fileUploader;
    private final AwsS3TransferManagerDownloader awsS3TransferManagerDownloader;

    public MultipartStorageService(FileDownloadRepository fileDownloadRepository,
                                   @Qualifier("awsS3TransferManagerUploader") FileUploader fileUploader,
                                   AwsS3TransferManagerDownloader awsS3TransferManagerDownloader) {
        this.fileDownloadRepository = fileDownloadRepository;
        this.fileUploader = fileUploader;
        this.awsS3TransferManagerDownloader = awsS3TransferManagerDownloader;
    }

    public void upload(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            upload(file);
        }
    }

    private void upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BaseException(ErrorType.FILE_UPLOAD_FAILED);
        }
        var destination = Paths.get(file.getOriginalFilename());
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            fileUploader.upload("yanolja-dev-pf-bucket", "partner-payment/test", destination.toFile());
        } catch (IOException e) {
            throw new BaseException(ErrorType.FILE_UPLOAD_FAILED, e);
        } finally {
            FileUtils.delete(destination);
        }
    }

    public Pair<String, Resource> download(String downloadKey) {
        var fileDownload = fileDownloadRepository.get(downloadKey)
                                                 .orElseThrow();

        return Pair.of(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + FileUtils.getFileName(fileDownload.key()), awsS3TransferManagerDownloader.download(fileDownload.bucket(), fileDownload.key()));
    }

    @Async
    public CompletableFuture<Pair<String, Resource>> downloadAsync(String downloadKey) {
        return CompletableFuture.completedFuture(download(downloadKey));
    }
}
