/**
 * 
 */
package com.example.h3.job;


import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import accessibility.QiangHongBaoService;
import ad.VersionParam;
import notification.IStatusBarNotification;
import notification.NotifyHelper;
import util.SpeechUtil;
import accessibility.AccessibilityHelper;
import accessibility.BaseAccessibilityJob;

import com.example.h3.FloatingWindowPic;
import com.byc.superrob.R;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
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
	private int mWechatOpenDelayTime=0;//拆包延时
	private SpeechUtil speeker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;

	public int mLuckyMoneyType=0;//红包类别：已拆过，福利包，有雷包；
	private LuckyMoneyReceiveJob mReceiveJob;
	private LuckyMoneyDetailJob mDetailJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoney mLuckyMoney;//红包对象；
	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
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
        mLuckyMoney=LuckyMoney.getLuckyMoney(context);//红包对象；
        speeker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        
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
    	//mVolumeChange.onDestroy();
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
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.i(TAG, sClassName);
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)service.closeClick();
			//-------------------------拆红包界面----------------------------------------------------

					if(mCurrentUI.indexOf(VersionParam.WINDOW_LUCKYMONEY_RECEIVEUI)!=-1){
						if(UnpackLuckyMoney(mRootNode))
							Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
						else
							Config.JobState=Config.STATE_NONE;
					}

			//-------------------------显示详细信息界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				//if(mLuckyMoneyType==Config.TYPE_LUCKYMONEY_THUNDER){
				if(Config.bShowMoney){
					UnpackLuckyMoneyShow(mRootNode) ;
				}
				mLuckyMoneyType=Config.TYPE_LUCKYMONEY_NONE;
				Config.JobState=Config.STATE_NONE;
				AccessibilityHelper.performBack(service);
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		if (eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
			//mRootNode=event.getSource();
		}
		//+++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

			if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY)return;
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				//AccessibilityHelper.printParentInfo(mRootNode);
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//AccessibilityHelper.recycle(mRootNode);
				//if(mLauncherJob.isMemberChatUi(mRootNode)){
			
					if(!Config.bSwitch)return;//总开关关闭；
					if(clickLuckyMoney(mRootNode))Config.JobState=Config.STATE_CLICK_LUCKYMONEY;			
				//}
				//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//}//if(WECHAT_PACKAGENAME.equals(pkn))
    }
  
    //红包来了，点击：
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean clickLuckyMoney(AccessibilityNodeInfo rootNode){

    	if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    		
    		mLuckyMoney.LuckyMoneyNode=mLuckyMoney.getLastLuckyMoneyNode(rootNode);
    		if(mLuckyMoney.LuckyMoneyNode==null)return false;
    		boolean bNewLuckyMoney=true;
    		if(Config.wv<1120){
    			mLuckyMoney.RobbedLuckyMoneyInfoNode=mLuckyMoney.getLastReceivedLuckyMoneyInfoNode(rootNode);
    			bNewLuckyMoney=mLuckyMoney.isNewLuckyMoney(mLuckyMoney.LuckyMoneyNode, mLuckyMoney.RobbedLuckyMoneyInfoNode);
    		}
    		if(bNewLuckyMoney){
    			if(Config.bNoPayFor)//过滤赔付包：
    			if(!mLuckyMoney.isLuckyMoneyLei(mLuckyMoney.LuckyMoneyNode))return false;
  
    					//进入抢包状态：
    					Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
    					ClickLuckyMoneyDelay();//点包延时显示；
    					return true;
    			//}
    		}
    	}
        return false;
    }

  
    //拆红包
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean UnpackLuckyMoney(AccessibilityNodeInfo rootNode) {
    	 if (rootNode == null) return false;
         speeker.speak("正在为您分析：");
         mLuckyMoneyType=Config.TYPE_LUCKYMONEY_THUNDER;
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
         	Rect outBounds=new Rect();
         	rootNode.getBoundsInScreen(outBounds);
     		Point position=new Point();
     		position.x=outBounds.right/2;
     		position.y=outBounds.bottom*3/5;
     		QiangHongBaoService.getQiangHongBaoService().startClick(position);
     		return true;
         }
        
        mLuckyMoneyType=mReceiveJob.IsLuckyMoneyReceive(rootNode);
        if(mLuckyMoneyType==Config.TYPE_LUCKYMONEY_THUNDER){
        	//mLauncherJob.preLuckyMoneyNode=null;
        	UnpackLuckyMoneyDelay();//拆包延时显示；
        	return mReceiveJob.OpenLuckyMoney(rootNode);//拆包
        }
        if(mLuckyMoneyType==Config.TYPE_LUCKYMONEY_WELL){
        	if(Config.bNoPayFor){//过滤赔付包：
        		speeker.speak("福利包不拆！");
        	}else{
            	UnpackLuckyMoneyDelay();//拆包延时显示；
            	return mReceiveJob.OpenLuckyMoney(rootNode);//拆包
        	}
        	//mLauncherJob.preLuckyMoneyNode=null;
        	return false;
        }
        return false;
    }
    //点包延时：
    private void ClickLuckyMoneyDelay() {
        //7.播放背景音乐：
        //mBackgroundMusic=BackgroundMusic.getInstance(context);
        //mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
    	speeker.speak("正在进行数据计算：");
    	mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=Config.getConfig(context).getWechatOpenDelayTime();
    	fwp.mSendMsg=Config.ACTION_CLICK_LUCKY_MONEY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    	fwp.StartSwitchPics();
    	//fwp.StopSwitchPics();
    	//fwp.RemoveFloatingWindowPic();
    	//mBackgroundMusic.stopBackgroundMusic();
    }
    //拆包延时：
    private void UnpackLuckyMoneyDelay() {
    	
    	//float f=0;
    	mWechatOpenDelayTime=Config.getConfig(context).getWechatOpenDelayTime();
    	//for(int i=0;i<mWechatOpenDelayTime;i++){
    		//handler.postDelayed(run, 10);
    	//}
    	float f=(float) (Math.random()*10000);
    	String s=String.valueOf(f);
    	Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	    try{
	    	  Thread.sleep(10*mWechatOpenDelayTime);
	    }catch(Exception e){
	    } 
	    if(mWechatOpenDelayTime>0){
	    	f=(float) (Math.random()*10000);
	    	s=String.valueOf(f);
	    	Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	    }

    }
    //拆包信息展示：
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void UnpackLuckyMoneyShow(AccessibilityNodeInfo rootNode) {
        if (rootNode == null) {return;}
        
        mDetailJob.LuckyMoneyDetailShow(rootNode);
        
    }
	private BroadcastReceiver ClickLuckyMoneyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_CLICK_LUCKY_MONEY.equals(action)) {
            	mBackgroundMusic.stopBackgroundMusic();
            	//点击红包：
            	if(mLuckyMoney.ClickLuckyMoney(mLuckyMoney.LuckyMoneyNode))
            		Config.JobState=Config.STATE_CLICK_LUCKYMONEY;
            	else
            		Config.JobState=Config.TYPE_LUCKYMONEY_NONE;
            }
            if(Config.ACTION_ACCESSBILITY_SERVICE_CLICK.equals(action)) {//点击事件;
    			//-------------------------拆红包界面----------------------------------------------------
            	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
				if(mCurrentUI.indexOf(VersionParam.WINDOW_LUCKYMONEY_RECEIVEUI)!=-1){
					if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY){
						int count=intent.getIntExtra("count", 0);
						if(count>10){//红包完了，没抢到！
							speeker.speak("红包完了！没抢到！");
				        	AccessibilityHelper.performBack(service);
				        	Config.JobState=Config.STATE_NONE;
				        	service.closeClick();
						}
					}
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

