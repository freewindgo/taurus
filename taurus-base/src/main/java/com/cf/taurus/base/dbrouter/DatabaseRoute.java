package com.cf.taurus.base.dbrouter;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DatabaseRoute
 *
 * @author 于文硕
 * @since 2018/5/11 17:39
 */
public class DatabaseRoute extends AbstractRoutingDataSource {
    public static final ThreadLocal<Integer> _select = new ThreadLocal();
    public static final ThreadLocal<Boolean> _trans = new ThreadLocal() {
        protected Boolean initialValue() {
            return false;
        }
    };
    protected static final Map<String, Integer> mapSingleRouter = new HashMap();
    public static int shift = 1;
    protected static final Map<String, RouteNode> mapClusterRouter = new HashMap();


    public void setTargetDataSources(Map<Integer, DataSource> clusterSources, Map<Integer, DataSource> singleSources) {
        Map<Object, Object> targetSources = new HashMap();
        if (null != clusterSources && clusterSources.size() > 0) {
            int size = clusterSources.size();
            shift = size - 1;
            if ((size & size - 1) != 0) {
                throw new IllegalArgumentException("Cluster DataSource must be a multiple of 2.");
            }

            for(int i = 0; i < size; ++i) {
                if (!clusterSources.containsKey(i)) {
                    throw new IllegalArgumentException("Cluster DataSource key must be continuous,and start from 0");
                }
            }

            Iterator var8 = clusterSources.entrySet().iterator();

            while(var8.hasNext()) {
                Map.Entry<Integer, DataSource> entry = (Map.Entry)var8.next();
                targetSources.put(entry.getKey(), entry.getValue());
            }
        }

        if (null != singleSources && singleSources.size() > 0) {
            Iterator var7 = singleSources.entrySet().iterator();

            while(var7.hasNext()) {
                Map.Entry<Integer, DataSource> entry = (Map.Entry)var7.next();
                if (targetSources.containsKey(entry.getKey())) {
                    throw new IllegalArgumentException("single DataSource key can not contain cluster Datasource key");
                }

                targetSources.put(entry.getKey(), entry.getValue());
            }
        }

        super.setTargetDataSources(targetSources);
    }

    protected Object determineCurrentLookupKey() {
        return _select.get();
    }

    public static Integer doSingleRoute(String key) throws Exception {
        if (null != key && key.length() > 0) {
            if (mapSingleRouter.containsKey(key)) {
                return (Integer)mapSingleRouter.get(key);
            } else {
                throw new Exception("do not contain key:" + key);
            }
        } else {
            throw new Exception("route key is blank");
        }
    }

    public static Integer doCluterRoute(Object[] args) throws Exception {
        Assert.state(args != null && args.length > 0 && args[0] != null, "first argument not found!");
        Object arg0 = args[0];
        RouteNode routeNode = null;
        if ((routeNode = (RouteNode)mapClusterRouter.get(arg0.getClass().getName())) != null) {
            return routeNode.process(arg0);
        } else {
            Long hotelId = null;
            if (arg0 instanceof Long) {
                hotelId = (Long)arg0;
            } else {
                hotelId = getHotelId(arg0);
            }

            if (hotelId != null && hotelId.longValue() > 0L) {
                return (int)(hotelId.longValue() & (long)shift);
            } else {
                throw new IllegalArgumentException("The first argument not support.");
            }
        }
    }

    private static Long getHotelId(Object arg) {
        try {
            PropertyDescriptor orgIdProp = BeanUtils.getPropertyDescriptor(arg.getClass(), "hotelId");
            Object hotelId = orgIdProp.getReadMethod().invoke(arg);
            if (hotelId != null && hotelId instanceof Long) {
                return (Long)hotelId;
            }
        } catch (Exception var3) {
            return null;
        }

        return null;
    }
}
