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
	public static String TAG = "byc001";//调试标识：
	private static FloatingWindow current;
	private Context context;
	//-------------------------------------------------------------------------
	//定义浮动窗口布局
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;
	
	private Button mFbtMove;
	private Button mFbtGet;
	private Button mFbtRet;
	private boolean bShow=false;//是否显示
	private SpeechUtil speaker ;
	public static int mIsTest=0;
	
	//计算延时：
	private FloatingWindowPic fwp;
	//
	private FloatingWindowText fwt;//显示浮动窗口显示投注值：
	private BackgroundMusic mBackgroundMusic;
	private String[] mRacings={"冠军","亚军","第三名","第四名","第五名","第六名","第七名","第八名","第九名","第十名"};//赛车组；
	private String[] mBettings={"大","小","单","双","龙","虎"};//赌注；
	//-----------------------------------------------------------------------------
	private WechatAccessibilityJob  monitor;
	private FloatingWindow(Context context) {
		this.context = context;
		TAG=Config.TAG;
		speaker=SpeechUtil.getSpeechUtil(context);
		createFloatView();
		mBackgroundMusic=BackgroundMusic.getInstance(context);
		//计算延时：
		fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		int w=Config.screenWidth-200;
		int h=Config.screenHeight-200;
		fwp.SetFloatViewPara(100, 200,w,h);
		//显示雷值窗口：
		fwt=FloatingWindowText.getFloatingWindowText(context);
		fwt.SetFloatViewPara(100, 200,w,w);
		//接收广播消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_CALC_DELAY);
        filter.addAction(Config.ACTION_CALC_SHOW);
        context.registerReceiver(calcThunderReceiver, filter);
        //监视窗口:
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
		//获取WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
		//设置window type
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
 			wmParams.type = LayoutParams.TYPE_TOAST; 
 			//wmParams.type = LayoutParams.TYPE_PHONE; 
 		else
 			wmParams.type = LayoutParams.TYPE_PHONE; 
		//设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888; 
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = 
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
          LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
          ;
        
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
        
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
        
        //设置悬浮窗口长宽数据  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(context);
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        //mWindowManager.addView(mFloatLayout, wmParams);
        
        //Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        //Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        //Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        //Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());      
        
        //浮动窗口按钮
        mFbtMove = (Button)mFloatLayout.findViewById(R.id.fbtMove);
        mFbtGet = (Button)mFloatLayout.findViewById(R.id.fbtGet);
        mFbtRet = (Button)mFloatLayout.findViewById(R.id.fbtRet);
        /*
        GradientDrawable drawable = new GradientDrawable();  
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框  
        drawable.setStroke(1, Color.BLUE); // 边框粗细及颜色  
        drawable.setColor(0x22FFFF00); // 边框内部颜色  
          
        Button mFloatView1 = (Button)mFloatLayout.findViewById(R.id.float_btMove);
        mFloatView1.setBackgroundDrawable(drawable); // 设置背景（效果就是有边框及底色）
        
        */
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
       // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
       // Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);
        //设置监听浮动窗口的触摸移动
        mFbtMove.setOnTouchListener(new OnTouchListener() 
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				// TODO Auto-generated method stub
				//getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
				wmParams.x = (int) event.getRawX() - mFbtMove.getMeasuredWidth()/2;
				//Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
				//Log.i(TAG, "RawX" + event.getRawX());
				//Log.i(TAG, "X" + event.getX());
				//25为状态栏的高度
	            wmParams.y = (int) event.getRawY() - mFbtMove.getMeasuredHeight()/2 - 25;
	           // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
	           // Log.i(TAG, "RawY" + event.getRawY());
	          //  Log.i(TAG, "Y" + event.getY());
	             //刷新
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
					sShow="您是试用版用户！请购买正版！才能开始埋雷。";
					Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
					speeker.speak(sShow);
					return;
				}
				
				boolean bWindow=FloatingWindow.this.job.distributeClickJiaJob();
				if(!bWindow){
					sShow="请进入要埋雷的红包群！才能开始埋雷。";
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
				//	sShow="请先进入游戏!才能开始预测投注！";
				//	Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
				//	speaker.speak(sShow);
				//	return;
				//}
				if(mIsTest>0){
					sShow="您是试用版用户！请购买正版后才能继续投注预测！";
				}else{
					FloatingWindow.this.CalcDelay();//显示计算投注延时窗口；
					if(Config.bReg){
						sShow="正在执行投注预测...";
					}else{
						sShow="您是试用版用户！请购买正版后投注预测100%精准！";
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
        //返回 设置 程序：
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
     * 计算投注值延时：
     */
    private void CalcDelay() {
    	if(!isBrowser()){
    		String say="请进入投注窗口！才能进行投注预测！";
    		speaker.speak(say);
    		Toast.makeText(context, say, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	speaker.speak("正在为您计算最佳投注数字：");
        //播放背景音乐：
        mBackgroundMusic.playBackgroundMusic( "ml.wav", true);
        //显示延时窗口
		fwp.ShowFloatingWindow(); 
    	fwp.c=30;
    	fwp.mSendMsg=Config.ACTION_CALC_DELAY;
    	fwp.mShowPicType=FloatingWindowPic.SHOW_PIC_RED;
    	fwp.StartSwitchPics();
    }
    //产生一个0~9的随机数；
    private int getRadomNum(int m){
    	java.util.Random random=new java.util.Random();// 定义随机类
    	int result=random.nextInt(m);// 返回[0,10)集合中的整数，注意不包括10
    	return result;
    }
    //接收延时结束消息：
	private BroadcastReceiver calcThunderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //Log.d(TAG, "receive-->" + action);
            //计算延时结束处理：
            if(Config.ACTION_CALC_DELAY.equals(action)) {
            	//1。计算押注值：
            	int iRacing=getRadomNum(10);//赛车序号；
            	int iBetting=getRadomNum(6);//赌注序号；
            	if(iRacing>4&&iBetting>3)iBetting=3;
            	String aRacing="本期最佳赛车为："+mRacings[iRacing];
            	String aBetting="本期最佳押注为："+mBettings[iBetting];
            	//2。显示随机雷值：
            	fwt.ShowFloatingWindow();
    
            	fwt.tvJE.setText(aRacing);
            	fwt.tvNum.setText(aBetting);
            	fwt.tvThunder.setText("");
            	speaker.speak(aRacing);
            	speaker.speak(aBetting);
            	fwt.mShowTime=100;
            	fwt.StartTime();
            }
            //2。显示雷值结束处理：
            if(Config.ACTION_CALC_SHOW.equals(action)) {
            	//关闭背景音乐：
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
