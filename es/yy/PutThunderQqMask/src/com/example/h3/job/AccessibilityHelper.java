/**
 * 
 */
package com.example.h3.job;

import java.util.List;

import com.example.h3.Config;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class AccessibilityHelper {
    /** 通过文本查找*/
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text,int i) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        if(i==-1)
        	return list.get(list.size()-1);
        else
        	return list.get(i);
    }
    /** 通过id查找*/
    public  static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId,int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if(list != null && !list.isEmpty()) {
            	if(i==-1)return list.get(list.size()-1);
                return list.get(i);
            }
        }
        return null;
    }
    //拆包延时：
    public  static void Sleep(int MilliSecond) {
    	
	    try{
	    	  Thread.sleep(MilliSecond);
	    }catch(Exception e){
	    } 
    }
    public static void recycle(AccessibilityNodeInfo info) {
  		if (info.getChildCount() == 0) {
  			//取信息
  			//mRedInfo[mIntInfo]=info.getText().toString();
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
          	//Bundle arguments = new Bundle();
          	//String sText="11";
          	//arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
          	//info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
  			
  			//if(info.isClickable())info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
  		} else {
  			Log.i(Config.TAG, "child widget--------------parent--------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			//Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			//Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycle(info.getChild(i));
  				}
  			}
  		}
  	}
}
