/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.PutThunderQq2.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;

import com.example.h3.util.SpeechUtil;
import com.example.h3.util.VolumeChangeListen;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
/**
 * @author byc
 *
 */
public class WechatAccessbilityJob extends BaseAccessbilityJob  {
	
	private static WechatAccessbilityJob current;
	//---------------------------------------------------------------------------
	private SpeechUtil speaker ;
	private FloatingWindow fw;//显示浮动窗口
	//private boolean bComp=false;
	private String mCurrentUI="";
	private LuckyMoneyPWDJob mPWDJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoneyPrepareJob mPrepareJob;
	private LuckyMoneyDetailJob mDetailJob;
	private VolumeChangeListen mVolumeChange;
	private boolean bDel=false;//删除广告语
	private AccessibilityNodeInfo mRootNode; 

	//----------------------------------------------------------------------------------------
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        //注意context的变化：
        //mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        //mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        speaker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
        //mVolumeChange.onVolumeChangeListener();
        //广告 
        Ad.init();
    }
    @Override
    public void onStopJob() {
    	mVolumeChange.onDestroy();
    	//mVolumeChange.
    }
    public static synchronized WechatAccessbilityJob getJob() {
        if(current == null) {
            current = new WechatAccessbilityJob();
        }
        return current;
    }
    
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	
    	final int eventType = event.getEventType();
    	String sShow="";
    	//广告：
    	Ad.getAd(context, event.getPackageName().toString()).onReceiveJob(event);
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//获取窗口名称：
			mCurrentUI=event.getClassName().toString();
			//Log.i(TAG, "当前窗口----------------》"+mCurrentUI);
			//Toast.makeText(context, mCurrentUI, Toast.LENGTH_LONG).show();
			//mRootNode=event.getSource();
			//if(mRootNode==null)return;
			//AccessibilityHelper.recycle(mRootNode);
		
			//1。++++++++++++++++++++++++++++++++++++是发包窗体：++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//埋雷完成：
				if(Config.JobState==Config.STATE_INPUT_CLOSING){
					if(mPWDJob.bSuccess){
						sShow="埋雷成功完成！";
						mPWDJob.bSuccess=false;
					}else{
						sShow="埋雷完成！";
					}
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speaker.speak(sShow);
					Config.JobState=Config.STATE_NONE;
				}
				//2。显示浮动窗体（点击加号）：
				fw.ShowFloatingWindow();
				//3。点击红包按钮：
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4。关闭悬浮按钮：
				fw.DestroyFloatingWindow();
				//5。不是发包状态则退出：
				//if(Config.JobState==Config.STATE_NONE)return;
			}
			//6。是发包状态：
			//+++++++++++++++++++++++++++++++++输入文本：++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3输入文本：
				//2.3输入文本：
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
		        AccessibilityNodeInfo rootNode = event.getSource();
		        if (rootNode == null) {return; }
				mPrepareJob.inputText2(rootNode);
				return;
				/*
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
				boolean bResult=mPrepareJob.inputText(event);
	        	if(bResult==false&&Config.bReg==false){
	        		String say="您是试用版用户！请手动完成后续埋雷工作！试用版用户出雷机率在0~5个之间。";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speeker.speak(say);
	        	}
	        	if(bResult)
	        		Config.JobState=Config.STATE_INPUT_PWD;
	        	else
	        		Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
				*/
				
			}
			//+++++++++++++++++++++++++++++输入密码++++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)){
				//2.4输入密码：
				if(Config.JobState==Config.STATE_INPUT_PWD){
		        AccessibilityNodeInfo rootNode = event.getSource();
		        if (rootNode == null) {return; }
				//AccessibilityHelper.recycle(rootNode);
				mPWDJob.putPWD(rootNode);
				Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
				}
			}
			//-------------------------显示领红包信息界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//AccessibilityHelper.recycle(mRootNode);
				//进一步判断界面：
				if(mDetailJob.isDetailUI(mRootNode)){//详细信息界面：
					mDetailJob.unpackLuckyMoneyShow(mRootNode);
					return;
				}
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//+++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {


		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
    }
    public boolean distributePutThunder(){
		boolean bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="请进入要埋雷的红包群！才能开始埋雷。";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_INPUT_TEXT;//进入输入文本状态；
			RootShellCmd.getRootShellCmd().initShellCmd();
			return true;
		}
    }
   
    //点击加号：
    public boolean distributeClickJiaJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        if(!mLauncherJob.isMemberChatUi(rootNode))return false;
		//mLauncherJob.mIntAD=mLauncherJob.mIntAD+1;
		//if(mLauncherJob.mIntAD>mLauncherJob.MAX_NO_REG_AD)mLauncherJob.SendAD(rootNode);//试用版发布广告；
        if(!mLauncherJob.ClickJia(rootNode))return false;
        return mLauncherJob.ClickLuckyMoney(rootNode);
    }


}
