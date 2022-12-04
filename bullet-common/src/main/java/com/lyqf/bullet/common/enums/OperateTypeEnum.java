package com.lyqf.bullet.common.enums;

/**
 * @author chenlang
 * @date 2022/5/23 11:22 上午
 */
public enum OperateTypeEnum {

    /**
     *
     */
    TXT_MSG(1), IMAG_MSG(2), LIKE_MSG(3), ONLINE_EXT_MSG(11), LOTTERY__SYS_MSG(12), RED_ENVELOPE_SYS_MSG(13),
    PRODUCT_SYS_MSG(14), HERART_PING(100);

    int code;

    OperateTypeEnum(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
