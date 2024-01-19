package cn.yiidii.boot.base;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 切面基础类
 * <p>封装一些基础方法</p>
 *
 * @author pangu
 */
public abstract class BaseAspect {

    /**
     * 获取切面方法上包含的指定注解
     */
    public <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Method[] methods = joinPoint.getSignature().getDeclaringType().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                if (m.getParameterTypes().length == arguments.length) {
                    return m.getAnnotation(annotationClass);
                }
            }
        }
        return null;
    }

}
