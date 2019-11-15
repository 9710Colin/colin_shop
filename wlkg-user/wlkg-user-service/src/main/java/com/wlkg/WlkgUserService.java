package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: Colin
 * @Date: 2019/11/13 11:55
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.wlkg.mapper")
public class WlkgUserService {
    public static void main(String[] args) {
        SpringApplication.run(WlkgUserService.class, args);
    }
}

