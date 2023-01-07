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
		config=Config.getConfig(MainActivity.this);//�� ʼ�������ࣻ
		initWidgets();
		
	}
	/*
	 *���ò����� 
	 */
    private void initWidgets(){
    	btStart=(Button)findViewById(R.id.btStart);
    	btClose=(Button)findViewById(R.id.btClose);
    	etPhoneNumber=(EditText) findViewById(R.id.etPhoneNumber);
    	spPhoneNumber = (Spinner)findViewById(R.id.spPhoneNumber);
    	tvResolution = (TextView)findViewById(R.id.tvResolution);
    	//-------------------------------�������------------------------------------------- 
    	Config.PhoneNumbers=config.getPhoneNumbers();
    	if(Config.PhoneNumbers!=null){
    		// ����Adapter���Ұ�����Դ
    		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Config.PhoneNumbers);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		//�� Adapter���ؼ�
    		spPhoneNumber.setAdapter(adapter);
    		spPhoneNumber.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
    		    @Override
    		    public void onItemSelected(AdapterView<?> parent, View view, 
    		            int pos, long id) {
    		   
    		        //String[] languages = getResources().getStringArray(R.array.languages);
    		        //Toast.makeText(MainActivity.this, "��������:"+languages[pos], 2000).show();
    		    	String phoneNumber=Config.PhoneNumbers[pos];
    		    	MainActivity.this.etPhoneNumber.setText(phoneNumber);
    		    	config.setSelPhoneNumber(pos);//������ѡ���ţ�
    	           	String say="��ǰѡ��"+phoneNumber;
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
        //��ʼ��λ��
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				if(etPhoneNumber.getText()==null)
				{
					Toast.makeText(MainActivity.this, "��ѡ��������ֻ��ţ�", Toast.LENGTH_LONG).show();
					return;
				}
				mPhoneNumber=etPhoneNumber.getText().toString();
				if(mPhoneNumber.length()!=11){
					Toast.makeText(MainActivity.this, "��������ȷ���ֻ��� ��", Toast.LENGTH_LONG).show();
					return;
				}
				if(!Config.isPhoneNumberValid(mPhoneNumber)){
					Toast.makeText(MainActivity.this, "��������ȷ���ֻ��� ��", Toast.LENGTH_LONG).show();
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
				// CoordType.GPS ��ת����������
				//converter.from(CoordType.GPS); 
				// sourceLatLng��ת������� LatLng����
				//converter.coord(latLng); 
				// ִ��ת������
				//LatLng desLatLng = converter.convert();
				//Toast.makeText(MainActivity.this, "X:"+desLatLng.longitude+","+"Y:"+desLatLng.latitude, Toast.LENGTH_LONG).show();
			}
		});//btn.setOnClickListener(
	    //��ȡ�ֱ���:
	    getResolution2();
	    String sResolution="�����ֱ��ʣ���"+Config.screenWidth+"*"+Config.screenHeight+"��\n�汾�ţ�"+Config.currentapiVersion+ ","
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
	    //��activityִ��onDestroyʱִ��mMapView.onDestroy()�����ٵ�ͼ
	    //mMapView.onDestroy();
	    //mm.onDestroy();
	  }
	 @Override
	 protected void onResume() {
	    super.onResume();
	    //��activityִ��onResumeʱִ��mMapView.onResume ()�����»��Ƽ��ص�ͼ
	    //mMapView.onResume();
	    //mm.onResume();
	    }
	 @Override
	 protected void onPause() {
	    super.onPause();
	    //��activityִ��onPauseʱִ��mMapView.onPause ()����ͣ��ͼ�Ļ���
	    //mMapView.onPause();
	    //mm.onPause();
	 }
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //��activityִ��onSaveInstanceStateʱִ��mMapView.onSaveInstanceState (outState)�������ͼ��ǰ��״̬
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
