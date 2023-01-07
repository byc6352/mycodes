/**
 * 
 */
package com.byc.control;



import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author byc
 *
 */
public class Config {
	   private static Config current;
	    private SharedPreferences preferences;
	    private Context context;
	    private SharedPreferences.Editor editor;
		public static final String PREFERENCE_NAME = "byc_control_config";//�����ļ�����
		public static final String ACTION_SERVICE_INFO = "byc_control_action_service_info";
		public static final String SERVICE_INFO_LOCATION = "byc_control_action_service_location";
		
		public static final String TAG = "byc001";//���Ա�ʶ��
	    //������IP
	    //public static final String HOST = "host";
		public static final String host = "119.23.68.205";
		//�������˿�
		//public static final String PORT = "port";
		public static final int port_order = 8100;//������ն˿�
		public static final int port_data = 8101;//���ݽ��ն˿�
	    
	    private Config(Context context) {
	        this.context = context;
	        //preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        //editor = preferences.edit(); 
	    }
	    public static synchronized Config getConfig(Context context) {
	        if(current == null) {
	            current = new Config(context.getApplicationContext());
	        }
	        return current;
	    }
}
