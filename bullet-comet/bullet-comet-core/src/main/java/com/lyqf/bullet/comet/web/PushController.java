package com.lyqf.bullet.comet.web;

import com.alibaba.fastjson.JSONObject;
import com.lyqf.bullet.comet.context.UserContextHolder;
import com.lyqf.bullet.comet.dto.UserRoomDTO;
import com.lyqf.bullet.comet.client.vo.PushReq;
import com.lyqf.bullet.comet.vo.PushMsg;
import com.lyqf.bullet.common.model.ApiResponse;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenlang
 * @date 2022/5/17 4:27 下午
 */
@Slf4j
@RestController
public class PushController {

    @PostMapping("/push")
    public ApiResponse<Boolean> push(@RequestBody PushReq pushReq) {
        log.info("接到收消息：{}", pushReq);
        
        PushMsg msg = new PushMsg();
        msg.setContent(pushReq.getContent());
        msg.setOperateType(pushReq.getOperateType());
        msg.setUserNickName(pushReq.getUserNickName());
        msg.setUserId(pushReq.getUserId());
        String outMsg = JSONObject.toJSONString(msg);

        // s to s
        if (pushReq.getToUserId() != null) {
            log.info("to user msg,to userId:{}", pushReq.getToUserId());
            Channel channel = UserContextHolder.userWithChannelMap.get(pushReq.getToUserId());
            if (channel != null) {
                channel.writeAndFlush(new TextWebSocketFrame(outMsg));
            }
        } else { // broadcast
            List<Channel> channelList = UserContextHolder.roomWithChannelsMap.get(pushReq.getRoomId());
            if (ObjectUtils.isNotEmpty(channelList)) {
                channelList.forEach(k -> {
                    UserRoomDTO userRoomDTO = UserContextHolder.channelWithUserMap.get(k);
                    if (userRoomDTO != null) {
                        Long channelUserId = userRoomDTO.getUserId();
                        // 不能自己发消息给自己
                        if (!pushReq.getUserId().equals(channelUserId)) {
                            k.writeAndFlush(new TextWebSocketFrame(outMsg));
                            log.info("向userid 为：{} 推送消息成功.", userRoomDTO.getUserId());
                        }
                    }
                });
            }
        }

        return ApiResponse.genSuccessData(true);
    }

}
