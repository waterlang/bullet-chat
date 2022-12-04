package com.lyqf.bullet.common.exception;

/**
 * @author chenlang
 * @date 2022/5/30 2:36 下午
 */
public class ProviderNotFoundException extends RuntimeException {
    private Integer code;
    private String ms;

    /**
     *
     * @param code
     * @param message
     */
    public ProviderNotFoundException(Integer code, String message) {
        this.code = code;
        this.ms = message;
    }
}
