package magic.annotation;

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
     * @return
     */
    String[] value() default {};

    /**
     * @return
     */
    int requestCode() default 0;

    /**
     * @return
     */
    boolean explain() default true;

    /**
     * @return
     */
    Limit limit() default Limit.ALL;

    /**
     *
     */
    enum Limit {
        /**
         *
         */
        ANY,

        /**
         *
         */
        ALL,
    }

}
