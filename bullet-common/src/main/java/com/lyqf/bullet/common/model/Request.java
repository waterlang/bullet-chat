package com.lyqf.bullet.common.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/11 7:00 下午
 */
@Data
public class Request implements Serializable {

    private static final long serialVersionUID = 4809210224829105997L;

    private Long userId;

    private Long roomId;

    private Integer operation;

    private String data;

}
