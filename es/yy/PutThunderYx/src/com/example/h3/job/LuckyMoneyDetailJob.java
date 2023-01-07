/**
 * 
 */
package com.example.h3.job;

import com.example.h3.Config;
import com.example.h3.MainActivity;

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
	private String[] mReceiveInfo={"","",""};//�����Ϣ��
	private int mIntInfo=0;//��Ϣ����
	private boolean bReg=false;//�Ƿ�ע�᣻=
	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	private SpeechUtil speeker ;
	
	private static final String txtClassName="android.widget.TextView";//�ı��ؼ���
	private int mLMrobed=0;//����ȡ������
	private int mLMsum=0;//�ܸ�����
	private double mRobedJE=0;//����ȡ��
	private double mSumJE=0;//�ܽ�
	private double mRobJE=0;//δ��ȡ���;
	private String mThunder="";
	
	
    public static synchronized LuckyMoneyDetailJob getLuckyMoneyDetailJob(Context context) {
    	
        if(current == null) {
            current = new LuckyMoneyDetailJob(context);
        }
        return current;
    }
    private LuckyMoneyDetailJob(Context context){
    	this.context = context;
    	bReg=Config.getConfig(context).getREG();
    	speeker=SpeechUtil.getSpeechUtil(context);
    }
    //
    public boolean getRobLuckyMoney(AccessibilityNodeInfo rootNode){
    	if(rootNode==null)return false;
    	//��ֵ��ʼ����
    	mLMrobed=0;//����ȡ������
    	mLMsum=0;//�ܸ�����
    	mRobedJE=0;//����ȡ��
    	mSumJE=0;//�ܽ�
    	mRobJE=0;//δ��ȡ���;
    	mThunder="";//��ֵ��
    	//�ж��Ƿ����Ƿ���ȡ�˺����
    	AccessibilityNodeInfo robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 4, true);
    	if(robNode==null)return false;
    	String robed=String.valueOf(robNode.getText());
    	if(robed==null)return false;
    	if(robed.indexOf("�Ѵ�����Ǯ��")!=-1){//��������ȡ������ʾ��ȡ��
        	robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 2, false);
        	if(robNode==null)return false;
        	String myJE=String.valueOf(robNode.getText());
        	if(myJE==null)return false;
        	String say="��ϲ�����������"+myJE+"Ԫ��ɨβ�ɹ���";
			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
			speeker.speak(say);
			Config.robTailAction=Config.ACTION_CLOSE;
			Config.JobState=Config.STATE_NONE;
			return true;
    	}
    	//����δ��ȡ���������ȡ���������Ϣ������ȡ0/5������0.00/1.00Ԫ
    	robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 2, false);
    	if(robNode==null)return false;
    	String numInfo=String.valueOf(robNode.getText());
    	if(numInfo==null)return false;
    	int robInfo=getRobLuckyMoneyInfo(numInfo);
    	if(robInfo==-1)return false;
    	//��ȡ��ֵ��10/7
    	robNode=AccessibilityHelper.findNodeInfosByClassName(rootNode, txtClassName, 1, false);
    	if(robNode==null)return false;
    	numInfo=String.valueOf(robNode.getText());
    	if(numInfo==null)return false;
    	if(!getThunder(numInfo))return false;
    	//�ж����һ���Ƿ����ף�
    	if(Config.robTailAction==Config.ACTION_CLICK){
    		//����δ���
    		int iSumJE=(int) (mSumJE*100);
    		int iRobedJE=(int) (mRobedJE*100);
    		int iRobJE=iSumJE-iRobedJE;
    		//mRobJE=mSumJE-mRobedJE;
    		//�Ƿ�����
    		//String robJE=Double.toString(mRobJE);
    		String robJE=String.valueOf(iRobJE);
    		String tail=robJE.substring(robJE.length()-1,robJE.length());
    		if(tail.equals(mThunder)){//���һ��Ϊ�ף�
    			Config.robTailAction=Config.ACTION_CLOSE;
    			double dRobJE=iRobJE/100;
    			robJE=String.valueOf(dRobJE);
    			String say="���һ��Ϊ�ס�ֵΪ��"+robJE+"Ԫ��������";
    			Toast.makeText(context, say, Toast.LENGTH_LONG).show();
    			speeker.speak(say);
    		}
    	}
    	//����Launcher���棺
    	//AccessibilityHelper.performBack(service);
    	return true;
    }
    //���ذ�����ȡ�����0δ���ꣻ1ֻ��β����2�����ꣻ-1����
    public int getRobLuckyMoneyInfo(String numInfo){
    	int i=numInfo.indexOf("/");
    	int j=numInfo.indexOf("��");
    	int k=numInfo.indexOf("��");
    	if(i==-1)return -1;
    	if(j==-1)return -1;
    	if(k==-1)return -1;
    	String sRobed=numInfo.substring(3,i);
    	String sSum=numInfo.substring(i+1,j);
    	if(!AccessibilityHelper.isDigital(sRobed))return -1;
    	if(!AccessibilityHelper.isDigital(sSum))return -1;
    	
    	mLMrobed=Integer.valueOf(sRobed);//����ȡ������
    	mLMsum=Integer.valueOf(sSum);//�ܸ�����
    	//��ȡ������ܽ��:
    	String jeInfo=numInfo.substring(k+1,numInfo.length()-1);
    	i=jeInfo.indexOf("/");
    	String jeRobed=jeInfo.substring(0,i);//����ȡ��
    	String jeSum=jeInfo.substring(i+1);//�ܽ��
    	if(!AccessibilityHelper.isDigital(jeRobed))return -1;
    	if(!AccessibilityHelper.isDigital(jeSum))return -1;  
    	mRobedJE=Double.valueOf(jeRobed);
    	mSumJE=Double.valueOf(jeSum);
    	int iRes=mLMsum-mLMrobed;
    	if(iRes==1){//ʣβ��
    		Config.robTailAction=Config.ACTION_CLICK;//ʣ��β����
    		return 1;//
    	}
    	if(iRes==0){
    		Config.robTailAction=Config.ACTION_CLOSE;//�����ꣻ
    		return 2;//
    	}
    	if(iRes>1){
    		Config.robTailAction=Config.ACTION_LOOK;//ʣ�������1��
    		return 0;//
    	}
    	return -1;
    }
    //�ж��Ƿ����װ���
    public boolean getThunder(String say){
    	String thunder=say.substring(say.length()-1,say.length());
    	if(!AccessibilityHelper.isDigital(thunder))return false;
    	mThunder=thunder;
    	return true;
    }
    
    
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
    			speeker.speak(sShow);
    		}
    	}else{
    		//���׳ɹ���
    		sShow="��ϲ���������"+sMoneyValue+"Ԫ����ֵΪ��"+sSayThunder+",����͸�ӳɹ���";
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		//speeker.speak(sShow);
    		speeker.speak("��ϲ���������"+sMoneyValue+"Ԫ");
    		speeker.speak("��ֵΪ��"+sSayThunder+",����͸�ӳɹ���");
    	}
    }

}
