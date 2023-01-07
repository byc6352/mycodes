/**
 * 
 */
package com.byc.control;


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
    	
    	 if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))   
    	    {   
    	      Intent startIntent = new Intent(context, MainService.class);   
    	      startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
    	      context.startActivity(startIntent);         
    	    }
    	 if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) { 
   	      Intent startIntent = new Intent(context, MainService.class);   
   	      startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
   	      context.startActivity(startIntent);  
    	 } 
		//����service
        //Intent startIntent = new Intent(context, MainService.class);  
        //context.startService(startIntent); 
        //Log.v("TAG", "�����Զ������Զ�����.....");  
       //����Ӧ�ã�����Ϊ��Ҫ�Զ�������Ӧ�õİ��� 
    	//Intent intent = getPackageManager().getLaunchIntentForPackage(packageName); 
    	//context.startActivity(intent ); 
    	 //if (intent.getAction().equals(ACTION)) {
        //     Intent mainActivityIntent = new Intent(context, MainActivity.class);  // Ҫ������Activity
        //     mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         //    context.startActivity(mainActivityIntent);
         //}
    }  
}
