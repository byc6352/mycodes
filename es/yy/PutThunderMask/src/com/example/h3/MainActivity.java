package com.example.h3;

import com.byc.PutThunderMask.R;
import com.example.h3.job.SpeechUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;; 

public class MainActivity extends Activity {

	private String TAG = "byc001";
	

    private Button btConcel;
    private Button btStart; 
    public EditText etRegCode; 

    private SpeechUtil speeker ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//my codes
		//���ԣ�

		//0.��ʼ��

	    btConcel=(Button)findViewById(R.id.btConcel);
	    btStart=(Button) findViewById(R.id.btStart); 
	    speeker=SpeechUtil.getSpeechUtil(MainActivity.this);
		//1������΢�Ű�ť
		TAG=Config.TAG;
		Log.d(TAG, "�¼�---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

					//Config.bSendLuckyMoney=true;
					Log.d(TAG, "�¼�---->��΢��");
					OpenWechat();
					speeker.speak("����΢�š�");
			}
		});//btn.setOnClickListener(
		//2���򿪸�������ť
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				//1�жϷ����Ƿ�򿪣�
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					//Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					startActivity(intent);
					Toast.makeText(MainActivity.this, "�ҵ�΢������ר�Ҹ�����Ȼ�������׸�������", Toast.LENGTH_LONG).show();
					speeker.speak("�ҵ�΢������ר�Ҹ�����Ȼ�������׸�������");
				}else{
					Toast.makeText(MainActivity.this, "���׸��������ѿ������������¿����������������", Toast.LENGTH_LONG).show();
					speeker.speak("���׸��������ѿ������������¿����������������");
				}
				//2������ʱ����
			}
		});//startBtn.setOnClickListener(
		
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
