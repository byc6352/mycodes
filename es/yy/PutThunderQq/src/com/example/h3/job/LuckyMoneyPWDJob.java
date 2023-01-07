/**
 * 
 */
package com.example.h3.job;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.byc.PutThunderQq.R;
import util.BackgroundMusic;
import util.ConfigCt;

import com.example.h3.Config;
import accessibility.QiangHongBaoService;

import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;

import util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyPWDJob {
	private static LuckyMoneyPWDJob current;
	public static String TAG = "byc001";	
	//��Ϣ���壺
	private static final int  MSG_RESULT=0x16;
	public Context context;
	private SpeechUtil speaker ;

	//ִ�гɹ����壺
	public boolean bSuccess=false;
	//����������ʱ��
	private FloatingWindowPic fwp;
	
	private BackgroundMusic mBackgroundMusic;
	
	private LuckyMoneyPWDJob(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//
		//fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int LayoutID=util.ResourceUtil.getLayoutId(context, "float_click_delay_show");
		fwp=FloatingWindowPic.getFloatingWindowPic(context,LayoutID);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//���չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_PUT_PWD_DELAY);
        context.registerReceiver(putPWDdelayReceiver, filter);
	}
    public static synchronized LuckyMoneyPWDJob getLuckyMoneyPWDJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPWDJob(context);
        }
        current.context=context;
        return current;
        
    }
    public Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == MSG_RESULT) {
            	mBackgroundMusic.stopBackgroundMusic();
            	bSuccess=true;
            	if(msg.arg1==0)bSuccess=false;
            	String say="";
            	if(Config.bReg){
   					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//7ϵͳ��
   						say="��Ҫ�����������ݣ����ܰٷְٳ��ף�";
   					}else{
   						if(ConfigCt.bRoot)
   							say="��Ҫ�����������ݣ����ܰٷְٳ��ף�";
   						else
   							say="����δROOT�����׻��ʼ�С��";
   					}
   					Toast.makeText(context,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
   				}else{//δע�᣺
					Toast.makeText(context, "����δ��Ȩ��", Toast.LENGTH_LONG).show();
					say="�������ð��û�����Ҫ�����������ݣ��������Ȩ��Ϊ���棬���ܰٷְٳ��ף�";
					if(bSuccess)say="�������ð��û�����Ҫ�����������ݣ��������Ȩ��Ϊ���棬���ܰٷְٳ��ף�";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
               }
               Config.JobState=Config.STATE_INPUT_CLOSING;
            }  
        }  
  
    }; 
    /*�Զ��������뿪ʼ*/
    public boolean autoPutPWD(){
    	PutPWDdelay();
    	bSuccess=false;
    	return true;
    }
    /*��ʱ��*/
    private void PutPWDdelay() {
    	speaker.speak("����Ϊ����������");
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=100;
    	fwp.mSendMsg=Config.ACTION_PUT_PWD_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    }
    /*��ʼ�������룺*/
	private BroadcastReceiver putPWDdelayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(Config.ACTION_PUT_PWD_DELAY.equals(action)) {
            	//��ʼ�������룺
            	clickPWDThread();
            }
        }
    };
    public void clickPWDThread(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//Looper.prepare();
				try {
					boolean result=clickPWD();
	                 Message msg = new Message();  
	                 msg.what =MSG_RESULT;
	                 if(result)msg.arg1=1;else msg.arg1=0;
	                 mHandler.sendMessage(msg); 
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
				//Looper.loop(); 
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    public boolean clickPWD(){
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
    		return InputPWDqq.getInputPWD(context).input(Config.sPWD);
    	else
    		return InputPWDqq.getInputPWD(context).input7(Config.sPWD);
    	}
	}

