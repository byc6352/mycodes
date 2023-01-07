/**
 * 
 */
package com.byc.smslocated;

import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;


/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 * 
 * @创建时间：2016年5月27日 下午3:01:31 
 * @项目名称： AMapLocationDemo
 * @author hongming.wang
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 * @since 2.5.0
 */
public class CheckPermissions  implements OnRequestPermissionsResultCallback{
	public static String TAG = "byc001";//调试标识：
	private Context context;
	private Activity activity;
	private static CheckPermissions current;
	 /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
            };
    private static final int PERMISSON_REQUESTCODE = 0;
    private CheckPermissions(Context context) {
        this.context = context;
        this.activity=(Activity)context;
        TAG=Config.TAG;
        if (Build.VERSION.SDK_INT >= 23) {
        	checkPermissions(needPermissions);
        }

    }
    public static synchronized CheckPermissions getCheckPermissions(Context context) {
        if(current == null) {
            current = new CheckPermissions(context);
        }
        return current;
    }
    /**
     * 
     * @param needRequestPermissonList
     * @since 2.5.0
     * requestPermissions方法是请求某一权限，
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(activity,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }
    /**
     * 获取权限集中需要申请权限的列表
     * 
     * @param permissions
     * @return
     * @since 2.5.0
     * checkSelfPermission方法是在用来判断是否app已经获取到某一个权限
     * shouldShowRequestPermissionRationale方法用来判断是否
     * 显示申请权限对话框，如果同意了或者不在询问则返回false
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                		activity, perm)) {
                    needRequestPermissonList.add(perm);
                } 
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
    * 申请权限结果的回调方法
    */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                //isNeedCheck = false;
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
       // super.onRequestPermissionsResult(requestCode,permissions, paramArrayOfInt);
    }

    /**
     * 显示提示信息
     * 
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     *  启动应用的设置
     * 
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

}
