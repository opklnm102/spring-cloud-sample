package me.dong.servicediscovery;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.Base64;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by huekim on 2019. 11. 10..
 */
@ExtendWith(MockitoExtension.class)
class AwsS3UploaderTest {

    @Mock
    private AmazonS3Client amazonS3Client;

    private AwsS3Uploader sut;

    private String bucket = "testing";

    @BeforeEach
    void setUp() {
        sut = new AwsS3Uploader(amazonS3Client, bucket);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void upload_FILE로_S3_업로드_성공() throws Exception {
        // given :
        var filePath = "test";
        var uploadFile = new File("test-upload-file");
        uploadFile.createNewFile();

        // when :
        sut.upload(filePath, uploadFile);

        // then :
        verify(amazonS3Client, times(1)).putObject(any());
    }

    @Test
    void upload_String으로_S3_업로드_성공() throws Exception {
        // given :
        var filePath = "test";
        var fileName = "test-upload-file";
        var content = "aaabbbccc";

        var key = String.format("%s/%s", filePath, fileName);
        var encodedContent = Base64.encodeAsString(content.getBytes(StandardCharsets.UTF_8));

        // when :
        sut.upload(filePath, fileName, content);

        // then :
        verify(amazonS3Client, times(1)).putObject(bucket, key, encodedContent);
    }

    @Test
    void readUploadUrl_S3에_업로드된_Object의_URL_조회() throws Exception {
        // given :
        var filePath = "test";
        var fileName = "test-upload-file";

        var key = String.format("%s/%s", filePath, fileName);
        when(amazonS3Client.getUrl(bucket, key)).thenReturn(new URL("http://testing"));

        // when :
        var uploadUrl = sut.readUploadUrl(filePath, fileName);

        // then :
        verify(amazonS3Client, times(1)).getUrl(bucket, key);
        then(uploadUrl).isEqualTo("http://testing");
    }
}
