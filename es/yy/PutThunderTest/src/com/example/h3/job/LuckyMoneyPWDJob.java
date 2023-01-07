/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.example.h3.Config;
import com.example.h3.QiangHongBaoService;

import android.content.Context;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class LuckyMoneyPWDJob {
	private static LuckyMoneyPWDJob current;
	public static String TAG = "byc001";	
	//public QiangHongBaoService service;
	public Context context;
	//PWD面板坐标值：
	private int mCellLen=0;
	private int mCellHigh=0;
	private int mPadLeft=0;
	private int mPadTop=0;
	
	private LuckyMoneyPWDJob(Context context) {
		this.context = context;
	}
    public static synchronized LuckyMoneyPWDJob getLuckyMoneyPWDJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPWDJob(context);
        }
        current.context=context;
        return current;
        
    }
    //输入密码：
    public boolean putPWD(){
    		//recycle(rootNode);
    		String sNum="";
    		String sPWD=Config.sPWD;
    		String sOrder="";
    		Log.i(TAG,"PWD--------------------------------------------->");
    		//AccessibilityHelper.recycle(rootNode);
    		AccessibilityHelper.Sleep(500);
    		for(int i=0;i<6;i++){
    			sNum=sPWD.substring(i,i+1);
    			sOrder=getNumOrder2(sNum);
    			if(sOrder==null)return false;
    			//sOrder="input tap 120 764";
    			AccessibilityHelper.Sleep(500);
    			Log.i(TAG,sOrder);
    			RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
    		}
    		return true;
    }
    //获取
    private String getNumOrder2(String sNum){
    	if(mCellLen==0){
    		mCellLen=Config.screenWidth/3;
    		mCellHigh=mCellLen/2;
    		mPadLeft=0;
    		mPadTop=Config.screenHeight-4*mCellHigh;
    	}
    	int iNum=Integer.parseInt(sNum);
    	String sOrder=getNumPosFromPad(iNum);
    	return sOrder;
    }
    //获取
    private String getNumOrder(AccessibilityNodeInfo rootNode,String sNum){
    	if(mCellLen==0){
    		AccessibilityNodeInfo ParentNode=AccessibilityHelper.findNodeInfosById(rootNode,"com.tencent.mm:id/a0c",-1);
    		Log.i(TAG,"ParentNode----------------------------------------->");
    		if(ParentNode==null){
    			Log.i(TAG,"ParentNode  IS NULL.");
    			return null;
    		}
    		Rect outBounds=new Rect();
    		ParentNode.getBoundsInScreen(outBounds);
    		int x=outBounds.left;
    		int y=outBounds.top;
    		int b=outBounds.bottom;
    		int r=outBounds.right;
    		mCellLen=(r-x)/3;
    		mCellHigh=(b-y)/4;
    		mPadLeft=x;
    		mPadTop=y;
    	}

    	int iNum=Integer.parseInt(sNum);
    	String sOrder=getNumPosFromPad(iNum);
    	return sOrder;
    }
    //计算密码面板数字坐标：
    private String getNumPosFromPad(int iNum){
    	int x=0;
    	int y=0;

    	switch(iNum){
    	case 1:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 2:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 3:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 4:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 5:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 6:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 7:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 8:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 9:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 0:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+3*mCellHigh+mCellHigh/2;
    		break;
    	}
    	return "input tap "+String.valueOf(x)+" "+String.valueOf(y);
    }
    
    /*
    //输入密码：
    public boolean putPWD(AccessibilityEvent event){

    		AccessibilityNodeInfo rootNode = event.getSource();     
    		if (rootNode == null) {return false;}
    		//recycle(rootNode);
    		String sNum="";
    		String sPWD=Config.sPWD;
    		String sOrder="";
    		for(int i=0;i<6;i++){
    			sNum=sPWD.substring(i,i+1);
    			sOrder=getNumOrder(rootNode,sNum);
    			if(sOrder==null)return false;
    			//sOrder="input tap 120 764";
    			RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
    		}
    		return true;
    }
     */
}
