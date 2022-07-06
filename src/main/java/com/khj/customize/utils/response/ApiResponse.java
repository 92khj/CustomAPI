package com.khj.customize.utils.response;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean status;
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse ok(T data,String msg){
        return ApiResponse.builder().status(true).code(ResponseCode.OK.getHttpStatus().value()).message(msg).data(data).build();
    }

    public static ApiResponse ok(List<Object> byTest) {
        return ApiResponse.builder().status(true).code(ResponseCode.OK.getHttpStatus().value()).build();
    }

    public static ApiResponse error(int code, String msg) {
        return ApiResponse.builder().status(false).code(code).message(msg).build();
    }

    public static ApiResponse error2(int code, String msg) {
        return ApiResponse.builder().status(false).code(code).message(msg).build();
    }

}
