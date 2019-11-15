package com.wlkg.client;

import com.wlkg.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 13:25
 * @Description:
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
