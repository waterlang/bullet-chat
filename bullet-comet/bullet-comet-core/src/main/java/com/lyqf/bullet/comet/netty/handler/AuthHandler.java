package com.lyqf.bullet.comet.netty.handler;

import com.lyqf.bullet.comet.context.UserContextHolder;
import com.lyqf.bullet.comet.dto.UserRoomDTO;
import com.lyqf.bullet.comet.manager.UserClearOnlineManager;
import com.lyqf.bullet.comet.util.IpUtil;
import com.lyqf.bullet.comet.util.SpringContextBeanUtil;
import com.lyqf.bullet.comet.util.UrlUtil;
import com.lyqf.bullet.comet.vo.LoginReq;
import com.lyqf.bullet.common.model.ApiResponse;
import com.lyqf.bullet.logic.client.UserAuthClient;
import com.lyqf.bullet.logic.client.vo.AuthUserResp;
import com.lyqf.bullet.logic.client.vo.ClearOnlineReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author chenlang
 * @date 2022/5/11 3:05 下午
 */
@Slf4j
@SuppressWarnings("all")
public class AuthHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker webSocketServerHandshaker;

    private UserAuthClient userAuthClient = SpringContextBeanUtil.getBean(UserAuthClient.class);

    private static LongAdder adder = new LongAdder();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // websocket第一步是通过http建立连接，第二步才是真正的websocket数据
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest)msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketInfo(ctx, (WebSocketFrame)msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent evnet = (IdleStateEvent)evt;
            log.info("收到连接信息长时间没有消息事件。。。");
            // 判断Channel是否读空闲, 读空闲时移除Channel
            if (evnet.state().equals(IdleState.READER_IDLE)) {
                UserRoomDTO userRoomDTO = UserContextHolder.channelWithUserMap.get(ctx.channel());
                if (userRoomDTO == null) {
                    return;
                }

                log.info("该连接信息长时间没有消息，userid:{} 开始断开连接。。", userRoomDTO.getUserId());
                ClearOnlineReq req = new ClearOnlineReq();
                req.setRoomId(userRoomDTO.getRoomId());
                req.setUserId(userRoomDTO.getUserId());
                userAuthClient.clearOnlineByUserId(req);

                UserContextHolder.clear(ctx.channel(), userRoomDTO.getUserId(), userRoomDTO.getRoomId());
                ctx.channel().close();
                log.info("该连接信息长时间没有消息，userid:{} 断开连接完成", userRoomDTO.getUserId());
            }
        }
        ctx.fireUserEventTriggered(evt);
    }

    /**
     * 建立连接时 依赖http，但是header会添加 Upgrade 作为判断
     * 
     * @param ctx
     * @param request
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))) {
            log.warn("protobuf don't support websocket");
            ctx.channel().close();
            return;
        }

        String realAddress = IpUtil.getIpInfo(ctx, request);
        log.info("auth realAddress:{}", realAddress);

        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory("", null, true);

        webSocketServerHandshaker = handshakerFactory.newHandshaker(request);

        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 动态加入websocket的编解码处理
            webSocketServerHandshaker.handshake(ctx.channel(), request);
            String uri = request.uri();
            LoginReq loginReq = UrlUtil.getLoginInfo(uri);

//            AuthUserReq req = new AuthUserReq();
//            req.setNode(IpUtil.getLocalIp());
//            req.setRoomId(loginReq.getRoomId());
//            req.setToken(loginReq.getToken());
//            ApiResponse<AuthUserResp> authUserResult = userAuthClient.authUser(req);
//            Long userId = handleAuthResult(authUserResult, ctx);
            // Long userId = 12L;
//            log.info("auth finish,userId:{}", userId);
            log.info("auth finish,token:{}", loginReq.getToken());
            adder.increment();
            log.info("当前连接数：{}",adder.longValue());
        }
    }

    /**
     * 
     * @param authUserResult
     */
    private Long handleAuthResult(ApiResponse<AuthUserResp> authUserResult, ChannelHandlerContext ctx) {
        if (!authUserResult.isSuccess()) {
            ctx.channel().close();
            return -1L;
        }

        AuthUserResp resp = authUserResult.getData();
        if (resp == null || !resp.getLoginResult()) {
            ctx.channel().close();
            return -1L;
        }

        UserRoomDTO userRoomDTO = new UserRoomDTO();
        userRoomDTO.setRoomId(resp.getRoomId());
        userRoomDTO.setUserId(resp.getUserId());
        UserContextHolder.add(ctx.channel(), userRoomDTO);

        return resp.getUserId() == null ? -1 : resp.getUserId();
    }

    /**
     * websocket消息体
     * 
     * @param ctx
     * @param frame
     */
    @SuppressWarnings("all")
    private void handleWebSocketInfo(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路命令
        if (frame instanceof CloseWebSocketFrame) {
            log.info("收到客服端主动断开链接请求");
            UserClearOnlineManager.clearOnlineInfo(ctx,null);
            return;
        }

        // 判断是否Ping消息
        if (frame instanceof PingWebSocketFrame) {
            log.info("ping message:{}", frame.content().retain());
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 判断是否Pong消息
        if (frame instanceof PongWebSocketFrame) {
            log.info("pong message:{}", frame.content().retain());
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(frame.getClass().getName() + " frame type not supported");
        }

        // 后续消息交给MessageHandler处理
        ctx.fireChannelRead(frame.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        UserClearOnlineManager.clearOnlineInfo(ctx,cause);
    }

}
