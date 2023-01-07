package com.example.h3;

import com.byc.PutThunderYx.R;
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
	
    // ����SeekBar �� TextView���� 
    private SeekBar mSeekBar;
    private TextView tvSpeed; 
    public TextView tvRegState;
    public TextView tvRegWarm;
    public Button btReg;
    private Button btConcel;
    private Button btStart; 
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speeker ;
    
    //----------------------------------------------
    private EditText etMoneyValue;			//�������Է�Ϊ��λ��
    private EditText etLuckyMoneyNum;		//����
    private EditText etPWD;					//֧�����룺
    private EditText etLuckyMoneySay;		//����ϳ��ֵ����֣�

    private RadioGroup rgThunderPos;		//����λ��
    private RadioButton rbFen;
    private RadioButton rbJiao;
    private RadioButton rbYuan;
    private FlowRadioGroup rgThunderNum;		//�����ó��ֵ�������
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
    private RadioGroup rgRobTail;
    private RadioButton rbRobTail;
    private RadioButton rbNoRobTail;
    
    
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//my codes
		//���ԣ�

		//0.��ʼ��
	    mSeekBar=(SeekBar) findViewById(R.id.seekBar1);
	    tvSpeed = (TextView) findViewById(R.id.tvSpeed); 
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    btReg=(Button)findViewById(R.id.btReg);
	    btConcel=(Button)findViewById(R.id.btConcel);
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
	    rgRobTail = (RadioGroup)this.findViewById(R.id.rgRobTail);
	    rbRobTail=(RadioButton)findViewById(R.id.rbRobTail);
	    rbNoRobTail=(RadioButton)findViewById(R.id.rbNoRobTail);	    
	    //��ȡ�ֱ���:
	    getResolution2();
	    Log.d(TAG, "�¼�---->��TTS");
	    speeker=SpeechUtil.getSpeechUtil(MainActivity.this);
	    //0��1���ñ���
        String version = "";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = " v" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            
        }

        setTitle(getString(R.string.app_name) + version);
        setTitle(getString(R.string.Title) + version);
		//1������΢�Ű�ť
		TAG=Config.TAG;
		Log.d(TAG, "�¼�---->MainActivity onCreate");
		//btConcel=(Button)this.findViewById(R.id.btConcel);
		btConcel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				if(GetParams()){
					//Config.bSendLuckyMoney=true;
					Log.d(TAG, "�¼�---->��΢��");
					OpenWechat();
					speeker.speak("����΢�š����׿�ʼ��");
				}
			}
		});//btn.setOnClickListener(
		//2���򿪸�������ť
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!GetParams()){return;}
				//1�жϷ����Ƿ�򿪣�
				mBackgroundMusic.stopBackgroundMusic();
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					startActivity(intent);
					Toast.makeText(MainActivity.this, "�ҵ�����ר�ң�Ȼ�������׷���", Toast.LENGTH_LONG).show();
					speeker.speak("���ҵ�����ר�ң�Ȼ�������׷���");
				}else{
					Toast.makeText(MainActivity.this, "���׷����ѿ������������¿����������������", Toast.LENGTH_LONG).show();
					speeker.speak("���׷����ѿ������������¿����������������");
				}
				//2������ʱ����
			}
		});//startBtn.setOnClickListener(
		//3��SeekBar����
        mSeekBar.setOnSeekBarChangeListener(this); 

        Config.getConfig(this).SetWechatOpenDelayTime(0);
        //4.�Ƿ�ע�ᴦ��
        Config.bReg=false;
        if(getConfig().getREG()){
        	if(getConfig().getPhoneID().equals(getConfig().getPhoneIDFromHard())){
        		Config.bReg=true;
        	}
        }
        if(Config.bReg){
        	tvRegState.setText("��Ȩ״̬������Ȩ");
        	tvRegWarm.setText("���������ۺ���ϵQQ��2481878920");
        	etRegCode.setVisibility(View.INVISIBLE);
        	tvPlease.setVisibility(View.INVISIBLE);
        	btReg.setVisibility(View.INVISIBLE);
        	speeker.speak("��ӭʹ������ר�ң����������û���" );
        	
        }else{
        	speeker.speak("��ӭʹ������ר�ң��������ð��û���" );
        	
        }
        //5��ע�����̣�
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "��Ȩ���������", Toast.LENGTH_LONG).show();
					speeker.speak("��Ȩ���������");
					return;
				}
				getSock().RegStart(regCode, MainActivity.this);
				Log.d(TAG, "�¼�---->����");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
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
        //8.���ò���
        SetParams();
		
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speeker.speak("���������׷���");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speeker.speak("���ж����׷���");
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
            	//speeker.speak("�����Ӻ������");
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {
            	//speeker.speak("�����Ӻ������");
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
	
	//SeekBar�ӿڣ�
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, 
            boolean fromUser) {// ���϶���--��ֵ�ڸı� 
        // progressΪ��ǰ��ֵ�Ĵ�С 
    	tvSpeed.setText("�����������ٶ�:��ǰ�����ӳ٣�" + progress); 
    	
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
    	speeker.speak("��ǰ�����ӳ٣�" + seekBar.getProgress());
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
    //������Ϣע����Ϣ
    public Handler HandlerMain = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 0x12) {  
                Bundle bundle = msg.getData(); 
                String mRecvData=bundle.getString("msg");
                String sReg=mRecvData.substring(0,1);
                int testTime=Integer.parseInt(mRecvData.substring(2,5));
                if(sReg.equals("01")){//��Ȩ�ɹ�
                	getConfig().setREG(true);//1��д����Ȩ�ɹ���Ϣ��
                	getConfig().setTestTime(testTime);
                	tvRegState.setText("��Ȩ״̬������Ȩ");
                	tvRegWarm.setText("���������ۺ���ϵQQ��2481878920");
                	Toast.makeText(MainActivity.this, "��Ȩ�ɹ���", Toast.LENGTH_LONG).show();
                	speeker.speak("��Ȩ�ɹ������ѳ�Ϊ����ר�������û���" );
                	
                }else{
                	Toast.makeText(MainActivity.this, "��Ȩʧ�ܣ�", Toast.LENGTH_LONG).show();
                	speeker.speak("�ǳ���Ǹ!��Ȩʧ���ˣ�" );
                }
                Log.i(TAG, "handleMessage:" + mRecvData);
            }  
        }  
  
    }; 
    //ȡ�ò�����
    private boolean GetParams(){
    	boolean ret=false;
    	String sSay="";
    	//ȡ������������
    	//int iMoneyValue=0;
    	Config.sMoneyValue=etMoneyValue.getText().toString();
    	if(Config.sMoneyValue.equals("")){
    		sSay="�����÷�����";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	//iMoneyValue=Integer.parseInt(sMoneyValue);
    	//ȡ����������
    	//int iLuckyMoneyNum=0;
    	Config.sLuckyMoneyNum=etLuckyMoneyNum.getText().toString();
    	if(Config.sLuckyMoneyNum.equals("")){
    		sSay="�����÷���������";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	//iLuckyMoneyNum=Integer.parseInt(sLuckyMoneyNum);
    	//ȡ֧�����룺
    	//int iPWD=0;
    	Config.sPWD=etPWD.getText().toString();
    	if(Config.sPWD.equals("")){
    		sSay="������֧�����룡";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	
    	//ȡ����ϵ����� ��
    	Config.sLuckyMoneySay=etLuckyMoneySay.getText().toString();
    	if(Config.sLuckyMoneySay.equals("")){
    		sSay="�����ú���ϵ����֣�";
        	Toast.makeText(MainActivity.this, sSay, Toast.LENGTH_LONG).show();
        	speeker.speak(sSay );
        	return ret;
    	}
    	ret=true;
    	return ret;
    }
    //���ò�����
    private void SetParams(){
    	//�����ؼ���ʼ��
    	Config.bRobTail=getConfig().getRobTail();
    	if(Config.bRobTail)
    		rbRobTail.setChecked(true);
    	else
    		rbNoRobTail.setChecked(true);
    	 //������λ��
    	rgThunderPos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
                if(sChecked.equals("��")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_FEN);
                	speeker.speak("��ǰ���ã���Ϊ�ס�");
                	Toast.makeText(MainActivity.this, "��Ϊ��", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("��")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_JIAO);
                	speeker.speak("��ǰ���ã���Ϊ�ס�");
                	Toast.makeText(MainActivity.this, "��Ϊ��", Toast.LENGTH_LONG).show();
                }
                if(sChecked.equals("Ԫ")){
                	getConfig().setMoneyValuePos(Config.KEY_THUNDER_YUAN);
                	speeker.speak("��ǰ���ã�ԪΪ�ס�");
                	Toast.makeText(MainActivity.this, "ԪΪ��", Toast.LENGTH_LONG).show();
                }
                	
            }
         });
   	 //�󶨺��������λ��
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
                String sSay="��ǰ������Ϊ��"+sChecked+"���ס�";
                Toast.makeText(MainActivity.this,sSay, Toast.LENGTH_LONG).show();
                speeker.speak(sSay);
                
           }
        });
      	 //�󶨺��������λ��
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
                    //tv.setText("�����Ա��ǣ�" + rb.getText());
                   if(sChecked.equals("����βΪ��")){
                   	getConfig().setMoneySayPos(Config.KEY_THUNDER_TAIL);
                   	speeker.speak("��ǰ���ã�����������һ������Ϊ�ס�");
                   	Toast.makeText(MainActivity.this, "��ǰ���ã�����������һ������Ϊ�ס�", Toast.LENGTH_LONG).show();
                   }
                   if(sChecked.equals("������Ϊ��")){
                   	getConfig().setMoneySayPos(Config.KEY_THUNDER_HEAD);
                   	speeker.speak("��ǰ���ã�������е�һ������Ϊ�ס�");
                   	Toast.makeText(MainActivity.this, "��ǰ���ã�������е�һ������Ϊ�ס�", Toast.LENGTH_LONG).show();
                   }
               }
            });
     	 //�����Ƿ�ɨβ
      	rgRobTail.setOnCheckedChangeListener(new OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup arg0, int arg1) {
                   //��ȡ������ѡ�����ID
                  int radioButtonId = arg0.getCheckedRadioButtonId();
                  //����ID��ȡRadioButton��ʵ��
                   RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                   //�����ı����ݣ��Է���ѡ����
                   String sChecked=rb.getText().toString();

                  if(sChecked.equals("��β��")){
                  	getConfig().setRobTail(Config.KEY_ROB_TAIL);
                  	Config.bRobTail=true;
                  	speeker.speak("��ǰ���ã����׺���β����");
                  	Toast.makeText(MainActivity.this, "��ǰ���ã����׺���β����", Toast.LENGTH_LONG).show();
                  }
                  if(sChecked.equals("����β��")){
                  	getConfig().setRobTail(Config.KEY_NO_ROB_TAIL);
                  	Config.bRobTail=false;
                  	speeker.speak("��ǰ���ã����׺���β����");
                  	Toast.makeText(MainActivity.this, "��ǰ���ã����׺���β����", Toast.LENGTH_LONG).show();
                  }
              }
           });
      	
       	//���÷�����
       	etMoneyValue.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="��ǰ������"+s+"Ԫ";
       			speeker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
       	//���÷���������
       	etLuckyMoneyNum.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="��ǰ����������"+s+"��";
       			speeker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
       		}
       		
       	});
       	//���ú���
       	etLuckyMoneySay.addTextChangedListener(new TextWatcher(){
       		@Override
       		public void afterTextChanged(Editable s) {
       			String sShow="��ǰ����ϵ�����Ϊ��"+s+"��";
       			speeker.speak(sShow);
       			Toast.makeText(MainActivity.this, sShow, Toast.LENGTH_LONG).show();
       		}
       		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
       			
       		}
       		public void onTextChanged(CharSequence s, int start, int before,int count) {
       			
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
}
