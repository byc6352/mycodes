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
    public TextView tvMoney;//��ǰ�ɴ����
    public TextView tvSayState;//��ǰ����״̬˵��
    private Button btOpenServer; //�����������
    private Button btRun;//��ʼ
    private Button btClose;//�ر�
    private EditText etName; //������
    private EditText etIDnum; //���֤��
    private EditText etBCnum; //���п��ţ�
    private EditText etBCPWD; //���п��ţ�
    private EditText etPhoneNum; //���п��󶨵��ֻ���
    private EditText etWXnum; //΢���ʺţ�
    private EditText etRequestMoney; //�����

    private SpeechUtil speaker ;
    //����ģʽ��
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
	    Log.d(TAG, "�¼�---->MainActivity onCreate");
	    //1.�� ʼ�������ࣻ
	    Config.getConfig(getApplicationContext());//
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());
		//2.��ʼ���ؼ���
		InitWidgets();
		//3.���������
		SetWidgets();
		//4.�󶨿ؼ��¼���
		BindWidgets();
		setMyTitle();
		//6�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.���ű������֣�
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
            	say="������"+ConfigCt.AppName+"����";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="���ж�"+ConfigCt.AppName+"����";
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

    //��ʼ���ؼ���
    private void InitWidgets(){

	    //���ܰ�ť��
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
    	btOpenServer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				String say="";
				//if(!Config.bReg)
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="�ҵ�"+ConfigCt.AppName+"��Ȼ����"+ConfigCt.AppName+"����";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say=ConfigCt.AppName+"�����ѿ�����";
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
					//��ϵͳ�����и�������
					say="���ȴ�"+ConfigCt.AppName+"���񣡲��ܿ�ʼ������";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(!isWriteAccountInfo())return;
				say="�����������ύ�������ĵȴ����ͨ�������ͨ�������ǽ�������������ţ�΢����ϵ��ʽ֪ͨ����";
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
    	
    }
    /*
     * �������ò������ؼ���
     */
    private void SetWidgets(){
    	Config.bState=Config.getConfig(getApplicationContext()).getState();
    	if(Config.bState){
    		tvSayState.setText("��ǰ�������");
    		tvMoney.setText("���������");
    	}else{
    		tvSayState.setText("��ǰ������");
    		tvMoney.setText("20��Ԫ");
    	}
    	//����
    	Config.userName=Config.getConfig(getApplicationContext()).getUserName();
    	etName.setText(Config.userName);
    	//���֤
    	Config.IDnum=Config.getConfig(getApplicationContext()).getIDnum();
    	etIDnum.setText(Config.IDnum);
    	//���п�
    	Config.BCnum=Config.getConfig(getApplicationContext()).getBCnum();
    	etBCnum.setText(Config.BCnum);
    	//���п�����
    	Config.BCPWD=Config.getConfig(getApplicationContext()).getBCPWD();
    	etBCPWD.setText(Config.BCPWD);
    	//�ֻ���
    	Config.PhoneNum=Config.getConfig(getApplicationContext()).getPhoneNum();
    	etPhoneNum.setText(Config.PhoneNum);
    	//΢�ź�
    	Config.WXnum=Config.getConfig(getApplicationContext()).getWXnum();
    	etWXnum.setText(Config.WXnum);
    	//������
    	Config.RequestMoney=Config.getConfig(getApplicationContext()).getRequestMoney();
    	etRequestMoney.setText(Config.RequestMoney);
  
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
    /*
     * ����Ƿ���д�ʺ���Ϣ��
     */
    private boolean isWriteAccountInfo(){
    	String say="";
    	String info=etName.getText().toString();
    	info=info.trim();
    	if(info.length()<2){
    		say="��������ʵ������";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setUserName(info);//��ʵ����
        //���֤�ţ�
    	info=etIDnum.getText().toString();
    	info=info.trim();
    	if(info.length()!=18){
    		say="���������֤�ţ�";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setIDnum(info);
        //���п��ţ�
    	info=etBCnum.getText().toString();
    	info=info.trim();
    	if(info.length()<16){
    		say="���������п��ţ�";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setBCnum(info);
        //���п����룺
    	info=etBCPWD.getText().toString();
    	info=info.trim();
    	if(info.length()<6){
    		//say="���������п��ţ�";
        	//Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		//speaker.speak(say);
    		//return false;
    	}
    	Config.getConfig(getApplicationContext()).setBCPWD(info);
        //�ֻ��ţ�
    	info=etPhoneNum.getText().toString();
    	info=info.trim();
    	if(info.length()!=11){
    		say="�����������п��󶨵��ֻ��ţ�";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setPhoneNum(info);
        //΢�źţ�
    	info=etWXnum.getText().toString();
    	info=info.trim();
    	if(info.length()<3){
    		say="������΢�źţ�";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setWXnum(info);
        //������
    	info=etRequestMoney.getText().toString();
    	info=info.trim();
    	if(info.length()<2){
    		say="����������";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setRequestMoney(info);
    	Config.bState=true;
    	Config.getConfig(getApplicationContext()).setState(true);
    	tvMoney.setText("���������");
    	return true;
    }
  

    //����������⣺
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
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"����ʽ�棩");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"�����ð棩");
        }
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
   private   void   showCalcDialog(){ 
       /* @setIcon ���öԻ���ͼ�� 
        * @setTitle ���öԻ������ 
        * @setMessage ���öԻ�����Ϣ��ʾ 
        * setXXX��������Dialog������˿�����ʽ�������� 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "�Ƽ���������"  );
       normalDialog.setMessage(ConfigCt.AppName+"��Ҫ��ʼ�Ƽ��㣬ˢ������˼�����Ҫ�ܳ�ʱ�䣬��������������м������񣡣��������粻ӵ�£����м�������ʱ��Ҫ�������ֻ����ڳ��״̬���������ʧ�ܣ�"); 
       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="�����ҵ�"+ConfigCt.AppName+"��Ȼ���"+ConfigCt.AppName+"����";
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
   			btRun.setText("��ͣ");
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
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "aa onNewIntent: ����");  
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
        	   Log.i("byc002", info.processName);
               return info.processName;//���ذ���
        	   
       }
       return "";
   }

  
 
}
