package com.lyqf.bullet.push.biz;

import com.alibaba.fastjson.JSON;
import com.lyqf.bullet.comet.client.PushClient;
import com.lyqf.bullet.comet.client.vo.PushReq;
import com.lyqf.bullet.common.holder.RpcContextHolder;
import com.lyqf.bullet.push.manager.LiveNodeManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author chenlang
 * @date 2022/5/18 4:25 下午
 */
@Slf4j
@Component
public class CommonMsgPushBiz {

    @Resource
    private PushClient pushClient;

    @Autowired
    private LiveNodeManager liveNodeManager;

    /**
     * 
     * @param data
     */
    public void pushCommonMsg(String data) {
        // todo check biz logic and rate limit
        PushReq pushReq = JSON.parseObject(data, PushReq.class);
        // todo find roomid对应的的node id

        if (pushReq == null) {
            return;
        }

        if (pushReq.getToUserId() != null) {
            String nodeId = liveNodeManager.getNodeIdsByUserId(pushReq.getToUserId());
            if (StringUtils.isNotBlank(nodeId)) {
                RpcContextHolder.setStringParameter(nodeId);
                pushClient.push(pushReq);
            } else {
                log.info("单聊消息，没有找到用户：{}的连接信息", pushReq.getToUserId());
            }

        } else { // broadcast
            Set<String> nodeIds = liveNodeManager.getNodeIdsByRoomId(pushReq.getRoomId());
            if (!ObjectUtils.isEmpty(nodeIds)) {
                nodeIds.forEach(k -> {
                    RpcContextHolder.setStringParameter(k);
                    pushClient.push(pushReq);
                });
            } else {
                log.info("群聊消息，没有找到room：{}的连接信息", pushReq.getRoomId());
            }
        }

        log.info("push success:{}", data);

    }

}
