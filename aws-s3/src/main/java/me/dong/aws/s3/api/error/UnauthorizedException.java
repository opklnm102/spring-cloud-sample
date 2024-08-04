package me.dong.aws.s3.api.error;

import lombok.Getter;

@Getter
public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
        super(ErrorType.NOT_FOUND);
    }

    public UnauthorizedException(Object data) {
        super(ErrorType.NOT_FOUND, data);
    }

    public UnauthorizedException(Throwable cause) {
        super(ErrorType.NOT_FOUND, cause);
    }

    public UnauthorizedException(Throwable cause, Object data) {
        super(ErrorType.NOT_FOUND, cause, data);
    }
}
