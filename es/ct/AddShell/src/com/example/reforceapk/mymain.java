package com.example.reforceapk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;


public class mymain {
	private static final String CLASSES_DEX="classes.dex";
	private static final String CLASSES_DEX_0="classes0.dex";
	private static final String WORK_DIR="force";//工作目录；
	private static String shellFile=CLASSES_DEX;//壳文件；
	private static String shellForApkFile=CLASSES_DEX;//已加壳的classes.dex文件；
	private static String apkFile="";//要加壳的APK文件；
	private static String hostFile="";//宿主APK文件；
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(!SearchFile()){System.out.println("查找文件失败！");return;}//查找文件；
		System.out.println("");
		System.out.println("查找文件成功！");
		if(!addSellToDex(apkFile,shellFile)){System.out.println("加壳失败！");return;};//加壳；
		System.out.println("");
		System.out.println("加壳成功！");
		if(!ZipUtil2.delFileFromZip(hostFile, CLASSES_DEX)){System.out.println("删除宿主文件中的classes.dex失败！");return;};//删除宿主文件中的classes.dex
		System.out.println("");
		System.out.println("删除宿主文件中的classes.dex成功！");
		if(!ZipUtil2.addFileToZip(hostFile,shellForApkFile)){System.out.println("加壳后写入宿主文件失败！");return;};//加壳后写入宿主文件中;
		System.out.println("");
		System.out.println("加壳后写入宿主文件成功！");
	
	}
	
	//直接返回数据，读者可以添加自己加密方法
	private static byte[] encrpt(byte[] srcdata){
		for(int i = 0;i<srcdata.length;i++){
			srcdata[i] = (byte)(0xFE ^ srcdata[i]);
		}
		return srcdata;
	}

	/**
	 * 修改dex头，CheckSum 校验码
	 * @param dexBytes
	 */
	private static void fixCheckSumHeader(byte[] dexBytes) {
		Adler32 adler = new Adler32();
		adler.update(dexBytes, 12, dexBytes.length - 12);//从12到文件末尾计算校验码
		long value = adler.getValue();
		int va = (int) value;
		byte[] newcs = intToByte(va);
		//高位在前，低位在前掉个个
		byte[] recs = new byte[4];
		for (int i = 0; i < 4; i++) {
			recs[i] = newcs[newcs.length - 1 - i];
			System.out.println(Integer.toHexString(newcs[i]));
		}
		System.arraycopy(recs, 0, dexBytes, 8, 4);//效验码赋值（8-11）
		System.out.println(Long.toHexString(value));
		System.out.println();
	}


	/**
	 * int 转byte[]
	 * @param number
	 * @return
	 */
	public static byte[] intToByte(int number) {
		byte[] b = new byte[4];
		for (int i = 3; i >= 0; i--) {
			b[i] = (byte) (number % 256);
			number >>= 8;
		}
		return b;
	}

	/**
	 * 修改dex头 sha1值
	 * @param dexBytes
	 * @throws NoSuchAlgorithmException
	 */
	private static void fixSHA1Header(byte[] dexBytes)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(dexBytes, 32, dexBytes.length - 32);//从32为到结束计算sha--1
		byte[] newdt = md.digest();
		System.arraycopy(newdt, 0, dexBytes, 12, 20);//修改sha-1值（12-31）
		//输出sha-1值，可有可无
		String hexstr = "";
		for (int i = 0; i < newdt.length; i++) {
			hexstr += Integer.toString((newdt[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		System.out.println(hexstr);
	}

	/**
	 * 修改dex头 file_size值
	 * @param dexBytes
	 */
	private static void fixFileSizeHeader(byte[] dexBytes) {
		//新文件长度
		byte[] newfs = intToByte(dexBytes.length);
		System.out.println(Integer.toHexString(dexBytes.length));
		byte[] refs = new byte[4];
		//高位在前，低位在前掉个个
		for (int i = 0; i < 4; i++) {
			refs[i] = newfs[newfs.length - 1 - i];
			System.out.println(Integer.toHexString(newfs[i]));
		}
		System.arraycopy(refs, 0, dexBytes, 32, 4);//修改（32-35）
	}


	/**
	 * 以二进制读出文件内容
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static byte[] readFileBytes(File file) throws IOException {
		byte[] arrayOfByte = new byte[1024];
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		while (true) {
			int i = fis.read(arrayOfByte);
			if (i != -1) {
				localByteArrayOutputStream.write(arrayOfByte, 0, i);
			} else {
				return localByteArrayOutputStream.toByteArray();
			}
		}
	}
	/*
	 * 输入文件为：壳文件：shellFile,要加壳的apk文件:apkFile,宿主文件：hostFile
	 * 找到壳文件shellFile,并重新命名为：classes_0.dex;找到：apkFile,hostFile文件；
	 * @return 找到文件否？
	 * */
	private static boolean SearchFile(){
		File dir = new File(WORK_DIR);
		File fShellFile=new File(WORK_DIR+File.separator+CLASSES_DEX_0);//重新命名壳文件；
		if(!dir.exists())return false;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String filename=file.getName();
				if(filename.equals(CLASSES_DEX)){//重新命名壳文件；
					shellForApkFile=file.getPath();
					if(!file.renameTo(fShellFile))return false;
					shellFile=fShellFile.getPath();//壳文件；
				}else if(filename.contains("shell.apk")){
					hostFile=file.getPath();//宿主文件；
				}else{
					apkFile=file.getPath();//apk文件；
				}
			}
		}
		return true;
	}
	/*
	 * 对apk文件加壳，至dex文件；
	 * apkFile:要加壳的文件；shellFile:壳文件；
	 * @return 加壳成功否？
	 * */
	private static boolean addSellToDex(String apkFile,String shellFile){
		try {
			//File payloadSrcFile = new File("force/ForceApkObj.apk");   //需要加壳的程序
			File payloadSrcFile = new File(apkFile);   //需要加壳的程序
			System.out.println("apk size:"+payloadSrcFile.length());
			File unShellDexFile = new File(shellFile);	//解客dex
			byte[] payloadArray = encrpt(readFileBytes(payloadSrcFile));//以二进制形式读出apk，并进行加密处理//对源Apk进行加密操作
			byte[] unShellDexArray = readFileBytes(unShellDexFile);//以二进制形式读出dex
			int payloadLen = payloadArray.length;
			int unShellDexLen = unShellDexArray.length;
			int totalLen = payloadLen + unShellDexLen +4;//多出4字节是存放长度的。
			byte[] newdex = new byte[totalLen]; // 申请了新的长度
			//添加解壳代码
			System.arraycopy(unShellDexArray, 0, newdex, 0, unShellDexLen);//先拷贝dex内容
			//添加加密后的解壳数据
			System.arraycopy(payloadArray, 0, newdex, unShellDexLen, payloadLen);//再在dex内容后面拷贝apk的内容
			//添加解壳数据长度
			System.arraycopy(intToByte(payloadLen), 0, newdex, totalLen-4, 4);//最后4为长度
            //修改DEX file size文件头
			fixFileSizeHeader(newdex);
			//修改DEX SHA1 文件头
			fixSHA1Header(newdex);
			//修改DEX CheckSum文件头
			fixCheckSumHeader(newdex);

			//String str = "force/classes.dex";
			String str =shellForApkFile;
			File file = new File(str);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileOutputStream localFileOutputStream = new FileOutputStream(str);
			localFileOutputStream.write(newdex);
			localFileOutputStream.flush();
			localFileOutputStream.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
