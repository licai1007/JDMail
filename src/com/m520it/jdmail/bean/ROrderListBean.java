package com.m520it.jdmail.bean;

import java.io.Serializable;


public class ROrderListBean implements Serializable{
	private long oid;         //订单id
	private String orderNum;  //订单号
	private String items;     //商品图片
	private boolean paid;     //是否支付
	private int status;       //订单状态
	private String tn;        //订单令牌
	private double totalPrice;//订单总金额
	private String pname;//京东商品  不是购买的商品  用来标示某一个平台下的商品
	private String buyTime;//订单创建的时间
	public long getOid() {
		return oid;
	}
	public void setOid(long oid) {
		this.oid = oid;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	
}
