/**
 * 
 */
package com.byc.smslocation;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.amap.api.maps.MapView;

import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;  

/**
 * @author byc
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static final String CMD_LOCATION_SINGLE = "2001";//��ȡ��λ��Ϣ��
	private static final String TAG = "byc001";
	private Context context;
	public String address=""; //���ŵ�ַ��
	public String smsContent = "";//��������;
	public String receiveTime="";//����ʱ��
	public Date date;
	//private MyLocation mMyLocation;
	
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
				//messages[i].
			}  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String receiveTime = format.format(date); 
            Log.i(TAG, receiveTime);
            Log.i(TAG, address);
			Log.i(TAG, smsContent);
			//if(address.equals("+86"+Config.CurmPhoneNumber)){
			//if(smsContent.equals(CMD_LOCATION_SINGLE)){
				//mMyLocation=MyLocation.getMyLocation(context);//��λ����ʵ��
				//mMyLocation.locationInfo.info=address;
				//mMyLocation.Start();//������λ��67
				//DelSms(smsContent);//ɾ�����ţ�
				MyLocationMap mm=MyLocationMap.getMyLocationMap(null,null, null);
				if(mm.ParseSmsContent(smsContent)){
					mm.SetCenterPosition(mm.mMapXY);
					mm.SetMarker(mm.mMapXY);
					Toast.makeText(context, "��λ��ʽ��"+mm.mProvider, Toast.LENGTH_LONG).show();
					//DelSms(address);
					abortBroadcast();
				}

			//}
		}

	}
	/*
	 * ��������ɾ������
	 */
	private boolean DelSms(String address){
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
        if(res>0)return true;else return false;
	}
	/*
	 * ���Ͷ���
	 */
	public static boolean SendSms(String phoneNumber){
		String body=CMD_LOCATION_SINGLE;
		try{
			SmsManager manager = SmsManager.getDefault();
			manager.sendTextMessage(phoneNumber, null, body, null, null);
			return true;
		}catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
