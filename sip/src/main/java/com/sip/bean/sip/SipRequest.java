package com.sip.bean.sip;

/**
 * TO, FROM, Cseq, Call-ID, Max-Forwards, Via, contact, userAgent, contentLength 9
 */
public class SipRequest extends BaseSipHeader {

    private String recordRout;

    private String maxForward;

    private String allow;

    private String contentType;

    private String supported;

    public String getRecordRout() {
        return recordRout;
    }

    public void setRecordRout(String recordRout) {
        this.recordRout = recordRout;
    }

    public String getMaxForward() {
        return maxForward;
    }

    public void setMaxForward(String maxForward) {
        this.maxForward = maxForward;
    }

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSupported() {
        return supported;
    }

    public void setSupported(String supported) {
        this.supported = supported;
    }
}
