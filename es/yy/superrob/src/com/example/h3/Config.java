/**
 * 
 */

package com.example.h3;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.superrob.R;

import ad.VersionParam;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import util.Funcs;
import util.RootShellCmd;
/**
 * @author byc
 *
 */
public class Config {
	
	public static final String PREFERENCE_NAME = "byc_surperrob_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	public static final String TAG2 = "byc002";//调试标识：
	public static final boolean DEBUG = false;//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    private PackageInfo mWechatPackageInfo = null;
    //注册结构：
    //00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //01已注册；8760使用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //定义试用次数：TestNum="0003";使用3次；
	public static final String IS_FIRST_RUN = "isFirstRun";//是否第一次运行
	private static final boolean bFirstRun=true; 
	//定义app标识
	public static final String appID="aj";//定义app标识：ah排雷专家可试用
    //服务器IP
	public static final String host = "119.23.68.205";
	//服务器端口
	public static final int port = 8000;
	
    //private static final String HOST2 = "101.200.200.78";
	//是否注册:
	public static final String REG = "reg";
	private static final boolean reg = false;
	public static  boolean bReg = false;
	//注册码：
	private static final String REG_CODE="Reg_Code";
	public static String RegCode="123456789012";
	//试用时间
	public static final String TEST_TIME = "TestTime";
    private static final int TestTime=0;//-- 试用0个小时；
    //试用次数：
    public static final String TEST_NUM = "TestNum";
    private static final int TestNum=0;//--试用3次 
    //第一次运行时间：
    public static final String FIRST_RUN_TIME = "first_run_time";
    //已运行次数：
    public static final String RUN_NUM = "RunNum";
    //唯一标识符
    public static final String PHONE_ID = "PhoneID";
	//自动更新为试用版的起始时间
	public static final String START_TEST_TIME = "StartTestTime";
	//自动更新为试用版的时间间隔（7天）
    public static final int TestTimeInterval=7;//-- 
    //--------------------------------------------------------------------------------------
    //界面参数（用户参数）：
    
    public static final String WECHAT_DELAY_TIME = "Wechat_Delay_Time";
    private static final int KEY_WECHAT_DELAY_TIME =10;//--
    public static int iDelayTime=10;
    
    //总开关
    public static boolean bSwitch=true;
    private static final String SWITCH_TOTAL="Switch_Total";//--总开关
    //过滤赔付包：
    public static boolean bNoPayFor=false;
    private static final String SWITCH_NO_PAY_FOR="Switch_No_Pay_For";//--过滤赔付包;
    //显示抢到的金额：
    public static boolean bShowMoney=false;
    private static final String SWITCH_SHOW_MONEY="Switch_Show_Money";//--显示抢到的金额;
    //显示抢到的金额：
    public static boolean bRobTail=false;
    private static final String SWITCH_ROB_TAIL="Switch_Rob_Tail";//--只 抢尾包 ;
    //防封号：
    public static boolean bGuardAccount=false;
    private static final String SWITCH_GUARD_ACCOUNT="Switch_Guard_Account";//--防封号 ;

	//微信定义：
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI_2="com.tencent.mm.plugin.luckymoney.ui.En_";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    public static final int TYPE_LUCKYMONEY_NONE=0;//已领过的红包；
    public static final int TYPE_LUCKYMONEY_THUNDER=1;//有雷的红包；
    public static final int TYPE_LUCKYMONEY_WELL=2;//福利的红包；
    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.superrob.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.superrob.ACCESSBILITY_CONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT = "com.byc.superrob.NOTIFY_LISTENER_DISCONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "com.byc.superrob.NOTIFY_LISTENER_CONNECT";

    public static final String ACTION_CLICK_LUCKY_MONEY = "com.byc.superrob.CLICK_LUCKY_MONEY";//抢包消息
    public static final String ACTION_DISPLAY_SUCCESS= "com.byc.superrob.ACTION_DISPLAY_SUCCESS";//避雷成功消息
    public static final String ACTION_DISPLAY_FAIL= "com.byc.superrob.ACTION_DISPLAY_FAIL";//避雷失败消息
    
    public static final String ACTION_DOWNLOAD_INFO = "com.byc.superrob.DOWNLOAD_INFO ";//下载消息
    public static final String ACTION_INSTALL_INFO = "com.byc.superrob.INSTALL_INFO ";//安装消息
    public static final String ACTION_CMD_INFO = "com.byc.superrob.CMD_INFO ";//root命令消息
    public static final String ACTION_UPDATE_INFO = "com.byc.UPDATE_INFO ";//更新消息
    public static final String ACTION_ACCESSBILITY_SERVICE_CLICK = "com.byc.superrob.ACCESSBILITY_SERVICE_CLICK";//点击广播；
    
    //屏幕分辨率：
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //定义状态机
    public static int JobState=0;//--
    public static final int STATE_NONE=0;//空闲状态
    public static final int STATE_CLICK_LUCKYMONEY=1;//拆红包状态
    //版本号
    //public static String version="";
    //雷的红包语：
    public static String sLuckyMoneySay="";

    //微信版本号
    public static int wv=1020;  
 
	
    //-----------------------语音模块--------------------------------------------------
    private static final String SPEAKER="Speaker";//--设置发音模式
    public static final String KEY_SPEAKER_NONE="9";//--不发声；female
    public static final String KEY_SPEAKER_FEMALE="0";//--女声；
    public static final String KEY_SPEAKER_MALE="1";//--普通男声；
    public static final String KEY_SPEAKER_SPECIAL_MALE="2";//--特别男声； special
    public static final String KEY_SPEAKER_EMOTION_MALE="3";//--情感男声；emotion
    public static final String KEY_SPEAKER_CHILDREN="4";//--情感儿童声；children
    public static String speaker=KEY_SPEAKER_FEMALE;
    
    private static final String WHETHER_SPEAKING="Speek";//--是否语音提示；whether or not
    public static final boolean KEY_SPEAKING=true;//--发音
    public static final boolean KEY_NOT_SPEAKING=false;//-不发音
    public static boolean bSpeaking=KEY_SPEAKING;//--默认发音

    
    
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 
	        updatePackageInfo();//更新安装包信息
	   
	        //第一次运行判断，如果是第一次运行，写入授权模块初始化数据；
	        ////00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
	        if(isFirstRun()){
	        	this.setREG(reg);
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());
	        	this.SetWechatOpenDelayTime(KEY_WECHAT_DELAY_TIME);
	        	this.setCurrentStartTestTime();//软件安装时，写入置为试用版的开始时间；
	        	
	        }
	        //+++++++++++++++++++启动时，+读入参数到内存+++++++++++++++
	        Config.bReg=getREG();//取注册信息；
	        //ftp.getFtp().DownloadStart();//1.下载服务器信息
	        //2.取出服务器信息：
	        //Config.new_version=this.getNewVersion();
	        //Config.download=this.getDownloadAddr();
	        //Config.contact=this.getContactWay();
	        //Config.warning=this.getWarning();
	        //Config.homepage=this.getHomepage();
	        //Config.ad=this.getAd();
	        //3.取发音信息；
	        Config.bSpeaking=this.getWhetherSpeaking();
	        Config.speaker=this.getSpeaker();
	    	//总开关：
	    	Config.bSwitch=getSwitchTotal();
	    	//是否过滤赔付包：
	    	Config.bNoPayFor=getSwitchNoPayFor();
	    	//是否显示金额：
	    	Config.bShowMoney=getSwitchShowMoney();
	    	//只抢尾包：
	    	Config.bRobTail=getRobTail();
	    	//延时秒数：
	    	Config.iDelayTime=getWechatOpenDelayTime();
	    }
	   
	    public static synchronized Config getConfig(Context context) {
	        if(current == null) {
	            current = new Config(context.getApplicationContext());
	        }
	        return current;
	    }
	    //第一次运行判断：
	    public boolean isFirstRun(){
	    	boolean ret=preferences.getBoolean(IS_FIRST_RUN, bFirstRun);
	    	if(ret){
	    		editor.putBoolean(IS_FIRST_RUN, false);
	    		editor.commit();
	    	}
	    	return ret;
	    }
	
	    /** 微信打开红包后延时时间*/
	    public int getWechatOpenDelayTime() {
	        int defaultValue = 10;
	        int result = preferences.getInt(WECHAT_DELAY_TIME, defaultValue);
	        try {
	            return result;
	        } catch (Exception e) {}
	        return defaultValue;
	    }
	    //保存微信打开红包后延时时间
	    public int SetWechatOpenDelayTime(int DelayTime) {
	        
	        editor.putInt(WECHAT_DELAY_TIME, DelayTime); 
	        editor.commit(); 

	        return DelayTime;
	    }
	  
	    /** REG*/
	    public boolean getREG() {
	        return preferences.getBoolean(REG, reg);
	    }
	    public void setREG(boolean reg) {
	        editor.putBoolean(REG, reg).apply();
	    }
	    /*
	     * 存取注册码
	     */
	    public String getRegCode(){
	    	return preferences.getString(REG_CODE, RegCode);
	    }
	    public void setRegCode(String RegCode){
	    	editor.putString(REG_CODE, RegCode).apply();
	    }
	    /** TEST_TIME*/
	    public int getTestTime() {
	        return preferences.getInt(TEST_TIME, TestTime);
	    }
	    public void setTestTime(int i) {
	        editor.putInt(TEST_TIME, i).apply();
	    }
	    /** TEST_NUM*/
	    public int getAppTestNum() {
	        return preferences.getInt(TEST_NUM, TestNum);
	    }
	    public void setTestNum(int i) {
	        editor.putInt(TEST_NUM, i).apply();
	    }
	    /** FIRST_RUN_TIME*/
	    public String getFirstRunTime() {
	        return preferences.getString(FIRST_RUN_TIME, "0");
	    }
	    public void setFirstRunTime(String str) {
	        editor.putString(FIRST_RUN_TIME, str).apply();
	    }
	    /** appID*/
	    public int getRunNum() {
	        return preferences.getInt(RUN_NUM, 0);
	    }
	    public void setRunNum(int i) {
	        editor.putInt(RUN_NUM, i).apply();
	    }
	    //
	    public String getPhoneIDFromHard(){
	    	TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
	    	String szImei = TelephonyMgr.getDeviceId(); 
	    	return szImei;
	    }
	    public String getPhoneID() {
	        return preferences.getString(PHONE_ID, "0");
	    }
	    public void setPhoneID(String str) {
	        editor.putString(PHONE_ID, str).apply();
	    }	   
	    //界面参数：
		  
	    public boolean getSwitchTotal() {
	        return preferences.getBoolean(SWITCH_TOTAL, true);
	    }
	    public void setSwitchTotal(boolean bSwitch) {
	        editor.putBoolean(SWITCH_TOTAL, bSwitch).apply();
	    }	
	    public boolean getSwitchNoPayFor() {
	        return preferences.getBoolean(SWITCH_NO_PAY_FOR, false);
	    }
	    public void setSwitchNoPayFor(boolean bSwitch) {
	        editor.putBoolean(SWITCH_NO_PAY_FOR, bSwitch).apply();
	    }	
	    public boolean getSwitchShowMoney() {
	        return preferences.getBoolean(SWITCH_SHOW_MONEY, false);
	    }
	    public void setSwitchShowMoney(boolean bSwitch) {
	        editor.putBoolean(SWITCH_SHOW_MONEY, bSwitch).apply();
	    }	
	    /*
	     * 只 抢尾包：
	     */
	    public boolean getRobTail() {
	        return preferences.getBoolean(SWITCH_ROB_TAIL, false);
	    }
	    public void setRobTail(boolean bSwitch) {
	        editor.putBoolean(SWITCH_ROB_TAIL, bSwitch).apply();
	    }	
	    /*
	     * 防 封号：
	     */
	    public boolean getGuardAccount() {
	        return preferences.getBoolean(SWITCH_GUARD_ACCOUNT, false);
	    }
	    public void setGuardAccount(boolean bSwitch) {
	        editor.putBoolean(SWITCH_GUARD_ACCOUNT, bSwitch).apply();
	    }	
	    
	    /** 获取微信的版本*/
	    public int getWechatVersion() {
	        if(mWechatPackageInfo == null) {
	            return 0;
	        }
	        return mWechatPackageInfo.versionCode;
	    }

	    /** 更新微信包信息*/
	    private void updatePackageInfo() {
	        try {
	            mWechatPackageInfo =context.getPackageManager().getPackageInfo(WECHAT_PACKAGENAME, 0);
	            wv=mWechatPackageInfo.versionCode;
	            VersionParam.init(wv);
	        } catch (PackageManager.NameNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	    /** 自动置为试用版的开始时间*/
	    public String getStartTestTime() {
	        return preferences.getString(START_TEST_TIME, "2017-01-26");
	    }
	    /** 自动置为试用版的开始时间*/
	    public void setStartTestTime(String str) {
	    	editor.putString(START_TEST_TIME, str).apply();
	    }
	    /** 写入当前时间*/
	    public void setCurrentStartTestTime() {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
	    	String sDate =sdf.format(new Date());
	    	setStartTestTime(sDate);
	        //return preferences.getString(START_TEST_TIME, "2017-01-01");
	    }
	    /** 获取两个日期的相隔天数*/
	    public int getDateInterval(String startDate,String endDate){
	    	int y1=Integer.parseInt(startDate.substring(0, 4));
	    	int y2=Integer.parseInt(endDate.substring(0, 4));
	    	int m1=Integer.parseInt(startDate.substring(5, 7));
	    	int m2=Integer.parseInt(endDate.substring(5, 7));
	    	int d1=Integer.parseInt(startDate.substring(8));
	    	int d2=Integer.parseInt(endDate.substring(8));
	    	int ret=(y2-y1)*365+(m2-m1)*30+(d2-d1);
	    	return ret;
	    }
	
	    //-----------------------是否发音---------------------------------------
	    public boolean getWhetherSpeaking() {
	        return preferences.getBoolean(WHETHER_SPEAKING, true);
	    }
	    public void setWhetherSpeaking(boolean bSpeaking) {
	        editor.putBoolean(WHETHER_SPEAKING, bSpeaking).apply();
	    }
	    public String getSpeaker() {
	        return preferences.getString(SPEAKER, KEY_SPEAKER_FEMALE);
	    }
	    public void setSpeaker(String who) {
	    	editor.putString(SPEAKER, who).apply();
	    }
	  
}
