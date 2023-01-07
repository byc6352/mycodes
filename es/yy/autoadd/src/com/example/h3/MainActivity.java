package com.example.h3;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.autoadd.R;
import com.example.h3.util.SpeechUtil;
import com.example.h3.util.ftp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.content.BroadcastReceiver;
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
import android.widget.RadioGroup.OnCheckedChangeListener;; 

public class MainActivity extends Activity{

	private String TAG = "byc001";
	
    // ����SeekBar �� TextView���� 
  
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    public Button btReg;
    private Button btConcel;
    private Button btStart; 
    private Button btClose;
    public EditText etRegCode; 
    public TextView tvPlease;
    private com.example.h3.util.SpeechUtil speaker ;
   
    public EditText etAddFriendSay; 
    public EditText etScrollGalleryDownDate;
    //����ģʽ��
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    
    private PowerManager pManager;
    private PowerManager.WakeLock  mWakeLock;// = pManager.newWakeLock

    
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		
		//my codes
		keepWake();
	    //Log.d(TAG, "�¼�---->��TTS");
		TAG=Config.TAG;
	    Config.getConfig(MainActivity.this);//�� ʼ�������ࣻ
	    speaker=SpeechUtil.getSpeechUtil(MainActivity.this);
        //2����ȡ�ֱ���
        getResolution2();
		//3.�ؼ���ʼ��
		SetParams();
        //4.�Ƿ�ע�ᴦ������ʾ�汾��Ϣ(��������)��
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//��ʼ��������֤��
			Sock.getSock(MainActivity.this).VarifyStart();

		//6�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.���ű������֣�
        mBackgroundMusic=BackgroundMusic.getInstance(this);
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //7.7����Ϊ���ð�
        setAppToTestVersion();
		
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            String sayConnect="������ͨ��΢�̷���";
            String sayDisConnect="�ж�ͨ��΢�̷���";
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	
            	speaker.speak(sayConnect);
            	Toast.makeText(MainActivity.this, sayConnect, Toast.LENGTH_SHORT).show();
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak(sayDisConnect);
            	Toast.makeText(MainActivity.this, sayDisConnect, Toast.LENGTH_SHORT).show();
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
			Intent intent=new Intent();
			//Intent intent =new Intent(Intent.ACTION_VIEW,uri);
			intent.setAction("android.intent.action.VIEW");
			Uri content_url=Uri.parse(Config.homepage);
			intent.setData(content_url);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
    public boolean OpenSoft(String PackageName){
    	Intent intent = new Intent(); 
    	PackageManager packageManager = this.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(PackageName); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	this.startActivity(intent);
    	return true;
    }
    //���ò�����
    private void SetParams(){
    	//1�������ؼ���ʼ��+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	   
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    btConcel=(Button)findViewById(R.id.btConcel);
	    btStart=(Button) findViewById(R.id.btStart); 
	    btClose=(Button)findViewById(R.id.btClose);
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 
	    
	    etAddFriendSay=(EditText) findViewById(R.id.etAddFriendSay); 
	    etScrollGalleryDownDate=(EditText) findViewById(R.id.etScrollGalleryDownDate);
	    //����ģʽ��
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);   	
    	//2��++++++++++++++++++++++++�������+++++++++++++++++++++++++++++++++++++++++++++
	    Config.sAddFriendSay=getConfig().getAddFriendSay();
	    etAddFriendSay.setText(Config.sAddFriendSay);
	    Config.sScrollGalleryDownDate=getConfig().getScrollGalleryDownDate();
	    etScrollGalleryDownDate.setText(Config.sScrollGalleryDownDate);
    	//����ģʽ��
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

    	//++++++++++++++++++++++++++++�󶨲���+++++++++++++++++++++++++++++++++++++++++++++++++
		//1������΢��
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				mBackgroundMusic.stopBackgroundMusic();
				Config.sAddFriendSay=MainActivity.this.etAddFriendSay.getText().toString();
				Config.sScrollGalleryDownDate=MainActivity.this.etScrollGalleryDownDate.getText().toString();
				getConfig().setAddFriendSay(Config.sAddFriendSay);
				getConfig().setScrollGalleryDownDate(Config.sScrollGalleryDownDate);
				Log.d(TAG, "�¼�---->��΢��");
				OpenWechat();
				Config.iWorkPlatform=Config.WORK_PLATFORM_WECHAT;
				Config.JobState=Config.STATE_NONE;
				speaker.speak("����΢��");
			}
		});//btn.setOnClickListener(
		//2���򿪸�������ť
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//1�жϷ����Ƿ�򿪣�
				mBackgroundMusic.stopBackgroundMusic();
				Config.sAddFriendSay=MainActivity.this.etAddFriendSay.getText().toString();
				getConfig().setAddFriendSay(Config.sAddFriendSay);
				String say="";
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					startActivity(intent);
					say="�ҵ�ͨ��΢�̣�Ȼ����ͨ��΢�̷���";
				}else{
					say="ͨ��΢�̷����ѿ������������¿�����������������";
				}
				Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
				speaker.speak(say);
			}
		});//startBtn.setOnClickListener(
	
		//-------------------------------����QQ--------------------------------------------------
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//System.exit(0);
				mBackgroundMusic.stopBackgroundMusic();
				Config.sAddFriendSay=MainActivity.this.etAddFriendSay.getText().toString();
				getConfig().setAddFriendSay(Config.sAddFriendSay);
				Log.d(TAG, "�¼�---->��QQ");
				OpenSoft(Config.QQ_PACKAGENAME);
				Config.iWorkPlatform=Config.WORK_PLATFORM_QQ;
				Config.JobState=Config.STATE_NONE;
				speaker.speak("����QQ");
			}
		});//btn.setOnClickListener(
        //5��ע�����̣�
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
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
   	 //���� ģʽ
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
    //�����������⣺
   public void setMyTitle(){
        if(Config.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		  Config.version =info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name)+" v" + Config.version+"����ʽ�棩");
        }else{
      	  setTitle(getString(R.string.app_name)+" v" + Config.version+"�����ð棩");
        }
    }
   /**��ʾ�汾��Ϣ*/
   public void showVerInfo(boolean bReg){
       if(bReg){
       	Config.bReg=true;
       	getConfig().setREG(true);
       	tvRegState.setText("��Ȩ״̬������Ȩ");
       	tvRegWarm.setText("���������ۺ���ϵ"+Config.contact);
       	etRegCode.setVisibility(View.INVISIBLE);
       	tvPlease.setVisibility(View.INVISIBLE);
       	btReg.setVisibility(View.INVISIBLE);
       	speaker.speak("��ӭʹ��ͨ��΢�� �����������û���" );
       	tvRegWarm.setVisibility(View.VISIBLE);
       	
       }else{
       	Config.bReg=false;
       	getConfig().setREG(false);
       	tvRegState.setText("��Ȩ״̬��δ��Ȩ");
       	tvRegWarm.setText(Config.warning+"��������Ȩ��ϵ"+Config.contact);
       	etRegCode.setVisibility(View.VISIBLE);
       	tvPlease.setVisibility(View.VISIBLE);
       	btReg.setVisibility(View.VISIBLE);
       	speaker.speak("��ӭʹ��ͨ��΢�̣��������ð��û���" );
       	tvRegWarm.setVisibility(View.VISIBLE);
       	
       }
       String html = "<font color=\"blue\">�ٷ���վ���ص�ַ(������Ӵ�)��</font><br>";
       html+= "<a target=\"_blank\" href=\""+Config.homepage+"\"><font color=\"#FF0000\"><big><b>"+Config.homepage+"</b></big></font></a>";
       //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
       tvHomePage.setTextColor(Color.BLUE);
       tvHomePage.setBackgroundColor(Color.WHITE);//
       //tvHomePage.setTextSize(20);
       tvHomePage.setText(Html.fromHtml(html));
       tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
       setMyTitle();//�������� ����
       updateMeWarning(Config.version,Config.new_version);//������������
   }
   /**  ������������*/
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
   		ftp.getFtp().DownloadStart();//���ط�������Ϣ;
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
       normalDialog.setMessage("���°��������Ƿ�����������"); 
       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
               //...To-do
    		   Uri uri = Uri.parse(Config.download);    
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
   private void keepWake(){
       pManager = ((PowerManager) getSystemService(POWER_SERVICE));  
       mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK  
               | PowerManager.ON_AFTER_RELEASE, TAG);  
       mWakeLock.acquire(); 
   }
   @Override  
   protected void onResume() {  
       super.onResume();  
       //pManager = ((PowerManager) getSystemService(POWER_SERVICE));  
       //mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK  
       //        | PowerManager.ON_AFTER_RELEASE, TAG);  
       //mWakeLock.acquire();  
   }  
     
   @Override  
   protected void onPause() {  
       super.onPause();  
         
       if(null != mWakeLock){  
           //mWakeLock.release();  
       }  
   }  
}