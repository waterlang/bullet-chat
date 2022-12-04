package com.lyqf.bullet.comet.exception;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/11 4:26 下午
 */

@Data
public class ServiceBizException extends RuntimeException {

    private Integer code;
    private String msg;

    public ServiceBizException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
