package magic.core.module;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/21
 * <br>Email: developer.huajianjiang@gmail.com
 */
public interface LoginModule {
    /**
     * 检查登录回调
     * @return 如果已处于登录状态就返回 true,否则 false
     */
    boolean checkLogin();
}
