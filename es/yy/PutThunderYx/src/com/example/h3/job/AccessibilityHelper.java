/**
 * 
 */
package com.example.h3.job;

import java.util.ArrayList;
import java.util.List;

import com.example.h3.Config;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class AccessibilityHelper {
	private static List<AccessibilityNodeInfo> classNames= new ArrayList<AccessibilityNodeInfo>();
	private static final String nums="0123456789.";
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
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
    /** 通过控件查找*/
    public static AccessibilityNodeInfo findNodeInfosByClassName(AccessibilityNodeInfo rootNode, String className,int i,boolean bClear) {
    	
    	if(bClear){
    		classNames.clear();
    		recycleClassName(rootNode,className);
    	}   	
        if(classNames == null || classNames.isEmpty()) return null;
        if(i==-1)
        	return classNames.get(classNames.size()-1);
        else
        	return classNames.get(i);
    }
    public static void recycleClassName(AccessibilityNodeInfo info,String className) {
  		if (info.getChildCount() == 0) {

  			if(className.equals(info.getClassName())){
  				classNames.add(info);
  			}
  			//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  		} else {
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycleClassName(info.getChild(i),className);
  				}
  			}
  		}
  	}
    /** 通过关键字查找*/
    public static AccessibilityNodeInfo findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String... texts) {
        for(String key : texts) {
            AccessibilityNodeInfo info = findNodeInfosByText(nodeInfo, key,0);
            if(info != null) {
                return info;
            }
        }
        return null;
    }
    /** 返回主界面事件*/
    public static void performHome(AccessibilityService service) {
        if(service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /** 返回事件*/
    public static void performBack(AccessibilityService service) {
        if(service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /** 点击事件*/
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return;
        }
        if(nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }
    //数字判断：
    public static boolean isDigital(String s){
    	String c="";
    	for(int i=0;i<s.length();i++){
    		c=s.substring(i,i+1);
    		if(nums.indexOf(c)==-1)return false;
    	}
    	return true;
    }
    //拆包延时：
    public  static void Sleep(int MilliSecond) {
    	
	    try{
	    	  Thread.sleep(MilliSecond);
	    }catch(Exception e){
	    } 
    }
    //打印子控件：
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static void recycle2(AccessibilityNodeInfo info) {
  		if (info.getChildCount() == 0) {
  			//取信息
  			//mRedInfo[mIntInfo]=info.getText().toString();
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			Log.i(Config.TAG, "desc：" + info.getContentDescription());
  			Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);

  		} else {
  			//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			//Log.i(Config.TAG, "Text：" + info.getText());
  			//Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			//Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			//Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			//Rect outBounds=new Rect();
  			//info.getBoundsInScreen(outBounds);
  			//Log.i(Config.TAG, "outBounds:" + outBounds);
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycle2(info.getChild(i));
  				}
  			}
  		}
  	}
    //打印父子控件：
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static void recycle(AccessibilityNodeInfo info) {
  		if (info.getChildCount() == 0) {
  			//取信息
  			//mRedInfo[mIntInfo]=info.getText().toString();
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			Log.i(Config.TAG, "desc：" + info.getContentDescription());
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
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			//Log.i(TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text：" + info.getText());
  			Log.i(Config.TAG, "desc：" + info.getContentDescription());
  			Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
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
