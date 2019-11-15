package com.wlkg.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 22:43
 * @Description:
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),//new一个枚举对象
    CATEGORY_NOT_FOUND(30000,"没有此类别"),
    BRAND_IS_EMPTY(40000,"品牌为空"),
    SPECIFICATION_PARAMS_IS_EMPTY(4000,"反正为空"),
    GOODS_NOT_FOUND(2000,"没找到商品"),
    SPECIFICATION_GROUP_IS_EMPTY(5555,"规格组为空"),

    GOODS_UPDATE_ERROR(2001,"商品更新失败"),
    INVALID_VERFIY_CODE(555,"验证码错误"),
    INVALID_USERNAME_PASSWORD(55,"用户名或密码错误"),
    NO_AUTHORIZED(1002,"未授权用户！");
    private int code;
    private String msg;
}
