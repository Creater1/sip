package com.sip.bean;

import com.sip.utils.DataUtils;

public class PcapDataFrame {

	private byte[] desMac;

	private byte[] srcMac;

	private short frameType;

	public byte[] getDesMac() {
		return desMac;
	}

	public void setDesMac(byte[] desMac) {
		this.desMac = desMac;
	}

	public byte[] getSrcMac() {
		return srcMac;
	}

	public void setSrcMac(byte[] srcMac) {
		this.srcMac = srcMac;
	}

	public short getFrameType() {
		return frameType;
	}

	public void setFrameType(short frameType) {
		this.frameType = frameType;
	}
	
	public PcapDataFrame() {}

	@Override
	public String toString() {
		return "PcapDataFrame [frameType=" + DataUtils.shortToHexString(frameType) + "]";
	}
	
}
