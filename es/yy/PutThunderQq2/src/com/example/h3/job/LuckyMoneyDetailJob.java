/**
 * 
 */
package com.example.h3.job;

import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.util.SpeechUtil;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyDetailJob {
	private static LuckyMoneyDetailJob current;
	private Context context;
	private String mSayThunder="";//红包语 中 的雷值
	private String mSayThunder2="";//红包语 中 的雷值（临时存储）；
	private String mTotalMoneyCount="";//总金额
	private String mSendMan="";//红包发送者
	private String mLMcount="";//红包个数
	private String[] mReceiveInfo={"","","",""};//拆包信息【红包语 ，总金额，红包发送者，红包个数】
	private String[] mTmp={"",""};//临时存储器
	private String[][] mJeInfo;//领取金额信息；
	private int mRobbedCount=0;//领取数量；
	private int mCrashedCount=0;//中雷人数量；
	private int mIntInfo=0;//信息数；
	private boolean bReg=false;//是否注册；
	private boolean bRecycled=false;//是否退出循环
	private SpeechUtil speaker ;
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	bReg=Config.getConfig(context).getREG();
    	speaker=SpeechUtil.getSpeechUtil(context);
    	mJeInfo=new String[4][20];
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
    	//AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"收到的红包已存入余额    余额使用",0);
    	//if(LuckyMoneyNode!=null&&LuckyMoneyNode.getText()!=null){
    		//if("收到的红包已存入余额    余额使用".equals(LuckyMoneyNode.getText().toString())){
    			//获取金额和红包语：
    			bRecycled=false;
    			mIntInfo=1;
    			mRobbedCount=0;//领取红包数量；
    			mCrashedCount=0;//中雷数量;
    			recycleGetJeAndSay(rootNode);
    			//中雷显示：
    			return isCrashShow();
    		//}
    	//}
    	//return false;
    }
    //获取金额和红包语
    private void recycleGetJeAndSay(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			if(mIntInfo==4){//1.红包语
				if(info.getText()!=null){
					mReceiveInfo[0]=info.getText().toString();	
					getSayThunder(mReceiveInfo[0]);//获取红包语雷值
				}
				else mReceiveInfo[0]="1";	
			}
			if(mIntInfo==5){//2.总金额：共0.01元
				if(info.getText()!=null){
					mReceiveInfo[1]=info.getText().toString();	
				    //mReceiveInfo[3]="共10.00元";//测试------------------
				}
				else mReceiveInfo[1]="共0.01元";	
			}
			if(mIntInfo==6){//3.领取人：牛牛（招代理）的红包
				if(info.getText()!=null)
					mReceiveInfo[2]=info.getText().toString();	
				else mReceiveInfo[2]="牛牛（招代理）的红包";	
			}
			if(mIntInfo==7){//3.领取数量：
				if(info.getText()!=null)
					mReceiveInfo[3]=info.getText().toString();	
				else mReceiveInfo[3]="2个红包，已领完";	
			}
			//领取金额 ：
			if(mIntInfo>7){
				getMoney(info);
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
    /*是否是金额并获取金额*/
    public  boolean getMoney(AccessibilityNodeInfo node){ 
    	if(node.getText()==null)return false;
    	String money=node.getText().toString();
    	mTmp[1]=money;//存储现在值；
    	boolean b=getMoneyThunder(money);
    	mTmp[0]=money;//存储前一个值；
    	return b;
    }
    /*获取金额中的雷值*/
    public  boolean getMoneyThunder(String money){  
    	if(money.length()<2)return false;
    	String yuan=money.substring(money.length()-1,money.length());
    	if(yuan.equals("元")){
    		String moneyThunder=money.substring(money.length()-2,money.length()-1);
    		if(AccessibilityHelper.isDigital(moneyThunder)){//是金额：
    			mJeInfo[0][mRobbedCount]=mTmp[0];//领取人；
    			mJeInfo[1][mRobbedCount]=money;//金额；
    			mJeInfo[2][mRobbedCount]=moneyThunder;//雷值；
    			if(isPersonCrash(moneyThunder)){
    				mJeInfo[3][mRobbedCount]="1";//中雷标志；
    				mCrashedCount=mCrashedCount+1;//中雷数量;
    			}
    			mRobbedCount=mRobbedCount+1;
    			return true;
    		}
    	}
    	return false;
    }
    /*获取红包语中的雷值*/
    public  boolean getSayThunder(String say){  
    	if(say.length()==0)return false;
    	mSayThunder=say;
    	mSayThunder2=say;
    	return true;
    }
    /*个人是否中雷*/
    public  boolean isPersonCrash(String moneyThunder){  
    	if(mSayThunder.indexOf(moneyThunder)==-1)return  false;
    	if(mSayThunder2.length()>0)
    		mSayThunder2=mSayThunder2.replace(moneyThunder, "");
    	return true;
    }
    /*信息显示*/
    public  boolean isCrashShow(){ 
    	String say="";
    	boolean bCrash=false;
    	if(mSayThunder2.equals("")){//中雷了：
    		say="共有："+mCrashedCount+"人中雷！";
    		bCrash=true;
    	}else{//if(mSayThunder2.equals("")){//中雷了：
    		say="没有人中雷！";
    	}
		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
		//speeker.speak(sShow);
		speaker.speak(say);
		return bCrash;
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
    	String sMoneyThunder="";
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){
    		sSayThunder=sMoneySay.substring(0,1);
    	}
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
    	}
    	String sShow="";
    	//1。避雷成功判断：
    	if(sMoneyThunder.equals(sSayThunder)){
    		//碰雷：
    		//1。是试用版：2。正版：不显示；
    		sShow="试用版不享受本次透视服务！请购买正版！";
    		if(!bReg){
    			Toast.makeText(context, "试用版不享受本次透视服务！请购买正版！", Toast.LENGTH_LONG).show();
    			speaker.speak(sShow);
    		}
    	}else{
    		//避雷成功：
    		sShow="恭喜！抢到红包"+sMoneyValue+"元，雷值为："+sSayThunder+",避雷透视成功！";
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		//speeker.speak(sShow);
    		speaker.speak("恭喜！抢到红包"+sMoneyValue+"元");
    		speaker.speak("雷值为："+sSayThunder+",避雷透视成功！");
    	}
    }

}
