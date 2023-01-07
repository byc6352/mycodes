/**
 * 
 */
package com.byc.smslocation;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.CoordinateConverter.CoordType;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;



/**
 * @author byc
 *
 */
public class MyLocationMap {
	public static final String TAG = "byc001";//���Ա�ʶ��
	public static final int LOCATION_INTERVAL_TIME=2000;//������λʱ������
	private static MyLocationMap current;
	private MapView mMapView = null;
	private AMap aMap=null;
	private MyLocationStyle myLocationStyle=null;
	private UiSettings mUiSettings=null;//����һ��UiSettings����
	public double mX=0;
	public double mY=0;
	public LatLng mMapXY;
	public String mProvider=null;
    private static double a = 6378245.0;  
    private static double ee = 0.00669342162296594323;  
    private CoordinateConverter mConverter=null;
    public static synchronized MyLocationMap getMyLocationMap(Context context,MapView mapView,Bundle savedInstanceState) {
        if(current == null) {
            current = new MyLocationMap(context,mapView,savedInstanceState);
        }
        return current;
    }
    /*
     * ��ȡ��ͼ�ؼ�����
     */
    public MyLocationMap(Context context,MapView mapView,Bundle savedInstanceState) {
        //��ȡ��ͼ�ؼ�����
        mMapView = mapView;
        mapView.onCreate(savedInstanceState);// �˷����븲д���������Ҫ�ںܶ�����±����ͼ���Ƶĵ�ǰ״̬��
      //��ʼ����ͼ����������
        if (aMap == null) {
            aMap = mapView.getMap();        
        }
        aMap.showIndoorMap(true);     //true����ʾ���ڵ�ͼ��false������ʾ��
        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// �������ǵ�ͼģʽ��aMap�ǵ�ͼ����������
        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);//ҹ����ͼ��aMap�ǵ�ͼ����������
        aMap.setTrafficEnabled(true);//��ʾʵʱ·��ͼ�㣬aMap�ǵ�ͼ����������
        SetUiControl() ;
        SetLocationStyle(true);//���ö�λ���㣻
        mConverter  = new CoordinateConverter(context); 
        // CoordType.GPS ��ת����������
        mConverter.from(CoordType.GPS); 
        
    }
    /*
     * ��activityִ��onCreateʱִ��mMapView.onCreate(savedInstanceState)��������ͼ
     */
    public void onCreate(Bundle savedInstanceState) {
    	mMapView.onCreate(savedInstanceState);
    }
    /*
     * ��activityִ��onDestroyʱִ��mMapView.onDestroy()�����ٵ�ͼ
     */
    public void onDestroy() {
    	mMapView.onDestroy();
    	mUiSettings=null;
    	mConverter=null;
    	 myLocationStyle=null;
    	aMap=null;
    	mMapView = null;
    	current=null;
    }
    /*
     * ��activityִ��onResumeʱִ��mMapView.onResume ()�����»��Ƽ��ص�ͼ
     */
    public void onResume() {
    	mMapView.onResume();
    }
    /*
     * ��activityִ��onPauseʱִ��mMapView.onPause ()����ͣ��ͼ�Ļ���
     */
    public void onPause() {
    	mMapView.onPause();
    	
    }
    /*
     * ��activityִ��onSaveInstanceStateʱִ��mMapView.onSaveInstanceState (outState)�������ͼ��ǰ��״̬
     */
    public void onSaveInstanceState(Bundle outState) {
    	mMapView.onSaveInstanceState(outState);
    }
    /*
     * ���ö�λ���㣺
     */
    public void SetLocationStyle(boolean bLocationSingle) {
        //��ʼ����ͼ����������
        if (myLocationStyle == null) {
        	myLocationStyle = new MyLocationStyle();//��ʼ����λ������ʽ��       
        }
        if(bLocationSingle){//���ζ�λ��
        	myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//��λһ�Σ��ҽ��ӽ��ƶ�����ͼ���ĵ㡣
        }else{//������λ ��
        	myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//������λ���ҽ��ӽ��ƶ�����ͼ���ĵ㣬��λ�������豸������ת�����һ�����豸�ƶ�����1��1�ζ�λ��Ĭ��ִ�д���ģʽ��
        	myLocationStyle.interval(LOCATION_INTERVAL_TIME); //����������λģʽ�µĶ�λ�����ֻ��������λģʽ����Ч�����ζ�λģʽ�²�����Ч����λΪ���롣
        }
        aMap.setMyLocationStyle(myLocationStyle);//���ö�λ�����Style
        aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ������ʾ��λ���㣬false��ʾ���ض�λ���㲢�����ж�λ��Ĭ����false��
    }
    /*
     * ���ÿؼ���ָ���ڵ�ͼͼ���ϵ�һϵ�����ڲ�����ͼ��������������Ű�ť��ָ���롢��λ��ť�������ߵȡ�
     */
    public void SetUiControl() {
        //��ʼ����ͼ����������
        if (mUiSettings == null) {
        	mUiSettings = aMap.getUiSettings();//ʵ����UiSettings�����    
        } 
        mUiSettings.setZoomControlsEnabled(true);//���Ű�ť���ṩ�� App ���û����Ƶ�ͼ���ż���Ľ�����ť
        mUiSettings.setCompassEnabled(true);//ָ���������� App ���û�չʾ��ͼ����Ĭ�ϲ���ʾ��
        mUiSettings.setMyLocationButtonEnabled(true); //��ʾĬ�ϵĶ�λ��ť
        mUiSettings.setScaleControlsEnabled(true);//���Ʊ����߿ؼ��Ƿ���ʾ
        //setLogoPosition(int position);//����logoλ�� �ߵµ�ͼ�� logo Ĭ�������½���ʾ���������Ƴ�����֧�ֵ������̶�λ��
    }
    /*
     * �ı��ͼ�����ĵ�
     */
    public void SetCenterPosition(double dX,double dY) {
    	//���������ǣ��ӽǵ�����������ĵ����ꡢϣ�������������ż��𡢸�����0��~45�㣨��ֱ���ͼʱΪ0����ƫ���� 0~360�� (������Ϊ0) LatLng(39.977290,116.337000)
    	CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(dX,dY),18,30,0));
    	//AMap�����ṩ��ֱ���ƶ���ȥ�������ƶ����̶���
    	aMap.moveCamera(mCameraUpdate);
    	//����ϣ��չʾ�ĵ�ͼ���ż����ͼ�����ż���һ����Ϊ 17 ������ 3 �� 19������Խ��չʾ��ͼ����ϢԽ��ϸ��
    	//CameraUpdate mCameraUpdate �� CameraUpdateFactory.zoomTo(17);
    }
    /*
     * �ı��ͼ�����ĵ�
     */
    public void SetCenterPosition(LatLng latLng ) {
    	//���������ǣ��ӽǵ�����������ĵ����ꡢϣ�������������ż��𡢸�����0��~45�㣨��ֱ���ͼʱΪ0����ƫ���� 0~360�� (������Ϊ0) LatLng(39.977290,116.337000)
    	CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,18,30,0));
    	//AMap�����ṩ��ֱ���ƶ���ȥ�������ƶ����̶���
    	aMap.moveCamera(mCameraUpdate);
    	//����ϣ��չʾ�ĵ�ͼ���ż����ͼ�����ż���һ����Ϊ 17 ������ 3 �� 19������Խ��չʾ��ͼ����ϢԽ��ϸ��
    	//CameraUpdate mCameraUpdate �� CameraUpdateFactory.zoomTo(17);
    }
    /*
     *���Ƶ���
     */
    public void SetMarker(double dX,double dY) {
    	//LatLng latLng = new LatLng(39.906901,116.397972);
    	LatLng latLng = new LatLng(dX,dY);
    	//final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("����").snippet("DefaultMarker"));
    	MarkerOptions markerOption = new MarkerOptions();
    	markerOption.position(latLng);
    	markerOption.title("������").snippet("�����У�34.341568, 108.940174");
    	markerOption.draggable(false);//����Marker���϶�
    	//markerOption.setFlat(true);//����markerƽ����ͼЧ��
    	markerOption.visible(true);
    	//markerOption.alpha(arg0);���͸����
    	//markerOption.anchor(arg0, arg1);//���ǵ�ê��
    	
    	final Marker marker = aMap.addMarker(markerOption);
    	//SDK �ṩ�˸� Marker ���ö����ķ���
    	///Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
    	//long duration = 1000L;
    	//animation.setDuration(duration);
    	//animation.setInterpolator(new LinearInterpolator());
    	//marker.setAnimation(animation);
    	//marker.startAnimation();
    }
    /*
     *���Ƶ���
     */
    public void SetMarker(LatLng latLng) {
    	//final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("����").snippet("DefaultMarker"));
    	MarkerOptions markerOption = new MarkerOptions();
    	markerOption.position(latLng);
    	markerOption.title("������").snippet("�����У�34.341568, 108.940174");
    	markerOption.draggable(false);//����Marker���϶�
    	//markerOption.setFlat(true);//����markerƽ����ͼЧ��
    	markerOption.visible(true);
    	//markerOption.alpha(arg0);���͸����
    	//markerOption.anchor(arg0, arg1);//���ǵ�ê��
    	
    	final Marker marker = aMap.addMarker(markerOption);

    }
    /*
     *GPSת�ߵµ�ͼ���꣺
     */
    public LatLng GPS2GD(LatLng latLng) {
    	// sourceLatLng��ת������� LatLng����
    	mConverter.coord(latLng); 
    	// ִ��ת������
    	LatLng desLatLng = mConverter.convert();
    	return desLatLng;
    }
    /** 
     * �ֻ�GPS����ת�������� 
     * 
     * @param wgLoc 
     * @return 
     */  
    public static LatLng transformFromWGSToGCJ(LatLng wgLoc) {  
  
        //����ڹ��⣬��Ĭ�ϲ�����ת��  
        if (outOfChina(wgLoc.latitude, wgLoc.longitude)) {  
            return new LatLng(wgLoc.latitude, wgLoc.longitude);  
        }  
        double dLat = transformLat(wgLoc.longitude - 105.0,  
                wgLoc.latitude - 35.0);  
        double dLon = transformLon(wgLoc.longitude - 105.0,  
                wgLoc.latitude - 35.0);  
        double radLat = wgLoc.latitude / 180.0 * Math.PI;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);  
  
        return new LatLng(wgLoc.latitude + dLat, wgLoc.longitude + dLon);  
    }  
  
    public static double transformLat(double x, double y) {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y  
                + 0.2 * Math.sqrt(x > 0 ? x : -x);  
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x  
                * Math.PI)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0  
                * Math.PI)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y  
                * Math.PI / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }  
  
    public static double transformLon(double x, double y) {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1  
                * Math.sqrt(x > 0 ? x : -x);  
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x  
                * Math.PI)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0  
                * Math.PI)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x  
                / 30.0 * Math.PI)) * 2.0 / 3.0;  
        return ret;  
    }  
  
    public static boolean outOfChina(double lat, double lon) {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }  
    /*
     *�����������ݣ�
     */
    public boolean  ParseSmsContent(String smsContent) {
    	int  i=smsContent.indexOf("(");
    	Log.d(TAG, "i:"+i);
    	if(i<0) return false;
    	mProvider=smsContent.substring(0,i);
    	Log.d(TAG, "mProvider:"+mProvider);
    	int j=smsContent.indexOf(",");
    	Log.d(TAG, "j:"+j);
    	if(j<0) return false;
    	String sX=smsContent.substring(i+1,j);
    	Log.d(TAG, "sX:"+sX);
    	int k=smsContent.indexOf(")");
    	Log.d(TAG, "k:"+k);
    	if(k<0) return false;
    	String sY=smsContent.substring(j+1,k);
    	Log.d(TAG, "sY:"+sY);
    	if(sX==null||sY==null)return false;
    	try{
    		mX=Double.parseDouble(sX);
    		mY=Double.parseDouble(sY);
    		LatLng latLng=new LatLng(mY,mX);
    		mMapXY=GPS2GD(latLng);
    		//mMapXY=transformFromWGSToGCJ(latLng);
    		Log.d(TAG, "XY:"+mMapXY.longitude+","+mMapXY.latitude);
    		return true;
    	}catch(NumberFormatException e){
    		e.printStackTrace();
    	}
    	return false;
    }
}
