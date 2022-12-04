package com.lyqf.bullet.logic.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.alibaba.fastjson.JSON;
import com.lyqf.bullet.common.enums.LevelEnum;
import com.lyqf.bullet.logic.constant.KafkaConstant;
import com.lyqf.bullet.logic.dto.MsgDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/16 4:16 下午
 */
@Component
@Slf4j
public class KafkaManger {

    @Autowired
    KafkaTemplate kafkaTemplate;

    /**
     * todo 同一用户发到同一个partition中
     *   相同hashcode一样的roomId走同一个topic
     */
    public void sendMsg(MsgDTO msgDTO) {
        ListenableFuture<SendResult<String, String>> future = null;
        if (LevelEnum.SYSTEM.getCode().equals(msgDTO.getLevel())) {
            future = kafkaTemplate.send(KafkaConstant.SYS_MSG_TOPIC, JSON.toJSONString(msgDTO));
        } else if (LevelEnum.EXT.getCode().equals(msgDTO.getLevel())) {
            future = kafkaTemplate.send(KafkaConstant.EXT_MSG_TOPIC, JSON.toJSONString(msgDTO));
        } else {
            future = kafkaTemplate.send(KafkaConstant.COMMON_MSG_TOPIC, JSON.toJSONString(msgDTO));
        }

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("kafka 发送消息成功，result :{}", result.toString());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("kafka 发送消息失败", ex);
            }
        });
    }

}
