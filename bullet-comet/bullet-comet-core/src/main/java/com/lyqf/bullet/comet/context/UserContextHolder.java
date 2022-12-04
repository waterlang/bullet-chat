package com.lyqf.bullet.comet.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ObjectUtils;

import com.lyqf.bullet.comet.dto.UserRoomDTO;

import io.netty.channel.Channel;

/**
 * @author chenlang
 * @date 2022/5/11 6:41 下午
 */

@SuppressWarnings("all")
public class UserContextHolder {

    /**
     * 用户 -> 连接 map
     */
    public static Map<Long, Channel> userWithChannelMap = new ConcurrentHashMap<>();

    /**
     * 连接 -> 用户 map
     */
    public static Map<Channel, UserRoomDTO> channelWithUserMap = new ConcurrentHashMap<>();

    /**
     * 一个房间记录了哪些用户链接
     */
    public static Map<Long, List<Channel>> roomWithChannelsMap = new ConcurrentHashMap<>();

    /**
     * 
     * @param channel
     * @param userRoomDTO
     */
    public static void add(Channel channel, UserRoomDTO userRoomDTO) {
        userWithChannelMap.put(userRoomDTO.getUserId(), channel);
        channelWithUserMap.put(channel, userRoomDTO);
        List<Channel> channels = roomWithChannelsMap.get(userRoomDTO.getRoomId());
        if (ObjectUtils.isEmpty(channels)) {
            channels = new ArrayList<Channel>();
        }
        channels.add(channel);

        roomWithChannelsMap.put(userRoomDTO.getRoomId(), channels);
    }

    /**
     * 
     * @param channel
     * @param userId
     */
    public static void clear(Channel channel, Long userId, Long roomId) {
        if (channel != null) {
            channelWithUserMap.remove(channel);
        }

        if (userId != null) {
            userWithChannelMap.remove(userId);
        }

        if (roomId == null) {
            return;
        }

        List<Channel> channels = roomWithChannelsMap.get(roomId);
        if (!ObjectUtils.isEmpty(channels) && channels.contains(channels)) {
            channels.remove(channel);
        }
    }

}
