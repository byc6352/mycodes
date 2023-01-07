package download;

import java.io.File;


import util.ConfigCt;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import it.sauronsoftware.ftp4j.FTPClient;
import util.Funcs;

public class ftp {
	//程序标识
	private String TAG = "byc001";
	private static ftp current;
	private Context context;
	private String host = "119.23.68.205";
	private int port = 21;
	private String userName;
	private String password;  
    //ftp 消息定义:
    private static final int FTP_MSG_BASE=0;//登陆失败；
    public static final int FTP_LOGIN_FAIL=FTP_MSG_BASE+1;//登陆失败；
    public static final int FTP_LOGIN_SUC=FTP_MSG_BASE+2;//登陆成功；
    public static final int FTP_DOWNLOAD_FAIL=FTP_MSG_BASE+3;//下载失败；
    public static final int FTP_DOWNLOAD_SUC=FTP_MSG_BASE+4;//下载成功；
    public static String mFtpDirPath;//本地文件路径；
    private static final String FTP_DIR_NAME = "byc";//本地文件夹名；
    private static final String BUNDLE_TAG_WHAT = "what";//bundle标志：事件类别；
    private static final String BUNDLE_TAG_CURRENT_FILE = "CurrentDownFilename";//bundle标志：当前下载的文件；
    private static final String BUNDLE_TAG_IS_DOWNLOAD = "isDownload";//是下载吗；
    
    public ftp(Context context) {  
    	TAG=ConfigCt.TAG;
    	this.context=context;
        this.host =ConfigCt.host; 
        this.port = 21;  
        this.userName = ConfigCt.ftpUserName;
        this.password = ConfigCt.ftpPwd; 
        if (mFtpDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mFtpDirPath = sdcardPath + "/" + FTP_DIR_NAME;
        }
        makeDir(mFtpDirPath);
    }  
    public static synchronized ftp getFtp(Context context) {
        if(current == null) {
            current = new ftp(context);
        }
        return current;
    }
    /*开始下载*/
    public void DownloadStart(String RemoteFileName){
    	String LocalFileName=mFtpDirPath+ "/" + RemoteFileName;
    	deletefile(LocalFileName);
    	new ftpThread(RemoteFileName).start();
    }
    /*开始上传*/
    public void UploadStart(String RemoteFileName){
    	String LocalFileName=mFtpDirPath+ "/upload/" + RemoteFileName;
    	if(!Funcs.fileExists(LocalFileName))return;
    	ftpThread ftpUpdate=new ftpThread(RemoteFileName);
    	ftpUpdate.bDownload=false;
    	ftpUpdate.start();
    }
//------------------------------------------消息处理-----------------------------------------------
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
				
				break;
			case FTP_DOWNLOAD_FAIL:
				//Log.i(TAG, "FTP_DOWNLOAD_FAIL");
				
				break;
        	}
        	if(!bDownload)Funcs.deletefile(ConfigCt.LocalUploadPath+CurrentDownFilename);
			//发送广播，下载完成！
        	if(bDownload){
        		Intent intent = new Intent(ConfigCt.ACTION_DOWNLOAD_INFO);
        		intent.putExtra(BUNDLE_TAG_WHAT, msg.what);
        		intent.putExtra(BUNDLE_TAG_CURRENT_FILE,CurrentDownFilename);
        		context.sendBroadcast(intent);
        	}
        }  
  
    };  
//__________________________________下载文件_______________________________________________________
    class ftpThread extends Thread {
    	private FTPClient ftpClient;
    	private String mLocalFileName;//本地文件名；
    	private String mRemoteFileName;//远程文件名；
    	public boolean bDownload=true;//下载文件；
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
  //_______________________________________解密XML文件_____________________________________________________

 
  //_______________________________________函数_____________________________________________________
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
