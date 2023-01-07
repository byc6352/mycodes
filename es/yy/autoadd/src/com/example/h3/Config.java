/**
 * 
 */

package com.example.h3;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.baidu.android.common.logging.Log;
import com.example.h3.util.VersionParam;
import com.example.h3.util.ftp;

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
	
	public static final String PREFERENCE_NAME = "byc_clearthunder_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	public static final boolean DEBUG = true;//���Ա�ʶ��
	//΢�ŵİ���
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
    /** ������ʹ������ƥ�����С�汾�� */
    private static final int USE_ID_MIN_VERSION = 700;// 6.3.8 ��ӦcodeΪ680,6.3.9��ӦcodeΪ700
    private PackageInfo mWechatPackageInfo = null;
    private PackageInfo mQqPackageInfo = null;
    //ע��ṹ��
    //00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //01��ע�᣻8760ʹ��ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //�������ô�����TestNum="0003";ʹ��3�Σ�
	public static final String IS_FIRST_RUN = "isFirstRun";//�Ƿ��һ������
	private static final boolean bFirstRun=true; 
	//����app��ʶ
	public static final String APP_ID = "appID";
    private static final String appID="ba";//����app��ʶ��ͨ��΢��
    //������IP
    public static final String HOST = "host";
	public static final String host = "119.23.68.205";
	//�������˿�
	public static final String PORT = "port";
	private static final int port = 8000;
	
    //private static final String HOST2 = "101.200.200.78";
	//�Ƿ�ע��:
	public static final String REG = "reg";
	private static final boolean reg = false;
	public static  boolean bReg = false;
	//ע���룺
	private static final String REG_CODE="Reg_Code";
	private String RegCode="123456789012";
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
	//�ʺ��
	private static final String ADD_FRIEND_SAY="Add_Friend_Say";
	public static String sAddFriendSay="�ˣ������ױ��׺�������������ã�";
	//������������������ڣ�
	private static final String SCROLL_GALLERY_DOWN_DATE="Scroll_Gallery_down_date";
	public static String sScrollGalleryDownDate="2017-06-01";

	//΢�Ŷ��壺
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI_2="com.tencent.mm.plugin.luckymoney.ui.En_";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    //Ⱥ��Ա��Ϣ���棺
    public static final String WINDOW_CHATROOM_CHATROOMINFO_UI="com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";//
    //��Ա��ӽ��棺
    public static final String WINDOW_CONTACT_CONTACTINFO_UI="com.tencent.mm.plugin.profile.ui.ContactInfoUI";
    //��֤������棺
    public static final String WINDOW_SAYHI_PERMISSION_UI="com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI";
    //public static final String WINDOW_CHATROOM_CHATROOMINFO_UI="com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
    //QQȺ��Ա�б���棺
    public static final String WINDOW_QQ_TROOP_MEMBER_LIST_UI="com.tencent.mobileqq.activity.TroopMemberListActivity";//
    //QQȺ��Ա���Ͽ����棺
    public static final String WINDOW_QQ_TROOP_MEMBER_CARD_UI="com.tencent.mobileqq.activity.TroopMemberCardActivity";//
    //QQ���Ⱥ��Ա��֤���棺com.tencent.mobileqq.activity.AddFriendVerifyActivity
    public static final String WINDOW_QQ_ADD_FRIEND_VERIFY_UI="com.tencent.mobileqq.activity.AddFriendVerifyActivity";//
    //QQ���Ⱥ��Աʧ�ܽ��棺com.tencent.mobileqq.activity.AddFriendVerifyActivity
    public static final String WINDOW_QQ_ADD_FRIEND_FAIL_UI="android.app.Dialog";//
    //QQȺ���ý��棺com.tencent.mobileqq.activity.AddFriendVerifyActivity
    public static final String WINDOW_QQ_SETTING_TROOP_UI="com.tencent.mobileqq.activity.ChatSettingForTroop";//com.tencent.mobileqq.activity.ChatSettingForTroop
    

    //�㲥��Ϣ����
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.qianghongbao.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.qianghongbao.ACCESSBILITY_CONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT = "com.byc.qianghongbao.NOTIFY_LISTENER_DISCONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "com.byc.qianghongbao.NOTIFY_LISTENER_CONNECT";

    
    //��Ļ�ֱ��ʣ�
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
    //����״̬��
    public static int JobState=0;//--
    public static final int STATE_NONE=0;//����״̬
    public static final int STATE_CLICK_LUCKYMONEY=1;//����״̬
    public static final int STATE_CONTACT_INFO_UNADD=1;//��ϵ����ϸ��Ϣ״̬��δ��ӣ�
    public static final int STATE_SAYHI_PERMISSION=2;//��֤����״̬
    public static final int STATE_CONTACT_INFO_ADDED=3;//��ϵ����ϸ��Ϣ״̬������ӣ�
    public static final int STATE_SCROLL_PAGE=4;//��ҳ
    public static final int STATE_TROOP_MEMBER_CARD_UNADD=5;//qq��Ա���Ͽ�
    public static final int STATE_ADD_FRIEND_VERIFY=6;//qq��ӳ�Ա��֤
    public static final int STATE_ADD_FRIEND_VERIFY_FAIL=61;//qq��ӳ�Ա��֤ʧ��
    public static final int STATE_TROOP_MEMBER_CARD_ADDED=7;//qq��Ա���Ͽ�
    
    public static final int STATE_OPEN_GALLERY=101;//����Ƶ״̬
    public static final int STATE_SCROLL_GALLERY=201;//�������״̬
    public static final int STATE_SWIPE_GALLERY=301;//������Ƶ״̬
    
    public static int iWorkPlatform=0;//
    public static final int WORK_PLATFORM_WECHAT=0;//΢��
    public static final int WORK_PLATFORM_QQ=1;//QQ
    //�汾��
    public static String version="";

	//�Զ�����Ϊ���ð����ʼʱ��
	public static final String START_TEST_TIME = "StartTestTime";
	//�Զ�����Ϊ���ð��ʱ������7�죩
    public static final int TestTimeInterval=7;//-- 
    //΢�Ű汾��
    public static int wv=1020;  
    public static int qv=1020;
    //ftp
    public static String ftpUserName="byc";
    public static String ftpPwd="byc";
    //��������Ϣ��
    public static final String FTP_FILE_NAME="autoadd.xml";//���������ļ�����
    private static final String INFO_NEW_VERSION="Info_New_Version";//--�°汾 ��
    public static String  new_version="1.24";//�°汾�� 
    private static final String INFO_CONTACT="Info_Contact";//--
    public static String contact="QQ��1339524332΢��byc6354";//��ϵ��ʽ
    private static final String INFO_AD="Info_AD";//--
    public static String ad="����ר�ң�100%���ף�����ר��������񼸸��׾ͳ��������������á�";//�����
    private static final String INFO_DOWNLOAD="Info_Download";//--
    public static String download="http://119.23.68.205/android/autoadd.apk";//���ص�ַ
    private static final String INFO_HOMEPAGE="Info_HomePage";//--
    public static String homepage="http://119.23.68.205/android/index.htm";//���ص�ַ
    private static final String INFO_WARNING="Info_Warning";//--
    public static String warning="���棺���ֻ��ͽ�Ⱥ����ʾ����Ⱥ���ѡ���ɫ�������ڣ��뵽�ֻ�����������������ͨ��΢����ʾ�������ڡ�";//���ص�ַ
    //-----------------------����ģ��--------------------------------------------------
    private static final String SPEAKER="Speaker";//--���÷���ģʽ
    public static final String KEY_SPEAKER_NONE="9";//--��������female
    public static final String KEY_SPEAKER_FEMALE="0";//--Ů����
    public static final String KEY_SPEAKER_MALE="1";//--��ͨ������
    public static final String KEY_SPEAKER_SPECIAL_MALE="2";//--�ر������� special
    public static final String KEY_SPEAKER_EMOTION_MALE="3";//--���������emotion
    public static final String KEY_SPEAKER_CHILDREN="4";//--��ж�ͯ����children
    public static String speaker=KEY_SPEAKER_FEMALE;
    
    private static final String WHETHER_SPEAKING="Speek";//--�Ƿ�������ʾ��whether or not
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
	        updatePackageInfo();//���°�װ����Ϣ
	        //��һ�������жϣ�����ǵ�һ�����У�д����Ȩģ���ʼ�����ݣ�
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

	        	this.setCurrentStartTestTime();//�����װʱ��д����Ϊ���ð�Ŀ�ʼʱ�䣻
	        	
	        }
	        //+++++++++++++++++++����ʱ��+����������ڴ�+++++++++++++++
	        Config.bReg=getREG();//ȡע����Ϣ��
	        ftp.getFtp().DownloadStart();//1.���ط�������Ϣ
	        //2.ȡ����������Ϣ��
	        Config.new_version=this.getNewVersion();
	        Config.download=this.getDownloadAddr();
	        Config.contact=this.getContactWay();
	        Config.warning=this.getWarning();
	        Config.homepage=this.getHomepage();
	        Config.ad=this.getAd();
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
	    /*
	     * ��ȡ�Ӻ����ʺ���
	     */
	    public String getAddFriendSay(){
	    	return preferences.getString(ADD_FRIEND_SAY, sAddFriendSay);
	    }
	    public void setAddFriendSay(String addFriendSay){
	    	editor.putString(ADD_FRIEND_SAY, addFriendSay).apply();
	    }
	    /*
	     * ��ȡ����������ᵽ������ڣ�
	     */
	    public String getScrollGalleryDownDate(){
	    	return preferences.getString(SCROLL_GALLERY_DOWN_DATE, sScrollGalleryDownDate);
	    }
	    public void setScrollGalleryDownDate(String ScrollGalleryDownDate){
	    	editor.putString(SCROLL_GALLERY_DOWN_DATE, ScrollGalleryDownDate).apply();
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
	            wv=mWechatPackageInfo.versionCode;
	            VersionParam.init(wv);
	            mQqPackageInfo =context.getPackageManager().getPackageInfo(QQ_PACKAGENAME, 0);
	            qv=mQqPackageInfo.versionCode;
	            VersionParam.init(qv);
	            Log.i(TAG, "΢�Ű汾��"+wv);
	            Log.i(TAG, "QQ�汾��"+qv);
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
	    //----------------------------------�����������Ϣ----------------------------------
	    /** �°汾��*/
	    public String getNewVersion() {
	        return preferences.getString(INFO_NEW_VERSION, new_version);
	    }
	    /** �°汾��*/
	    public void setNewVersion(String version) {
	    	editor.putString(INFO_NEW_VERSION, version).apply();
	    }
	    /** ��ϵ��ʽ*/
	    public String getContactWay() {
	        return preferences.getString(INFO_CONTACT,contact);
	    }
	    /** ��ϵ��ʽ*/
	    public void setContactWay(String contactWay) {
	    	editor.putString(INFO_CONTACT, contactWay).apply();
	    }
	    /** �����*/
	    public String getAd() {
	        return preferences.getString(INFO_AD,ad);
	    }
	    /** �����*/
	    public void setAd(String Ad) {
	    	editor.putString(INFO_AD, Ad).apply();
	    }
	    /** ���µ�ַ*/
	    public String getDownloadAddr() {
	        return preferences.getString(INFO_DOWNLOAD, download);
	    }
	    /** ���µ�ַ*/
	    public void setDownloadAddr(String downloadAddr) {
	    	editor.putString(INFO_DOWNLOAD, downloadAddr).apply();
	    }
	    /**��ҳ��ַ*/
	    public String getHomepage() {
	        return preferences.getString(INFO_HOMEPAGE, homepage);
	    }
	    /** ��ҳ��ַ*/
	    public void setHomepage(String homepage) {
	    	editor.putString(INFO_HOMEPAGE, homepage).apply();
	    }
	    /**������Ϣ*/
	    public String getWarning() {
	        return preferences.getString(INFO_WARNING, warning);
	    }
	    /** ������Ϣ*/
	    public void setWarning(String warning) {
	    	editor.putString(INFO_WARNING, warning).apply();
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
