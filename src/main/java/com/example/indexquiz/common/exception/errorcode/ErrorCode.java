package com.example.indexquiz.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400
    FIELD_ERROR(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    URL_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 값의 타입이 잘못되었습니다."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "허용되지 않은 미디어 타입입니다."),
    ALREADY_DISCONNECTED(HttpStatus.BAD_REQUEST, "이미 클라이언트에서 요청이 종료되었습니다."),
    NO_COOKIE_FOUND(HttpStatus.BAD_REQUEST, "필수 쿠키 값이 존재하지 않습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),

    QUESTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 질문입니다."),
    QUESTION_OPTION_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 선택지입니다."),

    SOLUTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 해설입니다"),

    // 500
    INVALID_USER_ANSWERS(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 유저 답변 목록입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다. 관리자에게 문의하세요."),
    INITIALIZE_JDA_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Discord JDA 초기화에 실패하였습니다"),
    DISCORD_PROPERTIES_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "Discord 설정 정보 초기화에 실패하였습니다"),
    ;

    private final HttpStatus status;
    private final String message;
}
