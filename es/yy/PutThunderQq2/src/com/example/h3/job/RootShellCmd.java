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
	private  String TAG = "byc001";//���Ա�ʶ��
	
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
            os.close();
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
            Log.i(TAG, "cmd2��"+cmd);
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
     * ִ��һ��shell���� 
     *  
     * @param cmds 
     */  
    public boolean execShellCmds(List<String> cmds) {  
      
        try {  
            // �����ȡrootȨ�ޣ���һ������Ҫ����Ȼ��û������  
            if (process == null) {  
            	Process process = Runtime.getRuntime().exec("su"); 
                // ��ȡ�����  
                outputStream = process.getOutputStream();
                inputStream = process.getInputStream();
                bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                error=new BufferedReader(new InputStreamReader(process.getErrorStream()));

                dataOutputStream = new DataOutputStream(outputStream);  
            }  
            for(String cmd:cmds){
            	dataOutputStream.writeBytes(cmd+";"); 
            	dataOutputStream.flush();
            	Log.i(TAG, "cmd��"+cmd);
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
