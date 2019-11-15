package com.wlkg.controller;

import com.wlkg.pojo.User;
import com.wlkg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: Colin
 * @Date: 2019/11/13 16:12
 * @Description:
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;



    /**
     * 验证数据接口
     * - 请求方式：GET
     * - 请求路径：/check/{param}/{type}
     * - 请求参数：param,type
     * - 返回结果：true或fals- 请求方式：GET
     * - 请求路径：/check/{param}/{type}
     * - 请求参数：param,type
     * - 返回结果：true或false
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        return ResponseEntity.ok(this.userService.checkData(data, type));
    }
    /**
     * 发送手机验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(String phone){
        Boolean aBoolean = userService.sendVerifyCode(phone);
        return ResponseEntity.ok(null);
    }
    /**
     * 注册
     *
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
        //1、检验user对象的数据

        //2、实现用户注册
        this.userService.register(user,code);
        return ResponseEntity.ok(null);
    }


    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        User user = userService.queryUser(username, password);
        return ResponseEntity.ok(user);
    }
}
