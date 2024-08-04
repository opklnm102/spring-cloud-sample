package me.dong.aws.s3.api.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred", LogLevel.ERROR),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, ErrorCode.E400, "Invalid input value", LogLevel.INFO),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorCode.E401, "Unauthorized", LogLevel.INFO),
    NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E404, "Not found", LogLevel.WARN),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.E405, "Invalid method", LogLevel.INFO),
    CONFLICT(HttpStatus.CONFLICT, ErrorCode.E409, "Conflict", LogLevel.WARN),
    ALREADY_EXIST(HttpStatus.CONFLICT, ErrorCode.E409, "Already exist", LogLevel.WARN),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "failed upload file", LogLevel.ERROR),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "failed download file.", LogLevel.ERROR);

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;
}
