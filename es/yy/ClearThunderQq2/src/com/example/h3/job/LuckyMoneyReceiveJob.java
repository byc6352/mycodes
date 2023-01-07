/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.byc.qqclearthunder2.R;
import com.example.h3.BackgroundMusic;
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
	private Context context;
	public  String TAG = "byc001";//调试标识：
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;
	private String mMoneyValue;//抢到红包金额；
	private String mSayThunder;//雷值；
	private String mManOfSendLuckyMoney;//发包者；

	private String[] mReceiveInfo={"","","","",""};//拆包信息；
	private int mIntInfo=0;//信息数；
	private boolean bRecycled=false;//是否退出循环
	
	private static final int KEY_CRASH_THUNDER_NO=0;//没有中雷；
	private static final int KEY_CRASH_THUNDER_YES=1;//中雷；
	private static final int KEY_CRASH_THUNDER_UNSURE=2;//不确定；
	private int iCrashThunder=0;//中雷类型；
	
	private LuckyMoneyReceiveJob(Context context) {
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
    public static synchronized LuckyMoneyReceiveJob getLuckyMoneyReceiveJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyReceiveJob(context);
        }
        return current;
        
    }
    /*判断是否是红包领完窗体*/
    public boolean isNoLuckyMoneyUI(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"来晚一步，红包被领完了",0);
    	if(aNode!=null&&aNode.getText()!=null){
    		if("来晚一步，红包被领完了".equals(aNode.getText().toString()))return true;else return false;
    	}
    	return false;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
     * 从红包语中获取总金额：
     */
    public String getTotalMoney(String say){
    	//判断第二位是否是0：
    	String totalMoney="";
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
    			if(iMoneySayPos==Config.KEY_THUNDER_Middle)//雷在中间，说明第一位为金额；
    				totalMoney=say.substring(0,1);//取总金额：
    			if(iMoneySayPos==Config.KEY_THUNDER_HEAD)//雷在左边，说明金额在右；
    				totalMoney=say.substring(say.length()-1,say.length());//取总金额：
    			if(iMoneySayPos==Config.KEY_THUNDER_TAIL)//雷在右边，说明金额在左；
    				totalMoney=say.substring(0,1);//取总金额：
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
    	String thunder=say.replace(totalMoney,"");
    	//排除雷在中间位且选择单雷的情况：
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	if(Config.iThunderNum==Config.KEY_THUNDER_SINGLE&&iMoneySayPos==Config.KEY_THUNDER_Middle){
    		thunder=thunder.substring(0,1);
    	}
    	return thunder;
    }
    /*
     * 获取金额中的雷：
     */
    public String getMoneyThunder(String  je){
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	String sMoneyThunder="";
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=je.substring(je.length()-1,je.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=je.substring(je.length()-2,je.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=je.substring(je.length()-4,je.length()-3);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		String sFen=je.substring(je.length()-1,je.length());
    		String sJiao=je.substring(je.length()-2,je.length()-1);
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
    public  boolean unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo SeeDetailNode=AccessibilityHelper.findNodeInfosByText(rootNode,"查看领取详情",0);
    	if(SeeDetailNode==null)return false;
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"已存入余额",0);
    	if(LuckyMoneyNode!=null&&LuckyMoneyNode.getText()!=null){
    		if("已存入余额".equals(LuckyMoneyNode.getText().toString())){
    			UnpackLuckyMoneyShowDigital();//抢包数字显示；
    			//获取金额和红包语：
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    			//是否取到正确的金额：
    			//if(!AccessibilityHelper.isMoney(mReceiveInfo[1]))return false;
    			//if(!AccessibilityHelper.isDigital(mReceiveInfo[1]))return false;
    			//判断是否是雷包：
    			if(checkLuckyMoneySayType(mReceiveInfo[0])==Config.TYPE_LUCKYMONEY_THUNDER){
    				LuckyMoneyDetailShow(mReceiveInfo[0],mReceiveInfo[1]);   
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
    public void LuckyMoneyDetailShow(String say,String je){
    	String sMoneyThunder=getMoneyThunder(je);//金额中的雷；
    	String totalMoney=getTotalMoney(say);//从红包语中分离出金额；
    	String sSayThunder=getSayThunder(say,totalMoney);//红包语中的雷;
    	int iThunder=CrashThunder(sSayThunder,sMoneyThunder);//中雷类型;

    	mMoneyValue=je;//金额;
    	mSayThunder=sSayThunder;//雷值；
    	mManOfSendLuckyMoney=mReceiveInfo[2];//抢包者
    	//显示结果：
    	showResult(iThunder);
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
			if(mIntInfo==2){//来自牛牛（技术员）
				if(info.getText()!=null)
					mReceiveInfo[2]=info.getText().toString();	
				else mReceiveInfo[2]="来自网友";	
			}
			if(mIntInfo==3){//红包语
				if(info.getText()!=null)
					mReceiveInfo[0]=info.getText().toString();	
				else mReceiveInfo[0]="10/1";	
			}
			if(mIntInfo==4){//金额：
				if(info.getText()!=null)
					mReceiveInfo[1]=info.getText().toString();	
				else mReceiveInfo[1]="0.01";
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
    		say="恭喜！抢到"+mManOfSendLuckyMoney+"的红包"+mMoneyValue+"元，雷值为："+mSayThunder+",避雷透视成功！";//未授权
    	}
    	//显示：
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
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
    	if(Config.iThunderNum>2)Config.iThunderNum=1;
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){//尾为雷
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//双雷;
    			sSayThunder2=sMoneySay.substring(2,3);
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
    	mMoneyValue=sMoneyValue;//金额;
    	mSayThunder=sSayThunder;//雷值；
    	mManOfSendLuckyMoney=mReceiveInfo[2];//抢包者
    	//显示结果：
    	showResult(bCrash);
    }
    */

}
