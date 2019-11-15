package com.wlkg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Auther: Colin
 * @Date: 2019/11/13 10:11
 * @Description:
 */
@Data
@ConfigurationProperties(value = "wlkg.sms")
public class SmsProperties {
    String accessKeyId;
    String accessKeySecret;
    String signName;
    String verifyCodeTemplate;
}