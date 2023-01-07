/**
 * 
 */
package com.example.h3.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.h3.Config;
import com.example.h3.FloatingWindowCount;

import accessibility.AccessibilityHelper;
import util.Funcs;
import util.SpeechUtil;

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
	private boolean bReg=false;//是否注册；
	private boolean bRecycled=false;//是否退出循环
	private SpeechUtil speaker ;
	
	public int[] mThunderCounts=new int[10];//显示雷的统计值；
	private String[] mMoneys=new String[24];//存储金额;
	private int mMoneyCount=0;//已抢包的数量；
	
	private String[] mPreMoneys=new String[24];//前一个页面存储金额;
	private int mPreMoneyCount=0;//前一个页面已抢包的数量；
	private FloatingWindowCount mFWC;
	public boolean mIsShow=false;
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	bReg=Config.bReg;
    	speaker=SpeechUtil.getSpeechUtil(context);
    	mFWC=FloatingWindowCount.getInstance(context);
    }
    /*显示详细信息界面*/
    public void showFloatWindow(AccessibilityNodeInfo rootNode){
    	//1.判断是否是过期页面，超过1分钟的页面不处理；
    	AccessibilityNodeInfo nodeTime=AccessibilityHelper.findNodeInfosByText(rootNode,":",0);
    	if(nodeTime==null||nodeTime.getText()==null)return;
    	String sTime=nodeTime.getText().toString();
    	if(isOverdueUI(sTime))return ;
    	//2.判断是否有剩余包未领取，包未领取完，不处理；
    	List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByText("元");
        if(list == null || list.isEmpty()) return;
        int i=0;
        for(AccessibilityNodeInfo nodeInfo:list){
        	String txt=nodeInfo.getText().toString();
        	if(txt.contains("已领取"))return;//包未领取完，不处理:已领取
            if("0123456789".contains(txt.substring(0,1))&&"元".contains(txt.substring(txt.length()-1,txt.length()))){//第一位为数字，最后一位为：元
            	mMoneys[i]=txt.substring(0,txt.length()-1);
            	i=i+1;
            }
        }
        mMoneyCount=i;
        //3.判断是否是前后两次打开相同的页面；如果 是，不处理；
        if(isCalculatedUI(mPreMoneys,mMoneys,mPreMoneyCount,mMoneyCount))return ;
        //4.保存雷值；
        i=0;
        for(String money:mMoneys){
        	if(money==null)break;
        	String sThunder=money.substring(money.length()-1,money.length());
        	int iThunder=Funcs.str2int(sThunder);
        	if(iThunder==-1)return;
        	mThunderCounts[iThunder]=mThunderCounts[iThunder]+1;
        	i=i+1;
        	if(i>=mMoneyCount)break;
        }
        //5.保存金额值；
        saveMoneys();
        //6.显示悬浮窗口；
        mFWC.setThunderCounts(mThunderCounts);
        mFWC.ShowFloatingWindow();
        mIsShow=true;
    }
    /*判断是否是过期界面01:27:54*/
    public boolean isOverdueUI(String sTime){
    	if(sTime.length()!=8)return true;
    	int h=Funcs.str2int(sTime.substring(0,2));
    	if(h==-1)return true;
    	int m=Funcs.str2int(sTime.substring(3,5));
    	if(m==-1)return true;
    	int s=Funcs.str2int(sTime.substring(6,8));
    	if(s==-1)return true;
    	int totalSec=h*60*60+m*60+s;
    	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    	String sTime2 = formatter.format(new Date());
    	int h2=Funcs.str2int(sTime2.substring(0,2));
    	if(h2==-1)return true;
    	int m2=Funcs.str2int(sTime2.substring(3,5));
    	if(m2==-1)return true;
    	int s2=Funcs.str2int(sTime2.substring(6,8));
    	if(s2==-1)return true;
    	int totalSec2=h2*60*60+m2*60+s2;
    	if(totalSec2-totalSec>60)return true;//超过1分钟不统计；
    	return false;
    }
    /*判断是否是计算过的界面01:27:54*/
    public boolean isCalculatedUI(String[] PreMoneys,String[] Moneys,int PreMoneyCount,int MoneyCount){
    	if(PreMoneyCount!=MoneyCount)return false;
    	for(int i=0;i<MoneyCount;i++){
    		if(!PreMoneys[i].equals(Moneys[i]))return false;
    	}
    	return true;
    }
    /*保存界面的金额值01:27:54*/
    public void saveMoneys(){
    	mPreMoneyCount=mMoneyCount;
    	for(int i=0;i<mMoneyCount;i++){
    		mPreMoneys[i]=mMoneys[i];
    	}
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
