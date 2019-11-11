package me.dong.servicediscovery;

import java.io.File;

/**
 * Created by huekim on 2019. 11. 10..
 */
public interface FileUploader {
    void upload(String directoryName, File uploadFile);

    void upload(String filePath, String fileName, String content);

    String readUploadUrl(String filePath, String fileName);
}
