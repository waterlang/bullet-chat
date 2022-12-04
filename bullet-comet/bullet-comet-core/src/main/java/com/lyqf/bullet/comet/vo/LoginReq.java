package com.lyqf.bullet.comet.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenlang
 * @date 2022/5/11 4:19 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginReq {

    private Long roomId;

    private String token;

}
