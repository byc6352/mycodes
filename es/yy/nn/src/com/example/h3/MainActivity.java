package com.example.h3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.h3.permission.FloatWindowManager;
import util.SpeechUtil;
import com.example.nn.R;

import accessibility.QiangHongBaoService;
import ad.Ad2;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
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
import android.net.Uri;
import android.widget.Toast; 
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import download.DownloadService;
import lock.LockService;
import order.GuardService;
import order.JobWakeUpService;
import order.OrderService;
import util.BackgroundMusic;
import util.ConfigCt;
import util.Funcs;

public class MainActivity extends Activity implements
SeekBar.OnSeekBarChangeListener,CompoundButton.OnCheckedChangeListener{

	private String TAG = "byc001";
	
    // ????SeekBar ?? TextView???? 
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
    
    private Switch swNn;
    private Switch swAutoRob;
    private Switch swData;
    private Switch swAllPos;
    private RadioGroup rgNn;
    //private RadioGroup rgMoneySay; 
    private RadioButton rbSingle;
    private RadioButton rbDouble;
    private RadioButton rbThree;
    private RadioButton rbHeaderTail;
    private RadioButton rbPaiJiu;
    private RadioButton rbSanGong;
    
    private CheckBox ckN1;
    private CheckBox ckN2;
    private CheckBox ckN3;
    private CheckBox ckN4;
    private CheckBox ckN5;
    private CheckBox ckN6;
    private CheckBox ckN7;
    private CheckBox ckN8;
    private CheckBox ckN9;
    private CheckBox ckNn;
    private CheckBox ckDouble;
    private CheckBox ckOrder;
    private CheckBox ckJn;
    private CheckBox ckMn;
    private CheckBox ckBaozi;
    //????ģʽ??
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
		//???ԣ?

		//0.??ʼ??
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

	    swNn=(Switch)findViewById(R.id.swNn); //ţţ????
	    swAutoRob=(Switch)findViewById(R.id.swAutoRob); //?Զ?????????
	    swData=(Switch)findViewById(R.id.swData); //???ݲɼ?????
	    swAllPos=(Switch)findViewById(R.id.swAllPos); //????λ????????
	    rgNn = (RadioGroup)this.findViewById(R.id.rgNn);
	    rbSingle=(RadioButton)findViewById(R.id.rbSingle);
	    rbDouble=(RadioButton)findViewById(R.id.rbDouble);
	    rbThree=(RadioButton)findViewById(R.id.rbThree);
	    rbHeaderTail=(RadioButton)findViewById(R.id.rbHeaderTail);
	    rbPaiJiu=(RadioButton)findViewById(R.id.rbPaiJiu);
	    rbSanGong=(RadioButton)findViewById(R.id.rbSanGong);

	    //????ģʽ??
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);
	    Log.d(TAG, "?¼?---->????TTS");
	    Config.getConfig(getApplicationContext());//?? ʼ???????ࣻ
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());

		//1???رճ?????ť
		TAG=Config.TAG;
		Log.d(TAG, "?¼?---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
					if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
				}
				if(!QiangHongBaoService.isRunning()) {
					String say="???ȴ???΢??ţţ???񣬲??ܿ?ʼ??Ϸ????";
					Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				mBackgroundMusic.stopBackgroundMusic();
				Log.d(TAG, "?¼?---->????΢??");
				OpenWechat();
				speaker.speak("????΢?š?ף?????????죡");
				MainActivity.this.finish();
				//speeker.speak("?м???ţţ???ܣ????????ˣ?");
				//speeker.speak("?ֶ?????????????Ҫ???????????м???ţţ????????????");
				//System.exit(0);
			}
		});//btn.setOnClickListener(
		//2???򿪸?????????ť
		//btStart = (Button) findViewById(R.id.btStart); 
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//1?жϷ????Ƿ??򿪣?
				mBackgroundMusic.stopBackgroundMusic();
				/*
				if(!getConfig().getREG()){
					Toast.makeText(MainActivity.this, "?빺?????棬???ܿ????м???ţţ??????", Toast.LENGTH_LONG).show();
					speaker.speak("?빺?????棬???ܿ????м???ţţ??????");
					return;
				}
				*/
				
				if(!QiangHongBaoService.isRunning()) {
					//????ϵͳ?????и???????
					Log.d(TAG, "?¼?---->????ϵͳ?????и???????");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					Toast.makeText(MainActivity.this, "?ҵ??м?ţţ??Ȼ???????м???ţţ??????", Toast.LENGTH_LONG).show();
					speaker.speak("???ҵ??м?ţţ??Ȼ???????м???ţţ??????");
				}else{
					Toast.makeText(MainActivity.this, "?м???ţţ?????ѿ????????????¿?????????????????", Toast.LENGTH_LONG).show();
					speaker.speak("?м???ţţ?????ѿ????????????¿?????????????????");
				}
				//2??????ʱ????
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(Config.DEBUG){
					//QHBNotificationService.openNotificationServiceSettings(MainActivity.this);
				}
				mBackgroundMusic.stopBackgroundMusic();
				//moveTaskToBack(true);
				MainActivity.this.finish();
			}
		});//btn.setOnClickListener(

		//3??SeekBar????
        mSeekBar.setOnSeekBarChangeListener(this); 

        Config.getConfig(this).SetWechatOpenDelayTime(10);
        //4.?Ƿ?ע?ᴦ??????ʾ?汾??Ϣ(????????)??
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//??ʼ????????֤??
			Sock.getSock(this).VarifyStart();
   
        //5??ע?????̣?
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "??Ȩ????????????", Toast.LENGTH_LONG).show();
					speaker.speak("??Ȩ????????????");
					return;
				}
				getSock().RegStart(regCode);
				//Log.d(TAG, "?¼?---->????");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//6?????չ㲥??Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);

        registerReceiver(qhbConnectReceiver, filter);
        //7.???ű??????֣?
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.???ò???
        SetParams();

        //10????ȡ??Ļ?ֱ??ʣ?
        getResolution2();
        //11.??Ϊ???ð棻
        setAppToTestVersion();
        //12??ROOT
        //RootShellCmd.getRootShellCmd().initShellCmd();

	}

	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speaker.speak("??????ţţ??????");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("???ж?ţţ??????");
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
		if (id == R.id.action_floatwindow) {
			 if(FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))
				 Toast.makeText(MainActivity.this, "????????????Ȩ?ޣ?", Toast.LENGTH_LONG).show();
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
		if (id == R.id.action_calculate) {
			showCalcDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	
	//SeekBar?ӿڣ?
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, 
            boolean fromUser) {// ???϶???--??ֵ?ڸı? 
        // progressΪ??ǰ??ֵ?Ĵ?С 
    	tvSpeed.setText("?????þ????ٶ?:??ǰ?????ʣ???" + progress); 
    	
    } 
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {// ???϶??л????ô˷??? 
    	//mSpeed.setText("xh???ڵ???"); 
    } 
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {// ֹͣ?϶? 
    	//mSpeed.setText("xhֹͣ????"); 
    	//???浱ǰֵ
    	Config.getConfig(this).SetWechatOpenDelayTime(seekBar.getProgress());
    	speaker.speak("??ǰ?????ʣ?" + seekBar.getProgress());
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

    //???ò?????
    
    private void SetParams(){
    	//?????ؼ???ʼ??
    	//????ģʽ??
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//?Զ?????
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
    	//1.????????ʱ????ţţ???أ?
    	Config.bNn=true;
    	swNn.setChecked(true);
    	swAutoRob.setChecked(true);
    	Config.bAuto=true;
    	swData.setChecked(true);
    	swAllPos.setChecked(true);
    	//2.?????淨????
    	int iWangFa=getConfig().getNnWangFa();
    	switch (iWangFa) {
        	case Config.NN_SINGLE:
        		rbSingle.setChecked(true);
        		break;
        	case Config.NN_DOUBLE:
        		rbDouble.setChecked(true);
        		break;
        	case Config.NN_THREE:
        		rbThree.setChecked(true);
        		break;
        	case Config.NN_HT:
        		rbHeaderTail.setChecked(true);
        		break;
        	case Config.NN_PG:
        		rbPaiJiu.setChecked(true);
        		break;
        	case Config.NN_SG:
        		rbSanGong.setChecked(true);
        		break;
    	}
    	//3.????ţţ?淨
    	rgNn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //??ȡ????????ѡ??????ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //????ID??ȡRadioButton??ʵ??
               RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //?????ı????ݣ??Է???ѡ????
               String sChecked=rb.getText().toString();
                //tv.setText("?????Ա??ǣ?" + rb.getText());
               if(sChecked.equals("??βţţ?淨")){
               	getConfig().setNnWangFa(Config.NN_SINGLE);
               	speaker.speak("??ǰ???ã???βţţ?淨??");
               	Toast.makeText(MainActivity.this, "??ǰ???ã???βţţ?淨??", Toast.LENGTH_LONG).show();
               }
               if(sChecked.equals("˫βţţ?淨")){
               	getConfig().setNnWangFa(Config.NN_DOUBLE);
               	speaker.speak("??ǰ???ã?˫βţţ?淨??");
               	Toast.makeText(MainActivity.this, "??ǰ???ã?˫βţţ?淨??", Toast.LENGTH_LONG).show();
               }
               if(sChecked.equals("??βţţ?淨")){
               	getConfig().setNnWangFa(Config.NN_THREE);
               	speaker.speak("??ǰ???ã???βţţ?淨??");
               	Toast.makeText(MainActivity.this, "??ǰ???ã???βţţ?淨??", Toast.LENGTH_LONG).show();
               }
               if(sChecked.equals("??β????ţţ?淨")){
                  	getConfig().setNnWangFa(Config.NN_HT);
                  	speaker.speak("??ǰ???ã???β????ţţ?淨??");
                  	Toast.makeText(MainActivity.this, "??ǰ???ã???βţţ?淨??", Toast.LENGTH_LONG).show();
               } 
               if(sChecked.equals("?ƾ??淨")){
                  	getConfig().setNnWangFa(Config.NN_PG);
                  	speaker.speak("??ǰ???ã??ƾ??淨??");
                  	Toast.makeText(MainActivity.this, "??ǰ???ã??ƾ??淨??", Toast.LENGTH_LONG).show();
              }
               if(sChecked.equals("?????淨")){
                 	getConfig().setNnWangFa(Config.NN_SG);
                 	speaker.speak("??ǰ???ã??????淨??");
                 	Toast.makeText(MainActivity.this, "??ǰ???ã??????淨??", Toast.LENGTH_LONG).show();
             }
           }
        });
    	//3.????ţţ?ܿ???
    	swNn.setOnCheckedChangeListener(this);
    	swAutoRob.setOnCheckedChangeListener(this);
    	swData.setOnCheckedChangeListener(this);
    	swAllPos.setOnCheckedChangeListener(this);
    	 //???? ģʽ
    	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //??ȡ????????ѡ??????ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //????ID??ȡRadioButton??ʵ??
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //?????ı????ݣ??Է???ѡ????
                String sChecked=rb.getText().toString();
                String say="";
               if(sChecked.equals("?ر???????ʾ")){
            	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
               		say="??ǰ???ã??ر???????ʾ??";
               }
               if(sChecked.equals("Ů??")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
               		say="??ǰ???ã?Ů????ʾ??";
               }
               if(sChecked.equals("????")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_MALE;
               		say="??ǰ???ã???????ʾ??";
               }
               if(sChecked.equals("?ر?????")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
               		say="??ǰ???ã??ر???????ʾ??";
               }
               if(sChecked.equals("????????")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
               		say="??ǰ???ã???????????ʾ??";
               }
               if(sChecked.equals("???ж?ͯ??")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
               		say="??ǰ???ã????ж?ͯ????ʾ??";
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
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    	String sShow="";
        switch (compoundButton.getId()){
            case R.id.swNn:
                if(compoundButton.isChecked()){
                	sShow="?Ѵ???ţţ????";
                }
                else {
                	sShow="?ѹر?ţţ????";
                }
                Config.bNn=compoundButton.isChecked();
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swAutoRob:
                if(compoundButton.isChecked()){
                	sShow="?Զ?????ģʽ????";
                }
                else {
                	sShow="?ѹر??Զ?????ģʽ";
                }
                Config.bAuto=compoundButton.isChecked();
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swData:
                if(compoundButton.isChecked()){
                	sShow="?Ѵ??????ݲɼ????ܣ?????ţţ???ʸ??Ӿ?׼??";
                }
                else {
                	sShow="?ѹر????ݲɼ?????";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swAllPos:
                if(compoundButton.isChecked()){
                	sShow="?Ѵ???????λ????????";
                }
                else {
                	sShow="?ѹر?????λ????????";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;

        }
    }
    @SuppressWarnings("deprecation")
	private void getResolution2(){
        WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        Config.screenWidth= display.getWidth();    
        Config.screenHeight= display.getHeight();  
        Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    //???????????⣺
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
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"????ʽ?棩");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"?????ð棩");
        }
    }
   /**??ʾ?汾??Ϣ*/
   public void showVerInfo(boolean bReg){
   	ConfigCt.bReg=bReg;
	if(Ad2.currentQQ!=null)Ad2.currentQQ.getADinterval();
	if(Ad2.currentWX!=null)Ad2.currentWX.getADinterval();
       if(bReg){
       	Config.bReg=true;
       	getConfig().setREG(true);
       	tvRegState.setText("??Ȩ״̬??????Ȩ");
       	tvRegWarm.setText("?????????????ۺ???ϵ"+ConfigCt.contact);
       	etRegCode.setVisibility(View.INVISIBLE);
       	tvPlease.setVisibility(View.INVISIBLE);
       	btReg.setVisibility(View.INVISIBLE);
       	speaker.speak("??ӭʹ??"+ConfigCt.AppName+"???????????û???" );
       	
       }else{
       	Config.bReg=false;
       	getConfig().setREG(false);
       	tvRegState.setText("??Ȩ״̬??δ??Ȩ");
       	tvRegWarm.setText(ConfigCt.warning+"????????Ȩ??ϵ"+ConfigCt.contact);
       	etRegCode.setVisibility(View.VISIBLE);
       	tvPlease.setVisibility(View.VISIBLE);
       	btReg.setVisibility(View.VISIBLE);
       	speaker.speak("??ӭʹ??"+ConfigCt.AppName+"?????????ð??û???" );
       	
       }
       String html = "<font color=\"blue\">?ٷ???վ???ص?ַ(???????Ӵ???)??</font><br>";
       html+= "<a target=\"_blank\" href=\""+ConfigCt.homepage+"\"><font color=\"#FF0000\"><big><b>"+ConfigCt.homepage+"</b></big></font></a>";
       //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
       tvHomePage.setTextColor(Color.BLUE);
       tvHomePage.setBackgroundColor(Color.WHITE);//
       //tvHomePage.setTextSize(20);
       tvHomePage.setText(Html.fromHtml(html));
       tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
       setMyTitle();
       updateMeWarning(ConfigCt.version,ConfigCt.new_version);//????????????
   }
   /**  ????????????*/
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
   /** ??Ϊ???ð?*/
   public void setAppToTestVersion() {
   	String sStartTestTime=getConfig().getStartTestTime();//ȡ?Զ???Ϊ???ð??Ŀ?ʼʱ?䣻
   	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
   	String currentDate =sdf.format(new Date());//ȡ??ǰʱ?䣻
   	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//?õ?ʱ????????
   	if(timeInterval>Config.TestTimeInterval){//7??????Ϊ???ð棺
   		showVerInfo(false);
   		//ftp.getFtp().DownloadStart();//???ط???????Ϣ;
   	}
   }
   private   void   showUpdateDialog(){ 
       /* @setIcon ???öԻ???ͼ?? 
        * @setTitle ???öԻ??????? 
        * @setMessage ???öԻ?????Ϣ??ʾ 
        * setXXX????????Dialog?????????˿?????ʽ???????? 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "????????"  );
       normalDialog.setMessage("???°????????Ƿ???????????"); 
       normalDialog.setPositiveButton("ȷ??",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
               //...To-do
    		   Uri uri = Uri.parse(ConfigCt.download);    
    		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
    		   startActivity(it);  
           }
       }); 
       normalDialog.setNegativeButton("?ر?",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ??ʾ 
       normalDialog.show(); 
   } 
   private   void   showCalcDialog(){ 
       /* @setIcon ???öԻ???ͼ?? 
        * @setTitle ???öԻ??????? 
        * @setMessage ???öԻ?????Ϣ??ʾ 
        * setXXX????????Dialog?????????˿?????ʽ???????? 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "????ţţ????????"  );
       normalDialog.setMessage(ConfigCt.AppName+"??Ҫ????ţţ???ݣ???ʹ??ţţ???Ӿ?׼???˼?????Ҫ?ܳ?ʱ?䣬????????˯??ǰ???м??????񣡣????м???????ʱ??Ҫ???????ֻ????ڳ???״̬??????????ʧ?ܣ?"); 
       normalDialog.setPositiveButton("ȷ??",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="?????ҵ?"+ConfigCt.AppName+"??Ȼ??????ţţ???񣬲??ܼ???ţţ???ݣ?";
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
       normalDialog.setNegativeButton("?ر?",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ??ʾ 
       normalDialog.show(); 
   } 
   @Override
   public void onBackPressed() {
       //?˴?д??????̨?Ĵ???
	 //  moveTaskToBack(true); 
   }
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {//???????ؼ?????
           //?˴?д??????̨?Ĵ???
    	   //moveTaskToBack(true);
          // return true;
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
           /*һЩ???????絯??????????????*/
       //}
   }
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "nn onNewIntent: ????");  
	}
 
	   @Override
	   protected void onDestroy() {
		   super.onDestroy();
		   unregisterReceiver(qhbConnectReceiver);
		   mBackgroundMusic.stopBackgroundMusic();
	   }
}

