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
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface Prefs {
    /**
     * 绑定指定的 preferences key 到对应的 preferences
     */
    String value();

    /**
     * 可选的 SharedPreferences 文件名称，默认存储在默认的 packageName_preferences 文件中
     */
    String name() default "";

    /**
     * 可选的 SharedPreferences 文件创建模式，默认值为 {@code Context.MODE_PRIVATE}
     */
    int mode() default 0;

}
