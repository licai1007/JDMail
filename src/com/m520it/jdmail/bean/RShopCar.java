package com.m520it.jdmail.bean;

import java.io.Serializable;

public class RShopCar implements Serializable{
	private long id;//购物车明细id
	private long pid;//商品id
	private String pname;//商品名称
	private String pimageUrl;//商品图片路径
	private double pprice;//价格
	private int buyCount;//购买数
	private String pversion;//商品版本
	private int stockCount;//库存
	
	private long storeId;//商店id
	private String storeName;//商店名称
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getPimageUrl() {
		return pimageUrl;
	}
	public void setPimageUrl(String pimageUrl) {
		this.pimageUrl = pimageUrl;
	}
	public double getPprice() {
		return pprice;
	}
	public void setPprice(double pprice) {
		this.pprice = pprice;
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
	public int getStockCount() {
		return stockCount;
	}
	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
}
