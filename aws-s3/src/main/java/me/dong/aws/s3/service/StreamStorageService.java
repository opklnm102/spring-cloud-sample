package me.dong.aws.s3.service;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;
import me.dong.aws.s3.operation.AwsS3FileDownloader;
import me.dong.aws.s3.operation.AwsS3StreamUploader;
import me.dong.aws.s3.operation.FileUtils;
import me.dong.aws.s3.repository.FileDownloadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.utils.Pair;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Slf4j
public class StreamStorageService {
    private final FileDownloadRepository fileDownloadRepository;
    private final AwsS3FileDownloader fileDownloader;

    private final AwsS3StreamUploader fileUploader;

    public StreamStorageService(FileDownloadRepository fileDownloadRepository,
                                AwsS3StreamUploader fileUploader,
                                AwsS3FileDownloader fileDownloader) {
        this.fileDownloadRepository = fileDownloadRepository;
        this.fileUploader = fileUploader;
        this.fileDownloader = fileDownloader;
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
        fileUploader.upload("yanolja-dev-pf-bucket", "partner-payment/test", file);
    }

    public Pair<String, StreamingResponseBody> download(String downloadKey) {
        var fileDownload = fileDownloadRepository.get(downloadKey)
                                                 .orElseThrow();

        return Pair.of(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + FileUtils.getFileName(fileDownload.key()), fileDownloader.download(fileDownload.bucket(), fileDownload.key()));
    }
}
