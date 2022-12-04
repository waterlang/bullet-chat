package com.lyqf.bullet.common.exception;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/23 11:40 上午
 */

@Data
public class BizException extends RuntimeException {

    private Integer code;
    private String ms;


    /**
     * 
     * @param code
     * @param message
     */
    public BizException(Integer code, String message) {
        this.code = code;
        this.ms = message;
    }


}
