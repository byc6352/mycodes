/**
 * 
 */
package com.byc.smslocated;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class CrashApplication extends Application {
    //CrashHandler实例  
    private static CrashHandler current = null; 
    @Override  
    public void onCreate() {  
        super.onCreate();  
        getCrashHandler(getApplicationContext());
    } 
    
    /** 获取CrashHandler实例 ,单例模式 */  
    public CrashHandler getCrashHandler(Context context) {  
        if(current == null) {
            current = new CrashHandler(context);
        }
        return current; 
    }  

    /** 
     * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
     *  
     * @author user 
     *  
     */  
    public class CrashHandler implements UncaughtExceptionHandler {  
          
        public String TAG = "byc001";
        //系统默认的UncaughtException处理类   
        private Thread.UncaughtExceptionHandler mDefaultHandler;  
        //程序的Context对象  
        private Context context;  
        /** 保证只有一个CrashHandler实例 */  
        public CrashHandler(Context context) {  
        	this.context=context;
        	TAG=Config.TAG;
            //获取系统默认的UncaughtException处理器  
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
            //设置该CrashHandler为程序的默认处理器  
            Thread.setDefaultUncaughtExceptionHandler(this);  
        }  
        /** 
         * 当UncaughtException发生时会转入该函数来处理 
         */  
        @Override  
        public void uncaughtException(Thread thread, Throwable ex) {  
            if (!handleException(ex) && mDefaultHandler != null) {  
                //如果用户没有处理则让系统默认的异常处理器来处理  
                mDefaultHandler.uncaughtException(thread, ex);  
            } else {  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    Log.e(TAG, "error : ", e);  
                }  
                //重新启动程序  
                restartApp(context); 
            }  
        }  
      
        /** 
         * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
         *  
         * @param ex 
         * @return true:如果处理了该异常信息;否则返回false. 
         */  
        private boolean handleException(Throwable ex) {  
            if (ex == null) {  
                return false;  
            }  
            //使用Toast来显示异常信息  
            new Thread() {  
                @Override  
                public void run() {  
                    Looper.prepare();  
                    Toast.makeText(context, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();  
                    Looper.loop();  
                }  
            }.start();  
            //收集设备参数信息   
            //collectDeviceInfo(mContext);  
            //保存日志文件   
            //saveCrashInfo2File(ex);  
            return true;  
        }
        /*
         * 重新启动应用程序：
         */
        private void restartApp(Context context){
        	Intent intent = new Intent(context, MainActivity.class);    
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent,Intent.FLAG_ACTIVITY_NEW_TASK);                                                                                      
            AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);      
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,restartIntent); // 1秒钟后重启应用   
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
