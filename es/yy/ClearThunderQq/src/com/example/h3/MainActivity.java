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
	
    // ����SeekBar �� TextView���� 
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

		//my codes
		//���ԣ�
		//FloatingWindowPic.getFloatingWindowPic(this,R.layout.float_click_delay_show);

		//0.��ʼ��
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
	    //�Ƿ��Զ����أ�
	    rgSelReturn = (RadioGroup)this.findViewById(R.id.rgSelReturn);
	    rbAutoReturn=(RadioButton)findViewById(R.id.rbAutoReturn);
	    rbManualReturn=(RadioButton)findViewById(R.id.rbManualReturn);
	    //����ģʽ��
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);

	    //Log.d(TAG, "�¼�---->��TTS");
	    Config.getConfig(getApplicationContext());//�� ʼ�������ࣻ
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());
	    //0��1���ñ�

		//1���رճ���ť
		TAG=Config.TAG;
		Log.d(TAG, "�¼�---->MainActivity onCreate");
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
					String say="���ȴ�QQ���׷��񣬲��ܿ�ʼ��Ϸ����";
					Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				Log.d(TAG, "�¼�---->��QQ");
				OpenWechat();
				Config.JobState=Config.STATE_NONE;
				speaker.speak("����QQ��ף�������죡");
				if(Config.bAutoClearThunder){
					//speeker.speak("�ֶ�����ģʽ���ܣ�������ˣ��ֶ�����������Ҫ�������������ר������");
					speaker.speak("ȫ�Զ�����ģʽ���ܣ�������ˣ�");
					speaker.speak("��Ҫ�������������ר��������������");
				}else{
					speaker.speak("���Զ�����ģʽ���ܣ�������ˣ�");
					speaker.speak("�ֶ�����������Ҫ�������������ר������");
				//System.exit(0);
				}
				MainActivity.this.finish();
			}
		});//btn.setOnClickListener(
		//2���򿪸�������ť
		//btStart = (Button) findViewById(R.id.btStart); 
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//1�жϷ����Ƿ�򿪣�
				mBackgroundMusic.stopBackgroundMusic();
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					Toast.makeText(MainActivity.this, "�ҵ�QQ����ר�ң�Ȼ����QQ���׷���", Toast.LENGTH_LONG).show();
					speaker.speak("���ҵ�QQ����ר�ң�Ȼ����QQ���׷���");
				}else{
					Toast.makeText(MainActivity.this, "QQ���׷����ѿ������������¿����������������", Toast.LENGTH_LONG).show();
					speaker.speak("QQ���׷����ѿ������������¿����������������");
				}
				//2������ʱ����
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				//speeker.speak("�ر�����ר�ҡ�");
				if(Config.DEBUG){
					//shotScreen();
				}
				mBackgroundMusic.stopBackgroundMusic();
				moveTaskToBack(true);
			}
		});//btn.setOnClickListener(
		//3��SeekBar����
        mSeekBar.setOnSeekBarChangeListener(this); 

        //Config.getConfig(this).SetWechatOpenDelayTime(10);
        //4.�Ƿ�ע�ᴦ����ʾ�汾��Ϣ(��������)��
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//��ʼ��������֤��
			Sock.getSock(MainActivity.this).VarifyStart();
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
		//6�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        //7.���ű������֣�
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.���ò���
        SetParams();
        //9.��ȡ��Ļ�ֱ��ʣ�
        getResolution2();
        //10.��Ϊ���ð棻
        setAppToTestVersion();
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
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speaker.speak("���������׷���");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("���ж����׷���");
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
				 Toast.makeText(MainActivity.this, "������������Ȩ�ޣ�", Toast.LENGTH_LONG).show();
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
	
	//SeekBar�ӿڣ�
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, 
            boolean fromUser) {// ���϶���--��ֵ�ڸı� 
        // progressΪ��ǰ��ֵ�Ĵ�С 
    	tvSpeed.setText("�����ò��ٶ�:��ǰ���ӳ٣�" + progress); 
    	
    } 
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {// ���϶��л���ô˷��� 
    	//mSpeed.setText("xh���ڵ���"); 
    } 
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {// ֹͣ�϶� 
    	//mSpeed.setText("xhֹͣ����"); 
    	//���浱ǰֵ
    	Config.getConfig(this).SetWechatOpenDelayTime(seekBar.getProgress());
    	speaker.speak("��ǰ���ӳ٣�" + seekBar.getProgress());
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

    //���ò�����
    private void SetParams(){
    	//�����ؼ���ʼ��
    	Config.iMoneySay=getConfig().getMoneySayPos();
    	if(Config.iMoneySay==Config.KEY_THUNDER_TAIL){
    		rbTail.setChecked(true);//�����βΪ��
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_HEAD){
    		rbHead.setChecked(true);//�������Ϊ��
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_AUTO_READ){
    		rbAutoReadThunderPos.setChecked(true);//����ʶ���׵�λ��
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_MIDDLE){
    		rbAutoReadThunderPos.setChecked(true);//�м� ��λ��
    	}else if(Config.iMoneySay==Config.KEY_THUNDER_IS_SAY){
    		rbSayIsThunder.setChecked(true);//����������
    	}
    	
    	Config.iMoneyThunderPos=getConfig().getMoneyValuePos();
    	if(Config.iMoneyThunderPos==Config.KEY_THUNDER_FEN){
    		rbFen.setChecked(true);
    	}else if(Config.iMoneyThunderPos==Config.KEY_THUNDER_JIAO){
    		rbJiao.setChecked(true);
    	}else if(Config.iMoneyThunderPos==Config.KEY_THUNDER_YUAN){
    		rbYuan.setChecked(true);
    	}else if(Config.iMoneyThunderPos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		rbTwoTailAdd.setChecked(true);//��β �� ���Ϊ��
    	}
    	//����ģʽ��
    	int iClearThunderMode=getConfig().getClearThunderMode();
    	if(iClearThunderMode==Config.KEY_AUTO_CLEARE_THUNDER){
    		rbAutoClearThunder.setChecked(true);
    		Config.bAutoClearThunder=true;
    	}else if(iClearThunderMode==Config.KEY_SEMI_AUTO_CLEARE_THUNDER){
    		rbSemiAutoClearThunder.setChecked(true);
    		Config.bAutoClearThunder=false;
    	}
    	//����˫�ף�
    	int iThunderNum=getConfig().getThunderNum();
    	if(iThunderNum==Config.KEY_THUNDER_SINGLE){
    		rbSingleThunder.setChecked(true);//����
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
    	//�Ƿ����󷵻أ�
    	boolean bReturn=getConfig().getUnpackReturn();
    	if(bReturn==Config.KEY_AUTO_RETURN){
    		rbAutoReturn.setChecked(true);//�Զ�����
    	}else if(bReturn==Config.KEY_MANUAL_RETURN){
    		rbManualReturn.setChecked(true);
    	}
    	Config.bAutoReturn=bReturn;
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
    	//�����ʱ��
    	Config.iDelayTime=getConfig().getWechatOpenDelayTime();
    	mSeekBar.setProgress(Config.iDelayTime);
    	 //������λ��
    	rgMoneyValue.setOnCheckedChangeListener(new OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup arg0, int arg1) {
                 // TODO Auto-generated method stub
                 //��ȡ������ѡ�����ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //����ID��ȡRadioButton��ʵ��
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                 //�����ı����ݣ��Է���ѡ����
                String sChecked=rb.getText().toString();
                 //tv.setText("�����Ա��ǣ�" + rb.getText());
                String say="";
                if(sChecked.equals("��Ϊ��")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_FEN;
                	say="��ǰ���ã���Ϊ�ס�";
                
                }
                if(sChecked.equals("��Ϊ��")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_JIAO;
                	say="��ǰ���ã���Ϊ�ס�";
                
                }
                if(sChecked.equals("ԪΪ��")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_YUAN;
                	say="��ǰ���ã�ԪΪ�ס�";
                
                }
                if(sChecked.equals("����λβ�����Ϊ��")){
                	Config.iMoneyThunderPos=Config.KEY_THUNDER_TWO_TAIL_ADD;
                	say="��ǰ���ã�����λβ�����ֵ�ĸ�λ��Ϊ�ס�";
                
                }
                getConfig().setMoneyValuePos(Config.iMoneyThunderPos);
                speaker.speak(say);
            	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();	
                	
            }
         });
    	 //�����
    	rgMoneySay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
               if(sChecked.equals("���ں�������(���磺20/5)")){
            	   Config.iMoneySay=Config.KEY_THUNDER_TAIL;

               		say="��ǰ���ã����ں�������λ�á�";
               }
               if(sChecked.equals("���ں����ǰ��(���磺5/20)")){
            	   	Config.iMoneySay=Config.KEY_THUNDER_HEAD;
     
               		say="��ǰ���ã����ں����ǰ��λ�á�";
               }
               if(sChecked.equals("����ʶ���׵�λ��(���磺20/5,5/20���)")){
            	   	Config.iMoneySay=Config.KEY_THUNDER_AUTO_READ;
 
                  	say="��ǰ���ã�����ʶ���׵�λ�á�";
               }
               if(sChecked.equals("���ں�����м�(���磺20/5123,5Ϊ��)")){
           	   	Config.iMoneySay=Config.KEY_THUNDER_MIDDLE;
      
                 	say="��ǰ���ã�������м�λΪ�ס�";
              }
               if(sChecked.equals("����Ｔ����(���磺5)")){
              	   	Config.iMoneySay=Config.KEY_THUNDER_IS_SAY;
                    	
                    	say="��ǰ���ã������Ϊ�ף���������޽�";
                 }
               getConfig().setMoneySayPos(Config.iMoneySay);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
           }
        });
   	 //��˫�ף�
    	rgThunderNum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //��ȡ������ѡ�����ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //����ID��ȡRadioButton��ʵ��
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //�����ı����ݣ��Է���ѡ����
                String sChecked=rb.getText().toString();
                //tv.setText("�����Ա��ǣ�" + rb.getText());
                String say="";
                if(sChecked.equals("����")){
             	   Config.iThunderNum=Config.KEY_THUNDER_SINGLE;
             	   say="��ǰ���ã����ס�";
                
                }
                if(sChecked.equals("˫��")){
             	   Config.iThunderNum=Config.KEY_THUNDER_DOUBLE;
             	   say="��ǰ���ã�˫�ס�";
                }
                if(sChecked.equals("����")){
             	   Config.iThunderNum=Config.KEY_THUNDER_THREE;
             	   say="��ǰ���ã����ס�";
                }
                if(sChecked.equals("����")){
             	   Config.iThunderNum=Config.KEY_THUNDER_FOUR;
             	   say="��ǰ���ã����ס�";
                }
                if(sChecked.equals("����")){
             	   Config.iThunderNum=Config.KEY_THUNDER_FIVE;
             	   say="��ǰ���ã����ס�";
                }
                if(sChecked.equals("��������")){
               	   Config.iThunderNum=Config.KEY_THUNDER_ANY;
               	   say="��ǰ���ã���������1��5��֮�䣬��ȷ����";
                  }
             	getConfig().setThunderNum(Config.iThunderNum);
                	speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
           }
        });
    	 //�Ƿ������Զ����أ�
    	rgSelReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   //��ȡ������ѡ�����ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //����ID��ȡRadioButton��ʵ��
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //�����ı����ݣ��Է���ѡ����
                   String sChecked=rb.getText().toString();
                  if(sChecked.equals("������Զ�����")){
                  	getConfig().setUnpackReturn(Config.KEY_AUTO_RETURN);
                  	Config.bAutoReturn=true;
                  	speaker.speak("��ǰ���ã�������Զ����ء�");
                  	Toast.makeText(MainActivity.this, "��ǰ���ã�������Զ����ء�", Toast.LENGTH_LONG).show();
                  }
                  if(sChecked.equals("������ֶ�����")){
                  	getConfig().setUnpackReturn(Config.KEY_MANUAL_RETURN);
                  	Config.bAutoReturn=false;
                  	speaker.speak("��ǰ���ã�������ֶ����ء�");
                  	Toast.makeText(MainActivity.this, "��ǰ���ã�������ֶ����ء�", Toast.LENGTH_LONG).show();
                  }
              }
           });
   	 //����ģʽ
    	rgClearThunderMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //��ȡ������ѡ�����ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //����ID��ȡRadioButton��ʵ��
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //�����ı����ݣ��Է���ѡ����
                String sChecked=rb.getText().toString();
                //tv.setText("�����Ա��ǣ�" + rb.getText());
               if(sChecked.equals("ȫ�Զ�����")){
               	getConfig().setClearThunderMode(Config.KEY_AUTO_CLEARE_THUNDER);
               	Config.bAutoClearThunder=true;
               	speaker.speak("��ǰ���ã�ȫ�Զ����ס�");
               	Toast.makeText(MainActivity.this, "��ǰ���ã�ȫ�Զ����ס�", Toast.LENGTH_LONG).show();
               }
               if(sChecked.equals("���Զ�����")){
               	getConfig().setClearThunderMode(Config.KEY_SEMI_AUTO_CLEARE_THUNDER);
               	Config.bAutoClearThunder=false;
               	speaker.speak("��ǰ���ã����Զ����ס�");
               	Toast.makeText(MainActivity.this, "��ǰ���ã����Զ����ס�", Toast.LENGTH_LONG).show();
               }
           }
        });
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
      //�˴�д�����̨�Ĵ���
	  // moveTaskToBack(true); 
  }
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {//������ؼ�����
          //�˴�д�����̨�Ĵ���
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
	private   void   showCalcDialog(){ 
	        /* @setIcon ���öԻ���ͼ�� 
	         * @setTitle ���öԻ������ 
	         * @setMessage ���öԻ�����Ϣ��ʾ 
	         * setXXX��������Dialog������˿�����ʽ�������� 
	         */ 
	        final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
	        normalDialog.setIcon(R.drawable.ic_launcher); 
	        normalDialog.setTitle(  "����������������"  );
	        normalDialog.setMessage(ConfigCt.AppName+"��Ҫ�����������ݣ���ʹ���׸��Ӿ�׼���˼�����Ҫ�ܳ�ʱ�䣬��������˯��ǰ���м������񣡣����м�������ʱ��Ҫ�������ֻ����ڳ��״̬���������ʧ�ܣ�"); 
	        normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
	            @Override 
	            public void onClick(DialogInterface dialog,int which){ 
	            	if(!QiangHongBaoService.isRunning()) {
	    				String say="���ȴ�"+ConfigCt.AppName+"�����������"+ConfigCt.AppName+"���ݣ�";
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
	        normalDialog.setNegativeButton("�ر�",new DialogInterface.OnClickListener(){ 
	            @Override 
	            public void onClick(DialogInterface dialog,   int   which){ 
	            //...To-do 
	            } 
	        }); 
	        // ��ʾ 
	        normalDialog.show(); 
	} 
}
