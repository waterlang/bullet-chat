package com.lyqf.bullet.logic.web;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lyqf.bullet.comet.client.PushClient;
import com.lyqf.bullet.common.enums.LevelEnum;
import com.lyqf.bullet.logic.client.vo.UserMsgReq;
import com.lyqf.bullet.logic.client.vo.UserMsgResp;
import com.lyqf.bullet.logic.constant.RedisConstant;
import com.lyqf.bullet.logic.dto.MsgDTO;
import com.lyqf.bullet.logic.dto.UserInfoDTO;
import com.lyqf.bullet.logic.manager.KafkaManger;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/12 4:02 下午
 */

@Slf4j
@SuppressWarnings("all")
@RestController
@RequestMapping
public class UserMsgController {

    @Autowired
    private KafkaManger kafkaManger;

    @Autowired
    private PushClient pushClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @param userMsgReq
     * @return
     */
    @RequestMapping(value = "/send-msg", method = RequestMethod.POST)
    public UserMsgResp sendMsg(@RequestBody UserMsgReq userMsgReq) {
        log.info("receive sendMsg:{}", userMsgReq);

        // todo check rate limit
        UserMsgResp resp = new UserMsgResp();
        resp.setReqId(userMsgReq.getReqId());

        boolean result = check(userMsgReq);
        if (!result) {
            resp.setResult(false);
            return resp;
        }

        MsgDTO msgDTO = new MsgDTO();
        BeanUtils.copyProperties(userMsgReq, msgDTO);
        UserInfoDTO userInfoDTO = getUserInfoById(userMsgReq.getUserId());
        // todo 走heart
        msgDTO.setLevel(LevelEnum.COMMON.getCode());
        msgDTO.setUserNickName(userInfoDTO.getNickName());
        kafkaManger.sendMsg(msgDTO);
        resp.setResult(true);
        return resp;
    }

    /**
     *
     * @return
     */
    private UserInfoDTO getUserInfoById(Long userId) {
        String tokenKey = String.format(RedisConstant.USER_INFO_KEY, userId);
        UserInfoDTO userInfoDTO = (UserInfoDTO)redisTemplate.opsForValue().get(tokenKey);
        if (userInfoDTO == null) {
            userInfoDTO = new UserInfoDTO();
            userInfoDTO.setNickName("zhangsan");
            userInfoDTO.setUserIdCardName("张三");
            userInfoDTO.setHeadUrl("url");
        }
        return userInfoDTO;
    }

    /**
     *
     * @param userMsgReq
     * @return
     */
    private boolean check(UserMsgReq userMsgReq) {
        return checkRateLimit(userMsgReq);
    }

    /**
     * todo 放在consumer端 检测文本是否有违禁词
     * 
     * @return
     */
    private boolean checkTxt(UserMsgReq userMsgReq) {
        return true;
    }

    /**
     * todo 放在consumer端
     * 
     * @return
     */
    private boolean checkImag(UserMsgReq userMsgReq) {
        return true;
    }

    /**
     * 用户限流及全局限流
     * 
     * @return
     */
    private boolean checkRateLimit(UserMsgReq userMsgReq) {
        return true;
    }

}
