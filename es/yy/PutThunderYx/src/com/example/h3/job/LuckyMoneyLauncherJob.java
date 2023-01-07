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
	public AccessibilityNodeInfo mMyNode;//�Զ�����ȥ�İ���
	
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
    //�����Լ������İ���
    public AccessibilityNodeInfo getMyNode(AccessibilityNodeInfo rootNode){
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(rootNode, "�鿴���", -1);
        if(target==null)return null;
        target=target.getParent();
        if(target==null)return null;
        mMyNode=target;
        return target;
    }
    //����Ӻţ�
    public boolean ClickJia(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(rootNode, "���๦�ܰ�ť", -1);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
    //������2��
    public boolean ClickLuckyMoney(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target =  AccessibilityHelper.findNodeInfosByText(rootNode, "���",-1);
        if(target==null)return false;
        AccessibilityNodeInfo parent=target.getParent();
        while(parent!=null){
        	if(parent.isClickable()){parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);return true;}
        }
        return false;
    }
    //���Ⱥ�ģ�
    public boolean ClickGroup(AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(rootNode, "������Ϣ", 0);
        if(target==null)return false;
        target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return true;
    }
  
    //___________________��ȡȺ������________________________com.tencent.mm:id/ff
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
    /** �Ƿ�ΪȺ����*/
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
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("����");

        if(list != null && !list.isEmpty()) {
            AccessibilityNodeInfo parent = null;
            for(AccessibilityNodeInfo node : list) {
                if(!"android.widget.ImageView".equals(node.getClassName())) {
                    continue;
                }
                String desc = String.valueOf(node.getContentDescription());
                if(!"����".equals(desc)) {
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
    //��ȡ���µĺ��
    public AccessibilityNodeInfo getLastLuckyMoneyNode(AccessibilityNodeInfo nodeInfo){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindowΪ��");
            return null;
        }
        return findNodeInfosByText(nodeInfo,"��ȡ���",-1);
    }
    //��ȡ����ȡ�����Ϣ
    public AccessibilityNodeInfo getLastReceivedLuckyMoneyInfoNode(AccessibilityNodeInfo nodeInfo){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindowΪ��");
            return null;
        }
        return findNodeInfosById(nodeInfo,"com.tencent.mm:id/ho",-1);
    }
    //�ж��Ƿ�����ȡ�������
    public boolean isReceivedLuckyMoney(AccessibilityNodeInfo LuckyMoneyNode,AccessibilityNodeInfo ReceivedLuckyMoneyInfoNode){
    	//�޺��
    	if(LuckyMoneyNode==null)return true;
    	//û����ȡ��Ϣ
    	if(ReceivedLuckyMoneyInfoNode==null)return false;
    	//�жϣ�
    	Rect outBounds1=new Rect();
    	Rect outBounds2=new Rect();
    	LuckyMoneyNode.getBoundsInScreen(outBounds1);
    	ReceivedLuckyMoneyInfoNode.getBoundsInScreen(outBounds2);
    	if(outBounds2.top>outBounds1.top)
    		return true;
    	else
    		return false;
    }
    //��������
    public boolean ClickLuckyMoney(AccessibilityNodeInfo LuckyMoneyNode){
    	AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
    	if (parent != null) {
    		parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    		return true;
    	}
    	return false;
    }
    //��ȡ����ϵ�������Ϣ
    public String getLuckyMoneyTxt(AccessibilityNodeInfo LuckyMoneyNode){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (LuckyMoneyNode == null) {
            Log.w(TAG, "LuckyMoneyNode��");
            return null;
        }
        AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
        if(parent!=null){
        	AccessibilityNodeInfo LuckyMoneySayNode =parent.getChild(0);
        	return LuckyMoneySayNode.getText().toString();
        }
        return null;
    }
    //�ж��Ƿ����װ�
    public boolean isLuckyMoneyLei(AccessibilityNodeInfo LuckyMoneyNode){
    	//�޺��
    	if(LuckyMoneyNode==null)return false;
    	//��ȡ�����
    	String sLuckyMoneySay=getLuckyMoneyTxt(LuckyMoneyNode);
    	if(sLuckyMoneySay==null)return false;
    	//�жϣ�
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

