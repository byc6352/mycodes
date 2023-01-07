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
import android.view.Display;
import android.view.WindowManager;
/**
 * @author byc
 *
 */
public class Config {
	
	public static final String PREFERENCE_NAME = "byc_putthunderqq_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	public static final boolean DEBUG = true;//���Ա�ʶ��
	//΢�ŵİ���
	public static final String WECHAT_PACKAGENAME = "com.tencent.mobileqq";
    /** ������ʹ������ƥ�����С�汾�� */
    private static final int USE_ID_MIN_VERSION = 700;// 6.3.8 ��ӦcodeΪ680,6.3.9��ӦcodeΪ700
    private PackageInfo mWechatPackageInfo = null;
    //ע��ṹ��
    //00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //01��ע�᣻8760ʹ��ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //�������ô�����TestNum="0003";ʹ��3�Σ�
	public static final String IS_FIRST_RUN = "isFirstRun";//�Ƿ��һ������
	private static final boolean bFirstRun=true; 
	//����app��ʶ
	public static final String APP_ID = "appID";
    private static final String appID="ab";//����app��ʶ������ר��
    //������IP
    public static final String HOST = "host";
	private static final String host = "101.200.200.78";
	//�������˿�
	public static final String PORT = "port";
	private static final int port = 8188;
	
    //private static final String HOST2 = "101.200.200.78";
	//�Ƿ�ע��:
	public static final String REG = "reg";
	public static final boolean reg = false;
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
    //--------------------------------------------------------------------------------------
    //����������û���������
    public static final String WECHAT_DELAY_TIME = "Key_Wechat_Delay_Time";//������ʱ
    private static final int KEY_WECHAT_DELAY_TIME=10;//--
    public static int iDelayTime=10;
    //������
    private static final String SEND_MONEY_VALUE="Send_Money_Value";//�������
    public static final String KEY_SEND_MONEY_VALUE="20";//--Ĭ�Ϸ������20Ԫ
    public static String sMoneyValue="20";//--Ĭ�Ϸ������20Ԫ
    //����������
    private static final String SEND_LUCKY_MONEY_NUM="Send_Lucky_Money_Number";//��������
    public static final String KEY_LUCKY_MONEY_NUM="5";//--Ĭ�Ϸ�������5
    public static String sLuckyMoneyNum="5";//--Ĭ�Ϸ�������5
    //����������
    private static final String PAY_PWD="Pay_PWD";//֧������
    public static final String KEY_PWD="000000";//--Ĭ��֧������000000
    public static String sPWD="000000";//--Ĭ��֧������000000
    //����λ��
    private static final String MONEY_THUNDER_POS="Money_Thunder_Pos";//--�������ں���е�λ��
    public static final int KEY_THUNDER_FEN=0;//--��Ϊ��
    public static final int KEY_THUNDER_JIAO=1;//--��Ϊ��
    public static final int KEY_THUNDER_YUAN=2;//--ԪΪ��
    public static int iMoneyThunderPos=0;
    //
    private static final String MONEY_SAY_POS="Money_Say_Pos";//--�������ں�����е�λ��
    public static final int KEY_THUNDER_TAIL=0;//--βΪ��
    public static final int KEY_THUNDER_HEAD=1;//--��Ϊ��
    public static int iMoneySayPos=0;
    //����
    private static final String THUNDER_NUM="Thunder_Number";//��������
    public static final int KEY_THUNDER_NUM=5;//--Ĭ������5
    public static int iThunder=5;//--Ĭ������5
    //����ϳ��ֵ�����
    private static final String LUCKY_MONEY_SAY="Lucky_Money_Say";//����ϳ��ֵ�����
    public static final String KEY_LUCKY_MONEY_SAY="7";//--Ĭ�Ϻ���ϳ��ֵ�����
    public static String sLuckyMoneySay="20/7";//--Ĭ�Ϻ���ϳ��ֵ�����
    
    private static final String AUTO_GET_THUNDER="Auto_Get_Thunder";//--�Ƿ��Զ��Ƽ���ֵ
    public static final boolean KEY_AUTO_THUNDER=true;//--�Զ�������ֵ
    public static final boolean KEY_NO_AUTO_THUNDER=false;//--�ֶ���д��ֵ
    public static boolean bAutoThunder=true;
	//΢�Ŷ��壺
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static final int TYPE_LUCKYMONEY_NONE=0;//������ĺ����
    public static final int TYPE_LUCKYMONEY_THUNDER=1;//���׵ĺ����
    public static final int TYPE_LUCKYMONEY_WELL=2;//�����ĺ����
    //�㲥��Ϣ����
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.qianghongbao.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.qianghongbao.ACCESSBILITY_CONNECT";
    public static final String ACTION_THUNDER_RECEIVER = "com.byc.qianghongbao.THUNDER_RECEIVER";//������ֵ

    public static final String ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT = "com.byc.qianghongbao.NOTIFY_LISTENER_DISCONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "com.byc.qianghongbao.NOTIFY_LISTENER_CONNECT";
    public static final String ACTION_PUT_PWD_DELAY = "com.byc.qianghongbao.PUT_PWD_DELAY ";//����������ʱ��Ϣ
    public static final String ACTION_CALC_THUNDER_DELAY = "com.byc.qianghongbao.Calc_Thunder_DELAY ";//������ֵ��ʱ��Ϣ
    public static final String ACTION_CALC_THUNDER_SHOW = "com.byc.qianghongbao.Calc_Thunder_SHOW ";//������ֵ��ʾ��Ϣ

    //�����Ƿ�ʼ������
    public static boolean bSendLuckyMoney=false;
    //����UI���棺
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mobileqq.activity.SplashActivity";
    public static final String WINDOW_LUCKYMONEY_PREPARE_UI="com.tencent.mobileqq.activity.qwallet.SendHbActivity";
    public static final String WINDOW_LUCKYMONEY_WALLETPAY_UI="cooperation.qwallet.plugin.QWalletPluginProxyActivity";
    //����״̬��
    public static int JobState=0;//--
    public static final int STATE_NONE=0;//����״̬
    public static final int STATE_SENDING_LUCKYMONEY=1;//���ͺ��״̬
    public static final int STATE_INPUT_TEXT=2;//�����ı�״̬
    public static final int STATE_INPUT_PWD=3;//��������״̬
    public static final int STATE_INPUT_CLOSING=4;//���׸ոս���״̬
    //��Ļ�ֱ��ʣ�
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //��Ϣ���壺
    public static int MSG_REG=0x11;			//ע����Ϣ��
    public static int MSG_PUT_THUNDER=0x16;//���׿�ʼ��Ϣ��
    //public static int ACTION_
    //�Ƿ�ע�᣺
    public static boolean bReg=false;
    //�汾�ţ�
    public static String version="";
	//�Զ�����Ϊ���ð����ʼʱ��
	public static final String START_TEST_TIME = "StartTestTime";
	//�Զ�����Ϊ���ð��ʱ������7�죩
    public static final int TestTimeInterval=7;//-- 
    
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 
	        updatePackageInfo();//���°�װ����Ϣ
	        
	        //��һ�������жϣ�����ǵ�һ�����У�д����Ȩģ���ʼ�����ݣ�=
	        ////00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
	        if(isFirstRun()){
	        	this.setAppID(appID);//1��ʼ��appID
	        	this.setHOST(host);
	        	this.setPORT(port);
	        	this.setREG(reg);
	        	this.setTestTime(TestTime);
	        	//this.setFirstRunTime(str);
	        	this.setTestNum(TestNum);
	        	this.setRunNum(0);
	        	this.setPhoneID(getPhoneIDFromHard());
	        	this.SetWechatOpenDelayTime(KEY_WECHAT_DELAY_TIME);
	        	//д��Ĭ�ϲ�����
	        	this.setMoneyValue(KEY_SEND_MONEY_VALUE);//������
	        	this.setLuckyMoneyNum(KEY_LUCKY_MONEY_NUM);//����������
	        	this.setPayPWD(KEY_PWD);//֧�����룻
	        	this.setMoneyValuePos(KEY_THUNDER_FEN);//����λ�ã�
	        	this.setThunderNum(KEY_THUNDER_NUM);//������
	        	this.setLuckyMoneySay(KEY_LUCKY_MONEY_SAY);//����ϳ��ֵ�����
	        	this.setMoneySayPos(KEY_THUNDER_TAIL);//�׵����ͣ�
	        	this.setAutoThunder(KEY_AUTO_THUNDER);//�Զ�������ֵ��
	        	this.SetWechatOpenDelayTime(KEY_WECHAT_DELAY_TIME);//�ӳ�ʱ�䣻
	        	this.setCurrentStartTestTime();//������װʱ��д����Ϊ���ð�Ŀ�ʼʱ�䣻
	        }
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
	    /** ΢�Ŵ򿪺������ʱʱ��*/
	    public int getWechatOpenDelayTime() {
	        int defaultValue = 0;
	        int result = preferences.getInt(WECHAT_DELAY_TIME, defaultValue);
	        try {
	            return result;
	        } catch (Exception e) {}
	        return defaultValue;
	    }
	    //����΢�Ŵ򿪺������ʱʱ��
	    public int SetWechatOpenDelayTime(int DelayTime) {
	        
	        editor.putInt(WECHAT_DELAY_TIME, DelayTime); 
	        editor.commit(); 

	        return DelayTime;
	    }
	    //appID
	    /** appID*/
	    public String getAppID() {
	        return preferences.getString(APP_ID, appID);
	    }
	    public void setAppID(String str) {
	        editor.putString(APP_ID, str).apply();
	    }
	    /** HOST*/
	    public String getHOST() {
	        return preferences.getString(HOST, host);
	    }
	    public void setHOST(String str) {
	    	editor.putString(HOST, str).apply();
	    }
	    /** PORT*/
	    public int getPORT() {
	        return preferences.getInt(PORT, port);
	    }
	    public void setPORT(int port) {
	        editor.putInt(PORT, port).apply();
	    }
	    /** REG*/
	    public boolean getREG() {
	        return preferences.getBoolean(REG, reg);
	    }
	    public void setREG(boolean reg) {
	        editor.putBoolean(REG, reg).apply();
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
	    //���������+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    //���÷�����
	    public String getMoneyValue() {
	        return preferences.getString(SEND_MONEY_VALUE, KEY_SEND_MONEY_VALUE);
	    }
	    public void setMoneyValue(String sMoney) {
	        editor.putString(SEND_MONEY_VALUE, sMoney).apply();
	    }
	    //���÷���������
	    public String getLuckyMoneyNum() {
	        return preferences.getString(SEND_LUCKY_MONEY_NUM, KEY_LUCKY_MONEY_NUM);
	    }
	    public void setLuckyMoneyNum(String sLuckyMoneyNum) {
	        editor.putString(SEND_LUCKY_MONEY_NUM, sLuckyMoneyNum).apply();
	    }
	    //����֧�����룺
	    public String getPayPWD() {
	        return preferences.getString(PAY_PWD, KEY_PWD);
	    }
	    public void setPayPWD(String sPayPWD) {
	        editor.putString(PAY_PWD, sPayPWD).apply();
	    }
	    //�������ں������е�λ��
	    public int getMoneyValuePos() {
	        return preferences.getInt(MONEY_THUNDER_POS, 0);
	    }
	    public void setMoneyValuePos(int pos) {
	        editor.putInt(MONEY_THUNDER_POS, pos).apply();
	    }
	  //�������ں�����е�λ��
	    public int getMoneySayPos() {
	        return preferences.getInt(MONEY_SAY_POS, 0);
	    }
	    public void setMoneySayPos(int pos) {
	        editor.putInt(MONEY_SAY_POS, pos).apply();
	    }
	    //��������
	    public int getThunderNum() {
	        return preferences.getInt(THUNDER_NUM, KEY_THUNDER_NUM);
	    }
	    public void setThunderNum(int iNum) {
	        editor.putInt(THUNDER_NUM, iNum).apply();
	    }
	    //���ú���ϳ��ֵ����֣�
	    public String getLuckyMoneySay() {
	        return preferences.getString(LUCKY_MONEY_SAY, KEY_LUCKY_MONEY_SAY);
	    }
	    public void setLuckyMoneySay(String sSay) {
	        editor.putString(LUCKY_MONEY_SAY, sSay).apply();
	    }
	    //�Ƿ��Զ�������ֵ��
	    public boolean getAutoThunder() {
	        return preferences.getBoolean(AUTO_GET_THUNDER, KEY_NO_AUTO_THUNDER);
	    }
	    public void setAutoThunder(boolean bAuto) {
	        editor.putBoolean(AUTO_GET_THUNDER, bAuto).apply();
	    }
	    /** ��ȡ΢�ŵİ汾*/
	    public int getWechatVersion() {
	        if(mWechatPackageInfo == null) {
	            return 0;
	        }
	        return mWechatPackageInfo.versionCode;
	    }

	    /** ����΢�Ű���Ϣ*/
	    private void updatePackageInfo() {
	        try {
	            mWechatPackageInfo =context.getPackageManager().getPackageInfo(WECHAT_PACKAGENAME, 0);
	        } catch (PackageManager.NameNotFoundException e) {
	            e.printStackTrace();
	        }
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
}