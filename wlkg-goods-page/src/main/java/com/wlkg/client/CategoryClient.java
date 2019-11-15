package com.wlkg.client;

import com.wlkg.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/7 18:35
 * @Description:
 */
@FeignClient(name = "item-service")
public interface CategoryClient extends CategoryApi {
}
