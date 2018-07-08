package com.cf.taurus.common.message;

import lombok.Getter;

/**
 * UserMessage
 *
 * @author 于文硕
 * @since 2018/5/15 13:37
 */

@Getter
public enum UserMessage implements ResponseMessageInterface{
    NO_USER(502,"未查询到有效用户"),
    PASSWORD_INVALID(502, "密码错误"),
    GET_OPENID_FAILED(502, "微信服务调用失败"),
    ;

    private int code;
    private String message;

    UserMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
