package com.lyqf.bullet.logic.client.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/12  3:48 下午
 */

@Data
public class AuthUserReq  implements Serializable {

    private static final long serialVersionUID = 7090117243937573161L;

    private String token;

    private String userName;

    private Long roomId;

    private String node;



}
