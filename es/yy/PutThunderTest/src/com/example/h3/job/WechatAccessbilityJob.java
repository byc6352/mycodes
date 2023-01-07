/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.PutThunderTest.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.FloatingWindowPic;
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
	private LuckyMoneyDetailJob mDetailJob;

	//----------------------------------------------------------------------------------------
	private FloatingWindowPic fws1;//��ʾͼƬ��������
	private FloatingWindowPic fws2;//��ʾͼƬ��������
	private FloatingWindowPic fws3;//��ʾͼƬ��������
	private FloatingWindowPic fws4;//��ʾͼƬ��������
	private FloatingWindowPic fws5;//��ʾͼƬ��������
	
	private FloatingWindowPic fws6;//��ʾͼƬ��������
	private FloatingWindowPic fws7;//��ʾͼƬ��������
	private FloatingWindowPic fws8;//��ʾͼƬ��������
	private FloatingWindowPic fws9;//��ʾͼƬ��������
	private FloatingWindowPic fws10;//��ʾͼƬ��������
	
	private FloatingWindowPic fws11;//��ʾͼƬ��������
	private FloatingWindowPic fws12;//��ʾͼƬ��������
	private FloatingWindowPic fws13;//��ʾͼƬ��������
	private FloatingWindowPic fws14;//��ʾͼƬ��������
	private FloatingWindowPic fws15;//��ʾͼƬ��������
	private int i=1;

    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);
        //ע��context�ı仯��
        //mReceiveJob=LuckyMoneyReceiveJob.getLuckyMoneyReceiveJob(context);
        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
        mPWDJob=LuckyMoneyPWDJob.getLuckyMoneyPWDJob(context);
        mLauncherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mPrepareJob=LuckyMoneyPrepareJob.getLuckyMoneyPrepareJob(context);
        speeker=SpeechUtil.getSpeechUtil(context);
        fw=FloatingWindow.getFloatingWindow(this);
        mVolumeChange=VolumeChangeListen.getVolumeChangeListen(this);
        //mVolumeChange.onVolumeChangeListener();
        

        
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
    
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	
    	final int eventType = event.getEventType();
    	String sShow="";
    	//String sClassName=event.getClassName().toString();
		//Log.d(TAG, "�¼�---->" + event);
		if(Config.DEBUG){
			//String ui=event.getClassName().toString();
			//Log.d(TAG, "�¼�---->" + ui);
			//if(ui.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")){
				//UnpackLuckyMoney() ;
			//}
		}
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//��ȡ�������ƣ�
			mCurrentUI=event.getClassName().toString();
			//ͼƬ�滻��
			mDetailJob.ReplacePics(event);
			//++++++++++++++++++++++++++++++Ԥ������+++++++++++++++++++++++++++++++++++++++++++++++++
			//1���Ƿ������壺
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
				//������ɣ�
				if(bComp){
					sShow="������ɣ�";
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					bComp=false;
				}
				//2����ʾ�������壨����Ӻţ���
				fw.ShowFloatingWindow();
				//3����������ť��
				//if(!ClickLuckyMoney(event)){Config.bSendLuckyMoney=false;return;}
			}else  {
				//4���ر�������ť��
				fw.DestroyFloatingWindow();
				//5�����Ƿ���״̬���˳���
				//if(Config.JobState!=Config.STATE_SENDING_LUCKYMONEY)return;
			}
			//6���Ƿ���״̬��
			//++++++++++++++++++++++++++++++�����ı���+++++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
				//2.3�����ı���
				if(Config.currentapiVersion>=Build.VERSION_CODES.LOLLIPOP_MR1){
					if(!mPrepareJob.InputText2(event)){Config.JobState=Config.STATE_NONE;return;}
				}else{
					if(!mPrepareJob.InputText(event)){Config.JobState=Config.STATE_NONE;return;}
				}
				return;
				
			}
			//+++++++++++++++++++++++++++++��������++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_WALLETPAY_UI)){
				//2.4�������룺
				bComp=mPWDJob.putPWD();
				Config.JobState=Config.STATE_NONE;
				return;
			}
			//++++++++++++++++++++++++++++��ʾ��ϸ��Ϣ++++++++++++++++++++++++++++++++++++++++++++++
			if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
				//AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
				//AccessibilityHelper.recycle(rootNode);
			}
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//++++++++++++++++++++++++++++���ݱ仯 ++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			return;
		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
		//}//if(WECHAT_PACKAGENAME.equals(pkn))
    }
    
   
    
   
    //����Ӻţ�
    public boolean distributeClickJiaJob() {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {return false;}
        if(!mLauncherJob.isMemberChatUi(rootNode))return false;
        if(!mLauncherJob.ClickJia(rootNode))return false;
        return mLauncherJob.ClickLuckyMoney(rootNode);
    }


}
