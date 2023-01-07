package com.example.h3.job;

import com.byc.ControlMoneyQQ.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.util.FloatingWindowPic;
import com.example.h3.util.FloatingWindowText;
import com.example.h3.util.SpeechUtil;

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
	private SpeechUtil speaker ;
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
		speaker=SpeechUtil.getSpeechUtil(context);
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
    	speaker.speak("����Ϊ���ύ�������ݣ�");
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
              	String thunder="";
              	if(Config.iPlaying==Config.KEY_PUT_THUNDER_BUMP_PLAYING){
              		thunder=String.valueOf(getRadomNum());
              		Config.sLuckyMoneyNum="5";
              		Config.sLuckyMoneySay=thunder;
              	}else{
              		thunder=getThunder();
              		if(Config.iMoneySayPos==Config.KEY_THUNDER_TAIL)
              			Config.sLuckyMoneySay=Config.sMoneyValue+"/"+thunder;
              		if(Config.iMoneySayPos==Config.KEY_THUNDER_HEAD)
              			Config.sLuckyMoneySay=thunder+"/"+Config.sMoneyValue;
              	}
              	//String say="�����ֵΪ��"+thunder;
              	String say="���ύÿ����������";
              	//2����ʾ�����ֵ��
              	fwt.ShowFloatingWindow();
      
              	fwt.tvJE.setText("�������Ϊ��"+Config.sMoneyValue+"Ԫ");
              	fwt.tvNum.setText("��������Ϊ��"+Config.sLuckyMoneyNum+"��");
              	fwt.tvThunder.setText(say);
              	speaker.speak(say);
              	fwt.mShowTime=50;
              	//�㲥��ֵ��
              	sendBroadcastThunder(thunder);
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
      //����һ��0~9�������(�ų�һ�����֣���
      private int getRadomNumNoOne(int t){
      	int i=getRadomNum();
      	if(i==t)i=getRadomNumNoOne(t);
      	return i;
      }
      //����һ��0~9�������(�ų��������֣���
      private int getRadomNumNoTwo(int t1,int t2){
      	int i=getRadomNum();
      	if(i==t1||i==t2)i=getRadomNumNoTwo(t1,t2);
      	return i;
      }
      //����һ��0~9�������(�ų��������֣���
      private int getRadomNumNoThree(int t1,int t2,int t3){
      	int i=getRadomNum();
      	if(i==t1||i==t2||i==t3)i=getRadomNumNoThree(t1,t2,t3);
      	return i;
      }
      //����һ��0~9�������(�ų��ĸ����֣���
      private int getRadomNumNoFour(int t1,int t2,int t3,int t4){
      	int i=getRadomNum();
      	if(i==t1||i==t2||i==t3||i==t4)i=getRadomNumNoFour(t1,t2,t3,t4);
      	return i;
      }
      private String getThunder(){
      	String thunder="";
      	int t=0;
      	int t1=1;
      	int t2=2;
    	int t3=3;
    	int t4=4;
      	switch(Config.iMoneySayThunderNum){
      	case Config.KEY_MONEY_SAY_SINGLE_THUNDER:
      		t=getRadomNum();
      		thunder=String.valueOf(t);
      		break;
      	case Config.KEY_MONEY_SAY_DOUBLE_THUNDER:
      		t=getRadomNum();
      		t1=getRadomNumNoOne(t);
      		thunder=String.valueOf(t)+"/"+String.valueOf(t1);
      		break;
      	case Config.KEY_MONEY_SAY_THREE_THUNDER:
      		t=getRadomNum();
      		t1=getRadomNumNoOne(t);
      		t2=getRadomNumNoTwo(t,t1);
      		thunder=String.valueOf(t)+"/"+String.valueOf(t1)+"/"+String.valueOf(t2);
      		break;
    	case Config.KEY_MONEY_SAY_FOUR_THUNDER:
    		t=getRadomNum();
    		t1=getRadomNumNoOne(t);
    		t2=getRadomNumNoTwo(t,t1);
    		t3=getRadomNumNoThree(t,t1,t2);
    		thunder=String.valueOf(t)+String.valueOf(t1)+String.valueOf(t2)+String.valueOf(t3);
    		break;
    	case Config.KEY_MONEY_SAY_FIVE_THUNDER:
    		t=getRadomNum();
    		t1=getRadomNumNoOne(t);
    		t2=getRadomNumNoTwo(t,t1);
    		t3=getRadomNumNoThree(t,t1,t2);
    		t4=getRadomNumNoFour(t,t1,t2,t3);
    		thunder=String.valueOf(t)+String.valueOf(t1)+String.valueOf(t2)+String.valueOf(t3)+String.valueOf(t4);
    		break;
      	}
      	return thunder;
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
      		String say="�������ð��û���";
  			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
  			speaker.speak(say);
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
        	//if(!nodeInput(target,txt))return false;
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
