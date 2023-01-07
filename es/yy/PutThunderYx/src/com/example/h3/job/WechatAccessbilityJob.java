/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.PutThunderYx.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.VolumeChangeListen;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
/**
 * @author byc
 *
 */
public class WechatAccessbilityJob extends BaseAccessbilityJob  {
	
	private static WechatAccessbilityJob current;
	//---------------------------------------------------------------------------
	private SpeechUtil speeker ;
	private FloatingWindow fw;//��ʾ��������
	private boolean bComp=false;
	private String mCurrentUI="";
	private LuckyMoneyPWDJob mPWDJob;
	private LuckyMoneyLauncherJob mLauncherJob;
	private LuckyMoneyPrepareJob mPrepareJob;
	private VolumeChangeListen mVolumeChange;
	private LuckyMoneyReceiveJob mReceiveJob;
	private LuckyMoneyDetailJob mDetailJob;
	AccessibilityNodeInfo mRootNode;

	//----------------------------------------------------------------------------------------
    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        //ע��context�ı仯��
        mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        speeker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
        mVolumeChange.onVolumeChangeListener();
    }
    @Override
    public void onStopJob() {
    	mVolumeChange.onDestroy();
    	//mVolumeChange.
    }
    public static synchronized WechatAccessbilityJob getJob() {
        if(current == null) {
            current = new WechatAccessbilityJob();
        }
        return current;
    }
    
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	
    	final int eventType = event.getEventType();
    	String sShow="";

		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//��ȡ�������ƣ�
			mCurrentUI=event.getClassName().toString();
			Log.i(TAG, mCurrentUI);
			//+++++++++++++++++++++++++++++++++1���Ƿ������壺++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//������ɣ�
				//Config.JobState=Config.STATE_INPUT_CLOSING;//����
				if(Config.JobState==Config.STATE_INPUT_CLOSING){
					if(mPWDJob.bSuccess){
						sShow="���׳ɹ���ɣ�";
						mPWDJob.bSuccess=false;
					}else{
						sShow="������ɣ�";
					}
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					if(Config.bRobTail){//����ɨβ״̬
					mRootNode=event.getSource();
					if(mRootNode==null){Config.JobState=Config.STATE_NONE;return;}
					if(mLauncherJob.getMyNode(mRootNode)!=null){
						Config.JobState=Config.STATE_INPUT_ROB_TAIL;
						Config.robTailAction=Config.ACTION_LOOK;
						//AccessibilityHelper.performClick(mLauncherJob.mMyNode);
						sShow="���ڽ���ɨβ����..,�������ӿɹر�ɨβ������";
						Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
						speeker.speak(sShow);
					}
					}
					
				}
				if(Config.JobState==Config.STATE_INPUT_ROB_TAIL){//����ɨβ״̬;
					if(Config.robTailAction==Config.ACTION_CLOSE){//�˳�ɨβ״̬��
						Config.JobState=Config.STATE_NONE;
						return;
					}
					AccessibilityHelper.performClick(mLauncherJob.mMyNode);//�����������棻

				}
				//Config.JobState=Config.STATE_NONE;
				//2����ʾ�������壨����Ӻţ���
				fw.ShowFloatingWindow();
				//3����������ť��
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4���ر�������ť��
				fw.DestroyFloatingWindow();
				//5�����Ƿ���״̬���˳���
				if(Config.JobState==Config.STATE_NONE)return;
			}
			//6���Ƿ���״̬��
			//+++++++++++++++++++++++++++++++++�����ı���++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3�����ı���
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
				
			}
			//+++++++++++++++++++++++++++++��������++++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)||mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLET_UI)){
				//2.4�������룺
				if(Config.JobState!=Config.STATE_INPUT_PWD)return;
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
				//AccessibilityHelper.recycle(rootNode);
				mPWDJob.putPWD(mRootNode);
				Config.JobState=Config.STATE_INPUT_CLOSING;
				return;
			}
			//WINDOW_LUCKYMONEY_CHATROOM_UI
			//+++++++++++++++++++++++++++++Ⱥ����Ϣ+++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_CHATROOM_UI)){
				if(Config.JobState!=Config.STATE_INPUT_GROUP)return;
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
				AccessibilityHelper.recycle2(mRootNode);
			}
			//+++++++++++++++++++++++++++++�鿴����Ԥ����Ϣ+++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_RECEIVEUI)){
				if(Config.JobState!=Config.STATE_INPUT_ROB_TAIL)return;
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
		        if(Config.robTailAction==Config.ACTION_LOOK){
		        	if(mReceiveJob.getLookLuckyNode(mRootNode)!=null){
		        		AccessibilityHelper.performClick(mReceiveJob.mLookLuckyNode);
		        	}//if(mReceiveJob.getLookLuckyNode(mRootNode)!=null){
		        }// if(Config.robTailAction==Config.ACTION_LOOK){
		        if(Config.robTailAction==Config.ACTION_CLICK){
		        	mReceiveJob.OpenLuckyMoney(mRootNode);
		        	//Config.JobState=Config.STATE_NONE;
		        }
				if(Config.robTailAction==Config.ACTION_CLOSE){//�˳�ɨβ״̬��
					Config.JobState=Config.STATE_NONE;
					fw.ShowFloatingWindow();
					return;
				}
			}
			//+++++++++++++++++++++++++++++�鿴������ϸ��Ϣ+++++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				if(Config.JobState!=Config.STATE_INPUT_ROB_TAIL)return;
				if(Config.robTailAction==Config.ACTION_CLOSE){//�˳�ɨβ״̬��
					Config.JobState=Config.STATE_NONE;
					return;
				}
				mRootNode= event.getSource();
		        if (mRootNode == null) {return; }
		        //AccessibilityHelper.recycle2(mRootNode);
		        mDetailJob.getRobLuckyMoney(mRootNode);
		        AccessibilityHelper.performBack(service);
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
			

    }
    /*
	    public boolean distributePutThunder(){
			boolean bWindow=distributeClickJiaJob();
			if(!bWindow){
				String sShow="�����Ҫ���׵ĺ��Ⱥ�����ܿ�ʼ���ס�";
				Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
				speeker.speak(sShow);
				fw.DestroyFloatingWindow();
				return false;
			}else{
				Config.JobState=Config.STATE_INPUT_TEXT;//���������ı�״̬��
				return true;
			}
	    }
	    */
   
    //����Ӻţ�
    public boolean distributePutThunder() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        //if(!mLauncherJob.isMemberChatUi(rootNode))return false;
        fw.DestroyFloatingWindow();
        String say="";
        String groupName=mLauncherJob.getGroupName(rootNode);
        if(groupName.equals("")){
        	say="�����Ҫ���׵ĺ��Ⱥ�����ܿ�ʼ���ס�";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
        	return false;//����Ⱥ��
        }
        String groupNames=getConfig().getGroupName();//ȡ�ѱ����Ⱥ�����ƣ�
        Log.i(TAG, groupName);
        if(groupNames.indexOf(groupName)!=-1){//û�б������
        	mLauncherJob.ClickGroup(rootNode);//���Ⱥ�ģ�
        	Config.JobState=Config.STATE_INPUT_GROUP;//��������Ⱥ��״̬��
        	say="����׼����������....���Ժ�...";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
			return true;
        }
        //���+�ţ�
        if(!mLauncherJob.ClickJia(rootNode)){
        	say="�����Ҫ���׵ĺ��Ⱥ�����ܿ�ʼ���ס�";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
        	return false;//����Ⱥ��
        }
        //��������ť��
        if(!mLauncherJob.ClickLuckyMoney(rootNode)){
        	say="�����Ҫ���׵ĺ��Ⱥ�����ܿ�ʼ���ס�";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
        	return false;//����Ⱥ��
        }
        Config.JobState=Config.STATE_INPUT_TEXT;
        return true;
    }


}
