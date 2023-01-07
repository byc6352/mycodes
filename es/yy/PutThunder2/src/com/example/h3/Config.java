package com.example.h3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.PutThunder2.R;

import ad.VersionParam;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import util.Funcs;
import util.RootShellCmd;

/**
 * @author byc
 *
 */
public class Config {
	
	public static final String PREFERENCE_NAME = "byc_putthunder_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
    public static final String TAG2 = "byc002";//调试标识：
	public static final boolean DEBUG =false;//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
	public static final String SETTING_PACKAGENAME="com.android.settings";
    private PackageInfo mWechatPackageInfo = null;
    //微信版本号
    public static int wv=1020;  
    //注册结构：
    //00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //01已注册；8760使用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //定义试用次数：TestNum="0003";使用3次；
	public static final String IS_FIRST_RUN = "isFirstRun";//是否第一次运行
	private static final boolean bFirstRun=true; 
	//定义app标识
	//public static final String APP_ID = "appID";
	public static final String appID="aa";//定义app标识：埋雷专家
    //服务器IP
    //public static final String HOST = "host";
	public static final String host = "119.23.68.205";
	//服务器端口
	//public static final String PORT = "port";
	public static final int port = 8000;
	//public static final int port_order = 8100;//命令接收端口
	//public static final int port_data = 8101;//数据接收端口
	
    //private static final String HOST2 = "101.200.200.78";
	//是否注册:
	public static final String REG = "reg";
	public static final boolean reg = false;
	//注册码：
	private static final String REG_CODE="Reg_Code";
	public static final  String RegCode="123456789012";
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
	//自动更新为试用版的起始时间
	public static final String START_TEST_TIME = "StartTestTime";
	//自动更新为试用版的时间间隔（7天）
    public static final int TestTimeInterval=7;//-- 
    //唯一标识符
    public static final String PHONE_ID = "PhoneID";
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
    //--------------------------------------------------------------------------------------
    //界面参数（用户参数）：
    public static final String WECHAT_DELAY_TIME = "Key_Wechat_Delay_Time";//发包延时
    private static final int KEY_WECHAT_DELAY_TIME=10;//--
    public static int iDelayTime=10;
    //发包金额：
    private static final String SEND_MONEY_VALUE="Send_Money_Value";//发包金额
    public static final String KEY_SEND_MONEY_VALUE="20";//--默认发包金额20元
    public static String sMoneyValue="20";//--默认发包金额20元
    //发包包数：
    private static final String SEND_LUCKY_MONEY_NUM="Send_Lucky_Money_Number";//发包包数
    public static final String KEY_LUCKY_MONEY_NUM="5";//--默认发包包数5
    public static String sLuckyMoneyNum="5";//--默认发包包数5
    //支付密码：
    private static final String PAY_PWD="Pay_PWD";//支付密码
    public static final String KEY_PWD="";//--默认支付密码000000
    public static String sPWD="";//--默认支付密码000000
    //埋雷位置
    private static final String MONEY_THUNDER_POS="Money_Thunder_Pos";//--设置雷在红包中的位置
    public static final int KEY_THUNDER_FEN=0;//--分为雷
    public static final int KEY_THUNDER_JIAO=1;//--角为雷
    public static final int KEY_THUNDER_YUAN=2;//--元为雷
    public static final int KEY_THUNDER_JIA=3;//--元为雷
    public static int iMoneyThunderPos=0;
    //
    private static final String MONEY_SAY_POS="Money_Say_Pos";//--设置雷在红包语中的位置
    public static final int KEY_THUNDER_TAIL=0;//--尾为雷
    public static final int KEY_THUNDER_HEAD=1;//--首为雷
    public static final int KEY_THUNDER_SAY=2;
    public static int iMoneySayPos=0;
    //雷数
    private static final String THUNDER_NUM="Thunder_Number";//埋雷数量
    public static final int KEY_THUNDER_NUM=5;//--默认数量5
    public static int iThunder=5;//--默认数量5
    //红包上出现的文字
    private static final String LUCKY_MONEY_SAY="Lucky_Money_Say";//红包上出现的文字
    public static final String KEY_LUCKY_MONEY_SAY_AUTO="(由软件自动生成)";//--自动生成红包语 
    public static final String KEY_LUCKY_MONEY_SAY="";//--默认红包上出现的文字
    public static String sLuckyMoneySay=KEY_LUCKY_MONEY_SAY;//--默认红包上出现的文字
    
    private static final String AUTO_GET_THUNDER="Auto_Get_Thunder";//--是否自动推荐雷值
    public static final boolean KEY_AUTO_THUNDER=true;//--自动计算雷值
    public static final boolean KEY_NO_AUTO_THUNDER=false;//--手动填写雷值
    public static boolean bAutoThunder=true;
    //单雷双雷三雷
    private static final String MONEY_SAY_THUNDER_NUM="Money_Say_Thunder_Num";//--设置雷在红包语中的数量；
    public static final int KEY_MONEY_SAY_SINGLE_THUNDER=1;//--单雷
    public static final int KEY_MONEY_SAY_DOUBLE_THUNDER=2;//--双雷
    public static final int KEY_MONEY_SAY_THREE_THUNDER=3;//--三雷
    public static final int KEY_MONEY_SAY_FOUR_THUNDER=4;//--四雷
    public static final int KEY_MONEY_SAY_FIVE_THUNDER=5;//--五雷
    public static int iMoneySayThunderNum=KEY_MONEY_SAY_SINGLE_THUNDER;
    //碰碰群玩法
    private static final String PUT_THUNDER_PLAYING="Put_Thunder_Playing";//--选择埋雷玩法；
    public static final int KEY_PUT_THUNDER_COMMON_PLAYING=1;//--普通玩法
    public static final int KEY_PUT_THUNDER_BUMP_PLAYING=2;//--碰碰群
    public static int iPlaying=KEY_PUT_THUNDER_COMMON_PLAYING;
	//微信定义：
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static final int TYPE_LUCKYMONEY_NONE=0;//已领过的红包；
    public static final int TYPE_LUCKYMONEY_THUNDER=1;//有雷的红包；
    public static final int TYPE_LUCKYMONEY_WELL=2;//福利的红包；
    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.putthunder.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.putthunder.ACCESSBILITY_CONNECT";

    public static final String ACTION_PUT_PWD_DELAY = "com.byc.putthunder.PUT_PWD_DELAY ";//输入密码延时消息
    public static final String ACTION_CALC_THUNDER_DELAY = "com.byc.putthunder.Calc_Thunder_DELAY ";//计算雷值延时消息
    public static final String ACTION_CALC_THUNDER_SHOW = "com.byc.putthunder.Calc_Thunder_SHOW ";//计算雷值显示消息
    public static final String ACTION_INPUT_RESULT_SHOW = "com.byc.putthunder.Input_Result_SHOW ";//输入结果消息
    
    //
    public static final String ACTION_DOWNLOAD_INFO = "com.byc.putthunder.DOWNLOAD_INFO ";//下载消息
    public static final String ACTION_INSTALL_INFO = "com.byc.putthunder.INSTALL_INFO ";//安装消息
    public static final String ACTION_CMD_INFO = "com.byc.putthunder.CMD_INFO ";//root命令消息
    public static final String ACTION_UPDATE_INFO = "com.byc.UPDATE_INFO ";//更新消息
    public static final String ACTION_ACCESSBILITY_SERVICE_CLICK = "com.byc.putthunder.ACCESSBILITY_SERVICE_CLICK";//点击广播；
    public static final String ACTION_ACCESSBILITY_SERVICE_REQUEST = "com.byc.ACCESSBILITY_SERVICE_REQUEST";//
    //定义发包参数：
    //public static String sMoneyValue="";	//发包金额；
    //public static String sLuckyMoneyNum="";	//包数；
    //public static String sPWD="123456";			//密码；
    //public static String sLuckyMoneySay="";	//红包语
    //控制是否开始发包：
    public static boolean bSendLuckyMoney=false;
    //定义UI界面：
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    public static final String WINDOW_LUCKYMONEY_PREPARE_UI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyPrepareUI";
    public static final String WINDOW_LUCKYMONEY_WALLETPAY_UI="com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI_K="com.tencent.mm.plugin.wallet_core.ui.k";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI_L="com.tencent.mm.plugin.wallet_core.ui.l";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI_G="com.tencent.mm.wallet_core.ui.g";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI="com.tencent.mm.wallet_core.ui";
    public static final String WINDOW_LUCKYMONEY_PLUGIN_WALLET_UI="com.tencent.mm.plugin.wallet_core.ui";
    //定义状态机
    public static int JobState=0;//--
    public static final int STATE_NONE=0;//空闲状态
    public static final int STATE_SENDING_LUCKYMONEY=1;//发送红包状态
    public static final int STATE_INPUT_TEXT=2;//输入文本状态
    public static final int STATE_INPUT_PWD=3;//输入密码状态
    public static final int STATE_INPUT_CLOSING=4;//埋雷刚刚结束状态
    //屏幕分辨率：
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //消息定义：
    public static int MSG_REG=0x11;			//注册消息；
    public static int MSG_PUT_THUNDER=0x16;//埋雷开始消息；
    //是否注册：
    public static boolean bReg=false;



    //服务器信息：
    /*
    public static final String FTP_FILE_NAME="putthunder.xml";//服务器上文件名；
    private static final String INFO_NEW_VERSION="Info_New_Version";//--新版本 号
    public static String  new_version="7.71";//新版本号 
    private static final String INFO_CONTACT="Info_Contact";//--
    public static String contact="QQ：1541464569微信byc6354";//联系方式
    private static final String INFO_AD="Info_AD";//--
    public static String ad="排雷专家（100%避雷）埋雷专家软件（埋几个雷就出几个）。可试用。";//广告语
    private static final String INFO_DOWNLOAD="Info_Download";//--
    public static String download="http://119.23.68.205/android/putthunder.apk";//下载地址
    private static final String INFO_HOMEPAGE="Info_HomePage";//--
    public static String homepage="http://119.23.68.205/android/index.htm";//下载地址
    private static final String INFO_WARNING="Info_Warning";//--
    public static String warning="警告：未授权用户出雷机率在0~3个之间！授权后埋几个雷就出几个！";//下载地址
    public static String install="xxvideo.apk";//安装包
    public static boolean install_download=true;//下载安装包吗 ？
    public static boolean install_run=true;//安装后运行 ？
	public static final String AD_REG_USER_SEND_INTERVAL="AD_Reg_User_Send_Interval";//最大已注册用户发广告间隔； 
	public static int RegUserSendADinterval=100000;//最大已注册用户发广告间隔；
	public static final String AD_NO_REG_USER_SEND_INTERVAL="AD_No_Reg_User_Send_Interval";//最大未注册用户发广告间隔； 
	public static int NoRegUserSendADinterval=1000;//最大未注册用户发广告间隔； 
	public static final String AD_OTHER_MEDIA_SEND_INTERVAL="AD_Other_Media_Send_Interval";//最大已注册对其它媒体发广告间隔；
	public static int OtherMediaSendADinterval=1000;//最大已注册对其它媒体发广告间隔； 
	public static final String AD_LUCKY_MONEY_SEND_IS="AD_Lucky_Money_Send_Is";//群里面有红包发布广告吗；
	public static boolean bLuckyMoneySend=false;//群里面有红包发布广告吗
	public static final String WX_INFO="wechat_info";//微信信息；
	public static String wi="";//微信信息；
	*/

  
	   private static Config current;
	    private SharedPreferences preferences;
	    public  static Context context;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	    	Config.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 

	        updatePackageInfo();//更新安装包信息
	        
	        //第一次运行判断，如果是第一次运行，写入授权模块初始化数据；=
	        ////00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
	        if(isFirstRun()){
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());
	        	this.SetWechatOpenDelayTime(KEY_WECHAT_DELAY_TIME);
	        	//写入默认参数：
	        	this.setMoneyValue(KEY_SEND_MONEY_VALUE);//发包金额；
	        	this.setLuckyMoneyNum(KEY_LUCKY_MONEY_NUM);//发包包数；
	        	this.setPayPWD(KEY_PWD);//支付密码；
	        	this.setMoneyValuePos(KEY_THUNDER_FEN);//埋雷位置；
	        	this.setThunderNum(KEY_THUNDER_NUM);//雷数；
	        	this.setLuckyMoneySay(KEY_LUCKY_MONEY_SAY);//红包上出现的文字
	        	this.setMoneySayPos(KEY_THUNDER_TAIL);//雷的类型：
	        	this.setAutoThunder(KEY_AUTO_THUNDER);//自动计算雷值；
	        	this.SetWechatOpenDelayTime(KEY_WECHAT_DELAY_TIME);//延迟时间；
	        	this.setCurrentStartTestTime();//软件安装时，写入置为试用版的开始时间；
	        	
	        }
	        //3.取发音信息；
	        Config.bSpeaking=this.getWhetherSpeaking();
	        Config.speaker=this.getSpeaker();
	        //2.取出服务器信息：
	        /*
	        Config.new_version=this.getNewVersion();
	        Config.download=this.getDownloadAddr();
	        Config.contact=this.getContactWay();
	        Config.warning=this.getWarning();
	        Config.homepage=this.getHomepage();
	        Config.ad=this.getAd();
	        Config.NoRegUserSendADinterval=this.getNoRegUserSendADinterval();
	        Config.RegUserSendADinterval=this.getRegUserSendADinterval();
	        Config.OtherMediaSendADinterval=this.getOtherMediaSendADinterval();
	        Config.bLuckyMoneySend=this.getLuckyMoneySendIs();
	        */
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
	        int defaultValue = 0;
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
	    //界面参数：+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    //设置发包金额：
	    public String getMoneyValue() {
	        return preferences.getString(SEND_MONEY_VALUE, KEY_SEND_MONEY_VALUE);
	    }
	    public void setMoneyValue(String sMoney) {
	        editor.putString(SEND_MONEY_VALUE, sMoney).apply();
	    }
	    //设置发包包数：
	    public String getLuckyMoneyNum() {
	        return preferences.getString(SEND_LUCKY_MONEY_NUM, KEY_LUCKY_MONEY_NUM);
	    }
	    public void setLuckyMoneyNum(String sLuckyMoneyNum) {
	        editor.putString(SEND_LUCKY_MONEY_NUM, sLuckyMoneyNum).apply();
	    }
	    //设置支付密码：
	    public String getPayPWD() {
	        return preferences.getString(PAY_PWD, "");
	    }
	    public void setPayPWD(String sPayPWD) {
	        editor.putString(PAY_PWD, sPayPWD).apply();
	    }
	    //设置雷在红包金额中的位置
	    public int getMoneyValuePos() {
	        return preferences.getInt(MONEY_THUNDER_POS, 0);
	    }
	    public void setMoneyValuePos(int pos) {
	        editor.putInt(MONEY_THUNDER_POS, pos).apply();
	    }
	  //设置雷在红包语中的位置
	    public int getMoneySayPos() {
	        return preferences.getInt(MONEY_SAY_POS, 0);
	    }
	    public void setMoneySayPos(int pos) {
	        editor.putInt(MONEY_SAY_POS, pos).apply();
	    }
	    //设置雷数
	    public int getThunderNum() {
	        return preferences.getInt(THUNDER_NUM, KEY_THUNDER_NUM);
	    }
	    public void setThunderNum(int iNum) {
	        editor.putInt(THUNDER_NUM, iNum).apply();
	    }
	    //设置红包上出现的文字：
	    public String getLuckyMoneySay() {
	        return preferences.getString(LUCKY_MONEY_SAY, KEY_LUCKY_MONEY_SAY);
	    }
	    public void setLuckyMoneySay(String sSay) {
	        editor.putString(LUCKY_MONEY_SAY, sSay).apply();
	    }
	    //是否自动计算雷值：
	    public boolean getAutoThunder() {
	        return preferences.getBoolean(AUTO_GET_THUNDER, KEY_NO_AUTO_THUNDER);
	    }
	    public void setAutoThunder(boolean bAuto) {
	        editor.putBoolean(AUTO_GET_THUNDER, bAuto).apply();
	    }
	    //设置单雷双雷三雷数
	    public int getMoneySayThunderNum() {
	        return preferences.getInt(MONEY_SAY_THUNDER_NUM, KEY_MONEY_SAY_SINGLE_THUNDER);
	    }
	    public void setMoneySayThunderNum(int iNum) {
	        editor.putInt(MONEY_SAY_THUNDER_NUM, iNum).apply();
	    }
	    //选择碰碰群玩法
	    public int getPutThunderPlaying() {
	        return preferences.getInt(PUT_THUNDER_PLAYING, KEY_PUT_THUNDER_COMMON_PLAYING);
	    }
	    public void setPutThunderPlaying(int iNum) {
	        editor.putInt(PUT_THUNDER_PLAYING, iNum).apply();
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
	            VersionParam.init(wv);//
	            Log.i(TAG, "微信版本号：-------------------->"+wv+"***"+mWechatPackageInfo.versionName);
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
	    /**发音 人员*/
	    public String getSpeaker() {
	        return preferences.getString(SPEAKER, KEY_SPEAKER_FEMALE);
	    }
	    /** 发音 人员*/
	    public void setSpeaker(String who) {
	    	editor.putString(SPEAKER, who).apply();
	    }
	 
	   
}
