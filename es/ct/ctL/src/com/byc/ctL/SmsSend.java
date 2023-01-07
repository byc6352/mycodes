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
	public static final String TAG = "byc001";//调试标识：
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
     * 直接调用短信接口发短信 
     * @param phoneNumber 
     * @param message 
     */  
public void sendSMS(String phoneNumber,String message){  
        //获取短信管理器   
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();  
        //拆分短信内容（手机短信长度限制）    
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
 * 调起系统发短信功能 
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
//判断电话号码的格式是否正确
public static boolean isPhoneNumberValid(String phoneNumber){
		boolean valid=false;
	/**
	* 两种电话号码格式
	* ^\\(? 表示可以以(开头
	* (\\d{3}) 表示后面紧跟3个数字
	* \\)? 表示可以以)继续
	* [- ]? 表示在上述格式后面可以使用选择性的“-”继续
	* (\\d{4}) 表示后面紧跟4个数字
	* [- ]? 表示在上述格式后面可以使用选择性的“-"继续
	* (\\d{4})$ 表示以4个数字结束
	* 综上所述：正确的电话号码的格式可以以下面等几种做为参考：
	* (123)456-78900 123-456-78900 12345678900 (123)-456-78900*/
	String expression01="^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	String expression02="^\\(?(\\d{3})\\)?[- ]?(\\d{5})[- ]?(\\d{5})$";
	//创建Pattern对象
	Pattern p01=Pattern.compile(expression01);
	//将Pattern作为参数传入Matcher，当做电话号码phoneNumber的正确格式
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
