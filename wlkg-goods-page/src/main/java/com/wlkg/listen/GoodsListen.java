package com.wlkg.listen;

import com.wlkg.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: Colin
 * @Date: 2019/11/11 17:39
 * @Description:
 */
@Component
public class GoodsListen {

    @Autowired
    private PageService pageService;

    /*@RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "wlkg.create.page.queue",durable = "true"),
            exchange = @Exchange(
                    value = "wlkg.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert","item.update"}
    ))
    public void listenCreate(Long spuId) throws Exception {
        if (spuId==null){
            return;
        }
        pageService.createHtml(spuId);
    }*/
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "wlkg.create.page.queue", durable = "true"),
            exchange = @Exchange(
                    value = "wlkg.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenCreate(Long id) throws Exception {
        if (id == null) {
            return;
        }
        // 创建页面
        pageService.createHtml(id);
    }

}
