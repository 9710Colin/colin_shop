package com.wlkg.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 17:03
 * @Description:
 */
@Table(name = "tb_sku")
@Data
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;// 商品特殊规格的键值对
    private String indexes;// 商品特殊规格的下标
    private Boolean enable;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间

    @Transient
    private Integer stock;// 库存 表单里会有一个stock属性值，直接封装sku里，这样对库存不要单独封装一个对象了
}
