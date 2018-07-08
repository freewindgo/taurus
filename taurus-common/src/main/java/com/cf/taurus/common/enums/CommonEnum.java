package com.cf.taurus.common.enums;

/**
 * @author 于文硕
 * @ClassName CommonEnum 通用状态枚举
 * @create 2018/3/14 10:18
 */
public enum CommonEnum implements BaseEnum {

    NORMAL((byte) 1, "未删除"),
    DELETED((byte) 2, "已删除"),

    YES((byte) 1, "是"),
    NO((byte) 2, "否"),;

    private Byte code;

    private String description;

    @Override
    public Byte getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    CommonEnum(Byte code, String description) {
        this.code = code;
        this.description = description;
    }
}
