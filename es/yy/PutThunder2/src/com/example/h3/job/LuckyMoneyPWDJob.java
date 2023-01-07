/**
 * 
 */
package com.example.h3.job;

import com.byc.PutThunder2.R;

import com.example.h3.Config;
import util.ConfigCt;

import accessibility.AccessibilityHelper;
import util.BackgroundMusic;
import com.example.h3.FloatingWindowPic;

import util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Build;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyPWDJob {
	private static LuckyMoneyPWDJob current;	
	public Context context;
	private SpeechUtil speaker ;
	//消息定义：
	private static final int  MSG_RESULT=0x16;
	//输入密码延时：
	private FloatingWindowPic fwp;
	//
	public boolean bSuccess=true;
	
	private BackgroundMusic mBackgroundMusic;
	
	private LuckyMoneyPWDJob(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
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
    public boolean autoPutPWD(AccessibilityNodeInfo rootNode){
    	//Toast.makeText(context, "请输入支付密码", Toast.LENGTH_LONG).show();
    	AccessibilityNodeInfo nodeInfo=AccessibilityHelper.findNodeInfosByText(rootNode,"请输入支付密码",0);//判断窗体
    	if(nodeInfo==null)return false;//
    	PutPWDdelay();
    	return true;
    }
    /*延时：*/
    private void PutPWDdelay() {
    	speaker.speak("正在为您分配红包金额：");
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=50;
    	fwp.mSendMsg=Config.ACTION_PUT_PWD_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    	fwp.StartSwitchPics();
    }
    /*开始输入密码：*/
	private BroadcastReceiver putPWDdelayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(Config.ACTION_PUT_PWD_DELAY.equals(action)) {
            	//开始输入密码：
            	clickPWDThread();
            	//关闭背景音乐：
            	//mBackgroundMusic.stopBackgroundMusic();
            	//mBackgroundMusic.playBackgroundMusic( "dd1.mp3", true);
            }
        }
    };
    public void clickPWDThread(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
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
				Looper.loop(); 
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    public boolean clickPWD(){
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
    		return InputPWD.getInputPWD(context).input(Config.sPWD);
    	else
    		return InputPWD.getInputPWD(context).input7(Config.sPWD);
    }
   
    
    /*
        	 @Override  
         public void run() {  
             //定义消息  
             Message msg = new Message();  
             msg.what = PWD_MSG;
             Bundle bundle = new Bundle();
             bundle.clear(); 
             String sNum="";
			 String sPWD=Config.sPWD;
			 String sOrder="";
			 int iNum=0;
			 //Log.i(TAG, "Thread Start------------>");
			 try{
				 Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 List<String> cmds = new ArrayList<String>();
			 for(int i=0;i<6;i++){
				 sNum=sPWD.substring(i,i+1); 
				 iNum=Integer.parseInt(sNum);
				 sOrder=getNumPosFromPad(iNum);
				 cmds.add(sOrder);
			 }
			//Log.i(TAG,sOrder);
			if(!RootShellCmd.getRootShellCmd().execs(cmds)){
					bundle.putBoolean(KEY_CLICK_PWD, false);
					msg.setData(bundle);
					HandlerClickPWD.sendMessage(msg);
					return;
			}
			bundle.putBoolean(KEY_CLICK_PWD, true);
			msg.setData(bundle);
			HandlerClickPWD.sendMessage(msg);
			return;
    	 }
      //获取
    private String getNumOrder2(String sNum){
    	if(mCellLen==0){
    		mCellLen=Config.screenWidth/3;
    		mCellHigh=mCellLen/2;
    		mPadLeft=0;
    		mPadTop=Config.screenHeight-4*mCellHigh;
    	}
    	int iNum=Integer.parseInt(sNum);
    	String sOrder=getNumPosFromPad(iNum);
    	return sOrder;
    }
    //获取
    private String getNumOrder(AccessibilityNodeInfo rootNode,String sNum){
    	if(mCellLen==0){
    		AccessibilityNodeInfo ParentNode=AccessibilityHelper.findNodeInfosById(rootNode,"com.tencent.mm:id/a0c",-1);
    		Log.i(TAG,"ParentNode----------------------------------------->");
    		if(ParentNode==null){
    			Log.i(TAG,"ParentNode  IS NULL.");
    			return null;
    		}
    		Rect outBounds=new Rect();
    		ParentNode.getBoundsInScreen(outBounds);
    		int x=outBounds.left;
    		int y=outBounds.top;
    		int b=outBounds.bottom;
    		int r=outBounds.right;
    		mCellLen=(r-x)/3;
    		mCellHigh=(b-y)/4;
    		mPadLeft=x;
    		mPadTop=y;
    	}

    	int iNum=Integer.parseInt(sNum);
    	String sOrder=getNumPosFromPad(iNum);
    	return sOrder;
    }
    //计算密码面板数字坐标：
    private String getNumPosFromPad(int iNum){
    	int x=0;
    	int y=0;

    	switch(iNum){
    	case 1:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 2:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 3:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh/2;
    		break;
    	case 4:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 5:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 6:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+mCellHigh+mCellHigh/2;
    		break;
    	case 7:
    		x=mPadLeft+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 8:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 9:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadTop+2*mCellHigh+mCellHigh/2;
    		break;
    	case 0:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadTop+3*mCellHigh+mCellHigh/2;
    		break;
    	}
    	return "input tap "+String.valueOf(x)+" "+String.valueOf(y);
    }
    //输入密码：
    public boolean putPWD(AccessibilityEvent event){

    		AccessibilityNodeInfo rootNode = event.getSource();     
    		if (rootNode == null) {return false;}
    		//recycle(rootNode);
    		String sNum="";
    		String sPWD=Config.sPWD;
    		String sOrder="";
    		for(int i=0;i<6;i++){
    			sNum=sPWD.substring(i,i+1);
    			sOrder=getNumOrder(rootNode,sNum);
    			if(sOrder==null)return false;
    			//sOrder="input tap 120 764";
    			RootShellCmd.getRootShellCmd().execShellCmd(sOrder);
    		}
    		return true;
    }
    
      //输入密码：
    public boolean putPWD(){
    		//recycle(rootNode);
    		String sNum="";
    		String sPWD=Config.sPWD;
    		String sOrder="";
    		//Log.i(TAG,"PWD--------------------------------------------->");
    		//AccessibilityHelper.recycle(rootNode);
    		//String say0="本机分辨率0：X："+Config.screenWidth+";Y:"+Config.screenHeight;
    		//Toast.makeText(context, say0, Toast.LENGTH_LONG).show();
    		for(int i=0;i<6;i++){
    			sNum=sPWD.substring(i,i+1);
    			sOrder=getNumOrder2(sNum);
    			if(sOrder==null)return false;
    			//sOrder="input tap 120 764";
    			AccessibilityHelper.Sleep(500);
    			//Log.i(TAG,sOrder);
    			Toast.makeText(context, sOrder, Toast.LENGTH_LONG).show();
    			if(!RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
    				if(Config.bReg)
    					Toast.makeText(context, "本机未ROOT！", Toast.LENGTH_LONG).show();
    				else	
    					Toast.makeText(context, "本机未授权！", Toast.LENGTH_LONG).show();
    				return false;
    			}
    		}
    		//String say="本机分辨率：X："+Config.screenWidth+";Y:"+Config.screenHeight;
    		//Toast.makeText(context, say, Toast.LENGTH_LONG).show();
    		return true;
    }
     */
}
