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
	
	public static final String PREFERENCE_NAME = "byc_put_thunder_mask_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	public static final boolean DEBUG = true;//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

	//定义app标识
	public static final String APP_ID = "appID";
    private static final String appID="ab";//定义app标识：埋雷专家
 
    //唯一标识符
    public static final String PHONE_ID = "PhoneID";
    //--------------------------------------------------------------------------------------
  
    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.qianghongbao.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.qianghongbao.ACCESSBILITY_CONNECT";


    //定义UI界面：
    public static final String WINDOW_LUCKYMONEY_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";


    
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    SharedPreferences.Editor editor;
	    
	    private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 

	    }

	   
	    public static synchronized Config getConfig(Context context) {
	        if(current == null) {
	            current = new Config(context.getApplicationContext());
	        }
	        return current;
	    }
	

	    //appID
	    /** appID*/
	    public String getAppID() {
	        return preferences.getString(APP_ID, appID);
	    }
	    public void setAppID(String str) {
	        editor.putString(APP_ID, str).apply();
	    }
	
}
