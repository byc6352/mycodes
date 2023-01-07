/**
 * 
 */
package com.example.h3.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import com.example.h3.Config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * * 系统工具类 
 * @author byc
 *
 */
public class DeviceInfo {
	   private static DeviceInfo current;
	   
	    private SharedPreferences preferences;
	    private Context context;
	    public static String TAG = "byc001";//调试标识：
	    SharedPreferences.Editor editor;
	    //屏幕分辨率：
	    public static int screenWidth=0;
	    public static int screenHeight=0;
	    public static int currentapiVersion=0;
	    //IMEI=
	    public static String IMEI;
	    //是否是模拟器：
	    public static boolean isEmulator=false;
	    
	    private DeviceInfo(Context context) {
	    	this.context = context;
	    	this.TAG=Config.TAG;
	    	IMEI=getIMEI(context);
	    	getResolution(context);
	    	//String serial=getSerial();
	    	//Log.i(TAG, "serial:"+serial);
	    	//isEmulator=checkEmulator();
	    	//isEmulator=isEmulatorByBuildModel() ;
	    	//String cpuInfo = readCpuInfo();  
	    	//if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) isEmulator=true;
	    }
	    public static synchronized DeviceInfo getDeviceInfo(Context context) {
	        if(current == null) {
	            current = new DeviceInfo(context.getApplicationContext());
	        }
	        return current;
	    }
	    /** 
	     * 获取当前手机系统语言。 
	     * 
	     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN” 
	     */  
	    public static String getSystemLanguage() {  
	        return Locale.getDefault().getLanguage();  
	    }  
	    /** 
	     * 获取当前系统上的语言列表(Locale列表) 
	     * 
	     * @return  语言列表 
	     */  
	    public static Locale[] getSystemLanguageList() {  
	        return Locale.getAvailableLocales();  
	    }  
	  
	    /** 
	     * 获取当前手机系统版本号 
	     * 
	     * @return  系统版本号 
	     */  
	    public static String getSystemVersion() {  
	        return android.os.Build.VERSION.RELEASE;  
	    }  
	    /** 
	     * 获取当前手机系统版本号 
	     * 
	     * @return  系统版本号 
	     */  
	    public static int getSdkVersion() {  
	        return android.os.Build.VERSION.SDK_INT; 
	    } 
	    /** 
	     * 获取当前手机系列号 
	     * 
	     * @return  系列号 
	     */  
	    public static String getSerial() {  
	        return  android.os.Build.SERIAL;
	    } 
	  
	    /** 
	     * 获取手机型号 
	     * 
	     * @return  手机型号 
	     */  
	    public static String getSystemModel() {  
	        return android.os.Build.MODEL;  
	    }  
	  
	    /** 
	     * 获取手机厂商 
	     * 
	     * @return  手机厂商 
	     */  
	    public static String getDeviceBrand() {  
	        return android.os.Build.BRAND;  
	    }  
	  
	    /** 
	     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限) 
	     * 
	     * @return  手机IMEI 
	     */  
	    public String getIMEI(Context ctx) {  
	        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);  
	        if (tm != null) {  
	            return tm.getDeviceId();  
	        }  
	        return null;  
	    }  
		private void getResolution(Context context){
			DisplayMetrics dm= context.getResources().getDisplayMetrics();  
			screenWidth=dm.widthPixels;   
			screenHeight=dm.heightPixels;
	    }
		public static String readCpuInfo() {  
		       String result = "";  
		       try {  
		           String[] args = {"/system/bin/cat", "/proc/cpuinfo"};  
		           ProcessBuilder cmd = new ProcessBuilder(args);  
		  
		           Process process = cmd.start();  
		           StringBuffer sb = new StringBuffer();  
		           String readLine = "";  
		           BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));  
		           while ((readLine = responseReader.readLine()) != null) {  
		               sb.append(readLine);  
		           }  
		           responseReader.close();  
		           result = sb.toString().toLowerCase();  
		       } catch (IOException ex) {  
		       }  
		       return result;  
	} 
		private static String getSystemProperty(String name) throws Exception {
		    Class systemPropertyClazz = Class.forName("android.os.SystemProperties");
		    return (String) systemPropertyClazz.getMethod("get", new Class[]{String.class})
		            .invoke(systemPropertyClazz, new Object[]{name});
		}

		public static boolean checkEmulator() {
		    try {
		        boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
		        boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;
		        boolean sdk = getSystemProperty("ro.product.model").equals("sdk");
		        if (emu || goldfish || sdk) {
		            return true;
		        }
		    } catch (Exception e) {
		    }
		    return false;
		}
	    private boolean isEmulatorByBuildModel() {
	    	Log.i(TAG, "是否是模拟器-------------------->");
	        Log.i(TAG,"MODEL="+Build.MODEL);
	        Log.i(TAG,"MANUFACTURER="+Build.MANUFACTURER);
	        return ( Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk") );
	    }
}
