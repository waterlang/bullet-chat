package com.lyqf.bullet.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/17 9:01 上午
 */

@Slf4j
@EnableFeignClients(basePackages = {"com.lyqf.bullet.*"})
@EnableDiscoveryClient
@SpringBootApplication
public class PushApp {

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(PushApp.class);
            app.run(args);
        } catch (Exception e) {
            log.warn("main方法启动失败,", e);
        }
    }
}
