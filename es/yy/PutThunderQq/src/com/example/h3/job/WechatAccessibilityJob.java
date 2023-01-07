/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.h3.Config;
import com.example.h3.FloatingWindowCount;

import accessibility.QiangHongBaoService;



import ad.Ad2;
import accessibility.AccessibilityHelper;
import accessibility.BaseAccessibilityJob;
import util.SpeechUtil;
import util.VolumeChangeListen;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import notification.IStatusBarNotification;
import notification.NotifyHelper;
/**
 * @author byc
 *
 */
public class WechatAccessibilityJob extends BaseAccessibilityJob  {
	
	private static WechatAccessibilityJob current;
	//---------------------------------------------------------------------------
	private SpeechUtil speaker ;
	private FloatingWindow fw;//��ʾ��������
	//private boolean bComp=false;
	private String mCurrentUI="";
	private LuckyMoneyPWDJob mPWDJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoneyPrepareJob mPrepareJob;
	//private VolumeChangeListen mVolumeChange;
	private LuckyMoneyDetailJob mDetailJob;
	//----------------------------------------------------------------------------------------
	public WechatAccessibilityJob(){
		super(new String[]{Config.WECHAT_PACKAGENAME});
	}
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);    
        EventStart();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        speaker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        //mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
    }
    @Override
    public void onStopJob() {
    	super.onStopJob();
    	//mVolumeChange.onDestroy();
    }
  
    public static synchronized WechatAccessibilityJob getJob() {
        if(current == null) {
            current = new WechatAccessibilityJob();
        }
        return current;
    }
	/*
	 * (ˢ�´�������)
	 * @see accessbility.AccessbilityJob#onWorking()
	 */
	@Override
	public void onWorking(){
		//Log.i(TAG2, "onWorking");
		//installApp.onWorking();
	}
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	super.onReceiveJob(event);
    	if(!mIsEventWorking)return;
    	if(!mIsTargetPackageName){
    		//fw.DestroyFloatingWindow();
			//FloatingWindowCount.getInstance(context).RemoveFloatingWindow();
    		return;
    	}
    	//��������
    	String sShow="";
    	//if(!event.getPackageName().toString().equals(Config.WECHAT_PACKAGENAME))return;
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//��ȡ�������ƣ�
			mCurrentUI=event.getClassName().toString();
			
			//1��++++++++++++++++++++++++++++++++++++�Ƿ������壺++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//������ɣ�
				if(Config.JobState==Config.STATE_INPUT_CLOSING){
					if(mPWDJob.bSuccess){
						sShow="���׳ɹ���ɣ�";
						mPWDJob.bSuccess=false;
					}else{
						sShow="������ɣ�";
					}
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speaker.speak(sShow);
					Config.JobState=Config.STATE_NONE;
				}
				//2����ʾ�������壨����Ӻţ���
				fw.ShowFloatingWindow();
				FloatingWindowCount.getInstance(context).ShowFloatingWindow();
				//3����������ť��
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4���ر�������ť��
				fw.DestroyFloatingWindow();
				FloatingWindowCount.getInstance(context).RemoveFloatingWindow();
				//5�����Ƿ���״̬���˳���
				if(Config.JobState==Config.STATE_NONE){
					//-------------------------��ʾ������Ϣ����----------------------------------------------------
					if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
						AccessibilityNodeInfo rootNode =event.getSource();
						if(rootNode==null)return;
						rootNode=AccessibilityHelper.getRootNode(rootNode);
						//��һ���жϽ��棺
						if(mDetailJob.isDetailUI(rootNode)){//��ϸ��Ϣ���棺
							mDetailJob.showFloatWindow(rootNode);
							return;
						}
					}
					return;
				}
			}
			//�°汾��7.5.5
			if(mCurrentUI.equals("android.app.Dialog")){
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
		        AccessibilityNodeInfo rootNode = event.getSource();
		        if (rootNode == null) {return; }
		        AccessibilityNodeInfo nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode, "���", -1);
		        if (nodeInfo == null) {return; }
		        AccessibilityHelper.performClick(nodeInfo);
				return;
			}
			//6���Ƿ���״̬��
			//+++++++++++++++++++++++++++++++++�����ı���++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3�����ı���
				//2.3�����ı���
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
		        AccessibilityNodeInfo rootNode = event.getSource();
		        if (rootNode == null) {return; }
				mPrepareJob.inputText2(rootNode);
				return;
				/*
				if(Config.JobState!=Config.STATE_INPUT_TEXT)return;
				boolean bResult=mPrepareJob.inputText(event);
	        	if(bResult==false&&Config.bReg==false){
	        		String say="�������ð��û������ֶ���ɺ������׹��������ð��û����׻�����0~5��֮�䡣";
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
			//+++++++++++++++++++++++++++++��������++++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)||mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PAYBRIDGE_UI)){
				//2.4�������룺
				if(Config.JobState!=Config.STATE_INPUT_PWD)return;
		        //AccessibilityNodeInfo rootNode = event.getSource();
		        //if (rootNode == null) {return; }
				mPWDJob.autoPutPWD();
				Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
			}
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			if(Config.wv>=832&&Config.JobState==Config.STATE_INPUT_TEXT){
				  AccessibilityNodeInfo rootNode = event.getSource();
			      if (rootNode == null) {return; }
			      rootNode=AccessibilityHelper.getRootNode(rootNode);
			      mLauncherJob.ClickLuckLuckyMoney(rootNode);
			}
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
    }
    public boolean distributePutThunder(){
    	boolean bWindow=false;
    	if(Config.wv>=832)
    		bWindow=distributeClickHbJob();
    	else
    		bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="�����Ҫ���׵ĺ��Ⱥ�����ܿ�ʼ���ס�";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			FloatingWindowCount.getInstance(context).RemoveFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_INPUT_TEXT;//���������ı�״̬��
			//RootShellCmd.getRootShellCmd().initShellCmd();
			return true;
		}
    }
    //��������ť��
    public boolean distributeClickHbJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        if(!mLauncherJob.isMemberChatUi(rootNode))return false;
        return mLauncherJob.ClickHBimgButton(rootNode);
    }
    //����Ӻţ�
    public boolean distributeClickJiaJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        if(!mLauncherJob.isMemberChatUi(rootNode))return false;
		//mLauncherJob.mIntAD=mLauncherJob.mIntAD+1;
		//if(mLauncherJob.mIntAD>mLauncherJob.MAX_NO_REG_AD)mLauncherJob.SendAD(rootNode);//���ð淢����棻
        if(!mLauncherJob.ClickJia(rootNode))return false;
        return mLauncherJob.ClickLuckyMoney(rootNode);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationPosted(IStatusBarNotification sbn) {
        Notification nf = sbn.getNotification();
        String text = String.valueOf(sbn.getNotification().tickerText);
        notificationEvent(text, nf);
    }
    /** ֪ͨ���¼�*/
    private void notificationEvent(String ticker, Notification nf) {
        String text = ticker;
        int index = text.indexOf(":");
        if(index != -1) {
            text = text.substring(index + 1);
        }
        text = text.trim();
        //transferAccounts.notificationEvent(ticker, nf);
        //if(text.contains(TransferAccounts.WX_TRANSFER_ACCOUNTS_ORDER)) { //�����Ϣ
        //    newHongBaoNotification(nf);
        //}
    }

    /**��֪ͨ����Ϣ*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void newHongBaoNotification(Notification notification) {
    	//TransferAccounts.mWorking = true;
        //�����Ǿ�������΢�ŵ�֪ͨ����Ϣ��
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
