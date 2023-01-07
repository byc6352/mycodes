package com.example.h3.job;


import com.example.h3.Config;
import accessibility.AccessibilityHelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;


/*
 * ����ࣻ��ʶ���β�ԣ��������������
 */
public class LuckyMoney {
	
	private static LuckyMoney current;
	private Context context;
	private static String TAG = "byc001";
	private static final String DIGITAL="0123456789";//
	public static final int TYPE_LUCKYMONEY_ME_ROBBED=1;//�Լ����ߵĺ����
	public static final int TYPE_LUCKYMONEY_YOU_ROBBED=2;//�������ߵĺ����
	public static final int TYPE_LUCKYMONEY_NO_ROBBED=0;//δ����ĺ����
	public static final int TYPE_LUCKYMONEY_COMMON=1;//��ͨ�ĺ����
	public static final int TYPE_LUCKYMONEY_PERSONALITY=2;//���Եĺ����
	public static final int TYPE_LUCKYMONEY_THUNDER=11;//�׵ĺ����
	public static final int TYPE_LUCKYMONEY_WELFARE=12;//�����ĺ����
	public static final int TYPE_LUCKYMONEY_PAY_FOR=13;//�⸶�ĺ����
	/*
	 * ����1����ȡ�ĺ�������Ĭ��δ��ȡ���Լ���ȡ�ģ�������ȡ�ģ�˭�����˺����
	 */
	public int WhoRobbed=TYPE_LUCKYMONEY_NO_ROBBED;//δ��ȡ
	/*
	 * ����2�����Ժ��������ͨ�����Ĭ����ͨ�����
	 */
	public int LuckyMoneyType=TYPE_LUCKYMONEY_COMMON;
	/*
	 * ����3���⸶���������������װ���Ĭ���װ�;
	 */
	public int LuckyMoneyDefined=TYPE_LUCKYMONEY_THUNDER;
	/*
	 * ��ʶҪ�����ĺ����
	 */
	public AccessibilityNodeInfo LuckyMoneyNode;
	/*
	 * �ѻ�ȡ�����Ϣ��
	 */
	public AccessibilityNodeInfo RobbedLuckyMoneyInfoNode;
	/*
	 * ���췽��
	 */
	private LuckyMoney(Context context) {
		this.context = context;
		TAG=Config.TAG;
	}
    public static synchronized LuckyMoney getLuckyMoney(Context context) {
        if(current == null) {
            current = new LuckyMoney(context);
        }
        return current;
    }
    /*
     * ��ȡ���һ��δ��ȡ����ͨ���
     */
    public AccessibilityNodeInfo getLastLuckyMoneyNode(AccessibilityNodeInfo rootNode){
    	return AccessibilityHelper.findNodeInfosByText(rootNode, "�����", -1);
    }
    //��ȡ���һ���Ѳ𿪵���ͨ�����
    public AccessibilityNodeInfo getLastLuckyMoneyNoded(AccessibilityNodeInfo rootNode){
    	return AccessibilityHelper.findNodeInfosByText(rootNode, "�Ѳ�", -1);
    }
    //��ȡ���һ�����Ժ����
    public AccessibilityNodeInfo getLastLuckyMoneyNode2(AccessibilityNodeInfo rootNode){
    	return AccessibilityHelper.findNodeInfosByText(rootNode, "QQ������԰�", -1);
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
    //��ȡ����ȡ�����Ϣ
    public AccessibilityNodeInfo getLastReceivedLuckyMoneyInfoNode(AccessibilityNodeInfo nodeInfo){
    	//(nodeInfo,"com.tencent.mm:id/ho",-1)
        if (nodeInfo == null)return null;
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
    //�ж��Ƿ����װ�
    public boolean isLuckyMoneyLei(AccessibilityNodeInfo LuckyMoneyNode){
    	//�޺��
    	if(LuckyMoneyNode==null)return false;
    	//��ȡ�����
    	String sLuckyMoneySay=getLuckyMoneyTxt(LuckyMoneyNode);
    	if(sLuckyMoneySay==null)return false;
    	//�жϣ�
    	if(checkLuckyMoneySayType(sLuckyMoneySay)==TYPE_LUCKYMONEY_THUNDER)
    		return true;
    	else
    		return false;
    }
    public int checkLuckyMoneySayType(String LuckyMoneySay){

    		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
    		if(DIGITAL.indexOf(LuckyMoneySayTail)==-1)
    			return TYPE_LUCKYMONEY_PAY_FOR;
    		else
    			return TYPE_LUCKYMONEY_THUNDER;
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
}