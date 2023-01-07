package com.example.h3.job;

import com.byc.PutThunderQq.R;
import util.BackgroundMusic;
import com.example.h3.Config;

import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import com.example.h3.FloatingWindowText;
import util.RootShellCmd;
import util.SpeechUtil;

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
	private SpeechUtil speaker ;
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
		speaker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//输入字符延时：
		//fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int LayoutID=util.ResourceUtil.getLayoutId(context, "float_click_delay_show");
		fwp=FloatingWindowPic.getFloatingWindowPic(context,LayoutID);
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
    	speaker.speak("正在为您计算最佳雷值：");
        //播放背景音乐：
        mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
        //显示延时窗口
    	fwp.c=50;
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
              	String thunder="";
              	if(Config.iPlaying==Config.KEY_PUT_THUNDER_BUMP_PLAYING){
              		//thunder=getThunderFromCount(LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context).mThunderCounts);
              		//thunder=String.valueOf(getRadomNum());
              		thunder=getThunder();
              		Config.sLuckyMoneyNum="5";
              		Config.sLuckyMoneySay=thunder;
              	}else{
              		thunder=getThunder();
              		//thunder=getThunderFromCount(LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context).mThunderCounts);
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
              	//广播雷值：
              	sendBroadcastThunder(thunder);
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
      		//thunder=String.valueOf(t)+"/"+String.valueOf(t1);
      		thunder=String.valueOf(t)+String.valueOf(t1);
      		break;
      	case Config.KEY_MONEY_SAY_THREE_THUNDER:
      		t=getRadomNum();
      		t1=getRadomNumNoOne(t);
      		t2=getRadomNumNoTwo(t,t1);
      		//thunder=String.valueOf(t)+"/"+String.valueOf(t1)+"/"+String.valueOf(t2);
      		thunder=String.valueOf(t)+String.valueOf(t1)+String.valueOf(t2);
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
      		String say="您是试用版用户！需要计算埋雷数据，或进行授权成为正版，才能百分百出雷！";
  			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
  			speaker.speak(say);
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
        	target=AccessibilityHelper.findNodeInfosByText(rootNode, "塞钱", -1) ;
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
        	if(RootShellCmd.getRootShellCmd(context).execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(1000);
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
    //-------------------------------从统计结果获取雷值--------------------------------------
    /*
     * 
     * 从统计结果获取埋值：
     */
    private String getThunderFromCount(int [] ThunderCounts){
      	String thunder="";
      	String thunders="";
      	int [] orders=new int[10];
      	System.arraycopy(ThunderCounts,0, orders,0, 10); 
      	bubbleSort3(orders,10);
      	if(orders[9]==0) return getThunder();
      	for(int i=9;i>=0;i--){
      		thunders=thunders+getOneThunder(ThunderCounts,orders[i],thunders);
      	}
      	thunder=thunders.substring(0,Config.iMoneySayThunderNum);
      	return thunder;
    }
    /*
     * 
     * 从数组的值返回数据角标：
     */
    private String getOneThunder(int [] ThunderCounts,int value,String thunders){
    	for(int i=0;i<10;i++){
    		if(ThunderCounts[i]==value){
    			if(!thunders.contains(String.valueOf(i)))return String.valueOf(i);
    		}
    	}
    	return "";
    }
    /*
     * 冒泡排序
     * */
    public static void bubbleSort3(int [] a, int n){
        int j , k;
        int flag = n ;//flag来记录最后交换的位置，也就是排序的尾边界

        while (flag > 0){//排序未结束标志
            k = flag; //k 来记录遍历的尾边界
            flag = 0;

            for(j=1; j<k; j++){
                if(a[j-1] > a[j]){//前面的数字大于后面的数字就交换
                    //交换a[j-1]和a[j]
                    int temp;
                    temp = a[j-1];
                    a[j-1] = a[j];
                    a[j]=temp;

                    //表示交换过数据;
                    flag = j;//记录最新的尾边界.
                }
            }
        }
    }    
}
