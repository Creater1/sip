package com.sip.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.sip.bean.*;
import com.sip.bean.sip.SipRequest;
import com.sip.utils.DataUtils;
import com.sip.utils.FileUtils;
import com.sip.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 解析文件
 *
 */
public class PcapParser extends Observable {

	private File pcap;
	private String savePath;

	private PcapStruct struct;
	private ProtocolData protocolData;
	private IPHeader ipHeader;
	private TCPHeader tcpHeader;
	private UDPHeader udpHeader;
	
	private List<String[]> datas = new ArrayList<String[]>();
	private List<String> filenames = new ArrayList<String>();
	
	private byte[] file_header = new byte[24];
	private byte[] data_header = new byte[16];
	private byte[] content;

	private int data_offset = 0;			// 偏移
	private byte[] data_content;			// 存取数据
	
	public PcapParser (File pcap) {
		this.pcap = pcap;
//		this.savePath = outDir.getAbsolutePath();
	}

	public boolean parse () {
		boolean rs = true;
		struct = new PcapStruct();
		List<PcapDataHeader> dataHeaders = new ArrayList<PcapDataHeader>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(pcap);
			//file_header读取文件的24字节数大小
			int m = fis.read(file_header);
			if (m > 0) {
				//解析文件头24个字节
				PcapFileHeader fileHeader = parseFileHeader(file_header);
				
				if (fileHeader == null) {
					LogUtils.printObj("fileHeader", "null");
				}
				struct.setFileHeader(fileHeader);

				while (m > 0) {
					//packagehead 读取16字节的数据头
					m = fis.read(data_header);
					PcapDataHeader dataHeader = parseDataHeader(data_header);
//					System.out.println("长度为： "+dataHeader.getLen());
					dataHeaders.add(dataHeader);

					//读取数据包大小
					content = new byte[dataHeader.getCaplen()];
//					LogUtils.printObj("content.length", content.length);
					m = fis.read(content);

					//ip解析
					protocolData = new ProtocolData();
					boolean isDone = parseContent(dataHeader.getLen());
					if (isDone) {
						break;
					}

//					createFiles(protocolData);

				}

				rs = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtils.closeStream(fis, null);
		}
//		System.out.println(dataHeaders.indexOf(1));
		return rs;
	}

	/**
	 *解析pcaphead文件头，24字节
	 */
	public PcapFileHeader parseFileHeader(byte[] file_header) throws IOException {

		PcapFileHeader fileHeader = new PcapFileHeader();
		byte[] buff_4 = new byte[4];
		byte[] buff_2 = new byte[2];

		int offset = 0;
		for (int i = 0; i < 4; i ++) {
			buff_4[i] = file_header[i + offset];
		}
		offset += 4;
		int magic = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setMagic(magic);

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = file_header[i + offset];
		}
		offset += 2;
		short magorVersion = DataUtils.byteArrayToShort(buff_2);
		fileHeader.setMagorVersion(magorVersion);

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = file_header[i + offset];
		}
		offset += 2;
		short minorVersion = DataUtils.byteArrayToShort(buff_2);
		fileHeader.setMinorVersion(minorVersion);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = file_header[i + offset];
		}
		offset += 4;
		int timezone = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setTimezone(timezone);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = file_header[i + offset];
		}
		offset += 4;
		int sigflags = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setSigflags(sigflags);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = file_header[i + offset];
		}
		offset += 4;
		int snaplen = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setSnaplen(snaplen);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = file_header[i + offset];
		}
		offset += 4;
		int linktype = DataUtils.byteArrayToInt(buff_4);
		fileHeader.setLinktype(linktype);

		return fileHeader;
	}


	/**
	 * 解析packet header 16字节
	 * @param data_header
	 * @return
	 */
	public PcapDataHeader parseDataHeader(byte[] data_header){
		byte[] buff_4 = new byte[4];
		PcapDataHeader dataHeader = new PcapDataHeader();
		int offset = 0;
		for (int i = 0; i < 4; i ++) {
			buff_4[i] = data_header[i + offset];
		}
		offset += 4;
		int timeS = DataUtils.byteArrayToInt(buff_4);
		dataHeader.setTimeS(timeS);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = data_header[i + offset];
		}
		offset += 4;
		int timeMs = DataUtils.byteArrayToInt(buff_4);
		dataHeader.setTimeMs(timeMs);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = data_header[i + offset];
		}
		offset += 4;

		DataUtils.reverseByteArray(buff_4);
		int caplen = DataUtils.byteArrayToInt(buff_4);
		dataHeader.setCaplen(caplen);


		for (int i = 0; i < 4; i ++) {
			buff_4[i] = data_header[i + offset];
		}
		offset += 4;
		//		int len = DataUtils.byteArrayToInt(buff_4);
		DataUtils.reverseByteArray(buff_4);
		int len = DataUtils.byteArrayToInt(buff_4);
		dataHeader.setLen(len);

//		LogUtils.printObjInfo(dataHeader);

		return dataHeader;
	}

	/**
	 * 解析
	 */
	private boolean parseContent(int dataHeaderLen) {
		// 1.
		readPcapDataFrame(content);
		// 2. IP
		ipHeader = readIPHeader(content);
		if (ipHeader == null) {							// ip头为空
			return true;
		}

		int offset = 14;							// packet包头的长度
		offset += 20;

		// 3.解析ip头，有tcp和ip两种分类
		String protocol = ipHeader.getProtocol() + "";
		if (ProtocolType.TCP.getType().equals(protocol)) {
			protocolData.setProtocolType(ProtocolType.TCP);
			tcpHeader = readTCPHeader(content, offset);
		} else if (ProtocolType.UDP.getType().equals(protocol)) {
			protocolData.setProtocolType(ProtocolType.UDP);
			udpHeader = readUDPHeader(content, offset);
//			System.out.println(udpHeader.getSrcPort());
			/***********************************************************************************
			解析sip
			 */
			parseSipHeader(content, dataHeaderLen);
		}

		return false;
	}

	/**
	 * 截取byte
	 * @return
	 */
	private String[] strLen(byte[] data, int offset){
//		int sipLen = P;
		int srcPos = offset - 42;
		byte[] desData = new byte[srcPos];
		System.arraycopy(data, 42, desData, 0, srcPos);
		System.out.println(DataUtils.byteArrayToStr(desData));
		String tmp = new String(desData);
		String[] ary = tmp.split("\n");
//		StringBuilder stringBuilder = new StringBuilder();

//		System.out.println(ary);
		return ary;
	}

	public String[] parseSipHeader(byte[] data, int dataHeaderLen){
		String[] ary = strLen(data, dataHeaderLen);
		return ary;
	}


    private UDPHeader readUDPHeader(byte[] content, int offset) {
        byte[] buff_2 = new byte[2];

        UDPHeader udp = new UDPHeader();
        for (int i = 0; i < 2; i ++) {
            buff_2[i] = content[i + offset];
//			LogUtils.printByteToBinaryStr("UDP: buff_2[" + i + "]", buff_2[i]);
        }
        offset += 2;									// offset = 36
        short srcPort = DataUtils.byteArrayToShort(buff_2);
        udp.setSrcPort(srcPort);

        String sourcePort = validateData(srcPort);
        protocolData.setSrcPort(sourcePort);

        for (int i = 0; i < 2; i ++) {
            buff_2[i] = content[i + offset];
        }
        offset += 2;									// offset = 38
        short dstPort = DataUtils.byteArrayToShort(buff_2);
        udp.setDstPort(dstPort);

        String desPort = validateData(dstPort);
        protocolData.setDesPort(desPort);

        for (int i = 0; i < 2; i ++) {
            buff_2[i] = content[i + offset];
        }
        offset += 2;									// offset = 40
        short length = DataUtils.byteArrayToShort(buff_2);
        udp.setLength(length);

        for (int i = 0; i < 2; i ++) {
            buff_2[i] = content[i + offset];
        }
        offset += 2;									// offset = 42
        short checkSum = DataUtils.byteArrayToShort(buff_2);
        udp.setCheckSum(checkSum);

        data_offset = offset;

        return udp;
    }

	private TCPHeader readTCPHeader(byte[] content2, int offset) {
		byte[] buff_2 = new byte[2];
		byte[] buff_4 = new byte[4];

		TCPHeader tcp = new TCPHeader();

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = content[i + offset];
//			LogUtils.printByteToBinaryStr("TCP: buff_2[" + i + "]", buff_2[i]);
		}
		offset += 2;									// offset = 36
		short srcPort = DataUtils.byteArrayToShort(buff_2);
		tcp.setSrcPort(srcPort);

		String sourcePort = validateData(srcPort);
		protocolData.setSrcPort(sourcePort);

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 38
		short dstPort = DataUtils.byteArrayToShort(buff_2);
		tcp.setDstPort(dstPort);

		String desPort = validateData(dstPort);
		protocolData.setDesPort(desPort);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = content[i + offset];
		}
		offset += 4;									// offset = 42
		int seqNum = DataUtils.byteArrayToInt(buff_4);
		tcp.setSeqNum(seqNum);

		for (int i = 0; i < 4; i ++) {
			buff_4[i] = content[i + offset];
		}
		offset += 4;									// offset = 46
		int ackNum = DataUtils.byteArrayToInt(buff_4);
		tcp.setAckNum(ackNum);

		byte headerLen = content[offset ++];			// offset = 47
		tcp.setHeaderLen(headerLen);

		byte flags = content[offset ++];				// offset = 48
		tcp.setFlags(flags);

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 50
		short window = DataUtils.byteArrayToShort(buff_2);
		tcp.setWindow(window);

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 52
		short checkSum = DataUtils.byteArrayToShort(buff_2);
		tcp.setCheckSum(checkSum);

		for (int i = 0; i < 2; i ++) {
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 54
		short urgentPointer = DataUtils.byteArrayToShort(buff_2);
		tcp.setUrgentPointer(urgentPointer);

//		LogUtils.printObj("tcp.offset", offset);
		data_offset = offset;
//		LogUtils.printObjInfo(tcp);

		return tcp;
	}



	public void readPcapDataFrame(byte[] content) {
		PcapDataFrame dataFrame = new PcapDataFrame();
		int offset = 12;
		byte[] buff_2 = new byte[2];
		for (int i = 0; i < 2; i ++) {
			buff_2[i] = content[i + offset];
		}
		short frameType = DataUtils.byteArrayToShort(buff_2);
		dataFrame.setFrameType(frameType);
//		System.out.println("frameType是"+dataFrame.toString());
//		LogUtils.printObjInfo(dataFrame);
	}

	private IPHeader readIPHeader(byte[] content) {
		int offset = 14;
		IPHeader ip = new IPHeader();

		byte[] buff_2 = new byte[2];
		byte[] buff_4 = new byte[4];

		byte varHLen = content[offset ++];				// offset = 15
//		LogUtils.printByteToBinaryStr("varHLen", varHLen);
		if (varHLen == 0) {
			return null;
		}
		
		ip.setVarHLen(varHLen);

		byte tos = content[offset ++];					// offset = 16
		ip.setTos(tos);

		for (int i = 0; i < 2; i ++) {		
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 18
		short totalLen = DataUtils.byteArrayToShort(buff_2);
		ip.setTotalLen(totalLen);

		for (int i = 0; i < 2; i ++) {			
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 20
		short id = DataUtils.byteArrayToShort(buff_2);
		ip.setId(id);

		for (int i = 0; i < 2; i ++) {					
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 22
		short flagSegment = DataUtils.byteArrayToShort(buff_2);
		ip.setFlagSegment(flagSegment);

		byte ttl = content[offset ++];					// offset = 23
		ip.setTtl(ttl);

		byte protocol = content[offset ++];				// offset = 24
		ip.setProtocol(protocol);

		for (int i = 0; i < 2; i ++) {					
			buff_2[i] = content[i + offset];
		}
		offset += 2;									// offset = 26
		short checkSum = DataUtils.byteArrayToShort(buff_2);
		ip.setCheckSum(checkSum);

		for (int i = 0; i < 4; i ++) {					
			buff_4[i] = content[i + offset];
		}
		offset += 4;									// offset = 30
		int srcIP = DataUtils.byteArrayToInt(buff_4);
		ip.setSrcIP(srcIP);

		// SourceIP
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			builder.append((int) (buff_4[i] & 0xff));
			builder.append(".");
		}
		builder.deleteCharAt(builder.length() - 1);
		String sourceIP = builder.toString();
		protocolData.setSrcIP(sourceIP);

		for (int i = 0; i < 4; i ++) {		
			buff_4[i] = content[i + offset];
		}
		offset += 4;									// offset = 34
		int dstIP = DataUtils.byteArrayToInt(buff_4);
		ip.setDstIP(dstIP);

		// ƴ�ӳ� DestinationIP
		builder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			builder.append((int) (buff_4[i] & 0xff));
			builder.append(".");
		}
		builder.deleteCharAt(builder.length() - 1);
		String destinationIP = builder.toString();
		protocolData.setDesIP(destinationIP);

//		LogUtils.printObjInfo(ip);

		return ip;
	}





	private String validateData (int data) {
		String rs = data + "";
		if (data < 0) {
			String binaryPort = Integer.toBinaryString(data);
			rs = DataUtils.binaryToDecimal(binaryPort) + "";
		}

		return rs;
	}

	/**
	 * 二进制转化为十六进制
	 * @param data
	 */
	public static void byteToHex(byte data){
		System.out.println(Integer.toHexString(data));
	}
	/**
	 * 二进制转化为字符串输出
	 */
	public static String byte2String(byte[] b)
	{
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		for (int i = 0; i < b.length; i++) {
			tmp = Integer.toHexString(b[i] & 0XFF);
			if (tmp.length() == 1){
				sb.append("0" + tmp);
			}else{
				sb.append(tmp);
			}

		}
		return sb.toString();
	}
    /**
     * 字符串输出
     */
    public void printStr(String str){
        System.out.println(str);
    }
}
