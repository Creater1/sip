package com.sip.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.sip.utils.LogUtils;

/**
 * PcapParser �Ĺ۲���
 * @author johnnie
 *
 */
public class PcapParserObserver implements Observer {

	private List<String[]> datas = new ArrayList<String[]>();
	
	public List<String[]> getDatas() {
		return datas;
	}
	
	public void setDatas(List<String[]> datas) {
		this.datas = datas;
	}
	
	public PcapParserObserver() {}
	
	@Override
	public void update(Observable observable, Object datas) {
		this.datas = (List<String[]>) datas;
	}

}
