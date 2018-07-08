package com.cf.taurus.base.dbrouter.interceptor;

import lombok.Getter;

/**
 * SplitNode
 *
 * @author 于文硕
 * @since 2018/5/11 17:37
 */
@Getter
public class SplitNode {
    private String method;
    private Integer splitKey;

    public SplitNode(String method, Integer splitKey) {
        this.method = method;
        this.splitKey = splitKey;
    }
}
