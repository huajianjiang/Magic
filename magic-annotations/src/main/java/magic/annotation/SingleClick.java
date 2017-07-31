package magic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Android Ui 控件点击交互去抖动
 * <p>
 *     该注解可防止 Ui 控件在进行点击交互时在指定的延迟时间内被多次点击造成的问题
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {
    /**
     * 设置延迟的最大时间，在给定时间内过滤掉 Ui 控件的多次点击交互事件，单位 ms，默认值为 500 ms
     */
    int value() default 500;
}
