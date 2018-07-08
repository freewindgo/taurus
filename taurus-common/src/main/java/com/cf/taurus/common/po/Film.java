package com.cf.taurus.common.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Film extends PageInfo{
    private Integer id;

    private Integer star;

    private String actor;

    private String name;

    private String film;

    private String type;

    private String tag;

    private String require;

    private Integer actorNum;

    private Integer isGood;

    private Integer tAction;

    private Integer tDrama;

    private Integer tMystery;

    private Integer tRomance;

    private Integer tHorror;

    private Integer tComedy;

    private Integer tCartoon;

    private Integer tMusic;

    private String actor1;

    private String actor2;

    private String actor3;

    private String actor4;

    private String actor5;

    private String actor6;

}