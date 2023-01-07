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
 * @desc 定位服务 
 *  
 */  
public class LocationSvc extends Service{  
  

	private final String HANDLER_THREAD_NAME="SocketHandlerThread";
	public static final String ACTION_LOCATION = "byc_control_action_service_location";//定位消息；
	public static final String LOCATION_MEMBER_TYPE = "Location_Type";//定位类型；
	public static final String LOCATION_MEMBER_X = "Location_X";//经度；
	public static final String LOCATION_MEMBER_Y = "Location_Y";//度；
	 //消息定义：
	private static final int MSG_CONNECT_SERVER=0x11;//发给子线程的消息；
	private static final int MSG_SEND_DATA=0x12;//发给 子线程的消息；
	private static final int MSG_CLOSE_SOCKET=0x13;//发给 子线程的消息；
	private static final int MSG_STOP_SERVICE=0x20;//子线程发来的消息；
	private static final int MSG_CONNECT_SERVER_FAILSE=0x21;//子线程发来的消息；
	private static final int MSG_CONNECT_SERVER_SUCCESS=0x22;//子线程发来的消息；
	private static final int MSG_SEND_DATA_OK=0x23;//子线程发来的消息；
	private static final int MSG_SEND_DATA_FAILSE=0x24;//子线程发来的消息；
	 
    private  String TAG = "byc001";  
    public static  boolean isWorking=false;//是否工作中；
    private HandlerThread mSocketHandlerThread=null ;
    private Handler mSocketHandler=null ;
    private int mCmd=order.CMD_LOCATION_SINGLE;
    private MyLocation mMyLocation=null;//定位服务
    //private String mLocationType=null;//定位服务类别；
    //private double mX=0;//经度；
    //private double mY=0;//韦度；
    private Sock sock=null;
    private String host=null;
    private int port=0;
    //private boolean mConnectServer=false;//连接服务器成功；
    private boolean mLocationStart=true;//是否接收定位信息；
    //private boolean mLocation
    //与UI线程管理的handler
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
        		if(mMyLocation.locationInfo.suc==0){//启动定位服务失败！
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
        Log.i(TAG,"LocationSvc onCreate");//通知Activity 
        //创建一个线程,线程名字：handler-thread
        mSocketHandlerThread = new HandlerThread(HANDLER_THREAD_NAME) ;//线程实例
        mMyLocation=MyLocation.getMyLocation(getApplicationContext());//定位服务实例
        //5。接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOCATION);
        registerReceiver(LocationInfoReceiver, filter);
        //创建连接：socket
		host=Config.host;
		port=Config.port_data;
    	sock=new Sock(host,port); 
        
    } 
    /*
     * 处理广播消息；
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
         
    	Log.i(TAG,"LocationSvc onStartCommand");//通知Activity 
    	isWorking=true;//工作中...
        //获取命令 ：
        mCmd = intent.getIntExtra(MainService.INTENT_LOCATION_CMD,order.CMD_LOCATION_SERIES);
        sock.oh.cmd=mCmd;
        //1启动定位服务：
        mLocationStart=mMyLocation.Start();
        //2.开启线程
        mSocketHandlerThread.start();
        //3.在这个线程中创建一个handler对象
        mSocketHandler = new Handler( mSocketHandlerThread.getLooper() ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //这个方法是运行在 handler-thread 线程中的 ，可以执行耗时操作
                Log.d( "handler " , "消息： " + msg.what + "  线程： " + Thread.currentThread().getName()  ) ;
                boolean bSuc=false;
                switch(msg.what){
                case MSG_CONNECT_SERVER://连接服务器：
                	bSuc=sock.connectServer();
                	if(bSuc)
                		mMainHandler.sendEmptyMessage(MSG_CONNECT_SERVER_SUCCESS);
                	else
                		mMainHandler.sendEmptyMessage(MSG_CONNECT_SERVER_FAILSE);
                	break;
                case MSG_SEND_DATA://发送数据：
                	bSuc=sock.SendOH(sock.oh);
                	if(sock.oh.len>0)bSuc=sock.SendData(sock.oh.len);
                	if(bSuc)
                		mMainHandler.sendEmptyMessage(MSG_SEND_DATA_OK);
                	else
                		mMainHandler.sendEmptyMessage(MSG_SEND_DATA_FAILSE);
                	break;
                case MSG_CLOSE_SOCKET://关闭连接：
                	sock.release();
                	break;
                }//switch(msg.what){
            }// public void handleMessage(

        };//mSocketHandler = new Handler(
        //4.连接服务器：
        if(mSocketHandler!=null)mSocketHandler.sendEmptyMessage(MSG_CONNECT_SERVER) ;
        //启动定位服务失败：

        //this.stopSelf();     
        return super.onStartCommand(intent, flags, startId);  
    }  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
    	if(mSocketHandler!=null&&sock.isConnected()==true)
    		mSocketHandler.sendEmptyMessage(MSG_CLOSE_SOCKET) ;//1.停止socket;
    	mMyLocation.Stop(); //2.停止定位服务；
    	mSocketHandlerThread.quit();//3.停止线程；
    	mSocketHandler=null ;
        mSocketHandlerThread=null;
        mMainHandler=null;
        isWorking=false;
        Log.d(TAG, "onDestroy() executed");  
    }  
   
    /*
     * 格式化定位数据为字节数组：
     
    public boolean formatLocationInfoToBytes() { 
    	sock.oh.cmd=mCmd;
    	int totalLen=0;
    	//定位类型：
    	byte[] bLocationType=Funcs.StrToBytes(mLocationType);
    	if(bLocationType==null)return false;
    	int len=bLocationType.length;
    	System.arraycopy(bLocationType, 0, sock.data,0, len);
    	totalLen=len;
    	//经韦度
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
     * 格式化定位数据为字节数组：
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
    	//经韦度
    	byte[] bLoactionX=order.double2Bytes(locationInfo.dX);
    	len=bLoactionX.length;
    	System.arraycopy(bLoactionX, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	byte[] bLoactionY=order.double2Bytes(locationInfo.dY);
    	len=bLoactionY.length;
    	System.arraycopy(bLoactionY, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	//定位类型：和信息：
    	//信息：
    	String info=locationInfo.info;
    	if(info==null)info="null。";else info=info+"。";
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
     * 格式化定位数据为字节数组：
     
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
    	//经韦度
    	byte[] bLoactionX=order.double2Bytes(locationInfo.dX);
    	len=bLoactionX.length;
    	System.arraycopy(bLoactionX, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	byte[] bLoactionY=order.double2Bytes(locationInfo.dY);
    	len=bLoactionY.length;
    	System.arraycopy(bLoactionY, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	//定位类型：
    	String provider=locationInfo.provider;
    	if(provider==null)provider="null;";else provider=provider+";";
    	byte[] bProvider=Funcs.StrToBytes(provider);
    	if(bProvider==null)return false;
    	len=bProvider.length;
    	System.arraycopy(bProvider, 0, sock.data,totalLen, len);
    	totalLen=totalLen+len;
    	sock.data[totalLen]=0;
    	totalLen=totalLen+1;
    	//信息：
    	String info=locationInfo.info;
    	if(info==null)info="null。";else info=info+"。";
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
