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
    // 声明SeekBar 和 TextView对象 
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
    
    private RadioGroup rgSelMaj; //麻将选择：
    private RadioButton rbyile;//逸乐系列
    private RadioButton rbxianlai;//闲来麻将
    private RadioButton rbguizhou;//贵州麻将
    private RadioButton rbyouxian;//悠闲碰糊
    private RadioButton rbzhuangz;//转转麻将
    private RadioButton rbdatan;//大唐麻将
    private RadioButton rbbuxin;//阜新麻将
    private RadioButton rbzhongz;//中至麻将
    private RadioButton rbkele;//科乐麻将
    private RadioButton rbqieyou;//雀友会麻将
    private RadioButton rbliuzhou;//柳州麻将
    private RadioButton rbliuzhou2;//非常柳州麻将
    private RadioButton rblcgc;//龙城国粹
    private RadioButton rb52;//52
    private RadioButton rbpppfz;//皮皮四川麻将
    private RadioButton rbhb;//河北麻将
    private RadioButton rbOther;//52
    private RadioButton rb54;//52
    private RadioButton rb55;//52
    private RadioButton rb56;//52
    private RadioButton rb57;//52
    private RadioButton rb58;//52
    private RadioButton rb59;//52
    private RadioButton rb60;//52
    private RadioButton rb61;//52
    //private RadioButton rbxianlai;//闲来麻将
    //private RadioButton rbxianlai;//闲来麻将
    private RadioGroup rgNumMan; //人数选择
    private RadioButton rbNumManTwo;//四人麻将
    private RadioButton rbNumManThree;//三人麻将
    private RadioButton rbNumManFour;//四人麻将 
    
    private RadioGroup rgSelZimo; //自摸机率：
    private RadioButton rbZimo10;//机率10
    private RadioButton rbZimo20;//机率10
    private RadioButton rbZimo30;//机率10
    private RadioButton rbZimo40;//机率10
    private RadioButton rbZimo50;//机率10
    private RadioButton rbZimo60;//机率10
    private RadioButton rbZimo70;//机率10
    private RadioButton rbZimo80;//机率10
    //---------------------------------------------------
    private RadioGroup rgSelHaopai; //好牌机率：
    private RadioButton rbHaopai10;//机率10
    private RadioButton rbHaopai20;//机率10
    private RadioButton rbHaopai30;//机率10
    private RadioButton rbHaopai40;//机率10
    private RadioButton rbHaopai50;//机率10
    private RadioButton rbHaopai60;//机率10
    private RadioButton rbHaopai70;//机率10
    private RadioButton rbHaopai80;//机率10
    private RadioButton rbHaopai90;//机率10
    //发音模式：
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    private BackgroundMusic mBackgroundMusic;
    private FloatingWindow fw;//显示浮动窗口
    private boolean mSelMajOK=false;//确认麻将选择；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		TAG=Config.TAG;
		//1。语音
		Config.getConfig(getApplicationContext());//初 始化配置类；
		 speaker=SpeechUtil.getSpeechUtil(getApplicationContext()); 
		 AppInfo.getAppInfo(getApplicationContext());//初始化函数类；
		 fw=FloatingWindow.getFloatingWindow(getApplicationContext());//初始化浮动窗口类；
		 //3.控件初始化
		 SetParams();
        //2。获取分辨率
		getResolution2();
		String sResolution="本机分辨率：（"+Config.screenWidth+"*"+Config.screenHeight+"）";
		tvResolution.setText(sResolution);
		  //5.是否注册处理（显示版本信息(包括标题)）
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//开始服务器验证：
					Sock.getSock(MainActivity.this).VarifyStart();
		//5。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        registerReceiver(qhbConnectReceiver, filter);
       
        //6.播放背景音乐：
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.置为试用版；
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
            	speaker.speak("已连接麻将透视服务！");
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	speaker.speak("已中断麻将透视服务！");
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
				 Toast.makeText(MainActivity.this, "已授予悬浮窗权限！", Toast.LENGTH_LONG).show();
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
    //配置参数：
    private void SetParams(){
    	//参数控件初始化
    	//参数控件初始化etMajName
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
    	  //发音模式：
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound);
    	//-------------------------------读入参数-------------------------------------------
    	//麻将类型选择：

	    Config.iSelMaj=getConfig().getSelMaj();
	    spSelMaj.setSelection(Config.iSelMaj);
	    //麻将名称：
	    // 建立数据源
	    //String[] mItems = Funcs.getFuncs(MainActivity.this).GetAppNames();
	    List<String> mItems = AppInfo.getAppInfo(MainActivity.this).GetListAppNames();
	 // 建立Adapter并且绑定数据源
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
	    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice );//simple_spinner_dropdown_item
	    //绑定 Adapter到控件
	    spSelMajName.setAdapter(adapter);
	    Config.iSelMajName=getConfig().getSelMajName();
	    spSelMajName.setSelection(Config.iSelMajName);

	    //Config.MajName=getConfig().getMajName();//麻将名称;
	    //etMajName.setText(Config.MajName);
	    
    	Config.iMajType=getConfig().getMajType();
    	if(Config.iMajType==Config.MAJ_TYPE_YILE){
    		rbyile.setChecked(true);//逸乐麻将
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
    	//人数选择：
    	Config.iNumMan=getConfig().getNumMan();
    	if(Config.iNumMan==Config.NUM_MAN_THREE){
    		rbNumManThree.setChecked(true);//逸乐麻将
    	}else if(Config.iNumMan==Config.NUM_MAN_FOUR){
    		rbNumManFour.setChecked(true);
    	}else if(Config.iNumMan==Config.NUM_MAN_TWO){
    		rbNumManTwo.setChecked(true);
    	}
    	//自摸机率 ：
    	Config.iZimo=getConfig().getZimo();
    	if(Config.iZimo==Config.ZI_MO_10){
    		rbZimo10.setChecked(true);//自摸机率10
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
    	//好牌机率 ：
    	Config.iHaopai=getConfig().getHaopai();
    	if(Config.iHaopai==Config.HAO_PAI_10){
    		rbHaopai10.setChecked(true);//自摸机率10
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
    	//发音模式：
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//自动返回
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
    	//String html="●请先输入 验证授权码授权；\n";
    	//html+="●选择好你需要的功能；";
    	 //@SuppressWarnings("deprecation")
		//CharSequence charSequence=Html.fromHtml(html); 
    	 //tvSayContent.setText(charSequence);
    	
    	
	  
    	//-----------------------------绑定参数---------------------------------------------
      	 //麻将选择：
    	spSelMaj.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 // TODO Auto-generated method stub
                 /* 将所选mySpinner 的值带入myTextView 中*/
                 //myTextView.setText("您选择的是："+ adapter.getItem(arg2));
                 /* 将mySpinner 显示*/
                 //arg0.setVisibility(View.VISIBLE);id
            	// etMajName.setText(adapter.getItem(arg2));
            	 //Log.i(TAG, spSelMaj.getItemAtPosition(position).toString());
            	 //Toast.makeText(getApplicationContext(),"xxxx"+spSelMaj.getItemAtPosition(position),Toast.LENGTH_LONG).show();
            	 Config.MajType=spSelMaj.getItemAtPosition(position).toString();
            	 //etMajName.setText(Config.MajName);//设置麻将名称；
            	 getConfig().setSelMaj(position);//保存所选择麻将类别编号；
            	 //getConfig().setMajName(Config.MajType);//保存麻将类别；
            	 String say="当前选择："+Config.MajType+"类别。";
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
	        	getConfig().setSelMajName(Config.iSelMajName);//保存麻将；
	        	Config.MajName=spSelMajName.getItemAtPosition(pos).toString();
	        	getConfig().setMajName(Config.MajName);//保存麻将；
	        	if(pos==0)return;
	        	String say="当前选择："+Config.MajName+"!!!!!!!!警告：麻将名称选择正确才能透视成功！";
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
                     //获取变更后的选中项的ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //更新文本内容，以符合选中项
                     String sChecked=rb.getText().toString();
                     String say="";
                    if(sChecked.equals("逸乐系列")){
                    	getConfig().setMajType(Config.MAJ_TYPE_YILE);
                    	Config.iMajType=Config.MAJ_TYPE_YILE;
                    	say="当前选择：逸乐系列麻将。";
                    }
                    if(sChecked.equals("闲来麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_XIANLAI);
                    	Config.iMajType=Config.MAJ_TYPE_XIANLAI;
                    	say="当前选择：闲来麻将。";
                    }
                    if(sChecked.equals("贵州麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_GUAIZ);
                    	Config.iMajType=Config.MAJ_TYPE_GUAIZ;
                    	say="当前选择：贵州麻将。";
                    }
                    if(sChecked.equals("悠闲碰糊")){
                    	getConfig().setMajType(Config.MAJ_TYPE_YOUXIAN);
                    	Config.iMajType=Config.MAJ_TYPE_YOUXIAN;
                    	say="当前选择：悠闲碰糊。";
                    }
                    if(sChecked.equals("转转麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_ZHUANGZ);
                    	Config.iMajType=Config.MAJ_TYPE_ZHUANGZ;
                    	say="当前选择：转转麻将。";
                    }
                    if(sChecked.equals("大唐麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_DATAN);
                    	Config.iMajType=Config.MAJ_TYPE_DATAN;
                    	say="当前选择：大唐麻将。";
                    }
                    if(sChecked.equals("阜新麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_BUXIN);
                    	Config.iMajType=Config.MAJ_TYPE_BUXIN;
                    	say="当前选择：阜新麻将。";
                    }
                    if(sChecked.equals("中至系列")){
                    	getConfig().setMajType(Config.MAJ_TYPE_ZHONGZ);
                    	Config.iMajType=Config.MAJ_TYPE_ZHONGZ;
                    	say="当前选择：中至系列麻将。";
                    }
                    if(sChecked.equals("科乐系列")){
                    	getConfig().setMajType(Config.MAJ_TYPE_KELE);
                    	Config.iMajType=Config.MAJ_TYPE_KELE;
                    	say="当前选择：科乐系列麻将。";
                    }
                    if(sChecked.equals("雀友会系")){
                    	getConfig().setMajType(Config.MAJ_TYPE_QIEYOU);
                    	Config.iMajType=Config.MAJ_TYPE_QIEYOU;
                    	say="当前选择：雀友会系列麻将。";
                    }
                    if(sChecked.equals("皮皮四川麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_PPPFZ);
                    	Config.iMajType=Config.MAJ_TYPE_PPPFZ;
                    	say="当前选择：皮皮四川麻将。";
                    }
                    if(sChecked.equals("柳州麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_LIUZHOU);
                    	Config.iMajType=Config.MAJ_TYPE_LIUZHOU;
                    	say="当前选择：柳州麻将。";
                    }
                    if(sChecked.equals("非常柳州麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_LIUZHOU2);
                    	Config.iMajType=Config.MAJ_TYPE_LIUZHOU2;
                    	say="当前选择：非常柳州麻将。";
                    }
                    if(sChecked.equals("龙城国粹")){
                    	getConfig().setMajType(Config.MAJ_TYPE_LCGC);
                    	Config.iMajType=Config.MAJ_TYPE_LCGC;
                    	say="当前选择：龙城国粹麻将。";
                    }
                    if(sChecked.equals("52麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("河北麻将")){
                    	getConfig().setMajType(Config.MAJ_TYPE_hb);
                    	Config.iMajType=Config.MAJ_TYPE_hb;
                    	say="当前选择：河北麻将。";
                    }
                    if(sChecked.equals("其它")){
                    	getConfig().setMajType(Config.MAJ_TYPE_OTHER);
                    	Config.iMajType=Config.MAJ_TYPE_OTHER;
                    	say="当前选择：其它类型麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    if(sChecked.equals("52麻将1")){
                    	getConfig().setMajType(Config.MAJ_TYPE_52);
                    	Config.iMajType=Config.MAJ_TYPE_52;
                    	say="当前选择：52麻将。";
                    }
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
   	 //人数选择：
       	rgNumMan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //获取变更后的选中项的ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //更新文本内容，以符合选中项
                     String sChecked=rb.getText().toString();
                     String say="";
                     if(sChecked.equals("两人麻将")){
                    	 Config.iNumMan=Config.NUM_MAN_TWO;
                     	say="当前选择：两人麻将。";
                     }
                    if(sChecked.equals("三人麻将")){
                    	Config.iNumMan=Config.NUM_MAN_THREE;
                    	say="当前选择：三人麻将。";
                    }
                    if(sChecked.equals("四人麻将")){
                    	Config.iNumMan=Config.NUM_MAN_FOUR;
                    	say="当前选择：四人麻将。";
                    }
                    getConfig().setNumMan(Config.iNumMan);
                    //getConfig().setNumMan(Config.NUM_MAN_TWO);
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
      	 //自摸机率：
       	rgSelZimo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(RadioGroup arg0, int arg1) {
                     //获取变更后的选中项的ID
                    int radioButtonId = arg0.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                     RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                     //更新文本内容，以符合选中项
                     String sChecked=rb.getText().toString();
                     String say="";
                    if(sChecked.equals("自摸提高10%")){
                    	getConfig().setZimo(Config.ZI_MO_10);
                    	Config.iZimo=Config.ZI_MO_10;
                    	say="当前选择：自摸提高10%";
                    }
                    if(sChecked.equals("自摸提高20%")){
                    	getConfig().setZimo(Config.ZI_MO_20);
                    	Config.iZimo=Config.ZI_MO_20;
                    	say="当前选择：自摸提高20%";
                    }
                    if(sChecked.equals("自摸提高30%")){
                    	getConfig().setZimo(Config.ZI_MO_30);
                    	Config.iZimo=Config.ZI_MO_30;
                    	say="当前选择：自摸提高30%";
                    }
                    if(sChecked.equals("自摸提高40%")){
                    	getConfig().setZimo(Config.ZI_MO_40);
                    	Config.iZimo=Config.ZI_MO_40;
                    	say="当前选择：自摸提高40%";
                    }
                    if(sChecked.equals("自摸提高50%")){
                    	getConfig().setZimo(Config.ZI_MO_50);
                    	Config.iZimo=Config.ZI_MO_50;
                    	say="当前选择：自摸提高50%";
                    }
                    if(sChecked.equals("自摸提高60%")){
                    	getConfig().setZimo(Config.ZI_MO_60);
                    	Config.iZimo=Config.ZI_MO_60;
                    	say="当前选择：自摸提高60%";
                    }
                    if(sChecked.equals("自摸提高70%")){
                    	getConfig().setZimo(Config.ZI_MO_70);
                    	Config.iZimo=Config.ZI_MO_70;
                    	say="当前选择：自摸提高70%";
                    }
                    if(sChecked.equals("自摸提高80%")){
                    	getConfig().setZimo(Config.ZI_MO_80);
                    	Config.iZimo=Config.ZI_MO_80;
                    	say="当前选择：自摸提高80%";
                    }
                    if(sChecked.equals("自摸提高90%")){
                    	getConfig().setZimo(Config.ZI_MO_90);
                    	Config.iZimo=Config.ZI_MO_90;
                    	say="当前选择：自摸提高90%";
                    }
                
                    speaker.speak(say);
                	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
             });
     	 //好牌机率：
       	rgSelHaopai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    //获取变更后的选中项的ID
                   int radioButtonId = arg0.getCheckedRadioButtonId();
                   //根据ID获取RadioButton的实例
                    RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                    //更新文本内容，以符合选中项
                    String sChecked=rb.getText().toString();
                    String say="";
                   if(sChecked.equals("好牌提高10%")){
                   	getConfig().setHaopai(Config.HAO_PAI_10);
                   	Config.iHaopai=Config.HAO_PAI_10;
                   	say="当前选择：好牌机率提高10%";
                   }
                   if(sChecked.equals("好牌提高20%")){
                      	getConfig().setHaopai(Config.HAO_PAI_20);
                      	Config.iHaopai=Config.HAO_PAI_20;
                      	say="当前选择：好牌机率提高20%";
                    }
                   if(sChecked.equals("好牌提高30%")){
                     	getConfig().setHaopai(Config.HAO_PAI_30);
                     	Config.iHaopai=Config.HAO_PAI_30;
                     	say="当前选择：好牌机率提高30%";
                   }
                   if(sChecked.equals("好牌提高40%")){
                     	getConfig().setHaopai(Config.HAO_PAI_40);
                     	Config.iHaopai=Config.HAO_PAI_40;
                     	say="当前选择：好牌机率提高40%";
                   }
                   if(sChecked.equals("好牌提高50%")){
                     	getConfig().setHaopai(Config.HAO_PAI_50);
                     	Config.iHaopai=Config.HAO_PAI_50;
                     	say="当前选择：好牌机率提高50%";
                   }
                   if(sChecked.equals("好牌提高60%")){
                     	getConfig().setHaopai(Config.HAO_PAI_60);
                     	Config.iHaopai=Config.HAO_PAI_60;
                     	say="当前选择：好牌机率提高60%";
                   }
                   if(sChecked.equals("好牌提高70%")){
                     	getConfig().setHaopai(Config.HAO_PAI_70);
                     	Config.iHaopai=Config.HAO_PAI_70;
                     	say="当前选择：好牌机率提高70%";
                   }
                   if(sChecked.equals("好牌提高80%")){
                     	getConfig().setHaopai(Config.HAO_PAI_80);
                     	Config.iHaopai=Config.HAO_PAI_80;
                     	say="当前选择：好牌机率提高80%";
                   }
                   if(sChecked.equals("好牌提高90%")){
                     	getConfig().setHaopai(Config.HAO_PAI_90);
                     	Config.iHaopai=Config.HAO_PAI_90;
                     	say="当前选择：好牌机率提高90%";
                   }
                   speaker.speak(say);
               	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
                }
      	});
        //发音 模式
    	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                String sChecked=rb.getText().toString();
                String say="";
               if(sChecked.equals("关闭语音提示")){
            	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
               		say="当前设置：关闭语音提示。";
               }
               if(sChecked.equals("女声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
               		say="当前设置：女声提示。";
               }
               if(sChecked.equals("男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_MALE;
               		say="当前设置：男声提示。";
               }
               if(sChecked.equals("特别男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
               		say="当前设置：特别男声提示。";
               }
               if(sChecked.equals("情感男声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
               		say="当前设置：情感男声提示。";
               }
               if(sChecked.equals("情感儿童声")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
               		say="当前设置：情感儿童声提示。";
               }
        	   speaker.setSpeaking(Config.bSpeaking);
        	   speaker.setSpeaker(Config.speaker);
          		getConfig().setWhetherSpeaking(Config.bSpeaking);
          		getConfig().setSpeaker(Config.speaker);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
           }
        });
			
		//2。打开辅助服务按钮
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String say="";
				//1判断服务是否打开：
				mBackgroundMusic.stopBackgroundMusic();
				//是否选择麻将名称：
				if(spSelMajName.getSelectedItemPosition()==0){
					say="请选择麻将游戏！才能开始辅助透视功能！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				showSelMajNameDialog();
				
			}
		});//startBtn.setOnClickListener(
		//3。启动 游戏按钮
		btRunGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String say="";
				//1判断服务是否打开：
				mBackgroundMusic.stopBackgroundMusic();

				//是否选择麻将名称：
				if(spSelMajName.getSelectedItemPosition()==0){
					say="请选择麻将游戏！才能开始辅助透视功能！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					say="请先打开麻将透视服务！";
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
				//启动游戏并且打开悬浮窗口：
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
        //3。注册流程：
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "授权码输入错误！", Toast.LENGTH_LONG).show();
					speaker.speak("授权码输入错误！");
					return;
				}
				getSock().RegStart(regCode);
			}
		});//btReg.setOnClickListener(
    }
    /*
     * 麻将选择确认对话框：
     */
    private void showSelMajNameDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.ico);
        normalDialog.setTitle("麻将名称选择确认");
        String say="当前选择【"+Config.MajName+"】！请确认是否是所玩麻将游戏！警告：麻将选择错误将导致透视失败！";
        speaker.speak(say);
        normalDialog.setMessage(say);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	mSelMajOK=true;
            	String say;
				if(!QiangHongBaoService.isRunning()) {
					//打开系统设置中辅助功能
					Log.d(TAG, "事件---->打开系统设置中辅助功能");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="找到麻将专家，然后开启麻将透视服务。";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say="麻将透视服务已开启！";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}
            }
        });
        normalDialog.setNegativeButton("关闭", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            	mSelMajOK=false;
            }
        });
        // 显示
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
    /**显示版本信息*/
    public void showVerInfo(boolean bReg){
    	ConfigCt.bReg=bReg;
    	if(Ad2.currentQQ!=null)Ad2.currentQQ.getADinterval();
		if(Ad2.currentWX!=null)Ad2.currentWX.getADinterval();
        if(bReg){
        	Config.bReg=true;
        	getConfig().setREG(true);
        	tvRegState.setText("授权状态：已授权");
        	tvRegWarm.setText("正版升级技术售后联系"+ConfigCt.contact);
        	etRegCode.setVisibility(View.INVISIBLE);
        	tvPlease.setVisibility(View.INVISIBLE);
        	btReg.setVisibility(View.INVISIBLE);
        	speaker.speak("欢迎使用"+ConfigCt.AppName+"！您是正版用户！" );
        	
        }else{
        	Config.bReg=false;
        	getConfig().setREG(false);
        	tvRegState.setText("授权状态：未授权");
        	tvRegWarm.setText(ConfigCt.warning+"技术及授权联系"+ConfigCt.contact);
        	etRegCode.setVisibility(View.VISIBLE);
        	tvPlease.setVisibility(View.VISIBLE);
        	btReg.setVisibility(View.VISIBLE);
        	speaker.speak("欢迎使用"+ConfigCt.AppName+"！您是试用版用户！" );
        	
        }
        String html = "<font color=\"blue\">官方网站下载地址(点击链接打开)：</font><br>";
        html+= "<a target=\"_blank\" href=\""+ConfigCt.homepage+"\"><font color=\"#FF0000\"><big><b>"+ConfigCt.homepage+"</b></big></font></a>";
        //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
        tvHomePage.setTextColor(Color.BLUE);
        tvHomePage.setBackgroundColor(Color.WHITE);//
        //tvHomePage.setTextSize(20);
        tvHomePage.setText(Html.fromHtml(html));
        tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
        setMyTitle();
        updateMeWarning(ConfigCt.version,ConfigCt.new_version);//软件更新提醒
    }
    /**  软件更新提醒*/
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
        /* @setIcon 设置对话框图标 
         * @setTitle 设置对话框标题 
         * @setMessage 设置对话框消息提示 
         * setXXX方法返回Dialog对象，因此可以链式设置属性 
         */ 
        final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
        normalDialog.setIcon(R.drawable.ic_launcher); 
        normalDialog.setTitle(  "升级提醒"  );
        normalDialog.setMessage("有新版软件，是否现在升级？"); 
        normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override 
            public void onClick(DialogInterface dialog,int which){ 
                //...To-do
     		   Uri uri = Uri.parse(ConfigCt.download);    
     		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
     		   startActivity(it);  
            }
        }); 
        normalDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener(){ 
            @Override 
            public void onClick(DialogInterface dialog,   int   which){ 
            //...To-do 
            } 
        }); 
        // 显示 
        normalDialog.show(); 
        
    } 
    //设置软件标题：
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
      	  setTitle(getString(R.string.app_name) + ConfigCt.version+"（正式版）");
        }else{
      	  setTitle(getString(R.string.app_name) + ConfigCt.version+"（试用版）");
        }
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
   private   void   showCalcDialog(){ 
       /* @setIcon 设置对话框图标 
        * @setTitle 设置对话框标题 
        * @setMessage 设置对话框消息提示 
        * setXXX方法返回Dialog对象，因此可以链式设置属性 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "计算麻将数据提醒"  );
       normalDialog.setMessage(ConfigCt.AppName+"需要计算麻将数据，以使好牌率更加精准！此计算需要很长时间，请于晚上睡觉前运行计算任务！！运行计算任务时不要锁屏！手机处于充电状态！以免计算失败！"); 
       normalDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="请先找到"+ConfigCt.AppName+"，然后打开麻将透视服务，才能计算麻将数据！";
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
       normalDialog.setNegativeButton("关闭",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // 显示 
       normalDialog.show(); 
   } 
   private   void   showAddContactsDialog(){ 
       /* @setIcon 设置对话框图标 
        * @setTitle 设置对话框标题 
        * @setMessage 设置对话框消息提示 
        * setXXX方法返回Dialog对象，因此可以链式设置属性 
        */ 
	   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       //builder.setIcon(R.drawable.ic_launcher);
       builder.setIcon(ResourceUtil.getDimenId(getApplicationContext(), "ic_launcher"));
       builder.setTitle("请联系客服，激活软件");
       String say="请联系客服，激活软件!才能透视，换牌！";
   	   Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
	   speaker.speak(say);
       //builder.setMessage(say);
       final Contacts  cs=Contacts.getInstance(getApplicationContext(),ConfigCt.contact);
       String k1="客服1(QQ："+cs.QQ+")";
       String k2="客服2(微信："+cs.wx+")";
       final String[] css = {k1,k2};
       //    设置一个单项选择下拉框
       /**
        * 第一个参数指定我们要显示的一组下拉单选框的数据集合
        * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
        * 第三个参数给每一个单选项绑定一个监听器
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
               //Toast.makeText(MainActivity.this, "性别为：" + sex[which], Toast.LENGTH_SHORT).show();
           }
       });
       builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
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
       builder.setNegativeButton("试用", new DialogInterface.OnClickListener()
       {
           @Override
           public void onClick(DialogInterface dialog, int which)
           {
        		//启动游戏并且打开悬浮窗口：
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
       //此处写退向后台的处理
	   //moveTaskToBack(true); 
	   Log.i(Config.TAG, "maj onBackPressed: 调用");
	   finish();
   }
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
           //此处写退向后台的处理
    	   //moveTaskToBack(true);
    	   Log.i(Config.TAG, "maj KEYCODE_BACK: 调用");
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
       Log.i(Config.TAG, "maj onStop: 调用"); 
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
           /*一些处理，如弹出密码输入界面*/
       //}
   }
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices(this);
	    //SplashActivity.startAllServices();
		Log.i(Config.TAG, "maj onNewIntent: 调用");  
        //boolean bHide=this.getIntent().getBooleanExtra("hide", false);
        //hide(bHide);
	}
	
}

