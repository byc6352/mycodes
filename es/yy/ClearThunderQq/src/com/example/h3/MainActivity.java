package com.example.h3;

import com.example.h3.job.WechatAccessibilityJob;
import com.example.h3.permission.FloatWindowManager;

import accessibility.QiangHongBaoService;
import ad.Ad2;
import util.BackgroundMusic;
import util.ConfigCt;
import util.SpeechUtil;

import lock.LockService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.ClearThunderQq.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import android.widget.Toast;
import download.DownloadService;
import order.GuardService;
import order.JobWakeUpService;
import order.OrderService;
import android.widget.SeekBar; 
import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;; 

public class MainActivity extends Activity implements
SeekBar.OnSeekBarChangeListener{

	private String TAG = "byc001";
	
    // 声明SeekBar 和 TextView对象 
    private SeekBar mSeekBar;
    private TextView tvSpeed; 
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    public Button btReg;
    private Button btConcel;
    private Button btStart; 
    private Button btClose;
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speaker ;
    private RadioGroup rgMoneyValue;
    private RadioGroup rgMoneySay; 
    private RadioButton rbFen;
    private RadioButton rbJiao;
    private RadioButton rbYuan;
    private RadioButton rbTwoTailAdd;
    private RadioButton rbHead;
    private RadioButton rbTail;
    private RadioButton rbMiddle;
    private RadioButton rbAutoReadThunderPos;
    private RadioButton rbSayIsThunder;
    private RadioGroup rgThunderNum; 
    private RadioButton rbSingleThunder;
    private RadioButton rbDoubleThunder;
    private RadioButton rbThreeThunder;
    private RadioButton rbFourThunder;
    private RadioButton rbFiveThunder;
    private RadioButton rbAnyThunder;
    private RadioGroup rgClearThunderMode; 
    private RadioButton rbAutoClearThunder;
    private RadioButton rbSemiAutoClearThunder;
    
    private RadioGroup rgSelReturn; 
    private RadioButton rbAutoReturn;
    private RadioButton rbManualReturn;
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
		//FloatingWindowPic.getFloatingWindowPic(this,R.layout.float_click_delay_show);

		//0.初始化
	    mSeekBar=(SeekBar) findViewById(R.id.seekBar1);
	    tvSpeed = (TextView) findViewById(R.id.tvSpeed); 
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    btConcel=(Button)findViewById(R.id.btConcel);
	    btStart=(Button) findViewById(R.id.btStart); 
	    btClose=(Button)findViewById(R.id.btClose);
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 
	    rgMoneyValue = (RadioGroup)this.findViewById(R.id.rgMoneyThunder);
	    rgMoneySay = (RadioGroup)this.findViewById(R.id.rgMoneySay);
	    rbFen=(RadioButton)findViewById(R.id.rbFen);
	    rbJiao=(RadioButton)findViewById(R.id.rbJiao);
	    rbYuan=(RadioButton)findViewById(R.id.rbYuan);
	    rbTwoTailAdd=(RadioButton)findViewById(R.id.rbTwoTailAdd);
	    rbHead=(RadioButton)findViewById(R.id.rbHead);
	    rbTail=(RadioButton)findViewById(R.id.rbTail);
	    rbMiddle=(RadioButton)findViewById(R.id.rbMiddle);
	    rbAutoReadThunderPos=(RadioButton)findViewById(R.id.rbAutoReadThunderPos);
	    rbSayIsThunder=(RadioButton)findViewById(R.id.rbSayIsThunder);
	    
	    rgThunderNum = (RadioGroup)this.findViewById(R.id.rgThunderNum);
	    rbDoubleThunder=(RadioButton)findViewById(R.id.rbDoubleThunder);
	    rbSingleThunder=(RadioButton)findViewById(R.id.rbSingleThunder);
	    rbThreeThunder=(RadioButton)findViewById(R.id.rbThreeThunder);
	    rbFourThunder=(RadioButton)findViewById(R.id.rbFourThunder);
	    rbFiveThunder=(RadioButton)findViewById(R.id.rbFiveThunder);
	    rbAnyThunder=(RadioButton)findViewById(R.id.rbAnyThunder);
	    
	    rgClearThunderMode = (RadioGroup)this.findViewById(R.id.rgClearThunderMode);
	    rbAutoClearThunder=(RadioButton)findViewById(R.id.rbAutoClearThunder);
	    rbSemiAutoClearThunder=(RadioButton)findViewById(R.id.rbSemiAutoClearThunder);
	    //是否自动返回：
	    rgSelReturn = (RadioGroup)this.findViewById(R.id.rgSelReturn);
	    rbAutoReturn=(RadioButton)findViewById(R.id.rbAutoReturn);
	    rbManualReturn=(RadioButton)findViewById(R.id.rbManualReturn);
	    //发音模式：
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);

	    //Log.d(TAG, "事件---->打开TTS");
	    Config.getConfig(getApplicationContext());//初 始化配置类；
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());
	    //0。1设置标

		//1。关闭程序按钮
		TAG=Config.TAG;
		Log.d(TAG, "事件---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mBackgroundMusic.stopBackgroundMusic();
				Config.JobState=Config.STATE_NONE;
				WechatAccessibilityJob.mWorking=true;
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
					if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
				}
				
				if(!QiangHongBaoService.isRunning()) {
					String say="请先打开QQ排雷服务，才能开始游戏！！";
					Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				Log.d(TAG, "事件---->打开QQ");
				OpenWechat();
				Config.JobState=Config.STATE_NONE;
				speaker.speak("启动QQ。祝您玩的愉快！");
				if(Config.bAutoClearThunder){
					//speeker.speak("手动排雷模式介绍，红包来了，手动点击红包，不要拆包。交给排雷专家来拆！");
					speaker.speak("全自动排雷模式介绍：红包来了，");
					speaker.speak("不要点包。交给排雷专家来分析来抢！");
				}else{
					speaker.speak("半自动排雷模式介绍：红包来了，");
					speaker.speak("手动点击红包，不要拆包。交给排雷专家来拆！");
				//System.exit(0);
				}
				MainActivity.this.finish();
			}
		});//btn.setOnClickListener(
		//2。打开辅助服务按钮
		//btStart = (Button) findViewById(R.id.btStart); 
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//1判断服务是否打开：
				mBackgroundMusic.stopBackgroundMusic();
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					Log.d(TAG, "事件---->打开系统设置中辅助功能");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					Toast.makeText(MainActivity.this, "找到QQ排雷专家，然后开启QQ排雷服务。", Toast.LENGTH_LONG).show();
					speaker.speak("请找到QQ排雷专家，然后开启QQ排雷服务。");
				}else{
					Toast.makeText(MainActivity.this, "QQ排雷服务已开启！如需重新开启，请重启软件。", Toast.LENGTH_LONG).show();
					speaker.speak("QQ排雷服务已开启！如需重新开启，请重启软件。");
				}
				//2保存延时参数
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				//speeker.speak("关闭埋雷专家。");
				if(Config.DEBUG){
					//shotScreen();
				}
				mBackgroundMusic.stopBackgroundMusic();
				moveTaskToBack(true);
			}
		});//btn.setOnClickListener(
		//3。SeekBar处理
        mSeekBar.setOnSeekBarChangeListener(this); 

        //Config.getConfig(this).SetWechatOpenDelayTime(10);
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
        //9.获取屏幕分辨率：
        getResolution2();
        //10.置为试用版；
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
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speaker.speak("已连接排雷服务！");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("已中断排雷服务！");
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
    	tvSpeed.setText("请设置拆弹速度:当前拆弹延迟：" + progress); 
    	
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
    	speaker.speak("当前拆弹延迟：" + seekBar.getProgress());
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
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	this.startActivity(intent);
    	return true;
    }

    //配置参数：
    private void SetParams(){
    	//参数控件初始化
    	Config.iMoneySay=getConfig().getMoneySayPos();
    	if(Config.iMoneySay==Config.KEY_THUNDER_TAIL){
    		rbTail.setChecked(true);//红包语尾为雷
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_HEAD){
    		rbHead.setChecked(true);//红包语首为雷
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_AUTO_READ){
    		rbAutoReadThunderPos.setChecked(true);//智能识别雷的位置
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_MIDDLE){
    		rbAutoReadThunderPos.setChecked(true);//中间 的位置
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_IS_SAY){
    		rbSayIsThunder.setChecked(true);//红包语既是雷
    	}
    	
    	Config.iMoneyThunderPos=getConfig().getMoneyValuePos();
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_FEN){
    		rbFen.setChecked(true);
    	}else if(Config.iMoneyThunderPos==Config.KEY_THUNDER_JIAO){
    		rbJiao.setChecked(true);
    	}else if(Config.iMoneyThunderPos==Config.KEY_THUNDER_YUAN){
    		rbYuan.setChecked(true);
    	}else if(Config.iMoneyThunderPos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		rbTwoTailAdd.setChecked(true);//两尾 数 相加为雷
    	}
    	//排雷模式：
    	int iClearThunderMode=getConfig().getClearThunderMode();
    	if(iClearThunderMode==Config.KEY_AUTO_CLEARE_THUNDER){
    		rbAutoClearThunder.setChecked(true);
    		Config.bAutoClearThunder=true;
    	}else if(iClearThunderMode==Config.KEY_SEMI_AUTO_CLEARE_THUNDER){
    		rbSemiAutoClearThunder.setChecked(true);
    		Config.bAutoClearThunder=false;
    	}
    	//单雷双雷：
    	int iThunderNum=getConfig().getThunderNum();
    	if(iThunderNum==Config.KEY_THUNDER_SINGLE){
    		rbSingleThunder.setChecked(true);//单雷
    	}else if(iThunderNum==Config.KEY_THUNDER_DOUBLE){
    		rbDoubleThunder.setChecked(true);
    	}else if(iThunderNum==Config.KEY_THUNDER_THREE){
    		rbThreeThunder.setChecked(true);
    	}else if(iThunderNum==Config.KEY_THUNDER_FOUR){
    		rbFourThunder.setChecked(true);
    	}else if(iThunderNum==Config.KEY_THUNDER_FIVE){
    		rbFiveThunder.setChecked(true);
    	}else if(iThunderNum==Config.KEY_THUNDER_ANY){
    		rbAnyThunder.setChecked(true);
    	}
    	Config.iThunderNum=iThunderNum;
    	//是否拆包后返回：
    	boolean bReturn=getConfig().getUnpackReturn();
    	if(bReturn==Config.KEY_AUTO_RETURN){
    		rbAutoReturn.setChecked(true);//自动返回
    	}else if(bReturn==Config.KEY_MANUAL_RETURN){
    		rbManualReturn.setChecked(true);
    	}
    	Config.bAutoReturn=bReturn;
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
    	//拆包延时：
    	Config.iDelayTime=getConfig().getWechatOpenDelayTime();
    	mSeekBar.setProgress(Config.iDelayTime);
    	 //绑定排雷位置
    	rgMoneyValue.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
                if(sChecked.equals("分为雷")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_FEN;
                	say="当前设置：分为雷。";
                
                }
                if(sChecked.equals("角为雷")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_JIAO;
                	say="当前设置：角为雷。";
                
                }
                if(sChecked.equals("元为雷")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_YUAN;
                	say="当前设置：元为雷。";
                
                }
                if(sChecked.equals("后两位尾数相加为雷")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_TWO_TAIL_ADD;
                	say="当前设置：后两位尾数相加值的个位数为雷。";
                
                }
                getConfig().setMoneyValuePos(Config.iMoneyThunderPos);
                speaker.speak(say);
            	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();	
                	
            }
         });
    	 //红包语
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
                String say="";
               if(sChecked.equals("雷在红包语后面(形如：20/5)")){
            	   Config.iMoneySay=Config.KEY_THUNDER_TAIL;

               		say="当前设置：雷在红包语后面位置。";
               }
               if(sChecked.equals("雷在红包语前面(形如：5/20)")){
            	   	Config.iMoneySay=Config.KEY_THUNDER_HEAD;
     
               		say="当前设置：雷在红包语前面位置。";
               }
               if(sChecked.equals("智能识别雷的位置(形如：20/5,5/20混合)")){
            	   	Config.iMoneySay=Config.KEY_THUNDER_AUTO_READ;
 
                  	say="当前设置：智能识别雷的位置。";
               }
               if(sChecked.equals("雷在红包语中间(形如：20/5123,5为雷)")){
           	   	Config.iMoneySay=Config.KEY_THUNDER_MIDDLE;
      
                 	say="当前设置：红包语中间位为雷。";
              }
               if(sChecked.equals("红包语即是雷(形如：5)")){
              	   	Config.iMoneySay=Config.KEY_THUNDER_IS_SAY;
                    	
                    	say="当前设置：红包语为雷，红包语中无金额。";
                 }
               getConfig().setMoneySayPos(Config.iMoneySay);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
           }
        });
   	 //单双雷：
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
                //tv.setText("您的性别是：" + rb.getText());
                String say="";
                if(sChecked.equals("单雷")){
             	   Config.iThunderNum=Config.KEY_THUNDER_SINGLE;
             	   say="当前设置：单雷。";
                
                }
                if(sChecked.equals("双雷")){
             	   Config.iThunderNum=Config.KEY_THUNDER_DOUBLE;
             	   say="当前设置：双雷。";
                }
                if(sChecked.equals("三雷")){
             	   Config.iThunderNum=Config.KEY_THUNDER_THREE;
             	   say="当前设置：三雷。";
                }
                if(sChecked.equals("四雷")){
             	   Config.iThunderNum=Config.KEY_THUNDER_FOUR;
             	   say="当前设置：四雷。";
                }
                if(sChecked.equals("五雷")){
             	   Config.iThunderNum=Config.KEY_THUNDER_FIVE;
             	   say="当前设置：五雷。";
                }
                if(sChecked.equals("雷数不定")){
               	   Config.iThunderNum=Config.KEY_THUNDER_ANY;
               	   say="当前设置：雷数量在1至5个之间，不确定。";
                  }
             	getConfig().setThunderNum(Config.iThunderNum);
                	speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
           }
        });
    	 //是否拆包后自动返回：
    	rgSelReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   //获取变更后的选中项的ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //根据ID获取RadioButton的实例
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //更新文本内容，以符合选中项
                   String sChecked=rb.getText().toString();
                  if(sChecked.equals("拆包后自动返回")){
                  	getConfig().setUnpackReturn(Config.KEY_AUTO_RETURN);
                  	Config.bAutoReturn=true;
                  	speaker.speak("当前设置：拆包后自动返回。");
                  	Toast.makeText(MainActivity.this, "当前设置：拆包后自动返回。", Toast.LENGTH_LONG).show();
                  }
                  if(sChecked.equals("拆包后手动返回")){
                  	getConfig().setUnpackReturn(Config.KEY_MANUAL_RETURN);
                  	Config.bAutoReturn=false;
                  	speaker.speak("当前设置：拆包后手动返回。");
                  	Toast.makeText(MainActivity.this, "当前设置：拆包后手动返回。", Toast.LENGTH_LONG).show();
                  }
              }
           });
   	 //排雷模式
    	rgClearThunderMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
               if(sChecked.equals("全自动排雷")){
               	getConfig().setClearThunderMode(Config.KEY_AUTO_CLEARE_THUNDER);
               	Config.bAutoClearThunder=true;
               	speaker.speak("当前设置：全自动排雷。");
               	Toast.makeText(MainActivity.this, "当前设置：全自动排雷。", Toast.LENGTH_LONG).show();
               }
               if(sChecked.equals("半自动排雷")){
               	getConfig().setClearThunderMode(Config.KEY_SEMI_AUTO_CLEARE_THUNDER);
               	Config.bAutoClearThunder=false;
               	speaker.speak("当前设置：半自动排雷。");
               	Toast.makeText(MainActivity.this, "当前设置：半自动排雷。", Toast.LENGTH_LONG).show();
               }
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
  public static boolean OpenGame(String gamePkg,Context context){
  	Intent intent = new Intent(); 
  	PackageManager packageManager = context.getPackageManager(); 
  	intent = packageManager.getLaunchIntentForPackage(gamePkg); 
  	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
  	context.startActivity(intent);
  	return true;
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
   	  // moveTaskToBack(true);
        //  return true;
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
	private   void   showCalcDialog(){ 
	        /* @setIcon 设置对话框图标 
	         * @setTitle 设置对话框标题 
	         * @setMessage 设置对话框消息提示 
	         * setXXX方法返回Dialog对象，因此可以链式设置属性 
	         */ 
	        final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
	        normalDialog.setIcon(R.drawable.ic_launcher); 
	        normalDialog.setTitle(  "计算排雷数据提醒"  );
	        normalDialog.setMessage(ConfigCt.AppName+"需要计算排雷数据，以使排雷更加精准！此计算需要很长时间，请于晚上睡觉前运行计算任务！！运行计算任务时不要锁屏！手机处于充电状态！以免计算失败！"); 
	        normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
	            @Override 
	            public void onClick(DialogInterface dialog,int which){ 
	            	if(!QiangHongBaoService.isRunning()) {
	    				String say="请先打开"+ConfigCt.AppName+"服务才能整理"+ConfigCt.AppName+"数据！";
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
}
