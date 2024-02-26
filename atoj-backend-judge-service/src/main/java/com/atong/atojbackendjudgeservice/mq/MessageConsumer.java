package com.atong.atojbackendjudgeservice.mq;

import com.atong.atojbackendcommon.constant.RabbitMqConstant;
import com.atong.atojbackendjudgeservice.judge.JudgeService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 消费者代码
 */
@Component
@Slf4j
public class MessageConsumer {

    @Resource
    private JudgeService judgeService;


    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {RabbitMqConstant.QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        long questionId = Long.parseLong(message);
        try {
            judgeService.doJudge(questionId);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}