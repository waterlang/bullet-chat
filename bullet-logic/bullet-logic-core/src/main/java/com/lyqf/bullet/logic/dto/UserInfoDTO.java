package com.lyqf.bullet.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenlang
 * @date 2022/5/12 4:07 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String userIdCardName;
    private String nickName;
    private String headUrl;
}
