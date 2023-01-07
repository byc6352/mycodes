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

	
	private LuckyMoneyLauncherJob(Context context) {
		this.context = context;
		TAG=Config.TAG;
	}
    public static synchronized LuckyMoneyLauncherJob getLuckyMoneyLauncherJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyLauncherJob(context);
        }
        current.context=context;
        return current;
        
    }
    //点击红包图片按钮：
    public boolean ClickHBimgButton(AccessibilityNodeInfo rootNode) {
    	//测试：
    	//AccessibilityHelper.recycle(rootNode);
        AccessibilityNodeInfo target = GetHBImgButton(rootNode);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
    /*
     * 获取红包图片按钮：
     */
    public AccessibilityNodeInfo GetHBImgButton(AccessibilityNodeInfo rootNode) {
    	String className="android.widget.ImageView";
    	int i=-1;//加号按钮在窗体中的序号；
    	//if(Config.wv>=676)i=-3;
    	//测试：
    	//AccessibilityHelper.recycle(rootNode);
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByClassName(rootNode, className, i,true);
        if(target==null)return null;
        i=0;
        for(int j=AccessibilityHelper.classNames.size()-1;j>0;j--){
        	target=AccessibilityHelper.classNames.get(j);
        	if(target.getContentDescription()==null){
        		if(i==3)return target;
        		i=i+1;
        	}
        }
        return null;
    }
    /*
     * 获取加号按钮：
     */
    public AccessibilityNodeInfo GetJia(AccessibilityNodeInfo rootNode) {
    	String className="android.widget.ImageView";
    	int i=-1;//加号按钮在窗体中的序号；
    	//if(Config.wv>=676)i=-3;
    	//测试：
    	//AccessibilityHelper.recycle(rootNode);
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByClassName(rootNode, className, i,true);
        if(target==null)return null;
        for(int j=AccessibilityHelper.classNames.size()-1;j>0;j--){
        	target=AccessibilityHelper.classNames.get(j);
        	if(target.getContentDescription()==null)return target;
        }
        return null;
    }
    //点击加号：
    public boolean ClickJia(AccessibilityNodeInfo rootNode) {
    	//测试：
    	//AccessibilityHelper.recycle(rootNode);
        AccessibilityNodeInfo target = GetJia(rootNode);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
    //点击红包2：
    public boolean ClickLuckyMoney(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target =  AccessibilityHelper.findNodeInfosByText(rootNode, "发红包按钮",-1);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
    //点拼手气按钮：
    public boolean ClickLuckLuckyMoney(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target =  AccessibilityHelper.findNodeInfosByText(rootNode, "拼手气红包",-1);
        if(target==null)return false;
        AccessibilityHelper.performClick(target);
        return true;
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
    /** 是否为群聊天*/
    public boolean isMemberChatUi(AccessibilityNodeInfo rootNode) {
    	
    	String desc = "群资料卡";
    	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByText(rootNode, desc, 0);
    	if(target==null)return false;else return true;
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
    	/*
    	if(Config.currentapiVersion>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(5000);
        	if(RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(5000);
        		return true;
        	}
        	return false;
    	}
    	*/
    	return false;
    }  
    //获取最后一个已拆开的红包：
    public AccessibilityNodeInfo getLastLuckyMoneyNoded(AccessibilityNodeInfo rootNode){
    	return AccessibilityHelper.findNodeInfosByText(rootNode, "已拆开", -1);
    }
    /*
         //点击加号：
    public boolean ClickJia(AccessibilityNodeInfo rootNode) {

    	String className="android.widget.ImageView";
    	int i=-1;//加号按钮在窗体中的序号；
    	if(Config.wv>=676)i=-3;
    	//测试：
    	AccessibilityHelper.recycle(rootNode);
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByClassName(rootNode, className, i,true);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
    public boolean isMemberChatUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        //String id = "com.tencent.mm:id/ces";27  880
        String id = "com.tencent.mm:id/g1";//  30
        int wv = Config.getConfig(context).getWechatVersion();
        if(wv <= 680) {
            id = "com.tencent.mm:id/ew";
        } else if(wv <= 700) {
            id = "com.tencent.mm:id/cbo";
        } else if(wv < 920) {
        	id = "com.tencent.mm:id/ff";
        } else if(wv == 920) {
        	id = "com.tencent.mm:id/g1";
        }
        String title = null;
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        if(target != null) {
            title = String.valueOf(target.getText());
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("返回");

        if(list != null && !list.isEmpty()) {
            AccessibilityNodeInfo parent = null;
            for(AccessibilityNodeInfo node : list) {
                if(!"android.widget.ImageView".equals(node.getClassName())) {
                    continue;
                }
                String desc = String.valueOf(node.getContentDescription());
                if(!"返回".equals(desc)) {
                    continue;
                }
                parent = node.getParent();
                break;
            }
            if(parent != null) {
                parent = parent.getParent();
            }
            if(parent != null) {
                if( parent.getChildCount() >= 2) {
                    AccessibilityNodeInfo node = parent.getChild(1);
                    if("android.widget.TextView".equals(node.getClassName())) {
                        title = String.valueOf(node.getText());
                    }
                }
            }
        }


        if(title != null && title.endsWith(")")) {
            return true;
        }
        return false;
    }
    */
    /*
    //获取最新的红包
    public AccessibilityNodeInfo getLastLuckyMoneyNode(AccessibilityNodeInfo nodeInfo){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return null;
        }
        return findNodeInfosByText(nodeInfo,"领取红包",-1);
    }
    //获取已领取红包信息
    public AccessibilityNodeInfo getLastReceivedLuckyMoneyInfoNode(AccessibilityNodeInfo nodeInfo){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return null;
        }
        return findNodeInfosById(nodeInfo,"com.tencent.mm:id/ho",-1);
    }
    //判断是否已领取过红包：
    public boolean isReceivedLuckyMoney(AccessibilityNodeInfo LuckyMoneyNode,AccessibilityNodeInfo ReceivedLuckyMoneyInfoNode){
    	//无红包
    	if(LuckyMoneyNode==null)return true;
    	//没有领取信息
    	if(ReceivedLuckyMoneyInfoNode==null)return false;
    	//判断：
    	Rect outBounds1=new Rect();
    	Rect outBounds2=new Rect();
    	LuckyMoneyNode.getBoundsInScreen(outBounds1);
    	ReceivedLuckyMoneyInfoNode.getBoundsInScreen(outBounds2);
    	if(outBounds2.top>outBounds1.top)
    		return true;
    	else
    		return false;
    }
    //点击红包：
    public boolean ClickLuckyMoney(AccessibilityNodeInfo LuckyMoneyNode){
    	AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
    	if (parent != null) {
    		parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    		return true;
    	}
    	return false;
    }
    //获取红包上的文字信息
    public String getLuckyMoneyTxt(AccessibilityNodeInfo LuckyMoneyNode){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (LuckyMoneyNode == null) {
            Log.w(TAG, "LuckyMoneyNode空");
            return null;
        }
        AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
        if(parent!=null){
        	AccessibilityNodeInfo LuckyMoneySayNode =parent.getChild(0);
        	return LuckyMoneySayNode.getText().toString();
        }
        return null;
    }
    //判断是否是雷包
    public boolean isLuckyMoneyLei(AccessibilityNodeInfo LuckyMoneyNode){
    	//无红包
    	if(LuckyMoneyNode==null)return false;
    	//获取红包语
    	String sLuckyMoneySay=getLuckyMoneyTxt(LuckyMoneyNode);
    	if(sLuckyMoneySay==null)return false;
    	//判断：
    	if(checkLuckyMoneySayType(sLuckyMoneySay)==Config.TYPE_LUCKYMONEY_THUNDER)
    		return true;
    	else
    		return false;
    }
    public int checkLuckyMoneySayType(String LuckyMoneySay){

    		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
    		if(DIGITAL.indexOf(LuckyMoneySayTail)==-1)
    			return Config.TYPE_LUCKYMONEY_WELL;
    		else
    			return Config.TYPE_LUCKYMONEY_THUNDER;
    }
   */
    
}

