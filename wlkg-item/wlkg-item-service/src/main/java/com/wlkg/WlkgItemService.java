package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: Colin
 * @Date: 2019/10/21 14:43
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.wlkg.mapper")
public class WlkgItemService {
    public static void main(String[] args) {
        SpringApplication.run(WlkgItemService.class, args);
    }
}
