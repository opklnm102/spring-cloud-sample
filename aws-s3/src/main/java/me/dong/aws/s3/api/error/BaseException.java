package me.dong.aws.s3.api.error;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorType errorType;
    private final Object data;

    public BaseException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public BaseException(ErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }

    public BaseException(ErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
        this.data = null;
    }

    public BaseException(ErrorType errorType, Throwable cause, Object data) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
        this.data = data;
    }
}
