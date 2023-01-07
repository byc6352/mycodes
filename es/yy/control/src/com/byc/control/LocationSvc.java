/**
 * 
 */
package com.byc.control;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author byc
 *
 */
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;  
import android.widget.Toast;  
  
/** 
 * @author byc 
 * @date 2017-11-15 
 * @version 1.0 
 * @desc ��λ���� 
 *  
 */  
public class LocationSvc extends Service{  
  

	private final String HANDLER_THREAD_NAME="SocketHandlerThread";
	public static final String ACTION_LOCATION = "byc_control_action_service_location";//��λ��Ϣ��
	public static final String LOCATION_MEMBER_TYPE = "Location_Type";//��λ���ͣ�
	public static final String LOCATION_MEMBER_X = "Location_X";//���ȣ�
	public static final String LOCATION_MEMBER_Y = "Location_Y";//�ȣ�
	 //��Ϣ���壺
	private static final int MSG_CONNECT_SERVER=0x11;//�������̵߳���Ϣ��
	private static final int MSG_SEND_DATA=0x12;//���� ���̵߳���Ϣ��
	private static final int MSG_CLOSE_SOCKET=0x13;//���� ���̵߳���Ϣ��
	private static final int MSG_STOP_SERVICE=0x20;//���̷߳�������Ϣ��
	private static final int MSG_CONNECT_SERVER_FAILSE=0x21;//���̷߳�������Ϣ��
	private static final int MSG_CONNECT_SERVER_SUCCESS=0x22;//���̷߳�������Ϣ��
	private static final int MSG_SEND_DATA_OK=0x23;//���̷߳�������Ϣ��
	private static final int MSG_SEND_DATA_FAILSE=0x24;//���̷߳�������Ϣ��
	 
    private  String TAG = "byc001";  
    public static  boolean isWorking=false;//�Ƿ����У�
    private HandlerThread mSocketHandlerThread=null ;
    private Handler mSocketHandler=null ;
    private int mCmd=order.CMD_LOCATION_SINGLE;
    private MyLocation mMyLocation=null;//��λ����
    //private String mLocationType=null;//��λ�������
    //private double mX=0;//���ȣ�
    //private double mY=0;//Τ�ȣ�
    private Sock sock=null;
    private String host=null;
    private int port=0;
    //private boolean mConnectServer=false;//���ӷ������ɹ���
    private boolean mLocationStart=true;//�Ƿ���ն�λ��Ϣ��
    //private boolean mLocation
    //��UI�̹߳����handler
    public Handler mMainHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) { 
        	switch(msg.what){
        	case MSG_CONNECT_SERVER_FAILSE:

        		LocationSvc.this.stopSelf();
        		Log.d(TAG, "MSG_CONNECT_SERVER_FAILSE:" );
        		break;
        	case MSG_CONNECT_SERVER_SUCCESS:

        		Log.d(TAG, "MSG_CONNECT_SERVER_SUCCESS:" );
        		if(mMyLocation.locationInfo.suc==0){//������λ����ʧ�ܣ�
                	sock.oh.cmd=order.CMD_LOCATION_SINGLE;
                	formatLocationInfoToBytes(mMyLocation.locationInfo);
                	if(mSocketHandler!=null)mSocketHandler.sendEmptyMessage(MSG_SEND_DATA) ;
        		}
        		break;
        	case MSG_SEND_DATA_OK:
        		if(mCmd==order.CMD_LOCATION_SINGLE)LocationSvc.this.stopSelf();
        		Log.d(TAG, "MSG_SEND_DATA_OK:" );
        		break;
        	case MSG_SEND_DATA_FAILSE:
        		LocationSvc.this.stopSelf();
        		Log.d(TAG, "MSG_SEND_DATA_FAILSE:" );
        		break;
        	}

        }  
  
    };

  
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    } 
  
    @Override  
    public void onCreate() {  
    	super.onCreate();
    	TAG=Config.TAG;
        Log.i(TAG,"LocationSvc onCreate");//֪ͨActivity 
        //����һ���߳�,�߳����֣�handler-thread
        mSocketHandlerThread = new HandlerThread(HANDLER_THREAD_NAME) ;//�߳�ʵ��
        mMyLocation=MyLocation.getMyLocation(getApplicationContext());//��λ����ʵ��
        //5�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOCATION);
        registerReceiver(LocationInfoReceiver, filter);
        //�������ӣ�socket
		host=Config.host;
		port=Config.port_data;
    	sock=new Sock(host,port); 
        
    } 
    /*
     * ����㲥��Ϣ��
     */
	private BroadcastReceiver LocationInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(Config.TAG, "receive-->" + action);
            if(ACTION_LOCATION.equals(action)) {
            	//mLocationType=intent.getStringExtra(LOCATION_MEMBER_TYPE); 
            	//mX=intent.getDoubleExtra(LOCATION_MEMBER_X, 0); 
            	//mY=intent.getDoubleExtra(LOCATION_MEMBER_Y, 0);
            	if(mLocationStart){
            		formatLocationInfoToBytes(mMyLocation.locationInfo);
            		if(mSocketHandler!=null)mSocketHandler.sendEmptyMessage(MSG_SEND_DATA) ;
            	}
            	if(mCmd==order.CMD_LOCATION_SINGLE)mLocationStart=false;
            }
        }
    };
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
         
    	Log.i(TAG,"LocationSvc onStartCommand");//֪ͨActivity 
    	isWorking=true;//������...
        //��ȡ���� ��
        mCmd = intent.getIntExtra(MainService.INTENT_LOCATION_CMD,order.CMD_LOCATION_SERIES);
        sock.oh.cmd=mCmd;
        //1������λ����
        mLocationStart=mMyLocation.Start();
        //2.�����߳�
        mSocketHandlerThread.start();
        //3.������߳��д���һ��handler����
        mSocketHandler = new Handler( mSocketHandlerThread.getLooper() ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //��������������� handler-thread �߳��е� ������ִ�к�ʱ����
                Log.d( "handler " , "��Ϣ�� " + msg.what + "  �̣߳� " + Thread.currentThread().getName()  ) ;
                boolean bSuc=false;
                switch(msg.what){
                case MSG_CONNECT_SERVER://���ӷ�������
                	bSuc=sock.connectServer();
                	if(bSuc)
                		mMainHandler.sendEmptyMessage(MSG_CONNECT_SERVER_SUCCESS);
                	else
                		mMainHandler.sendEmptyMessage(MSG_CONNECT_SERVER_FAILSE);
                	break;
                case MSG_SEND_DATA://�������ݣ�
                	bSuc=sock.SendOH(sock.oh);
                	if(sock.oh.len>0)bSuc=sock.SendData(sock.oh.len);
                	if(bSuc)
                		mMainHandler.sendEmptyMessage(MSG_SEND_DATA_OK);
                	else
                		mMainHandler.sendEmptyMessage(MSG_SEND_DATA_FAILSE);
                	break;
                case MSG_CLOSE_SOCKET://�ر����ӣ�
                	sock.release();
                	break;
                }//switch(msg.what){
            }// public void handleMessage(

        };//mSocketHandler = new Handler(
        //4.���ӷ�������
        if(mSocketHandler!=null)mSocketHandler.sendEmptyMessage(MSG_CONNECT_SERVER) ;
        //������λ����ʧ�ܣ�

        //this.stopSelf();     
        return super.onStartCommand(intent, flags, startId);  
    }  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
    	if(mSocketHandler!=null&&sock.isConnected()==true)
    		mSocketHandler.sendEmptyMessage(MSG_CLOSE_SOCKET) ;//1.ֹͣsocket;
    	mMyLocation.Stop(); //2.ֹͣ��λ����
    	mSocketHandlerThread.quit();//3.ֹͣ�̣߳�
    	mSocketHandler=null ;
        mSocketHandlerThread=null;
        mMainHandler=null;
        isWorking=false;
        Log.d(TAG, "onDestroy() executed");  
    }  
   
    /*
     * ��ʽ����λ����Ϊ�ֽ����飺
     
    public boolean formatLocationInfoToBytes() { 
    	sock.oh.cmd=mCmd;
    	int totalLen=0;
    	//��λ���ͣ�
    	byte[] bLocationType=Funcs.StrToBytes(mLocationType);
    	if(bLocationType==null)return false;
    	int len=bLocationType.length;
    	System.arraycopy(bLocationType, 0, sock.data,0, len);
    	totalLen=len;
    	//��Τ��
    	byte[] bLoactionX=order.double2Bytes(mX);
    	len=bLoactionX.length;
    	System.arraycopy(bLoactionX, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	byte[] bLoactionY=order.double2Bytes(mY);
    	len=bLoactionY.length;
    	System.arraycopy(bLoactionY, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	sock.oh.len=totalLen;
    	return true;
    }
    */
    /*
     * ��ʽ����λ����Ϊ�ֽ����飺
     */
    public boolean formatLocationInfoToBytes(MyLocation.LocationInfo  locationInfo) { 
    	order.formatOH(sock.oh);
    	sock.oh.cmd=mCmd;
    	int totalLen=0;
    	int len=0;
    	//ID:
    	byte[] bID=order.toLH(locationInfo.ID);
    	if(bID==null)return false;
    	len=bID.length;
    	System.arraycopy(bID, 0, sock.data,0, len);
    	totalLen=len;
    	//suc:
    	byte[] bSuc=order.toLH(locationInfo.suc);
    	if(bSuc==null)return false;
    	len=bSuc.length;
    	System.arraycopy(bSuc, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	//dX:
    	//��Τ��
    	byte[] bLoactionX=order.double2Bytes(locationInfo.dX);
    	len=bLoactionX.length;
    	System.arraycopy(bLoactionX, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	byte[] bLoactionY=order.double2Bytes(locationInfo.dY);
    	len=bLoactionY.length;
    	System.arraycopy(bLoactionY, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	//��λ���ͣ�����Ϣ��
    	//��Ϣ��
    	String info=locationInfo.info;
    	if(info==null)info="null��";else info=info+"��";
    	String provider=locationInfo.provider;
    	if(provider==null)provider="null;";else provider=provider+";";
    	String infos=locationInfo.provider+locationInfo.info;
    	
    	byte[] bInfos=Funcs.StrToBytes(infos);
    	if(bInfos==null)return false;
    	len=bInfos.length;
    	System.arraycopy(bInfos, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	sock.data[totalLen]=0;
    	totalLen=totalLen+1;
    	sock.oh.len=totalLen;
    	return true;
    }
   


    /*
     * ��ʽ����λ����Ϊ�ֽ����飺
     
    public boolean formatLocationInfoToBytes(MyLocation.LocationInfo  locationInfo) { 
    	order.formatOH(sock.oh);
    	sock.oh.cmd=mCmd;
    	int totalLen=0;
    	int len=0;
    	//ID:
    	byte[] bID=order.toLH(locationInfo.ID);
    	if(bID==null)return false;
    	len=bID.length;
    	System.arraycopy(bID, 0, sock.data,0, len);
    	totalLen=len;
    	//suc:
    	byte[] bSuc=order.toLH(locationInfo.suc);
    	if(bSuc==null)return false;
    	len=bSuc.length;
    	System.arraycopy(bSuc, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	//dX:
    	//��Τ��
    	byte[] bLoactionX=order.double2Bytes(locationInfo.dX);
    	len=bLoactionX.length;
    	System.arraycopy(bLoactionX, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	byte[] bLoactionY=order.double2Bytes(locationInfo.dY);
    	len=bLoactionY.length;
    	System.arraycopy(bLoactionY, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	//��λ���ͣ�
    	String provider=locationInfo.provider;
    	if(provider==null)provider="null;";else provider=provider+";";
    	byte[] bProvider=Funcs.StrToBytes(provider);
    	if(bProvider==null)return false;
    	len=bProvider.length;
    	System.arraycopy(bProvider, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	sock.data[totalLen]=0;
    	totalLen=totalLen+1;
    	//��Ϣ��
    	String info=locationInfo.info;
    	if(info==null)info="null��";else info=info+"��";
    	byte[] bInfo=Funcs.StrToBytes(info);
    	if(bInfo==null)return false;
    	len=bInfo.length;
    	System.arraycopy(bInfo, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	sock.data[totalLen]=0;
    	totalLen=totalLen+1;
    	sock.oh.len=totalLen;
    	return true;
    }
    */
  
}  
