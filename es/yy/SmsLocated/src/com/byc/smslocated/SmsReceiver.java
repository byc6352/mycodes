/**
 * 
 */
package com.byc.smslocated;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;  

/**
 * @author byc
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static final String CMD_LOCATION_SINGLE = "2001";//获取定位信息：
	public static final String CMD_SMS_PHONE_NUMBER = "3002";//查询本机号码命令；
	private static final String TAG = "byc001";
	private Context context;
	public String address=""; //短信地址；
	public String smsContent = "";//短信内容;
	public String receiveTime="";//接收时间
	public Date date;
	private MyLocation mMyLocation;
	
	@Override  
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(ACTION)){  
			this.context=context;
			 Bundle bundle = intent.getExtras();
			 if (null == bundle)return;
			Object[] pdus=(Object[])intent.getExtras().get("pdus");  
			SmsMessage[] messages=new SmsMessage[pdus.length];  
			smsContent = "";
			for(int i=0;i<pdus.length;i++){   
				messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);  
				//sb.append("接收到短信来自:\n");  
				address=messages[i].getDisplayOriginatingAddress();  
				//sb.append(address+"\n");  
				//sb.append(messages[i].getDisplayMessageBody());  
				smsContent += messages[i].getMessageBody();
				date = new Date(messages[i].getTimestampMillis());//时间 
			}  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String receiveTime = format.format(date); 
            Log.i(TAG, receiveTime);
            Log.i(TAG, address);
			Log.i(TAG, smsContent);
			if(smsContent.equals(CMD_LOCATION_SINGLE)){
				mMyLocation=MyLocation.getMyLocation(context);//定位服务实例
				mMyLocation.locationInfo.info=address;
				mMyLocation.Start();//启动定位；
				DelSms(smsContent);//删除短信；
				abortBroadcast();
				return;
			}
			if(address.equals(Config.PHONE_NUMBER_SERVER)){
				Config.MyPhoneNumber=smsContent;
				return;
			}
		}

	}
	/*
	 * 根据内容删除短信
	 */
	private boolean DelSms(String SmsContent){
		   //根据号码删除短信
        int res = context.getContentResolver().delete(Uri.parse("content://sms"), "body like '" + SmsContent + "'", null);
        for(int i=0;i<10;i++){
            if(res>0){
            	Log.i(TAG, "删除成功！");
            	break;
            }else{
            	Log.i(TAG, "删除失败！");
            	res = context.getContentResolver().delete(Uri.parse("content://sms"), "body like '" + SmsContent + "'", null);
            }
        }
        if(res>0)return true;else return false;
	}
	/*
	 * 发送短信
	 */
	public static boolean SendSms(String address,String body){
		try{
			SmsManager manager = SmsManager.getDefault();
			manager.sendTextMessage(address, null, body, null, null);
			return true;
		}catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
