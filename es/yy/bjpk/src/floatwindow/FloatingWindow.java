/**
 * 
 */
package floatwindow;


import java.util.List;

import com.byc.bjpk.R;

import util.BackgroundMusic;
import com.example.h3.Config;
import com.example.h3.MainActivity;
import com.example.h3.job.WechatAccessibilityJob;
import util.SpeechUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
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
	//-------------------------------------------------------------------------
	//���帡�����ڲ���
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //���������������ò��ֲ����Ķ���
	private WindowManager mWindowManager;
	
	private Button mFbtMove;
	private Button mFbtGet;
	private Button mFbtRet;
	private boolean bShow=false;//�Ƿ���ʾ
	private SpeechUtil speaker ;
	public static int mIsTest=0;
	
	//������ʱ��
	private FloatingWindowPic fwp;
	//
	private FloatingWindowText fwt;//��ʾ����������ʾͶעֵ��
	private BackgroundMusic mBackgroundMusic;
	private String[] mRacings={"�ھ�","�Ǿ�","������","������","������","������","������","�ڰ���","�ھ���","��ʮ��"};//�����飻
	private String[] mBettings={"��","С","��","˫","��","��"};//��ע��
	//-----------------------------------------------------------------------------
	private WechatAccessibilityJob  monitor;
	private FloatingWindow(Context context) {
		this.context = context;
		TAG=Config.TAG;
		speaker=SpeechUtil.getSpeechUtil(context);
		createFloatView();
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//������ʱ��
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//��ʾ��ֵ���ڣ�
		fwt=FloatingWindowText.getFloatingWindowText(context);
		fwt.SetFloatViewPara(100, 200,w,w);
		//���չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_CALC_DELAY);
        filter.addAction(Config.ACTION_CALC_SHOW);
        context.registerReceiver(calcThunderReceiver, filter);
        //���Ӵ���:
        monitor=WechatAccessibilityJob.getJob();
	}
    public static synchronized FloatingWindow getFloatingWindow(Context context) {
        if(current == null) {
            current = new FloatingWindow(context);
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
 			//wmParams.type = LayoutParams.TYPE_PHONE; 
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
        mFbtMove = (Button)mFloatLayout.findViewById(R.id.fbtMove);
        mFbtGet = (Button)mFloatLayout.findViewById(R.id.fbtGet);
        mFbtRet = (Button)mFloatLayout.findViewById(R.id.fbtRet);
        /*
        GradientDrawable drawable = new GradientDrawable();  
        drawable.setShape(GradientDrawable.RECTANGLE); // ����  
        drawable.setStroke(1, Color.BLUE); // �߿��ϸ����ɫ  
        drawable.setColor(0x22FFFF00); // �߿��ڲ���ɫ  
          
        Button mFloatView1 = (Button)mFloatLayout.findViewById(R.id.float_btMove);
        mFloatView1.setBackgroundDrawable(drawable); // ���ñ�����Ч�������б߿򼰵�ɫ��
        
        */
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
				// TODO Auto-generated method stub
				//getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������
				wmParams.x = (int) event.getRawX() - mFbtMove.getMeasuredWidth()/2;
				//Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
				//Log.i(TAG, "RawX" + event.getRawX());
				//Log.i(TAG, "X" + event.getX());
				//25Ϊ״̬���ĸ߶�
	            wmParams.y = (int) event.getRawY() - mFbtMove.getMeasuredHeight()/2 - 25;
	           // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
	           // Log.i(TAG, "RawY" + event.getRawY());
	          //  Log.i(TAG, "Y" + event.getY());
	             //ˢ��
	            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});	
        
        mFbtGet.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
				String sShow="";
				/*
				if(!Config.bReg){
					sShow="�������ð��û����빺�����棡���ܿ�ʼ���ס�";
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					return;
				}
				
				boolean bWindow=FloatingWindow.this.job.distributeClickJiaJob();
				if(!bWindow){
					sShow="�����Ҫ���׵ĺ��Ⱥ�����ܿ�ʼ���ס�";
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					DestroyFloatingWindow();
				}else{
					Config.JobState=Config.STATE_SENDING_LUCKYMONEY;
				}
				*/
				//FloatingWindow.this.job.distributePutThunder();
				//MainActivity.get3(context);
				//MainActivity.get2(context);
				//MainActivity.getCurrentPkgName(context);
				//MainActivity.getAppProcessName(context);
				//FloatingWindow.this.DestroyFloatingWindow();
				//MainActivity.OpenGame(context.getPackageName(), context);
				//if(!isBrowser()){
				//	sShow="���Ƚ�����Ϸ!���ܿ�ʼԤ��Ͷע��";
				//	Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
				//	speaker.speak(sShow);
				//	return;
				//}
				if(mIsTest>0){
					sShow="�������ð��û����빺���������ܼ���ͶעԤ�⣡";
				}else{
					FloatingWindow.this.CalcDelay();//��ʾ����Ͷע��ʱ���ڣ�
					if(Config.bReg){
						sShow="����ִ��ͶעԤ��...";
					}else{
						sShow="�������ð��û����빺�������ͶעԤ��100%��׼��";
					}
					mIsTest=mIsTest+1;
				}
				Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
				speaker.speak(sShow);
				//String appID=MainActivity.getLollipopRecentTask(context);
				//Log.i(TAG, "appID----------------"+appID);
				//Toast.makeText(context, appID, Toast.LENGTH_LONG).show();
			}
		});
        //���� ���� ����
        mFbtRet.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				DestroyFloatingWindow();
				MainActivity.OpenGame(context,context.getPackageName());
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
    /*
     * ����Ͷעֵ��ʱ��
     */
    private void CalcDelay() {
    	if(!isBrowser()){
    		String say="�����Ͷע���ڣ����ܽ���ͶעԤ�⣡";
    		speaker.speak(say);
    		Toast.makeText(context, say, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	speaker.speak("����Ϊ���������Ͷע���֣�");
        //���ű������֣�
        mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
        //��ʾ��ʱ����
		fwp.ShowFloatingWindow(); 
    	fwp.c=30;
    	fwp.mSendMsg=Config.ACTION_CALC_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_RED;
    	fwp.StartSwitchPics();
    }
    //����һ��0~9���������
    private int getRadomNum(int m){
    	java.util.Random random=new java.util.Random();// ���������
    	int result=random.nextInt(m);// ����[0,10)�����е�������ע�ⲻ����10
    	return result;
    }
    //������ʱ������Ϣ��
	private BroadcastReceiver calcThunderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            //������ʱ��������
            if(Config.ACTION_CALC_DELAY.equals(action)) {
            	//1������Ѻעֵ��
            	int iRacing=getRadomNum(10);//������ţ�
            	int iBetting=getRadomNum(6);//��ע��ţ�
            	if(iRacing>4&&iBetting>3)iBetting=3;
            	String aRacing="�����������Ϊ��"+mRacings[iRacing];
            	String aBetting="�������ѺעΪ��"+mBettings[iBetting];
            	//2����ʾ�����ֵ��
            	fwt.ShowFloatingWindow();
    
            	fwt.tvJE.setText(aRacing);
            	fwt.tvNum.setText(aBetting);
            	fwt.tvThunder.setText("");
            	speaker.speak(aRacing);
            	speaker.speak(aBetting);
            	fwt.mShowTime=100;
            	fwt.StartTime();
            }
            //2����ʾ��ֵ��������
            if(Config.ACTION_CALC_SHOW.equals(action)) {
            	//�رձ������֣�
            	mBackgroundMusic.stopBackgroundMusic();
            }
        }
    };
    public boolean isBrowser(){
    	//ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
    	//List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
    	//String CurPkg=rti.get(0).topActivity.getPackageName();
    	//if(CurPkg.equals("com.android.browser"))return true;else return false;
    	//return monitor.distributeClickJiaJob();
    	return true;
    }
}
