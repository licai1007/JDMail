package com.m520it.jdmail.bean;

public class ROrderDetailsProducts {
	private double amount;//商品金额
	private int buyCount;//购买数
	private String piconUrl;//商品图片
	private long pid;//商品id
	private String pname;//商品名称
	private String version;//商品版本
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public String getPiconUrl() {
		return piconUrl;
	}
	public void setPiconUrl(String piconUrl) {
		this.piconUrl = piconUrl;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
