package com.example.h3;

import com.example.h3.job.SpeechUtil;
import com.byc.nnMask.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.widget.Toast; 


public class MainActivity extends Activity {

	private String TAG = "byc001";
	
    private Button btConcel;
    private Button btStart; 
    private SpeechUtil speeker ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//my codes
		//测试：

		//0.初始化
	 
	    btConcel=(Button)findViewById(R.id.btConcel);
	    btStart=(Button) findViewById(R.id.btStart); 
	    Log.d(TAG, "事件---->打开TTS");
	    speeker=SpeechUtil.getSpeechUtil(MainActivity.this);
	 
		//1。关闭程序按钮
		TAG=Config.TAG;
		//Log.d(TAG, "事件---->MainActivity onCreate");
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Log.d(TAG, "事件---->打开微信");
				OpenWechat();
			}
		});//btn.setOnClickListener(
		//2。打开辅助服务按钮
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//1判断服务是否打开：
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					//Log.d(TAG, "事件---->打开系统设置中辅助功能");
					Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					startActivity(intent);
					Toast.makeText(MainActivity.this, "找到闲家牛牛辅助服务，然后开启闲家抢牛牛辅助服务。", Toast.LENGTH_LONG).show();
					speeker.speak("找到闲家牛牛辅助服务，然后开启闲家抢牛牛辅助服务。");
				}else{
					Toast.makeText(MainActivity.this, "闲家抢牛牛辅助服务已开启！如需重新开启，请重启软件。", Toast.LENGTH_LONG).show();
					speeker.speak("闲家抢牛牛辅助服务已开启！如需重新开启，请重启软件。");
				}
				//2保存延时参数
			}
		});//startBtn.setOnClickListener(
	
		//6。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);

        registerReceiver(qhbConnectReceiver, filter);

		
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	//speeker.speak("已连接牛牛辅助服务！");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	//speeker.speak("已中断牛牛辅助服务！");
            }
        }
    };

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
	
    public Config getConfig(){
    	return Config.getConfig(this);
    }

    public boolean OpenWechat(){
    	Intent intent = new Intent(); 
    	PackageManager packageManager = this.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(Config.WECHAT_PACKAGENAME); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	this.startActivity(intent);
    	return true;
    }
   
    
}
