package me.dong.aws.s3.operation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.error.BaseException;
import me.dong.aws.s3.api.error.ErrorType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@UtilityClass
public class FileUtils {

    public static void delete(File file) {
        Objects.requireNonNull(file, "file");
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            log.warn("failed remove file {}", file.getAbsolutePath());
            throw new BaseException(ErrorType.DEFAULT_ERROR, e);
        }
    }

    public static void delete(Path path) {
        Objects.requireNonNull(path, "path");
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.warn("failed remove file {}", path.toAbsolutePath());
            throw new BaseException(ErrorType.DEFAULT_ERROR, e);
        }
    }

    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }
}
