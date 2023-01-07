/**
 * 
 */
package com.byc.smslocated;





import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class MainService  extends Service {
	private String TAG = "byc001";
	private MyLocation myLocation=null;
	public Config config;
	@Override
	public void onCreate() {
		
		myLocation=MyLocation.getMyLocation(getApplicationContext());
		config=Config.getConfig(getApplicationContext());
		//���չ㲥��Ϣ
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(Config.ACTION_UPDATE_INFO);
	    registerReceiver(ServiceReceiver, filter);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onDestroy() { 
		super.onDestroy();
		//orderThread.mOrderLooper.quit();//���������̣߳�
		Log.d(TAG, "DownloadManager onDestroy() executed");
	}
	@Override
	public boolean stopService(Intent name) {
		Log.d(TAG, "DownloadManager stopService() executed");
		return super.stopService(name);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "DownloadManager onStartCommand() executed");
		//mFtp.DownloadStart(Config.FTP_FILE_NAME);//��ʼ����
		myLocation.GetSingleLocation();
        //���͹㲥���Ѿ��Ͽ���������
        Intent intentInfo = new Intent(Config.ACTION_SERVICE_INFO);
        sendBroadcast(intentInfo);
		return super.onStartCommand(intent, flags, startId);
	}
	private BroadcastReceiver ServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(Config.TAG, "receive-->" + action);
            if(Config.ACTION_UPDATE_INFO.equals(action)) {
            	//requestPower();
            	 android.os.Process.killProcess(android.os.Process.myPid());    //��ȡPID 
            	 System.exit(0);   //����java��c#�ı�׼�˳���������ֵΪ0���������˳�
            } 
        }
    };

}
