package com.atong.atojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.atong.atojbackendquestionservice.mapper")
@EnableScheduling //
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.atong")
@EnableDiscoveryClient //开启注册中心服务发现
@EnableFeignClients(basePackages = {"com.atong.atojbackendserviceclient.service"}) // 找到对应 Feign 客户端Bean 的位置
public class AtojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtojBackendQuestionServiceApplication.class, args);
    }

}
