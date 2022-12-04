package com.lyqf.bullet.logic.util;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author chenlang
 * @date 2022/5/17 2:03 下午
 */
public class BulletDateUtil {

    /**
     *  获取当前时间的下一天这个时间
     * @return
     */
    public static Date genNextDateFromCurrentDate() {
        return DateUtils.addDays(new Date(), 1);
    }

}
