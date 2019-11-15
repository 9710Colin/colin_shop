package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 13:48
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WlkgAuthService {
    public static void main(String[] args) {
        SpringApplication.run(WlkgAuthService.class,args);
    }
}
