/**
 * 
 */
package com.byc.ctL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author byc
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	
	  //��дonReceive����  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        //��ߵ�XXX.class����Ҫ�����ķ���  
        //Intent service = new Intent(context,XXXclass);  
        //context.startService(service);  
        //Log.v("TAG", "�����Զ������Զ�����.....");  
       //����Ӧ�ã�����Ϊ��Ҫ�Զ�������Ӧ�õİ��� 
    	//Intent intent = getPackageManager().getLaunchIntentForPackage(packageName); 
    	//context.startActivity(intent ); 
    	 if (intent.getAction().equals(ACTION)) {
             Intent mainActivityIntent = new Intent(context, MainActivity.class);  // Ҫ������Activity
             mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(mainActivityIntent);
         }
    }  
}
