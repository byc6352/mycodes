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
     * ��ʾ��λ����δ����ȷ�϶Ի���
     */
    public static void showLocServiceDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("�ֻ�δ����λ�÷���")
                .setMessage("���� ����-λ����Ϣ (��λ�÷����))")
                .setNegativeButton("ȡ��", null)
                .setPositiveButton("ȥ����", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        MainActivity mAct=(MainActivity)context;
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            //context.startActivity(intent);                          
                            mAct.startActivityForResult(intent,0); //��Ϊ������ɺ󷵻ص���ȡ����
                        } catch (ActivityNotFoundException ex) {
                            intent.setAction(Settings.ACTION_SETTINGS);
                            try {
                                //context.startActivity(intent);
                            	mAct.startActivityForResult(intent,0); //��Ϊ������ɺ󷵻ص���ȡ����
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .show();
    }

    /**
     * ��ʾ��λȨ�ޱ��ܾ��Ի���
     */
    public static void showLocIgnoredDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("�ֻ��ѹر�λ��Ȩ��");
        builder.setMessage("���� ����-Ӧ��Ȩ�� (��λ��Ȩ�޴�))");

        //�����·�button����¼�
        builder.setPositiveButton("ȥ����", new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //���öԻ����ǿ�ȡ����
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
