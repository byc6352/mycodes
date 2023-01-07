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
	//消息定义：
	private static final int  MSG_RESULT=0x16;
	public Context context;
	private SpeechUtil speaker ;

	//执行成功定义：
	public boolean bSuccess=false;
	//输入密码延时：
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
		//接收广播消息
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
   					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//7系统：
   						say="需要计算埋雷数据！才能百分百出雷！";
   					}else{
   						if(ConfigCt.bRoot)
   							say="需要计算埋雷数据！才能百分百出雷！";
   						else
   							say="本机未ROOT！出雷机率减小！";
   					}
   					Toast.makeText(context,say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
   				}else{//未注册：
					Toast.makeText(context, "本机未授权！", Toast.LENGTH_LONG).show();
					say="您是试用版用户！需要计算埋雷数据，或进行授权成为正版，才能百分百出雷！";
					if(bSuccess)say="您是试用版用户！需要计算埋雷数据，或进行授权成为正版，才能百分百出雷！";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
               }
               Config.JobState=Config.STATE_INPUT_CLOSING;
            }  
        }  
  
    }; 
    /*自动输入密码开始*/
    public boolean autoPutPWD(){
    	PutPWDdelay();
    	bSuccess=false;
    	return true;
    }
    /*延时：*/
    private void PutPWDdelay() {
    	speaker.speak("正在为您分配红包金额：");
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=100;
    	fwp.mSendMsg=Config.ACTION_PUT_PWD_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    }
    /*开始输入密码：*/
	private BroadcastReceiver putPWDdelayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(Config.ACTION_PUT_PWD_DELAY.equals(action)) {
            	//开始输入密码：
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

