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
	private static final String WORK_DIR="force";//����Ŀ¼��
	private static String shellFile=CLASSES_DEX;//���ļ���
	private static String shellForApkFile=CLASSES_DEX;//�Ѽӿǵ�classes.dex�ļ���
	private static String apkFile="";//Ҫ�ӿǵ�APK�ļ���
	private static String hostFile="";//����APK�ļ���
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(!SearchFile()){System.out.println("�����ļ�ʧ�ܣ�");return;}//�����ļ���
		System.out.println("");
		System.out.println("�����ļ��ɹ���");
		if(!addSellToDex(apkFile,shellFile)){System.out.println("�ӿ�ʧ�ܣ�");return;};//�ӿǣ�
		System.out.println("");
		System.out.println("�ӿǳɹ���");
		if(!ZipUtil2.delFileFromZip(hostFile, CLASSES_DEX)){System.out.println("ɾ�������ļ��е�classes.dexʧ�ܣ�");return;};//ɾ�������ļ��е�classes.dex
		System.out.println("");
		System.out.println("ɾ�������ļ��е�classes.dex�ɹ���");
		if(!ZipUtil2.addFileToZip(hostFile,shellForApkFile)){System.out.println("�ӿǺ�д�������ļ�ʧ�ܣ�");return;};//�ӿǺ�д�������ļ���;
		System.out.println("");
		System.out.println("�ӿǺ�д�������ļ��ɹ���");
	
	}
	
	//ֱ�ӷ������ݣ����߿�������Լ����ܷ���
	private static byte[] encrpt(byte[] srcdata){
		for(int i = 0;i<srcdata.length;i++){
			srcdata[i] = (byte)(0xFE ^ srcdata[i]);
		}
		return srcdata;
	}

	/**
	 * �޸�dexͷ��CheckSum У����
	 * @param dexBytes
	 */
	private static void fixCheckSumHeader(byte[] dexBytes) {
		Adler32 adler = new Adler32();
		adler.update(dexBytes, 12, dexBytes.length - 12);//��12���ļ�ĩβ����У����
		long value = adler.getValue();
		int va = (int) value;
		byte[] newcs = intToByte(va);
		//��λ��ǰ����λ��ǰ������
		byte[] recs = new byte[4];
		for (int i = 0; i < 4; i++) {
			recs[i] = newcs[newcs.length - 1 - i];
			System.out.println(Integer.toHexString(newcs[i]));
		}
		System.arraycopy(recs, 0, dexBytes, 8, 4);//Ч���븳ֵ��8-11��
		System.out.println(Long.toHexString(value));
		System.out.println();
	}


	/**
	 * int תbyte[]
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
	 * �޸�dexͷ sha1ֵ
	 * @param dexBytes
	 * @throws NoSuchAlgorithmException
	 */
	private static void fixSHA1Header(byte[] dexBytes)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(dexBytes, 32, dexBytes.length - 32);//��32Ϊ����������sha--1
		byte[] newdt = md.digest();
		System.arraycopy(newdt, 0, dexBytes, 12, 20);//�޸�sha-1ֵ��12-31��
		//���sha-1ֵ�����п���
		String hexstr = "";
		for (int i = 0; i < newdt.length; i++) {
			hexstr += Integer.toString((newdt[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		System.out.println(hexstr);
	}

	/**
	 * �޸�dexͷ file_sizeֵ
	 * @param dexBytes
	 */
	private static void fixFileSizeHeader(byte[] dexBytes) {
		//���ļ�����
		byte[] newfs = intToByte(dexBytes.length);
		System.out.println(Integer.toHexString(dexBytes.length));
		byte[] refs = new byte[4];
		//��λ��ǰ����λ��ǰ������
		for (int i = 0; i < 4; i++) {
			refs[i] = newfs[newfs.length - 1 - i];
			System.out.println(Integer.toHexString(newfs[i]));
		}
		System.arraycopy(refs, 0, dexBytes, 32, 4);//�޸ģ�32-35��
	}


	/**
	 * �Զ����ƶ����ļ�����
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
	 * �����ļ�Ϊ�����ļ���shellFile,Ҫ�ӿǵ�apk�ļ�:apkFile,�����ļ���hostFile
	 * �ҵ����ļ�shellFile,����������Ϊ��classes_0.dex;�ҵ���apkFile,hostFile�ļ���
	 * @return �ҵ��ļ���
	 * */
	private static boolean SearchFile(){
		File dir = new File(WORK_DIR);
		File fShellFile=new File(WORK_DIR+File.separator+CLASSES_DEX_0);//�����������ļ���
		if(!dir.exists())return false;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String filename=file.getName();
				if(filename.equals(CLASSES_DEX)){//�����������ļ���
					shellForApkFile=file.getPath();
					if(!file.renameTo(fShellFile))return false;
					shellFile=fShellFile.getPath();//���ļ���
				}else if(filename.contains("shell.apk")){
					hostFile=file.getPath();//�����ļ���
				}else{
					apkFile=file.getPath();//apk�ļ���
				}
			}
		}
		return true;
	}
	/*
	 * ��apk�ļ��ӿǣ���dex�ļ���
	 * apkFile:Ҫ�ӿǵ��ļ���shellFile:���ļ���
	 * @return �ӿǳɹ���
	 * */
	private static boolean addSellToDex(String apkFile,String shellFile){
		try {
			//File payloadSrcFile = new File("force/ForceApkObj.apk");   //��Ҫ�ӿǵĳ���
			File payloadSrcFile = new File(apkFile);   //��Ҫ�ӿǵĳ���
			System.out.println("apk size:"+payloadSrcFile.length());
			File unShellDexFile = new File(shellFile);	//���dex
			byte[] payloadArray = encrpt(readFileBytes(payloadSrcFile));//�Զ�������ʽ����apk�������м��ܴ���//��ԴApk���м��ܲ���
			byte[] unShellDexArray = readFileBytes(unShellDexFile);//�Զ�������ʽ����dex
			int payloadLen = payloadArray.length;
			int unShellDexLen = unShellDexArray.length;
			int totalLen = payloadLen + unShellDexLen +4;//���4�ֽ��Ǵ�ų��ȵġ�
			byte[] newdex = new byte[totalLen]; // �������µĳ���
			//��ӽ�Ǵ���
			System.arraycopy(unShellDexArray, 0, newdex, 0, unShellDexLen);//�ȿ���dex����
			//��Ӽ��ܺ�Ľ������
			System.arraycopy(payloadArray, 0, newdex, unShellDexLen, payloadLen);//����dex���ݺ��濽��apk������
			//��ӽ�����ݳ���
			System.arraycopy(intToByte(payloadLen), 0, newdex, totalLen-4, 4);//���4Ϊ����
            //�޸�DEX file size�ļ�ͷ
			fixFileSizeHeader(newdex);
			//�޸�DEX SHA1 �ļ�ͷ
			fixSHA1Header(newdex);
			//�޸�DEX CheckSum�ļ�ͷ
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
