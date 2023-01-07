/**
 * 
 */

package com.example.h3;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.baidu.android.common.logging.Log;
import com.byc.nnqq.R;

import util.Funcs;
import util.RootShellCmd;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
/**
 * @author byc
 *
 */
public class Config {
	
	public static final String PREFERENCE_NAME = "byc_nnqq_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	public static final String TAG2 = "byc002";//调试标识：
	public static final boolean DEBUG = false;//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mobileqq";
    //注册结构：
    //00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //01已注册；8760使用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //定义试用次数：TestNum="0003";使用3次；
	public static final String IS_FIRST_RUN = "isFirstRun";//是否第一次运行
	private static final boolean bFirstRun=true; 
	//定义app标识

	public static final String appID="af";//定义app标识：ah排雷专家可试用
    //服务器IP

	public static final String host = "119.23.68.205";
	//服务器端口
	public static final int port = 8000;
	//public static final int port_order = 8100;//命令接收端口
	//public static final int port_data = 8101;//数据接收端口
	
    //private static final String HOST2 = "101.200.200.78";
	//是否注册:
	public static final String REG = "reg";
	private static final boolean reg = false;
	public static  boolean bReg = false;
	//注册码：
	private static final String REG_CODE="Reg_Code";
	public static String RegCode="123456789012";
	public static final String REG_CODE_VALUE="123456789012";
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
    public static final int TestTimeInterval=7;//-- 8.89
    //--------------------------------------------------------------------------------------
    //界面参数（用户参数）：
    public static final String KEY_WECHAT_DELAY_TIME = "KEY_WECHAT_DELAY_TIME";
    private static final int key_wechat_delay_time=0;//--
    public static int iDelayTime=0;//--秒 抢延时时间：默认0；
    
    private static final String MONEY_THUNDER_POS="Money_Thunder_Pos";//--设置雷在红包中的位置
    public static final int KEY_THUNDER_FEN=0;//--分为雷
    public static final int KEY_THUNDER_JIAO=1;//--角为雷
    public static final int KEY_THUNDER_YUAN=2;//--元为雷
    
    private static final String MONEY_SAY_POS="Money_Say_Pos";//--设置雷在红包语中的位置
    public static final int KEY_THUNDER_TAIL=0;//--尾为雷
    public static final int KEY_THUNDER_HEAD=1;//--首为雷
	//微信定义：
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="cooperation.qwallet.plugin.QWalletPluginProxyActivity";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="cooperation.qwallet.plugin.QWalletPluginProxyActivity";
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mobileqq.activity.SplashActivity";
    public static final int TYPE_LUCKYMONEY_NONE=0;//已领过的红包；
    public static final int TYPE_LUCKYMONEY_THUNDER=1;//有雷的红包；
    public static final int TYPE_LUCKYMONEY_WELL=2;//福利的红包；
    //定义UI界面：
    //public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    public static final String WINDOW_LUCKYMONEY_PREPARE_UI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyPrepareUI";
    public static final String WINDOW_LUCKYMONEY_WALLETPAY_UI="com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI_K="com.tencent.mm.plugin.wallet_core.ui.k";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI_L="com.tencent.mm.plugin.wallet_core.ui.l";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI_G="com.tencent.mm.wallet_core.ui.g";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI="com.tencent.mm.wallet_core.ui";
    public static final String WINDOW_LUCKYMONEY_PLUGIN_WALLET_UI="com.tencent.mm.plugin.wallet_core.ui";
    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.nnqq.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.nnqq.ACCESSBILITY_CONNECT";
    public static final String ACTION_CLICK_LUCKY_MONEY = "com.byc.nnqq.CLICK_LUCKY_MONEY";//抢包消息
    //
    public static final String ACTION_DOWNLOAD_INFO = "com.byc.nnqq.DOWNLOAD_INFO ";//下载消息
    public static final String ACTION_INSTALL_INFO = "com.byc.nnqq.INSTALL_INFO ";//安装消息
    public static final String ACTION_CMD_INFO = "com.byc.nnqq.CMD_INFO ";//root命令消息
    public static final String ACTION_UPDATE_INFO = "com.byc.UPDATE_INFO ";//更新消息
    public static final String ACTION_ACCESSBILITY_SERVICE_CLICK = "com.byc.nnqq.ACCESSBILITY_SERVICE_CLICK";//点击广播；
    //牛牛玩法定义：
    public static final int NN_SINGLE=1;
    public static final int NN_DOUBLE=2;
    public static final int NN_THREE=3;
    public static final int NN_HT=4;//首尾；
    public static final int NN_PG=5;//牌九；
    
    private static final String NN_WANG_FA="NN_WangFa";//--设置雷在红包中的位置
    //全局变量：
    public static boolean bNn=true;//牛牛开关
    public static boolean bAuto=true;//自动抢开关
    
    //屏幕分辨率：
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //定义状态机
    public static int JobState=0;//--
    public static final int STATE_NONE=0;//空闲状态
    public static final int STATE_CLICK_LUCKYMONEY=1;//拆红包状态
    //QQ版本号:6.7.0(3.16:3095)496;6.6.9(3.16:3060)482;6.6.8(1.16:3050)478;6.6.6：3025(1.9)468；6.6.5(1.7)
    public static int wv=786;
    //版本号
    //public static String version="";

    //ftp
    //public static String ftpUserName="byc";
    //public static String ftpPwd="byc";
   
    //-----------------------语音模块--------------------------------------------------
    private static final String SPEAKER="Speaker";//--设置发音模式
    public static final String KEY_SPEAKER_NONE="9";//--不发声；female
    public static final String KEY_SPEAKER_FEMALE="0";//--女声；
    public static final String KEY_SPEAKER_MALE="1";//--普通男声；
    public static final String KEY_SPEAKER_SPECIAL_MALE="2";//--特别男声； special
    public static final String KEY_SPEAKER_EMOTION_MALE="3";//--情感男声；emotion
    public static final String KEY_SPEAKER_CHILDREN="4";//--情感儿童声；children
    public static String speaker=KEY_SPEAKER_FEMALE;
    
    private static final String WHETHER_SPEAKING="Speak";//--是否语音提示；whether or not
    public static final boolean KEY_SPEAKING=true;//--发音
    public static final boolean KEY_NOT_SPEAKING=false;//-不发音
    public static boolean bSpeaking=KEY_SPEAKING;//--默认发音
    
 
    
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    private PackageInfo mWechatPackageInfo = null;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit();  
	        updatePackageInfo();
	      
	        //第一次运行判断，如果是第一次运行，写入授权模块初始化数据；
	        ////00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
	        if(isFirstRun()){
	        
	        	this.setREG(reg);
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());
	        	this.SetWechatOpenDelayTime(key_wechat_delay_time);
	        	this.setCurrentStartTestTime();//软件安装时，写入置为试用版的开始时间；
	        	
	        }
	     
	        //3.取发音信息；
	        Config.bSpeaking=this.getWhetherSpeaking();
	        Config.speaker=this.getSpeaker();
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
	    /** 更新微信包信息*/
	    private void updatePackageInfo() {
	        try {
	            mWechatPackageInfo =context.getPackageManager().getPackageInfo(WECHAT_PACKAGENAME, 0);
	            wv=mWechatPackageInfo.versionCode;
	            //VersionParam.init(wv);
	            Log.i(TAG, "QQversion:"+wv);
	        } catch (PackageManager.NameNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	    /** 微信打开红包后延时时间*/
	    public int getWechatOpenDelayTime() {
	        int defaultValue = 0;
	        int result = preferences.getInt(KEY_WECHAT_DELAY_TIME, defaultValue);
	        try {
	            return result;
	        } catch (Exception e) {}
	        return defaultValue;
	    }
	    //保存微信打开红包后延时时间
	    public int SetWechatOpenDelayTime(int DelayTime) {
	        
	        editor.putInt(KEY_WECHAT_DELAY_TIME, DelayTime); 
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
	    public int getMoneyValuePos() {
	        return preferences.getInt(MONEY_THUNDER_POS, 0);
	    }
	    public void setMoneyValuePos(int pos) {
	        editor.putInt(MONEY_THUNDER_POS, pos).apply();
	    }	
	    public int getMoneySayPos() {
	        return preferences.getInt(MONEY_SAY_POS, 0);
	    }
	    public void setMoneySayPos(int pos) {
	        editor.putInt(MONEY_SAY_POS, pos).apply();
	    }	
	    //NN 玩法参数：
	    public int getNnWangFa() {
	        return preferences.getInt(NN_WANG_FA, 1);
	    }
	    public void setNnWangFa(int iWangFa) {
	        editor.putInt(NN_WANG_FA, iWangFa).apply();
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
	 
	    /**发音 人员*/
	    public String getSpeaker() {
	        return preferences.getString(SPEAKER, KEY_SPEAKER_FEMALE);
	    }
	    /** 发音 人员*/
	    public void setSpeaker(String who) {
	    	editor.putString(SPEAKER, who).apply();
	    }
	    //-----------------------是否发音---------------------------------------
	    public boolean getWhetherSpeaking() {
	        return preferences.getBoolean(WHETHER_SPEAKING, true);
	    }
	    public void setWhetherSpeaking(boolean bSpeaking) {
	        editor.putBoolean(WHETHER_SPEAKING, bSpeaking).apply();
	    }

}
