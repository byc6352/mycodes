package com.example.h3.job;

import com.byc.PutThunder2.R;

import com.example.h3.Config;
import accessibility.QiangHongBaoService;

import accessibility.AccessibilityHelper;
import util.BackgroundMusic;
import com.example.h3.FloatingWindowPic;
import com.example.h3.FloatingWindowText;

import util.RootShellCmd;
import util.SpeechUtil;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class LuckyMoneyPrepareJob {
	private static LuckyMoneyPrepareJob current;
	private Context context;
	private static String TAG = "byc001";
	private static final int MSG_RESULT = 0x41;//执行结果消息
	private SpeechUtil speaker ;
	//执行成功定义：
	public boolean bSuccess=false;
	//工作状态定义：
	public boolean mWorking=false;
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
		speaker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//输入字符延时：
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//显示雷值窗口：
		fwt=FloatingWindowText.getFloatingWindowText(context);
		fwt.SetFloatViewPara(100, 200,w,w);
		//接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_CALC_THUNDER_DELAY);
        filter.addAction(Config.ACTION_CALC_THUNDER_SHOW);
        filter.addAction(Config.ACTION_INPUT_RESULT_SHOW);
        context.registerReceiver(calcThunderReceiver, filter);
	}
    public static synchronized LuckyMoneyPrepareJob getLuckyMoneyPrepareJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPrepareJob(context);
        }
        current.context=context;
        return current;
        
    }
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
            case MSG_RESULT:
            	boolean bResult=true;
            	if(msg.arg1==0)bResult=false;
            	showResult(bResult);
            	mWorking=false;
                break;

            }
        }
    };
    //输入延时：
    private void CalcThunderDelay() {
    	speaker.speak("正在为您计算最佳雷值：");
        //播放背景音乐：
        mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
        //显示延时窗口
		fwp.ShowFloatingWindow(); 
    	fwp.c=30;
    	fwp.mSendMsg=Config.ACTION_CALC_THUNDER_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_RED;
    	fwp.StartSwitchPics();
    }
    //接收延时结束消息：
	private BroadcastReceiver calcThunderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //计算延时结束处理：
            if(Config.ACTION_CALC_THUNDER_DELAY.equals(action)) {
            	//1。计算随机雷值：
            	String thunder="";
            	if(Config.iPlaying==Config.KEY_PUT_THUNDER_BUMP_PLAYING){
            		thunder=String.valueOf(getRadomNum());
            		Config.sLuckyMoneyNum="5";
            		Config.sLuckyMoneySay=thunder;
            	}else{
            		thunder=getThunder();
            		if(Config.iMoneySayPos==Config.KEY_THUNDER_TAIL)
            			Config.sLuckyMoneySay=Config.sMoneyValue+"/"+thunder;
            		if(Config.iMoneySayPos==Config.KEY_THUNDER_HEAD)
            			Config.sLuckyMoneySay=thunder+"/"+Config.sMoneyValue;
              		if(Config.iMoneySayPos==Config.KEY_THUNDER_SAY)
              			Config.sLuckyMoneySay=thunder;
            	}
            	String say="最佳雷值为："+thunder;
            	//2。显示随机雷值：
            	fwt.ShowFloatingWindow();
    
            	fwt.tvJE.setText("发包金额为："+Config.sMoneyValue+"元");
            	fwt.tvNum.setText("发包包数为："+Config.sLuckyMoneyNum+"个");
            	fwt.tvThunder.setText(say);
            	speaker.speak(say);
            	fwt.mShowTime=50;
            	fwt.StartTime();
            }
            //2。显示雷值结束处理：开始输入：
            if(Config.ACTION_CALC_THUNDER_SHOW.equals(action)) {
            	//1。输入值：
            	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N&&Config.wv<1300)//判断版本号；
        			inputTextsThread7(mRootNode);
        		else
        			inputTextsThread(mRootNode);          	
            	//关闭背景音乐：
            	mBackgroundMusic.stopBackgroundMusic();
            }
            //2。显示雷值结束处理：输入结束处理：
            if(Config.ACTION_INPUT_RESULT_SHOW.equals(action)) {//
            	boolean bResult=intent.getBooleanExtra("input_result", false);
            	showResult(bResult);
            	mWorking=false;
            }
        }
    };
    //产生一个0~9的随机数：
    private int getRadomNum(){
    	java.util.Random random=new java.util.Random();// 定义随机类
    	int result=random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
    	return result;
    }
    //产生一个0~9的随机数(排除一个数字）；
    private int getRadomNumNoOne(int t){
    	int i=getRadomNum();
    	if(i==t)i=getRadomNumNoOne(t);
    	return i;
    }
    //产生一个0~9的随机数(排除两个数字）；
    private int getRadomNumNoTwo(int t1,int t2){
    	int i=getRadomNum();
    	if(i==t1||i==t2)i=getRadomNumNoTwo(t1,t2);
    	return i;
    }
    //产生一个0~9的随机数(排除三个数字）；
    private int getRadomNumNoThree(int t1,int t2,int t3){
    	int i=getRadomNum();
    	if(i==t1||i==t2||i==t3)i=getRadomNumNoThree(t1,t2,t3);
    	return i;
    }
    //产生一个0~9的随机数(排除四个数字）；
    private int getRadomNumNoFour(int t1,int t2,int t3,int t4){
    	int i=getRadomNum();
    	if(i==t1||i==t2||i==t3||i==t4)i=getRadomNumNoFour(t1,t2,t3,t4);
    	return i;
    }
    private String getThunder(){
    	String thunder="";
    	int t=0;
    	int t1=1;
    	int t2=2;
    	int t3=3;
    	int t4=4;
    	switch(Config.iMoneySayThunderNum){
    	case Config.KEY_MONEY_SAY_SINGLE_THUNDER:
    		t=getRadomNum();
    		thunder=String.valueOf(t);
    		break;
    	case Config.KEY_MONEY_SAY_DOUBLE_THUNDER:
    		t=getRadomNum();
    		t1=getRadomNumNoOne(t);
    		thunder=String.valueOf(t)+"/"+String.valueOf(t1);
    		break;
    	case Config.KEY_MONEY_SAY_THREE_THUNDER:
    		t=getRadomNum();
    		t1=getRadomNumNoOne(t);
    		t2=getRadomNumNoTwo(t,t1);
    		thunder=String.valueOf(t)+"/"+String.valueOf(t1)+"/"+String.valueOf(t2);
    		break;
    	case Config.KEY_MONEY_SAY_FOUR_THUNDER:
    		t=getRadomNum();
    		t1=getRadomNumNoOne(t);
    		t2=getRadomNumNoTwo(t,t1);
    		t3=getRadomNumNoThree(t,t1,t2);
    		thunder=String.valueOf(t)+String.valueOf(t1)+String.valueOf(t2)+String.valueOf(t3);
    		break;
    	case Config.KEY_MONEY_SAY_FIVE_THUNDER:
    		t=getRadomNum();
    		t1=getRadomNumNoOne(t);
    		t2=getRadomNumNoTwo(t,t1);
    		t3=getRadomNumNoThree(t,t1,t2);
    		t4=getRadomNumNoFour(t,t1,t2,t3);
    		thunder=String.valueOf(t)+String.valueOf(t1)+String.valueOf(t2)+String.valueOf(t3)+String.valueOf(t4);
    		break;
    	}
    	return thunder;
    }
    /*强制工作：*/
    public void setWorking(){
    	mWorking=false;
    }
    /*是否工作：*/
    public boolean isWorking(){
    	return mWorking;
    }
    /*工作入口：*/
    public void autoInputText(AccessibilityNodeInfo rootNode){
    	mRootNode=rootNode;
    	mWorking=true;
    	if(Config.bAutoThunder){//计算雷值
    		CalcThunderDelay();//1。延时显示；
    	}else{
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N&&Config.wv<1300)//判断版本号；
    			inputTextsThread7(rootNode);
    		else
    			inputTextsThread(rootNode);
    	}
    }

    //显示结果：
    private void showResult(boolean bSuccess){
    	if(bSuccess==false&&Config.bReg==false){
    		String say="您是试用版用户！需要计算埋雷数据，或进行授权成为正版，才能百分百出雷！";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speaker.speak(say);
    	}
    	if(bSuccess)
    		Config.JobState=Config.STATE_INPUT_PWD;
    	else
    		Config.JobState=Config.STATE_INPUT_CLOSING;
    }
 
    public boolean inputTexts7(final AccessibilityNodeInfo rootNode){
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)return false;//判断版本号；
    	AccessibilityNodeInfo nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode, "当前所在页面,发红包", 0);
    	if(nodeInfo==null)return false;//判断窗体
    	inputText7(rootNode,"元",Config.sMoneyValue);//定位金额
    	inputText7(rootNode,"个",Config.sLuckyMoneyNum);//定位金额
    	//nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode, "恭喜发财，大吉大利", 0);
    	nodeInfo=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 0,true);
    	if(nodeInfo==null)return false;
    	if(nodeInfo.getText()!=null&&nodeInfo.getText().toString().equals("恭喜发财，大吉大利")){
        	if(!nodeInput(nodeInfo,Config.sLuckyMoneySay))return false;
    	}
    	
    	nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode, "塞钱进红包", 0) ;//塞钱：
    	if(nodeInfo==null)return false;
    	if(!nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK))return false;
    	Config.JobState=Config.STATE_INPUT_PWD;
    	return true;
    }
    public void inputTextsThread7(final AccessibilityNodeInfo rootNode){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					boolean result=inputTexts7(rootNode);
	                 Message msg = new Message();  
	                 msg.what =MSG_RESULT;
	                 if(result)msg.arg1=1;else msg.arg1=0;
	                 mHandler.sendMessage(msg); 
				    //Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
				    //intent.putExtra("input_result", result);
				    //context.sendBroadcast(intent);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    public boolean inputText7(AccessibilityNodeInfo rootNode,String referTxt,String inputTxt){
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)return false;//判断版本号；
    	AccessibilityNodeInfo nodeInfo=AccessibilityHelper.findNodeInfosByTextAllMatched(rootNode,referTxt);//定位
    	if(nodeInfo==null)return false;//
    	Rect outBounds=new Rect();
    	nodeInfo.getBoundsInScreen(outBounds);
    	Point pos=new Point();
    	pos.x=outBounds.left-30;
    	pos.y=outBounds.top+22;
    	QiangHongBaoService service=QiangHongBaoService.getQiangHongBaoService();
    	service.pressLocation(pos);
    	AccessibilityHelper.Sleep(1000);
    	InputPWD.getInputPWD(context).input7(inputTxt);
    	return true;
    }
    public void inputTextsThread(final AccessibilityNodeInfo rootNode){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					boolean result=inputTexts(rootNode);
	                 Message msg = new Message();  
	                 msg.what =MSG_RESULT;
	                 if(result)msg.arg1=1;else msg.arg1=0;
	                 mHandler.sendMessage(msg); 
				    //Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
				    //intent.putExtra("input_result", result);
				    //context.sendBroadcast(intent);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    public boolean inputTexts(AccessibilityNodeInfo rootNode){

        	String txt="";
        	int wv = Config.getConfig(context).getWechatVersion();
        	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 0,true);
        	if(target==null)return false;
        	if(target.getText()!=null&&target.getText().toString().equals("恭喜发财，大吉大利")){
            	txt=Config.sLuckyMoneySay;
            	if(!nodeInput(target,txt))return false;
            	return false;
        	}
        	if(wv>=1000){
            	//金额：
            	txt=Config.sMoneyValue;
            	if(!nodeInput(target,txt))return false;
            	//个数：(rootNode, "本群共", 0)
            	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "输入金额", 0) ;
            	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText",1,false);
            	if(target==null)return false;
            	txt=Config.sLuckyMoneyNum;
            	if(!nodeInput(target,txt))return false;
        	}else{
        		//个数：(rootNode, "本群共", 0)
        		txt=Config.sLuckyMoneyNum;
        		if(!nodeInput(target,txt))return false;
        		//金额：
        		//target=AccessibilityHelper.findNodeInfosByText(rootNode, "输入金额", 0) ;
        		target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText",1,false);
        		if(target==null)return false;
        		txt=Config.sMoneyValue;
        		if(!nodeInput(target,txt))return false;
        	}
        	//红包语：
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "恭喜发财", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 2,false);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneySay;
        	if(!nodeInput(target,txt))return false;
        	//塞钱：
        	target=AccessibilityHelper.findNodeInfosByText(rootNode, "塞钱进红包", 0) ;
        	if(target==null)return false;
        	if(!target.performAction(AccessibilityNodeInfo.ACTION_CLICK))return false;
        	Config.JobState=Config.STATE_INPUT_PWD;
        	return true;
        
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  boolean nodeInput(AccessibilityNodeInfo edtNode,String txt){
    	if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){//android 5.0
    		Bundle arguments = new Bundle();
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
        	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	return true;
    	}
    	if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN_MR2){//android 4.3
    		ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
    		ClipData clip = ClipData.newPlainText("text",txt);  
    		clipboard.setPrimaryClip(clip);  
    		//edtNode.fo
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  
    		////粘贴进入内容  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(5000);
        	if(RootShellCmd.getRootShellCmd(context).execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(5000);
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
  
    
    
    
    
    
    
    
    
    
    
    
    //------------------------------------------------------------------------------------------------
    //输入文本：(不使用)
    public boolean inputText2(AccessibilityNodeInfo rootNode){

    	//if(!getPWDpadInfo(rootNode))return false;//获取PWD面板长宽和单元格信息；
    	bSuccess=false;
    	mRootNode=rootNode;
    	if(Config.bAutoThunder){//计算雷值
    		CalcThunderDelay();//1。延时显示；
    	}else{
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)//判断版本号；
    			inputTextsThread7(rootNode);
    		else
    			inputTextsThread(rootNode);
    		//showResult(bSuccess);
    	}
    	//PutPWDdelay();	
    	return bSuccess;
    }
//(不使用)
@SuppressLint("NewApi")
public void test(QiangHongBaoService service,AccessibilityNodeInfo rootNode){
	AccessibilityNodeInfo node=service.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
	//AccessibilityNodeInfo node=service.findFocus(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
	if(node==null)Toast.makeText(context, "findFocus=null", Toast.LENGTH_LONG).show();else {
		Rect outBounds=new Rect();
		node.getBoundsInScreen(outBounds);
		Log.d(Config.TAG, "-----------------.>getClassName:"+node.getClassName());
		Log.e(Config.TAG, "-----------------.>getText:"+node.getText());
			Log.e(Config.TAG, "-----------------.>outBounds:" + outBounds);
		nodeInput7(node,"12");
		Toast.makeText(context, "findFocus=true", Toast.LENGTH_LONG).show();
		
	}
}
//(不使用)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public  boolean nodeInput7(AccessibilityNodeInfo edtNode,String txt){
	if(Config.currentapiVersion>=Build.VERSION_CODES.LOLLIPOP){//android 5.0
		Bundle arguments = new Bundle();
    	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
    	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    	//return true;
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
/*
   //输入文本：
    public boolean InputText(AccessibilityEvent event) {
    	String ui=event.getClassName().toString();
    	AccessibilityNodeInfo target=null;
    	String id="";
    	String sText="";
    	String sOrder="";
    	
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {return false;}
        if(ui.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
        	
        	//Bundle arguments = new Bundle();
        	//1。设置包数：--------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";//com.tencent.mm:id/bej
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneyNum;
        	target.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	sOrder="input text "+sText;
        	RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
        	AccessibilityHelper.Sleep(1000);
        	//2。设置发包金额：----------------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";//com.tencent.mm:id/bej
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,1);
        	if(target==null)return false;
        	sText=Config.sMoneyValue;
        	target.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	sOrder="input text "+sText;
        	RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
        	AccessibilityHelper.Sleep(1000);
        	//3。设置红包语
        	id = "com.tencent.mm:id/bdw";//com.tencent.mm:id/bgm 恭喜发财，大吉大利！
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneySay;
        	target.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	sOrder="input text "+sText;
        	RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
        	AccessibilityHelper.Sleep(1000);
        	//4.点击塞钱进红包
        	id = "com.tencent.mm:id/b9s";//塞钱进红包 com.tencent.mm:id/bbi
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	return true;
        }
        return false;
    }

    //输入文本：
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public boolean InputText2(AccessibilityEvent event) {
    	String ui=event.getClassName().toString();
    	AccessibilityNodeInfo target=null;
    	String id="";
    	String sText="";
    	
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {         
            return false;
        }
        if(ui.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyPrepareUI")){
        	
        	Bundle arguments = new Bundle();
        	//1。设置包数：--------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneyNum;
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
        	target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	//2。设置发包金额：----------------------------------------------------------------------
        	id = "com.tencent.mm:id/bbt";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,1);
        	if(target==null)return false;
        	sText=Config.sMoneyValue;
          	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
        	target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	//3。设置红包语
        	id = "com.tencent.mm:id/bdw";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	sText=Config.sLuckyMoneySay;
          	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, sText);
        	target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	//4.点击塞钱进红包
        	id = "com.tencent.mm:id/b9s";
        	target = AccessibilityHelper.findNodeInfosById(nodeInfo, id,0);
        	if(target==null)return false;
        	target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	return true;
        }
        return false;
    }
public boolean inputText3(AccessibilityEvent event){
	String ui=event.getClassName().toString();

    AccessibilityNodeInfo rootNode = event.getSource();
    if (rootNode == null) {return false;}
    if(ui.equals(Config.WINDOW_LUCKYMONEY_PREPARE_UI)){
    	mIntNode=1;
    	mbClose=false;
    	mbResult=true;
    	recycleInput(rootNode);
    	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByText(rootNode, "塞钱进红包", 0) ;
    	if(target==null)return mbResult;
    	target.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    	return mbResult;
    }
    return false;
}
public  void recycleInput(AccessibilityNodeInfo info) {
	if(mbClose)return;
		if (info.getChildCount() == 0) {
			//取信息
			//mRedInfo[mIntInfo]=info.getText().toString();
			//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
			//Log.i(Config.TAG, "Text：" + info.getText());
			//Log.i(Config.TAG, "windowId:" + info.getWindowId());
			//Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
			//Log.i(Config.TAG, "isClickable:" + info.isClickable());
			//Rect outBounds=new Rect();
			//info.getBoundsInScreen(outBounds);
			//Log.i(Config.TAG, "outBounds:" + outBounds);
      	//Bundle arguments = new Bundle();
      	//String sText="11";
      	//arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
      	//info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
			
			//if(info.isClickable())info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			String txt="";
			if("android.widget.EditText".equals(info.getClassName())){
				if(mIntNode==1){
					txt=Config.sLuckyMoneyNum;
					mIntNode=2;
				}else if(mIntNode==2){
					txt=Config.sMoneyValue;
					mIntNode=3;
				}else if(mIntNode==3){
					txt=Config.sLuckyMoneySay;
				}
				if(!nodeInput(info,txt)){
					mbResult=false;
					mbClose=true;
				}
			}//if("android.widget.EditText".equals(info.getClassName())){
			if("塞钱进红包".equals(info.getText())){
				info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				mbResult=true;
				mbClose=true;
			}
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(mbClose)return;
				if(info.getChild(i)!=null){
					recycleInput(info.getChild(i));
				}
			}
		}
	}
	*/
