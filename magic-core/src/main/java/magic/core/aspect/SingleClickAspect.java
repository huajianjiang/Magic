package magic.core.aspect;

import magic.core.util.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import magic.annotation.SingleClick;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Aspect
public class SingleClickAspect {
    private long mStartTime = 0;

    private SingleClickAspect() {
    }

    @Pointcut("execution(@magic.annotation.SingleClick * *(..)) && @annotation(metaData)")
    public void method(SingleClick metaData) {
    }

    @Around("method(metaData)")
    public Object singleClickAdvice(ProceedingJoinPoint joinPoint, SingleClick metaData)
            throws Throwable
    {
        final int delayTime = metaData.value();
        boolean intercept = false;

        if (mStartTime == 0) {
            mStartTime = System.currentTimeMillis();
        } else {
            long currTime = System.currentTimeMillis();
            long diffTime = currTime - mStartTime;
            if (diffTime <= delayTime) {
                intercept = true;
            } else {
                mStartTime = System.currentTimeMillis();
            }
            Logger.e("SingleClickAspect", "diffTime=" + diffTime);
        }

        Logger.e("SingleClickAspect", "mStartTime=" + mStartTime);

        if (intercept) {
            return null;
        }

        return joinPoint.proceed();
    }

}
