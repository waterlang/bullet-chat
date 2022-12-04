package com.lyqf.bullet.logic.client.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenlang
 * @date 2022/5/16 2:49 下午
 */

@Data
public class UserMsgReq implements Serializable {

    private static final long serialVersionUID = 2869741037236201052L;

    /**
     * 唯一id
     */
    private String reqId;

    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 用户身证证姓名
     */
    private String userIdCardName;

    /**
     * 头像
     */
    private String headUrl;

    /**
     * 
     */
    private Long roomId;

    /**
     * 单聊的userId,该值为null代表群聊
     */
    private Long toUserId;

    /**
     *
     */
    private Integer operateType;

    /**
     * 具体内容
     */
    private String content;
}
