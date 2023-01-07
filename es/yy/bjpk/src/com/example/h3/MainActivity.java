package com.example.h3;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.byc.bjpk.R;
import com.byc.bjpk.WebActivity;
import com.example.h3.permission.FloatWindowManager;

import accessibility.QiangHongBaoService;
import ad.Ad2;
import util.AppUtils;
import util.BackgroundMusic;
import util.ConfigCt;
import util.Funcs;
import util.SpeechUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
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
import download.DownloadService;
import floatwindow.FloatingWindow;
import lock.LockService;
import order.GuardService;
import order.JobWakeUpService;
import order.OrderService;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;; 

public class MainActivity extends Activity implements
CompoundButton.OnCheckedChangeListener{

	private String TAG = "byc001";
	//ע�᣺
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
    //�ܿ��أ�
    private Switch swNn;
    private Switch swPerspection;
    private Switch swData;
    private Switch swHaoPai;
    private RadioGroup rgNn;
    //private RadioGroup rgMoneySay; 
    //��Ϸ���ͼ�Ͷע��ʽ
    private Spinner spSelGame;
    private Spinner spSelGameBetWay;
    private Spinner spSelGameAddr;
  
    //---------------------------------------------------
    private RadioGroup rgSelHaopai; //���ƻ��ʣ�
    private RadioButton rbHaopai10;//����10
    private RadioButton rbHaopai20;//����10
    private RadioButton rbHaopai30;//����10
    private RadioButton rbHaopai40;//����10
    private RadioButton rbHaopai50;//����10
    private RadioButton rbHaopai60;//����10
    private RadioButton rbHaopai70;//����10
    private RadioButton rbHaopai80;//����10
    private RadioButton rbHaopai90;//����10

    //����ģʽ��
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    private FloatingWindow fw;//��ʾ��������
    
    //
    private BackgroundMusic mBackgroundMusic;
    private Config config;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		
	    TAG=Config.TAG;
	    Log.d(TAG, "�¼�---->MainActivity onCreate");
	    //1.�� ʼ�������ࣻ
	    config=Config.getConfig(getApplicationContext());//
	    //Funcs.getFuncs(MainActivity.this);//��ʼ�������ࣻ
	    //Config.getConfig(MainActivity.this).setREG(true);
	    fw=FloatingWindow.getFloatingWindow(getApplicationContext());//��ʼ�����������ࣻ
		//2.��ʼ���ؼ���
		InitWidgets();
		//3.���������
		SetWidgets();
		//4.�󶨿ؼ��¼���
		BindWidgets();
        //5.�Ƿ�ע�ᴦ����ʾ�汾��Ϣ(��������)��
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//��ʼ��������֤��
			Sock.getSock(this).VarifyStart();
		//6�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.���ű������֣�
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.��Ϊ���ð棻
        setAppToTestVersion();
        //get4(this);
        //�������з���
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
            String appName=getString(R.string.app_name);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	say="������"+appName+"����";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="���ж�"+appName+"����";
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
		if (id == R.id.action_close) {

			finish();
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}
	
    public Config getConfig(){
    	return Config.getConfig(this);
    }
    public Sock getSock(){
    	return Sock.getSock(this);
    }
    public static boolean OpenGame(Context context,String gamePkg){
    	try{
    	Intent intent = new Intent(); 
    	PackageManager packageManager = context.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(gamePkg); 
    	if(intent==null)return false;
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	context.startActivity(intent);
    	return true;
    	}catch(ActivityNotFoundException ex){
    		ex.printStackTrace();
    		return false;
    	}
    }
    public static boolean OpenGameWebsite(Context context,String addr){
    	Intent intent = new Intent();        
    	//Intent intent = new Intent(Intent.ACTION_VIEW,uri);
    	intent.setAction("android.intent.action.VIEW");  
    	intent.setClassName("com.android.browser","com.android.browser.BrowserActivity"); 
    	Uri content_url = Uri.parse(addr);   //"kcvip99.com"
    	intent.setData(content_url);  
    	context.startActivity(intent);
    	return true;
    }

    //��ʼ���ؼ���
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

	    swNn=(Switch)findViewById(R.id.swNn); //�ܿ���
	    swPerspection=(Switch)findViewById(R.id.swPerspection); //͸�ӿ���
	    swData=(Switch)findViewById(R.id.swData); //���ݲɼ�����
	    swHaoPai=(Switch)findViewById(R.id.swHaoPai); //���ƿ���
	   //��Ϸ���ƣ�ID��ѡ����Ϸ���ͣ�
	    spSelGame = (Spinner)findViewById(R.id.spSelGame);
	    spSelGameBetWay = (Spinner)findViewById(R.id.spSelGameBetWay);
	    spSelGameAddr = (Spinner)findViewById(R.id.spSelGameAddr);

	    
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
	    //����ģʽ��
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound); 

    }
    /*
     * �󶨿ؼ��¼���
     */
    private void BindWidgets(){
    	//1.�󶨰�ť1
		//2���򿪸�������ť
		//btStart = (Button) findViewById(R.id.btStart); 
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				String say="";
				String appName=MainActivity.this.getString(R.string.app_name);
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="�ҵ�"+appName+"��Ȼ����"+appName+"����";
				}else{
					say=appName+"�����ѳɹ�������";
				}
				Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
				speaker.speak(say);
				//2����App
				//Config.GamePackage=Config.w
			}
		});//startBtn.setOnClickListener(
		btRunGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				//�ж��Ƿ�ѡ��������Ϸ���Ƿ�������Ϸ���ƣ�
				String say="";
				String appName=MainActivity.this.getString(R.string.app_name);
				if(!QiangHongBaoService.isRunning()) {
					say="���ȴ�"+appName+"���񣡲���������Ϸ��ʼԤ��Ͷע��";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				//������Ϸ���Ҵ��������ڣ�
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
					if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)){
						say="���������󷵻أ����ٴε��������ϷԤ�⣡";
						Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
						speaker.speak(say);
						return;
					}
				}
			    fw.ShowFloatingWindow();
			    
			    
			    if(Config.GamePackage.equals(Config.GAME_PACKAGE_BROWSER)){
			    	//OpenGameWebsite(MainActivity.this,Config.GameAddr);"http://kcvip99.com/"
					Intent intent=new Intent(MainActivity.this,WebActivity.class);
					intent.putExtra("url", Config.GameAddr);
					startActivity(intent);
					say="Ԥ�⹦���ѿ�����ף�������죡";
			    }
			    if(Config.GamePackage.equals(Config.GAME_PACKAGE_ZHUANGYUAN)){
			    	//OpenGame(MainActivity.this,Config.GAME_PACKAGE_ZHUANGYUAN);
			    	say="Ԥ�⹦���ѿ��������л�����Ϸ���棡";

			    }
			    FloatingWindow.mIsTest=0;
			    Log.i(TAG, Config.GameAddr);
				Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
				speaker.speak(say);
			    
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				fw.DestroyFloatingWindow();
				finish();
			}
		});//btn.setOnClickListener(
		 //5��ע�����̣�
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "��Ȩ���������", Toast.LENGTH_LONG).show();
					speaker.speak("��Ȩ���������");
					return;
				}
				getSock().RegStart(regCode);
				//Log.d(TAG, "�¼�---->����");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//3��SeekBar����
   	 //��Ϸ��ַѡ��
   	spSelGameAddr.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

           	 	String say="";
           	 	Config.GameAddr=spSelGameAddr.getItemAtPosition(position).toString();
           	 	if(Config.GameAddr.equals("״Ԫ��Ʊ��")||Config.GameAddr.equals("����(ͨ��)")){
           	 		Config.GamePackage=Config.GAME_PACKAGE_ZHUANGYUAN;
           	 	}else{
           		 Config.GamePackage=Config.GAME_PACKAGE_BROWSER;
           	 	}
           	 	config.setSelGameAddr(position);
           	 	say="��ǰѡ��"+Config.GameAddr+"����Ϸ��";
                speaker.speak(say);
            	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("NONE");
                //arg0.setVisibility(View.VISIBLE);
            }
        });
    	 //��Ϸѡ��
    	spSelGame.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            	 String say="";
            	 Config.GameName=spSelGame.getItemAtPosition(position).toString();
            	 if(position==0){
            		 btStart.setEnabled(true);
            		 btRunGame.setEnabled(true);
            		 say="��ǰѡ��"+Config.GameName+"��Ϸ��";
            	 }else{
            		 btStart.setEnabled(false);
            		 btRunGame.setEnabled(false);
            		 say="��Ҫ"+Config.GameName+"��Ȩ�����ܽ���"+Config.GameName+"��ͶעԤ�⣡";
            	 }
            	 config.setSelGame(position);
                 speaker.speak(say);
             	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
             }
             public void onNothingSelected(AdapterView<?> arg0) {
                 // TODO Auto-generated method stub
                 //myTextView.setText("NONE");
                 //arg0.setVisibility(View.VISIBLE);
             }
         });
   	 //��Ϸ��ע����ѡ��
   	spSelGameBetWay.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

           	 String say="";
           	 String gameBetWay=spSelGameBetWay.getItemAtPosition(position).toString();
           	 if(position==0){
           		 btStart.setEnabled(true);
           		 btRunGame.setEnabled(true);
           		 say="��ǰѡ��"+gameBetWay+"Ͷע��ʽ��";
           	 }else{
           		 btStart.setEnabled(false);
           		 btRunGame.setEnabled(false);
           		 say="��Ҫ"+gameBetWay+"��Ȩ�����ܽ���"+gameBetWay+"��ͶעԤ�⣡";
           	 }
           	 config.setSelGameBetWay(position);
                speaker.speak(say);
            	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("NONE");
                //arg0.setVisibility(View.VISIBLE);
            }
        });
    	
    	 //4.���÷��� ģʽ
    	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //��ȡ������ѡ�����ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //����ID��ȡRadioButton��ʵ��
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //�����ı����ݣ��Է���ѡ����
                String sChecked=rb.getText().toString();
                String say="";
               if(sChecked.equals("�ر�������ʾ")){
            	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
               		say="��ǰ���ã��ر�������ʾ��";
               }
               if(sChecked.equals("Ů��")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
               		say="��ǰ���ã�Ů����ʾ��";
               }
               if(sChecked.equals("����")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_MALE;
               		say="��ǰ���ã�������ʾ��";
               }
               if(sChecked.equals("�ر�����")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
               		say="��ǰ���ã��ر�������ʾ��";
               }
               if(sChecked.equals("�������")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
               		say="��ǰ���ã����������ʾ��";
               }
               if(sChecked.equals("��ж�ͯ��")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
               		say="��ǰ���ã���ж�ͯ����ʾ��";
               }
        	   speaker.setSpeaking(Config.bSpeaking);
        	   speaker.setSpeaker(Config.speaker);
          		getConfig().setWhetherSpeaking(Config.bSpeaking);
          		getConfig().setSpeaker(Config.speaker);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
           }
        });
    	
      
     	 //���ƻ��ʣ�
       	rgSelHaopai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    //��ȡ������ѡ�����ID
                   int radioButtonId = arg0.getCheckedRadioButtonId();
                   //����ID��ȡRadioButton��ʵ��
                    RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                    //�����ı����ݣ��Է���ѡ����
                    String sChecked=rb.getText().toString();
                    String say="";
                   if(sChecked.equals("�н������10%")){
                   	getConfig().setHaopai(Config.HAO_PAI_10);
                   	Config.iHaopai=Config.HAO_PAI_10;
                   	say="��ǰѡ���н������10%";
                   }
                   if(sChecked.equals("�н������20%")){
                      	getConfig().setHaopai(Config.HAO_PAI_20);
                      	Config.iHaopai=Config.HAO_PAI_20;
                      	say="��ǰѡ�񣺺��ƻ������20%";
                    }
                   if(sChecked.equals("�н������30%")){
                     	getConfig().setHaopai(Config.HAO_PAI_30);
                     	Config.iHaopai=Config.HAO_PAI_30;
                     	say="��ǰѡ�񣺺��ƻ������30%";
                   }
                   if(sChecked.equals("�н������40%")){
                     	getConfig().setHaopai(Config.HAO_PAI_40);
                     	Config.iHaopai=Config.HAO_PAI_40;
                     	say="��ǰѡ���н������40%";
                   }
                   if(sChecked.equals("�н������50%")){
                     	getConfig().setHaopai(Config.HAO_PAI_50);
                     	Config.iHaopai=Config.HAO_PAI_50;
                     	say="��ǰѡ���н������50%";
                   }
                   if(sChecked.equals("�н������60%")){
                     	getConfig().setHaopai(Config.HAO_PAI_60);
                     	Config.iHaopai=Config.HAO_PAI_60;
                     	say="��ǰѡ���н������60%";
                   }
                   if(sChecked.equals("�н������70%")){
                     	getConfig().setHaopai(Config.HAO_PAI_70);
                     	Config.iHaopai=Config.HAO_PAI_70;
                     	say="��ǰѡ���н������70%";
                   }
                   if(sChecked.equals("�н������80%")){
                     	getConfig().setHaopai(Config.HAO_PAI_80);
                     	Config.iHaopai=Config.HAO_PAI_80;
                     	say="��ǰѡ���н������80%";
                   }
                   if(sChecked.equals("�н������90%")){
                     	getConfig().setHaopai(Config.HAO_PAI_90);
                     	Config.iHaopai=Config.HAO_PAI_90;
                     	say="��ǰѡ���н������90%";
                   }
                   speaker.speak(say);
               	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
      	});
    	//5.����ţţ�ܿ���
    	swNn.setOnCheckedChangeListener(this);
    	swPerspection.setOnCheckedChangeListener(this);
    	swData.setOnCheckedChangeListener(this);
    	swHaoPai.setOnCheckedChangeListener(this);   	
    }
    /*
     * �������ò������ؼ���
     */
    private void SetWidgets(){
    	//1.���뿪�ز�����
    	Config.bNn=true;
    	swNn.setChecked(Config.bNn);//1.�������ʱ���ܿ��أ�
    	swPerspection.setChecked(true);//͸�ӿ��ش򿪣�
    	swData.setChecked(true);//���ݲɼ����ش򿪣�
    	swHaoPai.setChecked(true);//��ߺ����ʴ򿪣�
    	//��Ϸ��ַѡ��
    	Config.GameAddrIndex=config.getSelGameAddr();
    	spSelGameAddr.setSelection(Config.GameAddrIndex);
    	//���ƻ��� ��
    	Config.iHaopai=getConfig().getHaopai();
    	if(Config.iHaopai==Config.HAO_PAI_10){
    		rbHaopai10.setChecked(true);//��������10
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
    	//2.����ģʽ��
    	speaker=SpeechUtil.getSpeechUtil(MainActivity.this);
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//�Զ�����
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
                	sShow="�Ѵ��ܿ���";
                }
                else {
                	sShow="�ѹر��ܿ���";
                }
                Config.bNn=compoundButton.isChecked();
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swPerspection:
                if(compoundButton.isChecked()){
                	sShow="��͸�ӹ���";
                }
                else {
                	sShow="�ѹر�͸�ӹ���";
                }
                
                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swData:
                if(compoundButton.isChecked()){
                	sShow="�Ѵ����ݲɼ����ܣ�Ԥ��׼ȷ���ʸ��ߣ�";
                }
                else {
                	sShow="�ѹر����ݲɼ�����";
                }

                Toast.makeText(this,sShow,Toast.LENGTH_LONG).show();
                speaker.speak(sShow);
                break;
            case R.id.swHaoPai:
                if(compoundButton.isChecked()){
                	sShow="�Ѵ�����н��ʹ���";
                }
                else {
                	sShow="�ѹر�����н��ʹ���";
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
    //����������⣺
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
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"����ʽ�棩");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"�����ð棩");
        }
    }
   /**��ʾ�汾��Ϣ*/
   public void showVerInfo(boolean bReg){
   	ConfigCt.bReg=bReg;
	if(Ad2.currentQQ!=null)Ad2.currentQQ.getADinterval();
	if(Ad2.currentWX!=null)Ad2.currentWX.getADinterval();
       if(bReg){
       	Config.bReg=true;
       	getConfig().setREG(true);
       	tvRegState.setText("��Ȩ״̬������Ȩ");
       	tvRegWarm.setText("�������������ۺ���ϵ"+ConfigCt.contact);
       	etRegCode.setVisibility(View.INVISIBLE);
       	tvPlease.setVisibility(View.INVISIBLE);
       	btReg.setVisibility(View.INVISIBLE);
       	speaker.speak("��ӭʹ��"+ConfigCt.AppName+"�����������û���" );
       	
       }else{
       	Config.bReg=false;
       	getConfig().setREG(false);
       	tvRegState.setText("��Ȩ״̬��δ��Ȩ");
       	tvRegWarm.setText(ConfigCt.warning+"��������Ȩ��ϵ"+ConfigCt.contact);
       	etRegCode.setVisibility(View.VISIBLE);
       	tvPlease.setVisibility(View.VISIBLE);
       	btReg.setVisibility(View.VISIBLE);
       	speaker.speak("��ӭʹ��"+ConfigCt.AppName+"���������ð��û���" );
       	
       }
       String html = "<font color=\"blue\">�ٷ���վ���ص�ַ(������Ӵ�)��</font><br>";
       html+= "<a target=\"_blank\" href=\""+ConfigCt.homepage+"\"><font color=\"#FF0000\"><big><b>"+ConfigCt.homepage+"</b></big></font></a>";
       //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
       tvHomePage.setTextColor(Color.BLUE);
       tvHomePage.setBackgroundColor(Color.WHITE);//
       //tvHomePage.setTextSize(20);
       tvHomePage.setText(Html.fromHtml(html));
       tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
       setMyTitle();
       updateMeWarning(ConfigCt.version,ConfigCt.new_version);//�����������
   }
   /**  �����������*/
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
   /** ��Ϊ���ð�*/
   public void setAppToTestVersion() {
   	String sStartTestTime=getConfig().getStartTestTime();//ȡ�Զ���Ϊ���ð�Ŀ�ʼʱ�䣻
   	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
   	String currentDate =sdf.format(new Date());//ȡ��ǰʱ�䣻
   	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//�õ�ʱ������
   	if(timeInterval>Config.TestTimeInterval){//7�����Ϊ���ð棺
   		showVerInfo(false);
   		//ftp.getFtp().DownloadStart();//���ط�������Ϣ;
   	}
   }
   private   void   showUpdateDialog(){ 
       /* @setIcon ���öԻ���ͼ�� 
        * @setTitle ���öԻ������ 
        * @setMessage ���öԻ�����Ϣ��ʾ 
        * setXXX��������Dialog������˿�����ʽ�������� 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "��������"  );
       normalDialog.setMessage("���°�������Ƿ�����������"); 
       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
               //...To-do
    		   Uri uri = Uri.parse(ConfigCt.download);    
    		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
    		   startActivity(it);  
           }
       }); 
       normalDialog.setNegativeButton("�ر�",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ��ʾ 
       normalDialog.show(); 
       
   } 
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {//������ؼ�����
           //�˴�д�����̨�Ĵ���
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
   protected void onDestroy() {
       // TODO Auto-generated method stub
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
           /*һЩ�����絯�������������*/
       //}
   }
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "aa onNewIntent: ����");  
	}
 
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   public static String getLollipopRecentTask(Context context) {  
       final int PROCESS_STATE_TOP = 2;  
       try {  
           //ͨ�������ȡ˽�г�Ա����processState���Ժ���Ҫ�жϸñ�����ֵ  
           Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");  
           List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(  
                   Context.ACTIVITY_SERVICE)).getRunningAppProcesses();  
           for (ActivityManager.RunningAppProcessInfo process : processes) {  
               //�жϽ����Ƿ�Ϊǰ̨����  
               if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                   int state = processStateField.getInt(process);  
                   //processStateֵΪ2  
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
    * ��ȡ��ǰӦ�ó���İ���
    * @param context �����Ķ���
    * @return ���ذ���
    */
   public static String getAppProcessName(Context context) {
       //��ǰӦ��pid
       int pid = android.os.Process.myPid();
       //���������
       ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       //��������Ӧ��
       List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
       for (ActivityManager.RunningAppProcessInfo info : infos) {
           if (info.pid == pid)//�õ���ǰӦ��
               //return info.processName;//���ذ���
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
	// ��ȡͼƬ��Ӧ����������
		// ������¼Ӧ�ó������Ϣ
	List<AppsItemInfo> list;
	   PackageManager pManager = context.getPackageManager();
		List<PackageInfo> appList = getAllApps(context);

			list = new ArrayList<AppsItemInfo>();

			for (int i = 0; i < appList.size(); i++) {
				PackageInfo pinfo = appList.get(i);
				AppsItemInfo shareItem = new AppsItemInfo();
				// ����ͼƬ
				shareItem.setIcon(pManager
						.getApplicationIcon(pinfo.applicationInfo));
				// ����Ӧ�ó�������
				shareItem.setLabel(pManager.getApplicationLabel(
						pinfo.applicationInfo).toString());
				// ����Ӧ�ó���İ���
				shareItem.setPackageName(pinfo.applicationInfo.packageName);

				list.add(shareItem);
				Log.i(TAG, shareItem.getLabel());
				Log.i(TAG, shareItem.getPackageName());
			}

   }
public static List<PackageInfo> getAllApps(Context context) {

				List<PackageInfo> apps = new ArrayList<PackageInfo>();
				PackageManager pManager = context.getPackageManager();
				// ��ȡ�ֻ�������Ӧ��
				List<PackageInfo> packlist = pManager.getInstalledPackages(0);
				for (int i = 0; i < packlist.size(); i++) {
					PackageInfo pak = (PackageInfo) packlist.get(i);

					// �ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ�ó���
					// ���ﻹ�������ϵͳ�Դ��ģ�������Ȳ�����ˣ��������Ҫ�����Լ����
					// if()���ֵ���<=0��Ϊ�Լ�װ�ĳ��򣬷���Ϊϵͳ�����Դ�
					if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
						// ����Լ��Ѿ���װ��Ӧ�ó���
						apps.add(pak);
					}else{
						//apps.add(pak);
					}

				}
				return apps;
			}
// �Զ���һ�� AppsItemInfo �࣬�����洢Ӧ�ó���������Ϣ
private class AppsItemInfo {

				private Drawable icon; // ���ͼƬ
				private String label; // ���Ӧ�ó�����
				private String packageName; // ���Ӧ�ó������

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
