package com.wlkg.pojo;

import lombok.Data;

import javax.persistence.Transient;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 20:04
 * @Description:
 */
@Data
public class SpuBo extends Spu {
    @Transient
    String cname;// 商品分类名称
    @Transient
    String bname;// 品牌名称
}
