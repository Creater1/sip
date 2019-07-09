package com.sip.bean;

/**
 * Э������
 * @author johnnie
 *
 */
public enum ProtocolType {
	
	OTHER("0"), 				// ����Э��ţ�Ĭ��Ϊ0
	TCP("6"), 					// TCP Э��ţ�6
	UDP("17");					// UDP Э��ţ�17
	
	private String type;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	private ProtocolType(String type) {
		this.type = type;
	}
	
}
