/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import com.byc.qqclearthunder2.R;
import com.example.h3.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private String[] mReceiveInfo={"","","",""};//�����Ϣ��
	private int mIntInfo=0;//��Ϣ����
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	private SpeechUtil speaker ;
	private BackgroundMusic mBackgroundMusic;
	private FloatingWindowPic fwp;
	private String mMoneyValue;//���������
	private String mSayThunder;//��ֵ��
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
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
    /*�Ƿ�����ϸ��Ϣ����*/
    public boolean isDetailUI(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"����",0);
    	if(aNode==null)return false;
    	aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"QQ���",0);
    	if(aNode==null)return false;
    	aNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�����¼",0);
    	if(aNode==null)return false;
    	return true;
    }
    /*��ʾ��ϸ��Ϣ*/
    public  boolean unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�յ��ĺ���Ѵ������    ���ʹ��",0);
    	if(LuckyMoneyNode!=null&&LuckyMoneyNode.getText()!=null){
    		if("�յ��ĺ���Ѵ������    ���ʹ��".equals(LuckyMoneyNode.getText().toString())){
    			UnpackLuckyMoneyShowDigital();//����������ʾ��
    			//��ȡ���ͺ���
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    			//�ж��Ƿ����װ���
    			if(checkLuckyMoneySayType(mReceiveInfo[0])==Config.TYPE_LUCKYMONEY_THUNDER){
    				LuckyMoneyDetailShow(mReceiveInfo[1],mReceiveInfo[0]);   
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
      public void LuckyMoneyDetailShow(String je,String say){
      	//��ȡ������
      	int iMoneyValuePos=Config.getConfig(context).getMoneyValuePos();
      	int iMoneySayPos=Config.getConfig(context).getMoneySayPos();
      	//��ܳɹ���������3.0Ԫ����ֵΪ��7�����׳ɹ���
      	//���ʧ�ܣ�δ��Ȩ�û�������͸�ӷ��񣡶��ʧ�ܣ�
      	String sMoneyValue=je.substring(0,je.length()-1);
      	//Log.i(Config.TAG, sMoneyValue);
      	String sMoneySay=say;
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
  			if(mIntInfo==4){//�����
  				if(info.getText()!=null)
  					mReceiveInfo[0]=info.getText().toString();	
  				else mReceiveInfo[0]="10/1";	
  			}
  			if(mIntInfo==9){//��
  				if(info.getText()!=null)
  					mReceiveInfo[1]=info.getText().toString();	
  				else mReceiveInfo[1]="0.01Ԫ";
  				String sYuan=mReceiveInfo[1];
  				sYuan=sYuan.substring(sYuan.length()-1,sYuan.length());
  				if(sYuan.equals("Ԫ"))
  					bRecycled=true;
  			}
  			if(mIntInfo==10){//��
  				if(info.getText()!=null)
  					mReceiveInfo[1]=info.getText().toString();	
  				else mReceiveInfo[1]="0.01Ԫ";
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
    public void LuckyMoneyDetailShow(AccessibilityNodeInfo info){
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
    	String sSayThunder="";
    	String sSayThunder2="";//˫��;
    	String sMoneyThunder="";
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
    		
    	}
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
    	}
    	String sShow="";
    	boolean bCrash=false;
    	//�Ƚϵ�һ���ף�
    	if(sMoneyThunder.equals(sSayThunder))bCrash=true;
    	//�Ƚϵڶ����ף�
    	if(Config.iThunderNum==2){//˫��;
    		if(sMoneyThunder.equals(sSayThunder2))bCrash=true;
    	}
    	//�������ã�
    	if(bCrash){
    		if(!Config.bReg)sShow="���ð治���ܱ���͸�ӷ����빺�����棡";//δ��Ȩ
    	}else{//δ����
    		sShow="��ϲ���������"+sMoneyValue+"Ԫ����ֵΪ��"+sSayThunder+",����͸�ӳɹ���";
    	}
    	//��ʾ��
    	if(!sShow.equals("")){
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		speaker.speak(sShow);	
    	}
    	
    }

}
