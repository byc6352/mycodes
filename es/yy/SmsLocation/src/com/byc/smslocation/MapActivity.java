package com.byc.smslocation;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends Activity {
	public static String TAG="byc001";
	//MapView mMapView = null;
	//AMap aMap=null;
	//private UiSettings mUiSettings;//定义一个UiSettings对象
	MyLocationMap mm=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mm=MyLocationMap.getMyLocationMap(MapActivity.this,(MapView)findViewById(R.id.map), savedInstanceState);
	}

	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
	    mm.onDestroy();
	  }
	 @Override
	 protected void onResume() {
	    super.onResume();
	    //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
	    mm.onResume();
	    }
	 @Override
	 protected void onPause() {
	    super.onPause();
	    //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
	    mm.onPause();
	    }
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
	    mm.onSaveInstanceState(outState);
	  } 
}
