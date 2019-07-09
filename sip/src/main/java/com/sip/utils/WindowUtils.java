package com.sip.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * ���幤����
 * @author johnnie
 * @time 2015��12��13
 */
public class WindowUtils {

	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private WindowUtils () {}
	
	/**
	 * ��ȡ�û�������Ļ���
	 * @return
	 */
	public static int getScreenWidth () {
		return screenSize.width;
	}

	/**
	 * ��ȡ�û�������Ļ�߶�
	 * @return
	 */
	public static int getScreenHeight () {
		return screenSize.height;
	}
	
}
