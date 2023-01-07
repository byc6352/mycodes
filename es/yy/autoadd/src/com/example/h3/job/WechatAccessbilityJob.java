/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.autoadd.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.AccessibilityHelper;
import com.example.h3.util.FloatingWindow;

import com.example.h3.util.SpeechUtil;
import com.example.h3.util.VersionParam;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
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
	//-------------------------------拆包延时---------------------------------------------

	private SpeechUtil speaker ;
	private BackgroundMusic mBackgroundMusic;

	public int mLuckyMoneyType=0;//红包类别：已拆过，福利包，有雷包；
	private LuckyMoneyLauncherJob mLauncherJob;

	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
	private boolean bDel=false;//删除广告语
	
	private FloatingWindow fw;//显示浮动窗口
	private ChatroomInfo mChatroomInfo;//群聊信息窗口
	private SnsUser mSnsUser;//好友相册窗口
	private AccessibilityNodeInfo mTargetNode;
	private boolean bClick=false;
	


    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
     
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);

        
        fw=FloatingWindow.getFloatingWindow(this,null);
        mChatroomInfo=ChatroomInfo.getChatroomInfo(context);
        mSnsUser=SnsUser.getSnsUser(context);
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
		//++++++++++++++++++++++++++++++++++++窗体改变+++++++++++++++++++++++++++++++++++++++++++++++++
    	Log.i(TAG, "sClassName:"+sClassName);
		mRootNode=event.getSource();
		if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
		//AccessibilityHelper.printParentInfo(mRootNode);
		//mRootNode=AccessibilityHelper.getRootNode(mRootNode);
		
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.i(TAG, "窗体--------------------》"+mCurrentUI);
			AccessibilityHelper.recycle(mRootNode);
			//弹出菜单窗体：-----------------删除 广告语---------------------------------------------------------
			if(bDel){
			if(mCurrentUI.equals("com.tencent.mm.ui.base.k")){
				AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
				if(adPop!=null){
					AccessibilityNodeInfo parent=adPop.getParent();
					if(parent!=null)parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					bDel=false;
				}
			}
			}
			//-------------------------群聊好友界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_CHATROOM_CHATROOMINFO_UI)){
				//显示按钮 ：
				//2。显示浮动窗体（点击加号）：
				if(mChatroomInfo.bWorking){//工作中....
					//List<AccessibilityNodeInfo> chatMembers=mChatroomInfo.getChatMembers(mRootNode);//重新获取成员列表：
			        //if(chatMembers==null){Log.i(TAG, "mChatroomInfo.bWorking:chatMembers==null");return ;}//获取成员列表 ；
			        //if(mChatroomInfo.mChatMembers.size()==0){Log.i(TAG, "mChatroomInfo.bWorking:mChatMembers.size()==0");return ;}
			        
					if(mChatroomInfo.mIndex>=mChatroomInfo.mChatMembers.size()){//本页加人结束
						if(mChatroomInfo.isChatroomInfoLastPage(mRootNode)){//已经是最后一页：
							mChatroomInfo.mIndex=0;
							mChatroomInfo.bWorking=false;//工作结束;
							Config.JobState=Config.STATE_NONE;
							fw.ShowFloatingWindow();
							String sShow="本次加群成员成功完成！";
							Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
							speaker.speak(sShow);
						}else{//翻页：
							AccessibilityHelper.performScrollForward(mRootNode);//翻页
							Config.JobState=Config.STATE_SCROLL_PAGE;
							fw.ShowFloatingWindow();

						}
						return;
					}//if(mChatroomInfo.mIndex>=mChatroomInfo.mChatMembers.size()){//结束
					//Toast.makeText(context, ":"+mChatroomInfo.mIndex, Toast.LENGTH_SHORT).show();
					
					AccessibilityNodeInfo chatMember=mChatroomInfo.mChatMembers.get(mChatroomInfo.mIndex);

					if(!AccessibilityHelper.performClick(chatMember)){//点击失败时：
						Log.i(TAG, "performClick Error:mChatroomInfo.mIndex="+mChatroomInfo.mIndex);
						Toast.makeText(context,  "失败于:"+mChatroomInfo.mIndex, Toast.LENGTH_LONG).show();
						mChatroomInfo.mIndex=mChatroomInfo.mIndex+1;
						chatMember=mChatroomInfo.mChatMembers.get(mChatroomInfo.mIndex);
						if(!AccessibilityHelper.performClick(chatMember)){//点击失败时：
							Log.i(TAG, "第二次performClick Error:mChatroomInfo.mIndex="+mChatroomInfo.mIndex);
							Toast.makeText(context,  "第二次失败于:"+mChatroomInfo.mIndex, Toast.LENGTH_LONG).show();
						}
					}
					Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
					mChatroomInfo.mIndex=mChatroomInfo.mIndex+1;
					Log.i(TAG, ":"+mChatroomInfo.mIndex);

				}else{//空闲
					fw.mFbtFunc1.setText("加群好友");
					fw.ShowFloatingWindow();
				}
			}else{
				//4。关闭悬浮按钮：
				//fw.DestroyFloatingWindow();
			}//if(mCurrentUI.equals(Config.WINDOW_CHATROOM_CHATROOMINFO_UI)){
			//-------------------------添加好友详细信息界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_CONTACT_CONTACTINFO_UI)){
				if(Config.JobState==Config.STATE_CONTACT_INFO_UNADD){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "添加到通讯录",0);
					//if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
					if(mTargetNode==null){
						Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
						AccessibilityHelper.performBack(service);
						return;
					}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_SAYHI_PERMISSION;
				}
				
				if(Config.JobState==Config.STATE_CONTACT_INFO_ADDED){//返回的界面
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
				}
			}
			//-------------------------验证申请界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_SAYHI_PERMISSION_UI)){
				if(Config.JobState!=Config.STATE_SAYHI_PERMISSION)return;
				mTargetNode=AccessibilityHelper.findNodeInfosByClassName(mRootNode, "android.widget.EditText", 0, true);
				if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
				if(mTargetNode.getText()==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
				String sayHi=mTargetNode.getText().toString();
				if(!sayHi.equals(Config.sAddFriendSay)){nodeInput(mTargetNode,Config.sAddFriendSay);}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "发送",0,true);
				if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
				AccessibilityHelper.performClick(mTargetNode);
				Config.JobState=Config.STATE_CONTACT_INFO_ADDED;
			}
			//-------------------------好友相册----------------------------------------------------
			if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){
				if(mSnsUser.bWorking&&Config.JobState==Config.STATE_OPEN_GALLERY){//工作中....
					fw.ShowFloatingWindow();
					if(mSnsUser.mIndex>=mSnsUser.mGallerys.size()){//本页加人结束
						if(mSnsUser.isGalleryLastPage(mRootNode)){//已经是最后一页：
							mSnsUser.mIndex=0;
							mSnsUser.bWorking=false;//工作结束;
							Config.JobState=Config.STATE_NONE;
							fw.ShowFloatingWindow();
							String sShow="本次打开视频成功完成！";
							Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
							speaker.speak(sShow);
						}else{//翻页：
							//AccessibilityHelper.performScrollForward2(mRootNode);//翻页
							mSnsUser.ScrollPage(mRootNode);
							Config.JobState=Config.STATE_SCROLL_PAGE;
							fw.ShowFloatingWindow();

						}
						return;//本页打开完所有视频
					}//本页加人结束
					AccessibilityNodeInfo galleryMember=mSnsUser.mGallerys.get(mSnsUser.mIndex);//获取下一个视频

					if(!AccessibilityHelper.performClickSingle(galleryMember)){//点击失败时：
						Log.i(TAG, "performClick Error:mSnsUser.mIndex="+mSnsUser.mIndex);
					}
					Config.JobState=Config.STATE_OPEN_GALLERY;
					mSnsUser.mIndex=mSnsUser.mIndex+1;
					Log.i(TAG, ":"+mSnsUser.mIndex);
				}else{//空闲
					fw.mFbtFunc1.setText("依次打开相册视频");
					fw.ShowFloatingWindow();
				}
			}else{
				//4。关闭悬浮按钮：
				//fw.DestroyFloatingWindow();
			}//if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){
			
			//-------------------------视频界面----------------------------------------------------
			if(mCurrentUI.equals(VersionParam.WINDOW_SNSGALLERY_UI)){
				//fw.ShowFloatingWindow();
				if(Config.JobState==Config.STATE_OPEN_GALLERY){
					
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "返回",0);
					//if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
					if(mTargetNode==null){
						Config.JobState=Config.STATE_OPEN_GALLERY;
						//AccessibilityHelper.performBack(service);
						return;
					}
					//保存日期：
					mSnsUser.SaveGalleryDate(mRootNode);
					AccessibilityHelper.Sleep(10000);
					AccessibilityHelper.performClickSingle(mTargetNode);
					Config.JobState=Config.STATE_OPEN_GALLERY;
				}
			}
			
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		
		
		
		
		if (eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
			//mRootNode=event.getSource();
		}
		//+++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			mRootNode=event.getSource();
			if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
			//AccessibilityHelper.printParentInfo(mRootNode);
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			Log.i(TAG, sClassName+"-----------------TYPE_WINDOW_CONTENT_CHANGED----------------------");
			AccessibilityHelper.recycle(mRootNode);
			//if(sClassName.equals("android.widget.ListView")&&Config.JobState==Config.STATE_SCROLL_PAGE){
			if(Config.JobState==Config.STATE_SCROLL_PAGE){
				Log.i(TAG, " AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: STATE_SCROLL_PAGE");
				if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){//好友相册窗口
					AccessibilityHelper.Sleep(6000);
					distributeGetGallerys();
				}
				if(mCurrentUI.equals(VersionParam.WINDOW_CHATROOM_CHATROOMINFO_UI)){//
					distributeClickJiaJob();
				}
				return;
			}
			if(Config.JobState==Config.STATE_SCROLL_GALLERY&&sClassName.equals("android.widget.ListView")&&mSnsUser.bWorking==true){
				AccessibilityHelper.Sleep(3000);
				mRootNode=event.getSource();
				if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
				//AccessibilityHelper.printParentInfo(mRootNode);
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				if(mSnsUser.isScrollGalleryNeedDate(mRootNode)){//到达目标日期：
					mSnsUser.bWorking=false;
					Config.JobState=Config.STATE_NONE;
					String say="已滚动到要求的地方！滚动结束！";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{//继续翻动：
					mSnsUser.ScrollPage(mRootNode);
				}
			}
			if(Config.JobState==Config.STATE_SWIPE_GALLERY&&mSnsUser.bWorking==true){				
				//mRootNode=event.getSource();
				//if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
				//AccessibilityHelper.printParentInfo(mRootNode);
				//mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				if(mSnsUser.isGalleryUI(mCurrentUI)){
					//if(bClick){
						//mSnsUser.ClickGallery();
						//bClick=false;
					//}else{	
						AccessibilityHelper.Sleep(10000);
						//保存日期：
						mRootNode=service.getRootInActiveWindow();
						if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
						//AccessibilityHelper.printParentInfo(mRootNode);
						mRootNode=AccessibilityHelper.getRootNode(mRootNode);
						mSnsUser.SaveGalleryDate(mRootNode);
						
						mSnsUser.SwipeGallery();
						//mSnsUser.ClickGallery();
						//bClick=true;
					//}
					
				}
				
			}
			return;
			//if(Config.JobState==Config.STATE_CLICK_LUCKYMONEY)return;
			/*
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				mRootNode=event.getSource();
				if(mRootNode==null)return;
				//AccessibilityHelper.printParentInfo(mRootNode);
				mRootNode=AccessibilityHelper.getRootNode(mRootNode);
				//AccessibilityHelper.recycle(mRootNode);
				if(mLauncherJob.isMemberChatUi(mRootNode)){
					//实现长按指定 内容：----------------------------弹出删除菜单-------------------------------------
					AccessibilityNodeInfo adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mLauncherJob.mStrAD ,-1);
					if(adNode!=null){
						if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
					}
					//----------------------------------------------------------------------------------
					mLauncherJob.mIntAD=mLauncherJob.mIntAD+1;
					if(mLauncherJob.mIntAD>mLauncherJob.MAX_NO_REG_AD)mLauncherJob.SendAD(mRootNode);//试用版发布广告；
					if(!Config.bSwitch)return;//总开关关闭；
					if(clickLuckyMoney(mRootNode))Config.JobState=Config.STATE_CLICK_LUCKYMONEY;			
				}
				//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
			*/
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//}//if(WECHAT_PACKAGENAME.equals(pkn))
    }
  
    //点击加号：
    public boolean distributeClickJiaJob() {
    	mChatroomInfo.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mChatroomInfo.bWorking=false;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeClickJiaJob start:");
        if (rootNode == null) {Log.i(TAG, "distributeClickJiaJob start:rootNode == null");return false;}
        if(!mChatroomInfo.isChatroomInfoUi(rootNode)) {Log.i(TAG, "distributeClickJiaJob start:isChatroomInfoUi == false");return false;}

        List<AccessibilityNodeInfo> chatMembers=mChatroomInfo.getChatMembers(rootNode);
        if(chatMembers==null){Log.i(TAG, "distributeClickJiaJob start:chatMembers==null");return false;}//获取成员列表 ；
        
        if(mChatroomInfo.mChatMembers.size()==0){Log.i(TAG, "distributeClickJiaJob start:mChatMembers.size()==0");return false;}
        
        AccessibilityNodeInfo chatFirstMember=mChatroomInfo.mChatMembers.get(mChatroomInfo.mIndex);
        //if(chatFirstMember==null){Log.i(TAG, "distributeClickJiaJob start:chatFirstMember==null");return false;}
        
        if(!AccessibilityHelper.performClick(chatFirstMember)){Log.i(TAG, "distributeClickJiaJob start:performClick == false");return false;}//点击第一个成员；
        mChatroomInfo.mIndex=mChatroomInfo.mIndex+1;
        mChatroomInfo.bWorking=true;
        Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
		String sShow="本次加群成员共："+String.valueOf(chatMembers.size())+"人。开始加人了，请稍后！";
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
        return true;
    }
    //开始打开视频：
    public boolean distributeGetGallerys() {
    	mSnsUser.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mSnsUser.bWorking=false;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeGetGallerys start:");
        if (rootNode == null) {Log.i(TAG, "distributeGetGallerys start:rootNode == null");return false;}
        List<AccessibilityNodeInfo> galleryMembers=mSnsUser.getGalleryMembers(rootNode);
        if(galleryMembers==null){Log.i(TAG, "distributeGetGallerys start:galleryMembers==null");return false;}//获取成员列表 ；
        if(mSnsUser.mGallerys.size()==0){Log.i(TAG, "distributeGetGallerys start:galleryMembers.size()==0");return false;}
        
        AccessibilityNodeInfo galleryFirstMember=mSnsUser.mGallerys.get(mSnsUser.mIndex);
        if(!AccessibilityHelper.performClick(galleryFirstMember)){Log.i(TAG, "distributeGetGallerys start:performClick == false");return false;}//点击第一个成员；
        mSnsUser.mIndex=mSnsUser.mIndex+1;
        mSnsUser.bWorking=true;
        Config.JobState=Config.STATE_OPEN_GALLERY;
		String sShow="本次打开视频共："+String.valueOf(galleryMembers.size())+"个。开始打开视频了，请稍后！";
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
        return true;
    }
    /*开始翻动相册*/
    public boolean distributeScollGallery() {
    	Config.JobState=Config.STATE_SCROLL_GALLERY;
    	mSnsUser.bWorking=true;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeScollGallery start:");
        if (rootNode == null) {Log.i(TAG, "distributeScollGallery start:rootNode == null");return false;}
    	mSnsUser.ScrollPage(rootNode);
    	return true;
    }
    /*开始滑动视频*/
    public boolean distributeSwipeGallery() {
        //AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        //Log.i(TAG, "distributeSwipeGallery start:");
        //if (rootNode == null) {Log.i(TAG, "distributeSwipeGallery start:rootNode == null");return false;}
    	//mSnsUser.ScrollPage(rootNode);
		boolean bWindow=mSnsUser.isSnsUserUI(mCurrentUI);
		if(!bWindow){
			String sShow="请先打开相册，才能滑动！";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			return false;
		}
		AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
	    Log.i(TAG, "distributeSwipeGallery start:");
	    if (rootNode == null) {Log.i(TAG, "distributeSwipeGallery start:rootNode == null");return false;}
	    List<AccessibilityNodeInfo> galleryMembers=mSnsUser.getGalleryMembers(rootNode);
	    if(galleryMembers==null){Log.i(TAG, "distributeSwipeGallery start:galleryMembers==null");return false;}//获取成员列表 ；
	    if(mSnsUser.mGallerys.size()==0){Log.i(TAG, "distributeSwipeGallery start:galleryMembers.size()==0");return false;}
	        
	    AccessibilityNodeInfo galleryLastMember=galleryMembers.get(galleryMembers.size()-1);
	    if(!AccessibilityHelper.performClick(galleryLastMember)){Log.i(TAG, "distributeSwipeGallery start:performClick == false");return false;}//点击最后一个成员；
		//if(mSnsUser.SwipeGallery()){
			Config.JobState=Config.STATE_SWIPE_GALLERY;
			mSnsUser.bWorking=true;
		//}else{
			//String sShow="滑动执行失败！";
			//Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			//speaker.speak(sShow);
			//return false;
		//}
    	return true;
    }
    //打开视频成功吗：
    public boolean distributeOpenGallery() {
		boolean bWindow=distributeGetGallerys();
		if(!bWindow){
			String sShow="请进入好友相册界面！才能开始打开视频。";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_OPEN_GALLERY;//进入输入文本状态；
			mSnsUser.bWorking=true;
			return true;
		}
    }
    /*停止工作*/
    public boolean distributeStopJob(){
    	mSnsUser.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mSnsUser.bWorking=false;
    	String say="已停止工作！工作成功完成！";
		Toast.makeText(context, say, Toast.LENGTH_LONG).show();
		speaker.speak(say);
		return true;
    }
    /*开始添加微信群成员*/
    public boolean distributeChatJob(int iFunc){
    	if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){//好友相册窗口
    		if(iFunc==1)return distributeOpenGallery();
    		if(iFunc==2)return distributeScollGallery();
    		if(iFunc==3)return distributeStopJob();
    		if(iFunc==4)return distributeSwipeGallery();
    	}
    	if(mCurrentUI.equals(VersionParam.WINDOW_CHATROOM_CHATROOMINFO_UI)){//好友相册窗口
    		return distributeAddChatMembers();
    	}
    	return false;
    }
    /*开始添加微信群成员*/
    public boolean distributeAddChatMembers(){
		boolean bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="请进入群信息界面！才能开始加群好友。";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_CONTACT_INFO_UNADD;//进入输入文本状态；
			mChatroomInfo.bWorking=true;
			return true;
		}
    }
    //点击加号：
    public void test() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return ;}
        if(!mChatroomInfo.isChatroomInfoUi(rootNode)) return ;
        AccessibilityHelper.recycle(rootNode);
        AccessibilityHelper.performScrollForward2(rootNode);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  boolean nodeInput(AccessibilityNodeInfo edtNode,String txt){
    	if(Config.currentapiVersion>=Build.VERSION_CODES.LOLLIPOP){//android 5.0
    		Bundle arguments = new Bundle();
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
        	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	return true;
    	}
    	if(Config.currentapiVersion>=Build.VERSION_CODES.JELLY_BEAN_MR2){//android 4.3
    		ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
    		ClipData clip = ClipData.newPlainText("text",txt);  
    		clipboard.setPrimaryClip(clip);  

    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  
    		////粘贴进入内容  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	
    	return false;
    }
	
}
