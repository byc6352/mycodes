/**
 * 
 */
package com.example.h3.job;

import com.baidu.android.common.logging.Log;
import util.BackgroundMusic;
import com.example.h3.Config;
import accessibility.AccessibilityHelper;
import com.example.h3.FloatingWindowPic;
import util.SpeechUtil;

import android.content.Context;
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
	
	private static final String DIGITAL="0123456789";//
	private String[] mReceiveInfo={"","","","",""};//�����Ϣ��
	private int mIntInfo=0;//��Ϣ����

	private boolean bRecycled=false;//�Ƿ��˳�ѭ��
	public int mLuckyMoneyType=0;//�������Ѳ���������������װ���
	private SpeechUtil speaker ;
	private BackgroundMusic mBackgroundMusic;
	
	private LuckyMoneyReceiveJob(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
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
    //��ʾ�ɹ���ʧ�ܽ��棺
    public void showAnimation(boolean bSuc,String money) {
    	/*
    	String say="";
    	if(!bSuc){
    		mBackgroundMusic.playBackgroundMusic( "ao.mp3", false);
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_FAIL;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_FAIL;
    		fwp.StartSwitchPics();
    		if(Config.bReg)
    			say="�����ӳ٣�û������";//��Ȩ
    		else
    			say="û���������ð��ٶȽ������빺�����棬�����ٶȼӿ�100����";//δ��Ȩ
    	}else{
    		fwp.ShowFloatingWindow(); 
    		fwp.c=20;
    		fwp.mSendMsg=Config.ACTION_DISPLAY_SUCCESS;
    		fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_SUC;
    		fwp.StartSwitchPics();
    		if(money==null)
    			say="��ϲ����������ˣ�";//δ��Ȩ
    		else
    			say="��ϲ���������"+money+"Ԫ��";
    	}
    	//��ʾ��
    	if(!say.equals("")){
    		Toast.makeText(context,say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);	
    	}
    	*/
    	//fwp.StopSwitchPics();
    	//fwp.RemoveFloatingWindowPic();
    	//mBackgroundMusic.stopBackgroundMusic();
    } 
    
    
    
    public  void unpackLuckyMoneyShow(AccessibilityNodeInfo rootNode){
    	AccessibilityNodeInfo LuckyMoneyNode=AccessibilityHelper.findNodeInfosByText(rootNode,"�Ѵ������",0);
    	if(LuckyMoneyNode!=null){
    	
    			UnpackLuckyMoneyShowDigital();//����������ʾ��
    			//��ȡ���ͺ���
    			bRecycled=false;
    			mIntInfo=1;
    			recycleGetJeAndSay(rootNode);
    	    	//��ܳɹ���������3.0Ԫ����ֵΪ��7�����׳ɹ���
    	    	String sMoneyValue=mReceiveInfo[1];
    	    	String sMoneyFen=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());//��λ
    	    	String sMoneyJao=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);//��λ
    	    	String sMoneyYuan=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);//Ԫλ
    	    	int iMoneyFen=Integer.parseInt(sMoneyFen);
    	    	int iMoneyJao=Integer.parseInt(sMoneyJao);
    	    	int iMoneyYuan=Integer.parseInt(sMoneyYuan);
    	    	String sShow="";
    	    	int nn=1;
    	    	//��ȡţţ�淨����,�ж�����ţ����
    	    	int iNnWangFa=Config.getConfig(context).getNnWangFa();
    	    	//�Ƿ������ð�
    	    	boolean bReg=Config.getConfig(context).getREG();
    	    	switch(iNnWangFa){
    	    	case Config.NN_SINGLE:
    	    		nn=iMoneyFen;
    	    		break;
    	    	case Config.NN_DOUBLE:
    	    		nn=iMoneyFen+iMoneyJao;
    	    		break;
    	    	case Config.NN_THREE:
    	    		nn=iMoneyFen+iMoneyJao+iMoneyYuan;
    	    		break;
    	    	case Config.NN_HT:
    	    		nn=iMoneyFen+iMoneyYuan;
    	    		break;
    	    	case Config.NN_PG:
    	    		nn=iMoneyFen+iMoneyJao;
    	    		break;
    	    	}
    			if(nn>6){
    				sShow="��ϲ������ţ"+nn+",ţţ͸�ӳɹ���";
    			}
    			if(nn==0&&(iNnWangFa!=Config.NN_PG)){
    				sShow="��ϲ������ţţ,ţţ͸�ӳɹ���";
    			}
    			if(nn>0&&nn<7){
    				if(bReg)
    					sShow="";
    				else
    					sShow="����ţ"+nn+",���ð治���ܱ���͸�ӷ����빺�����棡";
    			}
    			if(!sShow.equals("")){
    	    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    	    		speaker.speak(sShow);
    			}
    	}
    }
    private void recycleGetJeAndSay(AccessibilityNodeInfo info) {
    	if(bRecycled)return;
		if (info.getChildCount() == 0) {
			if(info.getText()==null)return;
			if(Config.DEBUG)Log.i(Config.TAG, mIntInfo+info.getText().toString());
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
    //�����ʾ���֣�
    private void UnpackLuckyMoneyShowDigital() {
    	
    	speaker.speak("����Ϊ��������");
    	float f=(float) (Math.random()*10000);
    	String s=String.valueOf(f);
    	Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
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
    
    public void unpackLuckyMoneyShow0(AccessibilityNodeInfo info){
    	mIntInfo=0;
    	bRecycled=false;
    	recycle(info);
    	String sMoneyValue=mReceiveInfo[2];//�����
    	String sMoneyFen=sMoneyValue.substring(sMoneyValue.length()-1,sMoneyValue.length());//��λ
    	String sMoneyJao=sMoneyValue.substring(sMoneyValue.length()-2,sMoneyValue.length()-1);//��λ
    	String sMoneyYuan=sMoneyValue.substring(sMoneyValue.length()-4,sMoneyValue.length()-3);//Ԫλ
    	int iMoneyFen=Integer.parseInt(sMoneyFen);
    	int iMoneyJao=Integer.parseInt(sMoneyJao);
    	int iMoneyYuan=Integer.parseInt(sMoneyYuan);
    	String sShow="";
    	int nn=1;
    	//��ȡţţ�淨����,�ж�����ţ����
    	int iNnWangFa=Config.getConfig(context).getNnWangFa();
    	//�Ƿ������ð�
    	boolean bReg=Config.getConfig(context).getREG();
    	switch(iNnWangFa){
    	case Config.NN_SINGLE:
    		nn=iMoneyFen;
    		break;
    	case Config.NN_DOUBLE:
    		nn=iMoneyFen+iMoneyJao;
    		break;
    	case Config.NN_THREE:
    		nn=iMoneyFen+iMoneyJao+iMoneyYuan;
    		break;
    	case Config.NN_HT:
    		nn=iMoneyFen+iMoneyYuan;
    		break;
    	case Config.NN_PG:
    		nn=iMoneyFen+iMoneyJao;
    		break;
    	}
		if(nn>6){
			sShow="��ϲ������ţ"+nn+",ţţ͸�ӳɹ���";
		}
		if(nn==0&&(iNnWangFa!=Config.NN_PG)){
			sShow="��ϲ������ţţ,ţţ͸�ӳɹ���";
		}
		if(nn>0&&nn<7){
			if(bReg)
				sShow="";
			else
				sShow="����ţ"+nn+",���ð治���ܱ���͸�ӷ����빺�����棡";
		}
		if(!sShow.equals("")){
    		Toast.makeText(context,sShow, Toast.LENGTH_LONG).show();
    		speaker.speak(sShow);
		}
    }

}
