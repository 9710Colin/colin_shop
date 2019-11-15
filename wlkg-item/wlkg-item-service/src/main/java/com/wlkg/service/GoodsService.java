package com.wlkg.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlkg.enums.ExceptionEnums;
import com.wlkg.exception.WlkgException;
import com.wlkg.mapper.*;
import com.wlkg.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: Colin
 * @Date: 2019/10/30 17:13
 * @Description:商品业务层
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 实现商品添加
     * @param spu
     */
    @Transient
    public void saveGoods(Spu spu) {
        //1.添加spu
        spu.setSaleable(true);//表示可销售
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        //2、添加spu(加完之后spu里自动就有id了 自增)
        spuMapper.insert(spu);
        //3、添加spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
        //3、添加sku
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku : spu.getSkus()) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            sku.setSpuId(spu.getId());
            skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        stockMapper.insertList(stocks);//一次性加进去

        sendMessage(spu.getId(),"insert");//item.insert

    }

    /**
     * 实现商品查询
     * @param page     当前页
     * @param rows     每一页的大小
     * @param saleable 上下架
     * @param key      关键词
     * @return
     */
    public PageResult<Spu> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key) {
        // 1、查询SPU
        // 分页,最多允许查100条
        PageHelper.startPage(page, Math.min(rows, 100));
        //创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //1.设置上下架条件
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //2.设置关键词搜索
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //3.设置默认排序
        example.setOrderByClause("last_update_time desc");
        List<Spu> spus = spuMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(spus)) {
            throw new WlkgException(ExceptionEnums.GOODS_NOT_FOUND);
        }
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        for (Spu spu : spus) {
            //2、查询spu的商品分类名称，要查三级分类
            List<String> names = categoryService.quaryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            //将分类名称拼接后存入
            spu.setCname(StringUtils.join(names, "/"));

            //3、查询spu品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spu.setBname(brand.getName());
        }
        PageResult<Spu> spuPageResult = new PageResult<>(spuPageInfo.getTotal(), spus);
        return spuPageResult;

    }

    public SpuDetail querySupDetailById(Long id) {

        return spuDetailMapper.selectByPrimaryKey(id);
    }

    //为了页面回显方便，我们一并把sku的库存stock也查询出来
    /**
     * 根据商品id查询所有的sku信息
     * @param spuId
     * @return
     */
    public List<Sku> querySkuBySpuId(Long spuId) {
        // 查询sku
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(record);

        //查询每个sku所关联的库存
        for (Sku sku : skus) {
            // 同时查询出库存
            sku.setStock(stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        }
        return skus;
    }

    /**
     * 修改商品
     * @param spu
     */
    @Transactional
    public void update(Spu spu) {
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);

        if (!CollectionUtils.isEmpty(skuList)) {
            //删除sku和stock
            skuMapper.delete(sku);
            //删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            for (Long id : ids) {
                stockMapper.deleteByPrimaryKey(id);
            }
            //测试
            if (stockMapper.selectByPrimaryKey(ids.get(0)) == null) {
                System.out.println("删除成功了");
            }
        }
        //修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        int i = spuMapper.updateByPrimaryKeySelective(spu);
        if (i != 1) {
            throw new WlkgException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        //修改detail
        i = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if (i != 1) {
            throw new WlkgException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        //新增sku和stock
        saveSkuAndStock(spu.getSkus(), spu.getId());

        sendMessage(spu.getId(),"update");

    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku : skus) {
            if (!sku.getEnable()) {
                continue;
            }
            // 保存sku
            sku.setSpuId(spuId);
            // 默认不参与任何促销
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);
            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        stockMapper.insertList(stocks);
    }

    //删除
    public void deleteGoodsById(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);

        if (!CollectionUtils.isEmpty(skuList)) {
            //删除sku和stock
            skuMapper.delete(sku);
            //删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            for (Long id5 : ids) {
                stockMapper.deleteByPrimaryKey(id5);
            }
            spuMapper.deleteByPrimaryKey(id);
            spuDetailMapper.deleteByPrimaryKey(id);

        }
    }

    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu==null){
            throw new WlkgException(ExceptionEnums.GOODS_NOT_FOUND);
        }
        //查询sku
        spu.setSkus(querySkuBySpuId(id));
        //查询detail
        spu.setSpuDetail(querySupDetailById(id));
        return spu;
    }
    private void sendMessage(Long spuId, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, spuId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
