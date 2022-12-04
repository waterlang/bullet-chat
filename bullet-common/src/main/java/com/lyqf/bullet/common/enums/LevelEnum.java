package com.lyqf.bullet.common.enums;

/**
 * @author chenlang
 * @date 2022/5/16 4:14 下午
 */
public enum LevelEnum {

    SYSTEM(1), EXT(2), COMMON(3), HEART(4);

    int code;

    LevelEnum(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
