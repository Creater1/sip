package com.sip.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

	/**
	 * ׷�����ݵ�  properties �ļ���
	 * @param pathname
	 * @param key
	 * @param value
	 * @param apend
	 */
	public static void write (String pathname, String key, String value) {
		write(pathname, key, value, true);
	}
	
	/**
	 * д�����ݵ�  properties �ļ���
	 * @param pathname
	 * @param key
	 * @param value
	 * @param apend   �Ƿ�׷��д��
	 */
	public static void write (String pathname, String key, String value, boolean apend) {
		Properties properties = new Properties();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(pathname), apend);
			properties.setProperty(key, value);
			properties.store(fos, null);
			FileUtils.closeStream(null, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �� properties �ļ��ж�ȡ����
	 * @param pathname
	 * @param key
	 * @return
	 */
	public static String read (String pathname, String key) {
		String value = null;
		Properties properties = new Properties();
		FileInputStream fis = getInStream(pathname);
		try {
			properties.load(fis);
			value = properties.getProperty(key);
			FileUtils.closeStream(fis, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	/**
	 * �ж� properties �ļ����Ƿ����ĳһֵ
	 * @param pathname
	 * @param key
	 * @return
	 */
	public static boolean contains (String pathname, String key) {
		boolean rs = false;
		Properties properties = new Properties();
		FileInputStream fis = getInStream(pathname);
		try {
			properties.load(fis);
			
			if (properties.containsKey(key)) {
				rs = true;
			}
			
			FileUtils.closeStream(fis, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	/**
	 * �ж��ļ������Ƿ�Ϊ��
	 * @param pathname
	 * @return
	 */
	public static boolean isEmpty (String pathname) {
		boolean isEmp = false;
		Properties properties = new Properties();
		FileInputStream fis = getInStream(pathname);
		try {
			properties.load(fis);
			if (properties.isEmpty()) {
				isEmp = true;
			} 
			FileUtils.closeStream(fis, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return isEmp;
	}
	
	/**
	 * ��ȡ����ֵ
	 * @param pathname
	 * @return
	 */
	public static Object[] getVals (String pathname) {
		Object[] values = null;
		Properties properties = new Properties();
		FileInputStream fis = getInStream(pathname);
		try {
			properties.load(fis);
			values = properties.values().toArray();
			FileUtils.closeStream(fis, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return values;
	}
	

	/**
	 * �õ�������
	 * @param pathname
	 * @return
	 */
	private static FileInputStream getInStream(String pathname) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(pathname));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}
	
	/**
	 * �������
	 * @param pathname
	 */
	public static void clear (String pathname) {
		FileUtils.createEmpFile(pathname);
	}
	

}
