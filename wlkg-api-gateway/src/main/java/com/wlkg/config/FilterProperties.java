package com.wlkg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 20:52
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "wlkg.filter")
public class FilterProperties {

    private List<String> allowPaths;

}
