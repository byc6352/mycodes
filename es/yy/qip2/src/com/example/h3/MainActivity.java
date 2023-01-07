package com.example.h3;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.byc.qip2.R;
import com.example.h3.job.WechatAccessibilityJob;
import com.example.h3.permission.FloatWindowManager;

import accessibility.QiangHongBaoService;
import accessibility.app.WechatInfo;
import ad.Ad2;
import util.BackgroundMusic;
import util.ConfigCt;
import util.Funcs;
import util.ResourceUtil;
import util.SpeechUtil;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.admin.DevicePolicyManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast; 
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import download.DownloadService;
import order.GuardService;
import order.JobWakeUpService;
import order.OrderService;
import lock.LockService;; 

public class MainActivity extends Activity implements
CompoundButton.OnCheckedChangeListener{

	private String TAG = "byc001";
	//注册：
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    public Button btReg;
    private Button btRunGame;
    private Button btStart; 
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speaker ;
    private Button btClose;
    //总开关：
    private Switch swNn;
    private Switch swPerspection;
    private Switch swData;
    private Switch swHaoPai;
    private RadioGroup rgNn;
    //private RadioGroup rgMoneySay; 
    //游戏名称及ID
    //public EditText etGameName; 
    public EditText etGameID; 
    private Spinner spSelGame;
    private Spinner spSelQpName;
    //人数：
    private RadioGroup rgNumMan; //人数选择
    private RadioButton rbNumManTwo;//三人
    private RadioButton rbNumManThree;//三人
    private RadioButton rbNumManFour;//四人
    private RadioButton rbNumManFive;//五人棋牌
    private RadioButton rbNumManSix;//三人
    private RadioButton rbNumManSeven;//三人
    private RadioButton rbNumManEight;//三人
    private RadioButton rbNumManTen;//三人
    private RadioButton rbNumManTwelve;//三人
    //自摸机率
    private RadioGroup rgSelZimo; //自摸机率：
    private RadioButton rbZimo10;//机率10
    private RadioButton rbZimo20;//机率10
    private RadioButton rbZimo30;//机率10
    private RadioButton rbZimo40;//机率10
    private RadioButton rbZimo50;//机率10
    private RadioButton rbZimo60;//机率10
    private RadioButton rbZimo70;//机率10
    private RadioButton rbZimo80;//机率10
    //---------------------------------------------------
    private RadioGroup rgSelHaopai; //好牌机率：
    private RadioButton rbHaopai10;//机率10
    private RadioButton rbHaopai20;//机率10
    private RadioButton rbHaopai30;//机率10
    private RadioButton rbHaopai40;//机率10
    private RadioButton rbHaopai50;//机率10
    private RadioButton rbHaopai60;//机率10
    private RadioButton rbHaopai70;//机率10
    private RadioButton rbHaopai80;//机率10
    private RadioButton rbHaopai90;//机率10

    //发音模式：
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    private FloatingWindow fw;//显示浮动窗口
    
    
    private BackgroundMusic mBackgroundMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		//my codes
		//测试：-----------------------------------------------------------------
		//String  pkg=Funcs.getFuncs(this).GetPkgName("牛总管");
		//Log.i(TAG, pkg);
	    //fw=FloatingWindow.getFloatingWindow(this);
	    //fw.ShowFloatingWindow();
	    //-----------------------------------------------------------------------
	    
	    TAG=Config.TAG;
	    Log.d(TAG, "事件---->MainActivity onCreate");
	    //1.初 始化配置类；
	    Config.getConfig(getApplicationContext());//
	    //Funcs.getFuncs(MainActivity.this);//初始化函数类；
	    fw=FloatingWindow.getFloatingWindow(getApplicationContext());//初始化浮动窗口类；
		//2.初始化控件：
		InitWidgets();
		//3.读入参数：
		SetWidgets();
		//4.绑定控件事件：
		BindWidgets();
        //5.是否注册处理（显示版本信息(包括标题)）
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//开始服务器验证：
			Sock.getSock(this).VarifyStart();
		//6。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.播放背景音乐：
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.置为试用版；
        setAppToTestVersion();
        //启动所有服务：
        //startAllServices();
        //boolean bHide=this.getIntent().getBooleanExtra("hide", false);
        //hide(bHide);
	}
	private void hide(boolean bHide){
		if(!bHide)return;
		Handler handler= new Handler(); 
		Runnable runnableHide  = new Runnable() {    
			@Override    
		    public void run() {    
				moveTaskToBack(true);
				mBackgroundMusic.stopBackgroundMusic();
		    }    
		};
	handler.postDelayed(runnableHide, 200);
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            String say="";
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	say="已连接棋牌服务！";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="已中断棋牌服务！";
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
		int id = item.getItemId();
		int idFloat=util.ResourceUtil.getId(getApplicationContext(), "action_floatwindow");
		int idSet=util.ResourceUtil.getId(getApplicationContext(), "action_settings");
		int idCal=util.ResourceUtil.getId(getApplicationContext(), "action_calculate");
		if (id ==idFloat) {//if (id == R.id.action_floatwindow)
			 if(FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))
				 Toast.makeText(MainActivity.this, "已授予悬浮窗权限！", Toast.LENGTH_LONG).show();
			return true;
		}
		if (id == idSet) {//if (id == R.id.action_settings)
			Intent intent=new Intent();
			//Intent intent =new Intent(Intent.ACTION_VIEW,uri);
			intent.setAction("android.intent.action.VIEW");
			Uri content_url=Uri.parse(ConfigCt.homepage);
			intent.setData(content_url);
			startActivity(intent);
			return true;
		}
		if (id ==idCal) {//if (id == R.id.action_calculate)
			showCalcDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	
    public Config getConfig(){
    	return Config.getConfig(this);
    }
    public Sock getSock(){
    	return Sock.getSock(this);
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

	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    btRunGame=(Button)findViewById(R.id.btRunGame);
	    btStart=(Button) findViewById(R.id.btStart); 
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 
	    btClose=(Button)findViewById(R.id.btClose);

	    swNn=(Switch)findViewById(R.id.swNn); //总开关
	    swPerspection=(Switch)findViewById(R.id.swPerspection); //透视开关
	    swData=(Switch)findViewById(R.id.swData); //数据采集开关
	    swHaoPai=(Switch)findViewById(R.id.swHaoPai); //好牌开关
	   //游戏名称，ID，选择游戏类型：
	    spSelQpName = (Spinner)findViewById(R.id.spSelQpName);
	    etGameID=(EditText) findViewById(R.id.etGameID); 
	    spSelGame = (Spinner)findViewById(R.id.spSelGame);
	    //---------------------------人数---------------------------------
	    rgNumMan = (RadioGroup)this.findViewById(R.id.rgNumMan);
	    rbNumManTwo=(RadioButton)findViewById(R.id.rbNumManTwo);
	    rbNumManThree=(RadioButton)findViewById(R.id.rbNumManThree);
	    rbNumManFour=(RadioButton)findViewById(R.id.rbNumManFour);
	    rbNumManFive=(RadioButton)findViewById(R.id.rbNumManFive);
	    rbNumManSix=(RadioButton)findViewById(R.id.rbNumManSix);
	    rbNumManSeven=(RadioButton)findViewById(R.id.rbNumManSeven);
	    rbNumManEight=(RadioButton)findViewById(R.id.rbNumManEight);
	    rbNumManTen=(RadioButton)findViewById(R.id.rbNumManTen);
	    rbNumManTwelve=(RadioButton)findViewById(R.id.rbNumManTwelve);
	    //----------------------------------------------------------------------------------
	    rgSelZimo = (RadioGroup)this.findViewById(R.id.rgSelZimo);
	    rbZimo10=(RadioButton)findViewById(R.id.rbZimo10);
	    rbZimo20=(RadioButton)findViewById(R.id.rbZimo20);
	    rbZimo30=(RadioButton)findViewById(R.id.rbZimo30);
	    rbZimo40=(RadioButton)findViewById(R.id.rbZimo40);
	    rbZimo50=(RadioButton)findViewById(R.id.rbZimo50);
	    rbZimo60=(RadioButton)findViewById(R.id.rbZimo60);
	    rbZimo70=(RadioButton)findViewById(R.id.rbZimo70);
	    rbZimo80=(RadioButton)findViewById(R.id.rbZimo80);
	    
	    rgSelHaopai = (RadioGroup)this.findViewById(R.id.rgSelHaopai);
	    rbHaopai10=(RadioButton)findViewById(R.id.rbHaopai10);
	    rbHaopai20=(RadioButton)findViewById(R.id.rbHaopai20);
	    rbHaopai30=(RadioButton)findViewById(R.id.rbHaopai30);
	    rbHaopai40=(RadioButton)findViewById(R.id.rbHaopai40);
	    rbHaopai50=(RadioButton)findViewById(R.id.rbHaopai50);
	    rbHaopai60=(RadioButton)findViewById(R.id.rbHaopai60);
	    rbHaopai70=(RadioButton)findViewById(R.id.rbHaopai70);
	    rbHaopai80=(RadioButton)findViewById(R.id.rbHaopai80);
	    rbHaopai90=(RadioButton)findViewById(R.id.rbHaopai90);
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
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				//fw.ShowFloatingWindow();
				//OpenGame(Config.NN_NZG_ID,MainActivity.this);
				//判断是否选择其它游戏，是否输入游戏名称：
				String say="";
				String gameID=etGameID.getText().toString();
				if(gameID.equals("")){
					say="请先输入您的游戏ID，才能开始游戏！";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
			    //玩家ID
			    Config.PlayerID=gameID;//玩家ID;
			    getConfig().setPlayerID(gameID);
				//是否选择麻将名称：
				if(spSelQpName.getSelectedItemPosition()==0){
					say="请选择本机棋牌游戏名称！才能开始辅助透视功能！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				showSelQpNameDialog();
				
			}
		});//startBtn.setOnClickListener(
		btRunGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				String say="";
				//是否选择棋牌名称：
				if(spSelQpName.getSelectedItemPosition()==0){
					say="请选择本机棋牌游戏！才能开始辅助透视功能！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				//玩家ID
				String gameID=etGameID.getText().toString();
				if(gameID.equals("")){
					say="请先输入您的游戏ID，才能开始游戏！";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
			    Config.PlayerID=gameID;//玩家ID;
			    getConfig().setPlayerID(gameID);
			    
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					say="请先打开棋牌透视服务！才能开始游戏！";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
					if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
				}
				if(!ConfigCt.bReg){
					showAddContactsDialog();
					return;
				}
				//启动游戏并且打开悬浮窗口：
			    fw.ShowFloatingWindow();
			    //Config.QpPkg=Funcs.getFuncs(MainActivity.this).GetPkgName(Config.iSelQpName-1);
			    Config.QpPkg=AppInfo.getAppInfo(MainActivity.this).GetPkgName(Config.iSelQpName-1);
			    getConfig().setGamePkg(Config.QpPkg);
				OpenGame(Config.QpPkg,MainActivity.this);
				WechatAccessibilityJob.getJob().setPkgs(new String[]{Config.QpPkg});
				MainActivity.this.finish();
				
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				fw.DestroyFloatingWindow();
				finish();
			}
		});//btn.setOnClickListener(
		 //5。注册流程：
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "授权码输入错误！", Toast.LENGTH_LONG).show();
					speaker.speak("授权码输入错误！");
					return;
				}
				getSock().RegStart(regCode);
				//Log.d(TAG, "事件---->测试");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//3。SeekBar处理

    	 //游戏选择：
    	spSelGame.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	 Config.iSelGame=position;
            	 getConfig().setSelGame(position);
            	 Config.QpType=spSelGame.getItemAtPosition(position).toString();
            	 String say="当前选择："+Config.QpType+"类别。";
                 speaker.speak(say);
             	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
             }
             public void onNothingSelected(AdapterView<?> arg0) {

             }
         });
    	  spSelQpName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
  	        @Override
  	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
  	       
  	        	Config.iSelQpName=pos;
  	        	getConfig().setSelQpName(pos);
  	        	Config.QpName=spSelQpName.getItemAtPosition(pos).toString();
  	        	getConfig().setQpName(Config.QpName);
  	        	if(pos==0)return;
  	        	String say="当前选择："+Config.QpName+"!注意：棋牌名称选择错误将导致透视失败！";
                  speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
  	        }
  	        @Override
  	        public void onNothingSelected(AdapterView<?> parent) {
  	            // Another interface callback
  	        }
  	    });
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
    	//人数选择：
       	rgNumMan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //获取变更后的选中项的ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //更新文本内容，以符合选中项
                     String sChecked=rb.getText().toString();
                     String say="";
                     if(sChecked.equals("二人棋牌")){
                     	getConfig().setNumMan(Config.NUM_MAN_TWO);
                     	Config.iNumMan=Config.NUM_MAN_TWO;
                     	say="当前选择：二人棋牌。";
                     }
                    if(sChecked.equals("三人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_THREE);
                    	Config.iNumMan=Config.NUM_MAN_THREE;
                    	say="当前选择：三人棋牌。";
                    }
                    if(sChecked.equals("四人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_FOUR);
                    	Config.iNumMan=Config.NUM_MAN_FOUR;
                    	say="当前选择：四人棋牌。";
                    }
                    if(sChecked.equals("五人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_FIVE);
                    	Config.iNumMan=Config.NUM_MAN_FIVE;
                    	say="当前选择：五人棋牌。";
                    }
                    if(sChecked.equals("六人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_SIX);
                    	Config.iNumMan=Config.NUM_MAN_SIX;
                    	say="当前选择：六人棋牌。";
                    }
                    if(sChecked.equals("七人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_SEVEN);
                    	Config.iNumMan=Config.NUM_MAN_SEVEN;
                    	say="当前选择：七人棋牌。";
                    }
                    if(sChecked.equals("八人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_EIGHT);
                    	Config.iNumMan=Config.NUM_MAN_EIGHT;
                    	say="当前选择：八人棋牌。";
                    }
                    if(sChecked.equals("十人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_TEN);
                    	Config.iNumMan=Config.NUM_MAN_TEN;
                    	say="当前选择：十人棋牌。";
                    }
                    if(sChecked.equals("十二人棋牌")){
                    	getConfig().setNumMan(Config.NUM_MAN_TWELVE);
                    	Config.iNumMan=Config.NUM_MAN_TWELVE;
                    	say="当前选择：十二人棋牌。";
                    }
                
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
      	 //自摸机率：
       	rgSelZimo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //获取变更后的选中项的ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //更新文本内容，以符合选中项
                     String sChecked=rb.getText().toString();
                     String say="";
                    if(sChecked.equals("自摸提高10%")){
                    	getConfig().setZimo(Config.ZI_MO_10);
                    	Config.iZimo=Config.ZI_MO_10;
                    	say="当前选择：自摸提高10%";
                    }
                    if(sChecked.equals("自摸提高20%")){
                    	getConfig().setZimo(Config.ZI_MO_20);
                    	Config.iZimo=Config.ZI_MO_20;
                    	say="当前选择：自摸提高20%";
                    }
                    if(sChecked.equals("自摸提高30%")){
                    	getConfig().setZimo(Config.ZI_MO_30);
                    	Config.iZimo=Config.ZI_MO_30;
                    	say="当前选择：自摸提高30%";
                    }
                    if(sChecked.equals("自摸提高40%")){
                    	getConfig().setZimo(Config.ZI_MO_40);
                    	Config.iZimo=Config.ZI_MO_40;
                    	say="当前选择：自摸提高40%";
                    }
                    if(sChecked.equals("自摸提高50%")){
                    	getConfig().setZimo(Config.ZI_MO_50);
                    	Config.iZimo=Config.ZI_MO_50;
                    	say="当前选择：自摸提高50%";
                    }
                    if(sChecked.equals("自摸提高60%")){
                    	getConfig().setZimo(Config.ZI_MO_60);
                    	Config.iZimo=Config.ZI_MO_60;
                    	say="当前选择：自摸提高60%";
                    }
                    if(sChecked.equals("自摸提高70%")){
                    	getConfig().setZimo(Config.ZI_MO_70);
                    	Config.iZimo=Config.ZI_MO_70;
                    	say="当前选择：自摸提高70%";
                    }
                    if(sChecked.equals("自摸提高80%")){
                    	getConfig().setZimo(Config.ZI_MO_80);
                    	Config.iZimo=Config.ZI_MO_80;
                    	say="当前选择：自摸提高80%";
                    }
                    if(sChecked.equals("自摸提高90%")){
                    	getConfig().setZimo(Config.ZI_MO_90);
                    	Config.iZimo=Config.ZI_MO_90;
                    	say="当前选择：自摸提高90%";
                    }
                
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
     	 //好牌机率：
       	rgSelHaopai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    //获取变更后的选中项的ID
                   int radioButtonId = arg0.getCheckedRadioButtonId();
                   //根据ID获取RadioButton的实例
                    RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                    //更新文本内容，以符合选中项
                    String sChecked=rb.getText().toString();
                    String say="";
                   if(sChecked.equals("好牌提高10%")){
                   	getConfig().setHaopai(Config.HAO_PAI_10);
                   	Config.iHaopai=Config.HAO_PAI_10;
                   	say="当前选择：好牌机率提高10%";
                   }
                   if(sChecked.equals("好牌提高20%")){
                      	getConfig().setHaopai(Config.HAO_PAI_20);
                      	Config.iHaopai=Config.HAO_PAI_20;
                      	say="当前选择：好牌机率提高20%";
                    }
                   if(sChecked.equals("好牌提高30%")){
                     	getConfig().setHaopai(Config.HAO_PAI_30);
                     	Config.iHaopai=Config.HAO_PAI_30;
                     	say="当前选择：好牌机率提高30%";
                   }
                   if(sChecked.equals("好牌提高40%")){
                     	getConfig().setHaopai(Config.HAO_PAI_40);
                     	Config.iHaopai=Config.HAO_PAI_40;
                     	say="当前选择：好牌机率提高40%";
                   }
                   if(sChecked.equals("好牌提高50%")){
                     	getConfig().setHaopai(Config.HAO_PAI_50);
                     	Config.iHaopai=Config.HAO_PAI_50;
                     	say="当前选择：好牌机率提高50%";
                   }
                   if(sChecked.equals("好牌提高60%")){
                     	getConfig().setHaopai(Config.HAO_PAI_60);
                     	Config.iHaopai=Config.HAO_PAI_60;
                     	say="当前选择：好牌机率提高60%";
                   }
                   if(sChecked.equals("好牌提高70%")){
                     	getConfig().setHaopai(Config.HAO_PAI_70);
                     	Config.iHaopai=Config.HAO_PAI_70;
                     	say="当前选择：好牌机率提高70%";
                   }
                   if(sChecked.equals("好牌提高80%")){
                     	getConfig().setHaopai(Config.HAO_PAI_80);
                     	Config.iHaopai=Config.HAO_PAI_80;
                     	say="当前选择：好牌机率提高80%";
                   }
                   if(sChecked.equals("好牌提高90%")){
                     	getConfig().setHaopai(Config.HAO_PAI_90);
                     	Config.iHaopai=Config.HAO_PAI_90;
                     	say="当前选择：好牌机率提高90%";
                   }
                   speaker.speak(say);
               	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
      	});
    	//5.设置牛牛总开关
    	swNn.setOnCheckedChangeListener(this);
    	swPerspection.setOnCheckedChangeListener(this);
    	swData.setOnCheckedChangeListener(this);
    	swHaoPai.setOnCheckedChangeListener(this);   	
    }
    /*
     * 读入配置参数到控件：
     */
    private void SetWidgets(){
    	//1.读入开关参数：
    	Config.bNn=true;
    	swNn.setChecked(Config.bNn);//1.软件启动时打开总开关：
    	swPerspection.setChecked(true);//透视开关打开；
    	swData.setChecked(true);//数据采集开关打开；
    	swHaoPai.setChecked(true);//提高好牌率打开；
    	//游戏选择：
	    Config.iSelGame=getConfig().getSelGame();
	    spSelGame.setSelection(Config.iSelGame);
	    // 建立数据源
	    //String[] mItems = Funcs.getFuncs(MainActivity.this).GetAppNames();
	    //List<String> mItems = Funcs.getFuncs(MainActivity.this).GetListAppNames();
	    List<String> mItems = AppInfo.getAppInfo(MainActivity.this).GetListAppNames();
	 // 建立Adapter并且绑定数据源
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
	    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice );//simple_spinner_dropdown_item
	    //绑定 Adapter到控件
	    spSelQpName.setAdapter(adapter);
	    //棋牌名称：
	    Config.QpName=getConfig().getQpName();//游戏名称;
	    //if(!Config.QpName.equals(""))Config.iSelQpName=Funcs.getFuncs(getApplicationContext()).GetListAppNamesIdx(Config.QpName);
	    //if(!Config.QpName.equals(""))Config.iSelQpName= AppInfo.getAppInfo(getApplicationContext()).GetListAppNamesIdx(Config.QpName);
	    if(Config.iSelQpName==-1||Config.iSelQpName==0)Config.iSelQpName=getConfig().getSelQpName();
	    spSelQpName.setSelection(Config.iSelQpName);
	    //玩家ID
	    Config.PlayerID=getConfig().getPlayerID();//玩家ID;
	    etGameID.setText(Config.PlayerID);
    	//人数选择：
    	Config.iNumMan=getConfig().getNumMan();
    	if(Config.iNumMan==Config.NUM_MAN_TWO){
    		rbNumManTwo.setChecked(true);//
    	}else if(Config.iNumMan==Config.NUM_MAN_THREE){
    		rbNumManThree.setChecked(true);//
    	}else if(Config.iNumMan==Config.NUM_MAN_FOUR){
    		rbNumManFour.setChecked(true);
    	}else if(Config.iNumMan==Config.NUM_MAN_FIVE){
    		rbNumManFive.setChecked(true);
    	}else if(Config.iNumMan==Config.NUM_MAN_SIX){
    		rbNumManSix.setChecked(true);
    	}else if(Config.iNumMan==Config.NUM_MAN_SEVEN){
    		rbNumManSeven.setChecked(true);
    	}else if(Config.iNumMan==Config.NUM_MAN_EIGHT){
    		rbNumManEight.setChecked(true);
    	}
    	//自摸机率 ：
    	Config.iZimo=getConfig().getZimo();
    	if(Config.iZimo==Config.ZI_MO_10){
    		rbZimo10.setChecked(true);//自摸机率10
    	}else if(Config.iZimo==Config.ZI_MO_20){
    		rbZimo20.setChecked(true);
    	}else if(Config.iZimo==Config.ZI_MO_30){
    		rbZimo30.setChecked(true);
    	}else if(Config.iZimo==Config.ZI_MO_40){
    		rbZimo40.setChecked(true);
    	}else if(Config.iZimo==Config.ZI_MO_50){
    		rbZimo50.setChecked(true);
    	}else if(Config.iZimo==Config.ZI_MO_60){
    		rbZimo60.setChecked(true);
    	}else if(Config.iZimo==Config.ZI_MO_70){
    		rbZimo70.setChecked(true);
    	}else if(Config.iZimo==Config.ZI_MO_80){
    		rbZimo80.setChecked(true);
    	}
    	//好牌机率 ：
    	Config.iHaopai=getConfig().getHaopai();
    	if(Config.iHaopai==Config.HAO_PAI_10){
    		rbHaopai10.setChecked(true);//自摸机率10
    	}else if(Config.iHaopai==Config.HAO_PAI_20){
    		rbHaopai20.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_30){
    		rbHaopai30.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_40){
    		rbHaopai40.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_50){
    		rbHaopai50.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_60){
    		rbHaopai60.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_70){
    		rbHaopai70.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_80){
    		rbHaopai80.setChecked(true);
    	}else if(Config.iHaopai==Config.HAO_PAI_90){
    		rbHaopai90.setChecked(true);
    	}
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
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    	String sShow="";
        switch (compoundButton.getId()){
            case R.id.swNn:
                if(compoundButton.isChecked()){
                	sShow="已打开总开关";
                }
                else {
                	sShow="已关闭总开关";
                }
                Config.bNn=compoundButton.isChecked();
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swPerspection:
                if(compoundButton.isChecked()){
                	sShow="打开透视功能";
                }
                else {
                	sShow="已关闭透视功能";
                }
                
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swData:
                if(compoundButton.isChecked()){
                	sShow="已打开数据采集功能，提高好牌概率更高！";
                }
                else {
                	sShow="已关闭数据采集功能";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swHaoPai:
                if(compoundButton.isChecked()){
                	sShow="已打开提高好牌率功能";
                }
                else {
                	sShow="已关闭提高好牌率功能";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;

        }
    }
    /*
     * 棋牌选择确认对话框：
     */
    private void showSelQpNameDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.ico);
        normalDialog.setTitle("本机棋牌名称选择确认");
        String say="当前选择"+Config.QpName+"，请确认是否是所玩棋牌游戏！警告：棋牌选择正确才能透视成功！";
        speaker.speak(say);
        normalDialog.setMessage(say);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	//mSelMajOK=true;
            	String say;
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					Log.d(TAG, "事件---->打开系统设置中辅助功能");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="找到棋牌专家，然后开启棋牌透视服务。";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say="棋牌透视服务已开启！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}
            }
        });
        normalDialog.setNegativeButton("关闭", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	//mSelMajOK=false;
            }
        });
        // 显示
        normalDialog.show();
    }
    @SuppressWarnings("deprecation")
	private void getResolution2(){
        WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        Config.screenWidth= display.getWidth();    
        Config.screenHeight= display.getHeight();  
        Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    //设置软件标题：
   public void setMyTitle(){
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
   /**显示版本信息*/
   public void showVerInfo(boolean bReg){
   	ConfigCt.bReg=bReg;
	if(Ad2.currentQQ!=null)Ad2.currentQQ.getADinterval();
	if(Ad2.currentWX!=null)Ad2.currentWX.getADinterval();
       if(bReg){
       	Config.bReg=true;
       	getConfig().setREG(true);
       	tvRegState.setText("授权状态：已授权");
       	tvRegWarm.setText("正版升级技术售后联系"+ConfigCt.contact);
       	etRegCode.setVisibility(View.INVISIBLE);
       	tvPlease.setVisibility(View.INVISIBLE);
       	btReg.setVisibility(View.INVISIBLE);
       	speaker.speak("欢迎使用"+ConfigCt.AppName+"！您是正版用户！" );
       	
       }else{
       	Config.bReg=false;
       	getConfig().setREG(false);
       	tvRegState.setText("授权状态：未授权");
       	tvRegWarm.setText(ConfigCt.warning+"技术及授权联系"+ConfigCt.contact);
       	etRegCode.setVisibility(View.VISIBLE);
       	tvPlease.setVisibility(View.VISIBLE);
       	btReg.setVisibility(View.VISIBLE);
       	speaker.speak("欢迎使用"+ConfigCt.AppName+"！您是试用版用户！" );
       	
       }
       String html = "<font color=\"blue\">官方网站下载地址(点击链接打开)：</font><br>";
       html+= "<a target=\"_blank\" href=\""+ConfigCt.homepage+"\"><font color=\"#FF0000\"><big><b>"+ConfigCt.homepage+"</b></big></font></a>";
       //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
       tvHomePage.setTextColor(Color.BLUE);
       tvHomePage.setBackgroundColor(Color.WHITE);//
       //tvHomePage.setTextSize(20);
       tvHomePage.setText(Html.fromHtml(html));
       tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
       setMyTitle();
       updateMeWarning(ConfigCt.version,ConfigCt.new_version);//软件更新提醒
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
   /** 置为试用版*/
   public void setAppToTestVersion() {
   	String sStartTestTime=getConfig().getStartTestTime();//取自动置为试用版的开始时间；
   	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
   	String currentDate =sdf.format(new Date());//取当前时间；
   	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//得到时间间隔；
   	if(timeInterval>Config.TestTimeInterval){//7天后置为试用版：
   		showVerInfo(false);
   		//ftp.getFtp().DownloadStart();//下载服务器信息;
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
       normalDialog.setTitle(  "计算棋牌数据提醒"  );
       normalDialog.setMessage(ConfigCt.AppName+"需要计算棋牌数据，才能透视成功！并且提高好牌率！此计算需要很长时间，请于晚上睡觉前运行计算任务！！运行计算任务时不要锁屏！手机处于充电状态！以免计算失败！"); 
       normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="请先找到"+ConfigCt.AppName+"，然后打开棋牌透视服务，才能计算棋牌数据！";
   				Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
   				speaker.speak(say);
   				QiangHongBaoService.startSetting(getApplicationContext());
   				return;
   			}
   			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
   				if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
   			}
   			CalcShow.getInstance(getApplicationContext()).showPic();
   			CalcShow.getInstance(getApplicationContext()).showTxt();
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
   /*
    * 添加联系人对话框
    * */
   private   void   showAddContactsDialog(){ 
       /* @setIcon 设置对话框图标 
        * @setTitle 设置对话框标题 
        * @setMessage 设置对话框消息提示 
        * setXXX方法返回Dialog对象，因此可以链式设置属性 
        */ 
	   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       //builder.setIcon(R.drawable.ic_launcher);
       builder.setIcon(ResourceUtil.getDimenId(getApplicationContext(), "ic_launcher"));
       builder.setTitle("请联系客服，激活软件");
       String say="请联系客服，激活软件!能透视，换牌！";
   	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
	    speaker.speak(say);
       //builder.setMessage(say);
       final Contacts  cs=Contacts.getInstance(getApplicationContext(),ConfigCt.contact);
       String k1="客服1(QQ："+cs.QQ+")";
       String k2="客服2(微信："+cs.wx+")";
       final String[] css = {k1,k2};
       //    设置一个单项选择下拉框
       /**
        * 第一个参数指定我们要显示的一组下拉单选框的数据集合
        * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
        * 第三个参数给每一个单选项绑定一个监听器
        */
       builder.setSingleChoiceItems(css, 0, new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface dialog, int which)
           {
        	   if(which==0){
        		   cs.openQQadd();
        	   }
        	   if(which==1){
        		   cs.openWXadd();
        	   }
               //Toast.makeText(MainActivity.this, "性别为：" + sex[which], Toast.LENGTH_SHORT).show();
           }
       });
       builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface dialog, int which)
           {
        	   if(which==0||which==-1){
        		   cs.openQQadd();
        	   }
        	   if(which==1){
        		   cs.openWXadd();
        	   }
           }
       });
       builder.setNegativeButton("试用", new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface dialog, int which)
           {
        	 //启动游戏并且打开悬浮窗口：
			    fw.ShowFloatingWindow();
			    //Config.QpPkg=Funcs.getFuncs(MainActivity.this).GetPkgName(Config.iSelQpName-1);
			    Config.QpPkg=AppInfo.getAppInfo(MainActivity.this).GetPkgName(Config.iSelQpName-1);
			    getConfig().setGamePkg(Config.QpPkg);
				OpenGame(Config.QpPkg,MainActivity.this);
				WechatAccessibilityJob.getJob().setPkgs(new String[]{Config.QpPkg});
				MainActivity.this.finish();
           }
       });
       builder.show();
   }
	 @Override
	    public void onBackPressed() {
	        //此处写退向后台的处理
	 	  // moveTaskToBack(true); 
	    }
	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
	            //此处写退向后台的处理
	     	   //moveTaskToBack(true);
	            //return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	    @Override
	    protected void onStop() {
	        // TODO Auto-generated method stub
	        super.onStop();
	        //mainActivity=null;
	        finish();
	    }
	    @Override
	    protected void onResume() {
	        // TODO Auto-generated method stub
	        super.onResume();
	        //if(!Utils.isActive)
	        //{
	            //Utils.isActive = true;
	            /*一些处理，如弹出密码输入界面*/
	        //}
	    }
	    @Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    	unregisterReceiver(qhbConnectReceiver);
	    	mBackgroundMusic.stopBackgroundMusic();
	        
	    }
		@Override
		protected void onNewIntent(Intent intent) {
		    super.onNewIntent(intent);
		    setIntent(intent);//must store the new intent unless getIntent() will return the old one
		    //startAllServices();
			Log.i(Config.TAG, "aa onNewIntent: 调用");  
		}
	 
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   public static String getLollipopRecentTask(Context context) {  
       final int PROCESS_STATE_TOP = 2;  
       try {  
           //通过反射获取私有成员变量processState，稍后需要判断该变量的值  
           Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");  
           List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(  
                   Context.ACTIVITY_SERVICE)).getRunningAppProcesses();  
           for (ActivityManager.RunningAppProcessInfo process : processes) {  
               //判断进程是否为前台进程  
               if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                   int state = processStateField.getInt(process);  
                   //processState值为2  
                   if (state == PROCESS_STATE_TOP) {  
                       String[] packname = process.pkgList;  
                       return packname[0];  
                   }  
               }  
           }  
       } catch (Exception e) {  
           e.printStackTrace();  
       }  
       return "";  
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
               //return info.processName;//返回包名
        	   Log.i("byc002", info.processName);
           Log.i("byc001", info.processName);
       }
       return "";
   }
   
   public static String getCurrentPkgName(Context context) {
	   ActivityManager.RunningAppProcessInfo currentInfo = null;
	   Field field = null;
	   int START_TASK_TO_FRONT = 2;
	   String pkgName = null;
	   try {
	   field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
	   } catch (Exception e) { e.printStackTrace(); }
	   ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	   List<RunningAppProcessInfo> appList = am.getRunningAppProcesses();
	   for (ActivityManager.RunningAppProcessInfo app : appList) {
	   if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
	   Integer state = null;
	   try {
	   state = field.getInt( app );
	   } catch (Exception e) { e.printStackTrace(); }
	   if (state != null && state == START_TASK_TO_FRONT) {
	   currentInfo = app;
	   break;
	   }
	   }
	   }
	   if (currentInfo != null) {
	   pkgName = currentInfo.processName;
	   }
	   //Log.i("byc001", pkgName);
	   return pkgName;
	   } 
   public static String get2(Context  context){
	   ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	   String _pkgName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
	   Log.i("byc001",_pkgName);
	   return  _pkgName;
   }
   public static String get3(Context  context){
	   String mPackageName="";
	   ActivityManager mActivityManager =(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	   if(Build.VERSION.SDK_INT > 20){
	       mPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
	   }else{ 
		   mPackageName =  mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
	   }
	   Log.i("byc003",mPackageName);
	
	   return mPackageName;
   }
   public  void  get4(Context context){
	// 获取图片、应用名、包名
		// 用来记录应用程序的信息
	List<AppsItemInfo> list;
	   PackageManager pManager = context.getPackageManager();
		List<PackageInfo> appList = getAllApps(context);

			list = new ArrayList<AppsItemInfo>();

			for (int i = 0; i < appList.size(); i++) {
				PackageInfo pinfo = appList.get(i);
				AppsItemInfo shareItem = new AppsItemInfo();
				// 设置图片
				shareItem.setIcon(pManager
						.getApplicationIcon(pinfo.applicationInfo));
				// 设置应用程序名字
				shareItem.setLabel(pManager.getApplicationLabel(
						pinfo.applicationInfo).toString());
				// 设置应用程序的包名
				shareItem.setPackageName(pinfo.applicationInfo.packageName);

				list.add(shareItem);
				Log.i(TAG, shareItem.getLabel());
				Log.i(TAG, shareItem.getPackageName());
			}

   }
			public static List<PackageInfo> getAllApps(Context context) {

				List<PackageInfo> apps = new ArrayList<PackageInfo>();
				PackageManager pManager = context.getPackageManager();
				// 获取手机内所有应用
				List<PackageInfo> packlist = pManager.getInstalledPackages(0);
				for (int i = 0; i < packlist.size(); i++) {
					PackageInfo pak = (PackageInfo) packlist.get(i);

					// 判断是否为非系统预装的应用程序
					// 这里还可以添加系统自带的，这里就先不添加了，如果有需要可以自己添加
					// if()里的值如果<=0则为自己装的程序，否则为系统工程自带
					if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
						// 添加自己已经安装的应用程序
						apps.add(pak);
					}

				}
				return apps;
			}
			// 自定义一个 AppsItemInfo 类，用来存储应用程序的相关信息
			private class AppsItemInfo {

				private Drawable icon; // 存放图片
				private String label; // 存放应用程序名
				private String packageName; // 存放应用程序包名

				public Drawable getIcon() {
					return icon;
				}

				public void setIcon(Drawable icon) {
					this.icon = icon;
				}

				public String getLabel() {
					return label;
				}

				public void setLabel(String label) {
					this.label = label;
				}

				public String getPackageName() {
					return packageName;
				}

				public void setPackageName(String packageName) {
					this.packageName = packageName;
				}

			}
		
}

