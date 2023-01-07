package com.example.h3.job;

import com.byc.PutThunderQqPP.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.util.FloatingWindowPic;
import com.example.h3.util.FloatingWindowText;

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
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class LuckyMoneyPrepareJob {
	private static LuckyMoneyPrepareJob current;
	private Context context;
	private static String TAG = "byc001";
	private SpeechUtil speeker ;
	//执行成功定义：
	public boolean bSuccess=false;
	//输入字符延时：
	private FloatingWindowPic fwp;
	//
	private FloatingWindowText fwt;//显示浮动窗口显示雷值
	
	private BackgroundMusic mBackgroundMusic;
	//根结点：
	private AccessibilityNodeInfo mRootNode;
	
	private LuckyMoneyPrepareJob(Context context) {
		this.context = context;
		TAG=Config.TAG;
		speeker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//输入字符延时：
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//显示雷值窗口：
		fwt=FloatingWindowText.getFloatingWindowText(context);
		//fwt.SetFloatViewPara(100, 200,w,w);
		//接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_CALC_THUNDER_DELAY);
        filter.addAction(Config.ACTION_CALC_THUNDER_SHOW);
        context.registerReceiver(calcThunderReceiver, filter);
	}
    public static synchronized LuckyMoneyPrepareJob getLuckyMoneyPrepareJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPrepareJob(context);
        }
        current.context=context;
        return current;
        
    }
    //输入延时：
    private void CalcThunderDelay() {
    	speeker.speak("正在为您计算最佳雷值：");
        //播放背景音乐：
        mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
        //显示延时窗口
    	fwp.c=30;
    	fwp.mSendMsg=Config.ACTION_CALC_THUNDER_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_RED;
    	fwp.ShowFloatingWindow();
    }
    //接收延时结束消息：
  	private BroadcastReceiver calcThunderReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {

              String action = intent.getAction();
              //Log.d(TAG, "receive-->" + action);
              //计算延时结束处理：
              if(Config.ACTION_CALC_THUNDER_DELAY.equals(action)) {
              	//1。计算随机雷值：
              	int thunder=getRadomNum();
              	//广播雷值：
              	sendBroadcastThunder(String.valueOf(thunder));
              	String say="最佳雷值为："+thunder;
              	//2。显示随机雷值：
              	fwt.ShowFloatingWindow();
      
              	fwt.tvJE.setText("发包金额为："+Config.sMoneyValue+"元");
              	fwt.tvNum.setText("发包包数为："+Config.sLuckyMoneyNum+"个");
              	fwt.tvThunder.setText(say);
              	speeker.speak(say);
              	fwt.mShowTime=50;
              	//fwt.StartTime();
              	//String s=Config.sMoneyValue+"/"+thunder;
              	String s=String.valueOf(thunder);
              	Config.sLuckyMoneySay=s;
              	//clickPWDthreadStart(); 
              }
              //2。显示雷值结束处理：
              if(Config.ACTION_CALC_THUNDER_SHOW.equals(action)) {
              	//1。输入值：
              	bSuccess=inputText(mRootNode);
              	showResult(bSuccess);
              	//关闭背景音乐：
              	mBackgroundMusic.stopBackgroundMusic();
              }
          }
      };
      //广播雷值：
      private void sendBroadcastThunder(String aThunder){
    	  Intent intent = new Intent(Config.ACTION_THUNDER_RECEIVER);
    	  intent.putExtra("aThunder", aThunder);
    	  context.sendBroadcast(intent);
      }
      //产生一个0~9的随机数：
      private int getRadomNum(){
      	java.util.Random random=new java.util.Random();// 定义随机类
      	int result=random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
      	return result;
      }
      //输入文本：
      public boolean inputText2(AccessibilityNodeInfo rootNode){

      	//if(!getPWDpadInfo(rootNode))return false;//获取PWD面板长宽和单元格信息；
      	bSuccess=false;
      	mRootNode=rootNode;
      	if(Config.bAutoThunder){//计算雷值
      		CalcThunderDelay();//1。延时显示；
      	}else{
      		bSuccess=inputText(rootNode);
      		showResult(bSuccess);
      	}
      	//PutPWDdelay();	
      	return bSuccess;
      }
      //显示结果：
      private void showResult(boolean bSuccess){
      	if(bSuccess==false&&Config.bReg==false){
      		String say="您是试用版用户！请手动完成后续埋雷工作！试用版用户出雷机率在0~3个之间。";
  			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
  			speeker.speak(say);
      	}
      	if(bSuccess)
      		Config.JobState=Config.STATE_INPUT_PWD;
      	else
      		Config.JobState=Config.STATE_INPUT_CLOSING;
      }
      
    public boolean inputText(AccessibilityNodeInfo rootNode){

        	String txt="";
        	//个数：(rootNode, "本群共", 0)
        	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 0,true);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneyNum;
        	if(!nodeInput(target,txt))return false;
        	//金额：
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "输入金额", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText",1,false);
        	if(target==null)return false;
        	txt=Config.sMoneyValue;
        	if(!nodeInput(target,txt))return false;
        	//红包语：
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "恭喜发财", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 2,false);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneySay;
        	if(!nodeInput(target,txt))return false;
        	//塞钱：
        	target=AccessibilityHelper.findNodeInfosByText(rootNode, "塞钱", 0) ;
        	if(target==null)return false;
        	if(!target.performAction(AccessibilityNodeInfo.ACTION_CLICK))return false;
        	return true;
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
    	if(Config.currentapiVersion>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(100);
        	if(RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(1000);
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
  

    
}
