package com.sip.bean;

public class ProtocolData {

	String srcIP;
	String desIP;
	
	String srcPort;
	String desPort;
	
	ProtocolType protocolType = ProtocolType.OTHER;

	public String getSrcIP() {
		return srcIP;
	}

	public void setSrcIP(String srcIP) {
		this.srcIP = srcIP;
	}

	public String getDesIP() {
		return desIP;
	}

	public void setDesIP(String desIP) {
		this.desIP = desIP;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getDesPort() {
		return desPort;
	}

	public void setDesPort(String desPort) {
		this.desPort = desPort;
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public ProtocolData() {
		// TODO Auto-generated constructor stub
	}

	public ProtocolData(String srcIP, String desIP, String srcPort,
			String desPort, ProtocolType protocolType) {
		this.srcIP = srcIP;
		this.desIP = desIP;
		this.srcPort = srcPort;
		this.desPort = desPort;
		this.protocolType = protocolType;
	}

	@Override
	public String toString() {
		return "ProtocolData [srcIP=" + srcIP
				+ ", desIP=" + desIP
				+ ", srcPort=" + srcPort
				+ ", desPort=" + desPort
				+ ", protocolType=" + protocolType
				+ "]";
	}

}
