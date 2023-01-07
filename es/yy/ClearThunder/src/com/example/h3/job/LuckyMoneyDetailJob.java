/**
 * 
 */
package com.example.h3.job;

import util.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.byc.ClearThunder.R;
import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.Funcs;
import util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
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
	private String[] mReceiveInfo={"","","",""};//拆包信息:0.发包人；1.红包语；2.抢到的金额；--；
	private int mIntInfo=0;//信息数；
	private boolean bRecycled=false;//是否退出循环
	private static final int KEY_CRASH_THUNDER_NO=0;//没有中雷；
	private static final int KEY_CRASH_THUNDER_YES=1;//中雷；
	private static final int KEY_CRASH_THUNDER_UNSURE=2;//不确定；
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;
	private String mMoneyValue;//抢到红包金额；
	private String mTotalMoney;//总金额；
	private String mSayThunder;//雷值；
	private String mManOfSendLuckyMoney;//发包者；
	//private LuckyMoneyLauncherJob LancherJob;
	private LuckyMoney mLuckyMoney;//红包对象；
	//private static final String WECHAT_GONG_XI="恭喜发财！大吉大利！";//
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	this.TAG=Config.TAG+"_LuckyMoneyDetailJob";
    	speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        //LancherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mLuckyMoney=LuckyMoney.getLuckyMoney(context);//红包对象；
        
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
    
    
    /*
     * 显示抢包结果：
     */
    public void LuckyMoneyDetailShow(AccessibilityNodeInfo info){
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	String man=mReceiveInfo[0];//发包人；
    	String say=mReceiveInfo[1];//红包语；
    	String je=mReceiveInfo[2];//红包金额；
    	//检测数据是否合格（是否是雷包）：
    	if(mLuckyMoney.checkLuckyMoneySayType(say)!=mLuckyMoney.TYPE_LUCKYMONEY_THUNDER)return;//检测红包语
    	if(!Funcs.isMoney(je))return ;//检测金额；
    	String sMoneyThunder=getMoneyThunder(je);//金额中的雷；
    	String sSayThunder="";//红包语中的雷;
    	String totalMoney=getTotalMoneyFromSay(say);//总金额;
    	
    	
    	if(Config.iMoneySay==Config.KEY_THUNDER_SAY_IS_THUNDER){//红包语既是 雷 ；
    		sSayThunder=say;
    	}else{
        	
        	sSayThunder=getSayThunder(say,totalMoney);//红包语中的雷;
    	}

    	int iThunder=CrashThunder(sSayThunder,sMoneyThunder);//中雷类型;
    	mMoneyValue=je;//金额;
    	mTotalMoney=totalMoney;//总 金额
    	mSayThunder=sSayThunder;//雷值；
    	mManOfSendLuckyMoney=man;//抢包者
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
    		say="恭喜！抢到"+mManOfSendLuckyMoney+"的红包"+mMoneyValue+"元，总金额为"+mTotalMoney+"元，雷值为："+mSayThunder+",避雷透视成功！";//未授权
    	}
    	//显示：
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    }
    /*
     * 删除红包语中的金额：
     */
    public String DelMoneyFromSay(String say,String money){
    	int i=say.indexOf(money);
    	int len=money.length();
    	String thunder=say;
    	if(i!=-1){
    		if(i==0){//金额在前面：
    			thunder=say.substring(len,say.length());
    		}else{//金额在后面
    			thunder=say.substring(0,say.length()-len);
    		}
    	}
    	return thunder;
    }
  
    /*
     * 从红包语中获取总金额：
     */
    public String getTotalMoneyFromSay(String say){
    	//判断第二位是否是0：
    	String totalMoney="";
    	if(say.length()<=2)return say;
    	String c2=say.substring(1,2);
    	if(c2.equals("0")){//金额在前：
    		String c3=say.substring(2,3);//第三位
    		if(c3.equals("0")){//第三位为0，说明金额大于100；
    			totalMoney=say.substring(0,3);//取总金额：
    		}else{//总金额 小于100
    			totalMoney=say.substring(0,2);//取总金额：
    		}
    		return totalMoney;
    	}else{//总金额在末位：
    		String cc1=say.substring(say.length()-1,say.length());
    		if(cc1.equals("0")){//进一步判断总金额是否在末位：
    			String cc2=say.substring(say.length()-2,say.length()-1);//倒数第二位；
    			if(cc2.equals("0")){//金额为大于100
    				totalMoney=say.substring(say.length()-3,say.length());//取总金额：
    			}else{//倒数第二位；
    				totalMoney=say.substring(say.length()-2,say.length());//取总金额：
    			}//金额小于100
    			return totalMoney;
    		}else{//if(cc1.equals("0")){
    			int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    			if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)//雷在中间，说明第一位为金额；
    				totalMoney=say.substring(0,1);//取总金额：
    			if(iMoneySayPos==Config.KEY_THUNDER_HEAD)//雷在左边，说明金额在右；
    				totalMoney=say.substring(say.length()-1,say.length());//取总金额：
    			if(iMoneySayPos==Config.KEY_THUNDER_TAIL)//雷在右边，说明金额在左；
    				totalMoney=say.substring(0,1);//取总金额：
    			if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)//智能识别；
    				totalMoney=say.substring(0,1);//取总金额：第一位为金额；
    			if(iMoneySayPos==Config.KEY_THUNDER_SAY_IS_THUNDER)//红包语既是雷；
    				totalMoney="";//取总金额：；
    		}//正数第二位与倒数第一位都不为0，说明金额为个位数；
    	}//if(c2.equals("0")){//金额在前： }else{//总金额在末位：
    	return totalMoney;
    }
    
    /*
     * 获取红包语中的雷：剔除红包语 中 的金额，既是雷字符串；
     */
    public String getSayThunder(String say,String  totalMoney){
    	int i=say.indexOf(totalMoney);
    	if(i==-1)return say;
    	//String thunder=say.replace(totalMoney,"");
    	String thunder=DelMoneyFromSay(say,totalMoney);
    	//排除雷在中间位且选择单雷的情况：
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	if(Config.iThunderNum==Config.KEY_THUNDER_SINGLE&&iMoneySayPos==Config.KEY_THUNDER_MIDDLE){
    		thunder=thunder.substring(0,1);
    	}
    	return thunder;
    }
    /*
     * 获取金额中的雷：
     */
    public String getMoneyThunder(String  je){
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	String sMoneyThunder=je.replace("元", "");
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
    
  

}


/*
   public void LuckyMoneyDetailShow1(AccessibilityNodeInfo info){
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
    	//String sMoneySay=mLuckyMoneySay;//01-05修改：从Lancher界面获取红包语；
    	Log.i(Config.TAG, "Detail-----"+sMoneySay);
    	if(sMoneySay.equals(WECHAT_GONG_XI)){//是恭喜发财！
			if(LancherJob.bNewLuckyMoney){//新红包;
				sMoneySay=LancherJob.mLuckyMoneySay;
			}//if(LancherJob.bNewLuckyMoney){//新红包;
		}//if(LuckyMoneySay.equals(WECHAT_GONG_XI)){//是恭喜发财！
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
      //获取Lancher界面传递过来的红包语
    public static void setLuckyMoneySay(String sLuckyMoneySay){
    	mLuckyMoneySay=sLuckyMoneySay;
    }
    */
