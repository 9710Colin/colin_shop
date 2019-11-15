package com.wlkg.service;

import com.wlkg.client.BrandClient;
import com.wlkg.client.CategoryClient;
import com.wlkg.client.GoodsClient;
import com.wlkg.client.SpecificationClient;
import com.wlkg.pojo.*;
import com.wlkg.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Colin
 * @Date: 2019/11/8 10:32
 * @Description:
 */
@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${wlkg.thymeleaf.destPath}")
    private String destPath;// C:\nginx-1.12.2 - bak\html



    private static final Logger logger = LoggerFactory.getLogger(PageService.class);

    public Map<String,Object> loadModel(Long id){
        //模型数据
        Map<String,Object> modelMap = new HashMap<>();
        //查询spu
        Spu spu = this.goodsClient.querySpuById(id);
        //查询spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        //查询sku
        List<Sku> skus = spu.getSkus();
        //准备品牌数据
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //准备商品分类
        List<Category> categories  = this.categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //查询规格参数?
        List<SpecGroup> specGroups = this.specificationClient.querySpecGroups(spu.getCid3());
        // 装填模型数据
        modelMap.put("spu", spu);
        modelMap.put("title", spu.getTitle());
        modelMap.put("subTitle", spu.getSubTitle());
        modelMap.put("detail", spuDetail);
        modelMap.put("skus", skus);
        modelMap.put("brand", brand);

        modelMap.put("categories", categories);
        modelMap.put("specs", specGroups);
        return modelMap;

    }

    /*
    * @Description //创建html页面
    * @Param [spuId]
    * @return void
    **/
    public void createHtml(Long spuId) throws Exception {
        //创建上下文
        Context context = new Context();
        //把数据加入上下文
        context.setVariables(loadModel(spuId));
        //创建输出路，关联到一个临时文件
        File temp = new File(spuId + ".html");

        //目标页面文件
        File dest = createPath(spuId);
        //备份原页面文件
        File bak = new File(spuId + "_bak.html");

        try(PrintWriter printWriter = new PrintWriter(temp, "UTF-8")){
            //利用thymeleaf模板引擎生成静态页面
            templateEngine.process("item",context,printWriter);
            if (dest.exists()){
                //如果目标文件已存在，先备份
                dest.renameTo(bak);
            }
            //将新页面覆盖旧页面
            FileCopyUtils.copy(temp,dest);
            //成功后将备份页面删除
            bak.delete();
        } catch (IOException e) {
            //失败后降备份页面恢复
            bak.renameTo(dest);
            //重新抛出异常 说明页面生成失效
            throw new Exception(e);
        }finally {
            //删除临时页面
            if (temp.exists()){
                temp.delete();
            }
        }
    }

    private File createPath(Long id) {
        if (id==null){
            return null;
        }
        File dest = new File(this.destPath);
        if (!dest.exists()){
            dest.mkdirs();
        }
        return new File(dest,id+".html");
    }

    /*
    * @Description //判断某个商品的页面是否存在
    * @Param [id]
    * @return boolean
    **/
    public boolean exists(Long id){
        return createPath(id).exists();
    }

    /*
    * @Description //异步创建html文件
    * @Param [id]
    * @return void
    **/
    public void syncCreateHtml(Long id){
        ThreadUtils.execute(()->{
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
