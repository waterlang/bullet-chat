package com.lyqf.bullet.common.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenlang
 * @date 2022/5/12 3:13 下午
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("all")
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 7090117243937573164L;


    public static final String ERROR_MES = "error";
    public static final String SUCCESS_MES = "success";

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer ERROR_CODE = 400;

    private Integer code;
    private String msg;
    private T data;

    /**
     * 
     * @return
     */
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    /**
     * 
     */
    public static ApiResponse genSuccessData(Object data) {
        return ApiResponse.builder().code(SUCCESS_CODE).msg(SUCCESS_MES).data(data).build();
    }

    public static ApiResponse genDefaultErrorData() {
        return ApiResponse.builder().code(ERROR_CODE).msg(ERROR_MES).build();
    }

    public static ApiResponse genErrorData(Integer code, String msg) {
        return ApiResponse.builder().code(code).msg(msg).build();
    }

    public static ApiResponse genErrorData(Integer code, String msg, Object data) {
        return ApiResponse.builder().code(code).msg(msg).data(data).build();
    }
}
