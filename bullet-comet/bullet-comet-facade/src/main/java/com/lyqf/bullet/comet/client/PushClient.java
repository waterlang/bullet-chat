package com.lyqf.bullet.comet.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyqf.bullet.comet.client.vo.PushReq;
import com.lyqf.bullet.common.model.ApiResponse;

/**
 * @author chenlang
 * @date 2022/5/17 4:31 下午
 */

@FeignClient("bullet-comet")
public interface PushClient {

    /**
     * 
     * @param pushReq
     * @return
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    ApiResponse<Boolean> push(@RequestBody  PushReq pushReq);

}
