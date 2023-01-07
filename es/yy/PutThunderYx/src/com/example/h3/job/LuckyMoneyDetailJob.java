/**
 * 
 */
package com.example.h3.job;

import com.example.h3.Config;
import com.example.h3.MainActivity;

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
	private String[] mReceiveInfo={"","",""};//拆包信息；
	private int mIntInfo=0;//信息数；
	private boolean bReg=false;//是否注册；=
	private boolean bRecycled=false;//是否退出循环
	private SpeechUtil speeker ;
	
	private static final String txtClassName="android.widget.TextView";//文本控件；
	private int mLMrobed=0;//已领取个数；
	private int mLMsum=0;//总个数；
	private double mRobedJE=0;//已领取金额；
	private double mSumJE=0;//总金额；
	private double mRobJE=0;//未领取金额;
	private String mThunder="";
	
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	bReg=Config.getConfig(context).getREG();
    	speeker=SpeechUtil.getSpeechUtil(context);
    }
    //
    public boolean getRobLuckyMoney(AccessibilityNodeInfo rootNode){
    	if(rootNode==null)return false;
    	//数值初始化：
    	mLMrobed=0;//已领取个数；
    	mLMsum=0;//总个数；
    	mRobedJE=0;//已领取金额；
    	mSumJE=0;//总金额；
    	mRobJE=0;//未领取金额;
    	mThunder="";//雷值；
    	//判断是否本人是否领取了红包：
    	AccessibilityNodeInfo robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 4, true);
    	if(robNode==null)return false;
    	String robed=String.valueOf(robNode.getText());
    	if(robed==null)return false;
    	if(robed.indexOf("已存入零钱，")!=-1){//本人已领取过：显示领取金额：
        	robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 2, false);
        	if(robNode==null)return false;
        	String myJE=String.valueOf(robNode.getText());
        	if(myJE==null)return false;
        	String say="恭喜！抢到红包："+myJE+"元。扫尾成功！";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
			Config.robTailAction=Config.ACTION_CLOSE;
			Config.JobState=Config.STATE_NONE;
			return true;
    	}
    	//本人未领取过红包：获取抢包情况信息：已领取0/5个，共0.00/1.00元
    	robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 2, false);
    	if(robNode==null)return false;
    	String numInfo=String.valueOf(robNode.getText());
    	if(numInfo==null)return false;
    	int robInfo=getRobLuckyMoneyInfo(numInfo);
    	if(robInfo==-1)return false;
    	//获取雷值：10/7
    	robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 1, false);
    	if(robNode==null)return false;
    	numInfo=String.valueOf(robNode.getText());
    	if(numInfo==null)return false;
    	if(!getThunder(numInfo))return false;
    	//判断最后一包是否碰雷：
    	if(Config.robTailAction==Config.ACTION_CLICK){
    		//计算未领金额：
    		int iSumJE=(int) (mSumJE*100);
    		int iRobedJE=(int) (mRobedJE*100);
    		int iRobJE=iSumJE-iRobedJE;
    		//mRobJE=mSumJE-mRobedJE;
    		//是否碰雷
    		//String robJE=Double.toString(mRobJE);
    		String robJE=String.valueOf(iRobJE);
    		String tail=robJE.substring(robJE.length()-1,robJE.length());
    		if(tail.equals(mThunder)){//最后一包为雷；
    			Config.robTailAction=Config.ACTION_CLOSE;
    			double dRobJE=iRobJE/100;
    			robJE=String.valueOf(dRobJE);
    			String say="最后一包为雷。值为："+robJE+"元。不抢。";
    			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
    			speeker.speak(say);
    		}
    	}
    	//返回Launcher界面：
    	//AccessibilityHelper.performBack(service);
    	return true;
    }
    //返回包的领取情况：0未领完；1只有尾包；2已领完；-1错误；
    public int getRobLuckyMoneyInfo(String numInfo){
    	int i=numInfo.indexOf("/");
    	int j=numInfo.indexOf("个");
    	int k=numInfo.indexOf("共");
    	if(i==-1)return -1;
    	if(j==-1)return -1;
    	if(k==-1)return -1;
    	String sRobed=numInfo.substring(3,i);
    	String sSum=numInfo.substring(i+1,j);
    	if(!AccessibilityHelper.isDigital(sRobed))return -1;
    	if(!AccessibilityHelper.isDigital(sSum))return -1;
    	
    	mLMrobed=Integer.valueOf(sRobed);//已领取个数；
    	mLMsum=Integer.valueOf(sSum);//总个数；
    	//领取金额与总金额:
    	String jeInfo=numInfo.substring(k+1,numInfo.length()-1);
    	i=jeInfo.indexOf("/");
    	String jeRobed=jeInfo.substring(0,i);//已领取金额；
    	String jeSum=jeInfo.substring(i+1);//总金额
    	if(!AccessibilityHelper.isDigital(jeRobed))return -1;
    	if(!AccessibilityHelper.isDigital(jeSum))return -1;  
    	mRobedJE=Double.valueOf(jeRobed);
    	mSumJE=Double.valueOf(jeSum);
    	int iRes=mLMsum-mLMrobed;
    	if(iRes==1){//剩尾包
    		Config.robTailAction=Config.ACTION_CLICK;//剩余尾包；
    		return 1;//
    	}
    	if(iRes==0){
    		Config.robTailAction=Config.ACTION_CLOSE;//已领完；
    		return 2;//
    	}
    	if(iRes>1){
    		Config.robTailAction=Config.ACTION_LOOK;//剩余包大于1；
    		return 0;//
    	}
    	return -1;
    }
    //判断是否是雷包：
    public boolean getThunder(String say){
    	String thunder=say.substring(say.length()-1,say.length());
    	if(!AccessibilityHelper.isDigital(thunder))return false;
    	mThunder=thunder;
    	return true;
    }
    
    
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
    			speeker.speak(sShow);
    		}
    	}else{
    		//避雷成功：
    		sShow="恭喜！抢到红包"+sMoneyValue+"元，雷值为："+sSayThunder+",避雷透视成功！";
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		//speeker.speak(sShow);
    		speeker.speak("恭喜！抢到红包"+sMoneyValue+"元");
    		speeker.speak("雷值为："+sSayThunder+",避雷透视成功！");
    	}
    }

}
