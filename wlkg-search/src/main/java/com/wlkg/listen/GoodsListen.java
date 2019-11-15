package com.wlkg.listen;

import com.wlkg.client.GoodsClient;
import com.wlkg.repository.GoodsRepository;
import com.wlkg.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: Colin
 * @Date: 2019/11/11 17:13
 * @Description:
 */
@Component
public class GoodsListen {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsRepository goodsRepository;

/*    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "wlkg.create.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "wlkg.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert","item.update"}
    ))
    public void listenCreat(Long spuId){
        //监听消息队列，更新索引
        Spu spu = goodsClient.querySpuById(spuId);
        Goods goods = goodsService.buildGoods(spu);
        goodsRepository.save(goods);
    }*/
    /**
     * 处理insert和update的消息
     * @param id
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "wlkg.create.index.queue", durable = "true"),
            exchange = @Exchange(
                    value = "wlkg.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenCreate(Long id) throws Exception {
        if (id == null) {
            return;
        }
        // 创建或更新索引
        this.goodsService.createIndex(id);
    }

}
