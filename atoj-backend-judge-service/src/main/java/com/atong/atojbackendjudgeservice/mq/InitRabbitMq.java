package com.atong.atojbackendjudgeservice.mq;

import com.atong.atojbackendcommon.constant.RabbitMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于创建测试程序用到的交互机和队列（只用在程序启动前执行一次）
 *
 * @author <a href="https://gitee.com/x-2022-k">阿通</a>
 * @CreateDate: 2023/11/15 18:01
 */
@Slf4j
public class InitRabbitMq {

    public static void doInit() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String exchangeName = RabbitMqConstant.EXCHANGE_NAME;
            channel.exchangeDeclare(exchangeName, RabbitMqConstant.EXCHANGE_TYPE);

            //创建队列, 随机分配一个队列名称
            String queueName = RabbitMqConstant.QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, RabbitMqConstant.ROUTING_KEY);
            log.info("消息队列启动成功");
        } catch (Exception e) {
            log.error("消息队列启动失败");
        }
    }
}
