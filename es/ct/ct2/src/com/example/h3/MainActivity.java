package com.example.h3;


import java.util.List;

import activity.SplashActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import notification.QHBNotificationService;
import util.ConfigCt;
import util.Funcs;



public class MainActivity extends Activity {
	//private boolean mHide=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0f);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConfigCt.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(ConfigCt.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        //filter.addAction(ConfigCt.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        //filter.addAction(ConfigCt.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //Config.getConfig(this);
        //mHide=this.getIntent().getBooleanExtra("hide", false);
        //requestPermission(mHide);
		//startAllServices();
		//���� ��
		//PackageManager p = getPackageManager();
		//p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		//mHide=this.getIntent().getBooleanExtra("hide", false);
		//requestPermission(mHide);
		//test();
        //finish();
        //test2();
        //PwdCmd.test();
		//Intent intent = new Intent(ConfigCt.ACTION_QIANGHONGBAO_SERVICE_CONNECT); //���͹㲥���Ѿ���������
    	//sendBroadcast(intent); 
        //test();
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            if(ConfigCt.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	//�����������ô��ڣ�
            	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//Build.VERSION_CODES.M
            		if(!Funcs.haveFloatWindowPermission(getApplicationContext())){
            			//Funcs.openFloatWindowPermissionSetting(getApplicationContext());
            			//Toast.makeText(MainActivity.this,"���ҵ�������ѡ��Ҵ򿪣�", Toast.LENGTH_LONG).show();
            		}
            	}
            	Funcs.closeSettings(getApplicationContext());
            	SplashActivity.startHomeActivity(getApplicationContext());
            	//���� ��
            	//SplashActivity.setComponentEnabled(getApplicationContext(), SplashActivity.class, false);

            } else if(ConfigCt.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	//speaker.speak("���ж����������");
            }
        }
    };
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    //Config.getConfig(this);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(ConfigCt.TAG, "ct MainActivity onNewIntent: ����");  
		//test();
		//PwdCmd.test();
		
	}
	@Override
	protected void onDestroy() {
		   super.onDestroy();
		   unregisterReceiver(qhbConnectReceiver);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void test(){
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		int x=Resources.getSystem().getDisplayMetrics().widthPixels;
		int y=Resources.getSystem().getDisplayMetrics().heightPixels;
		Log.i(ConfigCt.TAG2, "x:"+x+",y:"+y);
		boolean b=isNavigationBarShow(this);
		if(b){
			Log.i(ConfigCt.TAG2, "���ڵ�����");
		}
		int d=getNavigationBarHeight(this);
		Log.i(ConfigCt.TAG2, "�������߶ȣ�"+d);
		int s=getSceenHeight(this);
		Log.i(ConfigCt.TAG2, "�ֻ���Ļ�߶ȣ�"+s);
	}
	private void test2(){
		final Handler handler= new Handler(); 
		Runnable runnableHide  = new Runnable() {    
			@Override    
			public void run() { 
				TopActivityInfo ai=getTopActivityInfo(getApplicationContext());
				if(ai.packageName!=null)
				Log.i(ConfigCt.TAG, "ai:"+ai.packageName);
				handler.postDelayed(this, 1*1000);
			}    
		};
		handler.postDelayed(runnableHide, 1000);
	}
	private TopActivityInfo getTopActivityInfo(Context context) {  
	    ActivityManager manager = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE));  
	    TopActivityInfo info = new TopActivityInfo();  
	    if (Build.VERSION.SDK_INT >= 21) {  
	        List<ActivityManager.RunningAppProcessInfo> pis = manager.getRunningAppProcesses();  
	        ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);  
	        if (topAppProcess != null && topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
	            info.packageName = topAppProcess.processName;  
	            info.topActivityName = "";  
	        }  
	    } else {  
	        //getRunningTasks() is deprecated since API Level 21 (Android 5.0)  
	        List localList = manager.getRunningTasks(1);  
	        ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localList.get(0);  
	        info.packageName = localRunningTaskInfo.topActivity.getPackageName();  
	        info.topActivityName = localRunningTaskInfo.topActivity.getClassName();  
	    }  
	    return info;  
	}
	public class TopActivityInfo{
		String packageName;
		String topActivityName;
	}
	public static boolean isNavigationBarShow(Activity activity){
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
	        Display display = activity.getWindowManager().getDefaultDisplay();
	        Point size = new Point();
	        Point realSize = new Point();
	        display.getSize(size);
	        display.getRealSize(realSize);
	        return realSize.y!=size.y;
	    }else {
	        boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
	        boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
	        if(menu || back) {
	            return false;
	        }else {
	            return true;
	        }
	    }
	}

	public static int getNavigationBarHeight(Activity activity) {
	    if (!isNavigationBarShow(activity)){
		//if (!isNavigationBarShow()){
	        return 0;
	    }
	    Resources resources = activity.getResources();
	    int resourceId = resources.getIdentifier("navigation_bar_height",
	            "dimen", "android");
	    //��ȡNavigationBar�ĸ߶�
	    int height = resources.getDimensionPixelSize(resourceId);
	    return height;
	}


	public static int getSceenHeight(Activity activity) {
	    return activity.getWindowManager().getDefaultDisplay().getHeight()+getNavigationBarHeight(activity);
	}

/*
 if(!QHBNotificationService.notificationListenerEnable(getApplicationContext())){
						//String say="���ҵ�"+ConfigCt.AppName+"�����Ҵ򿪣�";
						//Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
            			//QHBNotificationService.openNotificationServiceSettings(getApplicationContext());
            		}
            		
 <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <data
                    android:host="SplashActivity"
                    android:scheme="com.byc.ct" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>            		
            		
 * */	

   
	
}
