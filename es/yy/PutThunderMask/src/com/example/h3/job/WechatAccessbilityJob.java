/**
 * 
 */
package com.example.h3.job;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.byc.PutThunderMask.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.QiangHongBaoService;
import com.example.h3.util.FloatingWindowPic;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
/**
 * @author byc
 *
 */
public class WechatAccessbilityJob extends BaseAccessbilityJob  {
	
	private static WechatAccessbilityJob current;
	//---------------------------------------------------------------------------

	private LuckyMoneyDetailJob mDetailJob;



    @Override
    public void onCreateJob(QiangHongBaoService service) {
        super.onCreateJob(service);

        mDetailJob=LuckyMoneyDetailJob.getLuckyMoneyDetailJob(context);
        context=getContext();
    }
    @Override
    public void onStopJob() {

    }
    public static synchronized WechatAccessbilityJob getJob() {
        if(current == null) {
            current = new WechatAccessbilityJob();
        }
        return current;
    }
    
    //----------------------------------------------------------------------------------------
    @Override
    public void onReceiveJob(AccessibilityEvent event) {
    	
    	final int eventType = event.getEventType();

		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			//Í¼Æ¬Ìæ»»£º
			mDetailJob.ReplacePics(event);

		}//if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) 
    }
}
