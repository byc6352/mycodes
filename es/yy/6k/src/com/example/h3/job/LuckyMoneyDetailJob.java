/**
 * 
 */
package com.example.h3.job;

import com.example.h3.Config;
import com.example.h3.MainActivity;

import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import util.SpeechUtil;

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
    
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void recycle(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//取信息
			if(info.getText()!=null)
				mReceiveInfo[2]=info.getText().toString();
			//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(Config.TAG, "Text("+mIntInfo+"):" + info.getText());

			if(mIntInfo==7){bRecycled=true;return;}
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
    	//躲避成功：抢到金额：3.0元；雷值为：7；避雷成功！
    	String sMoneyValue=mReceiveInfo[2];
    	//1。避雷成功判断：
    	String say="抢到红包"+sMoneyValue+"元。";
    	Toast.makeText(context, say, Toast.LENGTH_LONG).show();
    	speeker.speak(say);
    }

}
