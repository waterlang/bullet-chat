package com.lyqf.bullet.push.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.lyqf.bullet.push.biz.CommonMsgPushBiz;
import com.lyqf.bullet.push.constant.KafkaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/17 3:55 下午
 */
@Slf4j
@Component
public class CommonMsgConsumer {

    @Autowired
    private CommonMsgPushBiz commonMsgPushBiz;

    @KafkaListener(topics = KafkaConstant.COMMON_MSG_TOPIC)
    public void listen(ConsumerRecord<String, Object> record) {
        String data = record.value().toString();
        log.info("receive data :{}", data);
        commonMsgPushBiz.pushCommonMsg(data);
    }

}
