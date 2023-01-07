package com.example.h3.job;

import com.example.h3.Config;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class LuckyMoneyPrepareJob {
	private static LuckyMoneyPrepareJob current;
	private Context context;
	private static String TAG = "byc001";
	
	private LuckyMoneyPrepareJob(Context context) {
		this.context = context;
		TAG=Config.TAG;
	}
    public static synchronized LuckyMoneyPrepareJob getLuckyMoneyPrepareJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPrepareJob(context);
        }
        current.context=context;
        return current;
        
    }
    public boolean inputText(AccessibilityEvent event){
    	String ui=event.getClassName().toString();

        AccessibilityNodeInfo rootNode = event.getSource();
        if (rootNode == null) {return false;}
        if(ui.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
        	String txt="";
        	//个数：(rootNode, "本群共", 0)
        	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 0,true);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneyNum;
        	if(!nodeInput(target,txt))return false;
        	//金额：
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "输入金额", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText",1,false);
        	if(target==null)return false;
        	txt=Config.sMoneyValue;
        	if(!nodeInput(target,txt))return false;
        	//红包语：
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "恭喜发财", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 2,false);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneySay;
        	if(!nodeInput(target,txt))return false;
        	//塞钱：
        	target=AccessibilityHelper.findNodeInfosByText(rootNode, "塞钱进红包", 0) ;
        	if(target==null)return false;
        	if(!target.performAction(AccessibilityNodeInfo.ACTION_CLICK))return false;
        	return true;
        }
        return false;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  boolean nodeInput(AccessibilityNodeInfo edtNode,String txt){
    	if(Config.currentapiVersion>=Build.VERSION_CODES.LOLLIPOP){//android 5.0
    		Bundle arguments = new Bundle();
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
        	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	return true;
    	}
    	if(Config.currentapiVersion>=Build.VERSION_CODES.JELLY_BEAN_MR2){//android 4.3
    		ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
    		ClipData clip = ClipData.newPlainText("text",txt);  
    		clipboard.setPrimaryClip(clip);  

    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  
    		////粘贴进入内容  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	if(Config.currentapiVersion>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(100);
        	if(RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(1000);
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
  
       
}
