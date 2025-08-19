package com.example.indexquiz.common.exception.handler;

import com.example.indexquiz.common.exception.ErrorResponse;
import com.example.indexquiz.common.exception.custom.IndexQuizException;
import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(BindException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.FIELD_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.URL_PARAMETER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH);
    }

    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity<ErrorResponse> handleClientAbortException(ClientAbortException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.ALREADY_DISCONNECTED);
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public ResponseEntity<ErrorResponse> handleAsyncError(AsyncRequestNotUsableException exception) {
        loggingClientError(exception);
        if (isClientDisconnect(exception.getCause())) {
            return toResponse(ErrorCode.ALREADY_DISCONNECTED);
        }
        return toResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private boolean isClientDisconnect(Throwable cause) {
        return cause instanceof IOException
                && cause.getMessage() != null
                && cause.getMessage().contains("Broken pipe");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception
    ) {
        loggingClientError(exception);
        return toResponse(ErrorCode.METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException exception
    ) {
        loggingClientError(exception);
        return toResponse(ErrorCode.MEDIA_TYPE_NOT_SUPPORTED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.NO_RESOURCE_FOUND);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestCookieException(MissingRequestCookieException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.NO_COOKIE_FOUND);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException exception) {
        loggingClientError(exception);
        return toResponse(ErrorCode.FILE_UPLOAD_ERROR);
    }

    @ExceptionHandler(IndexQuizException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(IndexQuizException exception) {
        if (exception.getErrorCode().getStatus().is4xxClientError()) {
            loggingClientError(exception);
        }
        loggingServerError(exception);
        return toResponse(exception.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        loggingServerError(exception);
        return toResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void loggingClientError(Exception exception) {
        log.warn("exception message: {}", exception.getMessage());
    }

    private void loggingServerError(Exception exception) {
        log.error("exception message: {}", exception.getMessage());
    }


    private ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode) {
        HttpStatus status = errorCode.getStatus();
        String message = errorCode.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.status(status)
                .body(errorResponse);
    }
}
