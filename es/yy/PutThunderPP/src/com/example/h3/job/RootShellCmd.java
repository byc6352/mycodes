/**
 * 
 */
package com.example.h3.job;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;

import com.baidu.android.common.logging.Log;
import com.example.h3.Config;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

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
	DataOutputStream dataOutputStream;
	
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
    public final boolean exec(String cmd) {  
        try {  
            if (os == null) {  
                os = Runtime.getRuntime().exec("su").getOutputStream();  
            }  
            os.write(cmd.getBytes());  
            os.flush();  
            return true;
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;
        }  
    }  
	   /** 
  * 执行shell指令 
  *  
  * @param cmd s
  *            指令 
  */  
 public final boolean execs(List<String> cmds) {  
     for(String cmd : cmds){
     	AccessibilityHelper.Sleep(500);
     	Log.i(TAG, cmd);
     	if(!exec(cmd))return false;
     }
     return true;
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
     *
     */  
    public boolean initShellCmd() {  
        try {  
            	// 申请获取root权限，这一步很重要，不然会没有作用  
            	Process process = Runtime.getRuntime().exec("su"); 
            	// 获取输出流  
            	outputStream = process.getOutputStream();  
            	dataOutputStream = new DataOutputStream(outputStream);  
            	return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    }
    /** 
     * 执行shell命令 
     *  
     * @param cmds 
     */  
    public boolean execShellCmds(List<String> cmds) {  
      
    	if(cmds==null||cmds.isEmpty())return false;
        try {  
            // 申请获取root权限，这一步很重要，不然会没有作用  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // 获取输出流  
                outputStream = process.getOutputStream();  
                dataOutputStream = new DataOutputStream(outputStream);  
            }  
            for(String cmd : cmds){
            	AccessibilityHelper.Sleep(500);
            	Log.i(TAG, cmd);
            	dataOutputStream.writeBytes(cmd);  
            	dataOutputStream.flush(); 
            }
            dataOutputStream.close();  
            outputStream.close();  
            return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    } 
    /*
     * List<String> list = new ArrayList<String>();
for(String item : list){
    System.out.println("循环元素：" + item);
}
     */
}
