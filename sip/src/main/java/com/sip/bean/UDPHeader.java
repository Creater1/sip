package com.sip.bean;

import com.sip.utils.DataUtils;

/**
 * UDP 包头：由4个域组成，每个域各占用2个字节
 * @author johnnie
 *
 */
public class UDPHeader {
	
	private short srcPort;
	private short dstPort;
	private short length;
	private short checkSum;
	
	public short getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(short srcPort) {
		this.srcPort = srcPort;
	}
	public short getDstPort() {
		return dstPort;
	}
	public void setDstPort(short dstPort) {
		this.dstPort = dstPort;
	}
	public short getLength() {
		return length;
	}
	public void setLength(short length) {
		this.length = length;
	}
	public short getCheckSum() {
		return checkSum;
	}
	public void setCheckSum(short checkSum) {
		this.checkSum = checkSum;
	}
	
	public UDPHeader() {}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "UDPHeader [srcPort=" + srcPort
				+ ", dstPort=" + dstPort
				+ ", length=" + length
				+ ", checkSum=" + DataUtils.shortToHexString(checkSum)
				+ "]";
	}
	
}
