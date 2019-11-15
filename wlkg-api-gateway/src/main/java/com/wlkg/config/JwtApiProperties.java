package com.wlkg.config;

import com.wlkg.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;


//@Component
@Data
@ConfigurationProperties(prefix = "wlkg.jwt")
public class JwtApiProperties {

    private String pubKeyPath;// 公钥

    private PublicKey publicKey; // 公钥

    private String cookieName;

//    private static final Logger logger = LoggerFactory.getLogger(JwtApiProperties.class);

    @PostConstruct
    public void init(){
        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
//            logger.error("初始化公钥失败！", e);
            throw new RuntimeException();
        }
    }
}
