/**
 * 
 */
package com.byc.smslocated;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * @author byc
 *
 */
public class DlgUtils {
	/**
     * 显示定位服务未开启确认对话框
     */
    public static void showLocServiceDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("手机未开启位置服务")
                .setMessage("请在 设置-位置信息 (将位置服务打开))")
                .setNegativeButton("取消", null)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        MainActivity mAct=(MainActivity)context;
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            //context.startActivity(intent);                          
                            mAct.startActivityForResult(intent,0); //此为设置完成后返回到获取界面
                        } catch (ActivityNotFoundException ex) {
                            intent.setAction(Settings.ACTION_SETTINGS);
                            try {
                                //context.startActivity(intent);
                            	mAct.startActivityForResult(intent,0); //此为设置完成后返回到获取界面
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .show();
    }

    /**
     * 显示定位权限被拒绝对话框
     */
    public static void showLocIgnoredDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("手机已关闭位置权限");
        builder.setMessage("请在 设置-应用权限 (将位置权限打开))");

        //监听下方button点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
                context.startActivity(localIntent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
