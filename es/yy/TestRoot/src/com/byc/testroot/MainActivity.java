package com.byc.testroot;



import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dalvik.system.DexClassLoader;
import util.ExcCmd;

public class MainActivity extends Activity {
	public TextView tvShow;
	public TextView tvShow1;
	public TextView tvShow2;
	public Button btLoad;
	public Button btLoad1;
	public Button btLoad2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvShow=(TextView) findViewById(R.id.tvShow);
		tvShow1=(TextView) findViewById(R.id.tvShow1);
		tvShow2=(TextView) findViewById(R.id.tvShow2);
		btLoad=(Button) findViewById(R.id.btLoad);
		btLoad1=(Button) findViewById(R.id.btLoad1);
		btLoad2=(Button) findViewById(R.id.btLoad2);
		ExcCmd.getInstance().testCmd();
		rootResult();
		BindWidgets();
		//testScreen();
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
	private void rootResult(){
		final Handler handler= new Handler(); 
	    Runnable runnableRootResult  = new Runnable() {    
			@Override    
		    public void run() {    
				tvShow.setText(ExcCmd.getInstance().mOut);
				handler.postDelayed(this, 1000*1);    
		    }    
		};
		handler.postDelayed(runnableRootResult, 1000*1); 	    
	}
	 /*
     * 绑定控件事件：
     */
    private void BindWidgets(){
    	btLoad.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//loadUninstallApk();
				String say="屏幕高："+getScreenHeight()+"屏幕宽："+getScreenWidth();
				tvShow1.setText(say);
			}
		});//startBtn.setOnClickListener(
    	btLoad1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//loadUninstallApk();
				//loadInstalledApk();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
					//Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
					intent.setData(Uri.parse("package:com.byc.testroot"));
					startActivity(intent);
				}
			}
		});//startBtn.setOnClickListener(
    	btLoad2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//loadUninstallApk();
				//RunApp(MainActivity.this, "com.byc.ct","activity.SplashActivity");
				//RunApp(getApplicationContext(), "com.byc.ct");
				//RunApp(getApplicationContext(), "com.example.nn");
				//gotoSettingIgnoringBatteryOptimizations(MainActivity.this);
				//ignoreBatteryOptimization(MainActivity.this);
				//setBai2(MainActivity.this);
				//tvShow2.setText(android.os.Build.BRAND);
				getDirText();
			}
		});//startBtn.setOnClickListener(
    }
    /**
	 * 
	 * @Title: loadUninstallApk 
	 * @Description: 动态加载未安装的apk
	 * @return void    
	 * @throws
	 */
	private void loadUninstallApk(){
        String path = Environment.getExternalStorageDirectory() + File.separator+"byc"+ File.separator;
        String filename = "ct.apk";
 
        // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.
        File optimizedDirectoryFile = getDir("dex", 0) ;
        DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                											null, getClassLoader());
 
        try {
        	// 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity
            Class mLoadClass = classLoader.loadClass("activity.SplashActivity");
            Constructor constructor = mLoadClass.getConstructor(new Class[] {});
            Object testActivity = constructor.newInstance(new Object[] {});
            
            // 获取sayHello方法
            Method helloMethod = mLoadClass.getMethod("sayHello", null);
            helloMethod.setAccessible(true);
            Object content = helloMethod.invoke(testActivity, null);
            Toast.makeText(MainActivity.this, content.toString(), Toast.LENGTH_LONG).show();
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("byc001", e.getMessage());
            Toast.makeText(MainActivity.this,  e.getMessage(), Toast.LENGTH_LONG).show();
        }
	}
	 private int getScreenWidth() {
		 return Resources.getSystem().getDisplayMetrics().widthPixels;
	}

	private int getScreenHeight() {
		return Resources.getSystem().getDisplayMetrics().heightPixels;
	}
	  /*
     * testScreen：
     */
    private void testScreen(){
    	final Handler handler= new Handler(); 
    	Runnable runnable  = new Runnable() {    
    		@Override    
    		public void run() {    
    			String say="屏幕高："+getScreenHeight()+"屏幕宽："+getScreenWidth();
    			Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();    					
    			handler.postDelayed(this, 1000*5);    
    		}    
    	};
    	handler.postDelayed(runnable, 1000*1); 
    }
	  /*
     * testLoadCt：
     */
    private void loadInstalledApk() {
    	String pkgName = "com.byc.ct";
    	try {
    		
    		Context context = createPackageContext(pkgName,	Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE) ;//
    		Class cls = context.getClassLoader().loadClass("activity.SplashActivity") ;
    		// 获取sayHello方法
    		Method sayHelloMethod = cls.getMethod("sayHello", null);
    		sayHelloMethod.setAccessible(true);   
    		 Object sayHelloObject = cls.newInstance();
    		Object content = sayHelloMethod.invoke(sayHelloObject,null);            
    		Toast.makeText(MainActivity.this, content.toString(), Toast.LENGTH_LONG).show();
    		
            Method testMethod = cls.getMethod("test", Context.class,boolean.class);
            testMethod.setAccessible(true);
            testMethod.invoke(null,context,true);

    		
            //Method setComponentEnabledMethod = cls.getMethod("setComponentEnabled", Context.class,String.class,boolean.class);
            //setComponentEnabledMethod.setAccessible(true);
            //setComponentEnabledMethod.invoke(null,context, cls.getName(),true);
    		//startActivity(new Intent(context, cls)) ;  
            
    	}catch(NoSuchMethodException e) {
    		e.printStackTrace();		
    	}catch ( InvocationTargetException e) {
    		e.printStackTrace();
    		Log.d("byc003",e.toString()) ;		
    	}catch ( NameNotFoundException e) {
    		Log.d("byc003", e.toString()) ;		
    	}catch ( ClassNotFoundException e) {
    		Log.d("byc003", e.toString()) ;		
    	}catch ( IllegalAccessException e) {
    		Log.d("byc003", e.toString()) ;		
    	}catch ( InstantiationException e) {
    		Log.d("byc003", e.toString()) ;		
    	}
    	RunApp(this, pkgName,"activity.SplashActivity");
    }
    /** 
     * 运行：
     * @param context 
     * @param filename  文件名 
     * @return   PackageName：包名
     */  
	public static boolean RunApp(Context context,String pkgName,String mainClass) { 
		try{
			Intent intent = new Intent(Intent.ACTION_MAIN);   
			intent.addCategory(Intent.CATEGORY_LAUNCHER);  
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ComponentName cn = new ComponentName(pkgName, mainClass);               
			intent.setComponent(cn);  
			intent.putExtra("hide", true);
			context.startActivity(intent);
			return true;
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
			return false;
		}
    	
	}
	/**
     * <功能描述> 启动应用程序
     * @param context 
     * @param PackageName：包名
     * @return boolean [执行启动应用程序成功否]
     */
	public static boolean  RunApp(Context context,String pkg) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            // 获取指定包名的应用程序的PackageInfo实例
              packageInfo = packageManager.getPackageInfo(pkg, 0);
        } catch (NameNotFoundException e) {
            // 未找到指定包名的应用程序
              e.printStackTrace();
            return false;
        }
        if (packageInfo != null) {
            // 已安装应用
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(
                    resolveIntent, 0);
            ResolveInfo ri = null;
            try {
                ri = apps.iterator().next();
            } catch (Exception e) {
                e.printStackTrace();
                print(e);
                return false;
            }
            if (ri != null) {
                // 获取应用程序对应的启动Activity类名
                  String className = ri.activityInfo.name;
                // 启动应用程序对应的Activity
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName componentName = new ComponentName(pkg, className);
                intent.setComponent(componentName);
                intent.putExtra("hide", true);
                context.startActivity(intent);
            }
        }
        return true;
	}
	 private static String print(Exception ex) {

	        Writer writer = new StringWriter();
	        PrintWriter printWriter = new PrintWriter(writer);
	        ex.printStackTrace(printWriter);
	        Throwable cause = ex.getCause();
	        while (cause != null) {
	            cause.printStackTrace(printWriter);
	            cause = cause.getCause();
	        }
	        printWriter.close();
	        String result = writer.toString();
	        Log.i("byc003", result);
	        return result;
	 }
	 /**去设置电池忽略优化**/
	 private static int REQUEST_IGNORE_BATTERY_CODE = 1001;
	 private void gotoSettingIgnoringBatteryOptimizations(Activity context) {
	     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
	         try {
	        	 Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
	             String packageName = context.getPackageName();
	             //intent.putExtra("package", packageName);
	             intent.setData(Uri.parse("package:"+packageName));
	             //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	             context.startActivity(intent);
	             //context.startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	     }
	 }
	 /** * 忽略电池优化 */
	 public void ignoreBatteryOptimization(Activity activity) {
	      PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
	      boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
	     //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
	     if(!hasIgnored) {
	           Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS); 
	        intent.setData(Uri.parse("package:"+activity.getPackageName()));
	         startActivity(intent);
	     }
	 }
	 /** * 忽略电池优化 */
	 public void setBai(Activity activity) {
		 Intent intent = new  Intent();
		 intent.setClassName("com.android.settings", "com.android.settings.Settings$PowerUsageSummaryActivity");
		 startActivity(intent);


	 }
	 /** * 忽略电池优化 */
	 public void setBai2(Activity activity) {
		 if(Build.MANUFACTURER.equals("Xiaomi")) {
             Intent intent = new Intent();
             intent.setAction("miui.intent.action.OP_AUTO_START");
             intent.addCategory(Intent.CATEGORY_DEFAULT);
             startActivity(intent);
         }


	 }
	 /** * 忽略电池优化 */
	 public void getDirText() {
		 File odex = this.getDir("payload_odex", MODE_PRIVATE);
		 String apkFileName = odex.getAbsolutePath() + "/payload.apk";
		File dexFile = new File(apkFileName);
		Log.i("byc003", apkFileName);
		String sdcardPath = Environment.getExternalStorageDirectory().toString();
	    String workDir = sdcardPath + "/byc";
	    Log.i("byc003", workDir);
		tvShow2.setText(apkFileName);
	 }
}
