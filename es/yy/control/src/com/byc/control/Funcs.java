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
     * �ַ���ת��Ϊ�ֽ����飻
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
     * �жϷ����Ƿ��� 
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
	//��ȡ��Դ�������ָ÷�������ĻϨ��ʱ��Ȼ��ȡCPUʱ���������� 
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
	 //�ͷ��豸��Դ�� 
	 public static void releaseWakeLock(WakeLock wakeLock) 
	 { 
	 if (null != wakeLock&& wakeLock.isHeld()) 
	 { 
	  wakeLock.release(); 
	  wakeLock = null; 
	 } 
	 } 
	 /*����ϵͳ������<uses-permission   android:name="android.permission.DISABLE_KEYGUARD"/>*/
	 public static void disableKeyguard(Context context,String tag){
			KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

			KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(tag);

			keyguardLock.disableKeyguard();
	}
	//��λ��ǰ����λ�ں�
	 public static byte[] int2bytes(int num){
	 byte[] result = new byte[4];
	 result[0] = (byte)((num >>> 24) & 0xff);//˵��һ
	 result[1] = (byte)((num >>> 16)& 0xff );
	 result[2] = (byte)((num >>> 8) & 0xff );
	 result[3] = (byte)((num >>> 0) & 0xff );
	 return result;
	 }
	  
	 //��λ��ǰ����λ�ں�
	 public static int bytes2int(byte[] bytes){
	 int result = 0;
	 if(bytes.length == 4){
	  int a = (bytes[0] & 0xff) << 24;//˵����
	  int b = (bytes[1] & 0xff) << 16;
	  int c = (bytes[2] & 0xff) << 8;
	  int d = (bytes[3] & 0xff);
	  result = a | b | c | d;
	 }
	 return result;
	 }
	    /**
	     * ��ʱ���תΪʱ���ַ���
	     * <p>��ʽΪyyyy-MM-dd HH:mm:ss</p>
	     *
	     * @param milliseconds ����ʱ���
	     * @return ʱ���ַ���
	     */
	    public static String milliseconds2String(long milliseconds) {
	        return milliseconds2String(milliseconds, DEFAULT_SDF);
	    }

	    /**
	     * ��ʱ���תΪʱ���ַ���
	     * <p>��ʽΪ�û��Զ���</p>
	     *
	     * @param milliseconds ����ʱ���
	     * @param format       ʱ���ʽ
	     * @return ʱ���ַ���
	     */
	    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
	        return format.format(new Date(milliseconds));
	    }
	    /**
	     * ȥ���ַ����ұߵ�0
	     * @return ���ַ���
	     */
	    public static String trimR(String str) {
	        int i=str.indexOf(0);
	        String subStr=str;
	        if(i>0)
	        	subStr=str.substring(0,i);
	        return subStr;
	    }
	    /**
	     *�жϵ�ǰӦ�ó�����ǰ̨���Ǻ�̨<uses-permission android:name="android.permission.GET_TASKS" /> 
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
