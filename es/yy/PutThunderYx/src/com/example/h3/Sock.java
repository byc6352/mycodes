/**
 * 
 */
package com.example.h3;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream; 
import java.io.InputStream; 
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetSocketAddress;

import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.os.Bundle;
import android.accessibilityservice.AccessibilityService;

/**
 * @author byc
 *
 */
public class Sock {
	//�����ʶ
	private String TAG = "byc001";
	private static Sock current;
	private Context context;
	private String host = "101.200.200.78";
	private int port = 8188;
    private Socket socket = null;
    //private BufferedReader in = null;
    //private PrintWriter out = null;
    private byte buffer[] = {0,0,0,0,0,0,0};
    //ע��ṹ��
    //00δע�᣻0001����ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //01��ע�᣻8760ʹ��ʱ�䣻2016-01-01 12��00��00��ʼʱ�䣻0003���ô�����0001���ô�����
    //�������ô�����TestNum="0003";ʹ��3�Σ�
    private String appID="ah";//����app��ʶ��ah����ר�ҿ�����
    private String host2 = "192.168.1.108";
    private String mPhoneID="";//�豸ID
    private int mTestTime=0;//-- ����0��Сʱ�� 
    //private int TestNum=0;//--����3�� 
    //ͨѶЭ�飨���ͣ���ggDX4PL5WZFR9M123456789012�������գ�018196
    private String mSendData="";//����ͨѶЭ��
    private String mRecvData="";//����ͨѶЭ��018196
    private MainActivity mainAct;
    public Handler HandlerSock = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 0x11) {  
                String sReg=mRecvData.substring(0,2);
                mTestTime=Integer.parseInt(mRecvData.substring(2,6));
                if(sReg.equals("01")){//��Ȩ�ɹ�
                	getConfig().setREG(true);//1��д����Ȩ�ɹ���Ϣ��
                	getConfig().setTestTime(mTestTime);
                	mainAct.tvRegState.setText("��Ȩ״̬������Ȩ");
                	mainAct.tvRegWarm.setText("");
                	mainAct.etRegCode.setVisibility(View.INVISIBLE);
                	mainAct.tvPlease.setVisibility(View.INVISIBLE);
                	mainAct.btReg.setVisibility(View.INVISIBLE);
                	Toast.makeText(context, "��Ȩ�ɹ���", Toast.LENGTH_LONG).show();
                	Config.bReg=true;
                	
                }else{
                	Toast.makeText(context, "��Ȩ�������Ȩʧ�ܣ�", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "handleMessage:" + mRecvData);
            }  
        }  
  
    };  
	
    private Sock(Context context) {
    	this.context=context;
    	this.mainAct=(MainActivity)context;
    	//1�ж��Ƿ��һ�����У�����ǣ�
    	//�ڴ˳�ʼ����Ȩ�������
    	TAG=Config.TAG;
    	host=getConfig().getHOST();
    	port=getConfig().getPORT();
    	appID=getConfig().getAppID();
    	mPhoneID=getConfig().getPhoneID();
    	mPhoneID=mPhoneID.substring(0, 12);
        //������Ϣע����Ϣ
    	if(Config.DEBUG){
    		//host=host2;
    	}

    }
    public static synchronized Sock getSock(Context context) {
        if(current == null) {
            current = new Sock(context);
        }
        return current;
    }
    public Config getConfig(){
    	return Config.getConfig(context);
    }


    class SockThread extends Thread { 
    	public String mSendStr;
    	 public SockThread(String str) { 
    		 mSendStr=str;
    	 }
    	 @Override  
         public void run() {  
             //������Ϣ  
             Message msg = new Message();  
             msg.what = 0x11;
             Bundle bundle = new Bundle();
             bundle.clear(); 
             try{
                 //���ӷ����� ���������ӳ�ʱΪ5��  
            	 Log.d(TAG, "SOCKET Start:-------------------------->");
                 socket = new Socket();  
                 socket.connect(new InetSocketAddress(host, port), 5000); 
                 //��ȡ���������  
                 OutputStream out = socket.getOutputStream();
                 InputStream in=socket.getInputStream();

                 //��������
                 if (socket.isConnected()) {
                     if (!socket.isOutputShutdown()) {
                         out.write(mSendStr.getBytes("gbk"));  
                         out.flush(); 
                         Log.d(TAG, "SOCKET Send:"+mSendStr);
                     }

                 }//if (socket.isConnected()) {
                 //��������
                 //��ȡ������������Ϣ  
                 Log.d(TAG, "SOCKET Recv:---------------------------------->");
                 in.read(buffer);
                 mRecvData=new String(buffer);
                 Log.d(TAG, "SOCKET Recv:"+mRecvData);
                 bundle.putString("msg", mRecvData);  
                 msg.setData(bundle);  //
                 //������Ϣ �޸�UI�߳��е����  
                 HandlerSock.sendMessage(msg);  
                 //�رո������������  
                 in.close();  
                 out.close();  
                 socket.close();  
             } catch (IOException ex) {
                 ex.printStackTrace();
                 Log.d(TAG, "SOCKET ERROR:"+ex.getMessage());
                 Toast.makeText(context,"login exception:" + ex.getMessage(), Toast.LENGTH_LONG).show();
                 //return false;
             }//try{
    	 }
    }//class SockThread extends Thread { 
    public void RegStart(String regCode,MainActivity mainAct){
    	this.mainAct=mainAct;
    	mSendData=appID+mPhoneID+regCode;
    	new SockThread(mSendData).start();
    	return ;
    }
}
