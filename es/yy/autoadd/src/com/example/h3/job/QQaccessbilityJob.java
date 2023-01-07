/**
 * 
 */
package com.example.h3.job;

import java.util.List;

import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.AccessibilityHelper;
import com.example.h3.util.FloatingWindow;

import com.example.h3.util.SpeechUtil;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class QQaccessbilityJob extends BaseAccessbilityJob  {
	private static QQaccessbilityJob current;
	private SpeechUtil speaker ;
	private FloatingWindow fw;//显示浮动窗口
	private BackgroundMusic mBackgroundMusic;
	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
	private QqTroopMemberList mQqTroopMemberList;
	private AccessibilityNodeInfo mTargetNode;
	
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        
        speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        
        fw=FloatingWindow.getFloatingWindow(null,this);
        mQqTroopMemberList=QqTroopMemberList.getQqTroopMemberList(context);
    }
    @Override
    public void onStopJob() {

    }
    public static synchronized QQaccessbilityJob getJob() {
        if(current == null) {
            current = new QQaccessbilityJob();
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
		
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.i(TAG, "窗体--------------------》"+mCurrentUI);
			//AccessibilityHelper.recycle(mRootNode);
			//-------------------------群成员列表界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_LIST_UI)){
				if(mQqTroopMemberList.bWorking){//工作中....
					if(mQqTroopMemberList.mIndex>=mQqTroopMemberList.mQqMembers.size()){//本页加人结束
						mQqTroopMemberList.mMemberAdded=mQqTroopMemberList.mMemberAdded+mQqTroopMemberList.mQqMembers.size();//累积成员
						if(mQqTroopMemberList.isTroopMemberListLastPage(mRootNode)){//已经是最后一页：
							mQqTroopMemberList.mIndex=0;
							mQqTroopMemberList.bWorking=false;//工作结束;
							Config.JobState=Config.STATE_NONE;
							fw.ShowFloatingWindow();
							String sShow="本次加群成员成功完成！";
							Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
							speaker.speak(sShow);
						}else{//翻页：
							AccessibilityHelper.performScrollForward2(mRootNode);//翻页
							Config.JobState=Config.STATE_SCROLL_PAGE;
							fw.ShowFloatingWindow();
						}//if(mQqTroopMemberList.isTroopMemberListLastPage(mRootNode)){//已经是最后一页：
						return;//本页加人结束
					}//if(mQqTroopMemberList.mIndex>=mQqTroopMemberList.mQqMembers.size()){//本页加人结束
					AccessibilityNodeInfo QqMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);

					if(!AccessibilityHelper.performClick(QqMember)){//点击失败时：
						Log.i(TAG, "performClick Error:mChatroomInfo.mIndex="+mQqTroopMemberList.mIndex);
						Toast.makeText(context,  "失败于:"+mQqTroopMemberList.mIndex, Toast.LENGTH_LONG).show();
						mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
						QqMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);
						if(!AccessibilityHelper.performClick(QqMember)){//点击失败时：
							Log.i(TAG, "第二次performClick mQqTroopMemberList="+mQqTroopMemberList.mIndex);
							Toast.makeText(context,  "第二次失败于:"+mQqTroopMemberList.mIndex, Toast.LENGTH_LONG).show();
						}
					}//if(!AccessibilityHelper.performClick(QqMember)){//点击失败时：
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
					Log.i(TAG, ":"+mQqTroopMemberList.mIndex);
				}else{//空闲//if(mQqTroopMemberList.bWorking){//工作中....
					fw.ShowFloatingWindow();
				}////if(mQqTroopMemberList.bWorking){//工作中....
			}else{
				//4。关闭悬浮按钮：
				fw.DestroyFloatingWindow();
			}////if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_LIST_UI)){
			//-------------------------群成员资料卡界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_CARD_UI)){
				if(Config.JobState==Config.STATE_TROOP_MEMBER_CARD_UNADD){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "加好友",0);
					//if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
					if(mTargetNode==null){
						Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
						AccessibilityHelper.performBack(service);
						return;
					}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_ADD_FRIEND_VERIFY;
					return;
				}
				//--------------------------加完好友返回：------------------
				if(Config.JobState==Config.STATE_TROOP_MEMBER_CARD_ADDED){//返回的界面
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
				//--------------------------不允许加好友 ：------------------
				if(Config.JobState==Config.STATE_ADD_FRIEND_VERIFY){//不允许加好友 
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
			}//if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_CARD_UI)){
			//-------------------------验证申请界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_ADD_FRIEND_VERIFY_UI)){
				
				
				if(Config.JobState==Config.STATE_ADD_FRIEND_VERIFY_FAIL){//添加失败，请勿频繁操作
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
					return;
				}
				if(Config.JobState!=Config.STATE_ADD_FRIEND_VERIFY)return;
				//需要输入答案：
				//mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "输入答案",0);
				if(mQqTroopMemberList.isAnswer(mRootNode)){
					//AccessibilityHelper.performBack(service);	
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "取消",0,true);
					if(mTargetNode==null){AccessibilityHelper.performBack(service);AccessibilityHelper.performBack(service);return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
					return;
				}
				Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
				//输入信息：
				mTargetNode=AccessibilityHelper.findNodeInfosByClassName(mRootNode, "android.widget.EditText", 0, true);
				if(mTargetNode==null){AccessibilityHelper.performBack(service);return;}
				if(mTargetNode.getText()==null){AccessibilityHelper.performBack(service);return;}
				String sayHi=mTargetNode.getText().toString();
				if(!sayHi.equals(Config.sAddFriendSay)){nodeInput(mTargetNode,Config.sAddFriendSay);}
				//点发送按钮：
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "发送",0,true);
				if(mTargetNode==null){AccessibilityHelper.performBack(service);return;}
				AccessibilityHelper.performClick(mTargetNode);
			}
			//-------------------------添加失败界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_ADD_FRIEND_FAIL_UI)){
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "添加失败，请稍后再试",0,true);
				if(mTargetNode!=null){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "确定",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_ADD_FRIEND_VERIFY;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "添加失败，请勿频繁操作",0,true);
				if(mTargetNode!=null){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "确定",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					//AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_ADD_FRIEND_VERIFY_FAIL;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "对方号码疑似异常，加为好友存在安全风险。",0);
				if(mTargetNode!=null){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "继续",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					//Config.JobState=Config.STATE_ADD_FRIEND_VERIFY;
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "无法添加",0);
				if(mTargetNode!=null){//对方号码存在安全风险，暂时无法添加。
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "确定",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "风险提示",0);
				if(mTargetNode!=null){//对方号码近期已被他人举报，请谨慎添加。
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "取消",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
			}
			//-------------------------群设置界面----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_SETTING_TROOP_UI)){//获取群成员总人数：
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "名成员",0);
				if(mTargetNode==null){return;}
				String MemberCount=mTargetNode.getText().toString();
				mQqTroopMemberList.mMemberCount=Integer.parseInt(MemberCount.substring(0,MemberCount.length()-3));
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
		//+++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++
				if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

					//if(sClassName.equals("android.widget.ListView")&&Config.JobState==Config.STATE_SCROLL_PAGE){
					if(Config.JobState==Config.STATE_SCROLL_PAGE){
						Log.i(TAG, " AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: STATE_SCROLL_PAGE");
						distributeClickJiaJob();
						return;
					}
					return;
				}
    }// public void onReceiveJob(AccessibilityEvent event) {
    //点击加号：
    public boolean distributeClickJiaJob() {
    	mQqTroopMemberList.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mQqTroopMemberList.bWorking=false;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeClickJiaJob start:");
        if (rootNode == null) {Log.i(TAG, "distributeClickJiaJob start:rootNode == null");return false;}
        if(!mQqTroopMemberList.isQqTroopMemberListUi(rootNode)) {Log.i(TAG, "distributeClickJiaJob start:isChatroomInfoUi == false");return false;}

        List<AccessibilityNodeInfo> QqMembers=mQqTroopMemberList.getQqTroopMemberList(rootNode);
        if(QqMembers==null){Log.i(TAG, "distributeClickJiaJob QqMembers==null");return false;}//获取成员列表 ；
        
        if(mQqTroopMemberList.mQqMembers.size()==0){Log.i(TAG, "distributeClickJiaJob start:mQqTroopMemberList.size()==0");return false;}
        
        
        AccessibilityNodeInfo QqFirstMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);
        //if(chatFirstMember==null){Log.i(TAG, "distributeClickJiaJob start:chatFirstMember==null");return false;}
        
        if(!AccessibilityHelper.performClick(QqFirstMember)){
        	Log.i(TAG, "distributeClickJiaJob start:performClick(QqFirstMember) == false");
        	mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
        	QqFirstMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);
        	if(!AccessibilityHelper.performClick(QqFirstMember)){//点击第二 个成员；
        		Log.i(TAG, "distributeClickJiaJob start2:performClick(QqFirstMember) == false");
        		return false;
        	}
        	
        }//点击第一个成员；
        mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
        mQqTroopMemberList.bWorking=true;
        Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;//TroopMemberCard
		String sShow="本次加群成员共："+String.valueOf(QqMembers.size())+"人。总群人数："+mQqTroopMemberList.mMemberCount+"。已加人数："+mQqTroopMemberList.mMemberAdded+"。开始加人了，请稍后！";
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
        return true;
    }
    public boolean distributeAddQqMembers(){
		boolean bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="请进入群成员列表界面！才能开始加群好友。";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;//进入输入文本状态；
			mQqTroopMemberList.bWorking=true;
			return true;
		}
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
