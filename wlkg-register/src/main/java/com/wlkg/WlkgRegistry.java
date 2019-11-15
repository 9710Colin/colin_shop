package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: Colin
 * @Date: 2019/10/21 14:05
 * @Description:
 */
@SpringBootApplication
@EnableEurekaServer
public class WlkgRegistry {
    public static void main(String[] args) {
        SpringApplication.run(WlkgRegistry.class, args);
    }
}
