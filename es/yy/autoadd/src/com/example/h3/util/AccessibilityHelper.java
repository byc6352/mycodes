/**
 * 
 */
package com.example.h3.util;

import java.util.ArrayList;
import java.util.List;

import com.example.h3.Config;


import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class AccessibilityHelper {
	private static final String DIGITAL="0123456789";//
	private static List<AccessibilityNodeInfo> classNames= new ArrayList<AccessibilityNodeInfo>();
	
    /** ͨ���ı�����*/
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
    /** ͨ���ı�����*/
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text,int i,boolean bFullMatch) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) {
            return null;
        }
        if(bFullMatch){
        	for(int j=0;j<list.size();j++){
        		AccessibilityNodeInfo tmp=list.get(j);
        		if(tmp.getText().toString().equals(text))return tmp;
        	}
        }
        if(i==-1)
        	return list.get(list.size()-1);
        else
        	return list.get(i);
    }
    /** ͨ��id����*/
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
    /** ͨ��id����*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public  static List<AccessibilityNodeInfo> findNodeInfosById(AccessibilityNodeInfo rootNode, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return rootNode.findAccessibilityNodeInfosByViewId(resId);
        }
        return null;
    }
    /** �õ�rootNode*/
    public  static AccessibilityNodeInfo getRootNode(AccessibilityNodeInfo node){
    	if(node==null)return null;
    	AccessibilityNodeInfo parent=node.getParent();
    	AccessibilityNodeInfo tmp=node;
    	while(parent!=null){
    		tmp=parent;
    		parent=parent.getParent();
    	}
    	return tmp;
    }
    //�Ƿ������֣�
    public static boolean isDigital(String s){
    	if(DIGITAL.indexOf(s)==-1)return false;else return true;
    }
    //�����ʱ��
    public  static void Sleep(int MilliSecond) {
    	
	    try{
	    	  Thread.sleep(MilliSecond);
	    }catch(Exception e){
	    } 
    }
    /** ͨ���ؼ��ֲ���*/
    public static AccessibilityNodeInfo findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String... texts) {
        for(String key : texts) {
            AccessibilityNodeInfo info = findNodeInfosByText(nodeInfo, key,0);
            if(info != null) {
                return info;
            }
        }
        return null;
    }
    /** �����������¼�*/
    public static void performHome(AccessibilityService service) {
        if(service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /** �����¼�*/
    public static void performBack(AccessibilityService service) {
        if(service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /** ����¼�*/
    public static boolean performClick(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
        	Log.i("BYC001", "performClick:ACTION_CLICK:nodeInfo == null");
            return false;
        }
        if(nodeInfo.isClickable()) {
            //return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	int i=0;
        	while(!nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
        		Sleep(200);
        		i=i+1;
        		Log.i("BYC001", "performClick:ACTION_CLICK:false");
        		if(i>10)return false;
        		
        	}
        	return true;
        } else {
            return performClick(nodeInfo.getParent());
        }
    }
    /** ����¼�*/
    public static boolean performClickSingle(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
        	Log.i("BYC001", "performClick:ACTION_CLICK:nodeInfo == null");
            return false;
        }
        if(nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        	
        } else {
            return performClickSingle(nodeInfo.getParent());
        }
    }
    /** ������Ļ�¼�*/
    public static boolean performScrollForward(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	return false;
        }
        if(nodeInfo.isScrollable())return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        List<AccessibilityNodeInfo> ListNode=nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");
        if(ListNode.size()==0)return false;
        boolean bSuc=false;
        for(int i=0;i<ListNode.size();i++){
        	if(ListNode.get(i).isScrollable())bSuc=ListNode.get(i).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        	if(bSuc)return true;
        }
        return bSuc;
    }
    /** ������Ļ�¼�*/
    public static boolean performScrollForward2(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	return false;
        }
        if(nodeInfo.isScrollable())return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        
        if (nodeInfo.getChildCount() == 0) {
        	return false;
  		//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  		} else {
  			for (int i = 0; i < nodeInfo.getChildCount(); i++) {//���ҵ�һ���ӿؼ���
  				if(nodeInfo.getChild(i)!=null&&nodeInfo.getChild(i).isScrollable()&&nodeInfo.getChild(i).getClassName().toString().equals("android.widget.ListView")){
  					return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);  					
  				}
  			}//for (int i = 0; i < nodeInfo.getChildCount(); i++) {//���ҵ�һ���ӿؼ���
  			for (int i = 0; i < nodeInfo.getChildCount(); i++) {//�����ӿؼ���
  				if(nodeInfo.getChild(i)!=null){
  					if(performScrollForward(nodeInfo.getChild(i)))return true;
  				}
  			}
  			return false;
  		}//} else {  
    }
    public static void printParentInfo(AccessibilityNodeInfo child) {
    	if(child==null)return;
    	Rect outBounds=new Rect();
		Log.i(Config.TAG, "child widget----------------------------" + child.getClassName());
		Log.i(Config.TAG, "Text��" + child.getText());
		//Log.i(Config.TAG, "windowId:" + info.getWindowId());
		//Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
		Log.i(Config.TAG, "isClickable:" + child.isClickable());
		child.getBoundsInScreen(outBounds);
		Log.i(Config.TAG, "outBounds:" + outBounds);
		Log.i(Config.TAG, "getContentDescription:" + child.getContentDescription());
    	
    	AccessibilityNodeInfo parent=child.getParent();
    	int i=1;
    	while(parent!=null){
  			Log.i(Config.TAG, "parent"+i+" widget----------------------------" + parent.getClassName());
  			Log.i(Config.TAG, "Text��" + parent.getText());
  			//Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			//Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + parent.isClickable());
  			parent.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
  			Log.i(Config.TAG, "getContentDescription:" + parent.getContentDescription());
    		parent=parent.getParent();
    		i=i+1;
    	}
    }
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static void recycle(AccessibilityNodeInfo info) {
  		if (info.getChildCount() == 0) {
  			//ȡ��Ϣ
  			//mRedInfo[mIntInfo]=info.getText().toString();
  			Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  			Log.i(Config.TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text��" + info.getText());
  			//Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			//Log.i(Config.TAG, "ResouceId:" + info.getViewIdResourceName());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
  			Log.i(Config.TAG, "getContentDescription:" + info.getContentDescription());
  			Log.i(Config.TAG, "isScrollable:" + info.isScrollable());
          	//Bundle arguments = new Bundle();
          	//String sText="11";
          	//arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,sText);
          	//info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
  			
  			//if(info.isClickable())info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
  		} else {
  			Log.i(Config.TAG, "parent widget----------------------------" + info.getClassName());
  			Log.i(Config.TAG, "showDialog:" + info.canOpenPopup());
  			Log.i(Config.TAG, "Text��" + info.getText());
  			//Log.i(Config.TAG, "windowId:" + info.getWindowId());
  			Log.i(Config.TAG, "isClickable:" + info.isClickable());
  			Rect outBounds=new Rect();
  			info.getBoundsInScreen(outBounds);
  			Log.i(Config.TAG, "outBounds:" + outBounds);
  			Log.i(Config.TAG, "isScrollable:" + info.isScrollable());
  			for (int i = 0; i < info.getChildCount(); i++) {
  				if(info.getChild(i)!=null){
  					recycle(info.getChild(i));
  				}
  			}
  		}
  	}

    /** ͨ���ؼ�����*/
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

  
}


/*
//
//@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public void recycle(AccessibilityNodeInfo info) {
	if (info.getChildCount() == 0) {
		//ȡ��Ϣ
		//mRedInfo[mIntInfo]=info.getText().toString();
		//Log.i(TAG, "child widget----------------------------" + info.getClassName());
		//Log.i(TAG, "showDialog:" + info.canOpenPopup());
		//Log.i(TAG, "Text��" + info.getText());
		//Log.i(TAG, "windowId:" + info.getWindowId());
		//Log.i(TAG, "ResouceId:" + info.getViewIdResourceName());
		//Log.i(TAG, "isClickable:" + info.isClickable());
		if(info.isClickable())info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
	} else {
		for (int i = 0; i < info.getChildCount(); i++) {
			if(info.getChild(i)!=null){
				recycle(info.getChild(i));
			}
		}
	}
}

    public static boolean performScrollForward(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	return false;
        }
        if(nodeInfo.isScrollable())return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        
        if (nodeInfo.getChildCount() == 0) {
        	return false;
  		//Log.i(Config.TAG, "child widget----------------------------" + info.getClassName());
  		} else {
  			for (int i = 0; i < nodeInfo.getChildCount(); i++) {//���ҵ�һ���ӿؼ���
  				if(nodeInfo.getChild(i)!=null&&nodeInfo.getChild(i).isScrollable()){
  					return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);  					
  				}
  			}//for (int i = 0; i < nodeInfo.getChildCount(); i++) {//���ҵ�һ���ӿؼ���
  			for (int i = 0; i < nodeInfo.getChildCount(); i++) {//�����ӿؼ���
  				if(nodeInfo.getChild(i)!=null)performScrollForward(nodeInfo.getChild(i));
  			}
  		}  
    }
*/
