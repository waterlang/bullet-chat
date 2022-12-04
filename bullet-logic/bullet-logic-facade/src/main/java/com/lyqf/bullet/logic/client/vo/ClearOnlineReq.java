package com.lyqf.bullet.logic.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/17  1:57 下午
 */

@Data
public class ClearOnlineReq implements Serializable {

    private static final long serialVersionUID = 1760383264305020478L;

    private Long userId;

    private Long roomId;

}
