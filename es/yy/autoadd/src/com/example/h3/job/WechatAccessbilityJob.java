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
	//-------------------------------�����ʱ---------------------------------------------

	private SpeechUtil speaker ;
	private BackgroundMusic mBackgroundMusic;

	public int mLuckyMoneyType=0;//�������Ѳ���������������װ���
	private LuckyMoneyLauncherJob mLauncherJob;

	private String mCurrentUI="";
	private AccessibilityNodeInfo mRootNode; 
	private boolean bDel=false;//ɾ�������
	
	private FloatingWindow fw;//��ʾ��������
	private ChatroomInfo mChatroomInfo;//Ⱥ����Ϣ����
	private SnsUser mSnsUser;//������ᴰ��
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
		//++++++++++++++++++++++++++++++++++++����ı�+++++++++++++++++++++++++++++++++++++++++++++++++
    	Log.i(TAG, "sClassName:"+sClassName);
		mRootNode=event.getSource();
		if(mRootNode==null){Log.i(TAG, sClassName+":mRootNode==null");return ;}
		//AccessibilityHelper.printParentInfo(mRootNode);
		//mRootNode=AccessibilityHelper.getRootNode(mRootNode);
		
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			Log.i(TAG, "����--------------------��"+mCurrentUI);
			AccessibilityHelper.recycle(mRootNode);
			//�����˵����壺-----------------ɾ�� �����---------------------------------------------------------
			if(bDel){
			if(mCurrentUI.equals("com.tencent.mm.ui.base.k")){
				AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
				if(adPop!=null){
					AccessibilityNodeInfo parent=adPop.getParent();
					if(parent!=null)parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					bDel=false;
				}
			}
			}
			//-------------------------Ⱥ�ĺ��ѽ���----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_CHATROOM_CHATROOMINFO_UI)){
				//��ʾ��ť ��
				//2����ʾ�������壨����Ӻţ���
				if(mChatroomInfo.bWorking){//������....
					//List<AccessibilityNodeInfo> chatMembers=mChatroomInfo.getChatMembers(mRootNode);//���»�ȡ��Ա�б�
			        //if(chatMembers==null){Log.i(TAG, "mChatroomInfo.bWorking:chatMembers==null");return ;}//��ȡ��Ա�б� ��
			        //if(mChatroomInfo.mChatMembers.size()==0){Log.i(TAG, "mChatroomInfo.bWorking:mChatMembers.size()==0");return ;}
			        
					if(mChatroomInfo.mIndex>=mChatroomInfo.mChatMembers.size()){//��ҳ���˽���
						if(mChatroomInfo.isChatroomInfoLastPage(mRootNode)){//�Ѿ������һҳ��
							mChatroomInfo.mIndex=0;
							mChatroomInfo.bWorking=false;//��������;
							Config.JobState=Config.STATE_NONE;
							fw.ShowFloatingWindow();
							String sShow="���μ�Ⱥ��Ա�ɹ���ɣ�";
							Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
							speaker.speak(sShow);
						}else{//��ҳ��
							AccessibilityHelper.performScrollForward(mRootNode);//��ҳ
							Config.JobState=Config.STATE_SCROLL_PAGE;
							fw.ShowFloatingWindow();

						}
						return;
					}//if(mChatroomInfo.mIndex>=mChatroomInfo.mChatMembers.size()){//����
					//Toast.makeText(context, ":"+mChatroomInfo.mIndex, Toast.LENGTH_SHORT).show();
					
					AccessibilityNodeInfo chatMember=mChatroomInfo.mChatMembers.get(mChatroomInfo.mIndex);

					if(!AccessibilityHelper.performClick(chatMember)){//���ʧ��ʱ��
						Log.i(TAG, "performClick Error:mChatroomInfo.mIndex="+mChatroomInfo.mIndex);
						Toast.makeText(context,  "ʧ����:"+mChatroomInfo.mIndex, Toast.LENGTH_LONG).show();
						mChatroomInfo.mIndex=mChatroomInfo.mIndex+1;
						chatMember=mChatroomInfo.mChatMembers.get(mChatroomInfo.mIndex);
						if(!AccessibilityHelper.performClick(chatMember)){//���ʧ��ʱ��
							Log.i(TAG, "�ڶ���performClick Error:mChatroomInfo.mIndex="+mChatroomInfo.mIndex);
							Toast.makeText(context,  "�ڶ���ʧ����:"+mChatroomInfo.mIndex, Toast.LENGTH_LONG).show();
						}
					}
					Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
					mChatroomInfo.mIndex=mChatroomInfo.mIndex+1;
					Log.i(TAG, ":"+mChatroomInfo.mIndex);

				}else{//����
					fw.mFbtFunc1.setText("��Ⱥ����");
					fw.ShowFloatingWindow();
				}
			}else{
				//4���ر�������ť��
				//fw.DestroyFloatingWindow();
			}//if(mCurrentUI.equals(Config.WINDOW_CHATROOM_CHATROOMINFO_UI)){
			//-------------------------��Ӻ�����ϸ��Ϣ����----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_CONTACT_CONTACTINFO_UI)){
				if(Config.JobState==Config.STATE_CONTACT_INFO_UNADD){
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "��ӵ�ͨѶ¼",0);
					//if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
					if(mTargetNode==null){
						Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
						AccessibilityHelper.performBack(service);
						return;
					}
					AccessibilityHelper.performClick(mTargetNode);
					Config.JobState=Config.STATE_SAYHI_PERMISSION;
				}
				
				if(Config.JobState==Config.STATE_CONTACT_INFO_ADDED){//���صĽ���
					AccessibilityHelper.performBack(service);
					Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
				}
			}
			//-------------------------��֤�������----------------------------------------------------
			if(mCurrentUI.equals(Config.WINDOW_SAYHI_PERMISSION_UI)){
				if(Config.JobState!=Config.STATE_SAYHI_PERMISSION)return;
				mTargetNode=AccessibilityHelper.findNodeInfosByClassName(mRootNode, "android.widget.EditText", 0, true);
				if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
				if(mTargetNode.getText()==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
				String sayHi=mTargetNode.getText().toString();
				if(!sayHi.equals(Config.sAddFriendSay)){nodeInput(mTargetNode,Config.sAddFriendSay);}
				mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "����",0,true);
				if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
				AccessibilityHelper.performClick(mTargetNode);
				Config.JobState=Config.STATE_CONTACT_INFO_ADDED;
			}
			//-------------------------�������----------------------------------------------------
			if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){
				if(mSnsUser.bWorking&&Config.JobState==Config.STATE_OPEN_GALLERY){//������....
					fw.ShowFloatingWindow();
					if(mSnsUser.mIndex>=mSnsUser.mGallerys.size()){//��ҳ���˽���
						if(mSnsUser.isGalleryLastPage(mRootNode)){//�Ѿ������һҳ��
							mSnsUser.mIndex=0;
							mSnsUser.bWorking=false;//��������;
							Config.JobState=Config.STATE_NONE;
							fw.ShowFloatingWindow();
							String sShow="���δ���Ƶ�ɹ���ɣ�";
							Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
							speaker.speak(sShow);
						}else{//��ҳ��
							//AccessibilityHelper.performScrollForward2(mRootNode);//��ҳ
							mSnsUser.ScrollPage(mRootNode);
							Config.JobState=Config.STATE_SCROLL_PAGE;
							fw.ShowFloatingWindow();

						}
						return;//��ҳ����������Ƶ
					}//��ҳ���˽���
					AccessibilityNodeInfo galleryMember=mSnsUser.mGallerys.get(mSnsUser.mIndex);//��ȡ��һ����Ƶ

					if(!AccessibilityHelper.performClickSingle(galleryMember)){//���ʧ��ʱ��
						Log.i(TAG, "performClick Error:mSnsUser.mIndex="+mSnsUser.mIndex);
					}
					Config.JobState=Config.STATE_OPEN_GALLERY;
					mSnsUser.mIndex=mSnsUser.mIndex+1;
					Log.i(TAG, ":"+mSnsUser.mIndex);
				}else{//����
					fw.mFbtFunc1.setText("���δ������Ƶ");
					fw.ShowFloatingWindow();
				}
			}else{
				//4���ر�������ť��
				//fw.DestroyFloatingWindow();
			}//if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){
			
			//-------------------------��Ƶ����----------------------------------------------------
			if(mCurrentUI.equals(VersionParam.WINDOW_SNSGALLERY_UI)){
				//fw.ShowFloatingWindow();
				if(Config.JobState==Config.STATE_OPEN_GALLERY){
					
					mTargetNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "����",0);
					//if(mTargetNode==null){Config.JobState=Config.STATE_NONE;mChatroomInfo.bWorking=false;return;}
					if(mTargetNode==null){
						Config.JobState=Config.STATE_OPEN_GALLERY;
						//AccessibilityHelper.performBack(service);
						return;
					}
					//�������ڣ�
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
		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
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
				if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){//������ᴰ��
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
				if(mSnsUser.isScrollGalleryNeedDate(mRootNode)){//����Ŀ�����ڣ�
					mSnsUser.bWorking=false;
					Config.JobState=Config.STATE_NONE;
					String say="�ѹ�����Ҫ��ĵط�������������";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{//����������
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
						//�������ڣ�
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
					//ʵ�ֳ���ָ�� ���ݣ�----------------------------����ɾ���˵�-------------------------------------
					AccessibilityNodeInfo adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mLauncherJob.mStrAD ,-1);
					if(adNode!=null){
						if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
					}
					//----------------------------------------------------------------------------------
					mLauncherJob.mIntAD=mLauncherJob.mIntAD+1;
					if(mLauncherJob.mIntAD>mLauncherJob.MAX_NO_REG_AD)mLauncherJob.SendAD(mRootNode);//���ð淢����棻
					if(!Config.bSwitch)return;//�ܿ��عرգ�
					if(clickLuckyMoney(mRootNode))Config.JobState=Config.STATE_CLICK_LUCKYMONEY;			
				}
				//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
			}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
			*/
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//}//if(WECHAT_PACKAGENAME.equals(pkn))
    }
  
    //����Ӻţ�
    public boolean distributeClickJiaJob() {
    	mChatroomInfo.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mChatroomInfo.bWorking=false;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeClickJiaJob start:");
        if (rootNode == null) {Log.i(TAG, "distributeClickJiaJob start:rootNode == null");return false;}
        if(!mChatroomInfo.isChatroomInfoUi(rootNode)) {Log.i(TAG, "distributeClickJiaJob start:isChatroomInfoUi == false");return false;}

        List<AccessibilityNodeInfo> chatMembers=mChatroomInfo.getChatMembers(rootNode);
        if(chatMembers==null){Log.i(TAG, "distributeClickJiaJob start:chatMembers==null");return false;}//��ȡ��Ա�б� ��
        
        if(mChatroomInfo.mChatMembers.size()==0){Log.i(TAG, "distributeClickJiaJob start:mChatMembers.size()==0");return false;}
        
        AccessibilityNodeInfo chatFirstMember=mChatroomInfo.mChatMembers.get(mChatroomInfo.mIndex);
        //if(chatFirstMember==null){Log.i(TAG, "distributeClickJiaJob start:chatFirstMember==null");return false;}
        
        if(!AccessibilityHelper.performClick(chatFirstMember)){Log.i(TAG, "distributeClickJiaJob start:performClick == false");return false;}//�����һ����Ա��
        mChatroomInfo.mIndex=mChatroomInfo.mIndex+1;
        mChatroomInfo.bWorking=true;
        Config.JobState=Config.STATE_CONTACT_INFO_UNADD;
		String sShow="���μ�Ⱥ��Ա����"+String.valueOf(chatMembers.size())+"�ˡ���ʼ�����ˣ����Ժ�";
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
        return true;
    }
    //��ʼ����Ƶ��
    public boolean distributeGetGallerys() {
    	mSnsUser.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mSnsUser.bWorking=false;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeGetGallerys start:");
        if (rootNode == null) {Log.i(TAG, "distributeGetGallerys start:rootNode == null");return false;}
        List<AccessibilityNodeInfo> galleryMembers=mSnsUser.getGalleryMembers(rootNode);
        if(galleryMembers==null){Log.i(TAG, "distributeGetGallerys start:galleryMembers==null");return false;}//��ȡ��Ա�б� ��
        if(mSnsUser.mGallerys.size()==0){Log.i(TAG, "distributeGetGallerys start:galleryMembers.size()==0");return false;}
        
        AccessibilityNodeInfo galleryFirstMember=mSnsUser.mGallerys.get(mSnsUser.mIndex);
        if(!AccessibilityHelper.performClick(galleryFirstMember)){Log.i(TAG, "distributeGetGallerys start:performClick == false");return false;}//�����һ����Ա��
        mSnsUser.mIndex=mSnsUser.mIndex+1;
        mSnsUser.bWorking=true;
        Config.JobState=Config.STATE_OPEN_GALLERY;
		String sShow="���δ���Ƶ����"+String.valueOf(galleryMembers.size())+"������ʼ����Ƶ�ˣ����Ժ�";
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
        return true;
    }
    /*��ʼ�������*/
    public boolean distributeScollGallery() {
    	Config.JobState=Config.STATE_SCROLL_GALLERY;
    	mSnsUser.bWorking=true;
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        Log.i(TAG, "distributeScollGallery start:");
        if (rootNode == null) {Log.i(TAG, "distributeScollGallery start:rootNode == null");return false;}
    	mSnsUser.ScrollPage(rootNode);
    	return true;
    }
    /*��ʼ������Ƶ*/
    public boolean distributeSwipeGallery() {
        //AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        //Log.i(TAG, "distributeSwipeGallery start:");
        //if (rootNode == null) {Log.i(TAG, "distributeSwipeGallery start:rootNode == null");return false;}
    	//mSnsUser.ScrollPage(rootNode);
		boolean bWindow=mSnsUser.isSnsUserUI(mCurrentUI);
		if(!bWindow){
			String sShow="���ȴ���ᣬ���ܻ�����";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			return false;
		}
		AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
	    Log.i(TAG, "distributeSwipeGallery start:");
	    if (rootNode == null) {Log.i(TAG, "distributeSwipeGallery start:rootNode == null");return false;}
	    List<AccessibilityNodeInfo> galleryMembers=mSnsUser.getGalleryMembers(rootNode);
	    if(galleryMembers==null){Log.i(TAG, "distributeSwipeGallery start:galleryMembers==null");return false;}//��ȡ��Ա�б� ��
	    if(mSnsUser.mGallerys.size()==0){Log.i(TAG, "distributeSwipeGallery start:galleryMembers.size()==0");return false;}
	        
	    AccessibilityNodeInfo galleryLastMember=galleryMembers.get(galleryMembers.size()-1);
	    if(!AccessibilityHelper.performClick(galleryLastMember)){Log.i(TAG, "distributeSwipeGallery start:performClick == false");return false;}//������һ����Ա��
		//if(mSnsUser.SwipeGallery()){
			Config.JobState=Config.STATE_SWIPE_GALLERY;
			mSnsUser.bWorking=true;
		//}else{
			//String sShow="����ִ��ʧ�ܣ�";
			//Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			//speaker.speak(sShow);
			//return false;
		//}
    	return true;
    }
    //����Ƶ�ɹ���
    public boolean distributeOpenGallery() {
		boolean bWindow=distributeGetGallerys();
		if(!bWindow){
			String sShow="�������������棡���ܿ�ʼ����Ƶ��";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_OPEN_GALLERY;//���������ı�״̬��
			mSnsUser.bWorking=true;
			return true;
		}
    }
    /*ֹͣ����*/
    public boolean distributeStopJob(){
    	mSnsUser.mIndex=0;
    	Config.JobState=Config.STATE_NONE;
    	mSnsUser.bWorking=false;
    	String say="��ֹͣ�����������ɹ���ɣ�";
		Toast.makeText(context, say, Toast.LENGTH_LONG).show();
		speaker.speak(say);
		return true;
    }
    /*��ʼ���΢��Ⱥ��Ա*/
    public boolean distributeChatJob(int iFunc){
    	if(mCurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI)){//������ᴰ��
    		if(iFunc==1)return distributeOpenGallery();
    		if(iFunc==2)return distributeScollGallery();
    		if(iFunc==3)return distributeStopJob();
    		if(iFunc==4)return distributeSwipeGallery();
    	}
    	if(mCurrentUI.equals(VersionParam.WINDOW_CHATROOM_CHATROOMINFO_UI)){//������ᴰ��
    		return distributeAddChatMembers();
    	}
    	return false;
    }
    /*��ʼ���΢��Ⱥ��Ա*/
    public boolean distributeAddChatMembers(){
		boolean bWindow=distributeClickJiaJob();
		if(!bWindow){
			String sShow="�����Ⱥ��Ϣ���棡���ܿ�ʼ��Ⱥ���ѡ�";
			Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
			speaker.speak(sShow);
			fw.DestroyFloatingWindow();
			return false;
		}else{
			Config.JobState=Config.STATE_CONTACT_INFO_UNADD;//���������ı�״̬��
			mChatroomInfo.bWorking=true;
			return true;
		}
    }
    //����Ӻţ�
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
    		////ճ����������  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	
    	return false;
    }
	
}
