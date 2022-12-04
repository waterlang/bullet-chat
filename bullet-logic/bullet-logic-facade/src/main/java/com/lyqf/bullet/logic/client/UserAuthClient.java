package com.lyqf.bullet.logic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyqf.bullet.common.model.ApiResponse;
import com.lyqf.bullet.logic.client.vo.AuthUserReq;
import com.lyqf.bullet.logic.client.vo.AuthUserResp;
import com.lyqf.bullet.logic.client.vo.ClearOnlineReq;

/**
 * @author chenlang
 * @date 2022/5/12 3:10 下午
 */

@FeignClient("bullet-logic")
public interface UserAuthClient {

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    ApiResponse<AuthUserResp> authUser(@RequestBody  AuthUserReq req);

    /**
     *
     * @return
     */
    @RequestMapping(value = "/clear-online", method = RequestMethod.POST)
    ApiResponse<Boolean> clearOnlineByUserId(@RequestBody ClearOnlineReq req);
}
