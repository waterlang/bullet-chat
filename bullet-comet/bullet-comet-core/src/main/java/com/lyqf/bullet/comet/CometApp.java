package com.lyqf.bullet.comet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

/**
 *  前端使用：https://github.com/joewalnes/reconnecting-websocket
 * @author chenlang
 * @date 2022/5/10 5:32 下午
 */

@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lyqf.bullet.*"})
@SpringBootApplication
public class CometApp {

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(CometApp.class);
            app.run(args);

            BulletChatServer server = new BulletChatServer(8889);
            server.start();
        } catch (Exception e) {
            log.warn("main方法启动失败,", e);
        }
    }

}
