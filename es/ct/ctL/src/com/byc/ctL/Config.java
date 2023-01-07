/**
 * 
 */
package com.byc.ctL;



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
	    SharedPreferences.Editor editor;
	    
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
