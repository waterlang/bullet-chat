package com.lyqf.bullet.logic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lyqf.bullet.logic.constant.RedisConstant;
import com.lyqf.bullet.logic.dto.RoomDTO;
import com.lyqf.bullet.logic.dto.UserInfoDTO;

/**
 * @author chenlang
 * @date 2022/5/19 1:21 下午
 */
@RestController
@RequestMapping
public class UserAndRoomSimpleManagerController {

    @Autowired
    private RedisTemplate redisTemplate;



    @PostMapping("/room/add")
    public String addRoom(@RequestBody RoomDTO roomDTO) {
        String roomKey = String.format(RedisConstant.ROOM_KEY, roomDTO.getId());
        redisTemplate.opsForValue().set(roomKey, roomDTO);
        return "success";
    }



    /**
     * 
     * @param userInfoDTO
     * @return
     */
    @PostMapping("/user/add")
    public String addUser(@RequestBody UserInfoDTO userInfoDTO) {
        String tokenKey = String.format(RedisConstant.TOKEN_KEY, userInfoDTO.getId());
        redisTemplate.opsForValue().set(tokenKey, userInfoDTO);
        return "success";
    }

}
