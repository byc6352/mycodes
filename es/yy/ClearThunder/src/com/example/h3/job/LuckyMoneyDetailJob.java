/**
 * 
 */
package com.example.h3.job;

import util.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.byc.ClearThunder.R;
import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.Funcs;
import util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyDetailJob {
	private static LuckyMoneyDetailJob current;
	private Context context;
	public  String TAG = "byc001";//���Ա�ʶ��
	private String[] mReceiveInfo={"","","",""};//�����Ϣ:0.�����ˣ�1.����2.�����Ľ�--��
	private int mIntInfo=0;//��Ϣ����
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	private static final int KEY_CRASH_THUNDER_NO=0;//û�����ף�
	private static final int KEY_CRASH_THUNDER_YES=1;//���ף�
	private static final int KEY_CRASH_THUNDER_UNSURE=2;//��ȷ����
	private SpeechUtil speaker ;
	private FloatingWindowPic fwp;
	private BackgroundMusic mBackgroundMusic;
	private String mMoneyValue;//���������
	private String mTotalMoney;//�ܽ�
	private String mSayThunder;//��ֵ��
	private String mManOfSendLuckyMoney;//�����ߣ�
	//private LuckyMoneyLauncherJob LancherJob;
	private LuckyMoney mLuckyMoney;//�������
	//private static final String WECHAT_GONG_XI="��ϲ���ƣ��󼪴�����";//
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	this.TAG=Config.TAG+"_LuckyMoneyDetailJob";
    	speaker=SpeechUtil.getSpeechUtil(context);
        mBackgroundMusic=BackgroundMusic.getInstance(context);
        //LancherJob=LuckyMoneyLauncherJob.getLuckyMoneyLauncherJob(context);
        mLuckyMoney=LuckyMoney.getLuckyMoney(context);//�������
        
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
    
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void recycle(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//ȡ��Ϣ
			mReceiveInfo[mIntInfo]=info.getText().toString();

			//Log.i(TAG, "child widget----------------------------" + info.getClassName());
			//Log.i(TAG, "Text��" + info.getText());
			//Log.i(TAG, "windowId:" + info.getWindowId());
			if(mIntInfo==2){bRecycled=true;return;}
			mIntInfo=mIntInfo+1;
		} else {
			for (int i = 0; i < info.getChildCount(); i++) {
				if(info.getChild(i)!=null){
					recycle(info.getChild(i));
				}
			}
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
     * ��ʾ���������
     */
    public void LuckyMoneyDetailShow(AccessibilityNodeInfo info){
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	String man=mReceiveInfo[0];//�����ˣ�
    	String say=mReceiveInfo[1];//����
    	String je=mReceiveInfo[2];//�����
    	//��������Ƿ�ϸ��Ƿ����װ�����
    	if(mLuckyMoney.checkLuckyMoneySayType(say)!=mLuckyMoney.TYPE_LUCKYMONEY_THUNDER)return;//�������
    	if(!Funcs.isMoney(je))return ;//����
    	String sMoneyThunder=getMoneyThunder(je);//����е��ף�
    	String sSayThunder="";//������е���;
    	String totalMoney=getTotalMoneyFromSay(say);//�ܽ��;
    	
    	
    	if(Config.iMoneySay==Config.KEY_THUNDER_SAY_IS_THUNDER){//�������� �� ��
    		sSayThunder=say;
    	}else{
        	
        	sSayThunder=getSayThunder(say,totalMoney);//������е���;
    	}

    	int iThunder=CrashThunder(sSayThunder,sMoneyThunder);//��������;
    	mMoneyValue=je;//���;
    	mTotalMoney=totalMoney;//�� ���
    	mSayThunder=sSayThunder;//��ֵ��
    	mManOfSendLuckyMoney=man;//������
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
    		say="��ϲ������"+mManOfSendLuckyMoney+"�ĺ��"+mMoneyValue+"Ԫ���ܽ��Ϊ"+mTotalMoney+"Ԫ����ֵΪ��"+mSayThunder+",����͸�ӳɹ���";//δ��Ȩ
    	}
    	//��ʾ��
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    }
    /*
     * ɾ��������еĽ�
     */
    public String DelMoneyFromSay(String say,String money){
    	int i=say.indexOf(money);
    	int len=money.length();
    	String thunder=say;
    	if(i!=-1){
    		if(i==0){//�����ǰ�棺
    			thunder=say.substring(len,say.length());
    		}else{//����ں���
    			thunder=say.substring(0,say.length()-len);
    		}
    	}
    	return thunder;
    }
  
    /*
     * �Ӻ�����л�ȡ�ܽ�
     */
    public String getTotalMoneyFromSay(String say){
    	//�жϵڶ�λ�Ƿ���0��
    	String totalMoney="";
    	if(say.length()<=2)return say;
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
    			if(iMoneySayPos==Config.KEY_THUNDER_MIDDLE)//�����м䣬˵����һλΪ��
    				totalMoney=say.substring(0,1);//ȡ�ܽ�
    			if(iMoneySayPos==Config.KEY_THUNDER_HEAD)//������ߣ�˵��������ң�
    				totalMoney=say.substring(say.length()-1,say.length());//ȡ�ܽ�
    			if(iMoneySayPos==Config.KEY_THUNDER_TAIL)//�����ұߣ�˵���������
    				totalMoney=say.substring(0,1);//ȡ�ܽ�
    			if(iMoneySayPos==Config.KEY_THUNDER_AUTO_READ)//����ʶ��
    				totalMoney=say.substring(0,1);//ȡ�ܽ���һλΪ��
    			if(iMoneySayPos==Config.KEY_THUNDER_SAY_IS_THUNDER)//���������ף�
    				totalMoney="";//ȡ�ܽ���
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
    	//String thunder=say.replace(totalMoney,"");
    	String thunder=DelMoneyFromSay(say,totalMoney);
    	//�ų������м�λ��ѡ���׵������
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	if(Config.iThunderNum==Config.KEY_THUNDER_SINGLE&&iMoneySayPos==Config.KEY_THUNDER_MIDDLE){
    		thunder=thunder.substring(0,1);
    	}
    	return thunder;
    }
    /*
     * ��ȡ����е��ף�
     */
    public String getMoneyThunder(String  je){
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	String sMoneyThunder=je.replace("Ԫ", "");
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
    
  

}


/*
   public void LuckyMoneyDetailShow1(AccessibilityNodeInfo info){
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	//��ȡ������
    	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
    	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
    	//��ܳɹ���������3.0Ԫ����ֵΪ��7�����׳ɹ���
    	//���ʧ�ܣ�δ��Ȩ�û�������͸�ӷ��񣡶��ʧ�ܣ�
    	String sMoneyValue=mReceiveInfo[2];
    	String sMoneySay=mReceiveInfo[1];
    	//String sMoneySay=mLuckyMoneySay;//01-05�޸ģ���Lancher�����ȡ����
    	Log.i(Config.TAG, "Detail-----"+sMoneySay);
    	if(sMoneySay.equals(WECHAT_GONG_XI)){//�ǹ�ϲ���ƣ�
			if(LancherJob.bNewLuckyMoney){//�º��;
				sMoneySay=LancherJob.mLuckyMoneySay;
			}//if(LancherJob.bNewLuckyMoney){//�º��;
		}//if(LuckyMoneySay.equals(WECHAT_GONG_XI)){//�ǹ�ϲ���ƣ�
    	String sSayThunder="";
    	String sSayThunder2="";//˫��;
    	String sMoneyThunder="";
    	if(Config.iThunderNum>2)Config.iThunderNum=1;
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){//βΪ��
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    		if(Config.iThunderNum==2){//˫��;
    			sSayThunder2=sMoneySay.substring(sMoneySay.length()-3,sMoneySay.length()-2);//10/2/3
    			if(!AccessibilityHelper.isDigital(sSayThunder2))sSayThunder2=sMoneySay.substring(sMoneySay.length()-2,sMoneySay.length()-1);//10.23
    		}
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){//��Ϊ��
    		sSayThunder=sMoneySay.substring(0,1);
    		if(Config.iThunderNum==2){//˫��;
    			sSayThunder2=sMoneySay.substring(2,3);//2/3/10
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
    	mMoneyValue=sMoneyValue;
    	mSayThunder=sSayThunder;
    	//��ʾ�����
    	showResult(bCrash);
    	
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
      //��ȡLancher���洫�ݹ����ĺ����
    public static void setLuckyMoneySay(String sLuckyMoneySay){
    	mLuckyMoneySay=sLuckyMoneySay;
    }
    */
