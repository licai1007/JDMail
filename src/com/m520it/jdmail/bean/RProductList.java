package com.m520it.jdmail.bean;

public class RProductList {
	private long id;//商品id
	private double price;//商品价格
	private String name;//商品名称
	private String iconUrl;//商品图片
	private int commentCount;//评论数
	private int favcomRate;//好评率
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getFavcomRate() {
		return favcomRate;
	}
	public void setFavcomRate(int favcomRate) {
		this.favcomRate = favcomRate;
	}
}
