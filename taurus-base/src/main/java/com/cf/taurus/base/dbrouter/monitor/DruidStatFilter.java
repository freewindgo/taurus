package com.cf.taurus.base.dbrouter.monitor;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * DruidStatFilter
 * druid的过滤
 *
 * @author 于文硕
 * @since 2018/5/14 15:24
 */


@WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",
        initParams={
                @WebInitParam(name="exclusions",value="*.mp4,*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
        })
public class DruidStatFilter extends WebStatFilter{
}
