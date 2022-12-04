package com.lyqf.bullet.comet.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.lyqf.bullet.comet.constant.ExceptionCodeConstant;
import com.lyqf.bullet.comet.exception.ParamsException;
import com.lyqf.bullet.comet.vo.LoginReq;

import io.github.ljwlgl.util.UrlParamsUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/11 4:15 下午
 */
@Slf4j
public class UrlUtil {

    /**
     * 
     * @param url
     * @return
     */
    public static LoginReq getLoginInfo(String url) {
        log.info("getUrlParams url:{}", url);
        Map<String, String> data = UrlParamsUtil.split(url);

        LoginReq loginReq = new LoginReq();
        String roomId = data.get("roomId");
        if (StringUtils.isBlank(roomId)) {
            throw new ParamsException(ExceptionCodeConstant.PARAMS_ERROR_CODE, "房间号不能为空");
        }

        String token = data.get("token");
        if (StringUtils.isBlank(token)) {
            throw new ParamsException(ExceptionCodeConstant.PARAMS_ERROR_CODE, "没有认证信息!");
        }

        try {
            loginReq.setRoomId(Long.parseLong(roomId));
            loginReq.setToken(token);
        } catch (Exception e) {
            log.warn("e", e);
            throw new ParamsException(ExceptionCodeConstant.PARAMS_ERROR_CODE, "请求参数不正确");
        }

        return loginReq;
    }

}
