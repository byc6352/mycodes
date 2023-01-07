/**
 * 
 */
package com.example.h3.job;

import java.util.List;

import com.example.h3.Config;
import ad.VersionParam;
import accessibility.AccessibilityHelper;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
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
	private static final String DIGITAL="0123456789";//
	public AccessibilityNodeInfo LuckyMoneyNode;//������ˣ�

	
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
    /*���͡�����ȡ�ˡ�*/
    public void SendYouGet(AccessibilityNodeInfo rootNode){
    	String youget= "����ȡ��";
    	AccessibilityNodeInfo edtNode=AccessibilityHelper.findNodeInfosByClassName(rootNode,"android.widget.EditText",0,true);
    	if(edtNode==null)return;
    	if(!nodeInput(edtNode,youget))return;
        AccessibilityNodeInfo sendNode = AccessibilityHelper.findNodeInfosByText(rootNode, "����", -1);
        if(sendNode==null)return;
        sendNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        return;
    }
    
    
    
    
    //��ȡ���һ��������жϺ���Ƿ����ˣ�
    public AccessibilityNodeInfo getLastLuckyMoneyNode(AccessibilityNodeInfo rootNode){
    	//return AccessibilityHelper.findNodeInfosByText(rootNode, "�����", -1);
    	if(Config.wv<786)
    		return AccessibilityHelper.findNodeInfosByText(rootNode, "QQ���", -1);
    	else{
    		AccessibilityNodeInfo nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode, "QQ���", -1);
    		if(nodeInfo==null)return null;
    		nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode, "����鿴����", -1);
    		if(nodeInfo==null)return null;
    		if(nodeInfo.getClassName().equals("android.widget.RelativeLayout")&&nodeInfo.isClickable()==true)
    			return nodeInfo;
    	}
    	return null;
    }
    //��ȡ���һ���Ѳ𿪵ĺ����
    public AccessibilityNodeInfo getLastLuckyMoneyNoded(AccessibilityNodeInfo rootNode){
    	return AccessibilityHelper.findNodeInfosByText(rootNode, "�Ѳ�", -1);
    }
    //��������
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean ClickLuckyMoney(AccessibilityNodeInfo LuckyMoneyNode){
    	AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    		if (parent != null) {
    			parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    			return true;
    		}
    	 }
    	return false;
    }
    //��ȡ���һ�����Ժ����
    public AccessibilityNodeInfo getLastLuckyMoneyNode2(AccessibilityNodeInfo rootNode){
    	return AccessibilityHelper.findNodeInfosByText(rootNode, "QQ������԰�", -1);
    }
    //��ȡ����ȡ�����Ϣ
    public AccessibilityNodeInfo getLastReceivedLuckyMoneyInfoNode(AccessibilityNodeInfo nodeInfo){
    	//(nodeInfo,"com.tencent.mm:id/ho",-1)
        if (nodeInfo == null)return null;//{"icon":"qqwallet_custom_tips_icon.png","alt":""} ����ȡ��
        //return AccessibilityHelper.findNodeInfosByText(nodeInfo, "{\"icon\":\"qqwallet_custom_tips_icon.png\",\"alt\":\"\"}����ȡ��",-1); 
        return AccessibilityHelper.findNodeInfosByText(nodeInfo, "����ȡ��",-1);
    }
    //�ж��Ƿ��º����
    public boolean isNewLuckyMoney(AccessibilityNodeInfo LuckyMoneyNode,AccessibilityNodeInfo ReceivedLuckyMoneyInfoNode){
    	//�޺��
    	if(LuckyMoneyNode==null)return false;
    	//û����ȡ��Ϣ
    	if(ReceivedLuckyMoneyInfoNode==null)return true;
    	//�жϣ�
    	boolean bNewLuckyMoney=false;
    	Rect outBounds1=new Rect();
    	Rect outBounds2=new Rect();
    	LuckyMoneyNode.getBoundsInScreen(outBounds1);
    	ReceivedLuckyMoneyInfoNode.getBoundsInScreen(outBounds2);
    	if(outBounds2.top>outBounds1.top)bNewLuckyMoney=false;else bNewLuckyMoney=true;
    	return bNewLuckyMoney;
    }
    //��ȡ����ϵ�������Ϣ
	public String getLuckyMoneyTxt(AccessibilityNodeInfo LuckyMoneyNode){
        //AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (LuckyMoneyNode == null)return null;
        AccessibilityNodeInfo parent=LuckyMoneyNode.getParent();
        if(parent!=null){
        		if(parent.getContentDescription()!=null){
        			String desc=parent.getContentDescription().toString();
        			int i=desc.indexOf("������鿴����");
        			if(i!=-1){
        				String sSay=desc.substring(0,i);
        				return  sSay;
        			}//if(i!=-1){
        		}//if(parent.getContentDescription()!=null){
        	//return LuckyMoneySayNode.getText().toString();
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
    /** �Ƿ�Ϊ�����˵�*/
    public boolean isPopmenuUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        String txt="����";
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="ɾ��";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="����";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
       return true;
    }
    /** �Ƿ�ΪȺ����*/
    public boolean isMemberChatUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        String desc="Ⱥ���Ͽ�";
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
 
    /*�����ı�*/
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
    		////ճ����������  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	return false;
    }      
}

