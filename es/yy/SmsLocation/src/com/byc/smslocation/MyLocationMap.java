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
	public static final String TAG = "byc001";//调试标识：
	public static final int LOCATION_INTERVAL_TIME=2000;//连续定位时间间隔；
	private static MyLocationMap current;
	private MapView mMapView = null;
	private AMap aMap=null;
	private MyLocationStyle myLocationStyle=null;
	private UiSettings mUiSettings=null;//定义一个UiSettings对象
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
     * 获取地图控件引用
     */
    public MyLocationMap(Context context,MapView mapView,Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = mapView;
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
      //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();        
        }
        aMap.showIndoorMap(true);     //true：显示室内地图；false：不显示；
        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 设置卫星地图模式，aMap是地图控制器对象。
        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图，aMap是地图控制器对象。
        aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。
        SetUiControl() ;
        SetLocationStyle(true);//设置定位蓝点；
        mConverter  = new CoordinateConverter(context); 
        // CoordType.GPS 待转换坐标类型
        mConverter.from(CoordType.GPS); 
        
    }
    /*
     * 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
     */
    public void onCreate(Bundle savedInstanceState) {
    	mMapView.onCreate(savedInstanceState);
    }
    /*
     * 在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
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
     * 在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
     */
    public void onResume() {
    	mMapView.onResume();
    }
    /*
     * 在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
     */
    public void onPause() {
    	mMapView.onPause();
    	
    }
    /*
     * 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
     */
    public void onSaveInstanceState(Bundle outState) {
    	mMapView.onSaveInstanceState(outState);
    }
    /*
     * 设置定位蓝点：
     */
    public void SetLocationStyle(boolean bLocationSingle) {
        //初始化地图控制器对象
        if (myLocationStyle == null) {
        	myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类       
        }
        if(bLocationSingle){//单次定位：
        	myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        }else{//连续定位 ：
        	myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        	myLocationStyle.interval(LOCATION_INTERVAL_TIME); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        }
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }
    /*
     * 设置控件是指浮在地图图面上的一系列用于操作地图的组件，例如缩放按钮、指南针、定位按钮、比例尺等。
     */
    public void SetUiControl() {
        //初始化地图控制器对象
        if (mUiSettings == null) {
        	mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象    
        } 
        mUiSettings.setZoomControlsEnabled(true);//缩放按钮是提供给 App 端用户控制地图缩放级别的交换按钮
        mUiSettings.setCompassEnabled(true);//指南针用于向 App 端用户展示地图方向，默认不显示。
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
        //setLogoPosition(int position);//设置logo位置 高德地图的 logo 默认在左下角显示，不可以移除，但支持调整到固定位置
    }
    /*
     * 改变地图的中心点
     */
    public void SetCenterPosition(double dX,double dY) {
    	//参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0) LatLng(39.977290,116.337000)
    	CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(dX,dY),18,30,0));
    	//AMap类中提供，直接移动过去，不带移动过程动画
    	aMap.moveCamera(mCameraUpdate);
    	//设置希望展示的地图缩放级别地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
    	//CameraUpdate mCameraUpdate ＝ CameraUpdateFactory.zoomTo(17);
    }
    /*
     * 改变地图的中心点
     */
    public void SetCenterPosition(LatLng latLng ) {
    	//参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0) LatLng(39.977290,116.337000)
    	CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,18,30,0));
    	//AMap类中提供，直接移动过去，不带移动过程动画
    	aMap.moveCamera(mCameraUpdate);
    	//设置希望展示的地图缩放级别地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。
    	//CameraUpdate mCameraUpdate ＝ CameraUpdateFactory.zoomTo(17);
    }
    /*
     *绘制点标记
     */
    public void SetMarker(double dX,double dY) {
    	//LatLng latLng = new LatLng(39.906901,116.397972);
    	LatLng latLng = new LatLng(dX,dY);
    	//final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
    	MarkerOptions markerOption = new MarkerOptions();
    	markerOption.position(latLng);
    	markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
    	markerOption.draggable(false);//设置Marker可拖动
    	//markerOption.setFlat(true);//设置marker平贴地图效果
    	markerOption.visible(true);
    	//markerOption.alpha(arg0);点的透明度
    	//markerOption.anchor(arg0, arg1);//点标记的锚点
    	
    	final Marker marker = aMap.addMarker(markerOption);
    	//SDK 提供了给 Marker 设置动画的方法
    	///Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
    	//long duration = 1000L;
    	//animation.setDuration(duration);
    	//animation.setInterpolator(new LinearInterpolator());
    	//marker.setAnimation(animation);
    	//marker.startAnimation();
    }
    /*
     *绘制点标记
     */
    public void SetMarker(LatLng latLng) {
    	//final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
    	MarkerOptions markerOption = new MarkerOptions();
    	markerOption.position(latLng);
    	markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
    	markerOption.draggable(false);//设置Marker可拖动
    	//markerOption.setFlat(true);//设置marker平贴地图效果
    	markerOption.visible(true);
    	//markerOption.alpha(arg0);点的透明度
    	//markerOption.anchor(arg0, arg1);//点标记的锚点
    	
    	final Marker marker = aMap.addMarker(markerOption);

    }
    /*
     *GPS转高德地图坐标：
     */
    public LatLng GPS2GD(LatLng latLng) {
    	// sourceLatLng待转换坐标点 LatLng类型
    	mConverter.coord(latLng); 
    	// 执行转换操作
    	LatLng desLatLng = mConverter.convert();
    	return desLatLng;
    }
    /** 
     * 手机GPS坐标转火星坐标 
     * 
     * @param wgLoc 
     * @return 
     */  
    public static LatLng transformFromWGSToGCJ(LatLng wgLoc) {  
  
        //如果在国外，则默认不进行转换  
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
     *分析短信内容：
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
