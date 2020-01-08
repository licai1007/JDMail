package com.m520it.jdmail.bean;

import java.io.Serializable;

public class ROrderParam implements Serializable{
	private long oid;//订单id(表id)
	private String orderNum;//订单编号
	private double allPrice;//商品总价格
	private double freight;//运费
	private double totalPrice;//应付总金额
	private String buyTime;//订单创建的时间
	private int errorType;//0-成功 1-产品没库存了 2-系统失败
	private String pname;//京东商品  不是购买的商品  用来标示某一个平台下的商品
	private int payWay;//付款方式(0在线支付 1货到付款)
	private String tn;//订单令牌
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
	public double getAllPrice() {
		return allPrice;
	}
	public void setAllPrice(double allPrice) {
		this.allPrice = allPrice;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public int getErrorType() {
		return errorType;
	}
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getPayWay() {
		return payWay;
	}
	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	
}
