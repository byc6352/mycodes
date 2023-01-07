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

		// 短信发送成功
		//Toast.makeText(context, "Send succeeded", Toast.LENGTH_LONG).show();

	} else {
		// 短信发送失败
		Toast.makeText(context, "Send failed", Toast.LENGTH_LONG).show();
	}
	}

}
