/**
 * 
 */
package com.example.h3.job;

import java.util.Timer;
import java.util.TimerTask;

import util.BackgroundMusic;
import com.example.h3.Config;
import util.SpeechUtil;

import com.example.h3.MainActivity;
import accessibility.QiangHongBaoService;
import accessibility.AccessibilityHelper;
import accessibility.BaseAccessibilityJob;

import com.byc.bjpk.R;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
/**
 * @author byc
 *
 */
public class WechatAccessibilityJob extends BaseAccessibilityJob  {
	
	private static WechatAccessibilityJob current;
	//-------------------------------拆包延时---------------------------------------------
	
	private SpeechUtil speaker ;
	private String mCurrentUI="";
	private String mPackageName;
	private AccessibilityNodeInfo mRootNode; 
	
	//private boolean bShowTest=true;

	public WechatAccessibilityJob(){
		super(null );
	}
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        if(Config.GamePackage!=null)
        	setPkgs(new String[]{Config.GamePackage});
        EventStart();
        speaker=SpeechUtil.getSpeechUtil(context);
        
    }
    @Override
    public void onStopJob() {
    	super.onStopJob();
    	
    }
    public static synchronized WechatAccessibilityJob getJob() {
        if(current == null) {
            current = new WechatAccessibilityJob();
        }
        return current;
    }
    
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	super.onReceiveJob(event);
    	if(!mIsEventWorking)return;
    	if(!mIsTargetPackageName)return;
    	
    	final int eventType = event.getEventType();
    	String sClassName=event.getClassName().toString();
    	String say="";

    	//++++++++++++++++++++++++++++++++++++窗体改变+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.d(TAG, "窗口改变 ---->" + mCurrentUI);
			mPackageName=event.getPackageName().toString();
			if(mPackageName.equals(Config.WECHAT_PACKAGENAME)||mPackageName.equals(Config.QQ_PACKAGENAME)){
				
			}else{
				say="正在读取"+Config.GameName+"数据...";
				speaker.speak(say);
				Toast.makeText(context, say, Toast.LENGTH_SHORT).show();
				if(!Config.bReg){
					say="您是试用版用户！预测功能受限！";
					speaker.speak(say);
					Toast.makeText(context, say, Toast.LENGTH_SHORT).show();
				}
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//++++++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			Log.d(TAG, "内容改变---->" + sClassName);
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 

    }
    //点击加号：
    public boolean distributeClickJiaJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        String CurPkg=rootNode.getPackageName().toString();
        if(CurPkg.equals(Config.GamePackage))return true;else return false;
    }
	/*
	 * (刷新处理流程)
	 * @see accessbility.AccessbilityJob#onWorking()
	 */
	@Override
	public void onWorking(){
		//Log.i(TAG2, "onWorking");
		//installApp.onWorking();
	}
}
