/**
 * 
 */
package com.example.h3.job;

import java.util.List;

import com.example.h3.Config;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import accessibility.AccessibilityHelper;
/**
 * @author byc
 *
 */
public class LuckyMoneyLauncherJob {
	private static LuckyMoneyLauncherJob current;
	private Context context;
	private static String TAG = "byc001";
	private static final String DIGITAL="0123456789";//
	public AccessibilityNodeInfo LuckyMoneyNode;//红包来了；

	
	private LuckyMoneyLauncherJob(Context context) {
		this.context = context;
		TAG=Config.TAG;
	}
    public static synchronized LuckyMoneyLauncherJob getLuckyMoneyLauncherJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyLauncherJob(context);
        }
        return current;
        
    }
    /*发送“你领取了”*/
    public void SendYouGet(AccessibilityNodeInfo rootNode){
    	String youget= "你领取了";
    	AccessibilityNodeInfo edtNode=AccessibilityHelper.findNodeInfosByClassName(rootNode,"android.widget.EditText",0,true);
    	if(edtNode==null)return;
    	if(!nodeInput(edtNode,youget))return;
        AccessibilityNodeInfo sendNode = AccessibilityHelper.findNodeInfosByText(rootNode, "发送", -1);
        if(sendNode==null)return;
        sendNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return;
    }
  
    /** 是否为群聊天*/
    public boolean isMemberChatUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        String desc="群资料卡";
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(nodeInfo, desc, 0);
        if(target == null) {
        	return false;
        }else{
        	if("android.widget.ImageView".equals(target.getClassName())) {
        		return true;
        	}//&&desc == String.valueOf(target.getContentDescription())
        	return false;
        }  
    }
    /** 是否为弹出菜单*/
    public boolean isPopmenuUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        String txt="复制";
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="删除";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="撤回";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
       return true;
    }
 
    /*输入文本*/
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

    	return false;
    }      
    public String getSendLuckyMoneyManName(AccessibilityNodeInfo LuckyMoneyNode){
    	
    	AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
    	int i=1;
    	while(parent!=null){
    		
    		parent=parent.getParent();
    		i=i+1;
    		if(i>=6)break;
    	}
    	if(i!=6)return null;
    	if(parent==null)return null;
    	parent=parent.getChild(0);
    	if(parent==null)return null;
    	parent=parent.getChild(2);
    	if(parent==null)return null;
    	if(!"android.widget.ImageView".equals(parent.getClassName())) return null;
    	String desc = String.valueOf(parent.getContentDescription());
    	return desc;
    }
    
}

