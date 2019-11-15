package com.wlkg.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 17:00
 * @Description:
 */
/*整体是一个json格式数据，包含Spu表所有数据：
        - brandId：品牌id
        - cid1、cid2、cid3：商品分类id
        - subTitle：副标题
        - title：标题
        - spuDetail：是一个json对象，代表商品详情表数据
        - afterService：售后服务
        - description：商品描述
        - packingList：包装列表
        - specialSpec：sku规格属性模板
        - genericSpec：通用规格参数
        - skus：spu下的所有sku数组，元素是每个sku对象：
        - title：标题
        - images：图片
        - price：价格
        - stock：库存
        - ownSpec：特有规格参数
        - indexes：特有规格参数的下标*/

@Table(name = "tb_spu")
@Data
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架

    @JsonIgnore//因为从客户端提交数据没有这些东西，这些东西不需要从客户端提交，到时候给你设置好
    private Boolean valid;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间

    @JsonIgnore
    private Date lastUpdateTime;// 最后修改时间

    @Transient
    private String bname;//品牌名称

    @Transient
    private String cname;//分类名称

/*    请求参数：Spu的json格式的对象，spu中包含spuDetail和Sku集合。
    这里我们该怎么接收？我们可以直接对Spu进行扩展，添加两个新字段spuDetail和skus字段：*/

    @Transient//透明 这个字段不要和数据库映射
            SpuDetail spuDetail;// 商品详情 因为希望表单提交的所有数据全部封装到一个对象(spu)里，包括详情和sku信息

    @Transient
    List<Sku> skus;// sku列表

}
