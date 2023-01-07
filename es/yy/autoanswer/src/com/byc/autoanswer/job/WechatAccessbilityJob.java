/**
 * 
 */
package com.byc.autoanswer.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.autoanswer.BackgroundMusic;
import com.byc.autoanswer.Config;
import com.byc.autoanswer.IStatusBarNotification;
import com.byc.autoanswer.MainActivity;
import com.byc.autoanswer.QiangHongBaoService;
import com.byc.autoanswer.VersionParam;
import com.byc.autoanswer.util.AccessibilityHelper;
import com.byc.autoanswer.util.FloatingWindowPic;
import com.byc.autoanswer.util.NotifyHelper;

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
/**
 * @author byc
 *
 */
public class WechatAccessbilityJob extends BaseAccessbilityJob  {
	
	private static WechatAccessbilityJob current;
	//-------------------------------�����ʱ---------------------------------------------

	private SpeechUtil speeker ;

	private LuckyMoneyReceiveJob mReceiveJob;
	private LuckyMoneyDetailJob mDetailJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoney mLuckyMoney;//�������
	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
	private boolean bDel=false;//ɾ�������
    /** �����Ϣ�Ĺؼ���*/
    private static final String HONGBAO_TEXT_KEY = "[΢�ź��]";


    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mLuckyMoney=LuckyMoney.getLuckyMoney(context);//�������
        speeker=SpeechUtil.getSpeechUtil(context);

    }
    @Override
    public void onStopJob() {

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
    	String sClassName=event.getClassName().toString();
		//++++++++++++++++++++++++++++++++++++����ı�+++++++++++++++++++++++++++++++++++++++++++++++++
    	//Log.i(TAG, sClassName);
		//mRootNode=event.getSource();
		//if(mRootNode==null)return;
		//AccessibilityHelper.printParentInfo(mRootNode);
		//mRootNode=AccessibilityHelper.getRootNode(mRootNode);
		//AccessibilityHelper.recycle(mRootNode);
    	 //֪ͨ���¼�
        if(eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = event.getText();
            if(!texts.isEmpty()) {
                for(CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if(text.contains(HONGBAO_TEXT_KEY)) {
                        openNotify(event);
                        break;
                    }
                }
            }
        }
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.i(TAG, "����----------��"+mCurrentUI);
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			//-------------------------��������----------------------------------------------------
			if(mCurrentUI.indexOf(VersionParam.WINDOW_LUCKYMONEY_RECEIVEUI)!=-1){
				//if(UnpackLuckyMoney(mRootNode))
				if(mReceiveJob.OpenLuckyMoney(mRootNode))//���)
						Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
				else
						Config.JobState=Config.STATE_NONE;
			}
			//-------------------------��ʾ��ϸ��Ϣ����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				UnpackLuckyMoneyShow(mRootNode) ;
				//mLuckyMoneyType=Config.TYPE_LUCKYMONEY_NONE;
				Config.JobState=Config.STATE_NONE;
				AccessibilityHelper.performBack(service);
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		if (eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
			//mRootNode=event.getSource();
		}
		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

			if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY)return;
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				//AccessibilityHelper.printParentInfo(mRootNode);
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//AccessibilityHelper.recycle(mRootNode);
				if(mLauncherJob.isMemberChatUi(mRootNode)){
					if(clickLuckyMoney(mRootNode))Config.JobState=Config.STATE_CLICK_LUCKYMONEY;			
				}
				//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//}//if(WECHAT_PACKAGENAME.equals(pkn))
    }
  
    //������ˣ������
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean clickLuckyMoney(AccessibilityNodeInfo rootNode){

    	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode(rootNode);
    		mLuckyMoney.RobbedLuckyMoneyInfoNode=mLuckyMoney.getLastReceivedLuckyMoneyInfoNode(rootNode);
    		if(mLuckyMoney.isNewLuckyMoney(mLuckyMoney.LuckyMoneyNode, mLuckyMoney.RobbedLuckyMoneyInfoNode)){
    					//��������״̬��
    					Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
    					//ClickLuckyMoneyDelay();//�����ʱ��ʾ��
    					return true;
    			//}
    		}
    	}
        return false;
    }



    //�����Ϣչʾ��
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void UnpackLuckyMoneyShow(AccessibilityNodeInfo rootNode) {
        if (rootNode == null) {return;}
        
        mDetailJob.LuckyMoneyDetailShow(rootNode);
        
    }

    /** ��֪ͨ����Ϣ*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotify(AccessibilityEvent event) {
        if(event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        //�����Ǿ�������΢�ŵ�֪ͨ����Ϣ��
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    /** ��֪ͨ����Ϣ*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void newHongBaoNotification(Notification notification) {
        //isReceivingHongbao = true;
        //�����Ǿ�������΢�ŵ�֪ͨ����Ϣ��
        PendingIntent pendingIntent = notification.contentIntent;
        boolean lock = NotifyHelper.isLockScreen(getContext());

        if(!lock) {
            NotifyHelper.send(pendingIntent);
        } else {
            NotifyHelper.showNotify(getContext(), String.valueOf(notification.tickerText), pendingIntent);
        }

        if(lock || getConfig().getWechatMode() != Config.WX_MODE_0) {
            NotifyHelper.playEffect(getContext(), getConfig());
        }
    }
    /** ֪ͨ���¼�*/
    private void notificationEvent(String ticker, Notification nf) {
        String text = ticker;
        int index = text.indexOf(":");
        if(index != -1) {
            text = text.substring(index + 1);
        }
        text = text.trim();
        if(text.contains(HONGBAO_TEXT_KEY)) { //�����Ϣ
            newHongBaoNotification(nf);
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(IStatusBarNotification sbn) {
        Notification nf = sbn.getNotification();
        String text = String.valueOf(sbn.getNotification().tickerText);
        notificationEvent(text, nf);
    }

}
