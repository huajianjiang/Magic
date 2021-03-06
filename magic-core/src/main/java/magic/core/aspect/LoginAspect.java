package magic.core.aspect;

import magic.core.module.LoginModule;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class LoginAspect {

    private LoginAspect() {
    }

    @Pointcut("execution(@magic.annotation.RequireLogin * *(..))")
    public void method() {
    }

    @Around("method()")
    public Object loginAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean intercept = false;
        Object target = joinPoint.getTarget();

        if (target instanceof LoginModule) {
            LoginModule module = (LoginModule) target;
            intercept = !module.checkLogin();
        }

        if (intercept) {
            return null;
        }

        return joinPoint.proceed();
    }

}
