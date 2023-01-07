/**
 * 
 */
package com.example.h3.job;

import java.util.ArrayList;
import java.util.List;

import com.baidu.android.common.logging.Log;
import com.byc.PutThunderPP.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.QiangHongBaoService;
import com.example.h3.job.LuckyMoneyPWDJob.clickPWDthread;
import com.example.h3.util.FloatingWindowPic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	//public QiangHongBaoService service;
	public Context context;
	private SpeechUtil speeker ;
	//PWD�������ֵ��
	private int mCellLen=0;
	private int mCellHigh=0;
	private int mPadLeft=0;
	private int mPadTop=0;
	private int mPadBottom=0;
	//��Ϣ���壺
	private static final int  PWD_MSG=0x16;
	//��Ϣ�����壺
	private static final String KEY_CLICK_PWD="clickPWD";
	//ִ�гɹ����壺
	public boolean bSuccess=false;
	//����������ʱ��
	private FloatingWindowPic fwp;
	//
	private BackgroundMusic mBackgroundMusic;
	
	private LuckyMoneyPWDJob(Context context) {
		this.context = context;
		speeker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
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
    public Handler HandlerClickPWD = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == PWD_MSG) {
            	mBackgroundMusic.stopBackgroundMusic();
            	Bundle bundle=msg.getData();
            	bSuccess=bundle.getBoolean(KEY_CLICK_PWD);
               if(!bSuccess){
   				if(Config.bReg){
					Toast.makeText(context, "����δROOT��", Toast.LENGTH_LONG).show();
   				}
               }//if(bSuccess){
               //δע�᣺
               if(!Config.bReg){
					Toast.makeText(context, "����δ��Ȩ��", Toast.LENGTH_LONG).show();
	        		String say="�������ð��û������ֶ���ɺ������׹��������ð��û����׻�����0~3��֮�䡣";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speeker.speak(say);
               }
            }  
        }  
  
    };  
    //�������룺
    public boolean putPWD(AccessibilityNodeInfo rootNode){

    	if(!getPWDpadInfo(rootNode))return false;//��ȡPWD��峤���͵�Ԫ����Ϣ��
    	PutPWDdelay();
    	bSuccess=false;		
    	return true;
    }
    public boolean getPWDpadInfo(AccessibilityNodeInfo rootNode){
    	if(mCellLen>0)return true;
    	if(rootNode==null)return false;
    	Rect outBounds=new Rect();//
    	rootNode.getBoundsInScreen(outBounds);
    	int iFrameH=outBounds.bottom;
    	int iFrameW=outBounds.right;
    	mPadBottom=Config.screenHeight;
    	if(iFrameH==1184&&iFrameW==720){
        	mPadLeft=0;
        	mPadTop=780;
        	mCellLen=240;
        	mCellHigh=120;
        	return true;
    	}
    	if(iFrameH==1920&&iFrameW==1080){
        	mPadLeft=0;
        	mPadTop=1310;
        	mCellLen=360;
        	mCellHigh=180;
        	return true;
    	}
    	mPadLeft=0;
    	mCellLen=iFrameW/3;
    	mCellHigh=(int)(mCellLen/2);
    	mPadTop=iFrameH-4*mCellHigh;
    	//Log.i(TAG, "getPWDpadInfo success------------>");
    	return true;
    }
    //������������������꣺
    private String getNumPosFromPad(int iNum){
    	int x=0;
    	int y=0;

    	switch(iNum){
    	case 1:
    		x=mPadLeft+mCellLen/2;
    		y=mPadBottom-3*mCellHigh-mCellHigh/2;
    		break;
    	case 2:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-3*mCellHigh-mCellHigh/2;
    		break;
    	case 3:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadBottom-3*mCellHigh-mCellHigh/2;
    		break;
    	case 4:
    		x=mPadLeft+mCellLen/2;
    		y=mPadBottom-2*mCellHigh-mCellHigh/2;
    		break;
    	case 5:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-2*mCellHigh-mCellHigh/2;
    		break;
    	case 6:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadBottom-2*mCellHigh-mCellHigh/2;
    		break;
    	case 7:
    		x=mPadLeft+mCellLen/2;
    		y=mPadBottom-mCellHigh-mCellHigh/2;
    		break;
    	case 8:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-mCellHigh-mCellHigh/2;
    		break;
    	case 9:
    		x=mPadLeft+2*mCellLen+mCellLen/2;
    		y=mPadBottom-mCellHigh-mCellHigh/2;
    		break;
    	case 0:
    		x=mPadLeft+mCellLen+mCellLen/2;
    		y=mPadBottom-mCellHigh/2;
    		break;
    	}
    	return "input tap "+String.valueOf(x)+" "+String.valueOf(y);
    }
    //�����ʱ��
    private void PutPWDdelay() {
    	speeker.speak("����Ϊ����������");
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
		fwp.ShowFloatingWindow(); 
    	fwp.c=50;
    	fwp.mSendMsg=Config.ACTION_PUT_PWD_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_GREEN;
    	fwp.StartSwitchPics();
    }
    //
	private BroadcastReceiver putPWDdelayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_PUT_PWD_DELAY.equals(action)) {
            	//��ʼ�������룺
            	clickPWDthreadStart(); 
            	//�رձ������֣�
            	//mBackgroundMusic.stopBackgroundMusic();
            	//mBackgroundMusic.playBackgroundMusic( "dd1.mp3", true);
            }
        }
    };
    class clickPWDthread extends Thread { 
    	//public String mSendStr;
    	 public clickPWDthread() { 
    		 //mSendStr=str;
    	 }
    	 
    	 @Override  
         public void run() {  
             //������Ϣ  
             Message msg = new Message();  
             msg.what = PWD_MSG;
             Bundle bundle = new Bundle();
             bundle.clear(); 
             String sNum="";
			 String sPWD=Config.sPWD;
			 String sOrder="";
			 int iNum=0;
			 Log.i(TAG, "Thread Start------------>");
			 try{
				 Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 for(int i=0;i<6;i++){
				 try {
					 sNum=sPWD.substring(i,i+1);
					iNum=Integer.parseInt(sNum);
					sOrder=getNumPosFromPad(iNum);
					Log.i(TAG, sOrder);
					//sOrder="input tap 120 764";
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Log.i(TAG,sOrder);
				if(!RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
					bundle.putBoolean(KEY_CLICK_PWD, false);
					msg.setData(bundle);
					HandlerClickPWD.sendMessage(msg);
					return;
				}
			}//for(int i=0;i<6;i++){
			bundle.putBoolean(KEY_CLICK_PWD, true);
			msg.setData(bundle);
			HandlerClickPWD.sendMessage(msg);
			return;
    	 }
    	 
 
    }//class SockThread extends Thread { 
    public void clickPWDthreadStart(){
    	new clickPWDthread().start();
    	return ;
    }
   
    
    /*
        	 @Override  
         public void run() {  
             //������Ϣ  
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
      //��ȡ
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
    //��ȡ
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
    //������������������꣺
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
    //�������룺
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
    
      //�������룺
    public boolean putPWD(){
    		//recycle(rootNode);
    		String sNum="";
    		String sPWD=Config.sPWD;
    		String sOrder="";
    		//Log.i(TAG,"PWD--------------------------------------------->");
    		//AccessibilityHelper.recycle(rootNode);
    		//String say0="�����ֱ���0��X��"+Config.screenWidth+";Y:"+Config.screenHeight;
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
    					Toast.makeText(context, "����δROOT��", Toast.LENGTH_LONG).show();
    				else	
    					Toast.makeText(context, "����δ��Ȩ��", Toast.LENGTH_LONG).show();
    				return false;
    			}
    		}
    		//String say="�����ֱ��ʣ�X��"+Config.screenWidth+";Y:"+Config.screenHeight;
    		//Toast.makeText(context, say, Toast.LENGTH_LONG).show();
    		return true;
    }
     */
}