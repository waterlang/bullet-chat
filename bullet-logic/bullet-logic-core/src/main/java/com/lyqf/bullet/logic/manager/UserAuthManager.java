package com.lyqf.bullet.logic.manager;

import com.lyqf.bullet.comet.client.CloseUserConnectionClient;
import com.lyqf.bullet.common.exception.BizException;
import com.lyqf.bullet.common.holder.RpcContextHolder;
import com.lyqf.bullet.logic.acl.CloseUserConnectionFacadeAcl;
import com.lyqf.bullet.logic.client.vo.AuthUserReq;
import com.lyqf.bullet.logic.constant.RedisConstant;
import com.lyqf.bullet.logic.dto.RoomDTO;
import com.lyqf.bullet.logic.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author chenlang
 * @date 2022/5/23 11:37 上午
 */

@Slf4j
@SuppressWarnings("all")
@Component
public class UserAuthManager {

    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    private CloseUserConnectionClient closeUserConnectionFacade;

    @Resource
    private CloseUserConnectionFacadeAcl closeUserConnectionFacadeAcl;

    /**
     * 
     */
    public UserInfoDTO checkUserInfo(AuthUserReq req, HttpServletRequest request) {
        String tokenKey = String.format(RedisConstant.TOKEN_KEY, req.getToken());

        UserInfoDTO userInfoDTO = (UserInfoDTO)redisTemplate.opsForValue().get(tokenKey);
        if (userInfoDTO == null) {
            return genAnonymousAccount();
        }

        String roomKey = String.format(RedisConstant.ROOM_KEY, req.getRoomId());
        RoomDTO room = (RoomDTO)redisTemplate.opsForValue().get(roomKey);
        if (room == null) {
            throw new BizException(4002, "房间号信息不正确");
        }

        checkUserHasBanLog(userInfoDTO.getId());

        String userNodeKey =
            String.format(com.lyqf.bullet.common.constant.RedisConstant.USER_NODE, userInfoDTO.getId());
        String userNodeInfo = (String)redisTemplate.opsForValue().get(userNodeKey);
        if (StringUtils.isNotBlank(userNodeInfo)) {
            RpcContextHolder.setStringParameter(req.getNode());
            closeUserConnectionFacadeAcl.close(userInfoDTO.getId(), req.getRoomId());
            log.info("用户重复链接,剔除老连接,userId：{}", userInfoDTO.getId());
        }
        return userInfoDTO;
    }

    /**
     * 
     * @return
     */
    private UserInfoDTO genAnonymousAccount() {
        String startIdKey = String.format(RedisConstant.ANONYMOUS_USER_ID_START_KEY);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(redisTemplate.opsForValue().decrement(startIdKey));
        userInfoDTO.setNickName("匿名用户");

        String userInfoKey = String.format(RedisConstant.USER_INFO_KEY, userInfoDTO.getId());
        redisTemplate.opsForValue().set(userInfoKey, userInfoDTO, 3, TimeUnit.HOURS);
        log.info("create AnonymousAccount,userId:{}", userInfoDTO.getId());
        return userInfoDTO;
    }

    /**
     * 禁止登录
     */
    private void checkUserHasBanLog(Long userId) {
        log.info("该用户id没有被禁言");
    }

}
