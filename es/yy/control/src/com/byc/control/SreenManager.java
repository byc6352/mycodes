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
	/*屏蔽系统的屏保<uses-permission   android:name="android.permission.DISABLE_KEYGUARD"/>*/
	@SuppressWarnings("deprecation")
	public static void disableKeyguard(Context context){
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

		KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("KeyguardLock");

		keyguardLock.disableKeyguard();
	}
	 /**开启 保持屏幕唤醒<uses-permission android:name="android.permission.WAKE_LOCK"/>*/  
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
	 /**关闭 保持屏幕唤醒*/  
    public static void releaseWakeLock() {         
        if (wakeLock != null) {  
            wakeLock.release();  
            wakeLock = null;  
        } 
    }
    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }
    public static void LockReceiver(Context context){
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
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
