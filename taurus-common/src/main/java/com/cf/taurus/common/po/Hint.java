package com.cf.taurus.common.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Hint  extends PageInfo{
    private Integer id;

    private String title;

    private Integer userId;

    private String userName;

    private String content;

    private String ctime;

    private Integer deleted;

}