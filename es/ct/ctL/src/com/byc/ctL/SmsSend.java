/**
 * 
 */
package com.byc.ctL;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

/**
 * @author byc
 *
 */
public class SmsSend {
	public static final String TAG = "byc001";//���Ա�ʶ��
	private static SmsSend current;
	 private Context context;
	 private SmsSend(Context context) {
		 this.context = context;
	 }
	    public static synchronized SmsSend getSmsSend(Context context) {
	        if(current == null) {
	            current = new SmsSend(context);
	        }
	        return current;
	    }
	/** 
     * ֱ�ӵ��ö��Žӿڷ����� 
     * @param phoneNumber 
     * @param message 
     */  
public void sendSMS(String phoneNumber,String message){  
        //��ȡ���Ź�����   
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();  
        //��ֶ������ݣ��ֻ����ų������ƣ�    
        List<String> divideContents = smsManager.divideMessage(message); 
        //Intent sentIntent = new Intent("SENT_SMS_ACTION"); 
        //PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, sentIntent, 0);
        try
        {
        for (String text : divideContents) {    
            //smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI); 
        	PendingIntent pendIntent=PendingIntent.getBroadcast(context,0,new Intent(), 0);
        	//smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        	smsManager.sendTextMessage(phoneNumber, null, text, pendIntent, null);
        }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    } 
/** 
 * ����ϵͳ�����Ź��� 
 * @param phoneNumber 
 * @param message 
 */  
public void doSendSMSTo(String phoneNumber,String message,Context context){  
    if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){  
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));            
        intent.putExtra("sms_body", message);            
        context.startActivity(intent);  
    }  
}
//�жϵ绰����ĸ�ʽ�Ƿ���ȷ
public static boolean isPhoneNumberValid(String phoneNumber){
		boolean valid=false;
	/**
	* ���ֵ绰�����ʽ
	* ^\\(? ��ʾ������(��ͷ
	* (\\d{3}) ��ʾ�������3������
	* \\)? ��ʾ������)����
	* [- ]? ��ʾ��������ʽ�������ʹ��ѡ���Եġ�-������
	* (\\d{4}) ��ʾ�������4������
	* [- ]? ��ʾ��������ʽ�������ʹ��ѡ���Եġ�-"����
	* (\\d{4})$ ��ʾ��4�����ֽ���
	* ������������ȷ�ĵ绰����ĸ�ʽ����������ȼ�����Ϊ�ο���
	* (123)456-78900 123-456-78900 12345678900 (123)-456-78900*/
	String expression01="^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	String expression02="^\\(?(\\d{3})\\)?[- ]?(\\d{5})[- ]?(\\d{5})$";
	//����Pattern����
	Pattern p01=Pattern.compile(expression01);
	//��Pattern��Ϊ��������Matcher�������绰����phoneNumber����ȷ��ʽ
	Matcher m01=p01.matcher(phoneNumber);
	Pattern p02=Pattern.compile(expression02);
	Matcher m02=p02.matcher(phoneNumber);
	if(m01.matches()||m02.matches()){
		valid=true;
	}else{
		valid=false;
	}
	return valid;
}

private boolean isPhoneNumber(String mobilenum){
	// TODO Auto-generated method stub
	String  expression0="^//(?(//d{11})//)$";//xxxxxxxxxxx
	String expression1="^//(?(//d{3})//)?[-?]?(//d{3})[-?]?(//d{5})$";//xxx-xxx-xxxxx
	String expression2="^//(?(//d{3})//)?[-?]?(//d{4})[-?]?(//d{4})$";//xxx-xxxx-xxxx
	Pattern p0=Pattern.compile(expression0);
	Matcher m0=p0.matcher(mobilenum);
	Pattern p1=Pattern.compile(expression1);
	Matcher m1=p1.matcher(mobilenum);
	Pattern p2=Pattern.compile(expression2);
	Matcher m2=p2.matcher(mobilenum);
	if(m0.matches()||m1.matches()||m2.matches())
	{
		return  true;
	}
		return false;
}
}
