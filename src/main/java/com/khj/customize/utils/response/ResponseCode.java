package com.khj.customize.utils.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "성공"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    FAIL_TO_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패"),
    RESOURCE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "데이터가 없습니다"),
    ;

    private HttpStatus httpStatus;
    private String message;

}
