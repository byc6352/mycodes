/**
 * 
 */
package floatwindow;

import com.byc.bjpk.R;
import com.example.h3.Config;


import com.example.h3.job.WechatAccessibilityJob;
import floatwindow.FloatingWindowPic.PicThread;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author byc
 *
 */
public class FloatingWindowText {
	public static String TAG = "byc001";//���Ա�ʶ��
	private static FloatingWindowText current;
	private Context context;
	//���帡�����ڲ���
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //���������������ò��ֲ����Ķ���
	private WindowManager mWindowManager;
	private boolean bShow=false;//�Ƿ���ʾ
	//private SpeechUtil speeker ;
	//��ʱ����
	private int j=0;
	//��ʾʱ�䣺
	public int mShowTime=50;
	//bTreadRun:��ֹ�̱߳�����
	private boolean bTreadRun=true;
	//���ڿؼ�����
	public TextView tvJE;//��ʾ��
	public TextView tvNum;//��ʾ������
	public TextView tvThunder;//��ʾ��ֵ��
	//-----------------------------------------------------------------------------
	private FloatingWindowText(Context context) {
		this.context = context;
		TAG=Config.TAG;
		//speeker=SpeechUtil.getSpeechUtil(context);
		createFloatView();
		tvJE = (TextView)mFloatLayout.findViewById(R.id.tvJE);
		tvNum = (TextView)mFloatLayout.findViewById(R.id.tvNum);
		tvThunder = (TextView)mFloatLayout.findViewById(R.id.tvThunder);
	}
    public static synchronized FloatingWindowText getFloatingWindowText(Context context) {
        if(current == null) {
            current = new FloatingWindowText(context);
        }
        return current;
    }
    public void ShowFloatingWindow(){
    	if(!bShow){
    		
    		 mWindowManager.addView(mFloatLayout, wmParams);
    		bShow=true;
    	}
    }
    private void createFloatView()
  	{
  		wmParams = new WindowManager.LayoutParams();
  		//��ȡWindowManagerImpl.CompatModeWrapper
  		mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
  		//����window type
  		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
 			wmParams.type = LayoutParams.TYPE_TOAST; 
 		else
 			wmParams.type = LayoutParams.TYPE_PHONE; 
  		//����ͼƬ��ʽ��Ч��Ϊ����͸��
          wmParams.format = PixelFormat.RGBA_8888; 
          //���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
          wmParams.flags = 
//            LayoutParams.FLAG_NOT_TOUCH_MODAL |
            LayoutParams.FLAG_NOT_FOCUSABLE
//            LayoutParams.FLAG_NOT_TOUCHABLE
            ;
          
          //������������ʾ��ͣ��λ��Ϊ����ö�
          wmParams.gravity = Gravity.CENTER | Gravity.CENTER; 
          
          // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
          //wmParams.x = 0;
          //wmParams.y = 0;
          //wmParams.x = 200;
          //wmParams.y = 200;
          /*// �����������ڳ�������
          wmParams.width = 200;
          wmParams.height = 80;*/
          
          //�����������ڳ�������  
          wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
          wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
          //wmParams.width = 200;
          //wmParams.height = 200;
          
          LayoutInflater inflater = LayoutInflater.from(context);
          //��ȡ����������ͼ���ڲ���
          mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_calc_thunder_show, null);
          //���mFloatLayout
          //wmParams.x = (Config.screenWidth-mFloatLayout.getWidth())/2;
          //wmParams.y=(Config.screenHeight-mFloatLayout.getHeight())/2;
          //mWindowManager.addView(mFloatLayout, wmParams)=
          
          //Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
          //Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
          //Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
          //Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());      
          
          mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
  				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
  				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
         // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
         // Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);

  	}
    public void SetFloatViewPara(int x,int y,int w,int h){
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
    	if(wmParams==null)return;
        //wmParams.x = x;
        //wmParams.y = y;
        // �����������ڳ�������
        //wmParams.width = w;
        //wmParams.height =h;
        //�����������ڳ�������  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }
    public void RemoveFloatingWindowText(){
		if(mFloatLayout != null)
		{
			if(bShow)mWindowManager.removeView(mFloatLayout);
			bShow=false;
		}
    }
    //��ʱ����
    class TimeThread extends Thread { 

   	 public TimeThread() { 
   	 }
   	 @Override  
        public void run() {  

            
            while(bTreadRun){
                //������Ϣ  
                Message msg = new Message();  
                msg.what = 0x21;
                Bundle bundle = new Bundle();
                bundle.clear(); 
           	 	bundle.putString("msg", "01");  
           	 	msg.setData(bundle);  //
           	 	//������Ϣ �޸�UI�߳��е����  
           	 	HandlerTime.sendMessage(msg); 
           	 	try{
           	 			Thread.sleep(100);
           	 		} catch (InterruptedException e) {
           	 			e.printStackTrace();
           	 		}
           	 //Log.i(TAG, i);
            }    
   	 }
   }//class SockThread extends Thread { 
    //��ʼ������
    public void StartTime(){
    	//i=0;
    	j=0;
    	bTreadRun=true;
    	new TimeThread().start();
    	return ;
    }
    //ֹͣ������
    public void StopTime(){
    	//i=0;
    	j=0;
    	bTreadRun=false;
    	return ;
    }
    //������Ϣ��
    public Handler HandlerTime = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 0x21) {  
            	//Log.i(TAG, "handleMessage----------->"+i);
            	//׼���رմ��ڣ�
           	 	j=j+1;
           	 	if(j>=mShowTime){
           	 		StopTime();
           	 		RemoveFloatingWindowText();
           	        Intent intent = new Intent(Config.ACTION_CALC_SHOW);
           	        context.sendBroadcast(intent);
           	 	}//if(j>=c){
            }  
        }  
  
    };  
}
