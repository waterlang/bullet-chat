package com.lyqf.bullet.logic.dto;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/16 3:02 下午
 */
@Data
public class MsgDTO {

    /**
     *
     */
    private Long sysUserId;

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

    private Long roomId;

    /**
     * 消息优先级： 1 系统级别（12 抽奖 ，13 发红包，14 推产品，15 禁言，16 踢人 ） 2.扩展自定义消息 (20 用户上线) 3.普通消息 （1 文本聊天 2 图片聊天 3 点赞） 4.心跳消息
     */
    private Integer level;

    /**
     * 单聊的userId,该值为null代表群聊
     */
    private Long toUserId;

    /**
     * 操作类型 1 文本聊天 2 图片聊天 3 点赞 20 以上为扩展功能 11 用户上线，12 抽奖 ，13 发红包，14 推产品，
     */
    private Integer operateType;

    /**
     * 具体内容
     */
    private String content;

    /**
     * 扩展字段
     */
    private String ext;
}
