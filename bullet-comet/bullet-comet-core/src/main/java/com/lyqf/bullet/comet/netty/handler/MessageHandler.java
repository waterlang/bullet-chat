package com.lyqf.bullet.comet.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.lyqf.bullet.comet.context.UserContextHolder;
import com.lyqf.bullet.comet.dto.UserRoomDTO;
import com.lyqf.bullet.comet.manager.UserClearOnlineManager;
import com.lyqf.bullet.comet.util.SpringContextBeanUtil;
import com.lyqf.bullet.common.enums.OperateTypeEnum;
import com.lyqf.bullet.common.exception.BizException;
import com.lyqf.bullet.logic.client.UserMsgClient;
import com.lyqf.bullet.logic.client.vo.UserMsgReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenlang
 * @date 2022/5/11 3:48 下午
 */

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final String PING = "PING@!2#4%6&8";

    private static final String PONG = "PONG@!2#4%6&8";

    private UserMsgClient userMsgClient = SpringContextBeanUtil.getBean(UserMsgClient.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        UserRoomDTO userRoomDTO = UserContextHolder.channelWithUserMap.get(ctx.channel());
        if (userRoomDTO == null) {
            log.error("没找到用户数据。。。。");
            throw new BizException(4000, "用户数据没找到");
        }

        String msgData = msg.text();
        log.info("receive userId msg ,userId:{},msg:{}", userRoomDTO.getUserId(), msgData);
        if (StringUtils.isBlank(msgData)) {
            return;
        }

        UserMsgReq userMsgReq;
        //heart msg
        if (msgData.toUpperCase().contains(PING)) {
            userMsgReq = new UserMsgReq();
            userMsgReq.setUserId(userRoomDTO.getUserId());
            userMsgReq.setToUserId(userRoomDTO.getUserId());
            userMsgReq.setOperateType(OperateTypeEnum.HERART_PING.getCode());
            userMsgReq.setContent(PONG);
        } else {
            userMsgReq = JSONObject.parseObject(msgData, UserMsgReq.class);
            userMsgReq.setUserId(userRoomDTO.getUserId());
        }

        userMsgClient.sendMsg(userMsgReq);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        UserClearOnlineManager.clearOnlineInfo(ctx, cause);
    }

}
