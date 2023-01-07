/**
 * 
 */
package com.byc.control;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class SreenManager {
	private static WakeLock wakeLock; 
	/*����ϵͳ������<uses-permission   android:name="android.permission.DISABLE_KEYGUARD"/>*/
	@SuppressWarnings("deprecation")
	public static void disableKeyguard(Context context){
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

		KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("KeyguardLock");

		keyguardLock.disableKeyguard();
	}
	 /**���� ������Ļ����<uses-permission android:name="android.permission.WAKE_LOCK"/>*/  
    public static void acquireWakeLock(Context context) {  
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
        	releaseWakeLock();
           //PowerManager.ACQUIRE_CAUSES_WAKEUP| PowerManager.SCREEN_DIM_WAKE_LOCK
        	wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "SreenManager");  
        	wakeLock.acquire();  
        }
    }  
	 /**�ر� ������Ļ����*/  
    public static void releaseWakeLock() {         
        if (wakeLock != null) {  
            wakeLock.release();  
            wakeLock = null;  
        } 
    }
    /**
     * �����ֻ���Ļ������
     */
    public static void wakeUpAndUnlock(Context context) {
        // ��ȡ��Դ����������
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // ��ȡPowerManager.WakeLock����,����Ĳ���|��ʾͬʱ��������ֵ,������LogCat���õ�Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // ������Ļ
            wl.release(); // �ͷ�
        }
        // ��Ļ����
        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // ��Ļ����
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // ����
    }
    public static void LockReceiver(Context context){
        final IntentFilter filter = new IntentFilter();
        // ��Ļ�����㲥
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // ��Ļ�����㲥
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // ��Ļ�����㲥
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // ��������Դ���������ػ����Ի���������ʱϵͳ�ᷢ������㲥
        // example����ʱ����õ�ϵͳ�Ի���Ȩ�޿��ܸܺߣ��Ḳ��������������ߡ��ػ����Ի���֮�ϣ�
        // ���Լ�������㲥�����յ�ʱ�������Լ��ĶԻ�������pad���½ǲ��ֵ����ĶԻ���
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                	Log.d(Config.TAG, "onReceive");
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    //if(mLockState==LockState.locked)ShowWindow();
                    Log.d(Config.TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
        	    	SreenManager.disableKeyguard(context);
        	    	SreenManager.acquireWakeLock(context);
                    Log.d(Config.TAG, "screen off");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                   // HideWindow();
                   // mLockState=LockState.unLocked;
                    Log.d(Config.TAG, "screen unlock");
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(Config.TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                }
            }
        };
        Log.d(Config.TAG, "registerReceiver");
        context.registerReceiver(mBatInfoReceiver, filter);
    }
}
