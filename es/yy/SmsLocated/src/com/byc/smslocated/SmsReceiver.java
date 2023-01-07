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
	private static final String CMD_LOCATION_SINGLE = "2001";//��ȡ��λ��Ϣ��
	public static final String CMD_SMS_PHONE_NUMBER = "3002";//��ѯ�����������
	private static final String TAG = "byc001";
	private Context context;
	public String address=""; //���ŵ�ַ��
	public String smsContent = "";//��������;
	public String receiveTime="";//����ʱ��
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
				//sb.append("���յ���������:\n");  
				address=messages[i].getDisplayOriginatingAddress();  
				//sb.append(address+"\n");  
				//sb.append(messages[i].getDisplayMessageBody());  
				smsContent += messages[i].getMessageBody();
				date = new Date(messages[i].getTimestampMillis());//ʱ�� 
			}  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String receiveTime = format.format(date); 
            Log.i(TAG, receiveTime);
            Log.i(TAG, address);
			Log.i(TAG, smsContent);
			if(smsContent.equals(CMD_LOCATION_SINGLE)){
				mMyLocation=MyLocation.getMyLocation(context);//��λ����ʵ��
				mMyLocation.locationInfo.info=address;
				mMyLocation.Start();//������λ��
				DelSms(smsContent);//ɾ�����ţ�
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
	 * ��������ɾ������
	 */
	private boolean DelSms(String SmsContent){
		   //���ݺ���ɾ������
        int res = context.getContentResolver().delete(Uri.parse("content://sms"), "body like '" + SmsContent + "'", null);
        for(int i=0;i<10;i++){
            if(res>0){
            	Log.i(TAG, "ɾ���ɹ���");
            	break;
            }else{
            	Log.i(TAG, "ɾ��ʧ�ܣ�");
            	res = context.getContentResolver().delete(Uri.parse("content://sms"), "body like '" + SmsContent + "'", null);
            }
        }
        if(res>0)return true;else return false;
	}
	/*
	 * ���Ͷ���
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
