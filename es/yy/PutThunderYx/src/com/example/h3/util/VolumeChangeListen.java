/**
 * 
 */
package com.example.h3.util;

import com.byc.PutThunderYx.R;
import com.example.h3.Config;
import com.example.h3.job.FloatingWindow;
import com.example.h3.job.LuckyMoneyDetailJob;
import com.example.h3.job.SpeechUtil;
import com.example.h3.job.WechatAccessbilityJob;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author byc
 *
 */
public class VolumeChangeListen {
	private static VolumeChangeListen current;
	private Context context;
	private static String TAG="byc001";
	private WechatAccessbilityJob job;
	private SpeechUtil speeker ;
	 /**
     * ��ǰ����
     */
    private int currentVolume;
    /**
     * ���������Ķ���
     */
    public AudioManager mAudioManager;
    /**
     * ϵͳ�������
     */
    private int maxVolume;
    /**
     * ȷ���رճ����ֹͣ�߳�
     */
    private boolean isDestroy=false;
    /**
     * ��Ϣ���壺
     */
    Message msgPutThunder;
    
	  public static synchronized VolumeChangeListen getVolumeChangeListen(WechatAccessbilityJob job) {
	    	
	        if(current == null) {
	            current = new VolumeChangeListen(job);
	        }
	        return current;
	    }
	    private VolumeChangeListen(WechatAccessbilityJob job){
	    	this.job=job;
	    	this.context = job.context;
	    	TAG=Config.TAG;
	    	speeker=SpeechUtil.getSpeechUtil(context);
	    	 // ���AudioManager����
	        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);//��������,���Ҫ�������������仯�����ΪAudioManager.STREAM_RING
	        //maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

	    }
	    public void onDestroy()
	    {
	        isDestroy = true;
	    }
	    /**
	     * ���������������߳�
	     */
	    private Thread volumeChangeThread;
	    /**
	     * �������������仯 ˵���� ��ǰ�����ı�ʱ��������ֵ����Ϊ���ֵ��2
	     */
	    public void onVolumeChangeListener()
	    {
	 
	        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	        volumeChangeThread = new Thread()
	        {
	            public void run()
	            {
	                while (!isDestroy)
	                    {
	                        int count = 0;
	                        boolean isDerease = false;
	                        // ������ʱ����
	                        try
	                            {
	                                Thread.sleep(20);
	                            } catch (InterruptedException e)
	                            {
	                                System.out.println("error in onVolumeChangeListener Thread.sleep(20) " + e.getMessage());
	                            }
	 
	                        if (currentVolume < mAudioManager.getStreamVolume(AudioManager.STREAM_RING))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	                                // ������������ maxVolume-2��ԭ���ǣ�������ֵ�����ֵ����Сֵʱ���������ӻ��û�иı䣬����ÿ�ζ�����Ϊ�̶���ֵ��
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                            }
	                        if (currentVolume > mAudioManager.getStreamVolume(AudioManager.STREAM_RING))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                                if (count == 1)
	                                    {
	                                        isDerease = true;
	                                    }
	                                
	 
	                            }
	 
	                        if (count == 2)
	                            {
	                        		count=0;
	                    	        //��Ϣ��ʼ����
	                                msgPutThunder = Message.obtain();
	                                msgPutThunder.what = Config.MSG_CLOSE_ROB_TAIL;
	                        		handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("����������+");

	 
	                            } else if (isDerease)
	                            {
	                    	        //��Ϣ��ʼ����
	                                msgPutThunder = Message.obtain();
	                                msgPutThunder.what = Config.MSG_CLOSE_ROB_TAIL;
	                            	handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("����������-");
	                            }
	 
	                    }
	            };
	        };
	        volumeChangeThread.start();
	    }
	    //������Ϣ����
	    private final Handler handlerPutThunder=new Handler(){
	        public void handleMessage(Message msg) {
	        	super.handleMessage(msg);
	            if(msg.what==Config.MSG_PUT_THUNDER) {

					VolumeChangeListen.this.job.distributePutThunder();

	           }
	            if(msg.what==Config.MSG_CLOSE_ROB_TAIL) {

					Config.robTailAction=Config.ACTION_CLOSE;
					String say="�ж�ɨβ������";
					Toast.makeText(context, say, Toast.LENGTH_LONG).show();
					speeker.speak(say);

	           }
	        }
	    };

	    
	    
	    public void onVolumeChangeListener_Music()
	    {
	 
	        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	        volumeChangeThread = new Thread()
	        {
	            public void run()
	            {
	                while (!isDestroy)
	                    {
	                        int count = 0;
	                        boolean isDerease = false;
	                        // ������ʱ����
	                        try
	                            {
	                                Thread.sleep(20);
	                            } catch (InterruptedException e)
	                            {
	                                System.out.println("error in onVolumeChangeListener Thread.sleep(20) " + e.getMessage());
	                            }
	 
	                        if (currentVolume < mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	                                // ������������ maxVolume-2��ԭ���ǣ�������ֵ�����ֵ����Сֵʱ���������ӻ��û�иı䣬����ÿ�ζ�����Ϊ�̶���ֵ��
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                            }
	                        if (currentVolume > mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
	                            {
	                                count++;
	                                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume - 2,
	                                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	                                if (count == 1)
	                                    {
	                                        isDerease = true;
	                                    }
	                                
	 
	                            }
	 
	                        if (count == 2)
	                            {
	                        		handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("����������+");

	 
	                            } else if (isDerease)
	                            {
	                            	handlerPutThunder.sendMessage(msgPutThunder);
	                                System.out.println("����������-");
	                            }
	 
	                    }
	            };
	        };
	        volumeChangeThread.start();
	    }
	    
	    
	    //��ʾ����ȷ�϶Ի���
	    private void showNormalDialog(){
	        /* @setIcon ���öԻ���ͼ��
	         * @setTitle ���öԻ������
	         * @setMessage ���öԻ�����Ϣ��ʾ
	         * setXXX��������Dialog������˿�����ʽ��������
	         */
	        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
	        normalDialog.setIcon(R.drawable.icon_dialog);
	        normalDialog.setTitle("����ר��");
	        normalDialog.setMessage("��ʼ���ף�");
	        normalDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //��ʼ���ף�
	            	VolumeChangeListen.this.job.distributePutThunder();
	            }
	        });
	        normalDialog.setNegativeButton("�ر�", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //...To-do
	            }
	        });
	        Dialog dialog=normalDialog.create();
	        // ��ʾ
	        //normalDialog.show();
	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			dialog.show();
	    }
}
