/**
 * 
 */
package com.byc.ctL;

/**
 * @author byc
 *
 */
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.Iterator;  
import java.util.List;  
  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.json.JSONArray;  
import org.json.JSONObject;  
  
import android.annotation.SuppressLint;  
import android.annotation.TargetApi;  
import android.app.Activity;  
import android.app.AlertDialog;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;  
import android.location.GpsSatellite;  
import android.location.GpsStatus;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.net.wifi.WifiManager;  
import android.os.Build;  
import android.os.Bundle;  
import android.provider.Settings;
import android.util.Log;  
  

/** 
 * Android API�����ṩ�Ķ�λ����,Ҳ��GPS��λ�� 
 * GPS��λ���ǻ������Ƕ�λ�����ܻ���Ӱ��ܴ󡣲����ǵ���λ��Ҳ����ֻ�����Լ�֪����ĵ������ꡣ 
 * @author LuoSiye 
 */  
@TargetApi(Build.VERSION_CODES.CUPCAKE)  
@SuppressLint("NewApi")  
public class LocationUtil{  
  
    private static final String TAG = "LocationUtil";  
      
    private static LocationUtil instance;  
    private static Activity mActivity;  
    private static LocationManager locationManager;  
    private static LocationListener locationListener;  
      
    public static LocationUtil getInstance(Activity activity){  
        mActivity = activity;  
        if(instance==null){  
            instance = new LocationUtil();  
        }  
        locationManager = (LocationManager)mActivity.getSystemService(Context.LOCATION_SERVICE);  
        return instance;  
    }  
      
    /** 
     * �ж�GPS�����Ƿ��. 
     * false��������ʾ��,����������ں�̨ǿ�п����ķ�ʽ�� 
     * true:�����κδ��� 
     * @return 
     */  
    public static void isOpenGPS(){  
          
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){  
            AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);  
            dialog.setMessage("GPSδ�򿪣��Ƿ��?");  
            dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
                  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
                    // ������ɺ󷵻ص�ԭ���Ľ���  
                    mActivity.startActivityForResult(intent, 0);   
                }  
            });  
            dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
                  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    dialog.dismiss();  
                }  
            });  
            dialog.show();  
        }  
    }  
  
    /** 
     * ͨ��LocationListener����ȡLocation��Ϣ 
     */  
    public static void fromListenerGetLocation(){  
          
        locationListener = new LocationListener() {  
  
            @Override  
            public void onLocationChanged(Location location) {  
                //λ����Ϣ�仯ʱ����  
                Log.i(TAG, "γ�ȣ�"+location.getLatitude());  
                Log.i(TAG, "���ȣ�"+location.getLongitude());  
                Log.i(TAG, "���Σ�"+location.getAltitude());  
                Log.i(TAG, "ʱ�䣺"+location.getTime());  
            }  
  
            @Override  
            public void onStatusChanged(String provider, int status,Bundle extras) {  
                //GPS״̬�仯ʱ����  
            }  
  
            @Override  
            public void onProviderEnabled(String provider) {  
                //GPS����ʱ����  
            }  
  
            @Override  
            public void onProviderDisabled(String provider) {  
                //GPS����ʱ����   
            }  
        };  
        /** 
         * �󶨼��� 
         * ����1���豸����GPS_PROVIDER��NETWORK_PROVIDER���֣�ǰ����GPS,������GPRS�Լ�WIFI��λ 
         * ����2��λ����Ϣ��������.��λ�Ǻ��� 
         * ����3��λ�ñ仯��С���룺��λ�þ���仯������ֵʱ��������λ����Ϣ 
         * ����4������ 
         * ��ע������2��3���������3��Ϊ0�����Բ���3Ϊ׼������3Ϊ0����ͨ��ʱ������ʱ���£�����Ϊ0������ʱˢ�� 
         */  
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);  
    }  
      
    /** 
     * ������ȡLocation��ͨ�����·�����ȡ���������һ�ζ�λ��Ϣ�� 
     * ע�⣺Location location=new Location(LocationManager.GPS_PROVIDER)��ʽ��ȡ��location�ĸ�������ֵ����Ϊ0�� 
     */  
    public static void getLocation(){  
          
        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        Log.i(TAG, "γ�ȣ�"+location.getLatitude());  
        Log.i(TAG, "���ȣ�"+location.getLongitude());  
        Log.i(TAG, "���Σ�"+location.getAltitude());  
        Log.i(TAG, "ʱ�䣺"+location.getTime());  
          
    }  
      
    /** 
     * ��ȡGPS״̬����������GPS������ֹͣ����һ�ζ�λ�����Ǳ仯���¼��� 
     */  
    public static void getStatusListener(){  
          
        GpsStatus.Listener listener = new GpsStatus.Listener(){  
  
            @Override  
            public void onGpsStatusChanged(int event) {  
                if(event==GpsStatus.GPS_EVENT_FIRST_FIX){  
                    //��һ�ζ�λ  
                }else if(event==GpsStatus.GPS_EVENT_SATELLITE_STATUS){  
                    //����״̬�ı�  
                    GpsStatus gpsStauts= locationManager.getGpsStatus(null); // ȡ��ǰ״̬  
                    int maxSatellites = gpsStauts.getMaxSatellites(); //��ȡ���ǿ�����Ĭ�����ֵ  
                    Iterator<GpsSatellite> it = gpsStauts.getSatellites().iterator();//����һ��������������������  
                    int count = 0;  
                    while (it.hasNext() && count <= maxSatellites) {      
                        count++;     
                        GpsSatellite s = it.next();    
                    }  
                    Log.i(TAG, "��������"+count+"������");  
                }else if(event==GpsStatus.GPS_EVENT_STARTED){  
                    //��λ����  
                }else if(event==GpsStatus.GPS_EVENT_STOPPED){  
                    //��λ����  
                }  
            }  
        };  
        //��  
        locationManager.addGpsStatusListener(listener);   
    }  
      
    /** 
     * ��ȡ��������״̬ 
     * @return 
     */  
    public static List<GpsSatellite> getGpsStatus(){  
        List<GpsSatellite> result = new ArrayList<GpsSatellite>();  
        GpsStatus gpsStatus = locationManager.getGpsStatus(null); // ȡ��ǰ״̬  
        //��ȡĬ�����������  
        int maxSatellites = gpsStatus.getMaxSatellites();  
        //��ȡ��һ�ζ�λʱ�䣨��������һ�ζ�λ��  
        int costTime=gpsStatus.getTimeToFirstFix();  
        Log.i(TAG, "��һ�ζ�λʱ��:"+costTime);  
        //��ȡ����  
        Iterable<GpsSatellite> iterable=gpsStatus.getSatellites();  
        //һ���ٴ�ת����Iterator  
        Iterator<GpsSatellite> itrator=iterable.iterator();  
        int count = 0;  
        while (itrator.hasNext() && count <= maxSatellites){  
            count++;  
            GpsSatellite s = itrator.next();  
            result.add(s);  
        }  
        return result;  
    }  
      
    /** 
     * ĳһ�����ǵ���Ϣ. 
     * @param gpssatellite 
     */  
    public static void getGpsStatelliteInfo(GpsSatellite gpssatellite){  
          
        //���ǵķ�λ�ǣ�����������    
    	Log.i(TAG, "���ǵķ�λ�ǣ�"+gpssatellite.getAzimuth());  
        //���ǵĸ߶ȣ�����������    
    	Log.i(TAG, "���ǵĸ߶ȣ�"+gpssatellite.getElevation());  
        //���ǵ�α��������룬��������    
    	Log.i(TAG, "���ǵ�α��������룺"+gpssatellite.getPrn());  
        //���ǵ�����ȣ�����������    
    	Log.i(TAG, "���ǵ�����ȣ�"+gpssatellite.getSnr());  
        //�����Ƿ�������������������    
    	Log.i(TAG, "�����Ƿ���������"+gpssatellite.hasAlmanac());  
        //�����Ƿ�������������������    
    	Log.i(TAG, "�����Ƿ���������"+gpssatellite.hasEphemeris());  
        //�����Ƿ����ڽ��ڵ�GPS��������    
    	Log.i(TAG, "�����Ƿ����ڽ��ڵ�GPS�������㣺"+gpssatellite.hasAlmanac());  
    }  
      
    /** 
     * ͨ��WIFI��ȡ��λ��Ϣ 
     */  
    public static void fromWIFILocation(){  
        //WIFI��MAC��ַ  
        WifiManager manager = (WifiManager)mActivity.getSystemService(Context.WIFI_SERVICE);   
        String wifiAddress = manager.getConnectionInfo().getBSSID();   
        //����WIFI��Ϣ��λ  
        DefaultHttpClient client = new DefaultHttpClient();   
        HttpPost post = new HttpPost("http://www.google.com/loc/json");   
        JSONObject holder = new JSONObject();  
        try {  
            holder.put("version" , "1.1.0");  
            holder.put( "host" , "maps.google.com");   
            JSONObject data;                 
            JSONArray array = new JSONArray();  
            if(wifiAddress!=null&&!wifiAddress.equals("")){  
                data = new JSONObject();   
                data.put("mac_address", wifiAddress);   
                data.put("signal_strength", 8);   
                data.put("age", 0);   
                array.put(data);   
            }  
            holder.put("wifi_towers",array);   
            StringEntity se = new StringEntity(holder.toString());   
            post.setEntity(se);  
            HttpResponse resp =client.execute(post);   
            int state =resp.getStatusLine().getStatusCode();   
            if (state == HttpStatus.SC_OK) {  
                HttpEntity entity =resp.getEntity();   
                if (entity != null) {  
                    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));  
                    StringBuffer sb = new StringBuffer();   
                    String resute = "";  
                    while ((resute =br.readLine()) != null) {                       
                        sb.append(resute);                     
                    }                     
                    br.close();   
                    data = new JSONObject(sb.toString());   
                    data = (JSONObject)data.get("location");   
                    Location loc = new Location(LocationManager.NETWORK_PROVIDER);  
                    loc.setLatitude((Double)data.get("latitude"));                     
                    loc.setLongitude((Double)data.get("longitude"));  
                    //������Ϣͬ����ȡ����  
                    Log.i(TAG, "latitude ===== "+(Double)data.get("latitude"));  
                    Log.i(TAG, "longitude ===== "+(Double)data.get("longitude"));  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
    }  
}  
