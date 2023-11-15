package com.atong.atojbackendcommon.constant;

/**
 * 消息队列常量
 *  @author <a href="https://gitee.com/x-2022-k">阿通</a>
 * @CreateDate: 2023/11/15 20:07
 */
public interface RabbitMqConstant {

    String EXCHANGE_NAME = "code_exchange";

    String EXCHANGE_TYPE = "direct";

    String QUEUE_NAME = "code_queue";

    String ROUTING_KEY = "my_routingKey";
}
