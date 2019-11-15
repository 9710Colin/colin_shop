package com.wlkg.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wlkg.config.FilterProperties;
import com.wlkg.config.JwtApiProperties;
import com.wlkg.utils.CookieUtils;
import com.wlkg.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({JwtApiProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private FilterProperties filterProperties;

    @Autowired
    private JwtApiProperties prop;

//    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest req = ctx.getRequest();
        // 获取路径
        String requestURI = req.getRequestURI();
        // 判断白名单
        return !isAllowPath(requestURI);
    }

    //判断当前的requestURI是否在白名单中
    private boolean isAllowPath(String requestURI) {
        // 定义一个标记
        boolean flag = false;
        // 遍历允许访问的路径
        for (String path : this.filterProperties.getAllowPaths()) {
            // 然后判断是否是符合
            if(requestURI.startsWith(path)){
                flag = true;
                break;
            }
        }
        return flag;
    }


    @Override
    public Object run() throws ZuulException {
        //1、获取cookies中的token值
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        // 2、采用公钥对token进行解密
        try {
            // 校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            // 没有token，登录校验失败 拦截
            ctx.setSendZuulResponse(false);
            //返回401状态码，也可以考虑重定向到登陆页
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
