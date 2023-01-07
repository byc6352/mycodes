/**
 * 
 */
package com.example.h3.job;

import com.byc.PutThunderTest.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.util.FloatingWindowPic;

import android.content.Context;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityEvent;
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
	private SpeechUtil speeker ;
	//---------类变量----------------------------------------------------------------------------
	Rect mBounds;
	//----------------------------------------------------------------------------------------
	private FloatingWindowPic fws1;//显示图片浮动窗口
	private FloatingWindowPic fws2;//显示图片浮动窗口
	private FloatingWindowPic fws3;//显示图片浮动窗口
	private FloatingWindowPic fws4;//显示图片浮动窗口
	private FloatingWindowPic fws5;//显示图片浮动窗口
	
	private FloatingWindowPic fws6;//显示图片浮动窗口
	private FloatingWindowPic fws7;//显示图片浮动窗口
	private FloatingWindowPic fws8;//显示图片浮动窗口
	private FloatingWindowPic fws9;//显示图片浮动窗口
	private FloatingWindowPic fws10;//显示图片浮动窗口
	
	private FloatingWindowPic fws11;//显示图片浮动窗口
	private FloatingWindowPic fws12;//显示图片浮动窗口
	private FloatingWindowPic fws13;//显示图片浮动窗口
	private FloatingWindowPic fws14;//显示图片浮动窗口
	private FloatingWindowPic fws15;//显示图片浮动窗口
	private FloatingWindowPic fwsLucky;//显示图片浮动窗口
	private FloatingWindowPic fwsLuckyNone;//显示图片浮动窗口
	private int i=1;
	private int mLuckyMoneyCount=0;//包数；
	private String mThunderNum="";//包数；
	
	
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
    	
        fws1=new FloatingWindowPic(context,R.layout.float_smallpic1);
        fws2=new FloatingWindowPic(context,R.layout.float_smallpic2);
        fws3=new FloatingWindowPic(context,R.layout.float_smallpic3);
        fws4=new FloatingWindowPic(context,R.layout.float_smallpic4);
        fws5=new FloatingWindowPic(context,R.layout.float_smallpic5);
        fws6=new FloatingWindowPic(context,R.layout.float_smallpic6);
        fws7=new FloatingWindowPic(context,R.layout.float_smallpic7);
        fws8=new FloatingWindowPic(context,R.layout.float_smallpic8);
        fws9=new FloatingWindowPic(context,R.layout.float_smallpic9);
        fws10=new FloatingWindowPic(context,R.layout.float_smallpic10);
        fws11=new FloatingWindowPic(context,R.layout.float_smallpic11);
        fws12=new FloatingWindowPic(context,R.layout.float_smallpic12);
        fws13=new FloatingWindowPic(context,R.layout.float_smallpic13);
        fws14=new FloatingWindowPic(context,R.layout.float_smallpic14);
        fws15=new FloatingWindowPic(context,R.layout.float_smallpic15);
        
        fwsLucky=new FloatingWindowPic(context,R.layout.float_lucky);
        fwsLuckyNone=new FloatingWindowPic(context,R.layout.float_luckynone);
    }
    
    private void recycleReplacePic(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			//mReceiveInfo[mIntInfo]=info.getText().toString();

			//Log.i(TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "Text：" + info.getText());
			//Log.i(TAG, "windowId:" + info.getWindowId());
			if( info.getText()==null)return;
			String je=info.getText().toString();
			info.getBoundsInScreen(mBounds);
			//取雷值：
			if(mIntInfo==2){
				mThunderNum=je.substring(je.length()-1, je.length());
			}
			//替换手气最佳：
			if(je.equals("手气最佳")){
				if(i==1&&mLuckyMoneyCount==4){
					
				}else if(i==2&&mLuckyMoneyCount==2){
					
				}else if(i==3&&mLuckyMoneyCount==4){
					
				}else {
					fwsLuckyNone.SetFloatViewPara(mBounds.left-34,mBounds.top-50,146,38);
					fwsLuckyNone.ShowFloatingWindow();
				}
			}
			//替换数值：
			if(je.indexOf("元")>-1){
				
				if(mIntInfo==6||mIntInfo==7)mLuckyMoneyCount=1;//第一个包
				else if(mIntInfo==9||mIntInfo==10)mLuckyMoneyCount=2;//第一个包
				else if(mIntInfo==12||mIntInfo==13)mLuckyMoneyCount=3;//第一个包
				else if(mIntInfo==15||mIntInfo==16)mLuckyMoneyCount=4;//第一个包
				else if(mIntInfo==18||mIntInfo==19)mLuckyMoneyCount=5;//第一个包
				switch(mLuckyMoneyCount){
				case 1:
					//--------------------------------1----------------------------------
					switch(i){
					case 1:
						
						fws1.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws1.ShowFloatingWindow();
						break;
					case 2:
						
						fws6.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws6.ShowFloatingWindow();
						break;
					case 3:
						
						fws11.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws11.ShowFloatingWindow();
						break;
					}
					//--------------------------------1----------------------------------
					break;
				case 2:
					//--------------------------------2----------------------------------
					switch(i){
					case 1:
						
						fws2.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws2.ShowFloatingWindow();
						break;
					case 2:
						
						fws7.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws7.ShowFloatingWindow();
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 3:
						
						fws12.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws12.ShowFloatingWindow();
						break;
					}
					//--------------------------------2----------------------------------
					break;
				case 3:
					//--------------------------------3----------------------------------
					switch(i){
					case 1:
						
						fws3.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws3.ShowFloatingWindow();

						break;
					case 2:
						
						fws8.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws8.ShowFloatingWindow();
						break;
					case 3:
						
						fws13.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws13.ShowFloatingWindow();
						break;
					}
					//-------------------------------3---------------------------------
					break;
				case 4:
					//-------------------------------4----------------------------------
					switch(i){
					case 1:
						
						fws4.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws4.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 2:
						
						fws9.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws9.ShowFloatingWindow();
						break;
					case 3:
						
						fws14.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws14.ShowFloatingWindow();
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					}
					//--------------------------------4----------------------------------
					break;
				case 5:
					//--------------------------------5----------------------------------
					switch(i){
					case 1:
						
						fws5.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws5.ShowFloatingWindow();
						break;
					case 2:
						
						fws10.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws10.ShowFloatingWindow();
						break;
					case 3:
						
						fws15.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws15.ShowFloatingWindow();
						break;
					}
					//--------------------------------5----------------------------------
					break;
				}//switch(mLuckyMoneyCount){
				
			}//if(je.indexOf("元")>-1){

			if(mIntInfo>=19){bRecycled=true;return;}
			mIntInfo=mIntInfo+1;
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycleReplacePic(info.getChild(i));
				}
			}
		}
	}
    
    private void recycleReplacePic1(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			//mReceiveInfo[mIntInfo]=info.getText().toString();

			//Log.i(TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "Text：" + info.getText());
			//Log.i(TAG, "windowId:" + info.getWindowId());
			if(mIntInfo==6){
				info.getBoundsInScreen(mBounds);
	        	fws1.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws1.ShowFloatingWindow();
	        	mLuckyMoneyCount=1;
			}
			if(mIntInfo==9){
				info.getBoundsInScreen(mBounds);
	        	fws2.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws2.ShowFloatingWindow();
	        	mLuckyMoneyCount=2;
			}
			if(mIntInfo==12){
				info.getBoundsInScreen(mBounds);
	        	fws3.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws3.ShowFloatingWindow();
	        	mLuckyMoneyCount=3;
			}
			if(mIntInfo==15){
				info.getBoundsInScreen(mBounds);
	        	fws4.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws4.ShowFloatingWindow();
	        	mLuckyMoneyCount=4;
			}
			if(mIntInfo==18){
				info.getBoundsInScreen(mBounds);
	        	fws5.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws5.ShowFloatingWindow();
	        	mLuckyMoneyCount=5;
			}
			if(mIntInfo>=18){bRecycled=true;return;}
			mIntInfo=mIntInfo+1;
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycleReplacePic1(info.getChild(i));
				}
			}
		}
	}

    private void recycleReplacePic2(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			//mReceiveInfo[mIntInfo]=info.getText().toString();

			//Log.i(TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "Text：" + info.getText());
			//Log.i(TAG, "windowId:" + info.getWindowId());
			if(mIntInfo==6){
				info.getBoundsInScreen(mBounds);
	        	fws6.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws6.ShowFloatingWindow();
	        	mLuckyMoneyCount=1;
			}
			if(mIntInfo==9){
				info.getBoundsInScreen(mBounds);
	        	fws7.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws7.ShowFloatingWindow();
	        	mLuckyMoneyCount=2;
			}
			if(mIntInfo==12){
				info.getBoundsInScreen(mBounds);
	        	fws8.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws8.ShowFloatingWindow();
	        	mLuckyMoneyCount=3;
			}
			if(mIntInfo==15){
				info.getBoundsInScreen(mBounds);
	        	fws9.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws9.ShowFloatingWindow();
	        	mLuckyMoneyCount=4;
			}
			if(mIntInfo==18){
				info.getBoundsInScreen(mBounds);
	        	fws10.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws10.ShowFloatingWindow();
	        	mLuckyMoneyCount=5;
			}
			if(mIntInfo>=18){bRecycled=true;return;}
			mIntInfo=mIntInfo+1;
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycleReplacePic2(info.getChild(i));
				}
			}
		}
	}
    
    private void recycleReplacePic3(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			//mReceiveInfo[mIntInfo]=info.getText().toString();

			//Log.i(TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "Text：" + info.getText());
			//Log.i(TAG, "windowId:" + info.getWindowId());
			if(mIntInfo==6){
				info.getBoundsInScreen(mBounds);
	        	fws11.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws11.ShowFloatingWindow();
	        	mLuckyMoneyCount=1;
			}
			if(mIntInfo==9){
				info.getBoundsInScreen(mBounds);
	        	fws12.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws12.ShowFloatingWindow();
	        	mLuckyMoneyCount=2;
			}
			if(mIntInfo==12){
				info.getBoundsInScreen(mBounds);
	        	fws13.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws13.ShowFloatingWindow();
	        	mLuckyMoneyCount=3;
			}
			if(mIntInfo==15){
				info.getBoundsInScreen(mBounds);
	        	fws14.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws14.ShowFloatingWindow();
	        	mLuckyMoneyCount=4;
			}
			if(mIntInfo==18){
				info.getBoundsInScreen(mBounds);
	        	fws15.SetFloatViewPara(mBounds.left,mBounds.top-50,94,43);
	        	fws15.ShowFloatingWindow();
	        	mLuckyMoneyCount=5;
			}
			if(mIntInfo>=18){bRecycled=true;return;}
			mIntInfo=mIntInfo+1;
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycleReplacePic3(info.getChild(i));
				}
			}
		}
	}
    public void ReplacePics(AccessibilityEvent event){
    	String sClassName=event.getClassName().toString();
    	AccessibilityNodeInfo rootNode=event.getSource();
    	if(rootNode==null)return;
    	if(sClassName.equals(Config.WINDOW_LUCKYMONEY_DETAILUI)){
    		String sShow="";
    		mBounds=new Rect();
    		mLuckyMoneyCount=0;//包数
    		mIntInfo=1;//控件数
    		bRecycled=false;
    		recycleReplacePic(rootNode);
    		/*
    	switch(i){
    	case 1: 
    		recycleReplacePic1(rootNode);
        	//sShow="恭喜！共有5人中雷。";
        	break;
    	case 2:
        	//fwb2.SetFloatViewPara(243, 481-50,233,140);
        	//fwb2.ShowFloatingWindow();   
        	//fws2.SetFloatViewPara(594,794-50,94,43);
        	//fws2.ShowFloatingWindow();
    		recycleReplacePic2(rootNode);
        	//sShow="恭喜！抢到牛牛。值为：6.19元";
        	break;
    	case 3:
        	//fwb3.SetFloatViewPara(243, 481-50,233,140);
        	//fwb3.ShowFloatingWindow();   
        	//fws3.SetFloatViewPara(594,794-50,94,43);
        	//fws3.ShowFloatingWindow();
    		recycleReplacePic3(rootNode);
        	//sShow="恭喜！抢到牛牛。值为：6.00元";
        	break;
    	}
    	*/
    	if(i==1)sShow="恭喜！共有5人中雷。雷值为："+mThunderNum;
    	else sShow="恭喜！共有4人中雷。雷值为："+mThunderNum;
		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
		speeker.speak(sShow);
    	i=i+1;
    	if(i>3)i=1;
		}else{
			//fw.DestroyFloatingWindow();
			fws1.RemoveFloatingWindowPic();
			fws2.RemoveFloatingWindowPic();
			fws3.RemoveFloatingWindowPic();
			fws4.RemoveFloatingWindowPic();
			fws5.RemoveFloatingWindowPic();
			fws6.RemoveFloatingWindowPic();
			fws7.RemoveFloatingWindowPic();
			fws8.RemoveFloatingWindowPic();
			fws9.RemoveFloatingWindowPic();
			fws10.RemoveFloatingWindowPic();
			fws11.RemoveFloatingWindowPic();
			fws12.RemoveFloatingWindowPic();
			fws13.RemoveFloatingWindowPic();
			fws14.RemoveFloatingWindowPic();
			fws15.RemoveFloatingWindowPic();
			fwsLucky.RemoveFloatingWindowPic();
			fwsLuckyNone.RemoveFloatingWindowPic();
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
  
}
