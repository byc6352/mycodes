/**
 * 
 */
package com.byc.ctL;



import android.app.Service;  
import android.content.Intent;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;  
import android.os.IBinder;  
import android.util.Log;  
import android.widget.Toast;  

/**
 * @author byc
 *
 */
public class MyService extends Service implements LocationListener {  
	  
    public static final String TAG = "byc001";  
    //private static final String TAG = "LocationSvc";  
    private LocationManager locationManager;  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        Log.d(TAG, "onCreate() executed");  
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);  
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
    	//
    	//WorkStart();
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) locationManager  
        .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,  
                this);  
        else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) locationManager  
        .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  
                this);  
        else Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();  
        Log.d(TAG, "onStartCommand() executed Close");  
        
        return super.onStartCommand(intent, flags, startId);  
    }  
      
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        Log.d(TAG, "onDestroy() executed");  
    }  
  
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    } 
    @Override  
    public void onLocationChanged(Location location) {  
        //Log.d(TAG, "Get the current position \n" + location);  
        //位置信息变化时触发  
        Log.i(TAG, "纬度："+location.getLatitude());  
        Log.i(TAG, "经度："+location.getLongitude());  
        Log.i(TAG, "海拔："+location.getAltitude());  
        Log.i(TAG, "时间："+location.getTime());  
        //通知Activity  
        //Intent intent = new Intent();  
        //intent.setAction(Common.LOCATION_ACTION);  
        //intent.putExtra(Common.LOCATION, location.toString());  
        //sendBroadcast(intent);  
  
        // 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。  
        //locationManager.removeUpdates(this);  
        //stopSelf();  
    }  
  
    @Override  
    public void onProviderDisabled(String provider) {  
    }  
  
    @Override  
    public void onProviderEnabled(String provider) {  
    }  
  
    @Override  
    public void onStatusChanged(String provider, int status, Bundle extras) {  
    }  
    
    class WorkThread extends Thread { 
    	 @Override  
         public void run() {  
    			for(int i=0;i<10000;i++){
    	    		  try{
    	    	    	  Thread.sleep(1000);
    	    	    }catch(Exception e){
    	    	    }
    	    		Log.d(TAG, "onStartCommand() executed");  
    	    	}
    	 }
    }
    public void WorkStart(){
    	new WorkThread().start();
    	return ;
    }

}
