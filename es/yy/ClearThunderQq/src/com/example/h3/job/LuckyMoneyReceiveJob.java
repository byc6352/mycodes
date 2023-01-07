/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.byc.ClearThunderQq.R;
import util.BackgroundMusic;
import com.example.h3.Config;

import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyReceiveJob {
	private static LuckyMoneyReceiveJob current;
	public  String TAG = "byc001";//���Ա�ʶ��
	private Context context;
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;
	private String mMoneyValue;//���������
	private String mSayThunder;//��ֵ��
	
	private static final String DIGITAL="0123456789";//
	private String[] mReceiveInfo={"","","","",""};//�����Ϣ��
	private int mIntInfo=0;//��Ϣ����
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	private static final int KEY_CRASH_THUNDER_NO=0;//û�����ף�
	private static final int KEY_CRASH_THUNDER_YES=1;//���ף�
	private static final int KEY_CRASH_THUNDER_UNSURE=2;//��ȷ����
	private int iCrashThunder=0;//�������ͣ�
	private String mManOfSendLuckyMoney;//�����ߣ�
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//���չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_DISPLAY_SUCCESS);
        filter.addAction(Config.ACTION_DISPLAY_FAIL);
        context.registerReceiver(ClickLuckyMoneyReceiver, filter);
	}
    public static synchronized LuckyMoneyReceiveJob getLuckyMoneyReceiveJob(Context context) {
        if(current == null) {
            current = new LuckyMoneyReceiveJob(context);
        }
        return current;
        
    }
    /*�ж��Ƿ��Ǻ�����괰��*/
    public boolean isNoLuckyMoneyUI(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"����һ����������~",0);
    	if(aNode!=null&&aNode.getText()!=null){
    		if("����һ����������~".equals(aNode.getText().toString()))return true;else return false;
    	}
    	return false;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public  boolean unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�Ѵ������",0);
    	if(LuckyMoneyNode!=null){
    		
    			UnpackLuckyMoneyShowDigital();//����������ʾ��
    			//��ȡ���ͺ���
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    			//�ж��Ƿ����װ���
    			if(checkLuckyMoneySayType(mReceiveInfo[0])==Config.TYPE_LUCKYMONEY_THUNDER){
    				LuckyMoneyDetailShow2(mReceiveInfo[1],mReceiveInfo[0]);   
    				return true;
    			}
    	}
    	return false;
    }
    public int checkLuckyMoneySayType(String LuckyMoneySay){
    	if(LuckyMoneySay.length()==0)return Config.TYPE_LUCKYMONEY_WELL;
		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
		if(DIGITAL.indexOf(LuckyMoneySayTail)==-1)
			return Config.TYPE_LUCKYMONEY_WELL;
		else
			return Config.TYPE_LUCKYMONEY_THUNDER;
}
    public void LuckyMoneyDetailShow2(String je,String say){
    	String sMoneyThunder=getMoneyThunder(je);//����е��ף�
    	String sSayThunder="";//������е���;
    	if(Config.iMoneySay==Config.KEY_THUNDER_IS_SAY){//�������� �� ��
    		sSayThunder=say;
    	}else{
        	
        	sSayThunder=getSayThunder(say);//������е���;
    	}
    	int iThunder=CrashThunder(sSayThunder,sMoneyThunder);//��������;

    	mMoneyValue=je;//���;
    	//mTotalMoney=totalMoney;//�� ���
    	mSayThunder=sSayThunder;//��ֵ��
    	mManOfSendLuckyMoney=mReceiveInfo[2];//������
    	//��ʾ�����
    	showResult(iThunder);
    }
    //��ʾ�ɹ���ʧ�ܽ��棺
    private void showResult(int iCrashThunder) {

    	String say="";
    	if(iCrashThunder>0){
    		mBackgroundMusic.playBackgroundMusic( "ao.mp3", false);
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_FAIL;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_FAIL;
    		fwp.StartSwitchPics();
    		if(Config.bReg)
    			if(iCrashThunder==1)
    				say="�����ӳ٣������ˣ�";//��Ȩ
    			else
    				say="�����ӳ٣����������ˣ�";//��Ȩ
    		else
    			if(iCrashThunder==1)
    				say="�����ˣ����ð治���ܱ���͸�ӷ����빺�����棡";//δ��Ȩ
    			else
    				say="���������ˣ����ð治���ܱ���͸�ӷ����빺�����棡";//δ��Ȩ
    	}else{
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_SUCCESS;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_SUC;
    		fwp.StartSwitchPics();
    		say="��ϲ������"+mManOfSendLuckyMoney+mMoneyValue+"Ԫ����ֵΪ��"+mSayThunder+",����͸�ӳɹ���";//δ��Ȩ
    	}
    	//��ʾ��
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    }
    /*
     * �Ƿ����ף��������ͣ�1���ף�2�������ף�0:û������;
     */
    public int CrashThunder(String SayThunder,String MoneyThunder){
    	if(SayThunder.indexOf(MoneyThunder)==-1){//û������:
    		return KEY_CRASH_THUNDER_NO;
    	}else{//���ף�
    		int i=getThunderNum(SayThunder);//�׵���ʵ������
    		if(i>1)return KEY_CRASH_THUNDER_UNSURE;else return KEY_CRASH_THUNDER_YES;
    	}

    }
    /*
     * �����׵���ʵ������
     */
    public int getThunderNum(String SayThunder){
    	String c="";
    	int k=0;
    	for(int i=0;i<SayThunder.length()-1;i++){
    		c=SayThunder.substring(i,i+1);
    		if(AccessibilityHelper.isDigital(c))k=k+1;
    	}
    	return  k;
    }
    /*
     * ��ȡ������е��ף��޳������ �� �Ľ��������ַ�����
     */
    public String getSayThunder(String say){
    	//�ų������м�λ��ѡ���׵������
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	String thunder=say;
    	try{
    	switch(Config.iThunderNum){
    	case Config.KEY_THUNDER_SINGLE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-1,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_DOUBLE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-2,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,3);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_THREE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-3,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,4);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,3);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,3);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_FOUR:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-4,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,5);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,4);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,4);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_FIVE:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-5,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,6);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,5);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,5);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	case Config.KEY_THUNDER_ANY:
    		if(iMoneySayPos==Config.KEY_THUNDER_TAIL)thunder=say.substring(say.length()-1,say.length());
    		if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)thunder=say.substring(1,2);
    		if(iMoneySayPos==Config.KEY_THUNDER_HEAD)thunder=say.substring(0,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)thunder=getSayThunderAuto(say,1);
    		if(iMoneySayPos==Config.KEY_THUNDER_IS_SAY)thunder=say;
    		break;
    	}
    	}catch(IndexOutOfBoundsException e){
    		e.printStackTrace();
    		thunder=say;
    	}
    	return thunder;
    }
    /*
     * ��ȡ������е��ף�����ʶ�𣺴ӵڶ�λ�Ƿ�Ϊ0�жϣ�
     */
    public String getSayThunderAuto(String say,int thunderNum){
    	String zero=say.substring(1,2);
    	if(zero.equals("0"))return say.substring(say.length()-thunderNum,say.length());
    	else return say.substring(0,thunderNum);
    }
    /*
     * ��ȡ����е��ף�
     */
    public String getMoneyThunder(String  je){
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	//String sMoneyThunder=je.replace("Ԫ", "");
    	String sMoneyThunder=je;
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyThunder.substring(sMoneyThunder.length()-1,sMoneyThunder.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyThunder.substring(sMoneyThunder.length()-2,sMoneyThunder.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyThunder.substring(sMoneyThunder.length()-4,sMoneyThunder.length()-3);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		String sFen=sMoneyThunder.substring(sMoneyThunder.length()-1,sMoneyThunder.length());
    		String sJiao=sMoneyThunder.substring(sMoneyThunder.length()-2,sMoneyThunder.length()-1);
    		int iFen=0;
    		int iJiao=0;
    		try {
    		    	iFen = Integer.parseInt(sFen);
    		    	iJiao = Integer.parseInt(sJiao);
    		} catch (NumberFormatException e) {
    		    e.printStackTrace();
    		    Log.i(TAG, "[KEY_THUNDER_TWO_TAIL_ADD]����������ת��ʧ�ܣ�");
    		    return je;
    		}
    		String tmp=String.valueOf(iFen+iJiao);
    		sMoneyThunder=tmp.substring(tmp.length()-1,tmp.length());
    	}
    	return sMoneyThunder;
    }
    public void LuckyMoneyDetailShow(String je,String say){
    	//��ȡ������
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	//��ܳɹ���������3.0Ԫ����ֵΪ��7�����׳ɹ���
    	//���ʧ�ܣ�δ��Ȩ�û�������͸�ӷ��񣡶��ʧ�ܣ�
    	String sMoneyValue=je;
    	String sMoneySay=say;
    	String sSayThunder="";
    	String sSayThunder2="";//˫��;
    	String sMoneyThunder="";
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//˫��;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//˫��;
    			sSayThunder2=sMoneySay.substring(2,3);
    		}
    	}
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
    	}
    	boolean bCrash=false;
    	//�Ƚϵ�һ���ף�
    	if(sMoneyThunder.equals(sSayThunder))bCrash=true;
    	//�Ƚϵڶ����ף�
    	if(Config.iThunderNum==2){//˫��;
    		if(sMoneyThunder.equals(sSayThunder2))bCrash=true;
    	}
    	mMoneyValue=sMoneyValue;
    	mSayThunder=sSayThunder;
    	//��ʾ�����
    	showResult(bCrash);
    }
  
    //�����ʾ���֣�
    private void UnpackLuckyMoneyShowDigital() {
    	
    	speaker.speak("����Ϊ��������");
    	float f=(float) (Math.random()*10000);
    	String s=String.valueOf(f);
    	Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    
    private void recycleGetJeAndSay(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			if(info.getText()==null)return;
			if(Config.DEBUG)Log.i(TAG, mIntInfo+info.getText().toString());
			//ȡ��Ϣ
			if(mIntInfo==1)mReceiveInfo[2]=info.getText().toString();//��������ˣ�
			if(mIntInfo==5)mReceiveInfo[0]=info.getText().toString();//�����
			if(mIntInfo==2)mReceiveInfo[1]=info.getText().toString();//���
			mIntInfo=mIntInfo+1;
			
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycleGetJeAndSay(info.getChild(i));
				}
			}
		}
	}
    //��ʾ�ɹ���ʧ�ܽ��棺
    private void showResult(boolean bCrash) {

    	String say="";
    	if(bCrash){
    		mBackgroundMusic.playBackgroundMusic( "ao.mp3", false);
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_FAIL;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_FAIL;
    		fwp.StartSwitchPics();
    		if(Config.bReg)
    			say="�����ӳ٣������ˣ�";//��Ȩ
    		else
    			say="�����ˣ����ð治���ܱ���͸�ӷ����빺�����棡";//δ��Ȩ
    	}else{
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_SUCCESS;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_SUC;
    		fwp.StartSwitchPics();
    		say="��ϲ���������"+mMoneyValue+"Ԫ����ֵΪ��"+mSayThunder+",����͸�ӳɹ���";//δ��Ȩ
    	}
    	//��ʾ��
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    	//fwp.StopSwitchPics();
    	//fwp.RemoveFloatingWindowPic();
    	//mBackgroundMusic.stopBackgroundMusic();
    }
	private BroadcastReceiver ClickLuckyMoneyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_DISPLAY_SUCCESS.equals(action)) {
            	
            }
            if(Config.ACTION_DISPLAY_FAIL.equals(action)) {
            	mBackgroundMusic.stopBackgroundMusic();
            }
        }
    };


}
