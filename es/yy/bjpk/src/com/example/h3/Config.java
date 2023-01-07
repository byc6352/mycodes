/**
 * 
 */

package com.example.h3;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.bjpk.R;

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
	
	public static final String PREFERENCE_NAME = "byc_bjpk_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
    public static final String TAG2 = "byc002";//���Ա�ʶ��
	public static final boolean DEBUG =false;//���Ա�ʶ��

    //ע��ṹ��
    //00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //01��ע�᣻8760ʹ��ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //�������ô�����TestNum="0003";ʹ��3�Σ�
	public static final String IS_FIRST_RUN = "isFirstRun";//�Ƿ��һ������
	private static final boolean bFirstRun=true; 
	//����app��ʶ
	public static final String appID="av";//����app��ʶ������PK
    //������IP
	public static final String host = "119.23.68.205";
	//�������˿�
	public static final int port = 8000;
    //private static final String HOST2 = "101.200.200.78";
	//�Ƿ�ע��:
	public static final String REG = "reg";
	public static  boolean bReg = false;
		//ע���룺
	private static final String REG_CODE="Reg_Code";
	public static final  String RegCode="123456789012";
	//����ʱ��
	public static final String TEST_TIME = "TestTime";
    private static final int TestTime=0;//-- ����0��Сʱ��
    //���ô�����
    public static final String TEST_NUM = "TestNum";
    private static final int TestNum=0;//--����3�� 
    //��һ������ʱ�䣺
    public static final String FIRST_RUN_TIME = "first_run_time";
    //�����д�����
    public static final String RUN_NUM = "RunNum";
    //Ψһ��ʶ��
    public static final String PHONE_ID = "PhoneID";
	//�Զ�����Ϊ���ð����ʼʱ��
	public static final String START_TEST_TIME = "StartTestTime";
	//�Զ�����Ϊ���ð��ʱ������7�죩
    public static final int TestTimeInterval=7;//-- 
    //--------------------------------------------------------------------------------------
    //����������û���������
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";

	//��Ϸ���壺
    public static final String WINDOW_NZG_UI="";

    //�㲥��Ϣ����
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.bjpk.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.bjpk.ACCESSBILITY_CONNECT";
    

    //-------------------------------------------------------------------------------------------------------
    public static final String ACTION_CALC_DELAY = "com.byc.bjpk.Calc_DELAY ";//������ʱ��Ϣ
    public static final String ACTION_CALC_SHOW = "com.byc.bjpk.Calc_SHOW ";//������ʾ��Ϣ
    //--------------------------------------------------------------------------------------------------------
    private static final String HAO_PAI="Hao_Pai";//--���ƻ��� 
    public static final int HAO_PAI_10=10;//--���ƻ���10
    public static final int HAO_PAI_20=20;//--���ƻ���10
    public static final int HAO_PAI_30=30;//--���ƻ���10
    public static final int HAO_PAI_40=40;//--���ƻ���10
    public static final int HAO_PAI_50=50;//--���ƻ���10
    public static final int HAO_PAI_60=60;//--���ƻ���10
    public static final int HAO_PAI_70=70;//--���ƻ���10
    public static final int HAO_PAI_80=80;//--���ƻ���10
    public static final int HAO_PAI_90=90;//--���ƻ���10
    public static int iHaopai=HAO_PAI_50;//--���ƻ���10
    
    

  //��Ϸ������
    public static final String GAME_PACKAGE_BROWSER="com.android.browser";//-- �������Ϸ��
    public static final String GAME_PACKAGE_ZHUANGYUAN="com.ScholarSMP";//-- ״Ԫ��Ʊ����
	public static final String GAME_PACKAGE = "Game_Package";//�洢��Ϸ������
    //public static String GamePackage="com.android.browser,com.tencent.mm";//-- ��Ϸ����ȫ�ֱ�����
    public static String GamePackage="com.android.browser";//-- ��Ϸ����ȫ�ֱ�����
	//΢�ŵİ���
	//΢�ŵİ���
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
	public static final String SETTING_PACKAGENAME="com.android.settings";
	//΢�ŵİ���
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
    //��ַ��
	private static final String GAME_ADDR= "Game_Addr";//�洢��Ϸ��ַ��
    private static final String SEL_GAME_ADDR_INDEX="Sel_Game_Addr_Index";//--��ѡ�� ����Ϸ��ַ�洢
    public static int GameAddrIndex=0;//--��ѡ�� ����Ϸ��ַ�洢
    public static String GameAddr="";//--��ѡ�� ����Ϸ��ַ�洢
    //��Ϸ���ƣ�
    public static final String GAME_NAME= "Game_Name";//�洢��Ϸ����
    public static String GameName=null;//-- ��Ϸ��ȫ�ֱ�����
    private static final String SEL_GAME_INDEX="Sel_Game_Index";//--��ѡ�� ����Ϸ�洢
    //Ͷע��ʽ��
    public static final String GAME_BET_WAY= "Game_Bet_Way";//�洢��ϷͶע��ʽ��
    public static String GameBetWay=null;//-- ��ϷͶע��ʽȫ�ֱ�����
    private static final String SEL_GAME_BET_WAY_INDEX="Sel_Game_Index";//--��ѡ�� ����ϷͶע��ʽ�洢
    //������
    public static int iSelGameBetWay=0;//--ѡ�����ϷͶע��ʽ���;
    public static final String PLAYER_ID= "Player_ID";//�洢��Ϸ���ID��
    public static String PlayerID="";//-- ��Ϸ���ȫ�ֱ�����
    //ȫ�ֱ�����
    public static boolean bNn=true;//ţţ����P
    //public static boolean bAuto=true;//�Զ�������
    //�汾��
    //public static String version="";
    //��Ļ�ֱ��ʣ�
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
   

    //΢�Ű汾��
    public static int wv=1020; 
    //ftp
    //public static String ftpUserName="byc";
    //public static String ftpPwd="byc";
 
    //-----------------------����ģ��--------------------------------------------------
    private static final String SPEAKER="Speaker";//--���÷���ģʽ
    public static final String KEY_SPEAKER_NONE="9";//--��������female
    public static final String KEY_SPEAKER_FEMALE="0";//--Ů����
    public static final String KEY_SPEAKER_MALE="1";//--��ͨ������
    public static final String KEY_SPEAKER_SPECIAL_MALE="2";//--�ر������� special
    public static final String KEY_SPEAKER_EMOTION_MALE="3";//--���������emotion
    public static final String KEY_SPEAKER_CHILDREN="4";//--��ж�ͯ����children
    public static String speaker=KEY_SPEAKER_FEMALE;
    
    private static final String WHETHER_SPEAKING="Speak";//--�Ƿ�������ʾ��whether or not
    public static final boolean KEY_SPEAKING=true;//--����
    public static final boolean KEY_NOT_SPEAKING=false;//-������
    public static boolean bSpeaking=KEY_SPEAKING;//--Ĭ�Ϸ���
   
    
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 
	   
	        //��һ�������жϣ�����ǵ�һ�����У�д����Ȩģ���ʼ�����ݣ�
	        ////00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
	        if(isFirstRun()){
	    
	        	this.setREG(bReg);
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());
	        	this.setCurrentStartTestTime();//�����װʱ��д����Ϊ���ð�Ŀ�ʼʱ�䣻
	        	//ftp.getFtp().DownloadStart();//1.���ط�������Ϣ
	        }
	  
	        //3.ȡ������Ϣ��
	        Config.bSpeaking=this.getWhetherSpeaking();
	        Config.speaker=this.getSpeaker();
	    }
	   
	    public static synchronized Config getConfig(Context context) {
	        if(current == null) {
	            current = new Config(context.getApplicationContext());
	        }
	        return current;
	    }
	
	    //��һ�������жϣ�
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
	        return preferences.getBoolean(REG, bReg);
	    }
	    public void setREG(boolean reg) {
	        editor.putBoolean(REG, reg).apply();
	    }
	    /*
	     * ��ȡע����
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
	    //���������
	    //-------��ѡ����Ϸ��ַ���-----------------------------------------------------
	    public int getSelGameAddr() {
	        return preferences.getInt(SEL_GAME_ADDR_INDEX, 0);
	    }
	    public void setSelGameAddr(int idx) {
	        editor.putInt(SEL_GAME_ADDR_INDEX, idx).apply();
	    }
	    //-------��ѡ����Ϸ���-----------------------------------------------------
	    public int getSelGame() {
	        return preferences.getInt(SEL_GAME_INDEX, 0);
	    }
	    public void setSelGame(int idx) {
	        editor.putInt(SEL_GAME_INDEX, idx).apply();
	    }
	    //-------��ѡ����ע��ʽ���-----------------------------------------------------
	    public int getSelGameBetWay() {
	        return preferences.getInt(SEL_GAME_BET_WAY_INDEX, 0);
	    }
	    public void setSelGameBetWay(int idx) {
	        editor.putInt(SEL_GAME_BET_WAY_INDEX, idx).apply();
	    }
	    //-------��ѡ����Ϸ���ID-----------------------------------------------------
	    public String getPlayerID() {
	        return preferences.getString(PLAYER_ID, "");
	    }
	    public void setPlayerID(String PlayerID) {
	        editor.putString(PLAYER_ID, PlayerID).apply();
	    }
	    /*
	     * ��Ϸ���ƣ�
	     */
	    public String getGameName() {
	        return preferences.getString(GAME_NAME, "");
	    }
	    public void setGameName(String GameName) {
	        editor.putString(GAME_NAME, GameName).apply();
	    }	
	    /*
	     * ��Ϸ�����ƣ�
	     */
	    public String getGamePkg() {
	        return preferences.getString(GAME_PACKAGE, "");
	    }
	    public void setGamePkg(String GamePkg) {
	        editor.putString(GAME_PACKAGE, GamePkg).apply();
	    }

	    //-----------------------�ð���� ----------------------------------------
	    public int getHaopai() {
	        return preferences.getInt(HAO_PAI, HAO_PAI_10);
	    }
	    public void setHaopai(int iHaopai) {
	        editor.putInt(HAO_PAI, iHaopai).apply();
	    }

	  
	    /** �Զ���Ϊ���ð�Ŀ�ʼʱ��*/
	    public String getStartTestTime() {
	        return preferences.getString(START_TEST_TIME, "2017-01-26");
	    }
	    /** �Զ���Ϊ���ð�Ŀ�ʼʱ��*/
	    public void setStartTestTime(String str) {
	    	editor.putString(START_TEST_TIME, str).apply();
	    }
	    /** д�뵱ǰʱ��*/
	    public void setCurrentStartTestTime() {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
	    	String sDate =sdf.format(new Date());
	    	setStartTestTime(sDate);
	        //return preferences.getString(START_TEST_TIME, "2017-01-01");
	    }
	    /** ��ȡ�������ڵ��������*/
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
	    
	    /**���� ��Ա*/
	    public String getSpeaker() {
	        return preferences.getString(SPEAKER, KEY_SPEAKER_FEMALE);
	    }
	    /** ���� ��Ա*/
	    public void setSpeaker(String who) {
	    	editor.putString(SPEAKER, who).apply();
	    }
	    //-----------------------�Ƿ���---------------------------------------
	    public boolean getWhetherSpeaking() {
	        return preferences.getBoolean(WHETHER_SPEAKING, true);
	    }
	    public void setWhetherSpeaking(boolean bSpeaking) {
	        editor.putBoolean(WHETHER_SPEAKING, bSpeaking).apply();
	    }
	
}
