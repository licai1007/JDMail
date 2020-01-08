package com.m520it.jdmail.bean;

public class RBrand {
	private long id;//品牌id
	private String name;//品牌名称
	private long cid;//所属1级分类id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	
}
