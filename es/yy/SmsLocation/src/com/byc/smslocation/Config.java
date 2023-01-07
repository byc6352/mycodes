/**
 * 
 */
package com.byc.smslocation;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author byc
 *
 */
public class Config {
	public static final String PREFERENCE_NAME = "byc_Location_config";//配置文件名称
	
	public static final String TAG = "byc001";//调试标识：
	private static Config current;
	private SharedPreferences preferences;
	private Context context;
	private SharedPreferences.Editor editor;
	private Set<String> setPhoneNumbers = null; 
    //----------------------------------------------------------------------------------------
    private static final String SEL_PHONE_NUMBER_INDEX="Sel_Phone_Number_Index";//--所选择 的手机存储
    public static int iSelPhoneNumber=-1;//--选择的手机编号;
    private static final String PHONE_NUMBER_ARRARY="Phone_Number_Array";//--手机存储
    public static String[] PhoneNumbers=null;//--手机编号;
    public static String CurmPhoneNumber=null;//当前手机号
    //----------------------------------------------------------------------------------------
    public static int screenWidth=0;
    public static int screenHeight=0;
    public static int currentapiVersion=0;
	private Config(Context context) {
	        this.context = context;
	        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	        editor = preferences.edit(); 
	        setPhoneNumbers = new HashSet<String>();
	}
    public static synchronized Config getConfig(Context context) {
        if(current == null) {
            current = new Config(context);
        }
        return current;
    }
    //-------所选择手机编号-----------------------------------------------------
    public int getSelPhoneNumber() {
        return preferences.getInt(SEL_PHONE_NUMBER_INDEX, -1);
    }
    public void setSelPhoneNumber(int idx) {
        editor.putInt(SEL_PHONE_NUMBER_INDEX, idx).apply();
    }
    //-------手机号列表-----------------------------------------------------
    public String[] getPhoneNumbers() {
        //return preferences.getInt(SEL_PHONE_NUMBER_INDEX, -1);
    	setPhoneNumbers = preferences.getStringSet(PHONE_NUMBER_ARRARY, setPhoneNumbers); 
    	if (setPhoneNumbers.size() > 0) { 
    		PhoneNumbers= (String[]) setPhoneNumbers.toArray(new String[setPhoneNumbers.size()]);   //将SET转换为数组
    		return PhoneNumbers;
    	}else{
    		return null;
    	}
    }
    public void addPhoneNumbers(String phoneNumber) {
    	setPhoneNumbers = new HashSet<String>(setPhoneNumbers);
    	setPhoneNumbers.add(phoneNumber);
        editor.putStringSet(PHONE_NUMBER_ARRARY, setPhoneNumbers).apply();
    }
  //判断电话号码的格式是否正确
    public static boolean isPhoneNumberValid(String phoneNumber){
    		boolean valid=false;
    	/**
    	* 两种电话号码格式
    	* ^\\(? 表示可以以(开头
    	* (\\d{3}) 表示后面紧跟3个数字
    	* \\)? 表示可以以)继续
    	* [- ]? 表示在上述格式后面可以使用选择性的“-”继续
    	* (\\d{4}) 表示后面紧跟4个数字
    	* [- ]? 表示在上述格式后面可以使用选择性的“-"继续
    	* (\\d{4})$ 表示以4个数字结束
    	* 综上所述：正确的电话号码的格式可以以下面等几种做为参考：
    	* (123)456-78900 123-456-78900 12345678900 (123)-456-78900*/
    	String expression01="^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
    	String expression02="^\\(?(\\d{3})\\)?[- ]?(\\d{5})[- ]?(\\d{5})$";
    	//创建Pattern对象
    	Pattern p01=Pattern.compile(expression01);
    	//将Pattern作为参数传入Matcher，当做电话号码phoneNumber的正确格式
    	Matcher m01=p01.matcher(phoneNumber);
    	Pattern p02=Pattern.compile(expression02);
    	Matcher m02=p02.matcher(phoneNumber);
    	if(m01.matches()||m02.matches()){
    		valid=true;
    	}else{
    		valid=false;
    	}
    	return valid;
    }

}
