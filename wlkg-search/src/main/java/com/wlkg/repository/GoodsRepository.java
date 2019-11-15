package com.wlkg.repository;

import com.wlkg.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: Colin
 * @Date: 2019/11/6 17:05
 * @Description:
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
