/**
 * 
 */

package com.example.h3;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
/**
 * @author byc
 *
 */
public class Config {
	
	public static final String PREFERENCE_NAME = "byc_nnMask_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	public static final boolean DEBUG = true;//调试标识：
	//微信的包名
	public static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    //--------------------------------------------------------------------------------------
    //界面参数（用户参数）：

	//微信定义：
    public static final String WINDOW_LUCKYMONEY_RECEIVEUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String WINDOW_LUCKYMONEY_DETAILUI="com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

    //广播消息定义
    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.byc.qianghongbao.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.byc.qianghongbao.ACCESSBILITY_CONNECT";

    //牛牛玩法定义：
    public static final int NN_SINGLE=1;
    public static final int NN_DOUBLE=2;
    public static final int NN_THREE=3;
    public static final int NN_HT=4;//首尾；
    public static final int NN_PG=5;//牌九；
    
    private static final String NN_WANG_FA="NN_WangFa";//--设置雷在红包中的位置

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
	 	    //NN 玩法参数：
	    public int getNnWangFa() {
	        return preferences.getInt(NN_WANG_FA, 1);
	    }
	    public void setNnWangFa(int iWangFa) {
	        editor.putInt(NN_WANG_FA, iWangFa).apply();
	    }	
	    
}
