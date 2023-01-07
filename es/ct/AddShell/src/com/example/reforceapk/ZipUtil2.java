/**
 * 
 */
package com.example.reforceapk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * @author ASUS
 *
 */
public class ZipUtil2 {
	/*
	 * ʵ�ִ�zip��ɾ��ָ���ļ�(����λ��)
	 * */
	public static boolean delFileFromZip(String zipFile, String delfile) {//throws ZipException
		try{
		ZipFile zip = new ZipFile(zipFile);
		zip.setFileNameCharset("GBK");
		checkzipFile(zip);
		// ��ȡ�ļ�ͷ
		List<FileHeader> list = zip.getFileHeaders();
		String dname =null;
		boolean flag = true;
		     // �������forEach����ֲ����쳣,����for���Խ����
		     // ����������λ�������©head
		for (int i = 0; i < list.size(); i++) {
			dname = list.get(i).getFileName();
		         // ��ƥ���ʱ��������������endwith
			if (dname.endsWith(delfile)) {
				zip.removeFile(dname);
				--i;// ���������λ����
				flag = false;
				System.out.println(dname + "�ļ�ɾ���ɹ���");
			}
		}
		if (flag) {
			System.out.println("û���ҵ���Ҫɾ�����ļ���");
		}
		return true;
		}catch(ZipException e){
			e.printStackTrace();
		}
		return false;
	}
	/*
	 * ʵ������ļ���zip
	 * */
	public static boolean addFileToZip(String zipFilename, String afilename){
		try{
			ZipFile zipFile = new ZipFile(zipFilename);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			File aFile=new File(afilename);
			parameters.setFileNameInZip(aFile.getName());
			parameters.setSourceExternalStream(true);
			InputStream is = new FileInputStream(afilename);
			zipFile.addStream(is, parameters);
			is.close();//SecurityException IOException  throws ZipException 
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ZipException e){
			e.printStackTrace();
		}
		return false;
	}
	 private static void checkzipFile(ZipFile zip) throws ZipException {
		 if (!zip.isValidZipFile()) {
			 throw new ZipException("���ļ������ڻ򲻺Ϸ�!");
		 }
		 if (zip.isSplitArchive()) {
			 throw new ZipException("���ļ�Ϊ�־��ļ�,����ɾ��!");
		 }
	 }
}
