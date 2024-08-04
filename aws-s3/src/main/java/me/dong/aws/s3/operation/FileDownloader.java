package me.dong.aws.s3.operation;

import java.io.File;

/**
 * Created by huekim on 2019. 11. 10..
 */
public interface FileDownloader {
    void download(String directoryName, File uploadFile);

    void download(String filePath, String fileName, String content);

    String readDownloadUrl(String filePath, String fileName);
}
