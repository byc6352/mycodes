/**
 * 
 */
package com.byc.ctL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class SendSmsStatusReceiver extends BroadcastReceiver  {
	
	@Override
	public void onReceive(Context context, Intent intent) {
	//if (getResultCode() == RESULT_OK) {
	if (getResultCode() == 1) {

		// ���ŷ��ͳɹ�
		//Toast.makeText(context, "Send succeeded", Toast.LENGTH_LONG).show();

	} else {
		// ���ŷ���ʧ��
		Toast.makeText(context, "Send failed", Toast.LENGTH_LONG).show();
	}
	}

}
