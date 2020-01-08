package com.m520it.jdmail.bean;

public class SAddOrderProductParam {
	private long pid;//商品id
	private int buyCount;//购买数量
	private String type;//商品版本
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
