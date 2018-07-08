package com.cf.taurus.base.dbrouter.interceptor;

import com.cf.taurus.base.dbrouter.DatabaseRoute;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;

import java.util.Stack;

/**
 * TransactionalInterceptor
 *
 * @author 于文硕
 * @since 2018/5/11 17:44
 */
@Aspect
public class TransactionalInterceptor  implements Ordered {
    private int order;
    protected static final ThreadLocal<Stack<TransactionalNode>> _stack = new ThreadLocal() {
        protected Stack<TransactionalNode> initialValue() {
            return new Stack();
        }
    };

    public TransactionalInterceptor(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    @Around("@annotation(transactional)")
    public static Object pointTransactional(ProceedingJoinPoint point, Transactional transactional) throws Throwable {
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        Stack stack = (Stack) _stack.get();
        DatabaseRoute._trans.set(true);
        TransactionalNode node = new TransactionalNode(method, true);

        Object var5;
        try {
            stack.push(node);
            var5 = point.proceed();
        } finally {
            node = (TransactionalNode) stack.pop();
            if (stack.empty()) {
                DatabaseRoute._trans.set(false);
            } else {
                node = (TransactionalNode) stack.lastElement();
                if (!node.isTransactional()) {
                    DatabaseRoute._trans.set(false);
                }
            }

        }

        return var5;

    }
}
