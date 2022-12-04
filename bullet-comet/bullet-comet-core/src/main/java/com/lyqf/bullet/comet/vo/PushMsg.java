package com.lyqf.bullet.comet.vo;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/17 5:17 下午
 */
@Data
public class PushMsg {

    /**
     * 操作类型 1 文本聊天 2 图片聊天 3 点赞 10以上为扩展功能 （抽奖，发红包，推产品，禁言，踢人等）
     */
    private Integer operateType;

    /**
     * 具体内容
     */
    private String content;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String userNickName;

}
