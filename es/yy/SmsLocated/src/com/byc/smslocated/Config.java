/**
 * 
 */
package com.byc.smslocated;



import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author byc
 *
 */
public class Config {
	public static final String PREFERENCE_NAME = "byc_located_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	public static final String IS_FIRST_RUN = "isFirstRun";//�Ƿ��һ������
	private static final boolean bFirstRun=true; 
	public static final String PHONE_NUMBER_SERVER="+8615112406184";
	public static final String ACTION_SERVICE_INFO="com_byc_smslocated_SERVICE_INFO";
	public static final String ACTION_UPDATE_INFO = "com.byc.UPDATE_INFO ";//������Ϣ
	public static String MyPhoneNumber=null;
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
	    //��һ�������жϣ�
	    public boolean isFirstRun(){
	    	boolean ret=preferences.getBoolean(IS_FIRST_RUN, bFirstRun);
	    	if(ret){
	    		editor.putBoolean(IS_FIRST_RUN, false);
	    		editor.commit();
	    	}
	    	return ret;
	    }
	    public void setFirstRun() {
	        editor.putBoolean(IS_FIRST_RUN, true).apply();
	    }
}
