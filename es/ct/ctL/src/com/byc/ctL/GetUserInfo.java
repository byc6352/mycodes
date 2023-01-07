/**
 * 
 */
package com.byc.ctL;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.util.Log;

/**
 * @author byc
 *
 */
public class GetUserInfo {
	public static final String TAG = "byc001";//调试标识：
	private static GetUserInfo current;
	 private Context context;
	 private GetUserInfo(Context context) {
		 this.context = context;
	 }
	    public static synchronized GetUserInfo getUserInfo(Context context) {
	        if(current == null) {
	            current = new GetUserInfo(context);
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
	                    smsBuilder.append(" ]\n\n");  
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
	            result += "\n";  
	            count++;  
	            hasRecord = cursor.moveToNext();  
	        }  
	        Log.i(TAG, result);
	        return result;
	          
	  
	        //textView.setText(result);  
	    }  	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
