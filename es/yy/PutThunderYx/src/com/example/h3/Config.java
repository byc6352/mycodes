/**
 * 
 */

package com.example.h3;
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
	
	public static final String PREFERENCE_NAME = "byc_putthunderYx_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	public static final boolean DEBUG = true;//���Ա�ʶ��
	//΢�ŵİ���
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
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
    public static final String KEY_WECHAT_DELAY_TIME = "KEY_WECHAT_DELAY_TIME";
    private static final int key_wechat_delay_time=0;//--
    
    private static final String MONEY_THUNDER_POS="Money_Thunder_Pos";//--�������ں���е�λ��
    public static final int KEY_THUNDER_FEN=0;//--��Ϊ��
    public static final int KEY_THUNDER_JIAO=1;//--��Ϊ��
    public static final int KEY_THUNDER_YUAN=2;//--ԪΪ��
    
    private static final String MONEY_SAY_POS="Money_Say_Pos";//--�������ں�����е�λ��
    public static final int KEY_THUNDER_TAIL=0;//--βΪ��
    public static final int KEY_THUNDER_HEAD=1;//--��Ϊ��
    
    private static final String PUT_ROB_TAIL="Put_Rob_tail";//--���׺���β��
    public static final boolean KEY_NO_ROB_TAIL=false;//--����β
    public static final boolean KEY_ROB_TAIL=true;//--��β
    public static boolean bRobTail=false;
	//΢�Ŷ��壺
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static final int TYPE_LUCKYMONEY_NONE=0;//������ĺ����
    public static final int TYPE_LUCKYMONEY_THUNDER=1;//���׵ĺ����
    public static final int TYPE_LUCKYMONEY_WELL=2;//�����ĺ����
    //�㲥��Ϣ����
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.qianghongbao.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.qianghongbao.ACCESSBILITY_CONNECT";

    public static final String ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT = "com.byc.qianghongbao.NOTIFY_LISTENER_DISCONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "com.byc.qianghongbao.NOTIFY_LISTENER_CONNECT";
    public static final String ACTION_PUT_PWD_DELAY = "com.byc.qianghongbao.PUT_PWD_DELAY ";//����������ʱ��Ϣ
    //���巢��������
    public static String sMoneyValue="";	//������
    public static String sLuckyMoneyNum="";	//������
    public static String sPWD="123456";			//���룻
    public static String sLuckyMoneySay="";	//�����
    //�����Ƿ�ʼ������
    public static boolean bSendLuckyMoney=false;
    //����UI���棺
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    public static final String WINDOW_LUCKYMONEY_PREPARE_UI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyPrepareUI";
    public static final String WINDOW_LUCKYMONEY_WALLETPAY_UI="com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI";
    public static final String WINDOW_LUCKYMONEY_WALLET_UI="com.tencent.mm.plugin.wallet_core.ui.k";
    public static final String WINDOW_LUCKYMONEY_CHATROOM_UI="com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
    //����״̬��
    public static int JobState=0;//--
    public static final int STATE_NONE=0;//����״̬
    public static final int STATE_SENDING_LUCKYMONEY=1;//���ͺ��״̬
    public static final int STATE_INPUT_TEXT=2;//�����ı�״̬
    public static final int STATE_INPUT_PWD=3;//��������״̬
    public static final int STATE_INPUT_CLOSING=4;//���׸ոս���״̬
    public static final int STATE_INPUT_GROUP=5;//����Ⱥ��״̬
    public static final int STATE_INPUT_ROB_TAIL=5;//������β״̬
    //��β������
    public static int robTailAction=0;
    public static final int ACTION_LOOK=0;//��ѯ��
    public static final int ACTION_CLICK=1;//������
    public static final int ACTION_CLOSE=2;//������
    //��Ļ�ֱ��ʣ�=
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //��Ϣ���壺
    public static int MSG_REG=0x11;			//ע����Ϣ��
    public static int MSG_PUT_THUNDER=0x16;//���׿�ʼ��Ϣ��
    public static int MSG_CLOSE_ROB_TAIL=0x17;//��ֹɨβ��Ϣ��
    //�Ƿ�ע�᣺
    public static boolean bReg=false;
    //Ⱥ���ƣ�
    public static final String GROUP_NAME = "GroupName";
    
    
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
	        	this.SetWechatOpenDelayTime(key_wechat_delay_time);
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
	        int result = preferences.getInt(KEY_WECHAT_DELAY_TIME, defaultValue);
	        try {
	            return result;
	        } catch (Exception e) {}
	        return defaultValue;
	    }
	    //����΢�Ŵ򿪺������ʱʱ��
	    public int SetWechatOpenDelayTime(int DelayTime) {
	        
	        editor.putInt(KEY_WECHAT_DELAY_TIME, DelayTime); 
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
	    //���������
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
	    //��β��
	    public boolean getRobTail() {
	        return preferences.getBoolean(PUT_ROB_TAIL,true);
	    }
	    public void setRobTail(boolean bRob) {
	        editor.putBoolean(PUT_ROB_TAIL, bRob).apply();
	    }
	    //Ⱥ���ƣ�
	    public String getGroupName() {
	        return preferences.getString(GROUP_NAME, "");
	    }
	    public void setGroupName(String groupName) {
	        editor.putString(GROUP_NAME, groupName).apply();
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
}
