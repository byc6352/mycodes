/**
 * 
 */
package com.example.h3.job;

import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.util.SpeechUtil;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class LuckyMoneyDetailJob {
	private static LuckyMoneyDetailJob current;
	private Context context;
	private String mSayThunder="";//����� �� ����ֵ
	private String mSayThunder2="";//����� �� ����ֵ����ʱ�洢����
	private String mTotalMoneyCount="";//�ܽ��
	private String mSendMan="";//���������
	private String mLMcount="";//�������
	private String[] mReceiveInfo={"","","",""};//�����Ϣ������� ���ܽ���������ߣ����������
	private String[] mTmp={"",""};//��ʱ�洢��
	private String[][] mJeInfo;//��ȡ�����Ϣ��
	private int mRobbedCount=0;//��ȡ������
	private int mCrashedCount=0;//������������
	private int mIntInfo=0;//��Ϣ����
	private boolean bReg=false;//�Ƿ�ע�᣻
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	private SpeechUtil speaker ;
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	bReg=Config.getConfig(context).getREG();
    	speaker=SpeechUtil.getSpeechUtil(context);
    	mJeInfo=new String[4][20];
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
    	//AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�յ��ĺ���Ѵ������    ���ʹ��",0);
    	//if(LuckyMoneyNode!=null&&LuckyMoneyNode.getText()!=null){
    		//if("�յ��ĺ���Ѵ������    ���ʹ��".equals(LuckyMoneyNode.getText().toString())){
    			//��ȡ���ͺ���
    			bRecycled=false;
    			mIntInfo=1;
    			mRobbedCount=0;//��ȡ���������
    			mCrashedCount=0;//��������;
    			recycleGetJeAndSay(rootNode);
    			//������ʾ��
    			return isCrashShow();
    		//}
    	//}
    	//return false;
    }
    //��ȡ���ͺ����
    private void recycleGetJeAndSay(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			//ȡ��Ϣ
			if(mIntInfo==4){//1.�����
				if(info.getText()!=null){
					mReceiveInfo[0]=info.getText().toString();	
					getSayThunder(mReceiveInfo[0]);//��ȡ�������ֵ
				}
				else mReceiveInfo[0]="1";	
			}
			if(mIntInfo==5){//2.�ܽ���0.01Ԫ
				if(info.getText()!=null){
					mReceiveInfo[1]=info.getText().toString();	
				    //mReceiveInfo[3]="��10.00Ԫ";//����------------------
				}
				else mReceiveInfo[1]="��0.01Ԫ";	
			}
			if(mIntInfo==6){//3.��ȡ�ˣ�ţţ���д����ĺ��
				if(info.getText()!=null)
					mReceiveInfo[2]=info.getText().toString();	
				else mReceiveInfo[2]="ţţ���д����ĺ��";	
			}
			if(mIntInfo==7){//3.��ȡ������
				if(info.getText()!=null)
					mReceiveInfo[3]=info.getText().toString();	
				else mReceiveInfo[3]="2�������������";	
			}
			//��ȡ��� ��
			if(mIntInfo>7){
				getMoney(info);
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
    /*�Ƿ��ǽ���ȡ���*/
    public  boolean getMoney(AccessibilityNodeInfo node){ 
    	if(node.getText()==null)return false;
    	String money=node.getText().toString();
    	mTmp[1]=money;//�洢����ֵ��
    	boolean b=getMoneyThunder(money);
    	mTmp[0]=money;//�洢ǰһ��ֵ��
    	return b;
    }
    /*��ȡ����е���ֵ*/
    public  boolean getMoneyThunder(String money){  
    	if(money.length()<2)return false;
    	String yuan=money.substring(money.length()-1,money.length());
    	if(yuan.equals("Ԫ")){
    		String moneyThunder=money.substring(money.length()-2,money.length()-1);
    		if(AccessibilityHelper.isDigital(moneyThunder)){//�ǽ�
    			mJeInfo[0][mRobbedCount]=mTmp[0];//��ȡ�ˣ�
    			mJeInfo[1][mRobbedCount]=money;//��
    			mJeInfo[2][mRobbedCount]=moneyThunder;//��ֵ��
    			if(isPersonCrash(moneyThunder)){
    				mJeInfo[3][mRobbedCount]="1";//���ױ�־��
    				mCrashedCount=mCrashedCount+1;//��������;
    			}
    			mRobbedCount=mRobbedCount+1;
    			return true;
    		}
    	}
    	return false;
    }
    /*��ȡ������е���ֵ*/
    public  boolean getSayThunder(String say){  
    	if(say.length()==0)return false;
    	mSayThunder=say;
    	mSayThunder2=say;
    	return true;
    }
    /*�����Ƿ�����*/
    public  boolean isPersonCrash(String moneyThunder){  
    	if(mSayThunder.indexOf(moneyThunder)==-1)return  false;
    	if(mSayThunder2.length()>0)
    		mSayThunder2=mSayThunder2.replace(moneyThunder, "");
    	return true;
    }
    /*��Ϣ��ʾ*/
    public  boolean isCrashShow(){ 
    	String say="";
    	boolean bCrash=false;
    	if(mSayThunder2.equals("")){//�����ˣ�
    		say="���У�"+mCrashedCount+"�����ף�";
    		bCrash=true;
    	}else{//if(mSayThunder2.equals("")){//�����ˣ�
    		say="û�������ף�";
    	}
		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
		//speeker.speak(sShow);
		speaker.speak(say);
		return bCrash;
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
    	String sMoneyThunder="";
    	if(iMoneySayPos==Config.KEY_THUNDER_TAIL){
    		sSayThunder=sMoneySay.substring(sMoneySay.length()-1,sMoneySay.length());
    	}else if(iMoneySayPos==Config.KEY_THUNDER_HEAD){
    		sSayThunder=sMoneySay.substring(0,1);
    	}
    	if(iMoneyValuePos==Config.KEY_THUNDER_FEN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_JIAO){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);
    	}else if(iMoneyValuePos==Config.KEY_THUNDER_YUAN){
    		sMoneyThunder=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);
    	}
    	String sShow="";
    	//1�����׳ɹ��жϣ�
    	if(sMoneyThunder.equals(sSayThunder)){
    		//���ף�
    		//1�������ð棺2�����棺����ʾ��
    		sShow="���ð治���ܱ���͸�ӷ����빺�����棡";
    		if(!bReg){
    			Toast.makeText(context, "���ð治���ܱ���͸�ӷ����빺�����棡", Toast.LENGTH_LONG).show();
    			speaker.speak(sShow);
    		}
    	}else{
    		//���׳ɹ���
    		sShow="��ϲ���������"+sMoneyValue+"Ԫ����ֵΪ��"+sSayThunder+",����͸�ӳɹ���";
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		//speeker.speak(sShow);
    		speaker.speak("��ϲ���������"+sMoneyValue+"Ԫ");
    		speaker.speak("��ֵΪ��"+sSayThunder+",����͸�ӳɹ���");
    	}
    }

}
