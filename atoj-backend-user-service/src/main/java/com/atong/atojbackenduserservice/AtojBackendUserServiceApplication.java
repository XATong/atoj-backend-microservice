package com.atong.atojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.atong.atojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.atong")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.atong.atojbackendserviceclient.service"})
public class AtojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtojBackendUserServiceApplication.class, args);
    }

}
