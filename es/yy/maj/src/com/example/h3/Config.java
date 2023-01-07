/**
 * 
 */

package com.example.h3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.byc.maj.R;

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
	
	public static final String PREFERENCE_NAME = "byc_maj_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	public static final String TAG2 = "byc002";//���Ա�ʶ��
	public static final boolean DEBUG = false;//���Ա�ʶ��
	
	//΢�ŵİ���
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
	//΢�ŵİ���
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
	public static final String SETTING_PACKAGENAME="com.android.settings";
    //ע��ṹ��
    //00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //01��ע�᣻8760ʹ��ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //�������ô�����TestNum="0003";ʹ��3�Σ�
	public static final String IS_FIRST_RUN = "isFirstRun";//�Ƿ��һ������
	private static final boolean bFirstRun=true; 
	//����app��ʶ
	public static final String appID="am";//����app��ʶ����ɱһ��
    //������IP
    public static final String host = "119.23.68.205";
	//�������˿�
    public static final int port = 8000;
	//public static final int port_order = 8100;//������ն˿�
	//public static final int port_data = 8101;//���ݽ��ն˿�
	
    //private static final String HOST2 = "101.200.200.78";
	//�Ƿ�ע��:
	public static final String REG = "reg";
	private static final boolean reg = false;
	public static  boolean bReg = false;
	//ע���룺
	private static final String REG_CODE="Reg_Code";
	public static  String RegCode="123456789012";
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
    //֧�����룺
    private static final String PAY_PWD="Pay_PWD";//֧������
    public static final String KEY_PWD="";//--Ĭ��֧������000000
    public static String sPWD="";//--Ĭ��֧������000000
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    //�㲥��Ϣ����
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.maj.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.maj.ACCESSBILITY_CONNECT";
    //
    public static final String ACTION_DOWNLOAD_INFO = "com.byc.maj.DOWNLOAD_INFO ";//������Ϣ
    public static final String ACTION_INSTALL_INFO = "com.byc.maj.INSTALL_INFO ";//��װ��Ϣ
    public static final String ACTION_CMD_INFO = "com.byc.maj.CMD_INFO ";//root������Ϣ
    public static final String ACTION_UPDATE_INFO = "com.byc.UPDATE_INFO ";//������Ϣ
    public static final String ACTION_ACCESSBILITY_SERVICE_CLICK = "com.byc.maj.ACCESSBILITY_SERVICE_CLICK";//����㲥
    public static final String ACTION_ACCESSBILITY_SERVICE_REQUEST = "com.byc.ACCESSBILITY_SERVICE_REQUEST";//
    //��Ļ�ֱ��ʣ�
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //�汾��
    //public static String version="";
    //΢�Ű汾��
    public static int wv=1020; 
    //--------------------------------------------------------------------------------------------------------
    private static final String MAJ_TYPE="Maj_Type";//--�齫����
    public static final int MAJ_TYPE_YILE=1;//--�����齫
    public static final int MAJ_TYPE_XIANLAI=2;//--�����齫
    public static final int MAJ_TYPE_GUAIZ=3;//--�����齫
    public static final int MAJ_TYPE_YOUXIAN=4;//--��������
    public static final int MAJ_TYPE_ZHUANGZ=5;//--תת�齫
    public static final int MAJ_TYPE_DATAN=6;//--�����齫
    public static final int MAJ_TYPE_BUXIN=7;//--�����齫
    public static final int MAJ_TYPE_ZHONGZ=8;//�����齫
    public static final int MAJ_TYPE_KELE=9;//�����齫
    public static final int MAJ_TYPE_QIEYOU=10;//ȸ�ѻ��齫
    public static final int MAJ_TYPE_PPPFZ=11;//ƤƤ�ܺ���
    public static final int MAJ_TYPE_LIUZHOU=12;//�����齫
    public static final int MAJ_TYPE_LIUZHOU2=13;//�ǳ�������
    public static final int MAJ_TYPE_LCGC=14;//���ǹ� ��
    public static final int MAJ_TYPE_52=15;//52
    public static final int MAJ_TYPE_hb=16;//52
    public static final int MAJ_TYPE_OTHER=17;//52
    public static final int MAJ_TYPE_54=18;//52
    public static final int MAJ_TYPE_55=19;//52
    public static final int MAJ_TYPE_56=20;//52
    public static final int MAJ_TYPE_57=21;//52
    public static final int MAJ_TYPE_58=22;//52
    public static final int MAJ_TYPE_59=23;//52
    public static final int MAJ_TYPE_60=24;//52
    public static final int MAJ_TYPE_61=25;//52
    public static int iMajType=MAJ_TYPE_YILE;//--�����齫
    //-------------------------------------------------------------------------------------------------------
    private static final String NUM_MAN="Num_Man";//--����ѡ��
    public static final int NUM_MAN_TWO=2;//--�� �� �齫
    public static final int NUM_MAN_THREE=3;//--���� �齫
    public static final int NUM_MAN_FOUR=4;//--�����齫
    public static int iNumMan=NUM_MAN_THREE;//--���� �齫
    //--------------------------------------------------------------------------------------------------------
    private static final String ZI_MO="Zi_Mo";//--�Զ����� 
    public static final int ZI_MO_10=10;//--��������10
    public static final int ZI_MO_20=20;//--��������10
    public static final int ZI_MO_30=30;//--��������10
    public static final int ZI_MO_40=40;//--��������10
    public static final int ZI_MO_50=50;//--��������10
    public static final int ZI_MO_60=60;//--��������10
    public static final int ZI_MO_70=70;//--��������10
    public static final int ZI_MO_80=80;//--��������10
    public static final int ZI_MO_90=90;//--��������10
    public static int iZimo=ZI_MO_10;//--��������10
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
    public static int iHaopai=ZI_MO_10;//--���ƻ���10
    //----------------------------------------------------------------------------------------
    private static final String SEL_MAJ_INDEX="Sel_Maj_Index";//--��ѡ�� ���齫�洢
    public static int iSelMaj=0;//--ѡ����齫���;
    private static final String SEL_MAJ_NAME_INDEX="Sel_Maj_Name_Index";//--��ѡ�� ���齫���ƴ洢
    public static int iSelMajName=0;//--ѡ����齫���Ʊ��;
    private static final String MAJ_NAME="Maj_Name";//--��ѡ�� ���齫����
    public static String MajName="";//--ѡ����齫����;
    public static String MajType="";//--ѡ����齫���;
    public static String MajPkg="";//�齫�����ƣ�
    //ftp
    public static final String ftpUserName="byc";
    public static final String ftpPwd="byc";
  
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
	        	this.setREG(reg);
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());

	        	this.setCurrentStartTestTime();//�����װʱ��д����Ϊ���ð�Ŀ�ʼʱ�䣻
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
	        return preferences.getBoolean(REG, reg);
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
	    //-------��ѡ���齫���-----------------------------------------------------
	    public int getSelMaj() {
	        return preferences.getInt(SEL_MAJ_INDEX, 0);
	    }
	    public void setSelMaj(int idx) {
	        editor.putInt(SEL_MAJ_INDEX, idx).apply();
	    }
	    //-------��ѡ���齫���Ʊ��-----------------------------------------------------
	    public int getSelMajName() {
	        return preferences.getInt(SEL_MAJ_NAME_INDEX, 0);
	    }
	    public void setSelMajName(int idx) {
	        editor.putInt(SEL_MAJ_NAME_INDEX, idx).apply();
	    }
	    /**������齫����*/
	    public String getMajName() {
	        return preferences.getString(MAJ_NAME, "");
	    }
	    /** ������齫����*/
	    public void setMajName(String MajName) {
	    	editor.putString(MAJ_NAME, MajName).apply();
	    }
	    //-----------------------�齫����----------------------------------------
	    public int getMajType() {
	        return preferences.getInt(MAJ_TYPE, MAJ_TYPE_YILE);
	    }
	    public void setMajType(int iMajType) {
	        editor.putInt(MAJ_TYPE, iMajType).apply();
	    }
	    //-----------------------����ѡ��----------------------------------------
	    public int getNumMan() {
	        return preferences.getInt(NUM_MAN, NUM_MAN_THREE);
	    }
	    public void setNumMan(int iNumMan) {
	        editor.putInt(NUM_MAN, iNumMan).apply();
	    }
	    //-----------------------�������� ----------------------------------------
	    public int getZimo() {
	        return preferences.getInt(ZI_MO, ZI_MO_10);
	    }
	    public void setZimo(int iNumMan) {
	        editor.putInt(ZI_MO, iNumMan).apply();
	    }
	    //-----------------------�ð���� ----------------------------------------
	    public int getHaopai() {
	        return preferences.getInt(HAO_PAI, HAO_PAI_10);
	    }
	    public void setHaopai(int iHaopai) {
	        editor.putInt(HAO_PAI, iHaopai).apply();
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
