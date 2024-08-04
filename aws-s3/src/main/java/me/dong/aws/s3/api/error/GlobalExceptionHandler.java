package me.dong.aws.s3.api.error;

import lombok.extern.slf4j.Slf4j;
import me.dong.aws.s3.api.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ApiResponse<?>> handle(BaseException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("BaseException : {}", e.getMessage(), e);
            case WARN -> log.warn("BaseException : {}", e.getMessage(), e);
            case INFO -> log.info("BaseException : {}", e.getMessage(), e);
            case OFF -> {
            }
            default -> log.debug("BaseException : {}", e.getMessage(), e);
        }

        return ResponseEntity.status(e.getErrorType().getStatus())
                             .body(ApiResponse.error(e.getErrorType(), e.getData()));
    }

    @ExceptionHandler(SilentBaseException.class)
    protected ResponseEntity<ApiResponse<?>> handle(SilentBaseException e) {
        return ResponseEntity.status(e.getErrorType().getStatus())
                             .body(ApiResponse.error(e.getErrorType(), e.getData()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<?>> handle(HttpRequestMethodNotSupportedException e) {
        var errorType = ErrorType.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(errorType.getStatus())
                             .body(ApiResponse.error(errorType));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingRequestHeaderException.class,
            UnsupportedMediaTypeException.class, UnsupportedOperationException.class,
            IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ApiResponse<?>> handle(Exception e) {
        var errorType = ErrorType.INVALID_INPUT_VALUE;
        return ResponseEntity.status(errorType.getStatus())
                             .body(ApiResponse.error(errorType));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    protected ResponseEntity<ApiResponse<?>> handle(BindException e) {
        return bindError(e.getBindingResult());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("handle internal exception", e);
        var errorType = ErrorType.DEFAULT_ERROR;
        return ResponseEntity.status(errorType.getStatus())
                             .body(ApiResponse.error(errorType));
    }

    private ResponseEntity<ApiResponse<?>> bindError(BindingResult bindingResult) {
        var fieldErrors = bindingResult.getFieldErrors()
                                       .stream()
                                       .map(fieldError -> new ApiResponse.FieldError(fieldError.getField(),
                                               String.valueOf(fieldError.getRejectedValue()),
                                               fieldError.getDefaultMessage()))
                                       .toList();

        var errorType = ErrorType.INVALID_INPUT_VALUE;
        return ResponseEntity.status(errorType.getStatus())
                             .body(ApiResponse.error(errorType, fieldErrors));

    }
}
