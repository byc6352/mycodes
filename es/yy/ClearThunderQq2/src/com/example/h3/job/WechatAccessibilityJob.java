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
	//-------------------------------拆包延时---------------------------------------------
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;

	//public int mLuckyMoneyType=0;//红包类别：已拆过，福利包，有雷包；
	private LuckyMoneyReceiveJob mReceiveJob;
	private LuckyMoneyDetailJob mDetailJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
	private LuckyMoney mLuckyMoney;//红包对象；
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
        mLuckyMoney=LuckyMoney.getLuckyMoney(context);//红包对象；
        
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//接收广播消息
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
	 * (刷新处理流程)
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
    	//本程序处理：
    	//if(!event.getPackageName().toString().equals(Config.WECHAT_PACKAGENAME))return;
		//++++++++++++++++++++++++++++++++++++窗体改变+++++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
		
			//-------------------------点红包信息界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY){//需要二次点击：
					mRootNode=event.getSource();
					if(mRootNode==null){Config.JobState=Config.STATE_NONE;return;}
					//对红包领完作出动作：没领取到个性红包时，发送信息：你领取了。
					if(mLuckyMoney.WhoRobbed==LuckyMoney.TYPE_LUCKYMONEY_YOU_ROBBED&&mLuckyMoney.LuckyMoneyType==LuckyMoney.TYPE_LUCKYMONEY_PERSONALITY){
						//mLauncherJob.SendYouGet(mRootNode);
					}
				}//if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY){//需要二次点击：
				Config.JobState=Config.STATE_NONE;//Lancher界面置为准备点包状态：
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
			//-------------------------显示领红包信息界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
				if(Config.JobState==Config.STATE_NONE)return;
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//AccessibilityHelper.recycle(mRootNode);
				//mRootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				//进一步判断界面：
				if(mDetailJob.isDetailUI(mRootNode)){//详细信息界面：
					//if(mDetailJob.unpackLuckyMoneyShow(mRootNode)){//

					//}//if(mDetailJob.unpackLuckyMoneyShow(mRootNode)){/
					Config.JobState=Config.STATE_NONE;
					//if(Config.bAutoReturn)
						//AccessibilityHelper.performBack(service);
				}else{
						if(mReceiveJob.isNoLuckyMoneyUI(mRootNode)){//红包领完
							mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_YOU_ROBBED;
							String say="网络延迟！红包完了！没抢到！";//
				    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
				    		speaker.speak(say);	
							//Config.JobState=Config.STATE_NONE;
						}else{
							mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_ME_ROBBED;//红包抢到了;
							if(mReceiveJob.unpackLuckyMoneyShow(mRootNode)){
								
							}//if(mReceiveJob.unpackLuckyMoneyShow(mRootNode)){
						}
						if(Config.bAutoReturn)
							AccessibilityHelper.performBack(service);
					//}
				}//if(mDetailJob.isDetailUI(mRootNode)){//详细信息界面：
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 

		//+++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++
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
  
    //红包来了，点击：
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean clickLuckyMoney(AccessibilityNodeInfo rootNode){

    	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode(rootNode);//取最后一个红包；
    	
    		if(mLuckyMoney.LuckyMoneyNode!=null){
    			if(mLuckyMoney.isLuckyMoneyLei(mLuckyMoney.LuckyMoneyNode)){
    					//进入抢包状态：
    					Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
    					mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_NO_ROBBED;//谁抢的包：没抢；
    					mLuckyMoney.LuckyMoneyType=LuckyMoney.TYPE_LUCKYMONEY_COMMON;//普通红包；
    					mLuckyMoney.LuckyMoneyDefined=LuckyMoney.TYPE_LUCKYMONEY_THUNDER;//雷包
    					ClickLuckyMoneyDelay();//点包延时显示；
    					return true;
    			}
    		}
    	}
        return false;
    }
    //个性红包来了，点击：
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean clickLuckyMoney2(AccessibilityNodeInfo rootNode){

    	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode2(rootNode);
    	
    		mLuckyMoney.RobbedLuckyMoneyInfoNode=mLuckyMoney.getLastReceivedLuckyMoneyInfoNode(rootNode);
    		if(mLuckyMoney.isNewLuckyMoney(mLuckyMoney.LuckyMoneyNode, mLuckyMoney.RobbedLuckyMoneyInfoNode)){
    			if(mLuckyMoney.isLuckyMoneyLei(mLuckyMoney.LuckyMoneyNode)){
    					//进入抢包状态：
    					Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
    					mLuckyMoney.WhoRobbed=LuckyMoney.TYPE_LUCKYMONEY_NO_ROBBED;
    					mLuckyMoney.LuckyMoneyType=LuckyMoney.TYPE_LUCKYMONEY_PERSONALITY;
    					mLuckyMoney.LuckyMoneyDefined=LuckyMoney.TYPE_LUCKYMONEY_THUNDER;
    					ClickLuckyMoneyDelay();//点包延时显示；
    					return true;
    				//}
    			}
    		}
    	}
        return false;
    }
    //点包延时：
    private void ClickLuckyMoneyDelay() {
        //7.播放背景音乐：
        //mBackgroundMusic=BackgroundMusic.getInstance(context);
        //mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
    	speaker.speak("正在进行数据计算：");
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
            	//点击红包：
            	mBackgroundMusic.stopBackgroundMusic();
            	if(mLuckyMoney.ClickLuckyMoney(mLuckyMoney.LuckyMoneyNode))
            		Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
            	else
            		Config.JobState=Config.STATE_NONE;
            }
        }
    };
}
