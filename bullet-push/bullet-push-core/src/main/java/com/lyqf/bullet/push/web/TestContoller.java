package com.lyqf.bullet.push.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lyqf.bullet.comet.client.PushClient;
import com.lyqf.bullet.comet.client.vo.PushReq;
import com.lyqf.bullet.common.model.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author chenlang
 * @date 2022/5/19 7:45 下午
 */

@Slf4j
@RestController
public class TestContoller {

    @Resource
    private PushClient pushClient;

    @GetMapping("/a")
    public ApiResponse<Boolean> pushCommonMsg(@RequestParam  String data) {
        // todo check biz logic and rate limit
        PushReq pushReq = JSON.parseObject(data, PushReq.class);
        // todo find roomid对应的的node id

        pushClient.push(pushReq);
        log.info("push success:{}", data);

        return ApiResponse.genSuccessData(true);
    }
}
