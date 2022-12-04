package com.lyqf.bullet.logic;

import com.lyqf.bullet.logic.config.CustomerLoadBalancerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author chenlang
 * @date 2022/5/17 9:01 上午
 */

@Slf4j
@EnableFeignClients(basePackages = {"com.lyqf.bullet.comet.client"})
@EnableDiscoveryClient
@SpringBootApplication
@LoadBalancerClient(name = "myServer", configuration = CustomerLoadBalancerConfig.class)
public class LogicApp {

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(LogicApp.class);
            app.run(args);
        } catch (Exception e) {
            log.warn("main方法启动失败,", e);
        }
    }
}
