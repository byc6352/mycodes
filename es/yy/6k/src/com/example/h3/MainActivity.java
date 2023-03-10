package com.example.h3;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import util.AppUtils;
import util.BackgroundMusic;
import util.ConfigCt;
import util.Funcs;

import util.SpeechUtil;



import com.example.h3.permission.FloatWindowManager;
import com.example.kk.R;

import accessibility.QiangHongBaoService;
import accessibility.app.WechatInfo;
import ad.Ad2;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
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
import android.widget.CompoundButton;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.widget.Toast;
import download.DownloadService;
import download.ftp;
import download.install.InstallApp;
import lock.LockService;
import order.GuardService;
import order.JobWakeUpService;
import order.OrderService;

import order.screen.ScreenShotActivity;
import order.screen.Shotter;
import sms.SmsReceiver;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;; 

public class MainActivity extends Activity implements
SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener{

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
    //-----------------------------------------------
    private Switch swLargeSmall;//????????????
    private Switch swTail;//????????????
    private Switch swSec;		//????
    private Switch swNoSmall;		//????????
    private Switch swNoSecSmall;		//????????
    private Switch swMiddle;		//????????
    private Switch swValue;		//????????
    private Switch swDoubleTail;		//????????
    private Switch swNoSmall_Tail;		//????????
    private Switch swMiddle_Tail;		//????????
    private Switch swValue_Tail;		//????????
    private Switch swBaoZi;		//????????
    private Switch swGuardAccount;		//??????
    private Switch swPerspact;		//????????
    
    boolean bSwitchLargeSmall=true;
    boolean bSwitchTail=false;
    boolean bSwitchSec=false;

    private RadioGroup rgRobMode; //????????
    private RadioButton rbAutoRob;//??????????
    private RadioButton rbSemiAutoRob;//??????????
    
    private RadioGroup rgSelReturn; 
    private RadioButton rbAutoReturn;
    private RadioButton rbManualReturn;
    //??????????
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;

    
    //private InstallApp install;
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		TAG=Config.TAG;

		//install=InstallApp.getInstallApp(this);
		//1??Config??????
		Config.getConfig(getApplicationContext());//?? ????????????
		speaker=SpeechUtil.getSpeechUtil(getApplicationContext()); 
        //2????????????
        getResolution2();
		//3.??????????
		SetParams();
        Config.getConfig(this).SetWechatOpenDelayTime(10);
        //4.??????????????????????????(????????)??
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//????????????????
			Sock.getSock(MainActivity.this).VarifyStart();
		//5??????????????
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(qhbConnectReceiver, filter);
       
        //6.??????????????
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //7.????????????
        setAppToTestVersion();
     

	}
 
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speaker.speak("??????????????????");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("??????????????????");
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
            	//speeker.speak("????????????????");
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {
            	//speeker.speak("????????????????");
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
				 Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
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
	
	//SeekBar??????
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, 
            boolean fromUser) {// ????????--?????????? 
        // progress???????????????? 
    	tvSpeed.setText("????????????????:??????????????" + progress); 
    	
    } 
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {// ???????????????????? 
    	//mSpeed.setText("xh????????"); 
    } 
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {// ???????? 
    	//mSpeed.setText("xh????????"); 
    	//??????????
    	Config.getConfig(this).SetWechatOpenDelayTime(seekBar.getProgress());
    	speaker.speak("??????????????" + seekBar.getProgress());
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
    public void RunApp(String pkgName,String mainClass) { 
    	Intent intent = new Intent(Intent.ACTION_MAIN);   
    	intent.addCategory(Intent.CATEGORY_LAUNCHER);               
    	ComponentName cn = new ComponentName(pkgName, mainClass);               
    	intent.setComponent(cn);   
    	startActivity(intent);
    	return;
    }
    //??????????
    private void SetParams(){
    	//??????????????
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
	    int a1=R.id.tvPlease;
	    int a2=this.getResources().getIdentifier("tvPlease", "id", this.getPackageName());
	    Toast.makeText(MainActivity.this, "a1:"+a1+"    a2:"+a2, Toast.LENGTH_LONG).show();
	    rgRobMode = (RadioGroup)this.findViewById(R.id.rgRobMode);
	    rbAutoRob=(RadioButton)findViewById(R.id.rbAutoRob);
	    rbSemiAutoRob=(RadioButton)findViewById(R.id.rbSemiAutoRob);
	    
	    //??????????????
	    rgSelReturn = (RadioGroup)this.findViewById(R.id.rgSelReturn);
	    rbAutoReturn=(RadioButton)findViewById(R.id.rbAutoReturn);
	    rbManualReturn=(RadioButton)findViewById(R.id.rbManualReturn);


	    //??????????
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);
	    //-----------------------------------------------------
	    swLargeSmall=(Switch)findViewById(R.id.swLargeSmall);
	    swLargeSmall.setOnCheckedChangeListener(this);
	    swTail=(Switch)findViewById(R.id.swTail);
	    swTail.setOnCheckedChangeListener(this);
	    swSec=(Switch)findViewById(R.id.swSec);
	    swSec.setOnCheckedChangeListener(this);
	    swNoSmall=(Switch)findViewById(R.id.swNoSmall);
	    swNoSmall.setOnCheckedChangeListener(this);
	    swNoSecSmall=(Switch)findViewById(R.id.swNoSecSmall);
	    swNoSecSmall.setOnCheckedChangeListener(this);
	    swMiddle=(Switch)findViewById(R.id.swMiddle);
	    swMiddle.setOnCheckedChangeListener(this);
	    swValue=(Switch)findViewById(R.id.swValue);
	    swValue.setOnCheckedChangeListener(this);
	    swDoubleTail=(Switch)findViewById(R.id.swDoubleTail);
	    swDoubleTail.setOnCheckedChangeListener(this);
	    swNoSmall_Tail=(Switch)findViewById(R.id.swNoSmall_Tail);
	    swNoSmall_Tail.setOnCheckedChangeListener(this);
	    swMiddle_Tail=(Switch)findViewById(R.id.swMiddle_Tail);
	    swMiddle_Tail.setOnCheckedChangeListener(this);
	    swValue_Tail=(Switch)findViewById(R.id.swValue_Tail);
	    swValue_Tail.setOnCheckedChangeListener(this);
	    swBaoZi=(Switch)findViewById(R.id.swBaoZi);
	    swBaoZi.setOnCheckedChangeListener(this);
	    swGuardAccount=(Switch)findViewById(R.id.swGuardAccount);
	    swGuardAccount.setOnCheckedChangeListener(this);
	    swPerspact=(Switch)findViewById(R.id.swPerspact);
	    swPerspact.setOnCheckedChangeListener(this);

	    //-------------------------------------------------------------
	    bSwitchLargeSmall=getConfig().getSwitchLargeSmall();
    	swLargeSmall.setChecked(bSwitchLargeSmall);
    	bSwitchTail=getConfig().getSwitchTail();
    	swTail.setChecked(bSwitchTail);
    	bSwitchSec=getConfig().getSwitchSec();
    	swTail.setChecked(bSwitchTail);
    	Config.bSwitch=bSwitchLargeSmall || bSwitchTail||bSwitchSec;
    	
    	//-------------------------------????????-------------------------------------------
    	//??????????
    	int iRobLuckyMoneyMode=getConfig().getRobLuckyMoneyMode();
    	if(iRobLuckyMoneyMode==Config.KEY_AUTO_ROB){
    		rbAutoRob.setChecked(true);
    		Config.bAutoRob=true;
    	}else if(iRobLuckyMoneyMode==Config.KEY_SEMI_AUTO_ROB){
    		rbSemiAutoRob.setChecked(true);
    		Config.bAutoRob=false;
    	}
    	//????????????????
    	boolean bReturn=getConfig().getUnpackReturn();
    	if(bReturn==Config.KEY_AUTO_RETURN){
    		rbAutoReturn.setChecked(true);//????????
    	}else if(bReturn==Config.KEY_MANUAL_RETURN){
    		rbManualReturn.setChecked(true);
    	}
    	Config.bAutoReturn=bReturn;
    	swPerspact.setChecked(getConfig().getPerspact());
    	swGuardAccount.setChecked(getConfig().getGuardAccount());
    	//??????????
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//????????
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
    	//-----------------------------????????---------------------------------------------
 
    	//1??????????-----------------------------------------------
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				mBackgroundMusic.stopBackgroundMusic();

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
					if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
				}
				if(!QiangHongBaoService.isRunning()) {
					Toast.makeText(MainActivity.this, "????????????????????????????????????", Toast.LENGTH_LONG).show();
					speaker.speak("????????????????????????????????????");
					return;
				}
				Log.d(TAG, "????---->????????");
				OpenWechat();
				Config.JobState=Config.STATE_NONE;
				speaker.speak("????????????????????????");
				String wxInfo=ConfigCt.getInstance(MainActivity.this).getWechatInfo();
				if(wxInfo.length()==0){
					//WechatInfo.start();
					if(WechatInfo.getWechatInfo().isEnable()){
						WechatInfo.getWechatInfo().start();
					}
					Toast.makeText(MainActivity.this, "??????????????....", Toast.LENGTH_LONG).show();
				}
				if(Config.bAutoRob){
					//speeker.speak("??????????????????????????????????????????????????????????????????????");
					speaker.speak("6K??????????????????????????????????????????");
					speaker.speak("??????????????6K??????????????????????");
				}else{
					speaker.speak("6K????????????????????????????????????????????");
					speaker.speak("????????????????????????????6K??????????????");
				//System.exit(0);

				}
				MainActivity.this.finish();
			}
		});
		//2??????????????????
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//1??????????????????
				mBackgroundMusic.stopBackgroundMusic();
				if(!QiangHongBaoService.isRunning()) {
					//??????????????????????
					Log.d(TAG, "????---->??????????????????????");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					Toast.makeText(MainActivity.this, "????6K??????????????????6K??????????", Toast.LENGTH_LONG).show();
					speaker.speak("??????6K??????????????????????????????");
				}else{
					Toast.makeText(MainActivity.this, "????????????????????????????????????????????", Toast.LENGTH_LONG).show();
					speaker.speak("????????????????????");
				}
			}
		});//startBtn.setOnClickListener(

		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				if(Config.DEBUG){

				}
				//mBackgroundMusic.stopBackgroundMusic();
				//moveTaskToBack(true);
				MainActivity.this.finish();
			}
		});//btn.setOnClickListener(
        //3????????????
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(Config.DEBUG)QiangHongBaoService.getQiangHongBaoService().closeClick();
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "????????????????", Toast.LENGTH_LONG).show();
					speaker.speak("????????????????");
					return;
				}
				getSock().RegStart(regCode);
			}
		});//btReg.setOnClickListener(
		//4??SeekBar????
        mSeekBar.setOnSeekBarChangeListener(this); 
   	 //????????
    	rgRobMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //????????????????????ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //????ID????RadioButton??????
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //??????????????????????????
                String sChecked=rb.getText().toString();
                //tv.setText("????????????" + rb.getText());
               if(sChecked.equals("??????????")){
               	getConfig().setRobLucyMoneyMode(Config.KEY_AUTO_ROB);
               	getConfig().bAutoRob=true;
               	speaker.speak("??????????????????????");
               	Toast.makeText(MainActivity.this, "??????????????????????", Toast.LENGTH_LONG).show();
               }
               if(sChecked.equals("??????????")){
               	getConfig().setRobLucyMoneyMode(Config.KEY_SEMI_AUTO_ROB);
               	getConfig().bAutoRob=false;
               	speaker.speak("??????????????????????");
               	Toast.makeText(MainActivity.this, "??????????????????????", Toast.LENGTH_LONG).show();
               }
           }
        });
 	 //????????????????????
	rgSelReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup arg0, int arg1) {
              //????????????????????ID
             int radioButtonId = arg0.getCheckedRadioButtonId();
             //????ID????RadioButton??????
              RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
              //??????????????????????????
              String sChecked=rb.getText().toString();
             if(sChecked.equals("??????????????")){
             	getConfig().setUnpackReturn(Config.KEY_AUTO_RETURN);
             	Config.bAutoReturn=true;
             	speaker.speak("??????????????????????????");
             	Toast.makeText(MainActivity.this, "??????????????????????????", Toast.LENGTH_LONG).show();
             }
             if(sChecked.equals("??????????????")){
             	getConfig().setUnpackReturn(Config.KEY_MANUAL_RETURN);
             	Config.bAutoReturn=false;
             	speaker.speak("??????????????????????????");
             	Toast.makeText(MainActivity.this, "??????????????????????????", Toast.LENGTH_LONG).show();
             }
         }
      });
	 //???? ????
	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            //????????????????????ID
           int radioButtonId = arg0.getCheckedRadioButtonId();
           //????ID????RadioButton??????
            RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
            //??????????????????????????
            String sChecked=rb.getText().toString();
            String say="";
           if(sChecked.equals("????????????")){
        	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
           		say="????????????????????????";
           }
           if(sChecked.equals("????")){
        	   Config.bSpeaking=Config.KEY_SPEAKING;
        	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
           		say="????????????????????";
           }
           if(sChecked.equals("????")){
        	   Config.bSpeaking=Config.KEY_SPEAKING;
        	   Config.speaker=Config.KEY_SPEAKER_MALE;
           		say="????????????????????";
           }
           if(sChecked.equals("????????")){
        	   Config.bSpeaking=Config.KEY_SPEAKING;
        	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
           		say="????????????????????????";
           }
           if(sChecked.equals("????????")){
        	   Config.bSpeaking=Config.KEY_SPEAKING;
        	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
           		say="????????????????????????";
           }
           if(sChecked.equals("??????????")){
        	   Config.bSpeaking=Config.KEY_SPEAKING;
        	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
           		say="??????????????????????????";
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
    /*
     * ???? ??
     * 
     */
    public void test(String a,String b){
    	Toast.makeText(MainActivity.this,a+b, Toast.LENGTH_LONG).show();
    }
    /*
     * (non-Javadoc)
     * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    	String sShow="";
        switch (compoundButton.getId()){
            case R.id.swSec:
                if(compoundButton.isChecked())sShow="????????????";
                else  sShow="????????????";
                bSwitchSec=compoundButton.isChecked();
                Config.bSwitch=bSwitchLargeSmall || bSwitchTail||bSwitchSec;
                break;
            case R.id.swLargeSmall:
                if(compoundButton.isChecked())sShow="????????????";
                else sShow="????????????";
                bSwitchLargeSmall=compoundButton.isChecked();
                Config.bSwitch=bSwitchLargeSmall || bSwitchTail||bSwitchSec;
                break;
            case R.id.swNoSmall:
                if(compoundButton.isChecked())sShow="????????????";
                else sShow="????????????";
                break;
            case R.id.swNoSecSmall:
                if(compoundButton.isChecked())sShow="????????????????";
                else sShow="????????????????";
                break;
            case R.id.swMiddle:
                if(compoundButton.isChecked())sShow="??????????????";
                else sShow="??????????????";
                break;
            case R.id.swValue:
                if(compoundButton.isChecked())sShow="????????????????????????";
                else sShow="????????????????????????";
                break;
            case R.id.swTail:
                if(compoundButton.isChecked())sShow="????????????";
                else sShow="????????????";
                bSwitchTail=compoundButton.isChecked();
                Config.bSwitch=bSwitchLargeSmall || bSwitchTail||bSwitchSec;
                break;
            case R.id.swDoubleTail:
                if(compoundButton.isChecked())sShow="????????????";
                else sShow="????????????";
                break;
            case R.id.swNoSmall_Tail:
                if(compoundButton.isChecked())sShow="????????????????";
                else sShow="????????????????";
                break;
            case R.id.swMiddle_Tail:
                if(compoundButton.isChecked())sShow="??????????????????";
                else sShow="??????????????????";
                break;
            case R.id.swValue_Tail:
                if(compoundButton.isChecked())sShow="????????????????????????????";
                else sShow="????????????????????????????";
                break;
            case R.id.swBaoZi:
                if(compoundButton.isChecked())sShow="????????????????????????????????????";
                else sShow="??????????????";
                break;
            case R.id.swGuardAccount:
                if(compoundButton.isChecked()){
                	if(Config.bReg){
                		sShow="??????????????";
                		getConfig().setGuardAccount(true);
                	}else{
                		compoundButton.setChecked(false);
                		sShow="????????????????????????????";
                	}
                }else {
                	sShow="??????????????";
                	getConfig().setGuardAccount(false);
                }
                break;
            case R.id.swPerspact:
                if(compoundButton.isChecked()){
                	if(Config.bReg){
                		sShow="??????????????????????????????????";
                		getConfig().setPerspact(true);
                	}else{
                		sShow="??????????????????????????!";
                		compoundButton.setChecked(false);
                	}
                }else{
                	sShow="????????????";
                	getConfig().setPerspact(false);
                }
                break;
        }
        Toast.makeText(this,sShow,Toast.LENGTH_SHORT).show();
        speaker.speak(sShow);
    }
    @SuppressWarnings("deprecation")
	private void getResolution2(){
        WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        Config.screenWidth= display.getWidth();    
        Config.screenHeight= display.getHeight();  
        Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
    }
    /**????????????*/
    public void showVerInfo(boolean bReg){
    	ConfigCt.bReg=bReg;
    	if(Ad2.currentQQ!=null)Ad2.currentQQ.getADinterval();
		if(Ad2.currentWX!=null)Ad2.currentWX.getADinterval();
        if(bReg){
        	Config.bReg=true;
        	getConfig().setREG(true);
        	tvRegState.setText("????????????????");
        	tvRegWarm.setText("????????????????????"+ConfigCt.contact);
        	etRegCode.setVisibility(View.INVISIBLE);
        	tvPlease.setVisibility(View.INVISIBLE);
        	btReg.setVisibility(View.INVISIBLE);
        	speaker.speak("????????"+ConfigCt.AppName+"????????????????" );
        	
        }else{
        	Config.bReg=false;
        	getConfig().setREG(false);
        	tvRegState.setText("????????????????");
        	tvRegWarm.setText(ConfigCt.warning+"??????????????"+ConfigCt.contact);
        	etRegCode.setVisibility(View.VISIBLE);
        	tvPlease.setVisibility(View.VISIBLE);
        	btReg.setVisibility(View.VISIBLE);
        	speaker.speak("????????"+ConfigCt.AppName+"??????????????????" );
        	
        }
        String html = "<font color=\"blue\">????????????????(????????????)??</font><br>";
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
    //??????????????
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
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"??????????");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"??????????");
        }
    }
   /** ??????????*/
   public void setAppToTestVersion() {
   	String sStartTestTime=getConfig().getStartTestTime();//????????????????????????????
   	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
   	String currentDate =sdf.format(new Date());//????????????
   	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//??????????????
   	if(timeInterval>Config.TestTimeInterval){//7????????????????
   		showVerInfo(false);
   		//ftp.getFtp().DownloadStart();//??????????????;
   	}
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
   private   void   showUpdateDialog(){ 
       /* @setIcon ?????????????? 
        * @setTitle ?????????????? 
        * @setMessage ?????????????????? 
        * setXXX????????Dialog?????????????????????????? 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "????????"  );
       normalDialog.setMessage("??????????????????????????"); 
       normalDialog.setPositiveButton("????",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
               //...To-do
    		   Uri uri = Uri.parse(ConfigCt.download);    
    		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
    		   startActivity(it);  
           }
       }); 
       normalDialog.setNegativeButton("????",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ???? 
       normalDialog.show(); 
   } 
  
   @Override
   public void onBackPressed() {
       //????????????????????
	   //moveTaskToBack(true); 
   }
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {//??????????????
           //????????????????????
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
   protected void onDestroy() {
	   super.onDestroy();
	   mBackgroundMusic.stopBackgroundMusic();
	   unregisterReceiver(qhbConnectReceiver);
   }
   @Override
   protected void onResume() {
       // TODO Auto-generated method stub
       super.onResume();
       //if(!Utils.isActive)
       //{
           //Utils.isActive = true;
           /*????????????????????????????*/
       //}
   }
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "aa onNewIntent: ????");  
	}
 
}
