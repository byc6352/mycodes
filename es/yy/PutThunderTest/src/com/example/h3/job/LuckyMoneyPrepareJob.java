package com.example.h3.job;

import com.example.h3.Config;

import android.annotation.TargetApi;
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
    //输入文本：
    public boolean InputText(AccessibilityEvent event) {
    	String ui=event.getClassName().toString();
    	AccessibilityNodeInfo target=null;
    	String id="";
    	String sText="";
    	String sOrder="";
    	
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {return false;}
        if(ui.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
        	
        	//Bundle arguments = new Bundle();
        	//1。设置包数：--------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneyNum;
        	target.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	sOrder="input text "+sText;
        	RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
        	AccessibilityHelper.Sleep(1000);
        	//2。设置发包金额：----------------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,1);
        	if(target==null)return false;
        	sText=Config.sMoneyValue;
        	target.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	sOrder="input text "+sText;
        	RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
        	AccessibilityHelper.Sleep(1000);
        	//3。设置红包语
        	id = "com.tencent.mm:id/bdw";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneySay;
        	target.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	sOrder="input text "+sText;
        	RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
        	AccessibilityHelper.Sleep(1000);
        	//4.点击塞钱进红包
        	id = "com.tencent.mm:id/b9s";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	return true;
        }
        return false;
    }

    //输入文本：
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public boolean InputText2(AccessibilityEvent event) {
    	String ui=event.getClassName().toString();
    	AccessibilityNodeInfo target=null;
    	String id="";
    	String sText="";
    	
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {         
            return false;
        }
        if(ui.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyPrepareUI")){
        	
        	Bundle arguments = new Bundle();
        	//1。设置包数：--------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneyNum;
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
        	target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	//2。设置发包金额：----------------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,1);
        	if(target==null)return false;
        	sText=Config.sMoneyValue;
          	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
        	target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	//3。设置红包语
        	id = "com.tencent.mm:id/bdw";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneySay;
          	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, sText);
        	target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	//4.点击塞钱进红包
        	id = "com.tencent.mm:id/b9s";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	return true;
        }
        return false;
    }
    
}
