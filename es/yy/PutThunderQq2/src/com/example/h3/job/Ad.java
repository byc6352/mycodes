/**
 * 
 */
package com.example.h3.job;


import com.example.h3.Config;


import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;



/**
 * @author byc
 *
 */
public class Ad {
	public static final String WX_WINDOW_DEL_AD_SAY = "com.tencent.mm.ui.base.k";//微信广告语删除窗口；
	public static final String WX_WINDOW_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";//微信群聊，单聊窗口；
	public static final String QQ_WINDOW_LAUNCHER_UI="com.tencent.mobileqq.activity.SplashActivity";
	public static final String WX_PACKAGENAME = "com.tencent.mm";//微信的包名
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";//QQ的包名
	private boolean bDel=false;//删除广告语
	private static Ad current;//实例；
	private static Ad currentWX;//微信实例；
	private static Ad currentQQ;//QQ实例；
	private Context context;
	private String mCurrentUI="";
	private String mPackageName="";
	private AccessibilityNodeInfo mRootNode; //窗体根结点
	
	public static final int MAX_REG_AD=100000;//最大已注册用户发广告间隔； 
	public static final int MAX_NO_REG_AD=500;//最大未注册用户发广告间隔； 
	public static final int MAX_OTHER_AD=1000;//最大已注册对其它媒体发广告间隔； 
	public int mADmax=MAX_NO_REG_AD;//广告间隔；
	public int mADcount=1;//广告计数器；
	public static String mStrAD=Config.ad+"联系"+Config.contact+"下载地址（复制到浏览器打开）："+Config.homepage;//广告语；
	public static boolean bReg=Config.bReg;//
	public boolean bLuckyMoneySend=false;//群里面有红包发布广告吗
	public static String TAG="byc001";
	private static int mWXversion=0;
	private static int mQQversion=0;
	private static final int JOY_IN_OTHER=0;//游戏平台在媒体之外；
	private static final int JOY_IN_WX=1;//游戏平台在微信；
	private static final int JOY_IN_QQ=2;//游戏平台在qq；
	private static int mJoy=JOY_IN_OTHER;//当前游戏在QQ，微信之外；
    private Ad(Context context,String PackageName) {
        this.mPackageName = PackageName;
        this.context=context;
        mADmax=getADinterval(context,PackageName);
        if(PackageName.equals(WX_PACKAGENAME)){
        	mWXversion=getWXversion();       	
        }
        if(PackageName.equals(QQ_PACKAGENAME)){
        	mQQversion=getQQversion();
        }
        //initVersionParam(0);
    }
    public static synchronized Ad getAd(Context context,String PackageName) {
    	if(PackageName.equals(WX_PACKAGENAME)){
    		if(currentWX == null) {
    			currentWX= new Ad(context,WX_PACKAGENAME);
    		}
    		return currentWX;
    	}
    	if(PackageName.equals(QQ_PACKAGENAME)){
    		if(currentQQ == null) {currentQQ= new Ad(context,QQ_PACKAGENAME);}
    		return currentQQ;
    	}
        if(current == null) {
            current = new Ad(context,PackageName);
        }
        return current;
    }
    /*
     *初始化参数：
     */
    public static void init() {
    	//1.广告语；
    	mStrAD=Config.ad+"联系"+Config.contact+"下载地址（复制到浏览器打开）："+Config.homepage;
    	mJoy=JOY_IN_QQ;//当前游戏在微信；
    }
    /*
     * 计算广告间隔：
     */
    private int getADinterval(Context context,String PackageName){
    	bLuckyMoneySend=false;
    	mADmax=MAX_NO_REG_AD;//广告间隔；
    	//1.注册否；
    	bReg=Config.bReg;
    	if(bReg)if(Config.getConfig(context).getRegCode().equals(Config.RegCode))bReg=false;
    	if(bReg){
    		mADmax=MAX_REG_AD;//正版发广告间隔；
    	}   	   	
    	if(PackageName.equals(WX_PACKAGENAME)){
    		if(mJoy==JOY_IN_OTHER||mJoy==JOY_IN_QQ){//当前游戏在QQ，微信之外,广告间隔；
    			if(bReg)mADmax=MAX_OTHER_AD;
    			bLuckyMoneySend=true;
    		}
    	}
    	if(PackageName.equals(QQ_PACKAGENAME)){
    		if(mJoy==JOY_IN_OTHER||mJoy==JOY_IN_WX){//当前游戏在QQ，微信之外,广告间隔；
    			if(bReg)mADmax=MAX_OTHER_AD;
    			bLuckyMoneySend=true;
    		}
    	}
    	return mADmax;
    }
    /*
     *初始化版本参数：
     */
    public static void initVersionParam(int version) {
    	
    }
    /*
     * 接收工作任务：
     */
    public void onReceiveJob(AccessibilityEvent event) {
    	final int eventType = event.getEventType();
    	String sClassName=event.getClassName().toString();
		//mRootNode=event.getSource();
		//if(mRootNode==null)return;
		//mRootNode=AccessibilityHelper.getRootNode(mRootNode);
		//AccessibilityHelper.recycle(mRootNode);
		
		
		//+++++++++++++++++++++++++++++++++窗口改变+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			mCurrentUI=sClassName;
			if(event.getPackageName().equals(WX_PACKAGENAME)){
				//WXdelADsay(event);
				if(mWXversion>=1120)WXdelADsay2(event);else WXdelADsay(event);
			}
			if(event.getPackageName().equals(QQ_PACKAGENAME)){
				QQdelADsay(event);
			}
		}
		//+++++++++++++++++++++++++++++++++内容改变+++++++++++++++++++++++++++++++++++++++++++++++
		if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			if(event.getPackageName().equals(WX_PACKAGENAME)){
				WXsendAD(event);
			}
			if(event.getPackageName().equals(QQ_PACKAGENAME)){
				QQsendAD(event);
			}
		}
    }
    /*
     * 微信删除广告语：
     */
    public void WXdelADsay2(AccessibilityEvent event) {
		//弹出菜单窗体：-----------------删除 广告语---------------------------------------------------------
		if(!bDel)return;
		if(mCurrentUI.equals("android.widget.FrameLayout")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				bDel=false;
			}
		}
		if(mCurrentUI.equals("android.widget.FrameLayout")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "更多",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				//bDel=false;
			}
		}
		if(mCurrentUI.equals("com.tencent.mm.ui.LauncherUI")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				//bDel=false;
			}
		}
		if(mCurrentUI.equals("com.tencent.mm.ui.base.i")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				bDel=false;
			}
		}
		
    }
    /*
     * 微信删除广告语：
     */
    public void WXdelADsay(AccessibilityEvent event) {
		//弹出菜单窗体：-----------------删除 广告语---------------------------------------------------------
		if(bDel){
		if(mCurrentUI.equals(WX_WINDOW_DEL_AD_SAY)){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				bDel=false;
			}
		}
		}
    }
    /*
     * 微信发送广告：
     */
    public void WXsendAD(AccessibilityEvent event) {
    	if(mCurrentUI.equals(WX_WINDOW_LAUNCHER_UI)){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			mADcount=mADcount+1;
			Log.i(TAG, "mADcount:"+mADcount);
			AccessibilityNodeInfo adNode=null;
			if(!bLuckyMoneySend){//群里面有红包则不发布广告：
				adNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "微信红包", -1);
				if(adNode!=null)mADcount=1;
			}
			if(mADcount>mADmax)WXsendADsay(mRootNode);//试用版发布广告；
			//实现长按指定 内容：-----------------------弹出 删除菜单-------------------------------------------------------
			adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mStrAD ,-1);
			if(adNode!=null){
				if(AccessibilityHelper.performLongClick(adNode))bDel=true;
				//if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
			}
			
    	}
    }
    /*发布广告信息*/
    public void WXsendADsay(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo edtNode=AccessibilityHelper.findNodeInfosByClassName(rootNode,"android.widget.EditText",0,true);
    	if(edtNode==null)return;
    	mADcount=1;
    	mStrAD=Config.ad+"联系"+Config.contact+"下载地址（复制到浏览器打开）："+Config.homepage;//广告语；
    	if(!nodeInput(edtNode,mStrAD))return;
    	AccessibilityHelper.Sleep(200);
        AccessibilityNodeInfo sendNode = AccessibilityHelper.findNodeInfosByText(rootNode, "发送", -1);
        if(sendNode==null)return;
        if(sendNode.performAction(AccessibilityNodeInfo.ACTION_CLICK))bDel=true;
        
        return;
    }
    /*输入文本*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  boolean nodeInput(AccessibilityNodeInfo edtNode,String txt){
    	if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){//android 5.0
    		Bundle arguments = new Bundle();
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
        	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	return true;
    	}
    	if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){//android 4.3
    		ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
    		ClipData clip = ClipData.newPlainText("text",txt);  
    		clipboard.setPrimaryClip(clip);  

    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  
    		////粘贴进入内容  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	/*
    	if(Config.currentapiVersion>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(5000);
        	if(RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(5000);
        		return true;
        	}
        	return false;
    	}
    	*/
    	return false;
    }  
    //++++++++++++++++++++++++++++++++++++++++++QQ+++++++++++++++++++++++++++++++++++++++++++
    /*
     * QQ删除广告语：
     */
    public void QQdelADsay(AccessibilityEvent event) {
		//弹出菜单窗体：-----------------删除 广告语---------------------------------------------------------
		//-------------------------删除信息对话框------------------------------------------------------
		if(!bDel)return;
		if(mCurrentUI.equals("android.app.Dialog")){
			bDel=false;
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			AccessibilityNodeInfo delNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除后将不会出现在你的消息记录中",-1);
			if(delNode==null)return;
			delNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
			if(delNode==null)return;
			AccessibilityHelper.performClick(delNode);
		}
    }
    /*
     * QQ发送广告：
     */
    public void QQsendAD(AccessibilityEvent event) {
    	if(mCurrentUI.equals(QQ_WINDOW_LAUNCHER_UI)){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			//---------------------------------是否是弹出式窗体----------------------------------------
			if(bDel){
			if(QQisPopmenuUi(mRootNode)){
				AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "删除",-1);
				if(adPop!=null){
					AccessibilityHelper.performClick(adPop);
				}
			}
			}
			//if(mLauncherJob.isMemberChatUi(mRootNode)){
				//实现长按指定 内容：-----------------------弹出 删除菜单-------------------------------------------------------
				AccessibilityNodeInfo adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mStrAD ,-1);
				if(adNode!=null){
					if(AccessibilityHelper.performLongClick(adNode))bDel=true;
					//if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
				}
				//-----------------------------------发布广告-----------------------------------------------
				mADcount=mADcount+1;
				if(mADcount>mADmax)QQsendAD(mRootNode);//试用版发布广告；
				//_____________________________________如果是红包群，则不发布广告____________________________________________	
				if(!bLuckyMoneySend){
					adNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "QQ红包", -1);
					if(adNode!=null)mADcount=0;
				}

			//}
			//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
		}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    }
    /** 是否为弹出菜单*/
    public boolean QQisPopmenuUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        String txt="复制";
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="删除";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="撤回";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
       return true;
    }
    /*发布广告信息*/
    public void QQsendAD(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo edtNode=AccessibilityHelper.findNodeInfosByClassName(rootNode,"android.widget.EditText",0,true);
    	if(edtNode==null)return;
    	mADcount=1;
    	mStrAD=Config.ad+"联系"+Config.contact+"下载地址（复制到浏览器打开）："+Config.homepage;//广告语；
    	if(!nodeInput(edtNode,mStrAD))return;
    	AccessibilityHelper.Sleep(200);
        AccessibilityNodeInfo sendNode = AccessibilityHelper.findNodeInfosByText(rootNode, "发送", -1);
        if(sendNode==null)return;
        if(sendNode.performAction(AccessibilityNodeInfo.ACTION_CLICK))bDel=true;
        return;
    }
    /** 微信包版本*/
    private int getWXversion() {
        try {
        	PackageInfo WechatPackageInfo =context.getPackageManager().getPackageInfo(WX_PACKAGENAME, 0);
            int v=WechatPackageInfo.versionCode;
            Log.i(TAG, "内部版本号："+v+"；外部版本号："+WechatPackageInfo.versionName);
            return v;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            
        }
        return 0;
    }
    /** QQ版本*/
    private int getQQversion() {
        try {
        	PackageInfo QQPackageInfo =context.getPackageManager().getPackageInfo(QQ_PACKAGENAME, 0);
            int v=QQPackageInfo.versionCode;
            Log.i(TAG, "内部版本号："+v+"；外部版本号："+QQPackageInfo.versionName);
            return v;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            
        }
        return 0;
    }

}