package com.wlkg.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: Colin
 * @Date: 2019/11/14 13:55
 * @Description:
 */
public class ObjectUtils {

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Long toLong(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Double || obj instanceof Float) {
            return Long.valueOf(StringUtils.substringBefore(obj.toString(), "."));
        }
        if (obj instanceof Number) {
            return Long.valueOf(obj.toString());
        }
        if (obj instanceof String) {
            return Long.valueOf(obj.toString());
        } else {
            return 0L;
        }
    }

    public static Integer toInt(Object obj) {
        return toLong(obj).intValue();
    }
}

