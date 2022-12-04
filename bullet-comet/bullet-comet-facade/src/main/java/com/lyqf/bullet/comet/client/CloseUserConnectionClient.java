package com.lyqf.bullet.comet.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyqf.bullet.common.model.ApiResponse;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenlang
 * @date 2022/5/23 11:33 上午
 */
@FeignClient("bullet-comet")
public interface CloseUserConnectionClient {

    /**
     * 断开用户老连接
     * 
     * @param userId
     * @param roomId
     * @return
     */
    @RequestMapping(value = "/close", method = RequestMethod.GET)
    ApiResponse<Boolean> close(@RequestParam Long userId, @RequestParam Long roomId);

}
