package com.wlkg.service;

import com.wlkg.enums.ExceptionEnums;
import com.wlkg.exception.WlkgException;
import com.wlkg.mapper.UserMapper;
import com.wlkg.pojo.User;
import com.wlkg.utils.CodecUtils;
import com.wlkg.utils.NumberUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Colin
 * @Date: 2019/11/13 14:26
 * @Description:
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    static final String KEY_PREFIX = "user:code:phone:";

    /**
     * 验证用户方法
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        User user = userMapper.selectOne(record);
        return user == null;
    }

    /**
     * 发送短信
     *
     * @param phone
     * @return
     */
    public Boolean sendVerifyCode(String phone) {
        //1.生成验证码
        String code = NumberUtils.generateCode(6);

        try {
            //2.保存redis
            //user:code:phone:18018121321
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            //3.发送消息
            Map<String, String> map = new HashMap<>();
            map.put("code", code);
            map.put("phone", phone);
            amqpTemplate.convertAndSend("wlkg.sms.exchange", "sms.verify.code", map);
            return true;
        } catch (AmqpException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     */
    public void register(User user, String code) {
        //1、检验短信验证码
        String prefix = KEY_PREFIX + user.getPhone();
        String redisCode = (String) redisTemplate.opsForValue().get(prefix);
        //检验验证码是否正确
        if (!code.equals(redisCode)) {
            //不正确，返回
            throw new WlkgException(ExceptionEnums.INVALID_VERFIY_CODE);
        }
        //2、生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //3、对密码加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        //4、写入数据库
        user.setCreated(new Date());

        if (userMapper.insert(user) == 1) {
            //5、删除redis中的验证码
            redisTemplate.delete(prefix);
        }
    }

    /**
     * 实现用户查询
     *
     * @param username
     * @param password
     * @return
     */
    public User queryUser(String username, String password) {
        //1.先根据用户名查询
        User user = new User();
        user.setUsername(username);
        User retUser = userMapper.selectOne(user);
        if (retUser == null) {
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        //2.比对密码
        String md5Pwd = CodecUtils.md5Hex(password, retUser.getSalt());
        if (!retUser.getPassword().equals(md5Pwd)) {
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        } else {
            return retUser;
        }

    }
}