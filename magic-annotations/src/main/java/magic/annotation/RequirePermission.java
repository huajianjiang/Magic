package magic.annotation;

import android.support.annotation.IntRange;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequirePermission {
    /**
     *
     */
    @SuppressWarnings("PointlessBitwiseExpression")
    int ALL = 0 << 15;
    /**
     *
     */
    int ANY = 1 << 15;

    /**
     * @return
     */
    String[] value() default {};

    /**
     * @return
     */
    @IntRange(from = 0, to = (1 << 15) - 1)
    int requestCode() default 0;

    /**
     * @return
     */
    boolean explain() default true;

    /**
     * @return
     * @see #ALL
     * @see #ANY
     */
    int limit() default ALL;

}
