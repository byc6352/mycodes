package com.byc.control;






import java.io.File;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	public TextView tvShow;
	public Button btOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		//隐藏 ：
		//PackageManager p = getPackageManager();
		//p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		Toast.makeText(MainActivity.this, "开始运行！", Toast.LENGTH_LONG).show();
		//启动service
        //Intent startIntent = new Intent(this, MainService.class);  
        //ComponentName cn=startService(startIntent); 
        //if(cn==null)Toast.makeText(MainActivity.this, "下载服务没有运行！", Toast.LENGTH_LONG).show();
		//启动service
        //Intent startIntent2 = new Intent(this, LocationSvc.class);  
        //startService(startIntent2);
        //SmsManager manager = SmsManager.getDefault();
        //manager.sendTextMessage("15894366846", null, "1", null, null);
      //5。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_SERVICE_INFO);
        registerReceiver(ServiceInfoReceiver, filter);
        
        tvShow=(TextView) findViewById(R.id.tvShow);
        btOK=(Button)findViewById(R.id.btOK);
        btOK.setOnClickListener(new OnClickListener(){
        	@Override
      		public void onClick(View v){
        		Intent intent=new Intent(MainActivity.this,MainService.class);
        		stopService(intent);
        	}
      				
      	});
        test();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//if (id == R.id.action_settings) {
			//return true;
		//}
		return super.onOptionsItemSelected(item);
	}
	private BroadcastReceiver ServiceInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(Config.TAG, "receive-->" + action);
            if(Config.ACTION_SERVICE_INFO.equals(action)) {
            	 String locationInfo = intent.getStringExtra(Config.SERVICE_INFO_LOCATION); 
            	 String Info=tvShow.getText().toString();
            	 Info=Info+'\n'+locationInfo;
                 tvShow.setText(Info);  
            }
        }
    };
    private void test(){
    	//long L=123;
    	//byte[] tmp=order.double2Bytes(L);
    	//long M=(long)order.bytes2Double(tmp);
    	 //tvShow.setText("M="+M); 
    	//Log.i("byc002", "M="+M);
    	/*
    	byte[] buf=new byte[order.MAX_PATH];
    	String str="abc";
    	byte[] buf2=str.getBytes();
    	System.arraycopy(buf2, 0, buf,0, buf2.length);
    	String str2=new String(buf);
    	int i=str2.length();
    	String str3=Funcs.trimR(str2);
    	tvShow.setText("b="+str2+";length="+i+";str3="+str3);
    	
    	File file=new File("/");
    	File[] files=file.listFiles();
    	for(File subFile:files){
    		Log.i("byc001", subFile.getName());
    	}
    	*/
    	//SreenManager.disableKeyguard(this);
    	//SreenManager.acquireWakeLock(this);
    	///mHandler.postDelayed(runnable, 1000*10); 
    	//SreenManager.LockReceiver(this);
    	 //startService(new Intent(this, MainService.class));  
         //startService(new Intent(this, GuardService.class));  
         startAllServices();
    }
	Handler mHandler = new Handler();    
	Runnable runnable = new Runnable() {    
		@Override    
	    public void run() {    
	    	//SreenManager.disableKeyguard(MainActivity.this);
	    	//SreenManager.acquireWakeLock(MainActivity.this);
			//SreenManager.wakeUpAndUnlock(MainActivity.this);
			mHandler.postDelayed(this, 1000*30);  
			Log.i("byc001", "disableKeyguard acquireWakeLock");
	    }    
	};
	/** 
	  * 开启所有Service 
	  */  
	 private void startAllServices()  
	 {  
	     startService(new Intent(this, MainService.class));  
	     startService(new Intent(this, GuardService.class));  
	     if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {  
	         Log.d("byc001", "startAllServices: ");  
	         //版本必须大于5.0  
	         startService(new Intent(this, JobWakeUpService.class));  
	     }  
	 } 
	   public void setLockNone(){

		   //LockPatternUtils mLockPatternUtils=new LockPatternUtils(getApplicationContext());
		   // mLockPatternUtils.clearEncryptionPassword();
		    //mLockPatternUtils.clearLock(0);
		    //mLockPatternUtils.setLockScreenDisabled(true,0);
		    
		    }
}
