package com.cf.taurus.common.message;

import lombok.Getter;

/**
 * ResponseMessage
 *
 * @author 于文硕
 * @since 2018/5/11 13:54
 */

@Getter
public enum ResponseMessage implements ResponseMessageInterface {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(501, "参数错误"),
    BUSINESS_ERROR(502, "业务异常"),
    NO_DATA(503,"未查询到有效数据")
    ;

    private int code;
    private String message;

    ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
