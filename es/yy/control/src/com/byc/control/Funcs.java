/**
 * 
 */
package com.byc.control;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * @author byc
 *
 */
public class Funcs {
	 public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    /*
     * 字符串转换为字节数组；
     */
    public static byte[] StrToBytes(String s){
    	try{
    		byte[] b=s.getBytes("gbk");
    		return b;
    	}catch(UnsupportedEncodingException e){
    		e.printStackTrace();
    		return null;
    	}
    }
    /** 
     * 判断服务是否开启 
     *  
     * @return 
     */  
    public static boolean isServiceRunning(Context context, String ServiceName) {  
        if (("").equals(ServiceName) || ServiceName == null)  
            return false;  
        ActivityManager myManager = (ActivityManager) context  
                .getSystemService(Context.ACTIVITY_SERVICE);  
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager  
                .getRunningServices(30);  
        for (int i = 0; i < runningService.size(); i++) {  
            if (runningService.get(i).service.getClassName().toString()  
                    .equals(ServiceName)) {  
                return true;  
            }  
        }  
        return false;  
    }  
	//获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行 
	 public static void acquireWakeLock(Context context,WakeLock wakeLock,String tag) 
	 { 
	 if (null == wakeLock) 
	 { 
	  PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE); 
	  wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, tag); 
	  if (null != wakeLock) 
	  { 
	  wakeLock.acquire(); 
	  } 
	 } 
	 } 
	 //释放设备电源锁 
	 public static void releaseWakeLock(WakeLock wakeLock) 
	 { 
	 if (null != wakeLock&& wakeLock.isHeld()) 
	 { 
	  wakeLock.release(); 
	  wakeLock = null; 
	 } 
	 } 
	 /*屏蔽系统的屏保<uses-permission   android:name="android.permission.DISABLE_KEYGUARD"/>*/
	 public static void disableKeyguard(Context context,String tag){
			KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

			KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(tag);

			keyguardLock.disableKeyguard();
	}
	//高位在前，低位在后
	 public static byte[] int2bytes(int num){
	 byte[] result = new byte[4];
	 result[0] = (byte)((num >>> 24) & 0xff);//说明一
	 result[1] = (byte)((num >>> 16)& 0xff );
	 result[2] = (byte)((num >>> 8) & 0xff );
	 result[3] = (byte)((num >>> 0) & 0xff );
	 return result;
	 }
	  
	 //高位在前，低位在后
	 public static int bytes2int(byte[] bytes){
	 int result = 0;
	 if(bytes.length == 4){
	  int a = (bytes[0] & 0xff) << 24;//说明二
	  int b = (bytes[1] & 0xff) << 16;
	  int c = (bytes[2] & 0xff) << 8;
	  int d = (bytes[3] & 0xff);
	  result = a | b | c | d;
	 }
	 return result;
	 }
	    /**
	     * 将时间戳转为时间字符串
	     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
	     *
	     * @param milliseconds 毫秒时间戳
	     * @return 时间字符串
	     */
	    public static String milliseconds2String(long milliseconds) {
	        return milliseconds2String(milliseconds, DEFAULT_SDF);
	    }

	    /**
	     * 将时间戳转为时间字符串
	     * <p>格式为用户自定义</p>
	     *
	     * @param milliseconds 毫秒时间戳
	     * @param format       时间格式
	     * @return 时间字符串
	     */
	    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
	        return format.format(new Date(milliseconds));
	    }
	    /**
	     * 去除字符串右边的0
	     * @return 新字符串
	     */
	    public static String trimR(String str) {
	        int i=str.indexOf(0);
	        String subStr=str;
	        if(i>0)
	        	subStr=str.substring(0,i);
	        return subStr;
	    }
	    /**
	     *判断当前应用程序处于前台还是后台<uses-permission android:name="android.permission.GET_TASKS" /> 
	     */
	    public static boolean isApplicationBroughtToBackground(final Context context) {
	        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	        if (!tasks.isEmpty()) {
	            ComponentName topActivity = tasks.get(0).topActivity;
	            if (!topActivity.getPackageName().equals(context.getPackageName())) {
	                return true;
	            }
	        }
	        return false;
	    }
}
