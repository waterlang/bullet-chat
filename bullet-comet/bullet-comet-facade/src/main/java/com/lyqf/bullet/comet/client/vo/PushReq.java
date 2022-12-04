package com.lyqf.bullet.comet.client.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenlang
 * @date 2022/5/17 4:31 下午
 */

@Data
public class PushReq implements Serializable {

    private static final long serialVersionUID = -1043305543639856086L;

    /**
     * 系统用户id
     */
    private Long sysUserId;

    private String sysUserName;

    private Long userId;

    private String userNickName;

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
