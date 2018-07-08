package com.cf.taurus.base.dbrouter;

import org.springframework.beans.BeanUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * DatabaseRouteRegister
 *
 * @author 于文硕
 * @since 2018/5/11 17:46
 */
public class DatabaseRouteRegister {
    public static void registSingleRouter(String name, Integer index) {
        if (null != name && null != index && name.length() > 0) {
            DatabaseRoute.mapSingleRouter.put(name, index);
        } else {
            throw new IllegalArgumentException("argument should not null!");
        }
    }

    public static void registClusterRouter(Class<?> clazz, String routeKey) throws NoSuchFieldException, IntrospectionException {
        if (null != clazz && null != routeKey && routeKey.length() > 0) {
            try {
                PropertyDescriptor property = BeanUtils.getPropertyDescriptor(clazz, routeKey);
                Method method = property.getReadMethod();
                DatabaseRoute.mapClusterRouter.put(clazz.getName(), new RouteNode(clazz, method.getReturnType(), method));
            } catch (SecurityException var4) {
                throw new IllegalArgumentException("regist route class error.", var4);
            }
        } else {
            throw new IllegalArgumentException("argument should not null!");
        }
    }
}
