package magic.core.aspect.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import magic.core.module.RuntimePermissionModule;
import magic.core.util.Logger;
import magic.core.util.Perms;
import magic.core.util.Preconditions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.List;

import magic.annotation.RequirePermission;

import static magic.annotation.RequirePermission.ALL;
import static magic.annotation.RequirePermission.ANY;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/7/19
 * <br>Email: developer.huajianjiang@gmail.com
 */
public final class PermissionHandler {
    private ProceedingJoinPoint mRequestJp;
    private RequirePermission mMetaData;

    PermissionHandler(ProceedingJoinPoint requestJp) {
        this(requestJp, null);
    }

    PermissionHandler(ProceedingJoinPoint requestJp, RequirePermission metaData) {
        mRequestJp = requestJp;
        mMetaData = checkPermMetaData(requestJp, metaData);
    }

    private static RequirePermission checkPermMetaData(ProceedingJoinPoint requestJp,
            RequirePermission metaData)
    {
        if (metaData == null) {
            MethodSignature signature = (MethodSignature) requestJp.getSignature();
            metaData = signature.getMethod().getAnnotation(RequirePermission.class);
        }
        return metaData;
    }

    @Nullable
    private static RuntimePermissionModule getModule(Object target) {
        return RuntimePermissionModule.class.isInstance(target) ? (RuntimePermissionModule) target :
                null;
    }

    Object onRequest() throws Throwable {
        final ProceedingJoinPoint requestJp = mRequestJp;
        final RequirePermission metaData = mMetaData;

        final Object target = requestJp.getTarget();
        final Activity context = Perms.getContext(target);
        final int limit = metaData.limit();
        final String[] perms = metaData.value();

        boolean ok;

        Logger.e("PermissionHandler", "proceedRequest>>>limit===>"+limit);

        switch (limit) {
            case ALL:
                ok = Perms.verifyAllPermissions(context, perms);
                break;
            case ANY:
                ok = Perms.verifyAnyPermissions(context, perms);
                break;
            default:
                return new IllegalArgumentException(
                        "unknown limit , please using RequirePermission.ALL or " +
                        "RequirePermission.ANY");
        }

        if (ok) {
            return requestJp.proceed();
        }

        boolean shouldExplain = metaData.explain();
        if (shouldExplain) {
            List<String> shouldExplainPerms = new ArrayList<>();
            for (String perm : perms) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, perm)) {
                    shouldExplainPerms.add(perm);
                }
            }

            final boolean hasShouldExplainPerms = !Preconditions.isNullOrEmpty(shouldExplainPerms);

            if (hasShouldExplainPerms) {
                RuntimePermissionModule module = getModule(target);
                if (module != null) {
                    module.showRequestPermissionsRationale(
                            shouldExplainPerms.toArray(new String[]{}), PermissionHandler.this);
                }
                return null;
            }
        }

        requestPermissions();

        return null;
    }

    static void onResponse(JoinPoint responseJp) throws Throwable {
        RuntimePermissionModule module = getModule(responseJp.getTarget());
        if (module == null) return;
        
        final Object[] args = responseJp.getArgs();
        final int prc = (int) args[0];
        final String[] perms = (String[]) args[1];
        final int[] grantResults = (int[]) args[2];
        final int requestCode = getRequestCode(prc);
        final int limit = getLimit(prc);

        boolean ok = false;

        Logger.e("PermissionHandler",
                "proceedResponse>>>limit===>" + limit + ",requestCode=" + requestCode);

        switch (limit) {
            case ALL:
                ok = Perms.verifyAllPermissions(grantResults);
                break;
            case ANY:
                ok = Perms.verifyAnyPermissions(grantResults);
                break;
        }

        List<String> grantedTemp = new ArrayList<>();
        List<String> deniedTemp = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            String perm = perms[i];
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedTemp.add(perm);
            } else {
                grantedTemp.add(perm);
            }
        }

        final String[] grantedPerms = grantedTemp.toArray(new String[]{});
        final String[] deniedPerms = deniedTemp.toArray(new String[]{});

        if (ok) {
            module.onRequestPermissionsGranted(requestCode, grantedPerms, deniedPerms);
        } else {
            module.onRequestPermissionsDenied(grantedPerms, deniedPerms);
        }
    }

    /**
     * 请求运行时权限
     */
    public void requestPermissions() {
        final ProceedingJoinPoint requestJp = mRequestJp;
        final RequirePermission metaData = mMetaData;
        final int prc = getPackagedRequestCode(metaData.limit(), metaData.requestCode());

        final Object context = requestJp.getTarget();
        final String[] perms = metaData.value();

        Logger.e("PermissionHandler", "requestPermissions>>>prc=" + prc);

        //检查权限请求所在的上下文环境
        if (context instanceof Activity) {
            // 权限请求所在上下文为 Framework 中的 Activity 或者 supportLibrary 中的 Activity
            // (FragmentActivity/AppCompatActivity)
            ActivityCompat.requestPermissions((Activity) context, perms, prc);
        } else if (context instanceof android.support.v4.app.Fragment) {
            // supportLibrary 中的 Fragment
            ((android.support.v4.app.Fragment) context).requestPermissions(perms, prc);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Framework 中的 Fragment
            ((android.app.Fragment) context).requestPermissions(perms, prc);
        } else {
            throw new RuntimeException("Can not find correct context for permissions request");
        }
    }


    private static final int LIMIT_SHIFT = 15;
    private static final int LIMIT_MASK = 1 << LIMIT_SHIFT;

    private static int getPackagedRequestCode(int limit, int requestCode) {
        return (limit & LIMIT_MASK) | (requestCode & ~LIMIT_MASK);
    }

    private static int getLimit(int packagedRequestCode) {
        return packagedRequestCode & LIMIT_MASK;
    }

    private static int getRequestCode(int packagedRequestCode) {
        return packagedRequestCode & ~LIMIT_MASK;
    }

}
