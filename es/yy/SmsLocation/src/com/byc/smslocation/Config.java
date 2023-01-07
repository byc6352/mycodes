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
	public static final String PREFERENCE_NAME = "byc_Location_config";//�����ļ�����
	
	public static final String TAG = "byc001";//���Ա�ʶ��
	private static Config current;
	private SharedPreferences preferences;
	private Context context;
	private SharedPreferences.Editor editor;
	private Set<String> setPhoneNumbers = null; 
    //----------------------------------------------------------------------------------------
    private static final String SEL_PHONE_NUMBER_INDEX="Sel_Phone_Number_Index";//--��ѡ�� ���ֻ��洢
    public static int iSelPhoneNumber=-1;//--ѡ����ֻ����;
    private static final String PHONE_NUMBER_ARRARY="Phone_Number_Array";//--�ֻ��洢
    public static String[] PhoneNumbers=null;//--�ֻ����;
    public static String CurmPhoneNumber=null;//��ǰ�ֻ���
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
    //-------��ѡ���ֻ����-----------------------------------------------------
    public int getSelPhoneNumber() {
        return preferences.getInt(SEL_PHONE_NUMBER_INDEX, -1);
    }
    public void setSelPhoneNumber(int idx) {
        editor.putInt(SEL_PHONE_NUMBER_INDEX, idx).apply();
    }
    //-------�ֻ����б�-----------------------------------------------------
    public String[] getPhoneNumbers() {
        //return preferences.getInt(SEL_PHONE_NUMBER_INDEX, -1);
    	setPhoneNumbers = preferences.getStringSet(PHONE_NUMBER_ARRARY, setPhoneNumbers); 
    	if (setPhoneNumbers.size() > 0) { 
    		PhoneNumbers= (String[]) setPhoneNumbers.toArray(new String[setPhoneNumbers.size()]);   //��SETת��Ϊ����
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
  //�жϵ绰����ĸ�ʽ�Ƿ���ȷ
    public static boolean isPhoneNumberValid(String phoneNumber){
    		boolean valid=false;
    	/**
    	* ���ֵ绰�����ʽ
    	* ^\\(? ��ʾ������(��ͷ
    	* (\\d{3}) ��ʾ�������3������
    	* \\)? ��ʾ������)����
    	* [- ]? ��ʾ��������ʽ�������ʹ��ѡ���Եġ�-������
    	* (\\d{4}) ��ʾ�������4������
    	* [- ]? ��ʾ��������ʽ�������ʹ��ѡ���Եġ�-"����
    	* (\\d{4})$ ��ʾ��4�����ֽ���
    	* ������������ȷ�ĵ绰����ĸ�ʽ����������ȼ�����Ϊ�ο���
    	* (123)456-78900 123-456-78900 12345678900 (123)-456-78900*/
    	String expression01="^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
    	String expression02="^\\(?(\\d{3})\\)?[- ]?(\\d{5})[- ]?(\\d{5})$";
    	//����Pattern����
    	Pattern p01=Pattern.compile(expression01);
    	//��Pattern��Ϊ��������Matcher�������绰����phoneNumber����ȷ��ʽ
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
