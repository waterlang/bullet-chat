package com.lyqf.bullet.comet.exception;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/11 4:26 下午
 */

@Data
public class ParamsException extends RuntimeException {

    private Integer code;
    private String msg;

    public ParamsException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
