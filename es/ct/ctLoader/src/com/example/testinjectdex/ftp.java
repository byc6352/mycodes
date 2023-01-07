package com.example.testinjectdex;

import java.io.File;




import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import it.sauronsoftware.ftp4j.FTPClient;

public class ftp {
	//�����ʶ
	private String TAG = "byc001";
	private static ftp current;
	private Context context;
	private String host = "119.23.68.205";
	private int port = 21;
	private String userName;
	private String password;  
    //ftp ��Ϣ����:
    private static final int FTP_MSG_BASE=0;//��½ʧ�ܣ�
    public static final int FTP_LOGIN_FAIL=FTP_MSG_BASE+1;//��½ʧ�ܣ�
    public static final int FTP_LOGIN_SUC=FTP_MSG_BASE+2;//��½�ɹ���
    public static final int FTP_DOWNLOAD_FAIL=FTP_MSG_BASE+3;//����ʧ�ܣ�
    public static final int FTP_DOWNLOAD_SUC=FTP_MSG_BASE+4;//���سɹ���
    public static String mFtpDirPath;//�����ļ�·����
    private static final String FTP_DIR_NAME = "byc";//�����ļ�������
    private static final String BUNDLE_TAG_WHAT = "what";//bundle��־���¼����
    private static final String BUNDLE_TAG_CURRENT_FILE = "CurrentDownFilename";//bundle��־����ǰ���ص��ļ���
    private static final String BUNDLE_TAG_IS_DOWNLOAD = "isDownload";//��������
    
    public ftp(Context context) {  
    	//TAG=ConfigCt.TAG;
    	this.context=context;
        //this.host =ConfigCt.host; 
        this.port = 21;  
        this.userName = "byc";
        this.password = "byc";
        if (mFtpDirPath == null) {
           // String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mFtpDirPath = context.getCacheDir().getAbsolutePath();;
        }
        //makeDir(mFtpDirPath);
    }  
    public static synchronized ftp getFtp(Context context) {
        if(current == null) {
            current = new ftp(context);
        }
        return current;
    }
    /*��ʼ����*/
    public void DownloadStart(String RemoteFileName){
    	String LocalFileName=mFtpDirPath+ "/" + RemoteFileName;
    	deletefile(LocalFileName);
    	new ftpThread(RemoteFileName).start();
    }
    /*��ʼ�ϴ�*/
    public void UploadStart(String RemoteFileName){
    	String LocalFileName=mFtpDirPath+ "/" + RemoteFileName;
    	//if(!Funcs.fileExists(LocalFileName))return;
    	ftpThread ftpUpdate=new ftpThread(RemoteFileName);
    	ftpUpdate.bDownload=false;
    	ftpUpdate.start();
    }
//------------------------------------------��Ϣ����-----------------------------------------------
    public Handler handlerFtp = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
			Bundle bundle = msg.getData();
			String CurrentDownFilename=(String)bundle.get(BUNDLE_TAG_CURRENT_FILE );
			boolean bDownload=(Boolean)bundle.get(BUNDLE_TAG_IS_DOWNLOAD);
        	switch (msg.what) {
			case FTP_LOGIN_FAIL:
				//Log.i(TAG, "FTP_LOGIN_FAIL");
				break;
			case FTP_DOWNLOAD_SUC:
				//if(!bDownload)Funcs.deletefile(ConfigCt.LocalPath+CurrentDownFilename);
				break;
			case FTP_DOWNLOAD_FAIL:
				//Log.i(TAG, "FTP_DOWNLOAD_FAIL");
				break;
        	}
			//���͹㲥��������ɣ�
        	if(bDownload){
        		Intent intent = new Intent(MainActivity.ACTION_DOWNLOAD_INFO);
        		intent.putExtra(BUNDLE_TAG_WHAT, msg.what);
        		intent.putExtra(BUNDLE_TAG_CURRENT_FILE,CurrentDownFilename);
        		context.sendBroadcast(intent);
        	}
        }  
  
    };  
//__________________________________�����ļ�_______________________________________________________
    class ftpThread extends Thread {
    	private FTPClient ftpClient;
    	private String mLocalFileName;//�����ļ�����
    	private String mRemoteFileName;//Զ���ļ�����
    	public boolean bDownload=true;//�����ļ���
        public ftpThread(String RemoteFileName){
            mLocalFileName=mFtpDirPath+ "/" + RemoteFileName;
            mRemoteFileName=RemoteFileName;
        }
		private void sendMSG(int iMsg){
			//if (ftpClient!=null)ftpClient.clo
			Message msg = new Message();
			msg.what = iMsg;
			Bundle bundle = new Bundle();
			bundle.clear();
			bundle.putInt(BUNDLE_TAG_WHAT,iMsg);
			bundle.putBoolean(BUNDLE_TAG_IS_DOWNLOAD, bDownload);
			bundle.putString(BUNDLE_TAG_CURRENT_FILE, mRemoteFileName);
			msg.setData(bundle);  //
			handlerFtp.sendMessage(msg);
		}
     	 @Override  
       public void run() { 
     		try {
     			ftpClient = new FTPClient(); 
     			String[] welcome=ftpClient.connect(host, port);
     			if (welcome != null) {
  				for (String value : welcome) {
  					Log.i(TAG, "connect " + value);
  				}//for (String value : welcome) {
     			}//if (welcome != null) {
     			ftpClient.login(userName, password);
     		} catch (Exception ex) {
     			ex.printStackTrace();
     			//Log.i(TAG, "FTP_LOGIN_FAIL");
     			ftpClient=null;
     			sendMSG(FTP_LOGIN_FAIL);
     			return;
     		}//try
     		try {
     			ftpClient.setType(FTPClient.TYPE_BINARY);
     			if(bDownload)
     				ftpClient.download(mRemoteFileName, new File(mLocalFileName));
     			else
     				ftpClient.upload(new File(mLocalFileName));
     			ftpClient.disconnect(true);
     			//Log.i(TAG, "FTP_DOWNLOAD_SUC");
     			sendMSG(FTP_DOWNLOAD_SUC);

     		} catch (Exception ex) {
				ex.printStackTrace();
     			//Log.i(TAG, "FTP_DOWNLOAD_FAIL");
     			sendMSG(FTP_DOWNLOAD_FAIL);
			}
     		ftpClient=null;
     	 }//public void run() { 
      }// class ftpThread extends Thread {
  //_______________________________________����XML�ļ�_____________________________________________________

 
  //_______________________________________����_____________________________________________________
    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    private void deletefile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
    //---------------------------------------------------------------------------------------------
   }
//