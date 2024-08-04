package me.dong.aws.s3.operation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;

/*
Presigned URL을 사용하면 multipart download가 가능하므로 성능 향상에 도움이 된다
 */
@Slf4j
@Component
public class AwsS3FileDownloader {
    private final S3AsyncClient s3Client;

    public AwsS3FileDownloader(S3AsyncClient s3AsyncClient) {
        this.s3Client = s3AsyncClient;
    }

    /*
    object의 inputstream을 읽어와서 StreamingResponseBody를 이용해 streaming
    약간의 memory를 사용
     */
    public StreamingResponseBody download(String bucket, String objectKey) {
        return outputStream -> {
            log.info("download start");
            try (outputStream; var inputStream = s3Client.getObject(b -> b.bucket(bucket).key(objectKey),
                    AsyncResponseTransformer.toBlockingInputStream()).join()) {
                log.info("downloading");
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            log.info("download completed");
        };
    }
}
