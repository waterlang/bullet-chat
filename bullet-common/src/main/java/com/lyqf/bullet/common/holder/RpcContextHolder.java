package com.lyqf.bullet.common.holder;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author chenlang
 * @date 2022/5/30 11:13 上午
 */
public class RpcContextHolder {

    private static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    /**
     *
     * @return
     */
    public static String getStringValue() {
        return context.get();
    }

    public static void setStringParameter(String data) {
        context.set(data);
    }

    /**
     * 
     */
    public static void remove() {
        context.remove();
    }
}
