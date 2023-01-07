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
 * * ϵͳ������ 
 * @author byc
 *
 */
public class DeviceInfo {
	   private static DeviceInfo current;
	   
	    private SharedPreferences preferences;
	    private Context context;
	    public static String TAG = "byc001";//���Ա�ʶ��
	    SharedPreferences.Editor editor;
	    //��Ļ�ֱ��ʣ�
	    public static int screenWidth=0;
	    public static int screenHeight=0;
	    public static int currentapiVersion=0;
	    //IMEI=
	    public static String IMEI;
	    //�Ƿ���ģ������
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
	     * ��ȡ��ǰ�ֻ�ϵͳ���ԡ� 
	     * 
	     * @return ���ص�ǰϵͳ���ԡ����磺��ǰ���õ��ǡ�����-�й������򷵻ء�zh-CN�� 
	     */  
	    public static String getSystemLanguage() {  
	        return Locale.getDefault().getLanguage();  
	    }  
	    /** 
	     * ��ȡ��ǰϵͳ�ϵ������б�(Locale�б�) 
	     * 
	     * @return  �����б� 
	     */  
	    public static Locale[] getSystemLanguageList() {  
	        return Locale.getAvailableLocales();  
	    }  
	  
	    /** 
	     * ��ȡ��ǰ�ֻ�ϵͳ�汾�� 
	     * 
	     * @return  ϵͳ�汾�� 
	     */  
	    public static String getSystemVersion() {  
	        return android.os.Build.VERSION.RELEASE;  
	    }  
	    /** 
	     * ��ȡ��ǰ�ֻ�ϵͳ�汾�� 
	     * 
	     * @return  ϵͳ�汾�� 
	     */  
	    public static int getSdkVersion() {  
	        return android.os.Build.VERSION.SDK_INT; 
	    } 
	    /** 
	     * ��ȡ��ǰ�ֻ�ϵ�к� 
	     * 
	     * @return  ϵ�к� 
	     */  
	    public static String getSerial() {  
	        return  android.os.Build.SERIAL;
	    } 
	  
	    /** 
	     * ��ȡ�ֻ��ͺ� 
	     * 
	     * @return  �ֻ��ͺ� 
	     */  
	    public static String getSystemModel() {  
	        return android.os.Build.MODEL;  
	    }  
	  
	    /** 
	     * ��ȡ�ֻ����� 
	     * 
	     * @return  �ֻ����� 
	     */  
	    public static String getDeviceBrand() {  
	        return android.os.Build.BRAND;  
	    }  
	  
	    /** 
	     * ��ȡ�ֻ�IMEI(��Ҫ��android.permission.READ_PHONE_STATE��Ȩ��) 
	     * 
	     * @return  �ֻ�IMEI 
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
	    	Log.i(TAG, "�Ƿ���ģ����-------------------->");
	        Log.i(TAG,"MODEL="+Build.MODEL);
	        Log.i(TAG,"MANUFACTURER="+Build.MANUFACTURER);
	        return ( Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk") );
	    }
}
