/**
 * 
 */
package com.byc.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author byc
 *
 */
public class SocketHelper {
	 public static boolean connectServer(Socket socket,InetSocketAddress addr){
		 try {
				//if(this.socket ==null)return false;
				//socket.connect(addr);
				if(socket !=null&&socket.isClosed()==false){socket.close();socket=null;}
				socket= new Socket();
				socket.connect(addr, 5000);
				if(socket.isConnected()){

	                return true;
				}
				return  false;

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	 }
	 /*
	  * ��ʱ��
	  */
	 public static void sleep(int MilliSecond) {
		 try{
			 	Thread.sleep(MilliSecond);
		   	}catch(InterruptedException e){
		   		e.printStackTrace();
		   	}catch(IllegalArgumentException e){
		   		e.printStackTrace();
		   	}  
	 }
	 /*
	  * �����ݣ�
	  */
	 public static int read(InputStream in,byte[] data,int off,int len) {
		 try{
			 int i=in.read(data, off, len);
			 return i;
		 }catch(IOException e){
		   		e.printStackTrace();
		   		return -1;
		   	} 
	 }
	 /*
	  * ѭ�������ݣ�
	  */
	 private static int RecycleRead(InputStream in,byte[] data,int off,int len) {
		 int i=read(in,data,off,len);
		 int j=i;
		 if(i==-1)return i;
		 while(j<len){
			 i=read(in,data,off+i,len); 
			 if(i==-1)return i;
			 j=j+i;
		 }
		 return j;
	 }
	 /*
	  * �������ݣ�
	  */
	 public static void SendData(OutputStream out,byte[] data){
		 try{
			 out.write(data);  
             out.flush(); 
		 }catch (IOException e) {
             e.printStackTrace();
         }//try{
	 }
	 /*
	  * ���հ�ͷ��
	  */
	 private static boolean recvPH(InputStream in,byte[] ph) {
		 int count=read(in,ph,0,ph.length);//����ͷ;
		 if(count!=ph.length){return false;}
		 return true;
	 }
	 /*
	  * �õ�����ͷ��
	  */
	 private static boolean PH2OH(byte[] ph,OrderHeader oh) {
		 oh=order.formatPHtoOH(ph,oh);//�õ�����ͷ��
		 if(order.VerifyOH(oh))return true;else return false;
	 }
	 /*
	  * �õ������������
	  */
	 public static OutputStream getOut(Socket socket) {
		 try{
			 OutputStream out=socket.getOutputStream();
			 return out;
		 }catch(IOException e){
			 e.printStackTrace();
			 return null;
		 }
	 }
	 /*
	  * �õ�������������
	  */
	 public static InputStream getIn(Socket socket) {
		 try{
			 InputStream in=socket.getInputStream();
			 return in;
		 }catch(IOException e){
			 e.printStackTrace();
			 return null;
		 }
	 }
	 /*
	  * �ͷ��ڴ棺
	  */
	 public static void ReleaseSocket(Socket socket,InputStream in,OutputStream out) {
		 try{
			 if(in!=null)in.close();
			 if(out!=null)out.close();
			 if(socket!=null&&socket.isClosed()==false)socket.close();
			 in=null;
			 out=null;
			 socket=null;
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
	 
}
