package com.sip.serviceImp;

import com.sip.service.PcapParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ParseFileImp {

    public static int setRet(String filePath) {
        int ctx = 0;
        InputStream in = null;
        try{
            byte[] tempbytes = new byte[2048];
            in = new FileInputStream(filePath);
            int m = in.read(tempbytes);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数


            int byte_len = tempbytes.length;
            while (m>0) {

            }

        }catch (Exception e){

        }

        return 0;
    }

}
