package com.cf.taurus.base.dbrouter.interceptor;

import com.cf.taurus.base.dbrouter.DatabaseRoute;
import com.cf.taurus.base.dbrouter.annotation.Split;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

import java.util.Objects;
import java.util.Stack;

/**
 * SplitInterceptor
 *
 * @author 于文硕
 * @since 2018/5/11 17:35
 */
@Slf4j
@Aspect
public class SplitInterceptor implements Ordered {
    private int order;
    protected static final ThreadLocal<Stack<SplitNode>> _stack = new ThreadLocal() {
        protected Stack<SplitNode> initialValue() {
            return new Stack();
        }
    };

    public SplitInterceptor(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    @Around("@annotation(split)")
    public static Object split(ProceedingJoinPoint point, Split split) throws Throwable {
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        Stack stack = (Stack)_stack.get();
        if (((Boolean) DatabaseRoute._trans.get()).booleanValue() && stack.isEmpty()) {
            throw new IllegalArgumentException(String.format("@Split must be in same or before @Transactional. ", method));
        } else {
            Integer key = null;
            if (null != split.value() && split.value().length() > 0) {
                key = DatabaseRoute.doSingleRoute(split.value());
                log.debug("dbrouter key1:{}", key);
            } else {
                key = DatabaseRoute.doCluterRoute(point.getArgs());
                log.debug("dbrouter key2:{},args:{}", key, point.getArgs());
            }

            if (!((Boolean) DatabaseRoute._trans.get()).booleanValue()) {
                DatabaseRoute._select.set(key);
            } else if (!Objects.equals(key, DatabaseRoute._select.get())) {
                throw new IllegalArgumentException(String.format("@Split transmit can not change. [%s] Call [%s]", ((SplitNode)((Stack)_stack.get()).lastElement()).getMethod(), method));
            }

            SplitNode node = new SplitNode(method, key);

            Object var6;
            try {
                stack.push(node);
                var6 = point.proceed();
            } finally {
                node = (SplitNode)stack.pop();
                if (stack.empty()) {
                    DatabaseRoute._select.remove();
                } else if (!((Boolean) DatabaseRoute._trans.get()).booleanValue()) {
                    node = (SplitNode)stack.lastElement();
                    DatabaseRoute._select.set(node.getSplitKey());
                }

            }

            return var6;
        }
    }
}
