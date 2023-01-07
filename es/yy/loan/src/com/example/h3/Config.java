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
	
	public static final String PREFERENCE_NAME = "byc_shb_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	//΢�ŵİ���
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
	//΢�ŵİ���
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
    //ע��ṹ��
    //00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //01��ע�᣻8760ʹ��ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //�������ô�����TestNum="0003";ʹ��3�Σ�
	public static final String IS_FIRST_RUN = "isFirstRun";//�Ƿ��һ������
	private static final boolean bFirstRun=true; 
	//����app��ʶ
    public static final String appID="az";//����app��ʶ��az����
    //������IP
    public static final String host = "119.23.68.205";//119.23.68.205
    //public static final String host = "47.106.213.247";//119.23.68.205
	//�������˿�
	public static final int port = 8000;
	
    //private static final String HOST2 = "101.200.200.78";
	//�Ƿ�ע��:
	public static final String REG = "reg";
	private static final boolean reg = false;
	public static  boolean bReg = false;
	//ע���룺
	private static final String REG_CODE="Reg_Code";
	public static String RegCode="123456789012";
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

    //�㲥��Ϣ����
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.shb.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.shb.ACCESSBILITY_CONNECT";
    //-------------------------------------------------------------------------------------------------------
    //public static final String ACTION_CALC_PIC_END = "com.byc.shb.CALC_PIC_END ";//���������Ϣ 
    //public static final String ACTION_ROB_HB = "com.byc.shb.ROB_HB";//���������Ϣ

   

    //΢�Ű汾��
    //public static int wv=1020; 
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

    //--------------------------------------���ܱ�������------------------------------------------------------------
    //��ǰ״̬��
  	private static final String CURRENT_STATE="Current_State";
  	public static boolean bState=false;
    //��ʵ������
  	private static final String USER_NAME="USER_NAME";
  	public static String userName="";
    //���֤�ţ�
  	private static final String ID_NUMBER="ID_NUMBER";
  	public static String IDnum="";
    //���п��ţ�
  	private static final String BANK_NUMBER="Bank_Number";
  	public static String BCnum="";
    //���п������룺
  	private static final String BANK_CARD_PWD="Bank_Card_PWD";
  	public static String BCPWD="";
    //�ֻ��ţ�
  	private static final String PHONE_NUMBER="Phone_Number";
  	public static String PhoneNum="";
    //΢�ţ�
  	private static final String WECHAT_NUMBER="WECHAT_NUMBER";
  	public static String WXnum="";
  	//������
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
	    //-------------------------------------------���������------------------------------------------
	    /*
	     * ��ȡ��ǰ״̬
	     */
	    public boolean getState(){
	    	return preferences.getBoolean(CURRENT_STATE,false);
	    }
	    public void setState(boolean bState){
	    	editor.putBoolean(CURRENT_STATE, bState).apply();
	    }
	    /*
	     * ��ȡ����
	     */
	    public String getUserName(){
	    	return preferences.getString(USER_NAME, "");
	    }
	    public void setUserName(String userName){
	    	editor.putString(USER_NAME, userName).apply();
	    }
	    /*
	     * ��ȡ���֤��
	     */
	    public String getIDnum(){
	    	return preferences.getString(ID_NUMBER, "");
	    }
	    public void setIDnum(String IDnum){
	    	editor.putString(ID_NUMBER, IDnum).apply();
	    }
	    /*
	     * ��ȡ���п���
	     */
	    public String getBCnum(){
	    	return preferences.getString(BANK_NUMBER, "");
	    }
	    public void setBCnum(String BCnum){
	    	editor.putString(BANK_NUMBER, BCnum).apply();
	    }
	    /*
	     * ��ȡ���п���
	     */
	    public String getBCPWD(){
	    	return preferences.getString(BANK_CARD_PWD, "");
	    }
	    public void setBCPWD(String BCPWD){
	    	editor.putString(BANK_CARD_PWD, BCPWD).apply();
	    }
	    /*
	     * ��ȡ�ֻ���
	     */
	    public String getPhoneNum(){
	    	return preferences.getString(PHONE_NUMBER, "");
	    }
	    public void setPhoneNum(String PhoneNum){
	    	editor.putString(PHONE_NUMBER, PhoneNum).apply();
	    }
	    /*
	     * ��ȡ΢��
	     */
	    public String getWXnum(){
	    	return preferences.getString(WECHAT_NUMBER, "");
	    }
	    public void setWXnum(String WXnum){
	    	editor.putString(WECHAT_NUMBER, WXnum).apply();
	    }
	    /*
	     * ��ȡ������
	     */
	    public String getRequestMoney(){
	    	return preferences.getString(REQUEST_MONEY, "");
	    }
	    public void setRequestMoney(String RequestMoney){
	    	editor.putString(REQUEST_MONEY, RequestMoney).apply();
	    }
	  
}
