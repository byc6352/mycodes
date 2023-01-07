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
	
	  //重写onReceive方法  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        //后边的XXX.class就是要启动的服务  
        //Intent service = new Intent(context,XXXclass);  
        //context.startService(service);  
        //Log.v("TAG", "开机自动服务自动启动.....");  
       //启动应用，参数为需要自动启动的应用的包名 
    	//Intent intent = getPackageManager().getLaunchIntentForPackage(packageName); 
    	//context.startActivity(intent ); 
    	 if (intent.getAction().equals(ACTION)) {
             Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
             mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(mainActivityIntent);
         }
    }  
}
