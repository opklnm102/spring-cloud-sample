package me.dong.aws.s3.repository;

import me.dong.aws.s3.domain.FileDownload;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileDownloadRepository {
    private final Map<String, FileDownload> files;

    public FileDownloadRepository() {
        files = new ConcurrentHashMap<>();
        add("1234", new FileDownload("yanolja-dev-pf-bucket", "search-dump-qa/dump/roomtypelist/2022/09/13/14/2022091314-log_roomtypelist.json"));
        add("12", new FileDownload("yanolja-dev-pf-bucket", "partner-payment/test/content-test2"));
    }

    public void add(String key, FileDownload value) {
        files.put(key, value);
    }

    public Optional<FileDownload> get(String key) {
        return Optional.ofNullable(files.get(key));
    }
}
