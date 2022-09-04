package com.wsk.parent.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author:WuShangke
 * @create:2022/8/20-20:45
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = "com.wsk.parent.order.mapper")
@EnableFeignClients(basePackages = "com.wsk")
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class,args);
    }
}
