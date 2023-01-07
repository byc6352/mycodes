/**
 * 
 */
package com.example.h3.job;

import java.util.Timer;
import java.util.TimerTask;

import util.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import accessibility.QiangHongBaoService;


import ad.Ad2;
import accessibility.AccessibilityHelper;
import accessibility.BaseAccessibilityJob;
import com.example.h3.FloatingWindowPic;

import util.SpeechUtil;


import com.byc.ClearThunderQq.R;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
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
	
	private static  WechatAccessibilityJob current;
	//-------------------------------�����ʱ---------------------------------------------
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;

	//public int mLuckyMoneyType=0;//�������Ѳ���������������װ���
	private LuckyMoneyReceiveJob mReceiveJob;
	private LuckyMoneyDetailJob mDetailJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
	private LuckyMoney mLuckyMoney;//�������

	public static boolean mWorking=true;
	private FloatingWindow fw=null;
	public WechatAccessibilityJob(){
		super(new String[]{Config.WECHAT_PACKAGENAME});
	}


    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        EventStart();
        mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        mLuckyMoney=LuckyMoney.getLuckyMoney(context);//�������
        
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//���չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_CLICK_LUCKY_MONEY);
        context.registerReceiver(ClickLuckyMoneyReceiver, filter);

        //��������
        fw=FloatingWindow.getFloatingWindow(context);
      
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
    	if(!mIsTargetPackageName)return;
    	
    	int eventType = event.getEventType();
    	if(event.getClassName()==null)return;
    	String sClassName=event.getClassName().toString();
    	if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
    		mCurrentUI=sClassName;
    	}
    	if(!mWorking)return;
    	debug(event);
		//++++++++++++++++++++++++++++++++++++����ı�+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			
			fw.ShowFloatingWindow();
			//-------------------------������Ϣ����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				Config.JobState=Config.STATE_NONE;
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
			//-------------------------��ʾ������Ϣ����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
				if(Config.JobState==Config.STATE_NONE)return;
				AccessibilityNodeInfo rootNode=event.getSource();
				if(rootNode==null)return;
				if(rootNode.getChildCount()==0)return;
				if(Config.wv>=818&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)return;
				processUnpackLuckyMoneyShow(rootNode);
				//Log.i("byc002", "rootNode.getChildCount()="+rootNode.getChildCount());
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 

		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

			if(!Config.bAutoClearThunder)return;
			if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY){
				//-------------------------�򿪺������----------------------------------------------------
				if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
					AccessibilityNodeInfo rootNode=event.getSource();
					if(rootNode==null)return;
					rootNode=AccessibilityHelper.getRootNode(rootNode);
					if(rootNode.getChildCount()==0)return;
					processUnpackLuckyMoneyShow(rootNode);
				}
				return;
			}
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				AccessibilityNodeInfo rootNode=event.getSource();
				if(rootNode==null)return;
				rootNode=AccessibilityHelper.getRootNode(rootNode);
				
				if(mLauncherJob.isMemberChatUi(rootNode)){
					if(clickLuckyMoney3(rootNode)){Config.JobState=Config.STATE_CLICK_LUCKYMONEY;return;}
				}
				//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
    }
    /*
     *
     */
    private void debug(AccessibilityEvent event){
      	if(Config.DEBUG){
        	final int eventType = event.getEventType();
        	if(event.getClassName()==null)return;
        	String sClassName=event.getClassName().toString();
    			Log.i("byc002", "mCurrentUI="+mCurrentUI);
    			if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
    				Log.i("byc002", "eventType=TYPE_WINDOW_STATE_CHANGED");
    			if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
    				Log.i("byc002", "eventType=TYPE_WINDOW_CONTENT_CHANGED");
    			Log.i("byc002", "Config.JobState="+Config.JobState);
    			
    			
    			if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
    				Log.i(TAG, "�¼�--------------------->TYPE_WINDOW_STATE_CHANGED");
    				Log.i(TAG, "����--------------------->"+sClassName);
    			}
    			else{
    				Log.i(TAG, "�¼�--------------------->TYPE_WINDOW_CONTENT_CHANGED");
    				Log.i(TAG, "ClassName--------------------->"+sClassName);
    				AccessibilityNodeInfo rootNode=event.getSource();
    				if(rootNode==null)return;
    				Log.i(TAG, "getSource--------------------->"+rootNode.getText());
    			}
    			Log.i(TAG, "����--------------------->"+event.getPackageName());
    				
    			AccessibilityNodeInfo rootNode=event.getSource();
    			if(rootNode==null)return;
    			rootNode=AccessibilityHelper.getRootNode(rootNode);
    			AccessibilityHelper.recycle(rootNode);
    		}
    }
    /*
     * ���������ʾ����
     */
    private void processUnpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	if(rootNode==null)return;
		if(rootNode.getChildCount()==0)return;
		//��һ���жϽ��棺
		if(mDetailJob.isDetailUI(rootNode)){//��ϸ��Ϣ���棺
			Config.JobState=Config.STATE_NONE;
		}else{
			//-------------------------------------------------------
				if(mReceiveJob.isNoLuckyMoneyUI(rootNode)){//�������
					mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_YOU_ROBBED;
					String say="������ˣ�û������";//
		    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
		    		speaker.speak(say);	
					//Config.JobState=Config.STATE_NONE;
				}else{
					mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_ME_ROBBED;//���������;
					//��ʾ��Ϣ��
					mReceiveJob.unpackLuckyMoneyShow(rootNode);
				}
				Config.JobState=Config.STATE_NONE;
				if(Config.bAutoReturn)
					AccessibilityHelper.performBack(service);
		}//if(mDetailJob.isDetailUI(mRootNode)){//��ϸ��Ϣ���棺
    }
    //������ˣ������
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean clickLuckyMoney3(AccessibilityNodeInfo rootNode){

    	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode(rootNode);//ȡ���һ�������
    		mLuckyMoney.RobbedLuckyMoneyInfoNode=mLuckyMoney.getLastReceivedLuckyMoneyInfoNode(rootNode);
    		if(mLuckyMoney.isNewLuckyMoney(mLuckyMoney.LuckyMoneyNode, mLuckyMoney.RobbedLuckyMoneyInfoNode)){
    			if(mLuckyMoney.isLuckyMoneyLei(mLuckyMoney.LuckyMoneyNode)){
    					//��������״̬��
    					Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
    					mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_NO_ROBBED;
    					mLuckyMoney.LuckyMoneyType=LuckyMoney.TYPE_LUCKYMONEY_PERSONALITY;
    					mLuckyMoney.LuckyMoneyDefined=LuckyMoney.TYPE_LUCKYMONEY_THUNDER;
    					ClickLuckyMoneyDelay();//�����ʱ��ʾ��
    					return true;
    				}
    			}
    	}
        return false;
    }
  
    //�����ʱ��
    private void ClickLuckyMoneyDelay() {
        //7.���ű������֣�
        //mBackgroundMusic=BackgroundMusic.getInstance(context);
        //mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
    	speaker.speak("���ڽ������ݼ��㣺");
    	mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=Config.getConfig(context).getWechatOpenDelayTime();
    	fwp.mSendMsg=Config.ACTION_CLICK_LUCKY_MONEY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    	fwp.StartSwitchPics();
    }
  
	private BroadcastReceiver ClickLuckyMoneyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_CLICK_LUCKY_MONEY.equals(action)) {
            	//��������
            	mBackgroundMusic.stopBackgroundMusic();
            	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
            	if(mLuckyMoney.ClickLuckyMoney(mLuckyMoney.LuckyMoneyNode))
            		Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
            	}
            }
        }
    };
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
       // transferAccounts.notificationEvent(ticker, nf);
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
