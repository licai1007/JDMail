package com.m520it.jdmail.bean;

public class Cart {
	private long id;
	private long productId;
	private int buyCount;
	private String pversion;
	public Cart() {
		super();
	}
	public Cart(long id, long productId, int buyCount, String pversion) {
		super();
		this.id = id;
		this.productId = productId;
		this.buyCount = buyCount;
		this.pversion = pversion;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public String getPversion() {
		return pversion;
	}
	public void setPversion(String pversion) {
		this.pversion = pversion;
	}
	
}
