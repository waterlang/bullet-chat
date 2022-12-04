package com.lyqf.bullet.logic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyqf.bullet.logic.client.vo.UserMsgReq;
import com.lyqf.bullet.logic.client.vo.UserMsgResp;

/**
 * @author chenlang
 * @date 2022/5/16 Re6 下午
 */
@FeignClient("bullet-logic")
public interface UserMsgClient {

    /**
     * 用户发送消息
     * 
     * @param userMsgReq
     * @return
     */
    @RequestMapping(value = "/send-msg", method = RequestMethod.POST)
    UserMsgResp sendMsg(@RequestBody  UserMsgReq userMsgReq);
}
