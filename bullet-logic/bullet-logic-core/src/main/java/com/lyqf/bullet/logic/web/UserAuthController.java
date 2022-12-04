package com.lyqf.bullet.logic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lyqf.bullet.common.constant.RedisConstant;
import com.lyqf.bullet.common.enums.LevelEnum;
import com.lyqf.bullet.common.enums.OperateTypeEnum;
import com.lyqf.bullet.common.model.ApiResponse;
import com.lyqf.bullet.logic.client.vo.AuthUserReq;
import com.lyqf.bullet.logic.client.vo.AuthUserResp;
import com.lyqf.bullet.logic.client.vo.ClearOnlineReq;
import com.lyqf.bullet.logic.dto.MsgDTO;
import com.lyqf.bullet.logic.dto.UserInfoDTO;
import com.lyqf.bullet.logic.manager.KafkaManger;
import com.lyqf.bullet.logic.manager.UserAuthManager;
import com.lyqf.bullet.logic.util.BulletDateUtil;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenlang
 * @date 2022/5/12 4:02 下午
 */

@Slf4j
@SuppressWarnings("all")
@RestController
@RequestMapping
public class UserAuthController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private KafkaManger kafkaManger;

    @Autowired
    private UserAuthManager userAuthManager;


    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ApiResponse<AuthUserResp> authUser(@RequestBody  AuthUserReq req) {
        AuthUserResp resp = new AuthUserResp();
        UserInfoDTO userInfoDTO = userAuthManager.checkUserInfo(req,httpServletRequest);
        resp.setUserId(userInfoDTO.getId());
        resp.setRoomId(req.getRoomId());
        saveUserWithNode(req, userInfoDTO.getId());
        sendMsgToMq(req, userInfoDTO);
        return ApiResponse.genSuccessData(resp);
    }

    private void sendMsgToMq(AuthUserReq authUserReq, UserInfoDTO userInfoDTO) {
        MsgDTO msgDTO = new MsgDTO();
        msgDTO.setLevel(LevelEnum.EXT.getCode());
        msgDTO.setRoomId(authUserReq.getRoomId());
        msgDTO.setUserId(userInfoDTO.getId());
        msgDTO.setOperateType(OperateTypeEnum.ONLINE_EXT_MSG.getCode());
        msgDTO.setUserNickName(userInfoDTO.getNickName());
        msgDTO.setHeadUrl(userInfoDTO.getHeadUrl());
        kafkaManger.sendMsg(msgDTO);
    }

    private void saveUserWithNode(AuthUserReq req, Long userId) {
        String roomNodesKey = String.format(RedisConstant.ROOM_NODE, req.getRoomId());
        String userNodeKey = String.format(RedisConstant.USER_NODE, userId);

        // 用户在哪个节点上
        redisTemplate.opsForValue().set(userNodeKey, req.getNode());
        log.info("saveUserWithNode userId:{} 分配在node :{}", userId, req.getNode());

        // room分布在哪些节点上
        redisTemplate.opsForSet().add(roomNodesKey, req.getNode());
        log.info("saveUserWithNode room:{} 分配在nodes :{}", req.getRoomId(), req.getNode());

        redisTemplate.expireAt(roomNodesKey, BulletDateUtil.genNextDateFromCurrentDate());
        redisTemplate.expireAt(userNodeKey, BulletDateUtil.genNextDateFromCurrentDate());
    }


    /**
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/clear-online", method = RequestMethod.POST)
    public ApiResponse<Boolean> clearOnlineByUserId(@RequestBody  ClearOnlineReq req) {
        String userNodeKey = String.format(RedisConstant.USER_NODE, req.getUserId());
        redisTemplate.delete(userNodeKey);
        log.info("clear online status ,userId:{}", req.getUserId());
        return ApiResponse.genSuccessData(true);
    }
}
