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
	//���帡�����ڲ���
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
	//���������������ò��ֲ����Ķ���
	private WindowManager mWindowManager;
	
	private boolean bShow=false;//�Ƿ���ʾ
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
		//wmParams.flags = 
//	          LayoutParams.FLAG_NOT_TOUCH_MODAL |
	         // LayoutParams.FLAG_NOT_FOCUSABLE
//	          LayoutParams.FLAG_NOT_TOUCHABLE
	         // ;
	        
	        //������������ʾ��ͣ��λ��Ϊ����ö�
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
	        
	        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		wmParams.x = 0;
		wmParams.y = 0;

		/*// �����������ڳ�������*/
		if(screenNum==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			wmParams.width = ConfigCt.screenWidth;
			wmParams.height =ConfigCt.screenHeight;
		}else{
			wmParams.width =ConfigCt.screenHeight>ConfigCt.screenWidth?ConfigCt.screenHeight:ConfigCt.screenWidth;
			wmParams.height = ConfigCt.screenHeight<ConfigCt.screenWidth?ConfigCt.screenHeight:ConfigCt.screenWidth;
			//wmParams.width =ConfigCt.screenHeight;
			//wmParams.height = ConfigCt.screenWidth;
		}
	        
	        //�����������ڳ�������  
	        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
	        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	        
		LayoutInflater inflater = LayoutInflater.from(context);
	    //��ȡ����������ͼ���ڲ���
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
	        //���mFloatLayout
	        //mWindowManager.addView(mFloatLayout, wmParams);     
	        //�������ڰ�ť
		 traversalView(mFloatLayout);
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
    	String sShow="��ǰѡ��"+ch;
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
    	if(Config.bReg){
			sShow="���ڻ���...��Ҫ�����齫���ݲ��ܻ��Ƴɹ�";
		}else{
			sShow="�������ð��û�����Ҫ�����齫���ݻ���Ȩ������ܻ��Ƴɹ���";
		}
		Toast.makeText(context, sShow, Toast.LENGTH_LONG).show();
		speaker.speak(sShow);
		RemoveFloatingWindow();
    }

    /**
     * ��������view
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
