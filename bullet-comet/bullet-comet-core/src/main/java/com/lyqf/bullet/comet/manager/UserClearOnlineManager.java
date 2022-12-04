package com.lyqf.bullet.comet.manager;

import com.lyqf.bullet.comet.context.UserContextHolder;
import com.lyqf.bullet.comet.dto.UserRoomDTO;
import com.lyqf.bullet.comet.util.SpringContextBeanUtil;
import com.lyqf.bullet.logic.client.UserAuthClient;
import com.lyqf.bullet.logic.client.vo.ClearOnlineReq;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/26 4:30 下午
 */

@Slf4j
public class UserClearOnlineManager {

    private static UserAuthClient userAuthClient = SpringContextBeanUtil.getBean(UserAuthClient.class);


    /**
     *
     * @param ctx
     * @param cause
     */
    public  static  void clearOnlineInfo(ChannelHandlerContext ctx, Throwable cause) {
        UserRoomDTO userRoomDTO = UserContextHolder.channelWithUserMap.get(ctx.channel());
        log.error(" authHandler error and close the channel,userId:{}",
            userRoomDTO == null ? -1 : userRoomDTO.getUserId(), cause);

        if (userRoomDTO == null) {
            ctx.channel().close();
            return;
        }

        ClearOnlineReq req = new ClearOnlineReq();
        req.setUserId(userRoomDTO.getUserId());
        req.setRoomId(userRoomDTO.getRoomId());
        userAuthClient.clearOnlineByUserId(req);

        UserContextHolder.clear(ctx.channel(), userRoomDTO.getUserId(), userRoomDTO.getRoomId());
        ctx.channel().close();

        log.info("clear userId online status finish,userId:{}", userRoomDTO.getUserId());
    }
}
