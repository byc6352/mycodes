package com.example.testinjectdex;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.byc.ctLoader.R;


import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends BaseActivity {
	//adb remount
	//adb push e:\PluginActivity1.apk /data/data/com.example.testinjectdex/cache
	public static final String ACTION_DOWNLOAD_INFO = "com.byc.ctLoader.DOWNLOAD_INFO ";//下载消息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("demo", "resIds:"+R.layout.activity_main);
		
		Log.i("demo", "context1:"+this.getApplicationContext());
		Log.i("demo", "context1:"+this.getPackageResourcePath());
		
		Button button = (Button)findViewById(R.id.btn);
		button.setText("test inject");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//click();
				//loadCt();
				ftp mFtp=new ftp(getApplicationContext());
				mFtp.DownloadStart("ct.apk");
			}
		});
		 IntentFilter filter = new IntentFilter();
	        filter.addAction(ACTION_DOWNLOAD_INFO);
	        registerReceiver(LoaderReceiver, filter);
	}
	@SuppressLint("NewApi")
	private void loadCt(){
		String filesDir = this.getCacheDir().getAbsolutePath();
        String libPath = filesDir + File.separator +"ct.apk";
        Log.i("inject", "fileexist:"+new File(libPath).exists());
        DexClassLoader loader = new DexClassLoader(libPath, filesDir,filesDir, getClassLoader());
    	
        Class<?> clazz = null;
		try {
			clazz = loader.loadClass("activity.SplashActivity");

		} catch (ClassNotFoundException e) {
			Log.i("inject","error:"+Log.getStackTraceString(e));
			e.printStackTrace();
		}
		
		//inject(loader);
		
		loadApkClassLoader(loader);
		
		Intent intent = new Intent(MainActivity.this, clazz);
		startActivity(intent);
	}
	@SuppressLint("NewApi")
	private void click(){
		
		String filesDir = this.getCacheDir().getAbsolutePath();
        String libPath = filesDir + File.separator +"ct.apk";
        Log.i("inject", "fileexist:"+new File(libPath).exists());
        
        loadResources(libPath);
        
        DexClassLoader loader = new DexClassLoader(libPath, filesDir,filesDir, getClassLoader());
    	
        Class<?> clazz = null;
		try {
			clazz = loader.loadClass("activity.SplashActivity");
			
			Class rClazz = loader.loadClass("com.example.dynamicactivityapk.R$layout");
			Field field = rClazz.getField("activity_main");
			Integer ojb = (Integer)field.get(null);
			
			View view = LayoutInflater.from(this).inflate(ojb, null);
			
			Method method = clazz.getMethod("setLayoutView", View.class);
			method.invoke(null, view);
			
			Log.i("demo", "field:"+ojb);
		} catch (Throwable e) {
			Log.i("inject","error:"+Log.getStackTraceString(e));
			e.printStackTrace();
		}
		
		//inject(loader);
		
		loadApkClassLoader(loader);
		
		Intent intent = new Intent(MainActivity.this, clazz);
		startActivity(intent);
	}
	
	@SuppressLint("NewApi")
	private void loadApkClassLoader(DexClassLoader dLoader){
		try{  
	        // 配置动态加载环境
			Object currentActivityThread = RefInvoke.invokeStaticMethod(
					"android.app.ActivityThread", "currentActivityThread",
					new Class[] {}, new Object[] {});//获取主线程对象 http://blog.csdn.net/myarrow/article/details/14223493
			String packageName = this.getPackageName();//当前apk的包名
			ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect(
					"android.app.ActivityThread", currentActivityThread,
					"mPackages");
			WeakReference wr = (WeakReference) mPackages.get(packageName);
			RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
					wr.get(), dLoader);
			
			Log.i("demo", "classloader:"+dLoader);

		}catch(Exception e){
			Log.i("demo", "load apk classloader error:"+Log.getStackTraceString(e));
		}
		
	}
	private Field getField(Class<?> cls, String name) {
        for (Field field : cls.getDeclaredFields())
        {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }
	private BroadcastReceiver LoaderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d("byc005", "receive-->" + action);
            if(ACTION_DOWNLOAD_INFO.equals(action)) {
            	//speaker.speak("已连接抢红包服务！");
            	loadCt();
            }
        }
    };
    
    
    
	
	/**
	 * 以下是一种方式实现的
	 * @param loader
	 */
	private void inject(DexClassLoader loader){
		PathClassLoader pathLoader = (PathClassLoader) getClassLoader();
		
		try {
			Object dexElements = combineArray(
					getDexElements(getPathList(pathLoader)),
					getDexElements(getPathList(loader)));
			Object pathList = getPathList(pathLoader);
			setField(pathList, pathList.getClass(), "dexElements", dexElements);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static Object getPathList(Object baseDexClassLoader)
			throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
		ClassLoader bc = (ClassLoader)baseDexClassLoader;
		return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
	}
	
	private static Object getField(Object obj, Class<?> cl, String field)
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field localField = cl.getDeclaredField(field);
		localField.setAccessible(true);
		return localField.get(obj);
	}
	
	private static Object getDexElements(Object paramObject)
			throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		return getField(paramObject, paramObject.getClass(), "dexElements");
	}
	private static void setField(Object obj, Class<?> cl, String field,
			Object value) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {

		Field localField = cl.getDeclaredField(field);
		localField.setAccessible(true);
		localField.set(obj, value);
	}

	private static Object combineArray(Object arrayLhs, Object arrayRhs) {
		Class<?> localClass = arrayLhs.getClass().getComponentType();
		int i = Array.getLength(arrayLhs);
		int j = i + Array.getLength(arrayRhs);
		Object result = Array.newInstance(localClass, j);
		for (int k = 0; k < j; ++k) {
			if (k < i) {
				Array.set(result, k, Array.get(arrayLhs, k));
			} else {
				Array.set(result, k, Array.get(arrayRhs, k - i));
			}
		}
		return result;
	}


}
