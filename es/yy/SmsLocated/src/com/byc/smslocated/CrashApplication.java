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
    //CrashHandlerʵ��  
    private static CrashHandler current = null; 
    @Override  
    public void onCreate() {  
        super.onCreate();  
        getCrashHandler(getApplicationContext());
    } 
    
    /** ��ȡCrashHandlerʵ�� ,����ģʽ */  
    public CrashHandler getCrashHandler(Context context) {  
        if(current == null) {
            current = new CrashHandler(context);
        }
        return current; 
    }  

    /** 
     * UncaughtException������,��������Uncaught�쳣��ʱ��,�и������ӹܳ���,����¼���ʹ��󱨸�. 
     *  
     * @author user 
     *  
     */  
    public class CrashHandler implements UncaughtExceptionHandler {  
          
        public String TAG = "byc001";
        //ϵͳĬ�ϵ�UncaughtException������   
        private Thread.UncaughtExceptionHandler mDefaultHandler;  
        //�����Context����  
        private Context context;  
        /** ��ֻ֤��һ��CrashHandlerʵ�� */  
        public CrashHandler(Context context) {  
        	this.context=context;
        	TAG=Config.TAG;
            //��ȡϵͳĬ�ϵ�UncaughtException������  
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
            //���ø�CrashHandlerΪ�����Ĭ�ϴ�����  
            Thread.setDefaultUncaughtExceptionHandler(this);  
        }  
        /** 
         * ��UncaughtException����ʱ��ת��ú��������� 
         */  
        @Override  
        public void uncaughtException(Thread thread, Throwable ex) {  
            if (!handleException(ex) && mDefaultHandler != null) {  
                //����û�û�д�������ϵͳĬ�ϵ��쳣������������  
                mDefaultHandler.uncaughtException(thread, ex);  
            } else {  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    Log.e(TAG, "error : ", e);  
                }  
                //������������  
                restartApp(context); 
            }  
        }  
      
        /** 
         * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. 
         *  
         * @param ex 
         * @return true:��������˸��쳣��Ϣ;���򷵻�false. 
         */  
        private boolean handleException(Throwable ex) {  
            if (ex == null) {  
                return false;  
            }  
            //ʹ��Toast����ʾ�쳣��Ϣ  
            new Thread() {  
                @Override  
                public void run() {  
                    Looper.prepare();  
                    Toast.makeText(context, "�ܱ�Ǹ,��������쳣,�����˳�.", Toast.LENGTH_LONG).show();  
                    Looper.loop();  
                }  
            }.start();  
            //�ռ��豸������Ϣ   
            //collectDeviceInfo(mContext);  
            //������־�ļ�   
            //saveCrashInfo2File(ex);  
            return true;  
        }
        /*
         * ��������Ӧ�ó���
         */
        private void restartApp(Context context){
        	Intent intent = new Intent(context, MainActivity.class);    
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent,Intent.FLAG_ACTIVITY_NEW_TASK);                                                                                      
            AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);      
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,restartIntent); // 1���Ӻ�����Ӧ��   
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
