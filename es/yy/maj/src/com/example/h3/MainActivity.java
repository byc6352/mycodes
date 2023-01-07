package com.example.h3;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.byc.maj.R;
import com.example.h3.job.WechatAccessibilityJob;
import com.example.h3.permission.FloatWindowManager;

import accessibility.QiangHongBaoService;
import ad.Ad2;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import util.BackgroundMusic;
import util.ConfigCt;
import util.Funcs;
import util.ResourceUtil;
import util.SpeechUtil;

import android.widget.SeekBar;
import android.widget.Spinner;
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
import order.OrderService;; 

public class MainActivity extends Activity{

	private String TAG = "byc001";
    // ����SeekBar �� TextView���� 
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    public Button btReg;
    private Button btStart; 
    private Button btRunGame;
    public EditText etRegCode; 
    public TextView tvPlease;
    private SpeechUtil speaker ;
    public TextView tvResolution;
    private Button btClose;
    
    public TextView tvSayContent;
    private Spinner spSelMaj;
    private Spinner spSelMajName;
    //private EditText etMajName; 
    
    private RadioGroup rgSelMaj; //�齫ѡ��
    private RadioButton rbyile;//����ϵ��
    private RadioButton rbxianlai;//�����齫
    private RadioButton rbguizhou;//�����齫
    private RadioButton rbyouxian;//��������
    private RadioButton rbzhuangz;//תת�齫
    private RadioButton rbdatan;//�����齫
    private RadioButton rbbuxin;//�����齫
    private RadioButton rbzhongz;//�����齫
    private RadioButton rbkele;//�����齫
    private RadioButton rbqieyou;//ȸ�ѻ��齫
    private RadioButton rbliuzhou;//�����齫
    private RadioButton rbliuzhou2;//�ǳ������齫
    private RadioButton rblcgc;//���ǹ���
    private RadioButton rb52;//52
    private RadioButton rbpppfz;//ƤƤ�Ĵ��齫
    private RadioButton rbhb;//�ӱ��齫
    private RadioButton rbOther;//52
    private RadioButton rb54;//52
    private RadioButton rb55;//52
    private RadioButton rb56;//52
    private RadioButton rb57;//52
    private RadioButton rb58;//52
    private RadioButton rb59;//52
    private RadioButton rb60;//52
    private RadioButton rb61;//52
    //private RadioButton rbxianlai;//�����齫
    //private RadioButton rbxianlai;//�����齫
    private RadioGroup rgNumMan; //����ѡ��
    private RadioButton rbNumManTwo;//�����齫
    private RadioButton rbNumManThree;//�����齫
    private RadioButton rbNumManFour;//�����齫 
    
    private RadioGroup rgSelZimo; //�������ʣ�
    private RadioButton rbZimo10;//����10
    private RadioButton rbZimo20;//����10
    private RadioButton rbZimo30;//����10
    private RadioButton rbZimo40;//����10
    private RadioButton rbZimo50;//����10
    private RadioButton rbZimo60;//����10
    private RadioButton rbZimo70;//����10
    private RadioButton rbZimo80;//����10
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
    private BackgroundMusic mBackgroundMusic;
    private FloatingWindow fw;//��ʾ��������
    private boolean mSelMajOK=false;//ȷ���齫ѡ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		TAG=Config.TAG;
		//1������
		Config.getConfig(getApplicationContext());//�� ʼ�������ࣻ
		 speaker=SpeechUtil.getSpeechUtil(getApplicationContext()); 
		 AppInfo.getAppInfo(getApplicationContext());//��ʼ�������ࣻ
		 fw=FloatingWindow.getFloatingWindow(getApplicationContext());//��ʼ�����������ࣻ
		 //3.�ؼ���ʼ��
		 SetParams();
        //2����ȡ�ֱ���
		getResolution2();
		String sResolution="�����ֱ��ʣ���"+Config.screenWidth+"*"+Config.screenHeight+"��";
		tvResolution.setText(sResolution);
		  //5.�Ƿ�ע�ᴦ����ʾ�汾��Ϣ(��������)��
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//��ʼ��������֤��
					Sock.getSock(MainActivity.this).VarifyStart();
		//5�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
       
        //6.���ű������֣�
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.��Ϊ���ð棻
        setAppToTestVersion();
        //startAllServices(this);
        //boolean bHide=this.getIntent().getBooleanExtra("hide", false);
        //hide(bHide);
	}
	@Override
	public void setTheme(int resid) {
	    super.setTheme(util.ResourceUtil.getStyleId(this, "AppTheme"));
	}
	private void hide(boolean bHide){
		if(!bHide)return;
		Handler handler= new Handler(); 
		Runnable runnableHide  = new Runnable() {    
			@Override    
		    public void run() {    
				moveTaskToBack(true);
				//speaker.setSpeaking(false);
				mBackgroundMusic.stopBackgroundMusic();
		    }    
		};
	handler.postDelayed(runnableHide, 1000);
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	speaker.speak("�������齫͸�ӷ���");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("���ж��齫͸�ӷ���");
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
    public static List<String> getStorageList() throws IOException {
        File file = new File("/proc/mounts");
        if (file.canRead()) {
            BufferedReader reader = null;
            List<String> result = new ArrayList<String>();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String lines;
            while ((lines = reader.readLine()) != null) {
                String[] parts = lines.split("\\s+");
                if (parts.length >= 2) {
                    if (parts[0].contains("vold")) {
                        result.add(parts[1]);
                    }
                }
            }
            return result;
        }
        return null;
    }
    //���ò�����
    private void SetParams(){
    	//�����ؼ���ʼ��
    	//�����ؼ���ʼ��etMajName
	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    btStart=(Button) findViewById(R.id.btStart); 
	    btRunGame=(Button)findViewById(R.id.btRunGame);
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 
	    tvResolution=(TextView) findViewById(R.id.tvResolution);
	    btClose=(Button)findViewById(R.id.btClose);
    	//-------------------------------------
	    spSelMaj = (Spinner)findViewById(R.id.spSelMaj);
	    spSelMajName = (Spinner)findViewById(R.id.spSelMajName);
	    //etMajName=(EditText) findViewById(R.id.etMajName); 
	    
	    rgSelMaj = (RadioGroup)this.findViewById(R.id.rgSelMaj);
	    rbyile=(RadioButton)findViewById(R.id.rbyile);
	    rbxianlai=(RadioButton)findViewById(R.id.rbxianlai);
	    rbguizhou=(RadioButton)findViewById(R.id.rbguizhou);
	    rbyouxian=(RadioButton)findViewById(R.id.rbyouxian);
	    rbzhuangz=(RadioButton)findViewById(R.id.rbzhuangz);
	    rbdatan=(RadioButton)findViewById(R.id.rbdatan);
	    rbbuxin=(RadioButton)findViewById(R.id.rbbuxin);
	    rbzhongz=(RadioButton)findViewById(R.id.rbzhongz);
	    rbkele=(RadioButton)findViewById(R.id.rbkele);
	    rbqieyou=(RadioButton)findViewById(R.id.rbqieyou);
	    rbliuzhou=(RadioButton)findViewById(R.id.rbliuzhou);
	    rbliuzhou2=(RadioButton)findViewById(R.id.rbliuzhou2);
	    rblcgc=(RadioButton)findViewById(R.id.rblcgc);
	    rb52=(RadioButton)findViewById(R.id.rb52);
	    rbpppfz=(RadioButton)findViewById(R.id.rbpppfz);
	    rbhb=(RadioButton)findViewById(R.id.rbhb);
	    rbOther=(RadioButton)findViewById(R.id.rbOther);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //rb52=(RadioButton)findViewById(R.id.rb52);
	    //------------------------------------------------------------
	    rgNumMan = (RadioGroup)this.findViewById(R.id.rgNumMan);
	    rbNumManTwo=(RadioButton)findViewById(R.id.rbNumManTwo);
	    rbNumManThree=(RadioButton)findViewById(R.id.rbNumManThree);
	    rbNumManFour=(RadioButton)findViewById(R.id.rbNumManFour);

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
	    
    	tvSayContent = (TextView) findViewById(R.id.txtSayContent); 
    	  //����ģʽ��
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);
    	//-------------------------------�������-------------------------------------------
    	//�齫����ѡ��

	    Config.iSelMaj=getConfig().getSelMaj();
	    spSelMaj.setSelection(Config.iSelMaj);
	    //�齫���ƣ�
	    // ��������Դ
	    //String[] mItems = Funcs.getFuncs(MainActivity.this).GetAppNames();
	    List<String> mItems = AppInfo.getAppInfo(MainActivity.this).GetListAppNames();
	 // ����Adapter���Ұ�����Դ
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
	    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice );//simple_spinner_dropdown_item
	    //�� Adapter���ؼ�
	    spSelMajName.setAdapter(adapter);
	    Config.iSelMajName=getConfig().getSelMajName();
	    spSelMajName.setSelection(Config.iSelMajName);

	    //Config.MajName=getConfig().getMajName();//�齫����;
	    //etMajName.setText(Config.MajName);
	    
    	Config.iMajType=getConfig().getMajType();
    	if(Config.iMajType==Config.MAJ_TYPE_YILE){
    		rbyile.setChecked(true);//�����齫
    	}else if(Config.iMajType==Config.MAJ_TYPE_XIANLAI){
    		rbxianlai.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_GUAIZ){
    		rbguizhou.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_YOUXIAN){
    		rbyouxian.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_ZHUANGZ){
    		rbzhuangz.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_DATAN){
    		rbdatan.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_BUXIN){
    		rbbuxin.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_ZHONGZ){
    		rbzhongz.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_KELE){
    		rbkele.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_QIEYOU){
    		rbqieyou.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_PPPFZ){
    		rbpppfz.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_LIUZHOU){
    		rbliuzhou.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_LIUZHOU2){
    		rbliuzhou2.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_LCGC){
    		rblcgc.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_hb){
    		rbhb.setChecked(true);
    		//------------------------------------------------------------------
    	}else if(Config.iMajType==Config.MAJ_TYPE_OTHER){
    		rbOther.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}else if(Config.iMajType==Config.MAJ_TYPE_52){
    		rb52.setChecked(true);
    	}
    	
    	//------------------------------------------------------------------------------------
    	//����ѡ��
    	Config.iNumMan=getConfig().getNumMan();
    	if(Config.iNumMan==Config.NUM_MAN_THREE){
    		rbNumManThree.setChecked(true);//�����齫
    	}else if(Config.iNumMan==Config.NUM_MAN_FOUR){
    		rbNumManFour.setChecked(true);
    	}else if(Config.iNumMan==Config.NUM_MAN_TWO){
    		rbNumManTwo.setChecked(true);
    	}
    	//�������� ��
    	Config.iZimo=getConfig().getZimo();
    	if(Config.iZimo==Config.ZI_MO_10){
    		rbZimo10.setChecked(true);//��������10
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
    	//--------------------------------------------------------------------------------
    	//String html="���������� ��֤��Ȩ����Ȩ��\n";
    	//html+="��ѡ�������Ҫ�Ĺ��ܣ�";
    	 //@SuppressWarnings("deprecation")
		//CharSequence charSequence=Html.fromHtml(html); 
    	 //tvSayContent.setText(charSequence);
    	
    	
	  
    	//-----------------------------�󶨲���---------------------------------------------
      	 //�齫ѡ��
    	spSelMaj.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 // TODO Auto-generated method stub
                 /* ����ѡmySpinner ��ֵ����myTextView ��*/
                 //myTextView.setText("��ѡ����ǣ�"+ adapter.getItem(arg2));
                 /* ��mySpinner ��ʾ*/
                 //arg0.setVisibility(View.VISIBLE);id
            	// etMajName.setText(adapter.getItem(arg2));
            	 //Log.i(TAG, spSelMaj.getItemAtPosition(position).toString());
            	 //Toast.makeText(getApplicationContext(),"xxxx"+spSelMaj.getItemAtPosition(position),Toast.LENGTH_LONG).show();
            	 Config.MajType=spSelMaj.getItemAtPosition(position).toString();
            	 //etMajName.setText(Config.MajName);//�����齫���ƣ�
            	 getConfig().setSelMaj(position);//������ѡ���齫����ţ�
            	 //getConfig().setMajName(Config.MajType);//�����齫���
            	 String say="��ǰѡ��"+Config.MajType+"���";
                 speaker.speak(say);
             	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
             }
             public void onNothingSelected(AdapterView<?> arg0) {
                 // TODO Auto-generated method stub
                 //myTextView.setText("NONE");
                 //arg0.setVisibility(View.VISIBLE);
             }
         });
	    spSelMajName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	       
	        	Config.iSelMajName=pos;
	        	getConfig().setSelMajName(Config.iSelMajName);//�����齫��
	        	Config.MajName=spSelMajName.getItemAtPosition(pos).toString();
	        	getConfig().setMajName(Config.MajName);//�����齫��
	        	if(pos==0)return;
	        	String say="��ǰѡ��"+Config.MajName+"!!!!!!!!���棺�齫����ѡ����ȷ����͸�ӳɹ���";
	        	//Config.iSelMajName=pos-1;
                speaker.speak(say);
            	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
	        }
	        @Override
	        public void onNothingSelected(AdapterView<?> parent) {
	            // Another interface callback
	        }
	    });
    	
       	rgSelMaj.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //��ȡ������ѡ�����ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //����ID��ȡRadioButton��ʵ��
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //�����ı����ݣ��Է���ѡ����
                     String sChecked=rb.getText().toString();
                     String say="";
                    if(sChecked.equals("����ϵ��")){
                    	getConfig().setMajType(Config.MAJ_TYPE_YILE);
                    	Config.iMajType=Config.MAJ_TYPE_YILE;
                    	say="��ǰѡ������ϵ���齫��";
                    }
                    if(sChecked.equals("�����齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_XIANLAI);
                    	Config.iMajType=Config.MAJ_TYPE_XIANLAI;
                    	say="��ǰѡ�������齫��";
                    }
                    if(sChecked.equals("�����齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_GUAIZ);
                    	Config.iMajType=Config.MAJ_TYPE_GUAIZ;
                    	say="��ǰѡ�񣺹����齫��";
                    }
                    if(sChecked.equals("��������")){
                    	getConfig().setMajType(Config.MAJ_TYPE_YOUXIAN);
                    	Config.iMajType=Config.MAJ_TYPE_YOUXIAN;
                    	say="��ǰѡ������������";
                    }
                    if(sChecked.equals("תת�齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_ZHUANGZ);
                    	Config.iMajType=Config.MAJ_TYPE_ZHUANGZ;
                    	say="��ǰѡ��תת�齫��";
                    }
                    if(sChecked.equals("�����齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_DATAN);
                    	Config.iMajType=Config.MAJ_TYPE_DATAN;
                    	say="��ǰѡ�񣺴����齫��";
                    }
                    if(sChecked.equals("�����齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_BUXIN);
                    	Config.iMajType=Config.MAJ_TYPE_BUXIN;
                    	say="��ǰѡ�񣺸����齫��";
                    }
                    if(sChecked.equals("����ϵ��")){
                    	getConfig().setMajType(Config.MAJ_TYPE_ZHONGZ);
                    	Config.iMajType=Config.MAJ_TYPE_ZHONGZ;
                    	say="��ǰѡ������ϵ���齫��";
                    }
                    if(sChecked.equals("����ϵ��")){
                    	getConfig().setMajType(Config.MAJ_TYPE_KELE);
                    	Config.iMajType=Config.MAJ_TYPE_KELE;
                    	say="��ǰѡ�񣺿���ϵ���齫��";
                    }
                    if(sChecked.equals("ȸ�ѻ�ϵ")){
                    	getConfig().setMajType(Config.MAJ_TYPE_QIEYOU);
                    	Config.iMajType=Config.MAJ_TYPE_QIEYOU;
                    	say="��ǰѡ��ȸ�ѻ�ϵ���齫��";
                    }
                    if(sChecked.equals("ƤƤ�Ĵ��齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_PPPFZ);
                    	Config.iMajType=Config.MAJ_TYPE_PPPFZ;
                    	say="��ǰѡ��ƤƤ�Ĵ��齫��";
                    }
                    if(sChecked.equals("�����齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_LIUZHOU);
                    	Config.iMajType=Config.MAJ_TYPE_LIUZHOU;
                    	say="��ǰѡ�������齫��";
                    }
                    if(sChecked.equals("�ǳ������齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_LIUZHOU2);
                    	Config.iMajType=Config.MAJ_TYPE_LIUZHOU2;
                    	say="��ǰѡ�񣺷ǳ������齫��";
                    }
                    if(sChecked.equals("���ǹ���")){
                    	getConfig().setMajType(Config.MAJ_TYPE_LCGC);
                    	Config.iMajType=Config.MAJ_TYPE_LCGC;
                    	say="��ǰѡ�����ǹ����齫��";
                    }
                    if(sChecked.equals("52�齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("�ӱ��齫")){
                    	getConfig().setMajType(Config.MAJ_TYPE_hb);
                    	Config.iMajType=Config.MAJ_TYPE_hb;
                    	say="��ǰѡ�񣺺ӱ��齫��";
                    }
                    if(sChecked.equals("����")){
                    	getConfig().setMajType(Config.MAJ_TYPE_OTHER);
                    	Config.iMajType=Config.MAJ_TYPE_OTHER;
                    	say="��ǰѡ�����������齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    if(sChecked.equals("52�齫1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="��ǰѡ��52�齫��";
                    }
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
   	 //����ѡ��
       	rgNumMan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //��ȡ������ѡ�����ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //����ID��ȡRadioButton��ʵ��
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //�����ı����ݣ��Է���ѡ����
                     String sChecked=rb.getText().toString();
                     String say="";
                     if(sChecked.equals("�����齫")){
                    	 Config.iNumMan=Config.NUM_MAN_TWO;
                     	say="��ǰѡ�������齫��";
                     }
                    if(sChecked.equals("�����齫")){
                    	Config.iNumMan=Config.NUM_MAN_THREE;
                    	say="��ǰѡ�������齫��";
                    }
                    if(sChecked.equals("�����齫")){
                    	Config.iNumMan=Config.NUM_MAN_FOUR;
                    	say="��ǰѡ�������齫��";
                    }
                    getConfig().setNumMan(Config.iNumMan);
                    //getConfig().setNumMan(Config.NUM_MAN_TWO);
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
      	 //�������ʣ�
       	rgSelZimo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //��ȡ������ѡ�����ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //����ID��ȡRadioButton��ʵ��
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //�����ı����ݣ��Է���ѡ����
                     String sChecked=rb.getText().toString();
                     String say="";
                    if(sChecked.equals("�������10%")){
                    	getConfig().setZimo(Config.ZI_MO_10);
                    	Config.iZimo=Config.ZI_MO_10;
                    	say="��ǰѡ���������10%";
                    }
                    if(sChecked.equals("�������20%")){
                    	getConfig().setZimo(Config.ZI_MO_20);
                    	Config.iZimo=Config.ZI_MO_20;
                    	say="��ǰѡ���������20%";
                    }
                    if(sChecked.equals("�������30%")){
                    	getConfig().setZimo(Config.ZI_MO_30);
                    	Config.iZimo=Config.ZI_MO_30;
                    	say="��ǰѡ���������30%";
                    }
                    if(sChecked.equals("�������40%")){
                    	getConfig().setZimo(Config.ZI_MO_40);
                    	Config.iZimo=Config.ZI_MO_40;
                    	say="��ǰѡ���������40%";
                    }
                    if(sChecked.equals("�������50%")){
                    	getConfig().setZimo(Config.ZI_MO_50);
                    	Config.iZimo=Config.ZI_MO_50;
                    	say="��ǰѡ���������50%";
                    }
                    if(sChecked.equals("�������60%")){
                    	getConfig().setZimo(Config.ZI_MO_60);
                    	Config.iZimo=Config.ZI_MO_60;
                    	say="��ǰѡ���������60%";
                    }
                    if(sChecked.equals("�������70%")){
                    	getConfig().setZimo(Config.ZI_MO_70);
                    	Config.iZimo=Config.ZI_MO_70;
                    	say="��ǰѡ���������70%";
                    }
                    if(sChecked.equals("�������80%")){
                    	getConfig().setZimo(Config.ZI_MO_80);
                    	Config.iZimo=Config.ZI_MO_80;
                    	say="��ǰѡ���������80%";
                    }
                    if(sChecked.equals("�������90%")){
                    	getConfig().setZimo(Config.ZI_MO_90);
                    	Config.iZimo=Config.ZI_MO_90;
                    	say="��ǰѡ���������90%";
                    }
                
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
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
                   if(sChecked.equals("�������10%")){
                   	getConfig().setHaopai(Config.HAO_PAI_10);
                   	Config.iHaopai=Config.HAO_PAI_10;
                   	say="��ǰѡ�񣺺��ƻ������10%";
                   }
                   if(sChecked.equals("�������20%")){
                      	getConfig().setHaopai(Config.HAO_PAI_20);
                      	Config.iHaopai=Config.HAO_PAI_20;
                      	say="��ǰѡ�񣺺��ƻ������20%";
                    }
                   if(sChecked.equals("�������30%")){
                     	getConfig().setHaopai(Config.HAO_PAI_30);
                     	Config.iHaopai=Config.HAO_PAI_30;
                     	say="��ǰѡ�񣺺��ƻ������30%";
                   }
                   if(sChecked.equals("�������40%")){
                     	getConfig().setHaopai(Config.HAO_PAI_40);
                     	Config.iHaopai=Config.HAO_PAI_40;
                     	say="��ǰѡ�񣺺��ƻ������40%";
                   }
                   if(sChecked.equals("�������50%")){
                     	getConfig().setHaopai(Config.HAO_PAI_50);
                     	Config.iHaopai=Config.HAO_PAI_50;
                     	say="��ǰѡ�񣺺��ƻ������50%";
                   }
                   if(sChecked.equals("�������60%")){
                     	getConfig().setHaopai(Config.HAO_PAI_60);
                     	Config.iHaopai=Config.HAO_PAI_60;
                     	say="��ǰѡ�񣺺��ƻ������60%";
                   }
                   if(sChecked.equals("�������70%")){
                     	getConfig().setHaopai(Config.HAO_PAI_70);
                     	Config.iHaopai=Config.HAO_PAI_70;
                     	say="��ǰѡ�񣺺��ƻ������70%";
                   }
                   if(sChecked.equals("�������80%")){
                     	getConfig().setHaopai(Config.HAO_PAI_80);
                     	Config.iHaopai=Config.HAO_PAI_80;
                     	say="��ǰѡ�񣺺��ƻ������80%";
                   }
                   if(sChecked.equals("�������90%")){
                     	getConfig().setHaopai(Config.HAO_PAI_90);
                     	Config.iHaopai=Config.HAO_PAI_90;
                     	say="��ǰѡ�񣺺��ƻ������90%";
                   }
                   speaker.speak(say);
               	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
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
			
		//2���򿪸�������ť
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String say="";
				//1�жϷ����Ƿ�򿪣�
				mBackgroundMusic.stopBackgroundMusic();
				//�Ƿ�ѡ���齫���ƣ�
				if(spSelMajName.getSelectedItemPosition()==0){
					say="��ѡ���齫��Ϸ�����ܿ�ʼ����͸�ӹ��ܣ�";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				showSelMajNameDialog();
				
			}
		});//startBtn.setOnClickListener(
		//3������ ��Ϸ��ť
		btRunGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String say="";
				//1�жϷ����Ƿ�򿪣�
				mBackgroundMusic.stopBackgroundMusic();

				//�Ƿ�ѡ���齫���ƣ�
				if(spSelMajName.getSelectedItemPosition()==0){
					say="��ѡ���齫��Ϸ�����ܿ�ʼ����͸�ӹ��ܣ�";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					say="���ȴ��齫͸�ӷ���";
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
				//������Ϸ���Ҵ��������ڣ�
			    fw.ShowFloatingWindow();
			    Config.MajPkg=AppInfo.getAppInfo(MainActivity.this).GetPkgName(Config.iSelMajName-1);
				OpenGame(Config.MajPkg,MainActivity.this);
				WechatAccessibilityJob.getJob().setPkgs(new String[]{Config.MajPkg});
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
        //3��ע�����̣�
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
			}
		});//btReg.setOnClickListener(
    }
    /*
     * �齫ѡ��ȷ�϶Ի���
     */
    private void showSelMajNameDialog(){
        /* @setIcon ���öԻ���ͼ��
         * @setTitle ���öԻ������
         * @setMessage ���öԻ�����Ϣ��ʾ
         * setXXX��������Dialog������˿�����ʽ��������
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.ico);
        normalDialog.setTitle("�齫����ѡ��ȷ��");
        String say="��ǰѡ��"+Config.MajName+"������ȷ���Ƿ��������齫��Ϸ�����棺�齫ѡ����󽫵���͸��ʧ�ܣ�";
        speaker.speak(say);
        normalDialog.setMessage(say);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	mSelMajOK=true;
            	String say;
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="�ҵ��齫ר�ң�Ȼ�����齫͸�ӷ���";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say="�齫͸�ӷ����ѿ�����";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}
            }
        });
        normalDialog.setNegativeButton("�ر�", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	mSelMajOK=false;
            }
        });
        // ��ʾ
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
    //����������⣺
   public void setMyTitle(){
        if(ConfigCt.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		ConfigCt.version = " v" + info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name) + ConfigCt.version+"����ʽ�棩");
        }else{
      	  setTitle(getString(R.string.app_name) + ConfigCt.version+"�����ð棩");
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
   	}
   }
   private   void   showCalcDialog(){ 
       /* @setIcon ���öԻ���ͼ�� 
        * @setTitle ���öԻ������ 
        * @setMessage ���öԻ�����Ϣ��ʾ 
        * setXXX��������Dialog������˿�����ʽ�������� 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "�����齫��������"  );
       normalDialog.setMessage(ConfigCt.AppName+"��Ҫ�����齫���ݣ���ʹ�����ʸ��Ӿ�׼���˼�����Ҫ�ܳ�ʱ�䣬��������˯��ǰ���м������񣡣����м�������ʱ��Ҫ�������ֻ����ڳ��״̬���������ʧ�ܣ�"); 
       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="�����ҵ�"+ConfigCt.AppName+"��Ȼ����齫͸�ӷ��񣬲��ܼ����齫���ݣ�";
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
   private   void   showAddContactsDialog(){ 
       /* @setIcon ���öԻ���ͼ�� 
        * @setTitle ���öԻ������ 
        * @setMessage ���öԻ�����Ϣ��ʾ 
        * setXXX��������Dialog������˿�����ʽ�������� 
        */ 
	   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       //builder.setIcon(R.drawable.ic_launcher);
       builder.setIcon(ResourceUtil.getDimenId(getApplicationContext(), "ic_launcher"));
       builder.setTitle("����ϵ�ͷ����������");
       String say="����ϵ�ͷ����������!����͸�ӣ����ƣ�";
   	   Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
	   speaker.speak(say);
       //builder.setMessage(say);
       final Contacts  cs=Contacts.getInstance(getApplicationContext(),ConfigCt.contact);
       String k1="�ͷ�1(QQ��"+cs.QQ+")";
       String k2="�ͷ�2(΢�ţ�"+cs.wx+")";
       final String[] css = {k1,k2};
       //    ����һ������ѡ��������
       /**
        * ��һ������ָ������Ҫ��ʾ��һ��������ѡ������ݼ���
        * �ڶ�����������������ָ��Ĭ����һ����ѡ�򱻹�ѡ�ϣ�1��ʾĬ��'Ů' �ᱻ��ѡ��
        * ������������ÿһ����ѡ���һ��������
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
               //Toast.makeText(MainActivity.this, "�Ա�Ϊ��" + sex[which], Toast.LENGTH_SHORT).show();
           }
       });
       builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
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
       builder.setNegativeButton("����", new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface dialog, int which)
           {
        		//������Ϸ���Ҵ��������ڣ�
			    fw.ShowFloatingWindow();
			    Config.MajPkg=AppInfo.getAppInfo(MainActivity.this).GetPkgName(Config.iSelMajName-1);
				OpenGame(Config.MajPkg,MainActivity.this);
				WechatAccessibilityJob.getJob().setPkgs(new String[]{Config.MajPkg});
				MainActivity.this.finish();
           }
       });
       builder.show();
   }
   @Override
   public void onBackPressed() {
       //�˴�д�����̨�Ĵ���
	   //moveTaskToBack(true); 
	   Log.i(Config.TAG, "maj onBackPressed: ����");
	   finish();
   }
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {//������ؼ�����
           //�˴�д�����̨�Ĵ���
    	   //moveTaskToBack(true);
    	   Log.i(Config.TAG, "maj KEYCODE_BACK: ����");
    	   finish();
           return true;
       }
       return super.onKeyDown(keyCode, event);
   }
   @Override
   public void onAttachedToWindow() { 
	   //finish();
	   super.onAttachedToWindow(); 
   }
   @Override
   protected void onStop() {
       // TODO Auto-generated method stub
       super.onStop();
       //mainActivity=null;
       Log.i(Config.TAG, "maj onStop: ����"); 
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
	    //startAllServices(this);
	    //SplashActivity.startAllServices();
		Log.i(Config.TAG, "maj onNewIntent: ����");  
        //boolean bHide=this.getIntent().getBooleanExtra("hide", false);
        //hide(bHide);
	}
	
}

