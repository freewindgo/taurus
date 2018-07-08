package com.cf.taurus.base.dbrouter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RouteNode
 *
 * @author 于文硕
 * @since 2018/5/11 17:40
 */
public class RouteNode {
    private Method method;
    private Class<?> clazz;
    private Class<?> type;

    public RouteNode(Class<?> clazz, Class<?> type, Method method) {
        if (type != Integer.class && type != Integer.TYPE && type != Long.class && type != Long.TYPE) {
            throw new IllegalArgumentException("return type not support");
        } else {
            this.clazz = clazz;
            this.type = type;
            this.method = method;
        }
    }

    public int process(Object arg) throws InvocationTargetException, IllegalAccessException {
        try {
            if (arg.getClass() != this.clazz && arg.getClass().getSuperclass() != this.clazz) {
                throw new IllegalAccessError();
            } else if (this.type != Integer.class && this.type != Integer.TYPE) {
                if (this.type != Long.class && this.type != Long.TYPE) {
                    throw new IllegalArgumentException("only int or long support.");
                } else {
                    return (int)((Long)this.method.invoke(arg)).longValue() & DatabaseRoute.shift;
                }
            } else {
                return ((Integer)this.method.invoke(arg)).intValue() & DatabaseRoute.shift;
            }
        } catch (IllegalArgumentException var3) {
            throw new IllegalArgumentException("this argument not support.");
        }
    }
}
