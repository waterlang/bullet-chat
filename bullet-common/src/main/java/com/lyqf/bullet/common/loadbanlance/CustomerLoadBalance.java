package com.lyqf.bullet.common.loadbanlance;

import com.lyqf.bullet.common.constant.CommonConstant;
import com.lyqf.bullet.common.exception.ProviderNotFoundException;
import com.lyqf.bullet.common.holder.RpcContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

/**
 * @author chenlang
 * @date 2022/5/19 2:40 下午
 */
@SuppressWarnings("all")
@Slf4j
public class CustomerLoadBalance implements ReactorServiceInstanceLoadBalancer {

    // 服务列表
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public CustomerLoadBalance(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
        String name) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        try {
            String customerNodeId = RpcContextHolder.getStringValue();
            log.info("choose customerNodeId:{}", customerNodeId);
            ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable();
            return supplier.get().next().map(serviceInstances -> chooseService(serviceInstances, customerNodeId));
        } finally {
            RpcContextHolder.remove();
        }
    }

    /**
     * 使用随机数获取服务
     *
     * @param instances
     * @return
     */
    private Response<ServiceInstance> chooseService(List<ServiceInstance> instances, String customerNodeId) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("customer loadbance. No servers available for service.........");
            }

            return new EmptyResponse();
        }

        if (StringUtils.isNotBlank(customerNodeId)) {
            for (ServiceInstance instance : instances) {
                String host = instance.getHost();
                if (customerNodeId.equalsIgnoreCase(host)) {
                    log.info("load banlance choose host:{} node", host);
                    return new DefaultResponse(instance);
                }
            }
            throw  new ProviderNotFoundException(CommonConstant.NO_PROVIDER_ERROR_CODE,"can't find host:" + customerNodeId + " provider");
        }

        // 随机算法
        int size = instances.size();
        Random random = new Random();
        ServiceInstance instance = instances.get(random.nextInt(size));
        return new DefaultResponse(instance);
    }
}
