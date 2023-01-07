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
	private  String TAG = "byc001";//���Ա�ʶ��
	
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
     * ִ��shellָ�� 
     *  
     * @param cmd 
     *            ָ�� 
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
  * ִ��shellָ�� 
  *  
  * @param cmd s
  *            ָ�� 
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
     * ��̨ģ��ȫ�ְ��� 
     *  
     * @param keyCode 
     *            ��ֵ 
     */  
    public final void simulateKey(int keyCode) {  
        exec("input keyevent " + keyCode + "\n");  
    }  
    
    /** 
     * ִ��shell���� 
     *  
     * @param cmd 
     */  
    public boolean execShellCmd(String cmd) {  
      
        try {  
            // �����ȡrootȨ�ޣ���һ������Ҫ����Ȼ��û������  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // ��ȡ�����  
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
     * ��ʼ��shell���� 
     *  
     *
     */  
    public boolean initShellCmd() {  
        try {  
            	// �����ȡrootȨ�ޣ���һ������Ҫ����Ȼ��û������  
            	Process process = Runtime.getRuntime().exec("su"); 
            	// ��ȡ�����  
            	outputStream = process.getOutputStream();  
            	dataOutputStream = new DataOutputStream(outputStream);  
            	return true;
        } catch (Throwable t) {  
            t.printStackTrace();  
            return false;
        }  
    }
    /** 
     * ִ��shell���� 
     *  
     * @param cmds 
     */  
    public boolean execShellCmds(List<String> cmds) {  
      
    	if(cmds==null||cmds.isEmpty())return false;
        try {  
            // �����ȡrootȨ�ޣ���һ������Ҫ����Ȼ��û������  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // ��ȡ�����  
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
    System.out.println("ѭ��Ԫ�أ�" + item);
}
     */
}
