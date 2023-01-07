/**
 * 
 */
package com.example.h3;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import util.ConfigCt;
import util.SpeechUtil;

/**
 * @author ASUS
 *
 */
public class FloatingWindowSelCard implements View.OnClickListener {
	private static FloatingWindowSelCard current;
	private Context context;
	//定义浮动窗口布局
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
	//创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;
	
	private boolean bShow=false;//是否显示
	private SpeechUtil speaker ;
	//-----------------------------------------------------------------------------
	private FloatingWindowSelCard(Context context) {
		this.context = context;
		speaker=SpeechUtil.getSpeechUtil(context);
		createFloatView();
	}
	 public static synchronized FloatingWindowSelCard getInstance(Context context) {
	        if(current == null) {
	            current = new FloatingWindowSelCard(context);
	        }
	        return current;
	}
	public void ShowFloatingWindow(){
	    	if(!bShow){
	    		
	    		 mWindowManager.addView(mFloatLayout, wmParams);
	    		bShow=true;
	    	}
	}
	private void createFloatView(){
		int screenNum = context.getResources().getConfiguration().orientation;
		wmParams = new WindowManager.LayoutParams();
			//获取WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
			//设置window type
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
			wmParams.type = LayoutParams.TYPE_TOAST; 
		else
			wmParams.type = LayoutParams.TYPE_PHONE; 
			//设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888; 
	        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		//wmParams.flags = 
//	          LayoutParams.FLAG_NOT_TOUCH_MODAL |
	         // LayoutParams.FLAG_NOT_FOCUSABLE
//	          LayoutParams.FLAG_NOT_TOUCHABLE
	         // ;
	        
	        //调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
	        
	        // 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 0;
		wmParams.y = 0;

		/*// 设置悬浮窗口长宽数据*/
		if(screenNum==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			wmParams.width = ConfigCt.screenWidth;
			wmParams.height =ConfigCt.screenHeight;
		}else{
			wmParams.width =ConfigCt.screenHeight>ConfigCt.screenWidth?ConfigCt.screenHeight:ConfigCt.screenWidth;
			wmParams.height = ConfigCt.screenHeight<ConfigCt.screenWidth?ConfigCt.screenHeight:ConfigCt.screenWidth;
			//wmParams.width =ConfigCt.screenHeight;
			//wmParams.height = ConfigCt.screenWidth;
		}
	        
	        //设置悬浮窗口长宽数据  
	        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
	        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	        
		LayoutInflater inflater = LayoutInflater.from(context);
	    //获取浮动窗口视图所在布局
		int layoutID=0;
		if(screenNum==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			//mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_sel_card, null);
			layoutID=util.ResourceUtil.getLayoutId(context, "float_sel_card"); 
		}
		else{
			//mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_sel_card_land, null);
			layoutID=util.ResourceUtil.getLayoutId(context, "float_sel_card_land");
		}
		 mFloatLayout = (LinearLayout) inflater.inflate(layoutID, null);
	        //添加mFloatLayout
	        //mWindowManager.addView(mFloatLayout, wmParams);     
	        //浮动窗口按钮
		 traversalView(mFloatLayout);
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
	}
    /*
     * (non-Javadoc)
     * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
     */
    @Override
    public void onClick(View v) {
    	Log.i(ConfigCt.TAG, "v:click");
    	if(v.getContentDescription()==null)return;
    	String ch=v.getContentDescription().toString();;
    	String sShow="当前选择："+ch;
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
    	if(Config.bReg){
			sShow="正在换牌...需要计算麻将数据才能换牌成功";
		}else{
			sShow="您是试用版用户！需要计算麻将数据或授权正版才能换牌成功！";
		}
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
		RemoveFloatingWindow();
    }

    /**
     * 遍历所有view
     * 
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
            	if(view instanceof ImageButton )
            		view.setOnClickListener(this);
            }
        }
    }
	/*
	 * 
	 * */
	public void RemoveFloatingWindow(){
		if(mFloatLayout != null){
			if(bShow)mWindowManager.removeView(mFloatLayout);
			bShow=false;
		}
		//Destroy();
	}
	/*
	 * 
	 * */
	public void Destroy(){
		wmParams=null;
		mFloatLayout=null;
		mWindowManager=null;
		current=null;
	}
}
