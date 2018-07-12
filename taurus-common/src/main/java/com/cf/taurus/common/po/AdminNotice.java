package com.cf.taurus.common.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminNotice {
    private Long id;

    private String content;

    private String ctime;

    private Integer deleted;

}