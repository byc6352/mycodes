/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.example.h3.Config;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class LuckyMoneyReceiveJob {
	private static LuckyMoneyReceiveJob current;
	private Context context;
	private LuckyMoneyLauncherJob LancherJob;
	
	private static final String WECHAT_LUCKY_SEND="发了一个红包，金额随机";//
	private static final String WECHAT_GONG_XI="恭喜发财！大吉大利！";//
	private static final String DIGITAL="0123456789";//
	private String[] mReceiveInfo={"","","","",""};//拆包信息；
	private int mIntInfo=0;//信息数；
	private boolean bClicked=false;//是 否点击过；
	private boolean bNeedClick=false;//是否点击红包
	private boolean bRecycled=false;//是否退出循环
	public int mLuckyMoneyType=0;//红包类别：已拆过，福利包，有雷包；
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
		LancherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
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
			if(bNeedClick){//点击：
				if(info.isClickable()){
					if(!bClicked)info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					bClicked=true;
				}
			}else{//查信息：
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
    //
    public int CheckLuckyMoneyType(String LuckyMoneySend,String LuckyMoneySay){
    	if(LuckyMoneySend.equals(WECHAT_LUCKY_SEND)){
    		Log.i(Config.TAG, LuckyMoneySay);
    		
    		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
    		if(DIGITAL.indexOf(LuckyMoneySayTail)==-1){
    			if(LuckyMoneySay.equals(WECHAT_GONG_XI)){//是恭喜发财！
    				if(LancherJob.bNewLuckyMoney){//新红包;
    					return LancherJob.mLuckyMoneyType;
    				}//if(LancherJob.bNewLuckyMoney){//新红包;
    			}//if(LuckyMoneySay.equals(WECHAT_GONG_XI)){//是恭喜发财！
    			return Config.TYPE_LUCKYMONEY_WELL;
    		}
    		else
    			return Config.TYPE_LUCKYMONEY_THUNDER;
    	}else{
    		return Config.TYPE_LUCKYMONEY_NONE;
    	}
    }
    public boolean OpenLuckyMoney(AccessibilityNodeInfo info){
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    		bNeedClick=true;
    		bClicked=false;
    		bRecycled=false;
    		mIntInfo=0;
    		recycle(info);
    		return true;
    	}
    	return false;
    }
    public int IsLuckyMoneyReceive(AccessibilityNodeInfo info){
    	bNeedClick=false;
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	return mLuckyMoneyType;
    }
}
