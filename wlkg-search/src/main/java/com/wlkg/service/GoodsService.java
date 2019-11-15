package com.wlkg.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wlkg.client.BrandClient;
import com.wlkg.client.CategoryClient;
import com.wlkg.client.GoodsClient;
import com.wlkg.client.SpecificationClient;
import com.wlkg.pojo.*;
import com.wlkg.repository.GoodsRepository;
import com.wlkg.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 根据Spu对象构建索引对象
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu) {
        String all = spu.getTitle()+" ";
        //1.查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> categoryNames = categories.stream().map(s->s.getName()).collect(Collectors.toList());
        all += StringUtils.join(categoryNames, " ")+" ";//小说 武侠 金庸
        //2.查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        all += brand.getName();

        //3.设置价格
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        //List<Long> prices = skus.stream().map(s->s.getPrice()).collect(Collectors.toList());
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            //skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuMap.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skuList.add(skuMap);
        });

        //4.设置规格
        List<SpecParam> params = specificationClient.querySpecParam(null, spu.getCid3(), true, null);
        //查询商品详情
        SpuDetail spuDetail = goodsClient.querySupDetailById(spu.getId());

        //获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //定义spec对应的map
        HashMap<String, Object> specs = new HashMap<>();
        //对规格进行遍历，并封装spec，其中spec的key是规格参数的名称，值是商品详情中的值
        for (SpecParam param : params) {
            //key是规格参数的名称
            String key = param.getName();
            Object value = "";

            if (param.getGeneric()) {
                //参数是通用属性，通过规格参数的ID从商品详情存储的规格参数中查出值
                value = genericSpec.get(param.getId());
                if (param.getNumeric()) {
                    //参数是数值类型，处理成段，方便后期对数值类型进行范围过滤
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                //参数不是通用类型
                value = specialSpec.get(param.getId());
            }
            value = (value == null ? "其他" : value);
            //存入map
            specs.put(key, value);
        }


        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(all);//设置标题，分类，甚至品牌
        goods.setPrice(prices);//设置价格
        goods.setSkus(JsonUtils.serialize(skuList));// 设置Skus的json数据
        goods.setSpecs(specs);//设置规格

        return goods;
    }

    //设置分段端信息
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /*
    * @Description //实现索引的搜索过程
    * @Param [request]
    * @return com.user.wlkg.common.pojo.PageResult<com.user.wlkg.pojo.Goods>
    **/
    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }

        //构建原始ES搜索api
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //1、构造搜索条件，根据all字段进行搜索
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));
        //2、设置分页
        int page = request.getPage();
        int size = request.getSize();
        //注：ESearch分页码从0开始，但是正常显示从1开始，所以要减1
        queryBuilder.withPageable(PageRequest.of(page-1,size));
        //3、筛选需要显示的字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id","skus","subTitle"}, null));
        //4、执行搜索 拿到当前数据
        Page<Goods> pageInfo = goodsRepository.search(queryBuilder.build());
        //5、构造返回对象
      /*  PageResult<Goods> result = new PageResult<>();
        result.setItems(pageInfo.getContent());
        result.getTotalPage(pageInfo.getTotalPages());
        result.getTotal(pageInfo.getTotalElements());
        return null;*/
        List<Goods> goods = pageInfo.getContent();
        long total = pageInfo.getTotalElements();
        long totalPage = pageInfo.getTotalPages();
        return new PageResult<>(total, totalPage, goods);

    }
    public void createIndex(Long id) throws IOException {
        //查询spu
        Spu spu = goodsClient.querySpuById(id);
        //构建goods
        Goods goods = buildGoods(spu);
        //存入索引库
        goodsRepository.save(goods);
    }

}
