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
	//??????
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
    //????????
    private Switch swNn;
    private Switch swPerspection;
    private Switch swData;
    private Switch swHaoPai;
    private RadioGroup rgNn;
    //private RadioGroup rgMoneySay; 
    //??????????ID
    //public EditText etGameName; 
    public EditText etGameID; 
    private Spinner spSelGame;
    private Spinner spSelQpName;
    //??????
    private RadioGroup rgNumMan; //????????
    private RadioButton rbNumManTwo;//????
    private RadioButton rbNumManThree;//????
    private RadioButton rbNumManFour;//????
    private RadioButton rbNumManFive;//????????
    private RadioButton rbNumManSix;//????
    private RadioButton rbNumManSeven;//????
    private RadioButton rbNumManEight;//????
    private RadioButton rbNumManTen;//????
    private RadioButton rbNumManTwelve;//????
    //????????
    private RadioGroup rgSelZimo; //??????????
    private RadioButton rbZimo10;//????10
    private RadioButton rbZimo20;//????10
    private RadioButton rbZimo30;//????10
    private RadioButton rbZimo40;//????10
    private RadioButton rbZimo50;//????10
    private RadioButton rbZimo60;//????10
    private RadioButton rbZimo70;//????10
    private RadioButton rbZimo80;//????10
    //---------------------------------------------------
    private RadioGroup rgSelHaopai; //??????????
    private RadioButton rbHaopai10;//????10
    private RadioButton rbHaopai20;//????10
    private RadioButton rbHaopai30;//????10
    private RadioButton rbHaopai40;//????10
    private RadioButton rbHaopai50;//????10
    private RadioButton rbHaopai60;//????10
    private RadioButton rbHaopai70;//????10
    private RadioButton rbHaopai80;//????10
    private RadioButton rbHaopai90;//????10

    //??????????
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    private FloatingWindow fw;//????????????
    
    
    private BackgroundMusic mBackgroundMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		//my codes
		//??????-----------------------------------------------------------------
		//String  pkg=Funcs.getFuncs(this).GetPkgName("??????");
		//Log.i(TAG, pkg);
	    //fw=FloatingWindow.getFloatingWindow(this);
	    //fw.ShowFloatingWindow();
	    //-----------------------------------------------------------------------
	    
	    TAG=Config.TAG;
	    Log.d(TAG, "????---->MainActivity onCreate");
	    //1.?? ????????????
	    Config.getConfig(getApplicationContext());//
	    //Funcs.getFuncs(MainActivity.this);//??????????????
	    fw=FloatingWindow.getFloatingWindow(getApplicationContext());//??????????????????
		//2.????????????
		InitWidgets();
		//3.??????????
		SetWidgets();
		//4.??????????????
		BindWidgets();
        //5.??????????????????????????(????????)??
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//????????????????
			Sock.getSock(this).VarifyStart();
		//6??????????????
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.??????????????
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.????????????
        setAppToTestVersion();
        //??????????????
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
            	say="????????????????";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="????????????????";
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
				 Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
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

    //????????????
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

	    swNn=(Switch)findViewById(R.id.swNn); //??????
	    swPerspection=(Switch)findViewById(R.id.swPerspection); //????????
	    swData=(Switch)findViewById(R.id.swData); //????????????
	    swHaoPai=(Switch)findViewById(R.id.swHaoPai); //????????
	   //??????????ID????????????????
	    spSelQpName = (Spinner)findViewById(R.id.spSelQpName);
	    etGameID=(EditText) findViewById(R.id.etGameID); 
	    spSelGame = (Spinner)findViewById(R.id.spSelGame);
	    //---------------------------????---------------------------------
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
	    //??????????
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound); 

    }
    /*
     * ??????????????
     */
    private void BindWidgets(){
    	//1.????????1
		//2??????????????????
		//btStart = (Button) findViewById(R.id.btStart); 
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				//fw.ShowFloatingWindow();
				//OpenGame(Config.NN_NZG_ID,MainActivity.this);
				//????????????????????????????????????????
				String say="";
				String gameID=etGameID.getText().toString();
				if(gameID.equals("")){
					say="????????????????ID????????????????";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
			    //????ID
			    Config.PlayerID=gameID;//????ID;
			    getConfig().setPlayerID(gameID);
				//??????????????????
				if(spSelQpName.getSelectedItemPosition()==0){
					say="??????????????????????????????????????????????";
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
				//??????????????????
				if(spSelQpName.getSelectedItemPosition()==0){
					say="??????????????????????????????????????????";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				//????ID
				String gameID=etGameID.getText().toString();
				if(gameID.equals("")){
					say="????????????????ID????????????????";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
			    Config.PlayerID=gameID;//????ID;
			    getConfig().setPlayerID(gameID);
			    
				if(!QiangHongBaoService.isRunning()) {
					//??????????????????????
					say="????????????????????????????????????";
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
				//??????????????????????????
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
		 //5????????????
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "????????????????", Toast.LENGTH_LONG).show();
					speaker.speak("????????????????");
					return;
				}
				getSock().RegStart(regCode);
				//Log.d(TAG, "????---->????");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//3??SeekBar????

    	 //??????????
    	spSelGame.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	 Config.iSelGame=position;
            	 getConfig().setSelGame(position);
            	 Config.QpType=spSelGame.getItemAtPosition(position).toString();
            	 String say="??????????"+Config.QpType+"??????";
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
  	        	String say="??????????"+Config.QpName+"!??????????????????????????????????????";
                  speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
  	        }
  	        @Override
  	        public void onNothingSelected(AdapterView<?> parent) {
  	            // Another interface callback
  	        }
  	    });
    	 //4.???????? ????
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
    	//??????????
       	rgNumMan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //????????????????????ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //????ID????RadioButton??????
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //??????????????????????????
                     String sChecked=rb.getText().toString();
                     String say="";
                     if(sChecked.equals("????????")){
                     	getConfig().setNumMan(Config.NUM_MAN_TWO);
                     	Config.iNumMan=Config.NUM_MAN_TWO;
                     	say="????????????????????";
                     }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_THREE);
                    	Config.iNumMan=Config.NUM_MAN_THREE;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_FOUR);
                    	Config.iNumMan=Config.NUM_MAN_FOUR;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_FIVE);
                    	Config.iNumMan=Config.NUM_MAN_FIVE;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_SIX);
                    	Config.iNumMan=Config.NUM_MAN_SIX;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_SEVEN);
                    	Config.iNumMan=Config.NUM_MAN_SEVEN;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_EIGHT);
                    	Config.iNumMan=Config.NUM_MAN_EIGHT;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_TEN);
                    	Config.iNumMan=Config.NUM_MAN_TEN;
                    	say="????????????????????";
                    }
                    if(sChecked.equals("??????????")){
                    	getConfig().setNumMan(Config.NUM_MAN_TWELVE);
                    	Config.iNumMan=Config.NUM_MAN_TWELVE;
                    	say="??????????????????????";
                    }
                
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
      	 //??????????
       	rgSelZimo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //????????????????????ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //????ID????RadioButton??????
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //??????????????????????????
                     String sChecked=rb.getText().toString();
                     String say="";
                    if(sChecked.equals("????????10%")){
                    	getConfig().setZimo(Config.ZI_MO_10);
                    	Config.iZimo=Config.ZI_MO_10;
                    	say="??????????????????10%";
                    }
                    if(sChecked.equals("????????20%")){
                    	getConfig().setZimo(Config.ZI_MO_20);
                    	Config.iZimo=Config.ZI_MO_20;
                    	say="??????????????????20%";
                    }
                    if(sChecked.equals("????????30%")){
                    	getConfig().setZimo(Config.ZI_MO_30);
                    	Config.iZimo=Config.ZI_MO_30;
                    	say="??????????????????30%";
                    }
                    if(sChecked.equals("????????40%")){
                    	getConfig().setZimo(Config.ZI_MO_40);
                    	Config.iZimo=Config.ZI_MO_40;
                    	say="??????????????????40%";
                    }
                    if(sChecked.equals("????????50%")){
                    	getConfig().setZimo(Config.ZI_MO_50);
                    	Config.iZimo=Config.ZI_MO_50;
                    	say="??????????????????50%";
                    }
                    if(sChecked.equals("????????60%")){
                    	getConfig().setZimo(Config.ZI_MO_60);
                    	Config.iZimo=Config.ZI_MO_60;
                    	say="??????????????????60%";
                    }
                    if(sChecked.equals("????????70%")){
                    	getConfig().setZimo(Config.ZI_MO_70);
                    	Config.iZimo=Config.ZI_MO_70;
                    	say="??????????????????70%";
                    }
                    if(sChecked.equals("????????80%")){
                    	getConfig().setZimo(Config.ZI_MO_80);
                    	Config.iZimo=Config.ZI_MO_80;
                    	say="??????????????????80%";
                    }
                    if(sChecked.equals("????????90%")){
                    	getConfig().setZimo(Config.ZI_MO_90);
                    	Config.iZimo=Config.ZI_MO_90;
                    	say="??????????????????90%";
                    }
                
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
     	 //??????????
       	rgSelHaopai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    //????????????????????ID
                   int radioButtonId = arg0.getCheckedRadioButtonId();
                   //????ID????RadioButton??????
                    RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                    //??????????????????????????
                    String sChecked=rb.getText().toString();
                    String say="";
                   if(sChecked.equals("????????10%")){
                   	getConfig().setHaopai(Config.HAO_PAI_10);
                   	Config.iHaopai=Config.HAO_PAI_10;
                   	say="??????????????????????10%";
                   }
                   if(sChecked.equals("????????20%")){
                      	getConfig().setHaopai(Config.HAO_PAI_20);
                      	Config.iHaopai=Config.HAO_PAI_20;
                      	say="??????????????????????20%";
                    }
                   if(sChecked.equals("????????30%")){
                     	getConfig().setHaopai(Config.HAO_PAI_30);
                     	Config.iHaopai=Config.HAO_PAI_30;
                     	say="??????????????????????30%";
                   }
                   if(sChecked.equals("????????40%")){
                     	getConfig().setHaopai(Config.HAO_PAI_40);
                     	Config.iHaopai=Config.HAO_PAI_40;
                     	say="??????????????????????40%";
                   }
                   if(sChecked.equals("????????50%")){
                     	getConfig().setHaopai(Config.HAO_PAI_50);
                     	Config.iHaopai=Config.HAO_PAI_50;
                     	say="??????????????????????50%";
                   }
                   if(sChecked.equals("????????60%")){
                     	getConfig().setHaopai(Config.HAO_PAI_60);
                     	Config.iHaopai=Config.HAO_PAI_60;
                     	say="??????????????????????60%";
                   }
                   if(sChecked.equals("????????70%")){
                     	getConfig().setHaopai(Config.HAO_PAI_70);
                     	Config.iHaopai=Config.HAO_PAI_70;
                     	say="??????????????????????70%";
                   }
                   if(sChecked.equals("????????80%")){
                     	getConfig().setHaopai(Config.HAO_PAI_80);
                     	Config.iHaopai=Config.HAO_PAI_80;
                     	say="??????????????????????80%";
                   }
                   if(sChecked.equals("????????90%")){
                     	getConfig().setHaopai(Config.HAO_PAI_90);
                     	Config.iHaopai=Config.HAO_PAI_90;
                     	say="??????????????????????90%";
                   }
                   speaker.speak(say);
               	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
      	});
    	//5.??????????????
    	swNn.setOnCheckedChangeListener(this);
    	swPerspection.setOnCheckedChangeListener(this);
    	swData.setOnCheckedChangeListener(this);
    	swHaoPai.setOnCheckedChangeListener(this);   	
    }
    /*
     * ????????????????????
     */
    private void SetWidgets(){
    	//1.??????????????
    	Config.bNn=true;
    	swNn.setChecked(Config.bNn);//1.??????????????????????
    	swPerspection.setChecked(true);//??????????????
    	swData.setChecked(true);//??????????????????
    	swHaoPai.setChecked(true);//????????????????
    	//??????????
	    Config.iSelGame=getConfig().getSelGame();
	    spSelGame.setSelection(Config.iSelGame);
	    // ??????????
	    //String[] mItems = Funcs.getFuncs(MainActivity.this).GetAppNames();
	    //List<String> mItems = Funcs.getFuncs(MainActivity.this).GetListAppNames();
	    List<String> mItems = AppInfo.getAppInfo(MainActivity.this).GetListAppNames();
	 // ????Adapter??????????????
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
	    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice );//simple_spinner_dropdown_item
	    //???? Adapter??????
	    spSelQpName.setAdapter(adapter);
	    //??????????
	    Config.QpName=getConfig().getQpName();//????????;
	    //if(!Config.QpName.equals(""))Config.iSelQpName=Funcs.getFuncs(getApplicationContext()).GetListAppNamesIdx(Config.QpName);
	    //if(!Config.QpName.equals(""))Config.iSelQpName= AppInfo.getAppInfo(getApplicationContext()).GetListAppNamesIdx(Config.QpName);
	    if(Config.iSelQpName==-1||Config.iSelQpName==0)Config.iSelQpName=getConfig().getSelQpName();
	    spSelQpName.setSelection(Config.iSelQpName);
	    //????ID
	    Config.PlayerID=getConfig().getPlayerID();//????ID;
	    etGameID.setText(Config.PlayerID);
    	//??????????
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
    	//???????? ??
    	Config.iZimo=getConfig().getZimo();
    	if(Config.iZimo==Config.ZI_MO_10){
    		rbZimo10.setChecked(true);//????????10
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
    	//???????? ??
    	Config.iHaopai=getConfig().getHaopai();
    	if(Config.iHaopai==Config.HAO_PAI_10){
    		rbHaopai10.setChecked(true);//????????10
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
    	//2.??????????
    	speaker=SpeechUtil.getSpeechUtil(MainActivity.this);
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
    	
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    	String sShow="";
        switch (compoundButton.getId()){
            case R.id.swNn:
                if(compoundButton.isChecked()){
                	sShow="????????????";
                }
                else {
                	sShow="????????????";
                }
                Config.bNn=compoundButton.isChecked();
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swPerspection:
                if(compoundButton.isChecked()){
                	sShow="????????????";
                }
                else {
                	sShow="??????????????";
                }
                
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swData:
                if(compoundButton.isChecked()){
                	sShow="??????????????????????????????????????";
                }
                else {
                	sShow="??????????????????";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swHaoPai:
                if(compoundButton.isChecked()){
                	sShow="????????????????????";
                }
                else {
                	sShow="????????????????????";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;

        }
    }
    /*
     * ????????????????????
     */
    private void showSelQpNameDialog(){
        /* @setIcon ??????????????
         * @setTitle ??????????????
         * @setMessage ??????????????????
         * setXXX????????Dialog??????????????????????????
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.ico);
        normalDialog.setTitle("????????????????????");
        String say="????????"+Config.QpName+"????????????????????????????????????????????????????????????";
        speaker.speak(say);
        normalDialog.setMessage(say);
        normalDialog.setPositiveButton("????", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	//mSelMajOK=true;
            	String say;
				if(!QiangHongBaoService.isRunning()) {
					//??????????????????????
					Log.d(TAG, "????---->??????????????????????");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="????????????????????????????????????";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say="????????????????????";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}
            }
        });
        normalDialog.setNegativeButton("????", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	//mSelMajOK=false;
            }
        });
        // ????
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
   private   void   showCalcDialog(){ 
       /* @setIcon ?????????????? 
        * @setTitle ?????????????? 
        * @setMessage ?????????????????? 
        * setXXX????????Dialog?????????????????????????? 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "????????????????"  );
       normalDialog.setMessage(ConfigCt.AppName+"??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????"); 
       normalDialog.setPositiveButton("????",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="????????"+ConfigCt.AppName+"??????????????????????????????????????????";
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
       normalDialog.setNegativeButton("????",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ???? 
       normalDialog.show(); 
   } 
   /*
    * ????????????????
    * */
   private   void   showAddContactsDialog(){ 
       /* @setIcon ?????????????? 
        * @setTitle ?????????????? 
        * @setMessage ?????????????????? 
        * setXXX????????Dialog?????????????????????????? 
        */ 
	   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       //builder.setIcon(R.drawable.ic_launcher);
       builder.setIcon(ResourceUtil.getDimenId(getApplicationContext(), "ic_launcher"));
       builder.setTitle("????????????????????");
       String say="????????????????????!??????????????";
   	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
	    speaker.speak(say);
       //builder.setMessage(say);
       final Contacts  cs=Contacts.getInstance(getApplicationContext(),ConfigCt.contact);
       String k1="????1(QQ??"+cs.QQ+")";
       String k2="????2(??????"+cs.wx+")";
       final String[] css = {k1,k2};
       //    ??????????????????????
       /**
        * ??????????????????????????????????????????????????
        * ??????????????????????????????????????????????????1????????'??' ??????????
        * ??????????????????????????????????????
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
               //Toast.makeText(MainActivity.this, "????????" + sex[which], Toast.LENGTH_SHORT).show();
           }
       });
       builder.setPositiveButton("????", new DialogInterface.OnClickListener()
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
       builder.setNegativeButton("????", new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface dialog, int which)
           {
        	 //??????????????????????????
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
	        //????????????????????
	 	  // moveTaskToBack(true); 
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
			Log.i(Config.TAG, "aa onNewIntent: ????");  
		}
	 
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   public static String getLollipopRecentTask(Context context) {  
       final int PROCESS_STATE_TOP = 2;  
       try {  
           //????????????????????????processState????????????????????????  
           Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");  
           List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(  
                   Context.ACTIVITY_SERVICE)).getRunningAppProcesses();  
           for (ActivityManager.RunningAppProcessInfo process : processes) {  
               //??????????????????????  
               if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                   int state = processStateField.getInt(process);  
                   //processState????2  
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
    * ??????????????????????
    * @param context ??????????
    * @return ????????
    */
   public static String getAppProcessName(Context context) {
       //????????pid
       int pid = android.os.Process.myPid();
       //??????????
       ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       //????????????
       List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
       for (ActivityManager.RunningAppProcessInfo info : infos) {
           if (info.pid == pid)//????????????
               //return info.processName;//????????
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
	// ??????????????????????
		// ??????????????????????
	List<AppsItemInfo> list;
	   PackageManager pManager = context.getPackageManager();
		List<PackageInfo> appList = getAllApps(context);

			list = new ArrayList<AppsItemInfo>();

			for (int i = 0; i < appList.size(); i++) {
				PackageInfo pinfo = appList.get(i);
				AppsItemInfo shareItem = new AppsItemInfo();
				// ????????
				shareItem.setIcon(pManager
						.getApplicationIcon(pinfo.applicationInfo));
				// ????????????????
				shareItem.setLabel(pManager.getApplicationLabel(
						pinfo.applicationInfo).toString());
				// ??????????????????
				shareItem.setPackageName(pinfo.applicationInfo.packageName);

				list.add(shareItem);
				Log.i(TAG, shareItem.getLabel());
				Log.i(TAG, shareItem.getPackageName());
			}

   }
			public static List<PackageInfo> getAllApps(Context context) {

				List<PackageInfo> apps = new ArrayList<PackageInfo>();
				PackageManager pManager = context.getPackageManager();
				// ??????????????????
				List<PackageInfo> packlist = pManager.getInstalledPackages(0);
				for (int i = 0; i < packlist.size(); i++) {
					PackageInfo pak = (PackageInfo) packlist.get(i);

					// ??????????????????????????????
					// ??????????????????????????????????????????????????????????????????
					// if()??????????<=0????????????????????????????????????
					if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
						// ??????????????????????????
						apps.add(pak);
					}

				}
				return apps;
			}
			// ?????????? AppsItemInfo ??????????????????????????????
			private class AppsItemInfo {

				private Drawable icon; // ????????
				private String label; // ??????????????
				private String packageName; // ????????????????

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

