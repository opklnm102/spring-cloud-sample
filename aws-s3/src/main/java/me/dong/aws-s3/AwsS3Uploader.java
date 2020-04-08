package me.dong.aws-s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Created by huekim on 2019. 11. 06..
 */
@Slf4j
public class AwsS3Uploader implements FileUploader {

    private final AmazonS3Client amazonS3Client;

    private final String bucket;

    public AwsS3Uploader(AmazonS3Client amazonS3Client, String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    /**
     * file을 S3 bucket에 저장
     *
     * @param filePath   file path in bucket
     * @param uploadFile file
     */
    @Override
    public void upload(String filePath, File uploadFile) {
        var key = generateKey(filePath, uploadFile.getName());
        var putObjectRequest = new PutObjectRequest(bucket, key, uploadFile)
                .withCannedAcl(CannedAccessControlList.Private);  // TODO: 2019.11.10 pulbic은?
        amazonS3Client.putObject(putObjectRequest);

        removeNewFile(uploadFile);
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
    public void upload(String filePath, String fileName, String content) {
        var encodedContent = Base64.encodeAsString(content.getBytes(StandardCharsets.UTF_8));
        var key = generateKey(filePath, fileName);

        amazonS3Client.putObject(bucket, key, encodedContent);
    }

    @Override
    public String readUploadUrl(String filePath, String fileName) {
        var key = generateKey(filePath, fileName);
        return amazonS3Client.getUrl(bucket, key).toString();
    }

    private String generateKey(String filePath, String fileName) {
        return String.format("%s/%s", filePath, fileName);
    }

    private void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            log.warn("failed remove file {}", targetFile.getAbsolutePath());
        }
    }
}
