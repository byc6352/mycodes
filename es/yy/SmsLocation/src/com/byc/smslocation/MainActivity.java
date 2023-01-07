package com.byc.smslocation;

import android.util.Log;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.CoordinateConverter.CoordType;
import com.amap.api.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//ActionBarActivity
	private String TAG = "byc001";
	private Button btStart; 
	private Button btClose; 
	public EditText etPhoneNumber;
	private Spinner spPhoneNumber;
	private Config config=null;
	private String mPhoneNumber=null;
	public TextView tvResolution;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//initMap(savedInstanceState);
		TAG=Config.TAG;
		config=Config.getConfig(MainActivity.this);//初 始化配置类；
		initWidgets();
		
	}
	/*
	 *配置参数： 
	 */
    private void initWidgets(){
    	btStart=(Button)findViewById(R.id.btStart);
    	btClose=(Button)findViewById(R.id.btClose);
    	etPhoneNumber=(EditText) findViewById(R.id.etPhoneNumber);
    	spPhoneNumber = (Spinner)findViewById(R.id.spPhoneNumber);
    	tvResolution = (TextView)findViewById(R.id.tvResolution);
    	//-------------------------------读入参数------------------------------------------- 
    	Config.PhoneNumbers=config.getPhoneNumbers();
    	if(Config.PhoneNumbers!=null){
    		// 建立Adapter并且绑定数据源
    		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Config.PhoneNumbers);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		//绑定 Adapter到控件
    		spPhoneNumber.setAdapter(adapter);
    		spPhoneNumber.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
    		    @Override
    		    public void onItemSelected(AdapterView<?> parent, View view, 
    		            int pos, long id) {
    		   
    		        //String[] languages = getResources().getStringArray(R.array.languages);
    		        //Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], 2000).show();
    		    	String phoneNumber=Config.PhoneNumbers[pos];
    		    	MainActivity.this.etPhoneNumber.setText(phoneNumber);
    		    	config.setSelPhoneNumber(pos);//保存所选择编号；
    	           	String say="当前选择："+phoneNumber;
    	            Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		    }
    		    @Override
    		    public void onNothingSelected(AdapterView<?> parent) {
    		        // Another interface callback
    		    }
    		});
    		Config.iSelPhoneNumber=config.getSelPhoneNumber();
    		if(Config.iSelPhoneNumber>=0)
    			spPhoneNumber.setSelection(Config.iSelPhoneNumber);
    	}//if(Config.PhoneNumbers!=null){
        //开始定位：
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				if(etPhoneNumber.getText()==null)
				{
					Toast.makeText(MainActivity.this, "请选择或输入手机号！", Toast.LENGTH_LONG).show();
					return;
				}
				mPhoneNumber=etPhoneNumber.getText().toString();
				if(mPhoneNumber.length()!=11){
					Toast.makeText(MainActivity.this, "请输入正确的手机号 ！", Toast.LENGTH_LONG).show();
					return;
				}
				if(!Config.isPhoneNumberValid(mPhoneNumber)){
					Toast.makeText(MainActivity.this, "请输入正确的手机号 ！", Toast.LENGTH_LONG).show();
					return;
				}
				Config.CurmPhoneNumber=mPhoneNumber;
				Config.getConfig(MainActivity.this).addPhoneNumbers(mPhoneNumber);
				SmsReceiver.SendSms(mPhoneNumber);
				Intent intent=new Intent(MainActivity.this,MapActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});//btReg.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
				//System.exit(0);
				//Intent intent=new Intent(MainActivity.this,MapActivity.class);
				//MainActivity.this.startActivity(intent);
				//MyLocationMap mm=MyLocationMap.getMyLocationMap(null, null);
				//mm.mX=102.53797795;//102.53798383
				//mm.mY=24.36135211;//24.36131022
				//mm.SetCenterPosition(mm.mX,mm.mY);
				//mm.SetMarker(mm.mX,mm.mY);
				//double mX=39.906901;
				//double mY=116.397972;
		
				//double mY=102.53797795;
				//double mX=24.36135211;
				//LatLng latLng=new LatLng(mX,mY);
				//CoordinateConverter converter  = new CoordinateConverter(MainActivity.this); 
				// CoordType.GPS 待转换坐标类型
				//converter.from(CoordType.GPS); 
				// sourceLatLng待转换坐标点 LatLng类型
				//converter.coord(latLng); 
				// 执行转换操作
				//LatLng desLatLng = converter.convert();
				//Toast.makeText(MainActivity.this, "X:"+desLatLng.longitude+","+"Y:"+desLatLng.latitude, Toast.LENGTH_LONG).show();
			}
		});//btn.setOnClickListener(
	    //获取分辨率:
	    getResolution2();
	    String sResolution="本机分辨率：（"+Config.screenWidth+"*"+Config.screenHeight+"）\n版本号："+Config.currentapiVersion+ ","
	    		+ android.os.Build.MODEL+ "," + android.os.Build.VERSION.SDK + ","+ android.os.Build.VERSION.RELEASE;
	    tvResolution.setText(sResolution);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//LocationMarker(1,1);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//LocationMarker(39.986919,116.353369);
			//mm.SetCenterPosition(39.986919,116.353369);
			//mm.SetMarker(39.986919,116.353369);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
	    //mMapView.onDestroy();
	    //mm.onDestroy();
	  }
	 @Override
	 protected void onResume() {
	    super.onResume();
	    //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
	    //mMapView.onResume();
	    //mm.onResume();
	    }
	 @Override
	 protected void onPause() {
	    super.onPause();
	    //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
	    //mMapView.onPause();
	    //mm.onPause();
	 }
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
	    //mMapView.onSaveInstanceState(outState);
	    //mm.onSaveInstanceState(outState);
	  } 
	    @SuppressWarnings("deprecation")
		private void getResolution2(){
	        WindowManager windowManager = getWindowManager();    
	        Display display = windowManager.getDefaultDisplay();    
	        Config.screenWidth= display.getWidth();    
	        Config.screenHeight= display.getHeight();  
	        Config.currentapiVersion=android.os.Build.VERSION.SDK_INT;
	    }
}
