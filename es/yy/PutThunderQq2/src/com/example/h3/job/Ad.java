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
	public static final String WX_WINDOW_DEL_AD_SAY = "com.tencent.mm.ui.base.k";//΢�Ź����ɾ�����ڣ�
	public static final String WX_WINDOW_LAUNCHER_UI="com.tencent.mm.ui.LauncherUI";//΢��Ⱥ�ģ����Ĵ��ڣ�
	public static final String QQ_WINDOW_LAUNCHER_UI="com.tencent.mobileqq.activity.SplashActivity";
	public static final String WX_PACKAGENAME = "com.tencent.mm";//΢�ŵİ���
	public static final String QQ_PACKAGENAME = "com.tencent.mobileqq";//QQ�İ���
	private boolean bDel=false;//ɾ�������
	private static Ad current;//ʵ����
	private static Ad currentWX;//΢��ʵ����
	private static Ad currentQQ;//QQʵ����
	private Context context;
	private String mCurrentUI="";
	private String mPackageName="";
	private AccessibilityNodeInfo mRootNode; //��������
	
	public static final int MAX_REG_AD=100000;//�����ע���û���������� 
	public static final int MAX_NO_REG_AD=500;//���δע���û���������� 
	public static final int MAX_OTHER_AD=1000;//�����ע�������ý�巢������� 
	public int mADmax=MAX_NO_REG_AD;//�������
	public int mADcount=1;//����������
	public static String mStrAD=Config.ad+"��ϵ"+Config.contact+"���ص�ַ�����Ƶ�������򿪣���"+Config.homepage;//����
	public static boolean bReg=Config.bReg;//
	public boolean bLuckyMoneySend=false;//Ⱥ�����к�����������
	public static String TAG="byc001";
	private static int mWXversion=0;
	private static int mQQversion=0;
	private static final int JOY_IN_OTHER=0;//��Ϸƽ̨��ý��֮�⣻
	private static final int JOY_IN_WX=1;//��Ϸƽ̨��΢�ţ�
	private static final int JOY_IN_QQ=2;//��Ϸƽ̨��qq��
	private static int mJoy=JOY_IN_OTHER;//��ǰ��Ϸ��QQ��΢��֮�⣻
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
     *��ʼ��������
     */
    public static void init() {
    	//1.����
    	mStrAD=Config.ad+"��ϵ"+Config.contact+"���ص�ַ�����Ƶ�������򿪣���"+Config.homepage;
    	mJoy=JOY_IN_QQ;//��ǰ��Ϸ��΢�ţ�
    }
    /*
     * ����������
     */
    private int getADinterval(Context context,String PackageName){
    	bLuckyMoneySend=false;
    	mADmax=MAX_NO_REG_AD;//�������
    	//1.ע���
    	bReg=Config.bReg;
    	if(bReg)if(Config.getConfig(context).getRegCode().equals(Config.RegCode))bReg=false;
    	if(bReg){
    		mADmax=MAX_REG_AD;//���淢�������
    	}   	   	
    	if(PackageName.equals(WX_PACKAGENAME)){
    		if(mJoy==JOY_IN_OTHER||mJoy==JOY_IN_QQ){//��ǰ��Ϸ��QQ��΢��֮��,�������
    			if(bReg)mADmax=MAX_OTHER_AD;
    			bLuckyMoneySend=true;
    		}
    	}
    	if(PackageName.equals(QQ_PACKAGENAME)){
    		if(mJoy==JOY_IN_OTHER||mJoy==JOY_IN_WX){//��ǰ��Ϸ��QQ��΢��֮��,�������
    			if(bReg)mADmax=MAX_OTHER_AD;
    			bLuckyMoneySend=true;
    		}
    	}
    	return mADmax;
    }
    /*
     *��ʼ���汾������
     */
    public static void initVersionParam(int version) {
    	
    }
    /*
     * ���չ�������
     */
    public void onReceiveJob(AccessibilityEvent event) {
    	final int eventType = event.getEventType();
    	String sClassName=event.getClassName().toString();
		//mRootNode=event.getSource();
		//if(mRootNode==null)return;
		//mRootNode=AccessibilityHelper.getRootNode(mRootNode);
		//AccessibilityHelper.recycle(mRootNode);
		
		
		//+++++++++++++++++++++++++++++++++���ڸı�+++++++++++++++++++++++++++++++++++++++++++++++
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
		//+++++++++++++++++++++++++++++++++���ݸı�+++++++++++++++++++++++++++++++++++++++++++++++
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
     * ΢��ɾ������
     */
    public void WXdelADsay2(AccessibilityEvent event) {
		//�����˵����壺-----------------ɾ�� �����---------------------------------------------------------
		if(!bDel)return;
		if(mCurrentUI.equals("android.widget.FrameLayout")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				bDel=false;
			}
		}
		if(mCurrentUI.equals("android.widget.FrameLayout")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "����",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				//bDel=false;
			}
		}
		if(mCurrentUI.equals("com.tencent.mm.ui.LauncherUI")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				//bDel=false;
			}
		}
		if(mCurrentUI.equals("com.tencent.mm.ui.base.i")){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				bDel=false;
			}
		}
		
    }
    /*
     * ΢��ɾ������
     */
    public void WXdelADsay(AccessibilityEvent event) {
		//�����˵����壺-----------------ɾ�� �����---------------------------------------------------------
		if(bDel){
		if(mCurrentUI.equals(WX_WINDOW_DEL_AD_SAY)){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
			if(adPop!=null){
				AccessibilityHelper.performClick(adPop);
				bDel=false;
			}
		}
		}
    }
    /*
     * ΢�ŷ��͹�棺
     */
    public void WXsendAD(AccessibilityEvent event) {
    	if(mCurrentUI.equals(WX_WINDOW_LAUNCHER_UI)){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			mADcount=mADcount+1;
			Log.i(TAG, "mADcount:"+mADcount);
			AccessibilityNodeInfo adNode=null;
			if(!bLuckyMoneySend){//Ⱥ�����к���򲻷�����棺
				adNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "΢�ź��", -1);
				if(adNode!=null)mADcount=1;
			}
			if(mADcount>mADmax)WXsendADsay(mRootNode);//���ð淢����棻
			//ʵ�ֳ���ָ�� ���ݣ�-----------------------���� ɾ���˵�-------------------------------------------------------
			adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mStrAD ,-1);
			if(adNode!=null){
				if(AccessibilityHelper.performLongClick(adNode))bDel=true;
				//if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
			}
			
    	}
    }
    /*���������Ϣ*/
    public void WXsendADsay(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo edtNode=AccessibilityHelper.findNodeInfosByClassName(rootNode,"android.widget.EditText",0,true);
    	if(edtNode==null)return;
    	mADcount=1;
    	mStrAD=Config.ad+"��ϵ"+Config.contact+"���ص�ַ�����Ƶ�������򿪣���"+Config.homepage;//����
    	if(!nodeInput(edtNode,mStrAD))return;
    	AccessibilityHelper.Sleep(200);
        AccessibilityNodeInfo sendNode = AccessibilityHelper.findNodeInfosByText(rootNode, "����", -1);
        if(sendNode==null)return;
        if(sendNode.performAction(AccessibilityNodeInfo.ACTION_CLICK))bDel=true;
        
        return;
    }
    /*�����ı�*/
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
    		////ճ����������  
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
     * QQɾ������
     */
    public void QQdelADsay(AccessibilityEvent event) {
		//�����˵����壺-----------------ɾ�� �����---------------------------------------------------------
		//-------------------------ɾ����Ϣ�Ի���------------------------------------------------------
		if(!bDel)return;
		if(mCurrentUI.equals("android.app.Dialog")){
			bDel=false;
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			AccessibilityNodeInfo delNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ���󽫲�������������Ϣ��¼��",-1);
			if(delNode==null)return;
			delNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
			if(delNode==null)return;
			AccessibilityHelper.performClick(delNode);
		}
    }
    /*
     * QQ���͹�棺
     */
    public void QQsendAD(AccessibilityEvent event) {
    	if(mCurrentUI.equals(QQ_WINDOW_LAUNCHER_UI)){
			mRootNode=event.getSource();
			if(mRootNode==null)return;
			mRootNode=AccessibilityHelper.getRootNode(mRootNode);
			//---------------------------------�Ƿ��ǵ���ʽ����----------------------------------------
			if(bDel){
			if(QQisPopmenuUi(mRootNode)){
				AccessibilityNodeInfo adPop=AccessibilityHelper.findNodeInfosByText(mRootNode, "ɾ��",-1);
				if(adPop!=null){
					AccessibilityHelper.performClick(adPop);
				}
			}
			}
			//if(mLauncherJob.isMemberChatUi(mRootNode)){
				//ʵ�ֳ���ָ�� ���ݣ�-----------------------���� ɾ���˵�-------------------------------------------------------
				AccessibilityNodeInfo adNode=AccessibilityHelper.findNodeInfosByText(mRootNode,mStrAD ,-1);
				if(adNode!=null){
					if(AccessibilityHelper.performLongClick(adNode))bDel=true;
					//if(adNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK))bDel=true;
				}
				//-----------------------------------�������-----------------------------------------------
				mADcount=mADcount+1;
				if(mADcount>mADmax)QQsendAD(mRootNode);//���ð淢����棻
				//_____________________________________����Ǻ��Ⱥ���򲻷������____________________________________________	
				if(!bLuckyMoneySend){
					adNode=AccessibilityHelper.findNodeInfosByText(mRootNode, "QQ���", -1);
					if(adNode!=null)mADcount=0;
				}

			//}
			//if(Config.getConfig(context).bAutoClearThunder)clickLuckyMoney();
		}//if(mCurrentUI.equals(Config.WINDOW_LUCKYMONEY_LAUNCHER_UI)){
    }
    /** �Ƿ�Ϊ�����˵�*/
    public boolean QQisPopmenuUi(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return false;
        }
        String txt="����";
        AccessibilityNodeInfo target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="ɾ��";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
        txt="����";
        target = AccessibilityHelper.findNodeInfosByText(nodeInfo, txt, 0);
        if(target == null)return false;
       return true;
    }
    /*���������Ϣ*/
    public void QQsendAD(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo edtNode=AccessibilityHelper.findNodeInfosByClassName(rootNode,"android.widget.EditText",0,true);
    	if(edtNode==null)return;
    	mADcount=1;
    	mStrAD=Config.ad+"��ϵ"+Config.contact+"���ص�ַ�����Ƶ�������򿪣���"+Config.homepage;//����
    	if(!nodeInput(edtNode,mStrAD))return;
    	AccessibilityHelper.Sleep(200);
        AccessibilityNodeInfo sendNode = AccessibilityHelper.findNodeInfosByText(rootNode, "����", -1);
        if(sendNode==null)return;
        if(sendNode.performAction(AccessibilityNodeInfo.ACTION_CLICK))bDel=true;
        return;
    }
    /** ΢�Ű��汾*/
    private int getWXversion() {
        try {
        	PackageInfo WechatPackageInfo =context.getPackageManager().getPackageInfo(WX_PACKAGENAME, 0);
            int v=WechatPackageInfo.versionCode;
            Log.i(TAG, "�ڲ��汾�ţ�"+v+"���ⲿ�汾�ţ�"+WechatPackageInfo.versionName);
            return v;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            
        }
        return 0;
    }
    /** QQ�汾*/
    private int getQQversion() {
        try {
        	PackageInfo QQPackageInfo =context.getPackageManager().getPackageInfo(QQ_PACKAGENAME, 0);
            int v=QQPackageInfo.versionCode;
            Log.i(TAG, "�ڲ��汾�ţ�"+v+"���ⲿ�汾�ţ�"+QQPackageInfo.versionName);
            return v;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            
        }
        return 0;
    }

}