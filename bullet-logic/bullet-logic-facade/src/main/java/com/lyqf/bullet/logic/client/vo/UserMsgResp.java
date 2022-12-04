package com.lyqf.bullet.logic.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/16 2:49 下午
 */

@Data
public class UserMsgResp implements Serializable {

    private static final long serialVersionUID = 2869741037236201052L;

    private String reqId;

    /**
     * ture 成功
     */
    private boolean result;

    /**
     * 返回其他内容信息
     */
    private String data;

}
