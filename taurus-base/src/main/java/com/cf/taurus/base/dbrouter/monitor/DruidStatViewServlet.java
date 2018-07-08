package com.cf.taurus.base.dbrouter.monitor;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * DruidStatViewServlet
 *
 * druid 页面监控配置   访问url  项目路径/druid/index.html
 *
 * 在使用springboot的时候,需要在启动类使用 @ServletComponentScan注解
 *
 * @author 于文硕
 * @since 2018/5/14 15:21
 */


@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",initParams = {
        //@WebInitParam(name = "allow",value = "127.0.0.1,192.168.1.142"),//白名单
        //@WebInitParam(name = "deny",value = "126.12.22.1"),//黑名单 (存在共同时，deny优先于allow)
        @WebInitParam(name="loginUsername",value="admin"),// 用户名
        @WebInitParam(name="loginPassword",value="123456"),// 密码
        @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能


})
public class DruidStatViewServlet extends StatViewServlet{
}
