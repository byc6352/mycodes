/**
 * 
 */
package com.example.h3.util;


import com.byc.autoadd.R;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.job.QQaccessbilityJob;
import com.example.h3.job.WechatAccessbilityJob;
import com.example.h3.util.SpeechUtil;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class FloatingWindow {
	public static String TAG = "byc001";//���Ա�ʶ��
	private static FloatingWindow current;
	private Context context;
	private WechatAccessbilityJob  job;
	private QQaccessbilityJob  QQjob;
	//-------------------------------------------------------------------------
	//���帡�����ڲ���
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //���������������ò��ֲ����Ķ���
	private WindowManager mWindowManager;
	
	private Button mFbtMove;
	public Button mFbtFunc1;
	public Button mFbtFunc2;
	public Button mFbtFunc3;
	public Button mFbtFunc4;
	private boolean bShow=false;//�Ƿ���ʾ
	private SpeechUtil speaker ;
	//-----------------------------------------------------------------------------
	private FloatingWindow() {
		

		TAG=Config.TAG;

	}
    public static synchronized FloatingWindow getFloatingWindow(WechatAccessbilityJob job,QQaccessbilityJob  QQjob) {
        if(current == null) {
            current = new FloatingWindow();
        }
		if(job!=null){current.job=job;current.context = job.context;}
		if(QQjob!=null){current.QQjob=QQjob;current.context = QQjob.context;}
		current.speaker=SpeechUtil.getSpeechUtil(current.context);
		if(current.mWindowManager==null)current.createFloatView();
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
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
 			wmParams.type = LayoutParams.TYPE_TOAST; 
 		else
 			wmParams.type = LayoutParams.TYPE_PHONE; 
		//����ͼƬ��ʽ��Ч��Ϊ����͸��
        wmParams.format = PixelFormat.RGBA_8888; 
        //���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
        wmParams.flags = 
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
          LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
          ;
        
        //������������ʾ��ͣ��λ��Ϊ����ö�
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
        
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
        wmParams.x = 0;
        wmParams.y = 0;

        /*// �����������ڳ�������
        wmParams.width = 200;
        wmParams.height = 80;*/
        
        //�����������ڳ�������  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(context);
        //��ȡ����������ͼ���ڲ���
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //���mFloatLayout
        //mWindowManager.addView(mFloatLayout, wmParams);
        
        //Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        //Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        //Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        //Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());      
        
        //�������ڰ�ť
        mFbtMove= (Button)mFloatLayout.findViewById(R.id.fbtMove);
        mFbtFunc1= (Button)mFloatLayout.findViewById(R.id.btFunc1);
        mFbtFunc2= (Button)mFloatLayout.findViewById(R.id.btFunc2);
        mFbtFunc3= (Button)mFloatLayout.findViewById(R.id.btFunc3);
        mFbtFunc4= (Button)mFloatLayout.findViewById(R.id.btFunc4);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
       // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
       // Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);
        //���ü����������ڵĴ����ƶ�
        mFbtMove.setOnTouchListener(new OnTouchListener() 
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				//getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������
				wmParams.x = (int) event.getRawX() - mFbtMove.getMeasuredWidth()/2;

	            wmParams.y = (int) event.getRawY() - mFbtMove.getMeasuredHeight()/2 - 25;

	             //ˢ��
	            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});	
        //��ť1���ܣ�
        mFbtFunc1.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_WECHAT)
					FloatingWindow.this.job.distributeChatJob(1);
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_QQ)
					FloatingWindow.this.QQjob.distributeAddQqMembers();
				//FloatingWindow.this.job.test();
				
			}
		});
        //��ť2���ܣ�
        mFbtFunc2.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_WECHAT)
					FloatingWindow.this.job.distributeChatJob(2);
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_QQ)
					FloatingWindow.this.QQjob.distributeAddQqMembers();
				//FloatingWindow.this.job.test();
				
			}
		});
        //��ť3���ܣ�
        mFbtFunc3.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_WECHAT)
					FloatingWindow.this.job.distributeChatJob(3);
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_QQ)
					FloatingWindow.this.QQjob.distributeAddQqMembers();
				//FloatingWindow.this.job.test();
				
			}
		});
        //��ť2���ܣ�
        mFbtFunc4.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_WECHAT)
					FloatingWindow.this.job.distributeChatJob(4);
				if(Config.iWorkPlatform==Config.WORK_PLATFORM_QQ)
					FloatingWindow.this.QQjob.distributeAddQqMembers();
				//FloatingWindow.this.job.test();
				
			}
		});
	}
    public void DestroyFloatingWindow(){
		if(mFloatLayout != null)
		{
			if(bShow)mWindowManager.removeView(mFloatLayout);
			bShow=false;
		}
    }
}
