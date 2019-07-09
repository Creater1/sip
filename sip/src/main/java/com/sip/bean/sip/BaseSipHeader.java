package com.sip.bean.sip;

/**
 * TO, FROM, Cseq, Call-ID, Max-Forwards, Via, contact, userAgent, contentLength
 */
public class BaseSipHeader {

    private String messageHeader;

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }
}
