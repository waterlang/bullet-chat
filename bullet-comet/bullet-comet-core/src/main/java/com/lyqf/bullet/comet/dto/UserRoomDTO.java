package com.lyqf.bullet.comet.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author chenlang
 * @date 2022/5/17 2:10 下午
 */
@Data
public class UserRoomDTO implements Serializable {

    private static final long serialVersionUID = 169849378576190669L;

    private Long userId;

    private Long roomId;

}
