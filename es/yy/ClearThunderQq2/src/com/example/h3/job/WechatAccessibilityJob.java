/**
 * 
 */
package com.example.h3.job;

import java.util.Timer;
import java.util.TimerTask;

import com.byc.qqclearthunder2.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import accessibility.QiangHongBaoService;
import accessibility.AccessibilityHelper;
import accessibility.BaseAccessibilityJob;

import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.annotation.TargetApi;
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
public class WechatAccessibilityJob extends BaseAccessibilityJob  {
	
	private static WechatAccessibilityJob current;
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
    	final int eventType = event.getEventType();
    	if(event.getClassName()==null)return;
    	String sClassName=event.getClassName().toString();
    	//����������
    	//if(!event.getPackageName().toString().equals(Config.WECHAT_PACKAGENAME))return;
		//++++++++++++++++++++++++++++++++++++����ı�+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
		
			//-------------------------������Ϣ����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY){//��Ҫ���ε����
					mRootNode=event.getSource();
					if(mRootNode==null){Config.JobState=Config.STATE_NONE;return;}
					//�Ժ����������������û��ȡ�����Ժ��ʱ��������Ϣ������ȡ�ˡ�
					if(mLuckyMoney.WhoRobbed==LuckyMoney.TYPE_LUCKYMONEY_YOU_ROBBED&&mLuckyMoney.LuckyMoneyType==LuckyMoney.TYPE_LUCKYMONEY_PERSONALITY){
						//mLauncherJob.SendYouGet(mRootNode);
					}
				}//if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY){//��Ҫ���ε����
				Config.JobState=Config.STATE_NONE;//Lancher������Ϊ׼�����״̬��
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
			//-------------------------��ʾ������Ϣ����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
				if(Config.JobState==Config.STATE_NONE)return;
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//AccessibilityHelper.recycle(mRootNode);
				//mRootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				//��һ���жϽ��棺
				if(mDetailJob.isDetailUI(mRootNode)){//��ϸ��Ϣ���棺
					//if(mDetailJob.unpackLuckyMoneyShow(mRootNode)){//

					//}//if(mDetailJob.unpackLuckyMoneyShow(mRootNode)){/
					Config.JobState=Config.STATE_NONE;
					//if(Config.bAutoReturn)
						//AccessibilityHelper.performBack(service);
				}else{
						if(mReceiveJob.isNoLuckyMoneyUI(mRootNode)){//�������
							mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_YOU_ROBBED;
							String say="�����ӳ٣�������ˣ�û������";//
				    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
				    		speaker.speak(say);	
							//Config.JobState=Config.STATE_NONE;
						}else{
							mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_ME_ROBBED;//���������;
							if(mReceiveJob.unpackLuckyMoneyShow(mRootNode)){
								
							}//if(mReceiveJob.unpackLuckyMoneyShow(mRootNode)){
						}
						if(Config.bAutoReturn)
							AccessibilityHelper.performBack(service);
					//}
				}//if(mDetailJob.isDetailUI(mRootNode)){//��ϸ��Ϣ���棺
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 

		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			if(!Config.bAutoClearThunder)return;
			if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY)return;
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				
				if(mLauncherJob.isMemberChatUi(mRootNode)){
				
					if(clickLuckyMoney(mRootNode)){Config.JobState=Config.STATE_CLICK_LUCKYMONEY;return;}		
					if(clickLuckyMoney2(mRootNode))Config.JobState=Config.STATE_CLICK_LUCKYMONEY;	
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
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode(rootNode);//ȡ���һ�������
    	
    		if(mLuckyMoney.LuckyMoneyNode!=null){
    			if(mLuckyMoney.isLuckyMoneyLei(mLuckyMoney.LuckyMoneyNode)){
    					//��������״̬��
    					Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
    					mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_NO_ROBBED;//˭���İ���û����
    					mLuckyMoney.LuckyMoneyType=LuckyMoney.TYPE_LUCKYMONEY_COMMON;//��ͨ�����
    					mLuckyMoney.LuckyMoneyDefined=LuckyMoney.TYPE_LUCKYMONEY_THUNDER;//�װ�
    					ClickLuckyMoneyDelay();//�����ʱ��ʾ��
    					return true;
    			}
    		}
    	}
        return false;
    }
    //���Ժ�����ˣ������
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean clickLuckyMoney2(AccessibilityNodeInfo rootNode){

    	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode2(rootNode);
    	
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
    				//}
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
    	fwp.c=Config.getConfig(context).getWechatOpenDelayTime();;
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
            	if(mLuckyMoney.ClickLuckyMoney(mLuckyMoney.LuckyMoneyNode))
            		Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
            	else
            		Config.JobState=Config.STATE_NONE;
            }
        }
    };
}