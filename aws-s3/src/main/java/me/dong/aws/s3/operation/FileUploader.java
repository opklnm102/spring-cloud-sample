package me.dong.aws.s3.operation;

import java.io.File;

/**
 * Created by huekim on 2019. 11. 10..
 */
public interface FileUploader {
    void upload(String bucket, String filePath, File uploadFile);

    void upload(String bucket, String filePath, String fileName, String content);
}
