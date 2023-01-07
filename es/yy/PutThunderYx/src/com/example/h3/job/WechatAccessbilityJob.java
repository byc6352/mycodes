/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.PutThunderYx.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
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
	private SpeechUtil speeker ;
	private FloatingWindow fw;//显示浮动窗口
	private boolean bComp=false;
	private String mCurrentUI="";
	private LuckyMoneyPWDJob mPWDJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoneyPrepareJob mPrepareJob;
	private VolumeChangeListen mVolumeChange;
	private LuckyMoneyReceiveJob mReceiveJob;
	private LuckyMoneyDetailJob mDetailJob;
	AccessibilityNodeInfo mRootNode;

	//----------------------------------------------------------------------------------------
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        //注意context的变化：
        mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        speeker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
        mVolumeChange.onVolumeChangeListener();
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
    
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	
    	final int eventType = event.getEventType();
    	String sShow="";

		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//获取窗口名称：
			mCurrentUI=event.getClassName().toString();
			Log.i(TAG, mCurrentUI);
			//+++++++++++++++++++++++++++++++++1。是发包窗体：++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//埋雷完成：
				//Config.JobState=Config.STATE_INPUT_CLOSING;//测试
				if(Config.JobState==Config.STATE_INPUT_CLOSING){
					if(mPWDJob.bSuccess){
						sShow="埋雷成功完成！";
						mPWDJob.bSuccess=false;
					}else{
						sShow="埋雷完成！";
					}
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					if(Config.bRobTail){//进入扫尾状态
					mRootNode=event.getSource();
					if(mRootNode==null){Config.JobState=Config.STATE_NONE;return;}
					if(mLauncherJob.getMyNode(mRootNode)!=null){
						Config.JobState=Config.STATE_INPUT_ROB_TAIL;
						Config.robTailAction=Config.ACTION_LOOK;
						//AccessibilityHelper.performClick(mLauncherJob.mMyNode);
						sShow="正在进行扫尾操作..,按音量加可关闭扫尾操作。";
						Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
						speeker.speak(sShow);
					}
					}
					
				}
				if(Config.JobState==Config.STATE_INPUT_ROB_TAIL){//进入扫尾状态;
					if(Config.robTailAction==Config.ACTION_CLOSE){//退出扫尾状态；
						Config.JobState=Config.STATE_NONE;
						return;
					}
					AccessibilityHelper.performClick(mLauncherJob.mMyNode);//进入抢包界面；

				}
				//Config.JobState=Config.STATE_NONE;
				//2。显示浮动窗体（点击加号）：
				fw.ShowFloatingWindow();
				//3。点击红包按钮：
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4。关闭悬浮按钮：
				fw.DestroyFloatingWindow();
				//5。不是发包状态则退出：
				if(Config.JobState==Config.STATE_NONE)return;
			}
			//6。是发包状态：
			//+++++++++++++++++++++++++++++++++输入文本：++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3输入文本：
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
				
			}
			//+++++++++++++++++++++++++++++输入密码++++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)||mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLET_UI)){
				//2.4输入密码：
				if(Config.JobState!=Config.STATE_INPUT_PWD)return;
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
				//AccessibilityHelper.recycle(rootNode);
				mPWDJob.putPWD(mRootNode);
				Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
			}
			//WINDOW_LUCKYMONEY_CHATROOM_UI
			//+++++++++++++++++++++++++++++群聊信息+++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_CHATROOM_UI)){
				if(Config.JobState!=Config.STATE_INPUT_GROUP)return;
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
				AccessibilityHelper.recycle2(mRootNode);
			}
			//+++++++++++++++++++++++++++++查看手气预备信息+++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
				if(Config.JobState!=Config.STATE_INPUT_ROB_TAIL)return;
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
		        if(Config.robTailAction==Config.ACTION_LOOK){
		        	if(mReceiveJob.getLookLuckyNode(mRootNode)!=null){
		        		AccessibilityHelper.performClick(mReceiveJob.mLookLuckyNode);
		        	}//if(mReceiveJob.getLookLuckyNode(mRootNode)!=null){
		        }// if(Config.robTailAction==Config.ACTION_LOOK){
		        if(Config.robTailAction==Config.ACTION_CLICK){
		        	mReceiveJob.OpenLuckyMoney(mRootNode);
		        	//Config.JobState=Config.STATE_NONE;
		        }
				if(Config.robTailAction==Config.ACTION_CLOSE){//退出扫尾状态；
					Config.JobState=Config.STATE_NONE;
					fw.ShowFloatingWindow();
					return;
				}
			}
			//+++++++++++++++++++++++++++++查看手气详细信息+++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				if(Config.JobState!=Config.STATE_INPUT_ROB_TAIL)return;
				if(Config.robTailAction==Config.ACTION_CLOSE){//退出扫尾状态；
					Config.JobState=Config.STATE_NONE;
					return;
				}
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
		        //AccessibilityHelper.recycle2(mRootNode);
		        mDetailJob.getRobLuckyMoney(mRootNode);
		        AccessibilityHelper.performBack(service);
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
			

    }
    /*
	    public boolean distributePutThunder(){
			boolean bWindow=distributeClickJiaJob();
			if(!bWindow){
				String sShow="请进入要埋雷的红包群！才能开始埋雷。";
				Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
				speeker.speak(sShow);
				fw.DestroyFloatingWindow();
				return false;
			}else{
				Config.JobState=Config.STATE_INPUT_TEXT;//进入输入文本状态；
				return true;
			}
	    }
	    */
   
    //点击加号：
    public boolean distributePutThunder() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        //if(!mLauncherJob.isMemberChatUi(rootNode))return false;
        fw.DestroyFloatingWindow();
        String say="";
        String groupName=mLauncherJob.getGroupName(rootNode);
        if(groupName.equals("")){
        	say="请进入要埋雷的红包群！才能开始埋雷。";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
        	return false;//不是群聊
        }
        String groupNames=getConfig().getGroupName();//取已保存的群聊名称：
        Log.i(TAG, groupName);
        if(groupNames.indexOf(groupName)!=-1){//没有保存过：
        	mLauncherJob.ClickGroup(rootNode);//点击群聊，
        	Config.JobState=Config.STATE_INPUT_GROUP;//进入输入群聊状态；
        	say="正在准备埋雷数据....请稍候...";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
			return true;
        }
        //点击+号；
        if(!mLauncherJob.ClickJia(rootNode)){
        	say="请进入要埋雷的红包群！才能开始埋雷。";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
        	return false;//不是群聊
        }
        //点击红包按钮：
        if(!mLauncherJob.ClickLuckyMoney(rootNode)){
        	say="请进入要埋雷的红包群！才能开始埋雷。";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
        	return false;//不是群聊
        }
        Config.JobState=Config.STATE_INPUT_TEXT;
        return true;
    }


}
