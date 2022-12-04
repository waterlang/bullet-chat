package com.lyqf.bullet.push.manager;

import com.lyqf.bullet.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author chenlang
 * @date 2022/5/24 5:28 下午
 */

@Component
public class LiveNodeManager {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * todo 后面改走rpc调用logic服务获取节点信息（redis集群做业务分离）
     * 
     * @param roomId
     * @return
     */
    public Set<String> getNodeIdsByRoomId(Long roomId) {
        String roomNodesKey = String.format(RedisConstant.ROOM_NODE, roomId);
        return redisTemplate.opsForSet().members(roomNodesKey);
    }

    /**
     * todo 后面改走rpc调用logic服务获取节点信息（redis集群做业务分离）
     *
     * @param userId
     * @return
     */
    public String getNodeIdsByUserId(Long userId) {
        String roomNodesKey = String.format(RedisConstant.USER_NODE, userId);
        return (String) redisTemplate.opsForValue().get(roomNodesKey);
    }
}
