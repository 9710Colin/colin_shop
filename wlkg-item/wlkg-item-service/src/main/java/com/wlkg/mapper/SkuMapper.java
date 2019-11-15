package com.wlkg.mapper;

import com.wlkg.pojo.Sku;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 17:24
 * @Description:
 */
public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku> {
}
