package com.cf.taurus.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * EmptyUtils
 * <p>
 * 校验Obj和Collection是否为空
 *
 * @author 于文硕
 * @since 2018/5/15 11:39
 */
public class EmptyUtils {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || "0".equals(obj);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean isNotEmpty(Object obj) {
        return obj != null && !"0".equals(obj);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }


}
