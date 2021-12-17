package com.hero.simplepermissionsdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.tbruyelle.rxpermissions3.Permission;
import com.tbruyelle.rxpermissions3.RxPermissions;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * <pre>
 * 权限工具类
 * </pre>
 * Author by sun, Email 1910713921@qq.com, Date on 2021/11/1
 * <p>
 * RxPermissions使用注意事项：
 * 1、使用RxPermissions的FragmentActivity或者Fragment的onRequestPermissionsResult方法不能删除super方法，否则收不到回调
 * 2、注意gradle依赖版本对应rxjava的版本，即RxPermissions3对应rxjava3，2则对应2
 */
public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    //权限申请自定义码    调用系统申请权限响应吗
    public static final int GET_PERMISSION_REQUEST = 6600;
    //权限申请自定义码   调用跳转设置  允许权限
    public static final int GOTO_PERMISSION_REQUEST = 6601;

    /**
     * 检查是否有权限
     *
     * @param ctx
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context ctx, String[] permissions) {
        if (ctx == null) {
            return false;
        }

        if (permissions == null || permissions.length == 0) {
            return true;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(ctx, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     * 仅适用于已经申请过一次权限,如在启动时申请过权限，以后触发权限申请则可以调用此方法
     * 选择第一次请求或者禁止并勾选禁止后不再询问 则弹出系统权限设置
     *
     * @param activity
     * @param permissions
     * @return 是否已经有权限   true 有权限
     */
    public static boolean requestPermissionShowDialog(final Activity activity, String[] permissions, int requestCode, int gotoRequestCode) {
        if (activity == null) {
            return false;
        }
        boolean hasPermissions = hasPermissions(activity, permissions);
        if (hasPermissions) {
            return true;
        }
        boolean shouldShow = shouldShowRequestPermissionRationale(activity, permissions);
        if (shouldShow) {
            requestPermission(activity, permissions, requestCode);
        } else {
            String permissionNames = getPermissionNames(permissions);
            //选择禁止并勾选禁止后不再询问
            String authorizationTitle = "授权";
            String authorizationDesc = "需要允许以下授权才可使用:\n" + permissionNames;
            String authorizationBtn = "前往设置授权";

            showPermissionDialog(activity, authorizationTitle, authorizationDesc,
                    authorizationBtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            //调起应用设置页面
                            activity.startActivityForResult(intent, gotoRequestCode);
                        }
                    });
        }
        return false;
    }

    /**
     * 获取权限名称  换行排列
     *
     * @param permissions
     * @return
     */
    public static String getPermissionNames(String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return "";
        }

        String pNames = "";
        for (int i = 0; i < permissions.length; i++) {
            String pn = PermissionNameEnum.getPermissionName(permissions[i]);
            if (!pNames.contains(pn)) {
                pNames = pNames + pn + "；";
                if (i != permissions.length - 1) {
                    pNames = pNames + "\n";
                }
            }
        }
        return pNames;
    }

    /**
     * 请求权限
     * 仅适用于已经申请过一次权限
     * 选择第一次请求或者禁止并勾选禁止后不再询问 则弹出系统权限设置
     *
     * @param activity
     * @param permissions
     * @return 是否已经有权限
     */
    public static boolean requestPermissionShowDialog(final Activity activity, String[] permissions) {
        return requestPermissionShowDialog(activity, permissions, GET_PERMISSION_REQUEST, GOTO_PERMISSION_REQUEST);
    }

    /**
     * 方法判断该权限是否能够弹出带有请求权限的弹窗
     * 这个是判断权限是否处于不再询问状态的重要方法。
     *
     * @param ctx
     * @param permissions
     * @return true  能弹出
     */
    public static boolean shouldShowRequestPermissionRationale(final Activity ctx, String[] permissions) {
        if (ctx == null) {
            return false;
        }

        if (permissions == null || permissions.length == 0) {
            return false;
        }
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (!TextUtils.isEmpty(permission)) {
                boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(ctx, permission);
                //有一个权限不能弹出时，返回false
                if (!shouldShow) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 申请权限
     *
     * @param ctx
     * @param permissions
     */
    public static void requestPermission(final Activity ctx, String[] permissions, int requestCode) {
        if (ctx == null || permissions == null || permissions.length == 0) {
            return;
        }
        //申请权限
        ActivityCompat.requestPermissions(ctx, permissions, requestCode);
    }

    /**
     * 检查是否有权限 无权限则申请
     *
     * @param ctx
     * @param permissions
     * @param requestCode
     * @return true：有权限；   false：无权限
     */
    public static boolean requestPermissionAndCheck(final Activity ctx, String[] permissions, int requestCode) {
        if (hasPermissions(ctx, permissions)) {
            return true;
        }
        requestPermission(ctx, permissions, requestCode);
        return false;
    }

    /**
     * 检查是否有权限 无权限则申请
     *
     * @param ctx
     * @param permissions
     * @return true：有权限；   false：无权限
     */
    public static boolean requestPermissionAndCheck(final Activity ctx, String[] permissions) {
        return requestPermissionAndCheck(ctx, permissions, GET_PERMISSION_REQUEST);
    }

    /**
     * 申请权限
     *
     * @param ctx
     * @param permissions
     */
    public static void requestPermission(final Activity ctx, String[] permissions) {
        //申请权限
        requestPermission(ctx, permissions, GET_PERMISSION_REQUEST);
    }

    /**
     * Activity权限请求回调 处理  requestCode在外部判断
     * 回调中的处理：第一次申请弹出：“允许、禁止”，点击“禁止”：不处理；
     * 第一次申请弹出，点击框外，弹出框消失：弹出提示
     * 再次申请弹出：“允许、禁止且不再询问”，点击禁止不再询问：弹出提示；
     * 无法弹出框时：弹出提示
     *
     * @param activity
     * @param permissions
     * @param grantResults shouldShowRequestPermissionRationale 方法 未点击禁止时返回false；点击禁止时返回true；点击禁止并不再询问时返回false
     *                     因此无法判断是否是第一次弹出权限申请框
     * @return true 有权限  false 无权限
     */
    public static boolean requestPermissionsResult(Activity activity, String[] permissions, int[] grantResults, boolean isToastTip) {
        if (activity == null) {
            return false;
        }

        if (grantResults == null) {
            return false;
        }

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //处理允许权限后的操作
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "处理允许权限后的操作");
                }
                continue;
            }
            String permission = permissions[i];
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (shouldShow) {
                //禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                //处理用户点击禁止后的操作
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示\n" + "处理用户点击禁止后的操作");
                }
                return false;
            } else {
                //场景一：选择“禁止并不再询问”；场景二：用户点击系统申请权限弹出框外部，使对话框消失；场景三：再此之前已经点击过"禁止并不再询问",调用申请权限则直接回调到此处。
                //Toast提示用户前往设置允许权限
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "场景一：选择“禁止并不再询问”；场景二：用户点击系统申请权限弹出框外部，使对话框消失；场景三：再此之前已经点击过\"禁止并不再询问\",调用申请权限则直接回调到此处。\n"
                            + "Toast提示用户前往设置允许权限");
                }
                if (isToastTip) {
                    String authorizationStr = "请授权";
                    String permissionStr = "权限";

                    String permissionName = PermissionNameEnum.getPermissionName(permission);
                    Toast.makeText(activity, authorizationStr + permissionName + permissionStr, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Activity权限请求回调 处理  requestCode在外部判断
     * <p>
     * 回调中的处理：第一次申请弹出：“允许、禁止”，点击“禁止”：不处理；
     * 第一次申请弹出，点击框外，弹出框消失：弹出提示
     * 再次申请弹出：“允许、禁止且不再询问”，点击禁止不再询问：弹出提示；
     * 无法弹出框时：弹出提示
     *
     * @param activity
     * @param permissions
     * @param grantResults shouldShowRequestPermissionRationale 方法 未点击禁止时返回false；点击禁止时返回true；点击禁止并不再询问时返回false
     *                     因此无法判断是否是第一次弹出权限申请框
     */
    public static boolean requestPermissionsResult(Activity activity, String[] permissions, int[] grantResults) {
        return requestPermissionsResult(activity, permissions, grantResults, true);
    }

    /**
     * 用于Activity方法 onRequestPermissionsResult 回调中判断是否申请所有权限都通过了
     *
     * @param grantResults
     * @return
     */
    public static boolean hasPermissionGrantResults(int[] grantResults) {
        if (grantResults == null) {
            return false;
        }

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * 打开dialog
     *
     * @param act
     * @param title
     * @param msg
     * @param onClickListener 用户点击确定
     */
    public static AlertDialog showPermissionDialog(Activity act, String title, String msg, String btn,
                                                   DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn, onClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * 申请权限   简单回调
     * 多个权限，则是所有权限都通过才回调true
     *
     * @param activity
     * @param permissionStr
     * @param callback
     */
    public static void requestPermissionSimpleCallback(FragmentActivity activity, OnPermissionsSimpleCallback callback, String... permissionStr) {
        if (callback == null) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionCallback: callback是空");
            }
            return;
        }
        if (activity == null || permissionStr == null || permissionStr.length == 0) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionCallback: activity == null || TextUtils.isEmpty(permission)");
            }
            callback.refuse();
            return;
        }


        RxPermissions permissions = new RxPermissions(activity);
        permissions.setLogging(true);
        permissions.request(permissionStr).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "requestPermissionCallback: accept: " + aBoolean);
                }

                if (callback == null) {
                    return;
                }

                if (aBoolean != null && aBoolean) {
                    callback.allow();
                } else {
                    callback.refuse();
                }
            }
        });
    }

    /**
     * 单个权限申请   区分三种场景回调
     * RxPermissions.requestEach 方法参数为String... ，因此封装方法中参数permissionStr可根据需求自行修改为 String[]或String...
     * 多个权限则通过方法 permission.name.equalsIgnoreCase() 来区分
     *
     * @param activity
     * @param permissionStr
     * @param callback
     */
    public static void requestPermissionEachCallBack(
            FragmentActivity activity, String permissionStr, OnPermissionsEachCallback callback) {
        if (callback == null) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionEachCallBack: callback是空");
            }
            return;
        }
        if (activity == null || TextUtils.isEmpty(permissionStr)) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionEachCallBack: activity == null || TextUtils.isEmpty(permissionStr)");
            }
            callback.refuse();
            return;
        }
        RxPermissions permissions = new RxPermissions(activity);
        permissions.setLogging(true);
        permissions.requestEach(permissionStr)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (callback == null) {
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "requestPermissionEachCallBack: callback是空");
                            }
                            return;
                        }

                        if (!permission.name.equalsIgnoreCase(permissionStr)) {
                            return;
                        }

                        if (permission.granted) {
                            //处理允许权限后的操作
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "处理允许权限后的操作");
                            }
                            callback.allow();
                            return;
                        }
                        if (permission.shouldShowRequestPermissionRationale) {
                            //禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                            //处理用户点击禁止后的操作
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示\n" + "处理用户点击禁止后的操作");
                            }
                            callback.refuse();
                            return;
                        }

                        //场景一：选择“禁止并不再询问”；
                        //场景二：用户点击系统申请权限弹出框外部，使对话框消失；
                        //场景三：再此之前已经点击过"禁止并不再询问",调用申请权限则直接回调到此处。
                        //Toast提示用户前往设置允许权限
                        if (BuildConfig.DEBUG) {
                            Log.i(TAG, "场景一：选择“禁止并不再询问”；场景二：用户点击系统申请权限弹出框外部，使对话框消失；场景三：再此之前已经点击过\"禁止并不再询问\",调用申请权限则直接回调到此处。\n"
                                    + "Toast提示用户前往设置允许权限");
                        }
                        callback.refuseAndNotAskAgain();
                    }
                });
    }

    /**
     * 多个权限申请   区分三种场景回调
     * 多个权限则通过方法 permission.name.equalsIgnoreCase() 来区分
     * 相关参照requestPermissionEachCallBack()方法判断
     * @param activity
     * @param permissionStr
     * @param callback
     */
    public static void requestPermissionsEachCallBack(
            FragmentActivity activity, Consumer<Permission> callback, String... permissionStr) {
        if (callback == null || activity == null || permissionStr == null || permissionStr.length == 0) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionsEachCallBack: callback == null || activity == null || permissionStr == null || permissionStr.length == 0");
            }
            return;
        }

        RxPermissions permissions = new RxPermissions(activity);
        permissions.setLogging(true);
        permissions.requestEach(permissionStr)
                .subscribe(callback);
    }

    /**
     * 申请权限 多个权限申请 合并结果处理 区分三种场景回调
     *
     * @param activity
     * @param permissionStr
     * @param callback
     */
    public static void requestPermissionEachCombinedCallBack(
            FragmentActivity activity, OnPermissionsEachCallback callback, String... permissionStr) {
        if (callback == null) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionEachCallBack: callback是空");
            }
            return;
        }
        if (activity == null || permissionStr == null || permissionStr.length == 0) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "requestPermissionEachCallBack: activity == null || TextUtils.isEmpty(permissionStr)");
            }
            callback.refuse();
            return;
        }
        RxPermissions permissions = new RxPermissions(activity);
        permissions.setLogging(true);
        permissions.requestEachCombined(permissionStr)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (callback == null) {
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "requestPermissionEachCallBack: callback是空");
                            }
                            return;
                        }

                        if (permission.granted) {
                            //处理允许权限后的操作
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "处理允许权限后的操作");
                            }
                            callback.allow();
                            return;
                        }
                        if (permission.shouldShowRequestPermissionRationale) {
                            //禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                            //处理用户点击禁止后的操作
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示\n" + "处理用户点击禁止后的操作");
                            }
                            callback.refuse();
                            return;
                        }

                        //场景一：选择“禁止并不再询问”；
                        //场景二：用户点击系统申请权限弹出框外部，使对话框消失；
                        //场景三：再此之前已经点击过"禁止并不再询问",调用申请权限则直接回调到此处。
                        //Toast提示用户前往设置允许权限
                        if (BuildConfig.DEBUG) {
                            Log.i(TAG, "场景一：选择“禁止并不再询问”；场景二：用户点击系统申请权限弹出框外部，使对话框消失；场景三：再此之前已经点击过\"禁止并不再询问\",调用申请权限则直接回调到此处。\n"
                                    + "Toast提示用户前往设置允许权限");
                        }
                        callback.refuseAndNotAskAgain();
                    }
                });
    }

    /**
     * 申请权限 简单返回
     */
    interface OnPermissionsSimpleCallback {
        /**
         * 允许
         */
        void allow();

        /**
         * 拒绝
         */
        void refuse();
    }

    /**
     * 申请权限 详细场景返回
     */
    interface OnPermissionsEachCallback {
        /**
         * 允许
         */
        void allow();

        /**
         * 拒绝
         */
        void refuse();

        /**
         * 拒绝并不再提示
         */
        void refuseAndNotAskAgain();
    }
}
