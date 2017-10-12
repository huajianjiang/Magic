# Magic
Android 平台上的 AOP + APT 应用，现处于 Alpha 阶段。

### 已完成的模块：
* @ReqireLogin ：登录拦截 Aspect，处理 App 在进行页面跳转或者网络请求前的用户登录检查，如果未登录，直接后续方法拦截，从而实现预处理登录状态检查，而不是在后期通过其他方式判断处理，比如 OkHttp 的 Intercept 或者更加糟糕的依赖服务器接口请求后的 errorCode 处理；
* @RequirePermission：Android 6.0 运行时权限检查 Aspect，在一个方法需要预先获取用户授权的权限的情况下，可以自动化帮你处理相关权限请求和权限请求的结果；
* @SingleClick : 自动化处理那些需要 UI 控件点击事件去抖动的情况，方式 UI 被用户多次快速点击导致的重复页面问题；
* @Prefs: 对方法的返回值，类的成员变量的访问等进行自动化赋值，同时后续会新增 APT 方式在 compile 期间绑定 SharedPreferences 值；

##### 当然，AOP ，APT 还有更多可能。
