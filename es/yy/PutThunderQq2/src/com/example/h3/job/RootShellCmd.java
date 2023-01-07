/**
 * 
 */
package com.example.h3.job;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import com.example.h3.Config;

import android.util.Log;



/**
 * @author byc
 *
 */
public class RootShellCmd {
	
	private static RootShellCmd current;
	private  String TAG = "byc001";//调试标识：
	
	private OutputStream os;  
	private Process process;
	OutputStream outputStream ;
	InputStream inputStream ; 
	DataOutputStream dataOutputStream;
	BufferedReader bufferReader;
	BufferedReader error;
	
	public RootShellCmd() {
		TAG=Config.TAG;
     
	}
    public static synchronized RootShellCmd getRootShellCmd() {
        if(current == null) {
            current = new RootShellCmd();
        }
        return current;
    }
	   /** 
     * 执行shell指令 
     *  
     * @param cmd 
     *            指令 
     */  
    public final void exec(String cmd) {  
        try {  
            if (os == null) {  
                os = Runtime.getRuntime().exec("su").getOutputStream();  
            }  
            os.write(cmd.getBytes());  
            os.flush(); 
            os.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 后台模拟全局按键 
     *  
     * @param keyCode 
     *            键值 
     */  
    public final void simulateKey(int keyCode) {  
        exec("input keyevent " + keyCode + "\n");  
    }  
    
    /** 
     * 执行shell命令 
     *  
     * @param cmd 
     */  
    public boolean execShellCmd(String cmd) {  
      
        try {  
            // 申请获取root权限，这一步很重要，不然会没有作用  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // 获取输出流  
                outputStream = process.getOutputStream();  
                dataOutputStream = new DataOutputStream(outputStream);  
            }  
            
            dataOutputStream.writeBytes(cmd);  
            dataOutputStream.flush();  
            Log.i(TAG, "cmd2："+cmd);
            dataOutputStream.close();  
            outputStream.close();  
            return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    } 
    /** 
     * 初始化shell命令 
     *  
     * @param cmd 
     */  
    public boolean initShellCmd() {  
      
        try {  
            // 申请获取root权限，这一步很重要，不然会没有作用  
            	Process process = Runtime.getRuntime().exec("su"); 
                // 获取输出流  
                outputStream = process.getOutputStream();  
                dataOutputStream = new DataOutputStream(outputStream);  
                inputStream = process.getInputStream();
                bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                error=new BufferedReader(new InputStreamReader(process.getErrorStream()));

            return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    }
    /** 
     * 执行一组shell命令 
     *  
     * @param cmds 
     */  
    public boolean execShellCmds(List<String> cmds) {  
      
        try {  
            // 申请获取root权限，这一步很重要，不然会没有作用  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // 获取输出流  
                outputStream = process.getOutputStream();
                inputStream = process.getInputStream();
                bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                error=new BufferedReader(new InputStreamReader(process.getErrorStream()));

                dataOutputStream = new DataOutputStream(outputStream);  
            }  
            for(String cmd:cmds){
            	dataOutputStream.writeBytes(cmd+";"); 
            	dataOutputStream.flush();
            	Log.i(TAG, "cmd："+cmd);
            }
            dataOutputStream.close();
            if(process!=null)process.waitFor();else Log.i(TAG, "process is null");
            String line="";
            while ((line = error.readLine()) != null) {
                Log.i(TAG,line);
            }
             while ((line = bufferReader.readLine()) != null) {
             	Log.i(TAG,line);
             } 
            outputStream.close(); 
            bufferReader.close();
            error.close();
            return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    } 
}
