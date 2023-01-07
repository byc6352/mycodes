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
	private FloatingWindow fw;//��ʾ��������
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
		//++++++++++++++++++++++++++++++++++++����ı�+++++++++++++++++++++++++++++++++++++++++++++++++
    	Log.i(TAG, "sClassName:"+sClassName);
		mRootNode=event.getSource();
		if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
		
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.i(TAG, "����--------------------��"+mCurrentUI);
			//AccessibilityHelper.recycle(mRootNode);
			//-------------------------Ⱥ��Ա�б����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_LIST_UI)){
				if(mQqTroopMemberList.bWorking){//������....
					if(mQqTroopMemberList.mIndex>=mQqTroopMemberList.mQqMembers.size()){//��ҳ���˽���
						mQqTroopMemberList.mMemberAdded=mQqTroopMemberList.mMemberAdded+mQqTroopMemberList.mQqMembers.size();//�ۻ���Ա
						if(mQqTroopMemberList.isTroopMemberListLastPage(mRootNode)){//�Ѿ������һҳ��
							mQqTroopMemberList.mIndex=0;
							mQqTroopMemberList.bWorking=false;//��������;
							Config.JobState=Config.STATE_NONE;
							fw.ShowFloatingWindow();
							String sShow="���μ�Ⱥ��Ա�ɹ���ɣ�";
							Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
							speaker.speak(sShow);
						}else{//��ҳ��
							AccessibilityHelper.performScrollForward2(mRootNode);//��ҳ
							Config.JobState=Config.STATE_SCROLL_PAGE;
							fw.ShowFloatingWindow();
						}//if(mQqTroopMemberList.isTroopMemberListLastPage(mRootNode)){//�Ѿ������һҳ��
						return;//��ҳ���˽���
					}//if(mQqTroopMemberList.mIndex>=mQqTroopMemberList.mQqMembers.size()){//��ҳ���˽���
					AccessibilityNodeInfo QqMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);

					if(!AccessibilityHelper.performClick(QqMember)){//���ʧ��ʱ��
						Log.i(TAG, "performClick Error:mChatroomInfo.mIndex="+mQqTroopMemberList.mIndex);
						Toast.makeText(context,  "ʧ����:"+mQqTroopMemberList.mIndex, Toast.LENGTH_LONG).show();
						mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
						QqMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);
						if(!AccessibilityHelper.performClick(QqMember)){//���ʧ��ʱ��
							Log.i(TAG, "�ڶ���performClick mQqTroopMemberList="+mQqTroopMemberList.mIndex);
							Toast.makeText(context,  "�ڶ���ʧ����:"+mQqTroopMemberList.mIndex, Toast.LENGTH_LONG).show();
						}
					}//if(!AccessibilityHelper.performClick(QqMember)){//���ʧ��ʱ��
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
					Log.i(TAG, ":"+mQqTroopMemberList.mIndex);
				}else{//����//if(mQqTroopMemberList.bWorking){//������....
					fw.ShowFloatingWindow();
				}////if(mQqTroopMemberList.bWorking){//������....
			}else{
				//4���ر�������ť��
				fw.DestroyFloatingWindow();
			}////if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_LIST_UI)){
			//-------------------------Ⱥ��Ա���Ͽ�����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_CARD_UI)){
				if(Config.JobState==Config.STATE_TROOP_MEMBER_CARD_UNADD){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "�Ӻ���",0);
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
				//--------------------------������ѷ��أ�------------------
				if(Config.JobState==Config.STATE_TROOP_MEMBER_CARD_ADDED){//���صĽ���
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
				//--------------------------������Ӻ��� ��------------------
				if(Config.JobState==Config.STATE_ADD_FRIEND_VERIFY){//������Ӻ��� 
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
			}//if(mCurrentUI.equals(Config.WINDOW_QQ_TROOP_MEMBER_CARD_UI)){
			//-------------------------��֤�������----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_ADD_FRIEND_VERIFY_UI)){
				
				
				if(Config.JobState==Config.STATE_ADD_FRIEND_VERIFY_FAIL){//���ʧ�ܣ�����Ƶ������
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
					return;
				}
				if(Config.JobState!=Config.STATE_ADD_FRIEND_VERIFY)return;
				//��Ҫ����𰸣�
				//mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "�����",0);
				if(mQqTroopMemberList.isAnswer(mRootNode)){
					//AccessibilityHelper.performBack(service);	
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ȡ��",0,true);
					if(mTargetNode==null){AccessibilityHelper.performBack(service);AccessibilityHelper.performBack(service);return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
					return;
				}
				Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
				//������Ϣ��
				mTargetNode=AccessibilityHelper.findNodeInfosByClassName(mRootNode, "android.widget.EditText", 0, true);
				if(mTargetNode==null){AccessibilityHelper.performBack(service);return;}
				if(mTargetNode.getText()==null){AccessibilityHelper.performBack(service);return;}
				String sayHi=mTargetNode.getText().toString();
				if(!sayHi.equals(Config.sAddFriendSay)){nodeInput(mTargetNode,Config.sAddFriendSay);}
				//�㷢�Ͱ�ť��
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "����",0,true);
				if(mTargetNode==null){AccessibilityHelper.performBack(service);return;}
				AccessibilityHelper.performClick(mTargetNode);
			}
			//-------------------------���ʧ�ܽ���----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_ADD_FRIEND_FAIL_UI)){
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "���ʧ�ܣ����Ժ�����",0,true);
				if(mTargetNode!=null){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ȷ��",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_ADD_FRIEND_VERIFY;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "���ʧ�ܣ�����Ƶ������",0,true);
				if(mTargetNode!=null){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ȷ��",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					//AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_ADD_FRIEND_VERIFY_FAIL;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "�Է����������쳣����Ϊ���Ѵ��ڰ�ȫ���ա�",0);
				if(mTargetNode!=null){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "����",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					//Config.JobState=Config.STATE_ADD_FRIEND_VERIFY;
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_ADDED;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "�޷����",0);
				if(mTargetNode!=null){//�Է�������ڰ�ȫ���գ���ʱ�޷���ӡ�
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ȷ��",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "������ʾ",0);
				if(mTargetNode!=null){//�Է���������ѱ����˾ٱ����������ӡ�
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ȡ��",0,true);
					if(mTargetNode==null){return;}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;
					return;
				}
			}
			//-------------------------Ⱥ���ý���----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_QQ_SETTING_TROOP_UI)){//��ȡȺ��Ա��������
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "����Ա",0);
				if(mTargetNode==null){return;}
				String MemberCount=mTargetNode.getText().toString();
				mQqTroopMemberList.mMemberCount=Integer.parseInt(MemberCount.substring(0,MemberCount.length()-3));
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
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
    //����Ӻţ�
    public boolean distributeClickJiaJob() {
    	mQqTroopMemberList.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mQqTroopMemberList.bWorking=false;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeClickJiaJob start:");
        if (rootNode == null) {Log.i(TAG, "distributeClickJiaJob start:rootNode == null");return false;}
        if(!mQqTroopMemberList.isQqTroopMemberListUi(rootNode)) {Log.i(TAG, "distributeClickJiaJob start:isChatroomInfoUi == false");return false;}

        List<AccessibilityNodeInfo> QqMembers=mQqTroopMemberList.getQqTroopMemberList(rootNode);
        if(QqMembers==null){Log.i(TAG, "distributeClickJiaJob QqMembers==null");return false;}//��ȡ��Ա�б� ��
        
        if(mQqTroopMemberList.mQqMembers.size()==0){Log.i(TAG, "distributeClickJiaJob start:mQqTroopMemberList.size()==0");return false;}
        
        
        AccessibilityNodeInfo QqFirstMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);
        //if(chatFirstMember==null){Log.i(TAG, "distributeClickJiaJob start:chatFirstMember==null");return false;}
        
        if(!AccessibilityHelper.performClick(QqFirstMember)){
        	Log.i(TAG, "distributeClickJiaJob start:performClick(QqFirstMember) == false");
        	mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
        	QqFirstMember=mQqTroopMemberList.mQqMembers.get(mQqTroopMemberList.mIndex);
        	if(!AccessibilityHelper.performClick(QqFirstMember)){//����ڶ� ����Ա��
        		Log.i(TAG, "distributeClickJiaJob start2:performClick(QqFirstMember) == false");
        		return false;
        	}
        	
        }//�����һ����Ա��
        mQqTroopMemberList.mIndex=mQqTroopMemberList.mIndex+1;
        mQqTroopMemberList.bWorking=true;
        Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;//TroopMemberCard
		String sShow="���μ�Ⱥ��Ա����"+String.valueOf(QqMembers.size())+"�ˡ���Ⱥ������"+mQqTroopMemberList.mMemberCount+"���Ѽ�������"+mQqTroopMemberList.mMemberAdded+"����ʼ�����ˣ����Ժ�";
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
        return true;
    }
    public boolean distributeAddQqMembers(){
		boolean bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="�����Ⱥ��Ա�б���棡���ܿ�ʼ��Ⱥ���ѡ�";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_TROOP_MEMBER_CARD_UNADD;//���������ı�״̬��
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
    		////ճ����������  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	
    	return false;
    }
}
