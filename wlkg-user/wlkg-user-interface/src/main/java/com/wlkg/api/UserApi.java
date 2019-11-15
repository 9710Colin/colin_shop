package com.wlkg.api;

import com.wlkg.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 15:07
 * @Description:
 */

public interface UserApi {
    @GetMapping("query")
    User queryUsernameAndPassword(@RequestParam("username") String username, @RequestParam("password") String password);
    @PostMapping("register")
    Void register(@Valid User user, @RequestParam("code") String code);
    @PostMapping("/code")
    public Void sendVerifyCode(String phone);
    @GetMapping("check/{data}/{type}")
    public Boolean checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type);
}

