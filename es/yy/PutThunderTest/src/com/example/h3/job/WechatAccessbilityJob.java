/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.PutThunderTest.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.FloatingWindowPic;
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
	private LuckyMoneyDetailJob mDetailJob;

	//----------------------------------------------------------------------------------------
	private FloatingWindowPic fws1;//显示图片浮动窗口
	private FloatingWindowPic fws2;//显示图片浮动窗口
	private FloatingWindowPic fws3;//显示图片浮动窗口
	private FloatingWindowPic fws4;//显示图片浮动窗口
	private FloatingWindowPic fws5;//显示图片浮动窗口
	
	private FloatingWindowPic fws6;//显示图片浮动窗口
	private FloatingWindowPic fws7;//显示图片浮动窗口
	private FloatingWindowPic fws8;//显示图片浮动窗口
	private FloatingWindowPic fws9;//显示图片浮动窗口
	private FloatingWindowPic fws10;//显示图片浮动窗口
	
	private FloatingWindowPic fws11;//显示图片浮动窗口
	private FloatingWindowPic fws12;//显示图片浮动窗口
	private FloatingWindowPic fws13;//显示图片浮动窗口
	private FloatingWindowPic fws14;//显示图片浮动窗口
	private FloatingWindowPic fws15;//显示图片浮动窗口
	private int i=1;

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        //注意context的变化：
        //mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        speeker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
        //mVolumeChange.onVolumeChangeListener();
        

        
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
    	//String sClassName=event.getClassName().toString();
		//Log.d(TAG, "事件---->" + event);
		if(Config.DEBUG){
			//String ui=event.getClassName().toString();
			//Log.d(TAG, "事件---->" + ui);
			//if(ui.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")){
				//UnpackLuckyMoney() ;
			//}
		}
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//获取窗口名称：
			mCurrentUI=event.getClassName().toString();
			//图片替换：
			mDetailJob.ReplacePics(event);
			//++++++++++++++++++++++++++++++预备窗体+++++++++++++++++++++++++++++++++++++++++++++++++
			//1。是发包窗体：
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//埋雷完成：
				if(bComp){
					sShow="埋雷完成！";
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					bComp=false;
				}
				//2。显示浮动窗体（点击加号）：
				fw.ShowFloatingWindow();
				//3。点击红包按钮：
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4。关闭悬浮按钮：
				fw.DestroyFloatingWindow();
				//5。不是发包状态则退出：
				//if(Config.JobState!=Config.STATE_SENDING_LUCKYMONEY)return;
			}
			//6。是发包状态：
			//++++++++++++++++++++++++++++++输入文本：+++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3输入文本：
				if(Config.currentapiVersion>=Build.VERSION_CODES.LOLLIPOP_MR1){
					if(!mPrepareJob.InputText2(event)){Config.JobState=Config.STATE_NONE;return;}
				}else{
					if(!mPrepareJob.InputText(event)){Config.JobState=Config.STATE_NONE;return;}
				}
				return;
				
			}
			//+++++++++++++++++++++++++++++输入密码++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)){
				//2.4输入密码：
				bComp=mPWDJob.putPWD();
				Config.JobState=Config.STATE_NONE;
				return;
			}
			//++++++++++++++++++++++++++++显示详细信息++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				//AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
				//AccessibilityHelper.recycle(rootNode);
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//++++++++++++++++++++++++++++内容变化 ++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			return;
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//}//if(WECHAT_PACKAGENAME.equals(pkn))
    }
    
   
    
   
    //点击加号：
    public boolean distributeClickJiaJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        if(!mLauncherJob.isMemberChatUi(rootNode))return false;
        if(!mLauncherJob.ClickJia(rootNode))return false;
        return mLauncherJob.ClickLuckyMoney(rootNode);
    }


}
