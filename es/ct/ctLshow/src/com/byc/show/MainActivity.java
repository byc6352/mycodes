package com.byc.show;

import android.support.v7.app.ActionBarActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	MapView mMapView = null;
	AMap aMap=null;
	private UiSettings mUiSettings;//定义一个UiSettings对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//获取地图控件引用
	    mMapView = (MapView) findViewById(R.id.map);
	    //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
	    mMapView.onCreate(savedInstanceState);
	  //初始化地图控制器对象
	    if (aMap == null) {
	        aMap = mMapView.getMap();        
	    }
	       MyLocationStyle myLocationStyle;
	        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
	        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
	        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
	        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
	        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
	        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
	        aMap.showIndoorMap(true);     //true：显示室内地图；false：不显示；
	        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 设置卫星地图模式，aMap是地图控制器对象。
	        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图，aMap是地图控制器对象。
	        //aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。
	        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
	        mUiSettings.setCompassEnabled(true);
	        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
	        LatLng latLng = new LatLng(39.906901,116.397972);
	        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
	}
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
	    mMapView.onDestroy();
	  }
	 @Override
	 protected void onResume() {
	    super.onResume();
	    //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
	    mMapView.onResume();
	    }
	 @Override
	 protected void onPause() {
	    super.onPause();
	    //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
	    mMapView.onPause();
	    }
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
	    mMapView.onSaveInstanceState(outState);
	  } 
	 
	 
	 
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
