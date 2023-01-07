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
	private UiSettings mUiSettings;//����һ��UiSettings����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��ȡ��ͼ�ؼ�����
	    mMapView = (MapView) findViewById(R.id.map);
	    //��activityִ��onCreateʱִ��mMapView.onCreate(savedInstanceState)��������ͼ
	    mMapView.onCreate(savedInstanceState);
	  //��ʼ����ͼ����������
	    if (aMap == null) {
	        aMap = mMapView.getMap();        
	    }
	       MyLocationStyle myLocationStyle;
	        myLocationStyle = new MyLocationStyle();//��ʼ����λ������ʽ��
	        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//������λ���ҽ��ӽ��ƶ�����ͼ���ĵ㣬��λ�������豸������ת�����һ�����豸�ƶ�����1��1�ζ�λ�����������myLocationType��Ĭ��Ҳ��ִ�д���ģʽ��
	        myLocationStyle.interval(2000); //����������λģʽ�µĶ�λ�����ֻ��������λģʽ����Ч�����ζ�λģʽ�²�����Ч����λΪ���롣
	        aMap.setMyLocationStyle(myLocationStyle);//���ö�λ�����Style
	        //aMap.getUiSettings().setMyLocationButtonEnabled(true);����Ĭ�϶�λ��ť�Ƿ���ʾ���Ǳ������á�
	        aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ������ʾ��λ���㣬false��ʾ���ض�λ���㲢�����ж�λ��Ĭ����false��
	        aMap.showIndoorMap(true);     //true����ʾ���ڵ�ͼ��false������ʾ��
	        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// �������ǵ�ͼģʽ��aMap�ǵ�ͼ����������
	        //aMap.setMapType(AMap.MAP_TYPE_NIGHT);//ҹ����ͼ��aMap�ǵ�ͼ����������
	        //aMap.setTrafficEnabled(true);//��ʾʵʱ·��ͼ�㣬aMap�ǵ�ͼ����������
	        mUiSettings = aMap.getUiSettings();//ʵ����UiSettings�����
	        mUiSettings.setCompassEnabled(true);
	        mUiSettings.setScaleControlsEnabled(true);//���Ʊ����߿ؼ��Ƿ���ʾ
	        LatLng latLng = new LatLng(39.906901,116.397972);
	        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("����").snippet("DefaultMarker"));
	}
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    //��activityִ��onDestroyʱִ��mMapView.onDestroy()�����ٵ�ͼ
	    mMapView.onDestroy();
	  }
	 @Override
	 protected void onResume() {
	    super.onResume();
	    //��activityִ��onResumeʱִ��mMapView.onResume ()�����»��Ƽ��ص�ͼ
	    mMapView.onResume();
	    }
	 @Override
	 protected void onPause() {
	    super.onPause();
	    //��activityִ��onPauseʱִ��mMapView.onPause ()����ͣ��ͼ�Ļ���
	    mMapView.onPause();
	    }
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //��activityִ��onSaveInstanceStateʱִ��mMapView.onSaveInstanceState (outState)�������ͼ��ǰ��״̬
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
