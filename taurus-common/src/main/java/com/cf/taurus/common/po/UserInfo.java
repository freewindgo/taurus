package com.cf.taurus.common.po;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfo {
    private Long id;

    private String openId;

    private Byte sex;

    private String name;

    private String city;

    private String province;

    private String country;

    private String password;

    private Integer point;

    private String device;

    private String system;

    private String image;

    private Byte isUcg;

    private Byte isDelete;

    private String ctime;

    private String utime;

    /******以下为页面字段******/
    /**
     * 小程序用户登陆code，通过code获取openId
     */
    private String code;
}