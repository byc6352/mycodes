/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.byc.ClearThunderQq.R;
import util.BackgroundMusic;
import com.example.h3.Config;

import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyReceiveJob {
	private static LuckyMoneyReceiveJob current;
	public  String TAG = "byc001";//调试标识：
	private Context context;
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;
	private String mMoneyValue;//抢到红包金额；
	private String mSayThunder;//雷值；
	
	private static final String DIGITAL="0123456789";//
	private String[] mReceiveInfo={"","","","",""};//拆包信息；
	private int mIntInfo=0;//信息数；
	private boolean bRecycled=false;//是否退出循环
	private static final int KEY_CRASH_THUNDER_NO=0;//没有中雷；
	private static final int KEY_CRASH_THUNDER_YES=1;//中雷；
	private static final int KEY_CRASH_THUNDER_UNSURE=2;//不确定；
	private int iCrashThunder=0;//中雷类型；
	private String mManOfSendLuckyMoney;//发包者；
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
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
    public static synchronized LuckyMoneyReceiveJob getLuckyMoneyReceiveJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyReceiveJob(context);
        }
        return current;
        
    }
    /*判断是否是红包领完窗体*/
    public boolean isNoLuckyMoneyUI(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"来晚一步，领完啦~",0);
    	if(aNode!=null&&aNode.getText()!=null){
    		if("来晚一步，领完啦~".equals(aNode.getText().toString()))return true;else return false;
    	}
    	return false;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public  boolean unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"已存入余额",0);
    	if(LuckyMoneyNode!=null){
    		
    			UnpackLuckyMoneyShowDigital();//抢包数字显示；
    			//获取金额和红包语：
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    			//判断是否是雷包：
    			if(checkLuckyMoneySayType(mReceiveInfo[0])==Config.TYPE_LUCKYMONEY_THUNDER){
    				LuckyMoneyDetailShow2(mReceiveInfo[1],mReceiveInfo[0]);   
    				return true;
    			}
    	}
    	return false;
    }
    public int checkLuckyMoneySayType(String LuckyMoneySay){
    	if(LuckyMoneySay.length()==0)return Config.TYPE_LUCKYMONEY_WELL;
		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
		if(DIGITAL.indexOf(LuckyMoneySayTail)==-1)
			return Config.TYPE_LUCKYMONEY_WELL;
		else
			return Config.TYPE_LUCKYMONEY_THUNDER;
}
    public void LuckyMoneyDetailShow2(String je,String say){
    	String sMoneyThunder=getMoneyThunder(je);//金额中的雷；
    	String sSayThunder="";//红包语中的雷;
    	if(Config.iMoneySay==Config.KEY_THUNDER_IS_SAY){//红包语既是 雷 ；
    		sSayThunder=say;
    	}else{
        	
        	sSayThunder=getSayThunder(say);//红包语中的雷;
    	}
    	int iThunder=CrashThunder(sSayThunder,sMoneyThunder);//中雷类型;

    	mMoneyValue=je;//金额;
    	//mTotalMoney=totalMoney;//总 金额
    	mSayThunder=sSayThunder;//雷值；
    	mManOfSendLuckyMoney=mReceiveInfo[2];//抢包者
    	//显示结果：
    	showResult(iThunder);
    }
    //显示成功或失败界面：
    private void showResult(int iCrashThunder) {

    	String say="";
    	if(iCrashThunder>0){
    		mBackgroundMusic.playBackgroundMusic( "ao.mp3", false);
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_FAIL;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_FAIL;
    		fwp.StartSwitchPics();
    		if(Config.bReg)
    			if(iCrashThunder==1)
    				say="网络延迟！中雷了！";//授权
    			else
    				say="网络延迟！可能中雷了！";//授权
    		else
    			if(iCrashThunder==1)
    				say="中雷了！试用版不享受本次透视服务！请购买正版！";//未授权
    			else
    				say="可能中雷了！试用版不享受本次透视服务！请购买正版！";//未授权
    	}else{
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_SUCCESS;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_SUC;
    		fwp.StartSwitchPics();
    		say="恭喜！抢到"+mManOfSendLuckyMoney+mMoneyValue+"元，雷值为："+mSayThunder+",避雷透视成功！";//未授权
    	}
    	//显示：
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    }
    /*
     * 是否中雷：中雷类型：1中雷；2可能中雷；0:没有中雷;
     */
    public int CrashThunder(String SayThunder,String MoneyThunder){
    	if(SayThunder.indexOf(MoneyThunder)==-1){//没有中雷:
    		return KEY_CRASH_THUNDER_NO;
    	}else{//中雷：
    		int i=getThunderNum(SayThunder);//雷的真实个数：
    		if(i>1)return KEY_CRASH_THUNDER_UNSURE;else return KEY_CRASH_THUNDER_YES;
    	}

    }
    /*
     * 返回雷的真实个数：
     */
    public int getThunderNum(String SayThunder){
    	String c="";
    	int k=0;
    	for(int i=0;i<SayThunder.length()-1;i++){
    		c=SayThunder.substring(i,i+1);
    		if(AccessibilityHelper.isDigital(c))k=k+1;
    	}
    	return  k;
    }
    /*
     * 获取红包语中的雷：剔除红包语 中 的金额，既是雷字符串；
     */
    public String getSayThunder(String say){
    	//排除雷在中间位且选择单雷的情况：
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	String thunder=say;
    	try{
    	switch(Config.iThunderNum){
    	case Config.KEY_THUNDER_SINGLE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-1,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_DOUBLE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-2,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,3);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_THREE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-3,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,4);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,3);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,3);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_FOUR:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-4,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,5);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,4);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,4);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_FIVE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-5,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,6);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,5);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,5);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_ANY:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-1,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	}
    	}catch(IndexOutOfBoundsException e){
    		e.printStackTrace();
    		thunder=say;
    	}
    	return thunder;
    }
    /*
     * 获取红包语中的雷：智能识别：从第二位是否为0判断：
     */
    public String getSayThunderAuto(String say,int thunderNum){
    	String zero=say.substring(1,2);
    	if(zero.equals("0"))return say.substring(say.length()-thunderNum,say.length());
    	else return say.substring(0,thunderNum);
    }
    /*
     * 获取金额中的雷：
     */
    public String getMoneyThunder(String  je){
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	//String sMoneyThunder=je.replace("元", "");
    	String sMoneyThunder=je;
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyThunder.substring(sMoneyThunder.length()-1,sMoneyThunder.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyThunder.substring(sMoneyThunder.length()-2,sMoneyThunder.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyThunder.substring(sMoneyThunder.length()-4,sMoneyThunder.length()-3);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		String sFen=sMoneyThunder.substring(sMoneyThunder.length()-1,sMoneyThunder.length());
    		String sJiao=sMoneyThunder.substring(sMoneyThunder.length()-2,sMoneyThunder.length()-1);
    		int iFen=0;
    		int iJiao=0;
    		try {
    		    	iFen = Integer.parseInt(sFen);
    		    	iJiao = Integer.parseInt(sJiao);
    		} catch (NumberFormatException e) {
    		    e.printStackTrace();
    		    Log.i(TAG, "[KEY_THUNDER_TWO_TAIL_ADD]中数据类型转换失败！");
    		    return je;
    		}
    		String tmp=String.valueOf(iFen+iJiao);
    		sMoneyThunder=tmp.substring(tmp.length()-1,tmp.length());
    	}
    	return sMoneyThunder;
    }
    public void LuckyMoneyDetailShow(String je,String say){
    	//获取参数：
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	//躲避成功：抢到金额：3.0元；雷值为：7；避雷成功！
    	//躲避失败：未授权用户不享受透视服务！躲避失败！
    	String sMoneyValue=je;
    	String sMoneySay=say;
    	String sSayThunder="";
    	String sSayThunder2="";//双雷;
    	String sMoneyThunder="";
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(2,3);
    		}
    	}
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
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
			if(info.getText()==null)return;
			if(Config.DEBUG)Log.i(TAG, mIntInfo+info.getText().toString());
			//取信息
			if(mIntInfo==1)mReceiveInfo[2]=info.getText().toString();//发红包的人：
			if(mIntInfo==5)mReceiveInfo[0]=info.getText().toString();//红包语
			if(mIntInfo==2)mReceiveInfo[1]=info.getText().toString();//金额
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


}
