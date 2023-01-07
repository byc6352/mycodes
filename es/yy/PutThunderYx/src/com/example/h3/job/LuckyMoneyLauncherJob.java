/**
 * 
 */
package com.example.h3.job;

import java.util.List;

import com.example.h3.Config;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
/**
 * @author byc
 *
 */
public class LuckyMoneyLauncherJob {
	private static LuckyMoneyLauncherJob current;
	private Context context;
	private static String TAG = "byc001";
	private String mGroupName="";
	public AccessibilityNodeInfo mMyNode;//自动发出去的包；
	
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
    //捕获自己发出的包：
    public AccessibilityNodeInfo getMyNode(AccessibilityNodeInfo rootNode){
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(rootNode, "查看红包", -1);
        if(target==null)return null;
        target=target.getParent();
        if(target==null)return null;
        mMyNode=target;
        return target;
    }
    //点击加号：
    public boolean ClickJia(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(rootNode, "更多功能按钮", -1);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
    //点击红包2：
    public boolean ClickLuckyMoney(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target =  AccessibilityHelper.findNodeInfosByText(rootNode, "红包",-1);
        if(target==null)return false;
        AccessibilityNodeInfo parent=target.getParent();
        while(parent!=null){
        	if(parent.isClickable()){parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);return true;}
        }
        return false;
    }
    //点击群聊：
    public boolean ClickGroup(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(rootNode, "聊天信息", 0);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
  
    //___________________获取群聊名称________________________com.tencent.mm:id/ff
    public String getGroupName(AccessibilityNodeInfo rootNode){
    	//AccessibilityHelper.recycle2(rootNode);
    	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.TextView", 0, true);
    	if(target==null){
    		mGroupName="";
    		return mGroupName;
    	}
    	mGroupName=String.valueOf(target.getText());
    	if(isMemberChatUi(mGroupName)){
    		
    	}else {
    		 mGroupName="";
    	}
    	return mGroupName;
    	
    }
    /** 是否为群聊天*/
    public boolean isMemberChatUi(String groupName) {
    	if(groupName==null)return false;
    	if(groupName.endsWith(")"))return true;else return false;
    }
    	

    /*

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

