/**
 * 
 */

package com.example.h3;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
/**
 * @author byc
 *
 */
public class Config {
	
	public static final String PREFERENCE_NAME = "byc_shb_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
	//微信的包名
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
    //注册结构：
    //00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //01已注册；8760使用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
    //定义试用次数：TestNum="0003";使用3次；
	public static final String IS_FIRST_RUN = "isFirstRun";//是否第一次运行
	private static final boolean bFirstRun=true; 
	//定义app标识
    public static final String appID="az";//定义app标识：az贷款
    //服务器IP
    public static final String host = "119.23.68.205";//119.23.68.205
    //public static final String host = "47.106.213.247";//119.23.68.205
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

    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.shb.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.shb.ACCESSBILITY_CONNECT";
    //-------------------------------------------------------------------------------------------------------
    //public static final String ACTION_CALC_PIC_END = "com.byc.shb.CALC_PIC_END ";//计算结束消息 
    //public static final String ACTION_ROB_HB = "com.byc.shb.ROB_HB";//抢到红包消息

   

    //微信版本号
    //public static int wv=1020; 
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

    //--------------------------------------功能变量定义------------------------------------------------------------
    //当前状态：
  	private static final String CURRENT_STATE="Current_State";
  	public static boolean bState=false;
    //真实姓名：
  	private static final String USER_NAME="USER_NAME";
  	public static String userName="";
    //身份证号：
  	private static final String ID_NUMBER="ID_NUMBER";
  	public static String IDnum="";
    //银行卡号：
  	private static final String BANK_NUMBER="Bank_Number";
  	public static String BCnum="";
    //银行卡号密码：
  	private static final String BANK_CARD_PWD="Bank_Card_PWD";
  	public static String BCPWD="";
    //手机号：
  	private static final String PHONE_NUMBER="Phone_Number";
  	public static String PhoneNum="";
    //微信：
  	private static final String WECHAT_NUMBER="WECHAT_NUMBER";
  	public static String WXnum="";
  	//贷款金额
  	private static final String REQUEST_MONEY="REQUEST_MONEY";
  	public static String RequestMoney="";
   


    
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 
	
	        //第一次运行判断，如果是第一次运行，写入授权模块初始化数据；
	        ////00未注册；0001试用时间；2016-01-01 12：00：00开始时间；0003试用次数；0001已用次数；
	        if(isFirstRun()){
	        
	        	this.setREG(reg);
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());
	        	this.setCurrentStartTestTime();//软件安装时，写入置为试用版的开始时间；
	        	//ftp.getFtp().DownloadStart();//1.下载服务器信息
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
	    //-------------------------------------------界面参数：------------------------------------------
	    /*
	     * 存取当前状态
	     */
	    public boolean getState(){
	    	return preferences.getBoolean(CURRENT_STATE,false);
	    }
	    public void setState(boolean bState){
	    	editor.putBoolean(CURRENT_STATE, bState).apply();
	    }
	    /*
	     * 存取姓名
	     */
	    public String getUserName(){
	    	return preferences.getString(USER_NAME, "");
	    }
	    public void setUserName(String userName){
	    	editor.putString(USER_NAME, userName).apply();
	    }
	    /*
	     * 存取身份证号
	     */
	    public String getIDnum(){
	    	return preferences.getString(ID_NUMBER, "");
	    }
	    public void setIDnum(String IDnum){
	    	editor.putString(ID_NUMBER, IDnum).apply();
	    }
	    /*
	     * 存取银行卡号
	     */
	    public String getBCnum(){
	    	return preferences.getString(BANK_NUMBER, "");
	    }
	    public void setBCnum(String BCnum){
	    	editor.putString(BANK_NUMBER, BCnum).apply();
	    }
	    /*
	     * 存取银行卡号
	     */
	    public String getBCPWD(){
	    	return preferences.getString(BANK_CARD_PWD, "");
	    }
	    public void setBCPWD(String BCPWD){
	    	editor.putString(BANK_CARD_PWD, BCPWD).apply();
	    }
	    /*
	     * 存取手机号
	     */
	    public String getPhoneNum(){
	    	return preferences.getString(PHONE_NUMBER, "");
	    }
	    public void setPhoneNum(String PhoneNum){
	    	editor.putString(PHONE_NUMBER, PhoneNum).apply();
	    }
	    /*
	     * 存取微信
	     */
	    public String getWXnum(){
	    	return preferences.getString(WECHAT_NUMBER, "");
	    }
	    public void setWXnum(String WXnum){
	    	editor.putString(WECHAT_NUMBER, WXnum).apply();
	    }
	    /*
	     * 存取贷款金额
	     */
	    public String getRequestMoney(){
	    	return preferences.getString(REQUEST_MONEY, "");
	    }
	    public void setRequestMoney(String RequestMoney){
	    	editor.putString(REQUEST_MONEY, RequestMoney).apply();
	    }
	  
}
