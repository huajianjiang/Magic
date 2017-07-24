package magic.annotation;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Android 6.0 (+) 运行时权限注解
 * <p>
 *     在一个需要运行时权限检查和处理的方法中加上该注解,表明该方法需要运行时权限才能继续安全地执行该方法
 *
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/17
 * <br>Email: developer.huajianjiang@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequirePermission {
    /**
     * 权限请求模式 : 表明所请求的多个权限必须全部都被用户授予才能继续下面的操作,对单个权限请求无实际意义
     */
    @SuppressWarnings("PointlessBitwiseExpression")
    int ALL = 0 << 15;

    /**
     * 权限请求模式 : 表明所请求的多个权限中的任意一个被用户授予就可继续下面的操作,对单个权限请求无实际意义
     */
    int ANY = 1 << 15;

    /**
     * 客户端所请求的权限列表,既可以是单一的也可以声明多个权限
     * @return 声明的所需请求的权限列表
     */
    String[] value() default {};

    /**
     * 客户端必须声明权限请求码，如果客户端在同一个 java 文件中的多个地方需要请求不同的权限，此时需要指定不同的权限请求码来加以区分
     * <p>
     *     <b>注意: 该请求码必须 >= 0 ，并且取值范围在 低 15 位( 0 <= requestCode <= (1 <<< 15) - 1 )</b>
     *
     * @return 权限请求码，默认值为 0，表明同一个 java 文件中只有一个权限请求
     */
    @IntRange(from = 0, to = (1 << 15) - 1)
    int requestCode() default 0;

    /**
     * 指定该权限请求是否需要检查并处理那些需要向用户友好解释之前被拒的权限的使用原因
     * @return 是否需要检查并向用户展示权限使用解释的友好提示 Ui ，如果第一次权限请求被拒的话，默认值为 {@code true}
     */
    boolean explain() default true;

    /**
     * 设置权限请求限制模式,该值必须为 {@link #ALL} 和 {@link #ANY} 中的一个
     * @return 权限请求限制模式，默认值为 {@link #ALL}
     * @see #ALL
     * @see #ANY
     */
    @Limit
    int limit() default ALL;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ALL, ANY})
    @interface Limit {}

}
