package com.example.indexquiz.common.exception.custom;

import com.example.indexquiz.common.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class IndexQuizException extends RuntimeException {

    private final ErrorCode errorCode;

    public IndexQuizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
