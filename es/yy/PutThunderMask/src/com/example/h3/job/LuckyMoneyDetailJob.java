/**
 * 
 */
package com.example.h3.job;

import com.byc.PutThunderMask.R;
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
	private boolean bRecycled=false;//是否退出循环
	private boolean mSuccess=true;
	private SpeechUtil speeker ;
	//---------类变量----------------------------------------------------------------------------
	Rect mBounds;
	//----------------------------------------------------------------------------------------
	private FloatingWindowPic fws01;//显示图片浮动窗口
	private FloatingWindowPic fws02;//显示图片浮动窗口
	private FloatingWindowPic fws03;//显示图片浮动窗口
	private FloatingWindowPic fws04;//显示图片浮动窗口
	private FloatingWindowPic fws05;//显示图片浮动窗口
	
	private FloatingWindowPic fws11;//显示图片浮动窗口
	private FloatingWindowPic fws12;//显示图片浮动窗口
	private FloatingWindowPic fws13;//显示图片浮动窗口
	private FloatingWindowPic fws14;//显示图片浮动窗口
	private FloatingWindowPic fws15;//显示图片浮动窗口
	
	private FloatingWindowPic fws21;//显示图片浮动窗口
	private FloatingWindowPic fws22;//显示图片浮动窗口
	private FloatingWindowPic fws23;//显示图片浮动窗口
	private FloatingWindowPic fws24;//显示图片浮动窗口
	private FloatingWindowPic fws25;//显示图片浮动窗口
	
	private FloatingWindowPic fws31;//显示图片浮动窗口
	private FloatingWindowPic fws32;//显示图片浮动窗口
	private FloatingWindowPic fws33;//显示图片浮动窗口
	private FloatingWindowPic fws34;//显示图片浮动窗口
	private FloatingWindowPic fws35;//显示图片浮动窗口
	
	private FloatingWindowPic fws41;//显示图片浮动窗口
	private FloatingWindowPic fws42;//显示图片浮动窗口
	private FloatingWindowPic fws43;//显示图片浮动窗口
	private FloatingWindowPic fws44;//显示图片浮动窗口
	private FloatingWindowPic fws45;//显示图片浮动窗口
	
	private FloatingWindowPic fws51;//显示图片浮动窗口
	private FloatingWindowPic fws52;//显示图片浮动窗口
	private FloatingWindowPic fws53;//显示图片浮动窗口
	private FloatingWindowPic fws54;//显示图片浮动窗口
	private FloatingWindowPic fws55;//显示图片浮动窗口
	
	private FloatingWindowPic fws61;//显示图片浮动窗口
	private FloatingWindowPic fws62;//显示图片浮动窗口
	private FloatingWindowPic fws63;//显示图片浮动窗口
	private FloatingWindowPic fws64;//显示图片浮动窗口
	private FloatingWindowPic fws65;//显示图片浮动窗口
	
	private FloatingWindowPic fws71;//显示图片浮动窗口
	private FloatingWindowPic fws72;//显示图片浮动窗口
	private FloatingWindowPic fws73;//显示图片浮动窗口
	private FloatingWindowPic fws74;//显示图片浮动窗口
	private FloatingWindowPic fws75;//显示图片浮动窗口
	
	private FloatingWindowPic fws81;//显示图片浮动窗口
	private FloatingWindowPic fws82;//显示图片浮动窗口
	private FloatingWindowPic fws83;//显示图片浮动窗口
	private FloatingWindowPic fws84;//显示图片浮动窗口
	private FloatingWindowPic fws85;//显示图片浮动窗口
	
	private FloatingWindowPic fws91;//显示图片浮动窗口
	private FloatingWindowPic fws92;//显示图片浮动窗口
	private FloatingWindowPic fws93;//显示图片浮动窗口
	private FloatingWindowPic fws94;//显示图片浮动窗口
	private FloatingWindowPic fws95;//显示图片浮动窗口
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
    	speeker=SpeechUtil.getSpeechUtil(context);
    	
        fws01=new FloatingWindowPic(context,R.layout.float_smallpic01);
        fws02=new FloatingWindowPic(context,R.layout.float_smallpic02);
        fws03=new FloatingWindowPic(context,R.layout.float_smallpic03);
        fws04=new FloatingWindowPic(context,R.layout.float_smallpic04);
        fws05=new FloatingWindowPic(context,R.layout.float_smallpic05);

        fws11=new FloatingWindowPic(context,R.layout.float_smallpic11);
        fws12=new FloatingWindowPic(context,R.layout.float_smallpic12);
        fws13=new FloatingWindowPic(context,R.layout.float_smallpic13);
        fws14=new FloatingWindowPic(context,R.layout.float_smallpic14);
        fws15=new FloatingWindowPic(context,R.layout.float_smallpic15);
        
        fws21=new FloatingWindowPic(context,R.layout.float_smallpic21);
        fws22=new FloatingWindowPic(context,R.layout.float_smallpic22);
        fws23=new FloatingWindowPic(context,R.layout.float_smallpic23);
        fws24=new FloatingWindowPic(context,R.layout.float_smallpic24);
        fws25=new FloatingWindowPic(context,R.layout.float_smallpic25);
        
        fws31=new FloatingWindowPic(context,R.layout.float_smallpic31);
        fws32=new FloatingWindowPic(context,R.layout.float_smallpic32);
        fws33=new FloatingWindowPic(context,R.layout.float_smallpic33);
        fws34=new FloatingWindowPic(context,R.layout.float_smallpic34);
        fws35=new FloatingWindowPic(context,R.layout.float_smallpic35);
        
        fws41=new FloatingWindowPic(context,R.layout.float_smallpic41);
        fws42=new FloatingWindowPic(context,R.layout.float_smallpic42);
        fws43=new FloatingWindowPic(context,R.layout.float_smallpic43);
        fws44=new FloatingWindowPic(context,R.layout.float_smallpic44);
        fws45=new FloatingWindowPic(context,R.layout.float_smallpic45);
        
        fws51=new FloatingWindowPic(context,R.layout.float_smallpic51);
        fws52=new FloatingWindowPic(context,R.layout.float_smallpic52);
        fws53=new FloatingWindowPic(context,R.layout.float_smallpic53);
        fws54=new FloatingWindowPic(context,R.layout.float_smallpic54);
        fws55=new FloatingWindowPic(context,R.layout.float_smallpic55);
        
        fws61=new FloatingWindowPic(context,R.layout.float_smallpic61);
        fws62=new FloatingWindowPic(context,R.layout.float_smallpic62);
        fws63=new FloatingWindowPic(context,R.layout.float_smallpic63);
        fws64=new FloatingWindowPic(context,R.layout.float_smallpic64);
        fws65=new FloatingWindowPic(context,R.layout.float_smallpic65);
        
        fws71=new FloatingWindowPic(context,R.layout.float_smallpic71);
        fws72=new FloatingWindowPic(context,R.layout.float_smallpic72);
        fws73=new FloatingWindowPic(context,R.layout.float_smallpic73);
        fws74=new FloatingWindowPic(context,R.layout.float_smallpic74);
        fws75=new FloatingWindowPic(context,R.layout.float_smallpic75);
        
        fws81=new FloatingWindowPic(context,R.layout.float_smallpic81);
        fws82=new FloatingWindowPic(context,R.layout.float_smallpic82);
        fws83=new FloatingWindowPic(context,R.layout.float_smallpic83);
        fws84=new FloatingWindowPic(context,R.layout.float_smallpic84);
        fws85=new FloatingWindowPic(context,R.layout.float_smallpic85);
        
        fws91=new FloatingWindowPic(context,R.layout.float_smallpic91);
        fws92=new FloatingWindowPic(context,R.layout.float_smallpic92);
        fws93=new FloatingWindowPic(context,R.layout.float_smallpic93);
        fws94=new FloatingWindowPic(context,R.layout.float_smallpic94);
        fws95=new FloatingWindowPic(context,R.layout.float_smallpic95);
        
        fwsLucky=new FloatingWindowPic(context,R.layout.float_lucky);
        fwsLuckyNone=new FloatingWindowPic(context,R.layout.float_luckynone);
    }
    
    private void recycleReplacePic(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			if( info.getText()==null)return;
			String je=info.getText().toString();
			info.getBoundsInScreen(mBounds);
			//取雷值：
			if(mIntInfo==2){
				mThunderNum=je.substring(je.length()-1, je.length());
				if(AccessibilityHelper.isDigital(mThunderNum)){
					i=Integer.parseInt(mThunderNum);
				}else {
					bRecycled=true;
					mSuccess=false;
					return;
				}
			}
			
			//替换手气最佳：
			if(je.equals("手气最佳")){
				if(i==0&&mLuckyMoneyCount==4){
				}else if(i==1&&mLuckyMoneyCount==1){
				}else if(i==2&&mLuckyMoneyCount==2){
				}else if(i==3&&mLuckyMoneyCount==2){
				}else if(i==4&&mLuckyMoneyCount==1){
				}else if(i==5&&mLuckyMoneyCount==2){
				}else if(i==6&&mLuckyMoneyCount==2){
				}else if(i==7&&mLuckyMoneyCount==2){
				}else if(i==8&&mLuckyMoneyCount==3){
				}else if(i==9&&mLuckyMoneyCount==4){
					
				}else {
					fwsLuckyNone.SetFloatViewPara(mBounds.left-34,mBounds.top-50,146,38);
					fwsLuckyNone.ShowFloatingWindow();
				}
			}
			//替换数值：
			if(je.indexOf("元")>-1){
				
				if(mIntInfo==6||mIntInfo==7)mLuckyMoneyCount=1;//第一个包
				else if(mIntInfo==9||mIntInfo==10)mLuckyMoneyCount=2;//第二个包
				else if(mIntInfo==12||mIntInfo==13)mLuckyMoneyCount=3;//第三个包
				else if(mIntInfo==15||mIntInfo==16)mLuckyMoneyCount=4;//第四个包
				else if(mIntInfo==18||mIntInfo==19)mLuckyMoneyCount=5;//第五 个包
				switch(mLuckyMoneyCount){
				case 1:
					//--------------------------------1----------------------------------
					switch(i){
					case 0:
						
						fws01.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws01.ShowFloatingWindow();
						break;
					case 1:
						
						fws11.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws11.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 2:
						
						fws21.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws21.ShowFloatingWindow();
						break;
						
					case 3:
						
						fws31.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws31.ShowFloatingWindow();
						break;
					case 4:
						
						fws41.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws41.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 5:
						
						fws51.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws51.ShowFloatingWindow();
						break;
						
					case 6:
						
						fws61.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws61.ShowFloatingWindow();
						break;
					case 7:
						
						fws71.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws71.ShowFloatingWindow();
						break;
					case 8:
						
						fws81.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws81.ShowFloatingWindow();
						break;
						
					case 9:
						
						fws91.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws91.ShowFloatingWindow();
						break;
					}
					//--------------------------------1----------------------------------
					break;
				case 2:
					//--------------------------------2----------------------------------
					switch(i){
					case 0:
						
						fws02.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws02.ShowFloatingWindow();
						break;
					case 1:
						
						fws12.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws12.ShowFloatingWindow();
						break;
					case 2:
					
						fws22.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws22.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
						
					case 3:
						
						fws32.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws32.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 4:
						
						fws42.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws42.ShowFloatingWindow();
						break;
					case 5:
						
						fws52.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws52.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
						
					case 6:
						
						fws62.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws62.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 7:
						
						fws72.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws72.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 8:
						
						fws82.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws82.ShowFloatingWindow();
						break;
						
					case 9:
						
						fws92.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws92.ShowFloatingWindow();
						break;
					}
					//--------------------------------2----------------------------------
					break;
				case 3:
					//--------------------------------3----------------------------------
					switch(i){
					case 0:
						
						fws03.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws03.ShowFloatingWindow();
						break;
					case 1:
						
						fws13.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws13.ShowFloatingWindow();
						break;
					case 2:
						
						fws23.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws23.ShowFloatingWindow();
						break;
						
					case 3:
						
						fws33.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws33.ShowFloatingWindow();
						break;
					case 4:
						
						fws43.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws43.ShowFloatingWindow();
						break;
					case 5:
						
						fws53.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws53.ShowFloatingWindow();
						break;
						
					case 6:
						
						fws63.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws63.ShowFloatingWindow();
						break;
					case 7:
						
						fws73.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws73.ShowFloatingWindow();
						break;
					case 8:
						
						fws83.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws83.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
						
					case 9:
						
						fws93.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws93.ShowFloatingWindow();
						break;
					}
					//--------------------------------3----------------------------------
					break;
				case 4:
					//-------------------------------4----------------------------------
					switch(i){
					case 0:
						
						fws04.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws04.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					case 1:
						
						fws14.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws14.ShowFloatingWindow();
						break;
					case 2:
						
						fws24.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws24.ShowFloatingWindow();
						break;
						
					case 3:
						
						fws34.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws34.ShowFloatingWindow();
						break;
					case 4:
						
						fws44.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws44.ShowFloatingWindow();
						break;
					case 5:
						
						fws54.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws54.ShowFloatingWindow();
						break;
						
					case 6:
						
						fws64.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws64.ShowFloatingWindow();
						break;
					case 7:
						
						fws74.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws74.ShowFloatingWindow();
						break;
					case 8:
						
						fws84.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws84.ShowFloatingWindow();
						break;
						
					case 9:
						
						fws94.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws94.ShowFloatingWindow();
						
						//设置手气最佳：
						fwsLucky.SetFloatViewPara(mBounds.left-52,mBounds.top-50+53,146,38);
						fwsLucky.ShowFloatingWindow();
						break;
					}
					//-------------------------------4----------------------------------
					break;
				case 5:
					//--------------------------------5----------------------------------
					switch(i){
					case 0:
						
						fws05.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws05.ShowFloatingWindow();
						break;
					case 1:
						
						fws15.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws15.ShowFloatingWindow();
						break;
					case 2:
						
						fws25.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws25.ShowFloatingWindow();
						break;
						
					case 3:
						
						fws35.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws35.ShowFloatingWindow();
						break;
					case 4:
						
						fws45.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws45.ShowFloatingWindow();
						break;
					case 5:
						
						fws55.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws55.ShowFloatingWindow();
						break;
						
					case 6:
						
						fws65.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws65.ShowFloatingWindow();
						break;
					case 7:
						
						fws75.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws75.ShowFloatingWindow();
						break;
					case 8:
						
						fws85.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws85.ShowFloatingWindow();
						break;
						
					case 9:
						
						fws95.SetFloatViewPara(mBounds.left,mBounds.top-50,mBounds.right-mBounds.left,43);
						fws95.ShowFloatingWindow();
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
    		mSuccess=true;
    		recycleReplacePic(rootNode);
    		if(!mSuccess)return;
    	switch(i){
    	case 0:
        	sShow="恭喜！共有5人中雷。雷值为："+mThunderNum;
        	break;
    	case 1: 
        	sShow="恭喜！共有4人中雷。雷值为："+mThunderNum;
        	break;
    	case 2:
        	sShow="恭喜！共有5人中雷。雷值为："+mThunderNum;
        	break;
    	case 3:
        	sShow="恭喜！共有4人中雷。雷值为："+mThunderNum;
        	break;
    	case 4:
        	sShow="恭喜！共有5人中雷。雷值为："+mThunderNum;
        	break;
    	case 5:
        	sShow="恭喜！共有4人中雷。雷值为："+mThunderNum;
        	break;
    	case 6:
        	sShow="恭喜！共有5人中雷。雷值为："+mThunderNum;
        	break;
    	case 7:
        	sShow="恭喜！共有4人中雷。雷值为："+mThunderNum;
        	break;
    	case 8:
        	sShow="恭喜！共有5人中雷。雷值为："+mThunderNum;
        	break;
    	case 9:
        	sShow="恭喜！共有4人中雷。雷值为："+mThunderNum;
        	break;
    	}
		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
		speeker.speak(sShow);
		}else{
			//fw.DestroyFloatingWindow();
			fws01.RemoveFloatingWindowPic();
			fws02.RemoveFloatingWindowPic();
			fws03.RemoveFloatingWindowPic();
			fws04.RemoveFloatingWindowPic();
			fws05.RemoveFloatingWindowPic();

			fws11.RemoveFloatingWindowPic();
			fws12.RemoveFloatingWindowPic();
			fws13.RemoveFloatingWindowPic();
			fws14.RemoveFloatingWindowPic();
			fws15.RemoveFloatingWindowPic();
			
			fws21.RemoveFloatingWindowPic();
			fws22.RemoveFloatingWindowPic();
			fws23.RemoveFloatingWindowPic();
			fws24.RemoveFloatingWindowPic();
			fws25.RemoveFloatingWindowPic();
			
			fws31.RemoveFloatingWindowPic();
			fws32.RemoveFloatingWindowPic();
			fws33.RemoveFloatingWindowPic();
			fws34.RemoveFloatingWindowPic();
			fws35.RemoveFloatingWindowPic();
			
			fws41.RemoveFloatingWindowPic();
			fws42.RemoveFloatingWindowPic();
			fws43.RemoveFloatingWindowPic();
			fws44.RemoveFloatingWindowPic();
			fws45.RemoveFloatingWindowPic();
			
			fws51.RemoveFloatingWindowPic();
			fws52.RemoveFloatingWindowPic();
			fws53.RemoveFloatingWindowPic();
			fws54.RemoveFloatingWindowPic();
			fws55.RemoveFloatingWindowPic();
			
			fws61.RemoveFloatingWindowPic();
			fws62.RemoveFloatingWindowPic();
			fws63.RemoveFloatingWindowPic();
			fws64.RemoveFloatingWindowPic();
			fws65.RemoveFloatingWindowPic();
			
			fws71.RemoveFloatingWindowPic();
			fws72.RemoveFloatingWindowPic();
			fws73.RemoveFloatingWindowPic();
			fws74.RemoveFloatingWindowPic();
			fws75.RemoveFloatingWindowPic();
			
			fws81.RemoveFloatingWindowPic();
			fws82.RemoveFloatingWindowPic();
			fws83.RemoveFloatingWindowPic();
			fws84.RemoveFloatingWindowPic();
			fws85.RemoveFloatingWindowPic();
			
			fws91.RemoveFloatingWindowPic();
			fws92.RemoveFloatingWindowPic();
			fws93.RemoveFloatingWindowPic();
			fws94.RemoveFloatingWindowPic();
			fws95.RemoveFloatingWindowPic();
			
			fwsLucky.RemoveFloatingWindowPic();
			fwsLuckyNone.RemoveFloatingWindowPic();
		}
    }

  
}
