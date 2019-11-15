package com.wlkg.mq;

import com.aliyuncs.CommonResponse;
import com.aliyuncs.utils.StringUtils;
import com.wlkg.config.SmsProperties;
import com.wlkg.utils.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Auther: Colin
 * @Date: 2019/11/13 10:19
 * @Description:
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsUtils smsUtils;

    /*@Autowired
    private SmsProperties prop;*/

    /**
     * 发送短信验证码
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(value = "wlkg.sms.exchange", type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String, String> msg) {
        if (CollectionUtils.isEmpty(msg)) {
            // 放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if (StringUtils.isEmpty(phone)|| StringUtils.isEmpty(code)) {
            // 放弃处理
            return;
        }
        // 发送消息
        CommonResponse response = this.smsUtils.sendSms(phone, code);
    }
}
