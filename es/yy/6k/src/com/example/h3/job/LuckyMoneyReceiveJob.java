/**
 * 
 */
package com.example.h3.job;

import com.example.h3.Config;

import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class LuckyMoneyReceiveJob {
	private static LuckyMoneyReceiveJob current;
	private Context context;
	
	private static final String WECHAT_LUCKY_SEND="����һ�������������";//
	private static final String DIGITAL="0123456789";//
	private String[] mReceiveInfo={"","","","",""};//�����Ϣ��
	private int mIntInfo=0;//��Ϣ����
	private boolean bClicked=false;//�� ��������
	private boolean bNeedClick=false;//�Ƿ������
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	public int mLuckyMoneyType=0;//�������Ѳ���������������װ���
	//private static final int 
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
	}
    public static synchronized LuckyMoneyReceiveJob getLuckyMoneyReceiveJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyReceiveJob(context);
        }
        return current;
        
    }
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void recycle(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			if(bNeedClick){//�����
				if(info.isClickable()){
					if(!bClicked)info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					bClicked=true;
				}
			}else{//����Ϣ��
				if(info.getText()==null)return;
				if(info.getText().toString().equals(WECHAT_LUCKY_SEND)){
					mLuckyMoneyType=Config.TYPE_LUCKYMONEY_THUNDER;
					bRecycled=true;
					return;
				}	
			}//if(!bNeedClick){
			mIntInfo=mIntInfo+1;
			if(Config.DEBUG)Log.d(Config.TAG, "("+mIntInfo+")"+info.getClassName()+"------>"+info.getText());
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycle(info.getChild(i));
				}
			}
		}
	}
    public int CheckLuckyMoneyType(String LuckyMoneySend,String LuckyMoneySay){
    	if(LuckyMoneySend.equals(WECHAT_LUCKY_SEND)){
    		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
    		if(DIGITAL.indexOf(LuckyMoneySayTail)==-1)
    			return Config.TYPE_LUCKYMONEY_WELL;
    		else
    			return Config.TYPE_LUCKYMONEY_THUNDER;
    	}else{
    		return Config.TYPE_LUCKYMONEY_NONE;
    	}
    }
    public void OpenLuckyMoney(AccessibilityNodeInfo info){
    	bNeedClick=true;
    	bClicked=false;
    	bRecycled=false;
    	mIntInfo=0;
    	recycle(info);
    	return ;
    }
    public int IsLuckyMoneyReceive(AccessibilityNodeInfo info){
    	bNeedClick=false;
    	mIntInfo=0;
    	bRecycled=false;
    	mLuckyMoneyType=Config.TYPE_LUCKYMONEY_NONE;//�����ˣ�
    	recycle(info);
    	return mLuckyMoneyType;
    }
}
