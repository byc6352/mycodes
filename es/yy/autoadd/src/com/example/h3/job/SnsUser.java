/**
 * 
 */
package com.example.h3.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.h3.Config;
import com.example.h3.util.AccessibilityHelper;
import com.example.h3.util.RootShellCmd;
import com.example.h3.util.SpeechUtil;
import com.example.h3.util.VersionParam;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author byc
 *
 */
public class SnsUser {
	private static SnsUser current;
	private Context context;
	private SpeechUtil speaker ;
	public  int mIndex=0;
	public boolean bWorking=false;
	private int year=2017;
	private int mon=10;
	//private static List<AccessibilityNodeInfo> chatMembers= new ArrayList<AccessibilityNodeInfo>();
	public List<AccessibilityNodeInfo> mGallerys= null;//群成员名称集合；
	//private static final String CHAT_MEMBER_ID="com.tencent.mm:id/cgv";
	
	
	
    public static synchronized SnsUser getSnsUser(Context context) {
    	
        if(current == null) {
            current = new SnsUser(context);
        }
        return current;
    }
    private SnsUser(Context context){
    	this.context = context;
    	//bReg=Config.getConfig(context).getREG();
    	speaker=SpeechUtil.getSpeechUtil(context);
    }
    /** 得到群成员*/
    public List<AccessibilityNodeInfo> getGalleryMembers(AccessibilityNodeInfo rootNode) {
    	mGallerys=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.GalleryID);
    	return mGallerys;
    }
    /** 是否已经打开完所有视频*/ 
    public boolean isGalleryLastPage(AccessibilityNodeInfo rootNode) {
    	return false;
    }
    /** 翻页*/ 
    public boolean ScrollPage(AccessibilityNodeInfo rootNode) {
    	AccessibilityNodeInfo ListNode=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.SnsUserListID, 0);
    	if(ListNode==null)return false;
    	return ListNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }
    /** 得到相册最底下的日期*/ 
    public String GetGalleryLastDate(AccessibilityNodeInfo rootNode) {
    	AccessibilityNodeInfo dateNode=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.GalleryDateID,-1);
    	if(dateNode==null)return "";
    	String d=dateNode.getText().toString();
    	AccessibilityNodeInfo moonNode=AccessibilityHelper.findNodeInfosById(rootNode, VersionParam.GalleryMoonID,-1);
    	if(moonNode==null)return "";
    	String m=moonNode.getText().toString();
    	m=m.substring(0,m.length()-1);
    	//String date="2017-"+m+"-"+d;
    	String date=makeupDate(m,d);
    	return date;
    }
    /** 合成日期*/ 
    public String makeupDate(String m,String d) {
    	int cm=Integer.valueOf(m);
    	if(cm>mon){
    		year=year-1;
    	}
    	mon=cm;
    	String y=String.valueOf(year);
    	String date=y+"-"+m+"-"+d;
    	return date;
    }
    /** 是否已下拉到相册中要求的日期 */ 
    public boolean isScrollGalleryNeedDate(AccessibilityNodeInfo rootNode) {
    	String GalleryDate=GetGalleryLastDate(rootNode);
    	if(GalleryDate.equals(""))return false;
    	 DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
    	 try 
    	 {
             Date dt1 = df.parse(GalleryDate);
             Date dt2 = df.parse(Config.sScrollGalleryDownDate);
             if (dt1.getTime() <= dt2.getTime()) {
            	 return true;
             }else{
            	 return false;
             }
    	 } catch (Exception exception) {
             exception.printStackTrace();
         }
    	 return false;
    }
    /** 保存打开视频的视频日期 */ 
    public void SaveGalleryDate(AccessibilityNodeInfo rootNode) {
    	AccessibilityNodeInfo dateNode=AccessibilityHelper.findNodeInfosById(rootNode, "android:id/text1", 0);
		if(dateNode==null||dateNode.getText()==null)return;
		String strDate=dateNode.getText().toString();
		if(strDate.length()<12)return;
		String y=strDate.substring(0,4);
		int i=strDate.indexOf("月");
		int j=strDate.indexOf("日");
		if(i==-1||j==-1)return;
		String m=strDate.substring(5,i);
		String d=strDate.substring(i+1,j);
		String date=y+"-"+m+"-"+d;
		Config.getConfig(context).setScrollGalleryDownDate(date);
		return;
    }
    /** 是否是视频窗口*/ 
    public boolean isGalleryUI(AccessibilityNodeInfo rootNode) {
    	String sClassName=rootNode.getClassName().toString();
    	if(sClassName.equals(VersionParam.WINDOW_SNSGALLERY_UI))return true;else return false;
    }
    public boolean isGalleryUI(String CurrentUI) {
    	if(CurrentUI.equals(VersionParam.WINDOW_SNSGALLERY_UI))return true;else return false;
    }
    public boolean isSnsUserUI(String CurrentUI) {
    	if(CurrentUI.equals(VersionParam.WINDOW_SNSUSER_UI))return true;else return false;
    }
    /** 滑动视频*/ 
    public boolean SwipeGallery() {
    	String cmd="input swipe 180 592 540 592";
    	return RootShellCmd.getRootShellCmd().execShellCmd(cmd);
    }
    /** 点击视频*/ 
    public boolean ClickGallery() {
    	String cmd="input tap 360 592";
    	return RootShellCmd.getRootShellCmd().execShellCmd(cmd);
    }
}
