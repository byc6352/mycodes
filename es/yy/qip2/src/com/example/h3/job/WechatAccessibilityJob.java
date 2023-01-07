/**
 * 
 */
package com.example.h3.job;


import com.example.h3.Config;
import accessibility.QiangHongBaoService;
import notification.IStatusBarNotification;
import notification.NotifyHelper;

import util.SpeechUtil;

import accessibility.BaseAccessibilityJob;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;

import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class WechatAccessibilityJob extends BaseAccessibilityJob  {
	
	private static WechatAccessibilityJob current;
	//-------------------------------拆包延时---------------------------------------------
	
	private SpeechUtil speaker ;
	private String mPackageName;
	private int mCount=0;
	
	public WechatAccessibilityJob(){
		super(new String[]{Config.WECHAT_PACKAGENAME});
	}
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        EventStart();
        speaker=SpeechUtil.getSpeechUtil(context);
        mCount=0;
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
   
    //----
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	super.onReceiveJob(event);

    	if(!mIsEventWorking)return;
    	if(!mIsTargetPackageName)return;
    	
    	final int eventType = event.getEventType();
    	if(event.getClassName()==null)return;
    	String sClassName=event.getClassName().toString();    	
    

    	String say="";
    	//++++++++++++++++++++++++++++++++++++窗体改变+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mPackageName=event.getPackageName().toString();
			//if(mPackageName.equals(Config.WECHAT_PACKAGENAME)||mPackageName.equals(Config.QQ_PACKAGENAME)){
				
			//}else{
			mCount=mCount+1;
			if(mCount>10)return;
				say="正在读取棋牌数据...";
				speaker.speak(say);
				Toast.makeText(context, say, Toast.LENGTH_LONG).show();
				if(!Config.bReg){
					say="您是试用版用户！授权后才能透视！";
					speaker.speak(say);
					Toast.makeText(context, say, Toast.LENGTH_SHORT).show();
				}
			//}
			//Log.d(TAG, "窗口改变 ---->" + sClassName);
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//++++++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			//Log.d(TAG, "内容改变---->" + sClassName);
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)

    public void onNotificationPosted(IStatusBarNotification sbn) {
        Notification nf = sbn.getNotification();
        String text = String.valueOf(sbn.getNotification().tickerText);
        notificationEvent(text, nf);
    }
    /** 通知栏事件*/
    private void notificationEvent(String ticker, Notification nf) {
        String text = ticker;
        int index = text.indexOf(":");
        if(index != -1) {
            text = text.substring(index + 1);
        }
        text = text.trim();
        //transferAccounts.notificationEvent(ticker, nf);
        //if(text.contains(TransferAccounts.WX_TRANSFER_ACCOUNTS_ORDER)) { //红包消息
        //    newHongBaoNotification(nf);
        //}
    }

    /**打开通知栏消息*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void newHongBaoNotification(Notification notification) {
    	//TransferAccounts.mWorking = true;
        //以下是精华，将微信的通知栏消息打开
        PendingIntent pendingIntent = notification.contentIntent;
        boolean lock = NotifyHelper.isLockScreen(getContext());

        if(!lock) {
            NotifyHelper.send(pendingIntent);
        } else {
            //NotifyHelper.showNotify(getContext(), String.valueOf(notification.tickerText), pendingIntent);
        }

        if(lock) {
           // NotifyHelper.playEffect(getContext(), getConfig());
        }
    }

}

