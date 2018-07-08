package com.cf.taurus.common.util;

/**
 * CommonUtils
 *
 * @author 于文硕
 * @since 2018/6/5 18:08
 */
public class CommonUtils {

    public static Integer getPageStart(Integer start, Integer size){
        if(EmptyUtils.isNotEmpty(start) && EmptyUtils.isNotEmpty(size)){
            return start * size;
        }
        return 0;
    }
}
