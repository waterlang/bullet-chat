package com.lyqf.bullet.comet.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.Enumeration;

/**
 * @author chenlang
 * @date 2022/5/11 3:55 下午
 */

@Slf4j
public class IpUtil {

    /**
     * 
     * @param ctx
     * @param request
     * @return
     */
    public static String getIpInfo(ChannelHandlerContext ctx, FullHttpRequest request) {
        HttpHeaders headers = request.headers();
        // todo x-forwarded-for 可能有多个，第一个是最原始的
        String ip = headers.get("x-forwarded-for");
        if (StringUtils.isNotBlank(ip)) {
            return ip;
        }

        return getIpFromChannel(ctx.channel());
    }

    /**
     *
     * @param channel
     * @return
     */
    private static String getIpFromChannel(Channel channel) {
        if (null == channel) {
            return "";
        }
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }
            return addr;
        }

        return "";
    }

    /**
     * 获取本机ip
     * 
     * @return
     */
    public static String getLocalIp() {
        InetAddress localHost = getLocalHostExactAddress();
        if (localHost == null) {
            log.warn("cant find ip,return default ip 127.0.0.1");
            return "127.0.0.1";
        }
        return localHost.getHostAddress();
    }

    private static InetAddress getLocalHostExactAddress() {
        try {
            InetAddress candidateAddress = null;

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了 就是我们要找的 ,绝大部分情况下都会在此处返回你的ip地址值
                            return inetAddr;
                        }

                        // 若不是site-local地址 那就记录下该地址当作候选
                        if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }

                    }
                }
            }

            // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
            return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
        } catch (Exception e) {
            log.warn("e", e);
        }
        return null;
    }

}
