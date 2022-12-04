package com.lyqf.bullet.logic.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/12 4:39 下午
 */
@Data
public class AuthUserResp implements Serializable {
    private static final long serialVersionUID = -1185223096517555110L;

    /**
     * 是否是重复登录 true是
     */
    private Boolean repeatLogin = false;

    /**
     * 登录结果 true 为成功
     */
    private Boolean loginResult = true;

    /**
     * 
     */
    private String msg;

    private Long userId;

    private Long roomId;

}
