/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.byc.qqclearthunder2.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyDetailJob {
	private static LuckyMoneyDetailJob current;
	private Context context;
	public  String TAG = "byc001";//调试标识：
	private String[] mReceiveInfo={"","","",""};//拆包信息；
	private int mIntInfo=0;//信息数；
	private boolean bRecycled=false;//是否退出循环
	private SpeechUtil speaker ;
	private BackgroundMusic mBackgroundMusic;
	private FloatingWindowPic fwp;
	private String mMoneyValue;//抢到红包金额；
	private String mSayThunder;//雷值；
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	this.TAG=Config.TAG;
    	speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        
    		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
    		int w=Config.screenWidth-200;
    		int h=Config.screenHeight-200;
    		fwp.SetFloatViewPara(100, 200,w,h);
    		//接收广播消息
            IntentFilter filter = new IntentFilter();
            filter.addAction(Config.ACTION_DISPLAY_SUCCESS);
            filter.addAction(Config.ACTION_DISPLAY_FAIL);
            context.registerReceiver(ClickLuckyMoneyReceiver, filter);
    }
    /*是否是详细信息界面*/
    public boolean isDetailUI(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"返回",0);
    	if(aNode==null)return false;
    	aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"QQ红包",0);
    	if(aNode==null)return false;
    	aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"红包记录",0);
    	if(aNode==null)return false;
    	return true;
    }
    /*显示详细信息*/
    public  boolean unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"收到的红包已存入余额    余额使用",0);
    	if(LuckyMoneyNode!=null&&LuckyMoneyNode.getText()!=null){
    		if("收到的红包已存入余额    余额使用".equals(LuckyMoneyNode.getText().toString())){
    			UnpackLuckyMoneyShowDigital();//抢包数字显示；
    			//获取金额和红包语：
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    			//判断是否是雷包：
    			if(checkLuckyMoneySayType(mReceiveInfo[0])==Config.TYPE_LUCKYMONEY_THUNDER){
    				LuckyMoneyDetailShow(mReceiveInfo[1],mReceiveInfo[0]);   
    				return true;
    			}
    		}
    	}
    	return false;
    }
    public int checkLuckyMoneySayType(String LuckyMoneySay){

  		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
  		if(!AccessibilityHelper.isDigital(LuckyMoneySayTail))
  			return Config.TYPE_LUCKYMONEY_WELL;
  		else
  			return Config.TYPE_LUCKYMONEY_THUNDER;
  }
      public void LuckyMoneyDetailShow(String je,String say){
      	//获取参数：
      	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
      	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
      	//躲避成功：抢到金额：3.0元；雷值为：7；避雷成功！
      	//躲避失败：未授权用户不享受透视服务！躲避失败！
      	String sMoneyValue=je.substring(0,je.length()-1);
      	//Log.i(Config.TAG, sMoneyValue);
      	String sMoneySay=say;
      	String sSayThunder="";
      	String sSayThunder2="";//双雷;
      	String sMoneyThunder="";
      	if(Config.iThunderNum>2)Config.iThunderNum=1;
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){//尾为雷
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);//10/2/3
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){//首为雷
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(2,3);//2/3/10
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(1,2);//23.10
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ){//自动识别
    		//从倒数第二位判断：
    		String s=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);
    		if(AccessibilityHelper.isDigital(s)){//雷在第一位
    			sSayThunder=sMoneySay.substring(0,1);
        		if(Config.iThunderNum==2){//双雷;
        			sSayThunder2=sMoneySay.substring(2,3);
        			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(1,2);//23.10
        		}
    		}else{//雷在最后一位
        		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
        		if(Config.iThunderNum==2){//双雷;
        			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
        			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
        		}
    		}
    		
    	}else if(iMoneySayPos==Config.KEY_THUNDER_Middle){//中间位为雷
    		String c=sMoneySay.substring(1,2);//形如：1/245
    		if(!AccessibilityHelper.isDigital(c)){
    			sSayThunder=sMoneySay.substring(2,3);
    			if(Config.iThunderNum==2)sSayThunder2=sMoneySay.substring(3,4);//双雷;
    		}else{
    			c=sMoneySay.substring(2,3);//形如：10/245
    			if(!AccessibilityHelper.isDigital(c)){
        			sSayThunder=sMoneySay.substring(3,4);
        			if(Config.iThunderNum==2)sSayThunder2=sMoneySay.substring(4,5);//双雷;
    			}else{
    				c=sMoneySay.substring(3,4);//形如：100/245
    				if(!AccessibilityHelper.isDigital(c)){
            			sSayThunder=sMoneySay.substring(4,5);
            			if(Config.iThunderNum==2)sSayThunder2=sMoneySay.substring(5,6);//双雷;
    				}//if(!AccessibilityHelper.isDigital(c)){
    			}//if(!AccessibilityHelper.isDigital(c)){
    		}//if(!AccessibilityHelper.isDigital(c)){
    		
    	}//else if(iMoneySayPos==Config.KEY_THUNDER_Middle){//中间位为雷
      	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
      		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
      	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
      		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
      	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
      		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
      	}else if(iMoneyValuePos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		String sFen=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    		String sJiao=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    		int iFen=0;
    		int iJiao=0;
    		try {
    		    	iFen = Integer.parseInt(sFen);
    		    	iJiao = Integer.parseInt(sJiao);
    		} catch (NumberFormatException e) {
    		    e.printStackTrace();
    		    Log.i(TAG, "[KEY_THUNDER_TWO_TAIL_ADD]中数据类型转换失败！");
    		    return;
    		}
    		String tmp=String.valueOf(iFen+iJiao);
    		sMoneyThunder=tmp.substring(tmp.length()-1,tmp.length());
    	}

      	boolean bCrash=false;
      	//比较第一个雷：
      	if(sMoneyThunder.equals(sSayThunder))bCrash=true;
      	//比较第二个雷：
      	if(Config.iThunderNum==2){//双雷;
      		if(sMoneyThunder.equals(sSayThunder2))bCrash=true;
      	}
      	mMoneyValue=sMoneyValue;
      	mSayThunder=sSayThunder;
      	//显示结果：
      	showResult(bCrash);
      }
    
      //拆包显示数字：
      private void UnpackLuckyMoneyShowDigital() {
      	
      	speaker.speak("正在为您分析：");
      	float f=(float) (Math.random()*10000);
      	String s=String.valueOf(f);
      	Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
      }
      
      private void recycleGetJeAndSay(AccessibilityNodeInfo info) {
      	if(bRecycled)return;
  		if (info.getChildCount() == 0) {
  			//取信息
  			if(mIntInfo==4){//红包语
  				if(info.getText()!=null)
  					mReceiveInfo[0]=info.getText().toString();	
  				else mReceiveInfo[0]="10/1";	
  			}
  			if(mIntInfo==9){//金额：
  				if(info.getText()!=null)
  					mReceiveInfo[1]=info.getText().toString();	
  				else mReceiveInfo[1]="0.01元";
  				String sYuan=mReceiveInfo[1];
  				sYuan=sYuan.substring(sYuan.length()-1,sYuan.length());
  				if(sYuan.equals("元"))
  					bRecycled=true;
  			}
  			if(mIntInfo==10){//金额：
  				if(info.getText()!=null)
  					mReceiveInfo[1]=info.getText().toString();	
  				else mReceiveInfo[1]="0.01元";
  				bRecycled=true;
  			}
  			mIntInfo=mIntInfo+1;
  			
  		} else {
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycleGetJeAndSay(info.getChild(i));
  				}
  			}
  		}
  	}
      //显示成功或失败界面：
      private void showResult(boolean bCrash) {

      	String say="";
      	if(bCrash){
      		mBackgroundMusic.playBackgroundMusic( "ao.mp3", false);
      		fwp.ShowFloatingWindow(); 
      		fwp.c=20;
      		fwp.mSendMsg=Config.ACTION_DISPLAY_FAIL;
      		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_FAIL;
      		fwp.StartSwitchPics();
      		if(Config.bReg)
      			say="网络延迟！中雷了！";//授权
      		else
      			say="中雷了！试用版不享受本次透视服务！请购买正版！";//未授权
      	}else{
      		fwp.ShowFloatingWindow(); 
      		fwp.c=20;
      		fwp.mSendMsg=Config.ACTION_DISPLAY_SUCCESS;
      		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_SUC;
      		fwp.StartSwitchPics();
      		say="恭喜！抢到红包"+mMoneyValue+"元，雷值为："+mSayThunder+",避雷透视成功！";//未授权
      	}
      	//显示：
      	if(!say.equals("")){
      		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
      		speaker.speak(say);	
      	}
      	//fwp.StopSwitchPics();
      	//fwp.RemoveFloatingWindowPic();
      	//mBackgroundMusic.stopBackgroundMusic();
      }
  	private BroadcastReceiver ClickLuckyMoneyReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {

              String action = intent.getAction();
              //Log.d(TAG, "receive-->" + action);
              if(Config.ACTION_DISPLAY_SUCCESS.equals(action)) {
              	
              }
              if(Config.ACTION_DISPLAY_FAIL.equals(action)) {
              	mBackgroundMusic.stopBackgroundMusic();
              }
          }
      };
    
    
    
    
    
    
    
    
    
    
    
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void recycle(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			mReceiveInfo[mIntInfo]=info.getText().toString();

			//Log.i(TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "Text：" + info.getText());
			//Log.i(TAG, "windowId:" + info.getWindowId());
			if(mIntInfo==2){bRecycled=true;return;}
			mIntInfo=mIntInfo+1;
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycle(info.getChild(i));
				}
			}
		}
	}
    public void LuckyMoneyDetailShow(AccessibilityNodeInfo info){
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	//获取参数：
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	//躲避成功：抢到金额：3.0元；雷值为：7；避雷成功！
    	//躲避失败：未授权用户不享受透视服务！躲避失败！
    	String sMoneyValue=mReceiveInfo[2];
    	String sMoneySay=mReceiveInfo[1];
    	String sSayThunder="";
    	String sSayThunder2="";//双雷;
    	String sMoneyThunder="";
      	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){//尾为雷
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);//10/2/3
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){//首为雷
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(2,3);//2/3/10
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(1,2);//23.10
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ){//自动识别
    		//从倒数第二位判断：
    		String s=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);
    		if(AccessibilityHelper.isDigital(s)){//雷在第一位
    			sSayThunder=sMoneySay.substring(0,1);
        		if(Config.iThunderNum==2){//双雷;
        			sSayThunder2=sMoneySay.substring(2,3);
        			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(1,2);//23.10
        		}
    		}else{//雷在最后一位
        		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
        		if(Config.iThunderNum==2){//双雷;
        			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
        			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
        		}
    		}
    		
    	}
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
    	}
    	String sShow="";
    	boolean bCrash=false;
    	//比较第一个雷：
    	if(sMoneyThunder.equals(sSayThunder))bCrash=true;
    	//比较第二个雷：
    	if(Config.iThunderNum==2){//双雷;
    		if(sMoneyThunder.equals(sSayThunder2))bCrash=true;
    	}
    	//碰雷设置：
    	if(bCrash){
    		if(!Config.bReg)sShow="试用版不享受本次透视服务！请购买正版！";//未授权
    	}else{//未碰雷
    		sShow="恭喜！抢到红包"+sMoneyValue+"元，雷值为："+sSayThunder+",避雷透视成功！";
    	}
    	//显示：
    	if(!sShow.equals("")){
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		speaker.speak(sShow);	
    	}
    	
    }

}
