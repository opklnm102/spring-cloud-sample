package me.dong.aws.s3.operation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AwsS3Uploader test")
class AwsS3UploaderTest {
    private static final String bucket = "testing";

    private AwsS3Uploader sut;

    @Mock
    private S3AsyncClient s3AsyncClient;

    @BeforeEach
    void setUp() {
        sut = new AwsS3Uploader(s3AsyncClient);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("file object로 S3에 업로드 성공")
    void upload_success_file_object() throws Exception {
        // given
        var filePath = "test";
        var uploadFile = new File("test-upload-file");
        uploadFile.createNewFile();

        given(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class))).willReturn(CompletableFuture.completedFuture(null));

        // when
        sut.upload(bucket, filePath, uploadFile);

        // then
        verify(s3AsyncClient, times(1)).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
    }

    @Test
    @DisplayName("base64 encoded string으로 S3에 업로드 성공")
    void update_success_encoded_string() throws Exception {
        // given
        var filePath = "test";
        var fileName = "test-upload-file";
        var content = "aaabbbccc";

        given(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class))).willReturn(CompletableFuture.completedFuture(null));

        // when
        sut.upload(bucket, filePath, fileName, content);

        // then
        verify(s3AsyncClient, times(1)).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
    }

//    @Test
//    void readUploadUrl_S3에_업로드된_Object의_URL_조회() throws Exception {
//        // given :
//        var filePath = "test";
//        var fileName = "test-upload-file";
//
//        var key = String.format("%s/%s", filePath, fileName);
//        when(s3AsyncClient.getUrl(bucket, key)).thenReturn(new URL("http://testing"));
//
//        // when :
//        var uploadUrl = sut.readUploadUrl(filePath, fileName);
//
//        // then :
//        verify(s3AsyncClient, times(1)).getUrl(bucket, key);
//        then(uploadUrl).isEqualTo("http://testing");
//    }
}