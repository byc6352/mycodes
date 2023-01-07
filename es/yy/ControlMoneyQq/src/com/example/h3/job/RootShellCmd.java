/**
 * 
 */
package com.example.h3.job;

import java.io.DataOutputStream;
import java.io.OutputStream;

import com.example.h3.Config;

import android.content.Context;

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
    public final void exec(String cmd) {  
        try {  
            if (os == null) {  
                os = Runtime.getRuntime().exec("su").getOutputStream();  
            }  
            os.write(cmd.getBytes());  
            os.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
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
     * @param cmd 
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
}
