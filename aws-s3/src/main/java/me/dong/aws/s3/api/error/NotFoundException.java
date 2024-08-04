package me.dong.aws.s3.api.error;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {

    public NotFoundException() {
        super(ErrorType.NOT_FOUND);
    }

    public NotFoundException(Object data) {
        super(ErrorType.NOT_FOUND, data);
    }

    public NotFoundException(Throwable cause) {
        super(ErrorType.NOT_FOUND, cause);
    }

    public NotFoundException(Throwable cause, Object data) {
        super(ErrorType.NOT_FOUND, cause, data);
    }
}
