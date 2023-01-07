/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import util.BackgroundMusic;
import com.example.h3.Config;
import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.content.Context;
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
	
	private static final String DIGITAL="0123456789";//
	private String[] mReceiveInfo={"","","","",""};//拆包信息；
	private int mIntInfo=0;//信息数；

	private boolean bRecycled=false;//是否退出循环
	public int mLuckyMoneyType=0;//红包类别：已拆过，福利包，有雷包；
	private SpeechUtil speaker ;
	private BackgroundMusic mBackgroundMusic;
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
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
    //显示成功或失败界面：
    public void showAnimation(boolean bSuc,String money) {
    	/*
    	String say="";
    	if(!bSuc){
    		mBackgroundMusic.playBackgroundMusic( "ao.mp3", false);
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_FAIL;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_FAIL;
    		fwp.StartSwitchPics();
    		if(Config.bReg)
    			say="网络延迟！没抢到！";//授权
    		else
    			say="没抢到！试用版速度较慢！请购买正版，抢包速度加快100倍！";//未授权
    	}else{
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_SUCCESS;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_SUC;
    		fwp.StartSwitchPics();
    		if(money==null)
    			say="恭喜！抢到红包了！";//未授权
    		else
    			say="恭喜！抢到红包"+money+"元！";
    	}
    	//显示：
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    	*/
    	//fwp.StopSwitchPics();
    	//fwp.RemoveFloatingWindowPic();
    	//mBackgroundMusic.stopBackgroundMusic();
    } 
    
    
    
    public  void unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"已存入余额",0);
    	if(LuckyMoneyNode!=null){
    	
    			UnpackLuckyMoneyShowDigital();//抢包数字显示；
    			//获取金额和红包语：
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    	    	//躲避成功：抢到金额：3.0元；雷值为：7；避雷成功！
    	    	String sMoneyValue=mReceiveInfo[1];
    	    	String sMoneyFen=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());//分位
    	    	String sMoneyJao=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);//角位
    	    	String sMoneyYuan=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);//元位
    	    	int iMoneyFen=Integer.parseInt(sMoneyFen);
    	    	int iMoneyJao=Integer.parseInt(sMoneyJao);
    	    	int iMoneyYuan=Integer.parseInt(sMoneyYuan);
    	    	String sShow="";
    	    	int nn=1;
    	    	//获取牛牛玩法参数,判断抢到牛几：
    	    	int iNnWangFa=Config.getConfig(context).getNnWangFa();
    	    	//是否是试用版
    	    	boolean bReg=Config.getConfig(context).getREG();
    	    	switch(iNnWangFa){
    	    	case Config.NN_SINGLE:
    	    		nn=iMoneyFen;
    	    		break;
    	    	case Config.NN_DOUBLE:
    	    		nn=iMoneyFen+iMoneyJao;
    	    		break;
    	    	case Config.NN_THREE:
    	    		nn=iMoneyFen+iMoneyJao+iMoneyYuan;
    	    		break;
    	    	case Config.NN_HT:
    	    		nn=iMoneyFen+iMoneyYuan;
    	    		break;
    	    	case Config.NN_PG:
    	    		nn=iMoneyFen+iMoneyJao;
    	    		break;
    	    	}
    			if(nn>6){
    				sShow="恭喜！抢到牛"+nn+",牛牛透视成功！";
    			}
    			if(nn==0&&(iNnWangFa!=Config.NN_PG)){
    				sShow="恭喜！抢到牛牛,牛牛透视成功！";
    			}
    			if(nn>0&&nn<7){
    				if(bReg)
    					sShow="";
    				else
    					sShow="抢到牛"+nn+",试用版不享受本次透视服务！请购买正版！";
    			}
    			if(!sShow.equals("")){
    	    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    	    		speaker.speak(sShow);
    			}
    	}
    }
    private void recycleGetJeAndSay(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			if(info.getText()==null)return;
			if(Config.DEBUG)Log.i(Config.TAG, mIntInfo+info.getText().toString());
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
    //拆包显示数字：
    private void UnpackLuckyMoneyShowDigital() {
    	
    	speaker.speak("正在为您分析：");
    	float f=(float) (Math.random()*10000);
    	String s=String.valueOf(f);
    	Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
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
    
    public void unpackLuckyMoneyShow0(AccessibilityNodeInfo info){
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	String sMoneyValue=mReceiveInfo[2];//红包金额：
    	String sMoneyFen=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());//分位
    	String sMoneyJao=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);//角位
    	String sMoneyYuan=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);//元位
    	int iMoneyFen=Integer.parseInt(sMoneyFen);
    	int iMoneyJao=Integer.parseInt(sMoneyJao);
    	int iMoneyYuan=Integer.parseInt(sMoneyYuan);
    	String sShow="";
    	int nn=1;
    	//获取牛牛玩法参数,判断抢到牛几：
    	int iNnWangFa=Config.getConfig(context).getNnWangFa();
    	//是否是试用版
    	boolean bReg=Config.getConfig(context).getREG();
    	switch(iNnWangFa){
    	case Config.NN_SINGLE:
    		nn=iMoneyFen;
    		break;
    	case Config.NN_DOUBLE:
    		nn=iMoneyFen+iMoneyJao;
    		break;
    	case Config.NN_THREE:
    		nn=iMoneyFen+iMoneyJao+iMoneyYuan;
    		break;
    	case Config.NN_HT:
    		nn=iMoneyFen+iMoneyYuan;
    		break;
    	case Config.NN_PG:
    		nn=iMoneyFen+iMoneyJao;
    		break;
    	}
		if(nn>6){
			sShow="恭喜！抢到牛"+nn+",牛牛透视成功！";
		}
		if(nn==0&&(iNnWangFa!=Config.NN_PG)){
			sShow="恭喜！抢到牛牛,牛牛透视成功！";
		}
		if(nn>0&&nn<7){
			if(bReg)
				sShow="";
			else
				sShow="抢到牛"+nn+",试用版不享受本次透视服务！请购买正版！";
		}
		if(!sShow.equals("")){
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		speaker.speak(sShow);
		}
    }

}
