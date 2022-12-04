package com.lyqf.bullet.comet.web;

import com.lyqf.bullet.comet.context.UserContextHolder;
import com.lyqf.bullet.common.model.ApiResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlang
 * @date 2022/5/24 1:39 下午
 */

@Slf4j
@RestController
public class CloseUserConnectionController {

    @GetMapping("/close")
    public ApiResponse<Boolean> close(@RequestParam Long userId, @RequestParam Long roomId) {
        Channel channel = UserContextHolder.userWithChannelMap.get(userId);
        UserContextHolder.clear(channel, userId, roomId);

        if (channel != null) {
            channel.close();
        }
        log.info("重复登录，clear上一条链接信息，userId:{},roomId:{}", userId, roomId);
        return ApiResponse.genSuccessData(true);
    }

}
