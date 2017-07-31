package magic.core.aspect.permission;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class ResponsePermsAspect {

    private ResponsePermsAspect() {
    }

    @Pointcut("execution(public void onRequestPermissionsResult(int,String[],int[]))")
    public void method() {
    }

    @After("method()")
    public void onResponse(JoinPoint joinPoint) throws Throwable {
        PermissionHandler.onResponse(joinPoint);
    }

}

