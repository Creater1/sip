package com.sip.bean;

import com.sip.service.PcapParser;

import java.io.File;

public class main {
    public static void main(String []args){
        String path = "/Volumes/OSXRESERVED/project/sip/src/main/resources/sip.pcap";
        File file = new File(path);
        PcapParser pcapParser = new PcapParser(file);
        pcapParser.parse();

//        int c = (int) setRet(,pcap);
    }
}
