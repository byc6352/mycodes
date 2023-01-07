/**
 * 
 */
package com.byc.smslocated;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

/**
 * @author byc
 *
 */
/**
 * �̳���Activity��ʵ��Android6.0������ʱȨ�޼��
 * ��Ҫ��������ʱȨ�޼���Activity���Լ̳������
 * 
 * @����ʱ�䣺2016��5��27�� ����3:01:31 
 * @��Ŀ���ƣ� AMapLocationDemo
 * @author hongming.wang
 * @�ļ����ƣ�PermissionsChecker.java
 * @�������ƣ�PermissionsChecker
 * @since 2.5.0
 */
public class CheckPermissionsActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * ��Ҫ���м���Ȩ������
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
            };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * �ж��Ƿ���Ҫ��⣬��ֹ��ͣ�ĵ���
     */
    private boolean isNeedCheck = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedCheck){
            //checkPermissions(needPermissions);
        }
    }

    /**
     * 
     * @param needRequestPermissonList
     * @since 2.5.0
     * requestPermissions����������ĳһȨ�ޣ�
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * ��ȡȨ�޼�����Ҫ����Ȩ�޵��б�
     * 
     * @param permissions
     * @return
     * @since 2.5.0
     * checkSelfPermission�������������ж��Ƿ�app�Ѿ���ȡ��ĳһ��Ȩ��
     * shouldShowRequestPermissionRationale���������ж��Ƿ�
     * ��ʾ����Ȩ�޶Ի������ͬ���˻��߲���ѯ���򷵻�false
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, perm)) {
                    needRequestPermissonList.add(perm);
                } 
            }
        }
        return needRequestPermissonList;
    }

    /**
     * ����Ƿ����е�Ȩ�޶��Ѿ���Ȩ
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
    * ����Ȩ�޽���Ļص�����
    */
    @Override
    public void onRequestPermissionsResult(int requestCode,
            String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * ��ʾ��ʾ��Ϣ
     * 
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��ʾ");
        builder.setMessage("��ǰӦ��ȱ�ٶ��Ż�λȨ�ޡ�����\"����\"-\"Ȩ��\"-������Ȩ�ޡ�");

        // �ܾ�, �˳�Ӧ��
        builder.setNegativeButton("ȡ��",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("����",
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
     *  ����Ӧ�õ�����
     * 
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
