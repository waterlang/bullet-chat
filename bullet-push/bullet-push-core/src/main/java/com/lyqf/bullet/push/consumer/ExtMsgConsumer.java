package com.lyqf.bullet.push.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.lyqf.bullet.push.biz.ExtMsgPushBiz;
import com.lyqf.bullet.push.constant.KafkaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlang
 * @date 2022/5/17  3:55 下午
 */

@Slf4j
@Component
public class ExtMsgConsumer {

    @Autowired
    private ExtMsgPushBiz extMsgPushBiz;


    /**
     *
     * @param record
     */
    @KafkaListener(topics = KafkaConstant.EXT_MSG_TOPIC)
    public void listen(ConsumerRecord<String, Object> record) {
        String data = record.value().toString();
        log.info("ExtMsgConsumer receive data :{}", data);
        extMsgPushBiz.pushExtMsg(data);
    }



}
