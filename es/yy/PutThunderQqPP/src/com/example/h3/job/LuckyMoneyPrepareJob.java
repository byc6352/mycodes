package com.example.h3.job;

import com.byc.PutThunderQqPP.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.util.FloatingWindowPic;
import com.example.h3.util.FloatingWindowText;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class LuckyMoneyPrepareJob {
	private static LuckyMoneyPrepareJob current;
	private Context context;
	private static String TAG = "byc001";
	private SpeechUtil speeker ;
	//ִ�гɹ����壺
	public boolean bSuccess=false;
	//�����ַ���ʱ��
	private FloatingWindowPic fwp;
	//
	private FloatingWindowText fwt;//��ʾ����������ʾ��ֵ
	
	private BackgroundMusic mBackgroundMusic;
	//����㣺
	private AccessibilityNodeInfo mRootNode;
	
	private LuckyMoneyPrepareJob(Context context) {
		this.context = context;
		TAG=Config.TAG;
		speeker=SpeechUtil.getSpeechUtil(context);
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//�����ַ���ʱ��
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//��ʾ��ֵ���ڣ�
		fwt=FloatingWindowText.getFloatingWindowText(context);
		//fwt.SetFloatViewPara(100, 200,w,w);
		//���չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_CALC_THUNDER_DELAY);
        filter.addAction(Config.ACTION_CALC_THUNDER_SHOW);
        context.registerReceiver(calcThunderReceiver, filter);
	}
    public static synchronized LuckyMoneyPrepareJob getLuckyMoneyPrepareJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyPrepareJob(context);
        }
        current.context=context;
        return current;
        
    }
    //������ʱ��
    private void CalcThunderDelay() {
    	speeker.speak("����Ϊ�����������ֵ��");
        //���ű������֣�
        mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
        //��ʾ��ʱ����
    	fwp.c=30;
    	fwp.mSendMsg=Config.ACTION_CALC_THUNDER_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_RED;
    	fwp.ShowFloatingWindow();
    }
    //������ʱ������Ϣ��
  	private BroadcastReceiver calcThunderReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {

              String action = intent.getAction();
              //Log.d(TAG, "receive-->" + action);
              //������ʱ��������
              if(Config.ACTION_CALC_THUNDER_DELAY.equals(action)) {
              	//1�����������ֵ��
              	int thunder=getRadomNum();
              	//�㲥��ֵ��
              	sendBroadcastThunder(String.valueOf(thunder));
              	String say="�����ֵΪ��"+thunder;
              	//2����ʾ�����ֵ��
              	fwt.ShowFloatingWindow();
      
              	fwt.tvJE.setText("�������Ϊ��"+Config.sMoneyValue+"Ԫ");
              	fwt.tvNum.setText("��������Ϊ��"+Config.sLuckyMoneyNum+"��");
              	fwt.tvThunder.setText(say);
              	speeker.speak(say);
              	fwt.mShowTime=50;
              	//fwt.StartTime();
              	//String s=Config.sMoneyValue+"/"+thunder;
              	String s=String.valueOf(thunder);
              	Config.sLuckyMoneySay=s;
              	//clickPWDthreadStart(); 
              }
              //2����ʾ��ֵ��������
              if(Config.ACTION_CALC_THUNDER_SHOW.equals(action)) {
              	//1������ֵ��
              	bSuccess=inputText(mRootNode);
              	showResult(bSuccess);
              	//�رձ������֣�
              	mBackgroundMusic.stopBackgroundMusic();
              }
          }
      };
      //�㲥��ֵ��
      private void sendBroadcastThunder(String aThunder){
    	  Intent intent = new Intent(Config.ACTION_THUNDER_RECEIVER);
    	  intent.putExtra("aThunder", aThunder);
    	  context.sendBroadcast(intent);
      }
      //����һ��0~9���������
      private int getRadomNum(){
      	java.util.Random random=new java.util.Random();// ���������
      	int result=random.nextInt(10);// ����[0,10)�����е�������ע�ⲻ����10
      	return result;
      }
      //�����ı���
      public boolean inputText2(AccessibilityNodeInfo rootNode){

      	//if(!getPWDpadInfo(rootNode))return false;//��ȡPWD��峤��͵�Ԫ����Ϣ��
      	bSuccess=false;
      	mRootNode=rootNode;
      	if(Config.bAutoThunder){//������ֵ
      		CalcThunderDelay();//1����ʱ��ʾ��
      	}else{
      		bSuccess=inputText(rootNode);
      		showResult(bSuccess);
      	}
      	//PutPWDdelay();	
      	return bSuccess;
      }
      //��ʾ�����
      private void showResult(boolean bSuccess){
      	if(bSuccess==false&&Config.bReg==false){
      		String say="�������ð��û������ֶ���ɺ������׹��������ð��û����׻�����0~3��֮�䡣";
  			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
  			speeker.speak(say);
      	}
      	if(bSuccess)
      		Config.JobState=Config.STATE_INPUT_PWD;
      	else
      		Config.JobState=Config.STATE_INPUT_CLOSING;
      }
      
    public boolean inputText(AccessibilityNodeInfo rootNode){

        	String txt="";
        	//������(rootNode, "��Ⱥ��", 0)
        	AccessibilityNodeInfo target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 0,true);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneyNum;
        	if(!nodeInput(target,txt))return false;
        	//��
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "������", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText",1,false);
        	if(target==null)return false;
        	txt=Config.sMoneyValue;
        	if(!nodeInput(target,txt))return false;
        	//����
        	//target=AccessibilityHelper.findNodeInfosByText(rootNode, "��ϲ����", 0) ;
        	target=AccessibilityHelper.findNodeInfosByClassName(rootNode, "android.widget.EditText", 2,false);
        	if(target==null)return false;
        	txt=Config.sLuckyMoneySay;
        	if(!nodeInput(target,txt))return false;
        	//��Ǯ��
        	target=AccessibilityHelper.findNodeInfosByText(rootNode, "��Ǯ", 0) ;
        	if(target==null)return false;
        	if(!target.performAction(AccessibilityNodeInfo.ACTION_CLICK))return false;
        	return true;
    }
   
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  boolean nodeInput(AccessibilityNodeInfo edtNode,String txt){
    	if(Config.currentapiVersion>=Build.VERSION_CODES.LOLLIPOP){//android 5.0
    		Bundle arguments = new Bundle();
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
        	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	return true;
    	}
    	if(Config.currentapiVersion>=Build.VERSION_CODES.JELLY_BEAN_MR2){//android 4.3
    		ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
    		ClipData clip = ClipData.newPlainText("text",txt);  
    		clipboard.setPrimaryClip(clip);  

    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  
    		////ճ����������  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	if(Config.currentapiVersion>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(100);
        	if(RootShellCmd.getRootShellCmd().execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(1000);
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
  

    
}
