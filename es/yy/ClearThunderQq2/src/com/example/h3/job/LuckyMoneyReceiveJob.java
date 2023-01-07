/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.byc.qqclearthunder2.R;
import com.example.h3.BackgroundMusic;
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
	private Context context;
	public  String TAG = "byc001";//���Ա�ʶ��
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;
	private String mMoneyValue;//���������
	private String mSayThunder;//��ֵ��
	private String mManOfSendLuckyMoney;//�����ߣ�

	private String[] mReceiveInfo={"","","","",""};//�����Ϣ��
	private int mIntInfo=0;//��Ϣ����
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	
	private static final int KEY_CRASH_THUNDER_NO=0;//û�����ף�
	private static final int KEY_CRASH_THUNDER_YES=1;//���ף�
	private static final int KEY_CRASH_THUNDER_UNSURE=2;//��ȷ����
	private int iCrashThunder=0;//�������ͣ�
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
		this.TAG=Config.TAG;
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
    	AccessibilityNodeInfo aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"����һ���������������",0);
    	if(aNode!=null&&aNode.getText()!=null){
    		if("����һ���������������".equals(aNode.getText().toString()))return true;else return false;
    	}
    	return false;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
     * �Ӻ�����л�ȡ�ܽ�
     */
    public String getTotalMoney(String say){
    	//�жϵڶ�λ�Ƿ���0��
    	String totalMoney="";
    	String c2=say.substring(1,2);
    	if(c2.equals("0")){//�����ǰ��
    		String c3=say.substring(2,3);//����λ
    		if(c3.equals("0")){//����λΪ0��˵��������100��
    			totalMoney=say.substring(0,3);//ȡ�ܽ�
    		}else{//�ܽ�� С��100
    			totalMoney=say.substring(0,2);//ȡ�ܽ�
    		}
    		return totalMoney;
    	}else{//�ܽ����ĩλ��
    		String cc1=say.substring(say.length()-1,say.length());
    		if(cc1.equals("0")){//��һ���ж��ܽ���Ƿ���ĩλ��
    			String cc2=say.substring(say.length()-2,say.length()-1);//�����ڶ�λ��
    			if(cc2.equals("0")){//���Ϊ����100
    				totalMoney=say.substring(say.length()-3,say.length());//ȡ�ܽ�
    			}else{//�����ڶ�λ��
    				totalMoney=say.substring(say.length()-2,say.length());//ȡ�ܽ�
    			}//���С��100
    			return totalMoney;
    		}else{//if(cc1.equals("0")){
    			int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    			if(iMoneySayPos==Config.KEY_THUNDER_Middle)//�����м䣬˵����һλΪ��
    				totalMoney=say.substring(0,1);//ȡ�ܽ�
    			if(iMoneySayPos==Config.KEY_THUNDER_HEAD)//������ߣ�˵��������ң�
    				totalMoney=say.substring(say.length()-1,say.length());//ȡ�ܽ�
    			if(iMoneySayPos==Config.KEY_THUNDER_TAIL)//�����ұߣ�˵���������
    				totalMoney=say.substring(0,1);//ȡ�ܽ�
    		}//�����ڶ�λ�뵹����һλ����Ϊ0��˵�����Ϊ��λ����
    	}//if(c2.equals("0")){//�����ǰ�� }else{//�ܽ����ĩλ��
    	return totalMoney;
    }
    
    /*
     * ��ȡ������е��ף��޳������ �� �Ľ��������ַ�����
     */
    public String getSayThunder(String say,String  totalMoney){
    	int i=say.indexOf(totalMoney);
    	if(i==-1)return say;
    	String thunder=say.replace(totalMoney,"");
    	//�ų������м�λ��ѡ���׵������
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	if(Config.iThunderNum==Config.KEY_THUNDER_SINGLE&&iMoneySayPos==Config.KEY_THUNDER_Middle){
    		thunder=thunder.substring(0,1);
    	}
    	return thunder;
    }
    /*
     * ��ȡ����е��ף�
     */
    public String getMoneyThunder(String  je){
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	String sMoneyThunder="";
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=je.substring(je.length()-1,je.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=je.substring(je.length()-2,je.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=je.substring(je.length()-4,je.length()-3);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		String sFen=je.substring(je.length()-1,je.length());
    		String sJiao=je.substring(je.length()-2,je.length()-1);
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
    public  boolean unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo SeeDetailNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�鿴��ȡ����",0);
    	if(SeeDetailNode==null)return false;
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�Ѵ������",0);
    	if(LuckyMoneyNode!=null&&LuckyMoneyNode.getText()!=null){
    		if("�Ѵ������".equals(LuckyMoneyNode.getText().toString())){
    			UnpackLuckyMoneyShowDigital();//����������ʾ��
    			//��ȡ���ͺ���
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    			//�Ƿ�ȡ����ȷ�Ľ�
    			//if(!AccessibilityHelper.isMoney(mReceiveInfo[1]))return false;
    			//if(!AccessibilityHelper.isDigital(mReceiveInfo[1]))return false;
    			//�ж��Ƿ����װ���
    			if(checkLuckyMoneySayType(mReceiveInfo[0])==Config.TYPE_LUCKYMONEY_THUNDER){
    				LuckyMoneyDetailShow(mReceiveInfo[0],mReceiveInfo[1]);   
    				return true;
    			}
    		}
    	}
    	return false;
    }
    public int checkLuckyMoneySayType(String LuckyMoneySay){

		String LuckyMoneySayTail=LuckyMoneySay.substring(LuckyMoneySay.length()-1,LuckyMoneySay.length());
		if(!AccessibilityHelper.isDigital(LuckyMoneySayTail))
			return Config.TYPE_LUCKYMONEY_WELL;
		else
			return Config.TYPE_LUCKYMONEY_THUNDER;
}
    public void LuckyMoneyDetailShow(String say,String je){
    	String sMoneyThunder=getMoneyThunder(je);//����е��ף�
    	String totalMoney=getTotalMoney(say);//�Ӻ�����з������
    	String sSayThunder=getSayThunder(say,totalMoney);//������е���;
    	int iThunder=CrashThunder(sSayThunder,sMoneyThunder);//��������;

    	mMoneyValue=je;//���;
    	mSayThunder=sSayThunder;//��ֵ��
    	mManOfSendLuckyMoney=mReceiveInfo[2];//������
    	//��ʾ�����
    	showResult(iThunder);
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
			//ȡ��Ϣ
			if(mIntInfo==2){//����ţţ������Ա��
				if(info.getText()!=null)
					mReceiveInfo[2]=info.getText().toString();	
				else mReceiveInfo[2]="��������";	
			}
			if(mIntInfo==3){//�����
				if(info.getText()!=null)
					mReceiveInfo[0]=info.getText().toString();	
				else mReceiveInfo[0]="10/1";	
			}
			if(mIntInfo==4){//��
				if(info.getText()!=null)
					mReceiveInfo[1]=info.getText().toString();	
				else mReceiveInfo[1]="0.01";
				bRecycled=true;
			}
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
    		say="��ϲ������"+mManOfSendLuckyMoney+"�ĺ��"+mMoneyValue+"Ԫ����ֵΪ��"+mSayThunder+",����͸�ӳɹ���";//δ��Ȩ
    	}
    	//��ʾ��
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
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
    /*
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
    	if(Config.iThunderNum>2)Config.iThunderNum=1;
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){//βΪ��
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//˫��;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//˫��;
    			sSayThunder2=sMoneySay.substring(2,3);
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(1,2);//23.10
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ){//�Զ�ʶ��
    		//�ӵ����ڶ�λ�жϣ�
    		String s=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);
    		if(AccessibilityHelper.isDigital(s)){//���ڵ�һλ
    			sSayThunder=sMoneySay.substring(0,1);
        		if(Config.iThunderNum==2){//˫��;
        			sSayThunder2=sMoneySay.substring(2,3);
        			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(1,2);//23.10
        		}
    		}else{//�������һλ
        		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
        		if(Config.iThunderNum==2){//˫��;
        			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);
        			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
        		}
    		}
    		
    	}else if(iMoneySayPos==Config.KEY_THUNDER_Middle){//�м�λΪ��
    		String c=sMoneySay.substring(1,2);//���磺1/245
    		if(!AccessibilityHelper.isDigital(c)){
    			sSayThunder=sMoneySay.substring(2,3);
    			if(Config.iThunderNum==2)sSayThunder2=sMoneySay.substring(3,4);//˫��;
    		}else{
    			c=sMoneySay.substring(2,3);//���磺10/245
    			if(!AccessibilityHelper.isDigital(c)){
        			sSayThunder=sMoneySay.substring(3,4);
        			if(Config.iThunderNum==2)sSayThunder2=sMoneySay.substring(4,5);//˫��;
    			}else{
    				c=sMoneySay.substring(3,4);//���磺100/245
    				if(!AccessibilityHelper.isDigital(c)){
            			sSayThunder=sMoneySay.substring(4,5);
            			if(Config.iThunderNum==2)sSayThunder2=sMoneySay.substring(5,6);//˫��;
    				}//if(!AccessibilityHelper.isDigital(c)){
    			}//if(!AccessibilityHelper.isDigital(c)){
    		}//if(!AccessibilityHelper.isDigital(c)){
    		
    	}//else if(iMoneySayPos==Config.KEY_THUNDER_Middle){//�м�λΪ��
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_TWO_TAIL_ADD){
    		String sFen=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    		String sJiao=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    		int iFen=0;
    		int iJiao=0;
    		try {
    		    	iFen = Integer.parseInt(sFen);
    		    	iJiao = Integer.parseInt(sJiao);
    		} catch (NumberFormatException e) {
    		    e.printStackTrace();
    		    Log.i(TAG, "[KEY_THUNDER_TWO_TAIL_ADD]����������ת��ʧ�ܣ�");
    		    return;
    		}
    		String tmp=String.valueOf(iFen+iJiao);
    		sMoneyThunder=tmp.substring(tmp.length()-1,tmp.length());
    	}

    	boolean bCrash=false;
    	//�Ƚϵ�һ���ף�
    	if(sMoneyThunder.equals(sSayThunder))bCrash=true;
    	//�Ƚϵڶ����ף�
    	if(Config.iThunderNum==2){//˫��;
    		if(sMoneyThunder.equals(sSayThunder2))bCrash=true;
    	}
    	mMoneyValue=sMoneyValue;//���;
    	mSayThunder=sSayThunder;//��ֵ��
    	mManOfSendLuckyMoney=mReceiveInfo[2];//������
    	//��ʾ�����
    	showResult(bCrash);
    }
    */

}
