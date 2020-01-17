package com.aspect;

import com.anno.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class CacheAspect extends BaseCacheAspect {

    private final static Logger logger = LoggerFactory.getLogger(CacheAspect.class);

    public CacheAspect(RedissonClient template) {
        super(template);
    }

    @Pointcut("execution( * com.web..*.*(..)))")
    public void loginAspect() {
    }

    @Around("loginAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
        boolean flag = realMethod.isAnnotationPresent(Cache.class);
        if (flag) {
            // 记录下请求内容
            Cache cache = realMethod.getAnnotation(Cache.class);
            String key = getKey(joinPoint);
            if (cache.open()) {
                Object value = super.get(key);
                if (null != value) {
                    logger.info("key = {}, value 不为空",key);
                    return value;
                }
            }
            Object obj = joinPoint.proceed();
            super.set(key, obj, cache.expire());
            return obj;
        }
        return joinPoint.proceed();
    }

    private String getKey(ProceedingJoinPoint joinPoint) {
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入方法的对象
        Method method = signature.getMethod();
        //获取方法上的Aop注解
        Cache annotation = method.getAnnotation(Cache.class);
        //获取注解上的值如 : @MyAnnotation(key = "'param id is ' + #id")
        String keyEl = annotation.key();
        //将注解的值中的El表达式部分进行替换
        //创建解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        //获取表达式
        Expression expression = parser.parseExpression(keyEl);
        //设置解析上下文(有哪些占位符，以及每种占位符的值)
        EvaluationContext context = new StandardEvaluationContext();
        //获取参数值
        Object[] args = joinPoint.getArgs();
        //获取运行时参数的名称
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i].toString());
        }
        //解析,获取替换后的结果
        return expression.getValue(context).toString();
    }
}
