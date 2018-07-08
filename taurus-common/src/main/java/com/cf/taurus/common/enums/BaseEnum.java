package com.cf.taurus.common.enums;

public interface BaseEnum {

    /**
     * 该枚举的name
     *
     * @return
     */
    String name();

    /**
     * 获取枚举的code
     *
     * @return
     */
    Byte getCode();

    /**
     * 获取枚举的描述
     *
     * @return
     */
    String getDescription();
}
