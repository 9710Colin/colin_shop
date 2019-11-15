package com.wlkg.client;

import com.wlkg.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 15:14
 * @Description:
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
