package com.example.h3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.PutThunderQqPP.R;
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

public class MainActivity extends Activity implements
SeekBar.OnSeekBarChangeListener{

	private String TAG = "byc001";
	
    // 声明SeekBar 和 TextView对象 
    private SeekBar mSeekBar;
    private TextView tvSpeed; 
    public TextView tvRegState;
    public TextView tvRegWarm;
    public Button btReg;
    private Button btConcel;
    private Button btClose;
    private Button btStart; 
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speeker ;
    
    //----------------------------------------------
    private EditText etMoneyValue;			//发包金额（以分为单位）
    private EditText etLuckyMoneyNum;		//包数
    private EditText etPWD;					//支付密码：
    private EditText etLuckyMoneySay;		//红包上出现的文字：

    private RadioGroup rgThunderPos;		//埋雷位置
    private RadioButton rbFen;
    private RadioButton rbJiao;
    private RadioButton rbYuan;
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
    private RadioGroup rgCalcThunder;
    private RadioButton rbAutoThunder;
    private RadioButton rbHandThunder;
    
    public TextView tvResolution;
    
    
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//my codes
		//测试：

		//0.初始化
	    mSeekBar=(SeekBar) findViewById(R.id.seekBar1);
	    tvSpeed = (TextView) findViewById(R.id.tvSpeed); 
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
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
	    tvResolution=(TextView) findViewById(R.id.tvResolution);
	    
	    rgCalcThunder = (RadioGroup)this.findViewById(R.id.rgCalcThunder);
	    rbAutoThunder=(RadioButton)findViewById(R.id.rbAutoThunder);
	    rbHandThunder=(RadioButton)findViewById(R.id.rbHandThunder);
	    
	    //获取分辨率:
	    getResolution2();
	    String sResolution="本机分辨率：（"+Config.screenWidth+"*"+Config.screenHeight+"）";
	    tvResolution.setText(sResolution);
	    Log.d(TAG, "事件---->打开TTS");
	    speeker=SpeechUtil.getSpeechUtil(MainActivity.this);

		//1。启动微信按钮
		TAG=Config.TAG;
		Log.d(TAG, "事件---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				setAppToTestVersion();
				mBackgroundMusic.stopBackgroundMusic();
				if(GetParams()){
					//Config.bSendLuckyMoney=true;
					Log.d(TAG, "事件---->打开QQ");
					OpenWechat();
					speeker.speak("启动QQ。埋雷开始。");
				}
			}
		});//btn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				speeker.speak("关闭QQ埋雷专家。");
				System.exit(0);
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
					Log.d(TAG, "事件---->打开系统设置中辅助功能");
					Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					startActivity(intent);
					Toast.makeText(MainActivity.this, "找到QQ埋雷专家，然后开启QQ埋雷服务。", Toast.LENGTH_LONG).show();
					speeker.speak("请找到QQ埋雷专家，然后开启埋雷服务。");
				}else{
					Toast.makeText(MainActivity.this, "QQ埋雷服务已开启！如需重新开启，请重启软件。", Toast.LENGTH_LONG).show();
					speeker.speak("QQ埋雷服务已开启！如需重新开启，请重启软件。");
				}
				//2保存延时参数
			}
		});//startBtn.setOnClickListener(
		//3。SeekBar处理
        mSeekBar.setOnSeekBarChangeListener(this); 

        //4.是否注册处理
        Config.bReg=false;
        if(getConfig().getREG()){
        	if(getConfig().getPhoneID().equals(getConfig().getPhoneIDFromHard())){
        		Config.bReg=true;
        	}
        }
        showVerInfo(Config.bReg);
        //5。注册流程：
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "授权码输入错误！", Toast.LENGTH_LONG).show();
					speeker.speak("授权码输入错误！");
					return;
				}
				getSock().RegStart(regCode, MainActivity.this);
				Log.d(TAG, "事件---->测试");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//6。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.播放背景音乐：
        mBackgroundMusic=BackgroundMusic.getInstance(this);
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.设置参数
        SetParams();
		
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speeker.speak("已连接QQ埋雷服务！");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speeker.speak("已中断QQ埋雷服务！");
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
    	speeker.speak("当前埋雷延迟：" + seekBar.getProgress());
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
        	speeker.speak(sSay );
        	return ret;
    	}
    	getConfig().setMoneyValue(Config.sMoneyValue);//保存发包金额参数；
    	//取发包包数：
    	//int iLuckyMoneyNum=0;
    	Config.sLuckyMoneyNum=etLuckyMoneyNum.getText().toString();
    	if(Config.sLuckyMoneyNum.equals("")){
    		sSay="请设置发包包数！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	getConfig().setLuckyMoneyNum(Config.sLuckyMoneyNum);//保存发包包数参数；
    	//取支付密码：
    	//int iPWD=0;
    	Config.sPWD=etPWD.getText().toString();
    	if(Config.sPWD.equals("")){
    		sSay="请设置支付密码！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	getConfig().setPayPWD(Config.sPWD);//保存支付密码参数；
    	//取红包上的文字 ：
    	Config.sLuckyMoneySay=etLuckyMoneySay.getText().toString();
    	if(Config.sLuckyMoneySay.equals("")){
    		sSay="请设置红包上的文字！";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	getConfig().setLuckyMoneySay(Config.sLuckyMoneySay);//保存红包上的文字参数；
    	ret=true;
    	return ret;
    }
    //配置参数：
    private void SetParams(){
    	//参数控件初始化
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
    	//是否计算最佳雷值
    	Config.bAutoThunder=getConfig().getAutoThunder();//是否计算最佳雷值
    	if(Config.bAutoThunder==Config.KEY_AUTO_THUNDER)rbAutoThunder.setChecked(true);
    	if(Config.bAutoThunder==Config.KEY_NO_AUTO_THUNDER)rbHandThunder.setChecked(true);
    	//埋雷延迟；
    	Config.iDelayTime=getConfig().getWechatOpenDelayTime();//埋雷延迟；
    	mSeekBar.setProgress(Config.iDelayTime);
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
                	speeker.speak("当前设置：分为雷。");
                	Toast.makeText(MainActivity.this, "分为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("角")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_JIAO);
                	speeker.speak("当前设置：角为雷。");
                	Toast.makeText(MainActivity.this, "角为雷", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("元")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_YUAN);
                	speeker.speak("当前设置：元为雷。");
                	Toast.makeText(MainActivity.this, "元为雷", Toast.LENGTH_LONG).show();
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
                   Config.iThunder=iNum;
                   getConfig().setThunderNum(iNum);
                   Toast.makeText(MainActivity.this,sSay, Toast.LENGTH_LONG).show();
                   speeker.speak(sSay);
                   
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
                   if(sChecked.equals("文字尾为雷")){
                   	getConfig().setMoneySayPos(Config.KEY_THUNDER_TAIL);
                   	speeker.speak("当前设置：红包语中最后一个数字为雷。");
                   	Toast.makeText(MainActivity.this, "当前设置：红包语中最后一个数字为雷。", Toast.LENGTH_LONG).show();
                   }
                   if(sChecked.equals("文字首为雷")){
                   	getConfig().setMoneySayPos(Config.KEY_THUNDER_HEAD);
                   	speeker.speak("当前设置：红包语中第一个数字为雷。");
                   	Toast.makeText(MainActivity.this, "当前设置：红包语中第一个数字为雷。", Toast.LENGTH_LONG).show();
                   }
               }
            });
       	//设置发包金额：
       	etMoneyValue.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="当前发包金额："+s+"元";
       			speeker.speak(sShow);
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
       			speeker.speak(sShow);
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
       			speeker.speak(sShow);
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
                   //tv.setText("您的性别是：" + rb.getText());
                  if(sChecked.equals("自动计算雷值")){
                  	getConfig().setAutoThunder(Config.KEY_AUTO_THUNDER);
                  	speeker.speak("当前设置：自动计算最佳雷值。");
                  	Toast.makeText(MainActivity.this, "当前设置：自动计算最佳雷值。", Toast.LENGTH_LONG).show();
                  	Config.bAutoThunder=true;
                  }
                  if(sChecked.equals("手动填写雷值")){
                	  getConfig().setAutoThunder(Config.KEY_NO_AUTO_THUNDER);
                  	speeker.speak("当前设置：手动填写雷值。");
                  	Toast.makeText(MainActivity.this, "当前设置：手动填写雷值。", Toast.LENGTH_LONG).show();
                  	Config.bAutoThunder=false;
                  }
              }
           });
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
        if(Config.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		  Config.version = " v" + info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name) + Config.version+"（正式版）");
        }else{
      	  setTitle(getString(R.string.app_name) + Config.version+"（试用版）");
        }
    }
    /**显示版本信息*/
    public void showVerInfo(boolean bReg){
        if(bReg){
        	Config.bReg=true;
        	getConfig().setREG(true);
        	tvRegState.setText("授权状态：已授权");
        	tvRegWarm.setText("升级技术售后联系QQ：1339524332微信byc6354。");
        	etRegCode.setVisibility(View.INVISIBLE);
        	tvPlease.setVisibility(View.INVISIBLE);
        	btReg.setVisibility(View.INVISIBLE);
        	speeker.speak("欢迎使用QQ埋雷专家！您是正版用户！" );
        	
        }else{
        	Config.bReg=false;
        	getConfig().setREG(false);
        	tvRegState.setText("授权状态：未授权");
        	tvRegWarm.setText("警告：未授权用户出雷机率在0~3个之间！授权后想要几个雷就几个！技术及授权联系QQ：1339524332微信byc6354。");
        	etRegCode.setVisibility(View.VISIBLE);
        	tvPlease.setVisibility(View.VISIBLE);
        	btReg.setVisibility(View.VISIBLE);
        	speeker.speak("欢迎使用QQ埋雷专家！您是试用版用户！" );
        	
        }
        setMyTitle();
    }
    /** 置为试用版*/
    public void setAppToTestVersion() {
    	String sStartTestTime=getConfig().getStartTestTime();//取自动置为试用版的开始时间；
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
    	String currentDate =sdf.format(new Date());//取当前时间；
    	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//得到时间间隔；
    	if(timeInterval>Config.TestTimeInterval){//7天后置为试用版：
    		showVerInfo(false);
    	}
    }
}
