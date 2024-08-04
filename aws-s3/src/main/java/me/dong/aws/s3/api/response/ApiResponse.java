package me.dong.aws.s3.api.response;

import me.dong.aws.s3.api.error.ErrorMessage;
import me.dong.aws.s3.api.error.ErrorType;

public record ApiResponse<T>(ResultType result,
                             T data,
                             ErrorMessage error) {

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(ErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error));
    }

    public static ApiResponse<?> error(ErrorType error, Object data) {
        return new ApiResponse<>(ResultType.ERROR, data, new ErrorMessage(error, data));
    }

    public record FieldError(String field,
                             String value,
                             String reason) {
    }
}
